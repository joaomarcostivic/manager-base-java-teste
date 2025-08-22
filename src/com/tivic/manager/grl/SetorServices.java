package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.EelUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class SetorServices {

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;

	public static final int ERR_LANCAMENTO_DEBITO = -2;
	public static final int ERR_LANCAMENTO_CREDITO = -3;
	
	public static final int TP_SETOR_INTERNO = 0; //com o sistema
	public static final int TP_SETOR_INSTITUICAO = 1; //sem o sistema
	public static final int TP_SETOR_EXTERNO = 2;

	public static Result save(Setor setor){
		return save(setor, null);
	}
	
	public static Result save(Setor setor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(setor==null)
				return new Result(-1, "Erro ao salvar. Setor é nulo");
			
			int retorno;
			if(setor.getCdSetor()==0){
				retorno = SetorDAO.insert(setor, connect);
				setor.setCdSetor(retorno);
			}
			else {
				retorno = SetorDAO.update(setor, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SETOR", setor);
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
	
	public static Result remove(int cdSetor){
		return remove(cdSetor, false, null);
	}
	
	public static Result remove(int cdSetor, boolean cascade){
		return remove(cdSetor, cascade, null);
	}
	
	public static Result remove(int cdSetor, boolean cascade, Connection connect){
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
				retorno = SetorDAO.delete(cdSetor, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este setor está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Setor excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir setor!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllHierarquia(int cdEmpresa) {
		return getAllHierarquia(cdEmpresa, null);
	}

	public static ResultSetMap getAllHierarquia(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if (cdEmpresa > 0)
				criterios.add(new ItemComparator("CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = SetorDAO.find(criterios, connect);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("CD_SETOR_SUPERIOR") != 0) {
					int pointer = rsm.getPointer();
					int cdSetor = rsm.getInt("CD_SETOR_SUPERIOR");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("CD_SETOR", new Integer(rsm.getInt("CD_SETOR_SUPERIOR")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("CD_SETOR")==cdSetor;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("CD_SETOR")==cdSetor;
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
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFuncionarios(int cdSetor) {
		return getAllFuncionarios(cdSetor, null);
	}
	
	public static ResultSetMap getAllFuncionarios(int cdSetor, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM srh_dados_funcionais A, grl_pessoa B " +
															 "WHERE A.cd_pessoa   = B.cd_pessoa " +
															 "  AND A.cd_setor  = "+cdSetor).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Setor get(int cdSetor) {
		return get(cdSetor, null);
	}
	
	public static Setor get(int cdSetor, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {			
			connect = connect!=null ? connect : Conexao.conectar();
			Setor setor = SetorDAO.get(cdSetor, connect);
			return setor;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	} 
	
	public static ResultSetMap getAll(int cdEmpresa, boolean lgOrdenarBySetorExterno) {
		ResultSetMap rsm = getAll(cdEmpresa);
		
		ArrayList<String> ordem = new ArrayList<String>();
		ordem.add("TP_SETOR");
		ordem.add("NM_SETOR");
		rsm.orderBy(ordem);
		rsm.beforeFirst();
		
		return rsm;
	} 
	
	public static ResultSetMap getAll() {
		return getAll(-1, null);
	}
	
	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}
	
	public static ResultSetMap getAllSMED() {
		int cdEmpresa = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
		return getAll(cdEmpresa, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if(cdEmpresa>-1)
			criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return findCompleto(criterios, connect);
	}
	
	public static ResultSetMap findSMED(ArrayList<ItemComparator> criterios) {
		int cdEmpresa = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
		criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return findCompleto(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return findCompleto(criterios, connect);
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		
		try {
			String nmSetor = "";
			int cdEmpresa = -1;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			
//			boolean isConnectionNull = connect==null;
//			connect = connect!=null ? connect : Conexao.conectar();
			
			for(int i=0; i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().equalsIgnoreCase("nm_setor"))	{
					nmSetor = criterios.get(i).getValue();
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("A.cd_empresa"))	{
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				}
				else
					crt.add(criterios.get(i));
			}
						
			String sql = "SELECT A.*, B.nm_pessoa, " +
			  "   B.nm_pessoa AS nm_responsavel, " +
			  "	  C.nm_setor AS nm_setor_superior, " +
			  "   D.nm_razao_social, E.nm_pessoa as nm_fantasia"+
		      " FROM grl_setor A " +
		      "   LEFT OUTER JOIN grl_pessoa B ON (A.cd_responsavel = B.cd_pessoa) " +
		      "	  LEFT OUTER JOIN grl_setor C ON (A.cd_setor_superior = C.cd_setor) " +
		      "   LEFT OUTER JOIN grl_pessoa_juridica D ON (A.cd_empresa = D.cd_pessoa) "+
		      "	  LEFT OUTER JOIN grl_pessoa E ON (A.cd_empresa = E.cd_pessoa)"+
		      "WHERE (1=1) " +
		      ((nmSetor.equals("")) ? "" :  " AND (A.nm_setor LIKE '%"+nmSetor+"%' OR A.sg_setor LIKE '%"+nmSetor+"%') ") + 
		      (cdEmpresa > 0 ? " AND A.cd_empresa = " + cdEmpresa : "");
			
			ResultSetMap rsm = Search.find(sql, "ORDER BY E.nm_pessoa, A.tp_setor, A.nm_setor", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0,cdEmpresa, connect);
			if(lgProtocoloEel>0) {
				while(rsm.next()) {
					String nrSetorExterno = ((lgProtocoloEel==1 && rsm.getString("NR_SETOR_EXTERNO")!=null) ? 
							Util.removeZeroEsquerda(rsm.getString("NR_SETOR_EXTERNO"))+" - " : "");
					rsm.setValueToField("DS_SETOR_NR_SETOR_EXTERNO", nrSetorExterno + rsm.getString("NM_SETOR"));
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.findCompleto: " + e);
			return null;
		}
	}
	
	public static Setor getByName(String nmSetor) {
		return getByName(nmSetor, null);
	}

	public static Setor getByName(String nmSetor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_setor WHERE nm_setor=?");
			pstmt.setString(1, nmSetor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return SetorDAO.get(rs.getInt("cd_setor"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.getByName: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SetorDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Lista os setores marcados como 'RECEPÇÃO' de determinada empresa
	 * 
	 * @param cdEmpresa
	 * @return
	 */
	public static ResultSetMap getSetorRecepcao(int cdEmpresa) {
		return getSetorRecepcao(cdEmpresa, null);
	}
	
	public static ResultSetMap getSetorRecepcao(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_pessoa as nm_fantasia"
					+ " FROM grl_setor A"
					+ " JOIN grl_pessoa B ON (A.cd_empresa = B.cd_pessoa)"
					+ " WHERE A.cd_empresa = ?"
					+ " AND A.lg_recepcao = 1"
			);
			pstmt.setInt(1, cdEmpresa);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {
				rsm.setValueToField("NM_SETOR", "["+rsm.getString("NM_FANTASIA")+"] "+rsm.getString("NM_SETOR"));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getSetorRecepcao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getSetorRecepcao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSetoresTramitacao(int cdEmpresa) {
		return getSetoresTramitacao(cdEmpresa, null);
	}
	
	public static ResultSetMap getSetoresTramitacao(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int lgProtocoloEel = ParametroServices.getValorOfParametroAsInteger("LG_PROTOCOLO_EEL", 0,cdEmpresa, connect);
			int lgExibirInativos = ParametroServices.getValorOfParametroAsInteger("LG_EXIBIR_SETOR_INATIVO", 0,cdEmpresa, connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(cdEmpresa>-1)
				criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			if(lgExibirInativos==0)
				criterios.add(new ItemComparator("A.ST_SETOR", Integer.toString(ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = findCompleto(criterios, connect);
			while(rsm.next()) {
				String nrSetorExterno = (lgProtocoloEel==1 && rsm.getString("NR_SETOR_EXTERNO")!=null ? Util.removeZeroEsquerda(rsm.getString("NR_SETOR_EXTERNO"))+" - " : "");
				rsm.setValueToField("DS_SETOR_NR_SETOR_EXTERNO", nrSetorExterno + rsm.getString("NM_SETOR"));
			}
			rsm.beforeFirst();
			
			
			if(cdEmpresa>0) {
				if(connect==null)
					connect = Conexao.conectar();
				
				ResultSetMap rsmEmpresas = new ResultSetMap(connect.prepareStatement(
						"SELECT A.cd_empresa, B.nm_pessoa "
						+ " FROM grl_empresa A "
						+ " LEFT OUTER JOIN grl_pessoa B ON (A.cd_empresa = B.cd_pessoa)"
						+ " WHERE cd_empresa <> "+cdEmpresa).executeQuery());
				while(rsmEmpresas.next()) {
					cdEmpresa = rsmEmpresas.getInt("CD_EMPRESA");
					ResultSetMap rsmSetorRec = getSetorRecepcao(rsmEmpresas.getInt("CD_EMPRESA"), connect);
//					while(rsmSetorRec.next()) {
//						String nrSetorExterno = ((lgProtocoloEel==1 && rsmSetorRec.getString("NR_SETOR_EXTERNO")!=null) ? 
//								Util.removeZeroEsquerda(rsmSetorRec.getString("NR_SETOR_EXTERNO"))+" - " : "");
//						rsmSetorRec.setValueToField("DS_SETOR_NR_SETOR_EXTERNO", nrSetorExterno + rsmSetorRec.getString("NM_SETOR"));
//						rsm.addRegister(rsmSetorRec.getRegister());
//					}
				}
				rsm.beforeFirst();
				
				//ordenando rsm
				ArrayList<String> columns = new ArrayList<String>();
				columns.add("nm_fantasia");
				columns.add("nm_setor");
				columns.add("lg_recepcao");
				rsm.orderBy(columns);
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getSetorRecepcao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getSetorRecepcao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllSetorEel() {
		return getAllSetorEel(null);
	}
	
	public static ResultSetMap getAllSetorEel(Connection connect) {
		return EelUtils.getAllSetores();
	}
	
	public static String getListSetoresEel() {
		return getListSetoresEel(null);
	}
	
	public static String getListSetoresEel(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT nr_setor_externo FROM grl_setor "
					+ " WHERE nr_setor_externo IS NOT NULL"
					+ " AND tp_setor="+TP_SETOR_INTERNO);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			StringBuilder sb = new StringBuilder();
			
			while(rsm.next()) {
				sb.append("'");
				sb.append(rsm.getString("nr_setor_externo"));
				sb.append("'");
				
				if(rsm.hasMore()) {
					sb.append(",");
				}
			}
			
			return sb.toString();
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getListSetoresEel: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getListSetoresEel: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getSetorBySetorEel(String cdLocal) {
		return getSetorBySetorEel(cdLocal, null);
	}
	
	public static int getSetorBySetorEel(String cdLocal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int cdSetor = 0;
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_setor FROM grl_setor "
					+ " WHERE nr_setor_externo ='"+cdLocal+"'");
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cdSetor = rs.getInt("cd_setor");
			}
			
			return cdSetor;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getSetorBySetorEel: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.getSetorBySetorEel: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setParidadeEel() {
		return setParidadeEel(null);
	}
	
	public static Result setParidadeEel(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			PreparedStatement pstmt;
			ResultSetMap rsm = EelUtils.getSetoresUsados();
			
			while(rsm.next()) {
				pstmt = connect.prepareStatement("SELECT * FROM grl_setor"
												+ " WHERE nm_setor LIKE '"+rsm.getString("nm_setor_externo")+"'");
			}
			
			return new Result(1);
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.setParidadeEel: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! SetorServices.setParidadeEel: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}