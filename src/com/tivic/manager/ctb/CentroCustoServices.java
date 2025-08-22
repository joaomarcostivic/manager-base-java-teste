package com.tivic.manager.ctb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CentroCustoServices {
	
	
	public static Result save(CentroCusto centroCusto){
		return save(centroCusto, null);
	}

	public static Result save(CentroCusto centroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(centroCusto==null)
				return new Result(-1, "Erro ao salvar. CentroCusto é nulo");

			int retorno;
			if(centroCusto.getCdCentroCusto()==0){
				retorno = CentroCustoDAO.insert(centroCusto, connect);
				centroCusto.setCdCentroCusto(retorno);
			}
			else {
				retorno = CentroCustoDAO.update(centroCusto, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CENTROCUSTO", centroCusto);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdCentroCusto){
		return remove(cdCentroCusto, false, null);
	}
	public static Result remove(int cdCentroCusto, boolean cascade){
		return remove(cdCentroCusto, cascade, null);
	}
	public static Result remove(int cdCentroCusto, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = CentroCustoDAO.delete(cdCentroCusto, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllHierarquia(int cdPlanoCentroCusto ) {
		return getAllHierarquia(cdPlanoCentroCusto, null);
	}

	public static ResultSetMap getAllHierarquia(int cdPlanoCentroCusto, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = Search.find("SELECT * " +
									" FROM ctb_centro_custo  A "+
									" LEFT OUTER JOIN ctb_plano_centro_custo B on ( A.cd_plano_centro_custo = B.cd_plano_centro_custo ) " +
									"WHERE A.cd_plano_centro_custo = "+cdPlanoCentroCusto,
									"ORDER BY A.nr_centro_custo, A.cd_plano_centro_custo, A.cd_centro_custo_superior ",
									null, connect, isConnectionNull);
			
			//Organiza hierarquia de centros de custo
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("CD_CENTRO_CUSTO_SUPERIOR") != 0) {
					int pointer = rsm.getPointer();
					int cdCentroCustoSuperior = rsm.getInt("CD_CENTRO_CUSTO_SUPERIOR");
					HashMap<String,Object> register = rsm.getRegister();
					/*
					 *  Procura o próximo registro que possua a mesma categoria superior
					 */
					if (rsm.locate("CD_CENTRO_CUSTO", new Integer(rsm.getInt("CD_CENTRO_CUSTO_SUPERIOR")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("CD_CENTRO_CUSTO") == cdCentroCustoSuperior;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("CD_CENTRO_CUSTO")==cdCentroCustoSuperior;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoServices.getAllHierarquia: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap get(int cdCentroCusto) {
		return get(cdCentroCusto, null);
	}

	public static ResultSetMap get(int cdCentroCusto, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			return Search.find("SELECT A.*, " +
							   "	B.nm_setor " +
							   "FROM ctb_centro_custo A " +
							   "LEFT OUTER JOIN grl_setor B " +
							   "   ON (A.cd_setor = B.cd_setor) " +
							   "WHERE A.cd_centro_custo = " + cdCentroCusto, null, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			return Search.find("SELECT A.*, " +
							   "	B.nm_setor " +
							   "FROM ctb_centro_custo A " +
							   "LEFT OUTER JOIN grl_setor B " +
							   "   ON (A.cd_setor = B.cd_setor) ORDER BY B.nm_setor, A.nm_centro_custo", null, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllCentroCusto(int cdPlanoCentroCusto) {
		return getAllCentroCusto(cdPlanoCentroCusto, 0, null);
	}

	public static ResultSetMap getAllCentroCusto(int cdPlanoCentroCusto, int cdCentroCusto) {
		return getAllCentroCusto(cdPlanoCentroCusto, cdCentroCusto, null);
	}

	public static ResultSetMap getAllCentroCusto(int cdPlanoCentroCusto, int cdCentroCusto, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if (cdPlanoCentroCusto <= 0) {
				return null;
			}
			criterios.add(new ItemComparator("CD_PLANO_CENTRO_CUSTO", Integer.toString(cdPlanoCentroCusto), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = Search.find("SELECT A.*, B.nm_setor " +
										   "FROM ctb_centro_custo A " +
										   "   LEFT OUTER JOIN grl_setor B " +
										   "      ON (A.cd_setor = B.cd_setor) " +
										   "WHERE " + (cdCentroCusto > 0 ? "A.cd_centro_custo = " + cdCentroCusto + " OR A.cd_centro_custo_superior = " + cdCentroCusto : "(1=1)"), "ORDER BY A.nr_centro_custo", criterios, connect, true);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("CD_CENTRO_CUSTO_SUPERIOR") != 0) {
					int pointer = rsm.getPointer();
					int cdCentroCustoSuperior = rsm.getInt("CD_CENTRO_CUSTO_SUPERIOR");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("CD_CENTRO_CUSTO", new Integer(cdCentroCustoSuperior), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("CD_CENTRO_CUSTO") == cdCentroCustoSuperior;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm != null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("CD_CENTRO_CUSTO")==cdCentroCustoSuperior;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm == null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, A.nr_centro_custo || ' - ' || A.nm_centro_custo AS ds_centro_custo, " +
						   "	B.nr_centro_custo AS nr_centro_custo_superior, B.id_centro_custo AS id_centro_custo_superior, " +
						   "	B.nm_centro_custo AS nm_centro_custo_superior, " +
						   "	C.nm_plano_centro_custo, C.id_plano_centro_custo " +
						   "FROM ctb_plano_centro_custo C, ctb_empresa_exercicio D, ctb_centro_custo A " +
						   "	LEFT OUTER JOIN ctb_centro_custo B ON (A.cd_centro_custo_superior = B.cd_centro_custo) " +
						   "WHERE (A.cd_plano_centro_custo = C.cd_plano_centro_custo) " +
						   "    AND (C.cd_plano_centro_custo = D.cd_plano_centro_custo)", "ORDER BY A.nr_centro_custo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static String gerarNrCentroCusto(int cdCentroCustoSuperior, String idCentroCusto, Connection connect)	{
		try {
			String nrCentroCusto = idCentroCusto;
			ResultSet rs;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT nr_centro_custo, cd_centro_custo_superior " +
					"FROM ctb_centro_custo "+
	                "WHERE cd_centro_custo = ? ");
			if (cdCentroCustoSuperior > 0) {
				pstmt.setInt(1, cdCentroCustoSuperior);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					nrCentroCusto = rs.getString("nr_centro_custo") + "." + idCentroCusto;
				}
			}
			return nrCentroCusto;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoServices.gerarNrCentroCusto: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentroCustoServices.gerarNrCentroCusto: " +  e);
			return null;
		}
	}
	
	public static ResultSetMap findNrCentroCusto(String nrCentroCusto) {
		return findNrCentroCusto(nrCentroCusto, null);
	}

	public static ResultSetMap findNrCentroCusto(String nrCentroCusto, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstm = connect.prepareStatement("SELECT A.* FROM ctb_centro_custo A WHERE A.nr_centro_custo = '"+nrCentroCusto+"'");
			return new ResultSetMap(pstm.executeQuery());
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getCentroCustoOfPessoa(int cdPessoa) {
		return getCentroCustoOfPessoa(cdPessoa, null);
	}
	
	public static ResultSetMap getCentroCustoOfPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_PESSOA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
			return PessoaCentroCustoServices.find(criterios, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraServices.getContasOfUsuario: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios){
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			return CentroCustoDAO.find(criterios, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getMovimentoByCentroCusto(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal ){
		return getMovimentoByCentroCusto(cdEmpresa, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap getMovimentoByCentroCusto(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsmCentroCusto = getAll();
			while( rsmCentroCusto.next() ){
				rsmCentroCusto.setValueToField("VL_DESPESA", 0.0);
				rsmCentroCusto.setValueToField("VL_RECEITA", 0.0);
				
				ResultSetMap rsmReceitas = Search.find(
						" SELECT SUM(VL_CONTA_CATEGORIA) as VL_DESPESA, A.CD_CENTRO_CUSTO, B.* "+
						" FROM ADM_CONTA_PAGAR_CATEGORIA A "+
						" JOIN CTB_CENTRO_CUSTO B ON ( A.CD_CENTRO_CUSTO = B.CD_CENTRO_CUSTO ) "+
						" JOIN ADM_CONTA_PAGAR  C ON ( A.CD_CONTA_PAGAR = C.CD_CONTA_PAGAR "+
						"								AND C.CD_EMPRESA = "+cdEmpresa+
						"								AND (" +
						" 							 C.DT_EMISSAO BETWEEN '"+Util.formatDate(dtInicial, "yyyy-MM-dd")+"'"+
						" 											  AND '"+Util.formatDate(dtFinal, "yyyy-MM-dd")+"' ) ) "+
						" WHERE A.cd_centro_custo = "+rsmCentroCusto.getInt("CD_CENTRO_CUSTO"),
						" GROUP BY A.CD_CENTRO_CUSTO, B.CD_CENTRO_CUSTO ", null, connect, false);
				if( rsmReceitas.next() )
					rsmCentroCusto.setValueToField("VL_DESPESA", rsmReceitas.getDouble("VL_DESPESA"));
				
				ResultSetMap rsmDespesas = Search.find(
						" SELECT SUM(VL_CONTA_CATEGORIA) as VL_RECEITA, A.CD_CENTRO_CUSTO, B.* "+
						" FROM ADM_CONTA_RECEBER_CATEGORIA A "+
						" JOIN CTB_CENTRO_CUSTO B ON ( A.CD_CENTRO_CUSTO = B.CD_CENTRO_CUSTO ) "+
						" JOIN ADM_CONTA_RECEBER  C ON ( A.CD_CONTA_RECEBER = C.CD_CONTA_RECEBER "+
						"								AND C.CD_EMPRESA = "+cdEmpresa+
						"								AND (" +
						" 							 C.DT_EMISSAO BETWEEN '"+Util.formatDate(dtInicial, "yyyy-MM-dd")+"'"+
						" 											  AND '"+Util.formatDate(dtFinal, "yyyy-MM-dd")+"' ) ) "+
						" WHERE A.CD_CENTRO_CUSTO = "+rsmCentroCusto.getInt("CD_CENTRO_CUSTO"),
						" GROUP BY A.CD_CENTRO_CUSTO, B.CD_CENTRO_CUSTO ", null, connect, false);
				if( rsmDespesas.next() )
					rsmCentroCusto.setValueToField("VL_RECEITA", rsmDespesas.getDouble("VL_RECEITA"));

			}
			
			return rsmCentroCusto;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
}
