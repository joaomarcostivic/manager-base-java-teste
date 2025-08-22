package com.tivic.manager.ctb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class ContaPlanoContasServices {

	public static final int TP_SINTETICA = 0;
	public static final int TP_ANALITICA = 1;

	public static final int TP_INDEFINIDA  = 0;
	public static final int TP_DEVEDORA  = 1;
	public static final int TP_CREDORA   = 2;

	public static final String[] tipoConta = {"Sintética", "Analítica"};
	public static final String[] tipoNatureza = {"Devedora", "Credora"};

	public static ResultSetMap get(int cdContaPlanoContas) {
		return get(cdContaPlanoContas, null);
	}

	public static ResultSetMap get(int cdContaPlanoContas, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			return Search.find("SELECT A.*, " +
							   "   B.nm_conta, B.cd_historico_padrao, B.nr_conta_reduzida, B.nr_conta_impressao, " +
							   "   C.nm_historico AS nm_historico_padrao " +
							   "FROM ctb_conta_plano_contas A, ctb_conta B " +
							   "LEFT OUTER JOIN ctb_historico C " +
							   "   ON (B.cd_historico_padrao = C.cd_historico) " +
							   "WHERE (A.cd_conta = B.cd_conta) " + "" +
							   "   AND A.cd_conta_plano_contas = " + cdContaPlanoContas, null, true, connect!=null ? connect : Conexao.conectar(), connect==null);
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

	public static ContaPlanoContas getByCdConta(int cdConta, int cdPlanoContas) {
		return getByCdConta(cdConta, cdPlanoContas, null);
	}

	public static ContaPlanoContas getByCdConta(int cdConta, int cdPlanoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta_plano_contas WHERE cd_conta = ? AND cd_plano_contas = ?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdPlanoContas);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPlanoContas(rs.getInt("cd_conta_plano_contas"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_plano_contas"),
						rs.getInt("cd_conta_superior"),
						rs.getString("nr_conta"),
						rs.getInt("nr_digito"),
						rs.getInt("tp_conta"),
						rs.getInt("tp_natureza"),
						(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
						rs.getString("txt_observacao"),
						rs.getString("nr_conta_externa"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getFloat("pr_depreciacao"),
						rs.getInt("lg_orcamentaria"),
						rs.getString("id_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasServices.getByCdConta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasServices.getByCdConta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllHierarquia(int cdPlanoContas) {
		return getAllHierarquia(cdPlanoContas, 0, null);
	}

	public static ResultSetMap getAllHierarquia(int cdPlanoContas, int cdContaSuperior) {
		return getAllHierarquia(cdPlanoContas, cdContaSuperior, null);
	}

	public static ResultSetMap getAllHierarquia(int cdPlanoContas, int cdContaSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (cdPlanoContas <= 0)
				return null;
			if (isConnectionNull)
				connect = Conexao.conectar();

			String sql = "SELECT A.*, " +
						 "   B.nm_conta, B.cd_historico_padrao, B.nr_conta_reduzida, B.nr_conta_impressao, " +
						 "	 C.nm_historico AS nm_historico_padrao " +
		                 "FROM ctb_conta_plano_contas A, ctb_conta B " +
		                 "   LEFT OUTER JOIN ctb_historico C ON (B.cd_historico_padrao = C.cd_historico) " +
		                 "WHERE (A.cd_conta = B.cd_conta) " +
		                 "   AND A.cd_plano_contas = ? " +
		                 "	 AND A.cd_conta_superior " + ((cdContaSuperior == 0) ? " is null " : " = ? ") +
		                 "	 AND A.cd_plano_contas = " + cdPlanoContas + " " +
		                 "ORDER BY B.nr_conta ";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdPlanoContas);
			if(cdContaSuperior != 0)
				pstmt.setInt(2, cdContaSuperior);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while(rsm.next()){
					rsm.setValueToField("subResultSetMap", getAllHierarquia(cdPlanoContas, rsm.getInt("cd_conta"), connect));
			}
			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.getAllHierarquia: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}

	public static ResultSetMap getAllContas(int cdPlanoContas) {
		return getAllContas(cdPlanoContas, 0, -1, null);
	}

	public static ResultSetMap getAllContas(int cdPlanoContas, int cdContaPlanoContas) {
		return getAllContas(cdPlanoContas, cdContaPlanoContas, -1, null);
	}

	public static ResultSetMap getAllContas(int cdPlanoContas, int cdContaPlanoContas, int tpConta) {
		return getAllContas(cdPlanoContas, cdContaPlanoContas, tpConta, null);
	}

	public static ResultSetMap getAllContas(int cdPlanoContas, int cdContaPlanoContas, int tpConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if (cdPlanoContas <= 0) {
				return null;
			}
			String sql = "SELECT A.*, " +
					     "   B.nm_conta, B.cd_historico_padrao, B.nr_conta_reduzida, B.nr_conta_impressao, " +
					     "   C.nm_historico AS nm_historico_padrao " +
					     "FROM ctb_conta_plano_contas A, ctb_conta B " +
					     "   LEFT OUTER JOIN ctb_historico C " +
					     "      ON (B.cd_historico_padrao = C.cd_historico) " +
					     "WHERE (A.cd_conta = B.cd_conta) " +
					     "	AND A.cd_plano_contas = " + cdPlanoContas + (cdContaPlanoContas > 0 ? " AND (A.cd_conta_plano_contas = " + cdContaPlanoContas + " OR A.cd_conta_superior = " + cdContaPlanoContas + ")": "") +
					     (tpConta >= 0 ? "AND (A.tp_conta = " + tpConta + ")" : "");
			criterios.add(new ItemComparator("CD_PLANO_CONTAS", Integer.toString(cdPlanoContas), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.nr_conta", criterios, connect, false);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("CD_CONTA_SUPERIOR") != 0) {
					int pointer = rsm.getPointer();
					int cdContaSuperior = rsm.getInt("CD_CONTA_SUPERIOR");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("CD_CONTA_PLANO_CONTAS", new Integer(cdContaSuperior), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("CD_CONTA_PLANO_CONTAS") == cdContaSuperior;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm != null) {
							subRsm = parentNode == null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm == null ? null : subRsm.getRegister();
							isFound = subRsm == null ? false : subRsm.getInt("CD_CONTA_PLANO_CONTAS") == cdContaSuperior;
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
			System.err.println("Erro! ctb.ContaPlanoContasServices.getAllContas: " +  e);
			return  null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int calculaDigito(String nrConta){
		int nrDigito = 0;

		return nrDigito;
	}

	public static ContaPlanoContas save(ContaPlanoContas contaPlanoContas, Conta conta){
		return save(contaPlanoContas, conta, null);
	}

	public static ContaPlanoContas save(ContaPlanoContas contaPlanoContas, Conta conta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno  = 1;
			int nrDigito = calculaDigito(contaPlanoContas.getNrConta());
			// int cdConta;

			contaPlanoContas.setNrDigito(nrDigito);
			if (ContaServices.save(conta, connect) == null) {
				Conexao.rollback(connect);
				return null;
			}
			if(contaPlanoContas.getCdContaPlanoContas() == 0) {
				// cdConta = conta.getCdConta();

				contaPlanoContas.setCdConta(conta.getCdConta());
				contaPlanoContas.setNrConta(gerarNrConta(contaPlanoContas.getCdContaSuperior(), contaPlanoContas.getIdConta(), connect));

				retorno = ContaPlanoContasDAO.insert(contaPlanoContas, connect);
				if(retorno > 0){
					retorno = updateTiposContasSuperiores(contaPlanoContas.getCdConta(), connect);
					if(retorno < 0){
						Conexao.rollback(connect);
						return null;
					}
				}
			}
			else {
				// cdConta = contaPlanoContas.getCdConta();

				contaPlanoContas.setNrConta(gerarNrConta(contaPlanoContas.getCdContaSuperior(), contaPlanoContas.getIdConta(), connect));
				retorno = updateNrContasInferiores(contaPlanoContas.getCdConta(), contaPlanoContas.getCdPlanoContas(), contaPlanoContas.getNrConta(), connect);
				if(retorno < 0) {
					Conexao.rollback(connect);
					return null;
				}

				retorno = ContaPlanoContasDAO.update(contaPlanoContas, connect);
			}

			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			return contaPlanoContas;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String gerarNrConta(int cdContaSuperior, String idConta, Connection connect)	{
		try {
			String nrConta = idConta;
			ResultSet rs;
			PreparedStatement pstmt = connect.prepareStatement(
				"SELECT nr_conta, cd_conta_superior " +
				"FROM ctb_conta_plano_contas " +
	            "WHERE cd_conta_plano_contas = ? ");
			if (cdContaSuperior > 0) {
				pstmt.setInt(1, cdContaSuperior);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					nrConta = rs.getString("nr_conta") + "." + idConta;
				}
			}
			return nrConta;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.gerarNrConta: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.gerarNrConta: " +  e);
			return null;
		}
	}

	public static int updateNrContasInferiores(int cdConta, int cdPlanoContas, String prefixoConta)	{
		return updateNrContasInferiores(cdConta, cdPlanoContas, prefixoConta, null);
	}

	public static int updateNrContasInferiores(int cdConta, int cdPlanoContas, String prefixoConta, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 1;

			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, " +
					"	B.nm_conta, B.cd_historico_padrao, B.nr_conta_reduzida, B.nr_conta_impressao " +
					"FROM ctb_conta_plano_contas A, ctb_conta B " +
	                "WHERE A.cd_conta = B.cd_conta " +
	                "	AND A.cd_conta_superior = ? " +
	                "   AND A.cd_plano_contas = ? ");

			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdPlanoContas);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next())	{
				ContaPlanoContas contaPlanoContas = new ContaPlanoContas(rs.getInt("cd_conta_plano_contas"),
																		 rs.getInt("cd_conta"),
																		 rs.getInt("cd_plano_contas"),
																		 rs.getInt("cd_conta_superior"),
																		 prefixoConta + "." + rs.getString("id_conta"),
																		 rs.getInt("nr_digito"),
																		 rs.getInt("tp_conta"),
																		 rs.getInt("tp_natureza"),
																		 (rs.getTimestamp("dt_inativacao") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
																		 rs.getString("txt_observacao"),
																		 rs.getString("nr_conta_externa"),
																		 (rs.getTimestamp("dt_cadastro") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
																		 rs.getFloat("pr_depreciacao"),
																		 rs.getInt("lg_orcamentaria"),
																		 rs.getString("id_conta"));
				retorno = ContaPlanoContasDAO.update(contaPlanoContas, connect);
				if(retorno < 0) {
					Conexao.rollback(connect);
					return -1;
				}
				retorno = updateNrContasInferiores(contaPlanoContas.getCdConta(), contaPlanoContas.getCdPlanoContas(),contaPlanoContas.getNrConta(), connect);
			}

			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.updateNrContasInferiores: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int updateTiposContasSuperiores(int cdContaPlanoContas) {
		return updateTiposContasSuperiores(cdContaPlanoContas, null);
	}

	public static int updateTiposContasSuperiores(int cdContaPlanoContas, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int cdContaSuperior = 0;
		    int retorno = 1;

			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM ctb_conta_plano_contas " +
	                "WHERE cd_conta_superior = ? ");

			pstmt.setInt(1, cdContaPlanoContas);
			ResultSet rs = pstmt.executeQuery();

		    if(rs.next()) {
				cdContaSuperior = rs.getInt("cd_conta_superior");
				ContaPlanoContas contaPlanoContas = ContaPlanoContasDAO.get(cdContaSuperior, connect);
				if (contaPlanoContas != null) {
					contaPlanoContas.setTpConta(TP_SINTETICA);
					retorno = ContaPlanoContasDAO.update(contaPlanoContas, connect);
					if(retorno < 0) {
						Conexao.rollback(connect);
						return -1;
					}
					else {
						retorno = updateTiposContasSuperiores(cdContaSuperior, connect);
					}
				}
			}
			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.updateTiposContasSuperiores: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT DISTINCT A.*, A.nr_conta || ' - ' || F.nm_conta AS ds_conta, " +
			" 	B.nm_plano_contas, " +
			"   F.nm_conta, F.cd_historico_padrao, F.nr_conta_reduzida, F.nr_conta_impressao, F.cd_conta AS cd_conta, " +
			"   D.nm_historico AS nm_historico_padrao " +
			"FROM ctb_conta F " +
			"   LEFT OUTER JOIN ctb_conta_plano_contas A ON (F.cd_conta = A.cd_conta) " +
			"	LEFT OUTER JOIN ctb_plano_contas B ON (A.cd_plano_contas = B.cd_plano_contas) " +
			"   LEFT OUTER JOIN ctb_empresa_exercicio E ON (B.cd_plano_contas = E.cd_plano_contas) " +
			"	LEFT OUTER JOIN ctb_historico D ON (F.cd_historico_padrao = D.cd_historico) " +
			"WHERE (1=1)", "ORDER BY F.nm_conta", criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false, false);
	}

	public static ResultSetMap getAllCentroCusto(int cdContaPlanoContas) {
		return getAllCentroCusto(cdContaPlanoContas, null);
	}

	public static ResultSetMap getAllCentroCusto(int cdContaPlanoContas, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_conta_plano_contas, A.cd_centro_custo, A.pr_rateio, " +
										     "	 B.cd_conta, B.cd_conta_superior, B.cd_plano_contas, B.nr_conta, B.nr_digito, B.tp_conta, B.tp_natureza, B.dt_inativacao, " +
										     "	 B.txt_observacao, B.dt_cadastro, B.nr_conta_externa, B.pr_depreciacao, B.lg_orcamentaria, B.id_conta, " +
										     "	 C.cd_centro_custo_superior, C.cd_setor, C.cd_plano_centro_custo, C.nm_centro_custo, C.nr_centro_custo, C.id_centro_custo, " +
											 "   C.nr_centro_custo || ' - ' || C.nm_centro_custo AS ds_centro_custo, " +
										     "	 D.cd_historico_padrao, D.nm_conta, D.nr_conta_reduzida, D.nr_conta_impressao, " +
										     "	 E.nm_setor, E.id_setor " +
										     "FROM ctb_conta_centro_custo A, ctb_conta_plano_contas B, ctb_conta D, ctb_centro_custo C " +
											 "	 LEFT OUTER JOIN grl_setor E ON (C.cd_setor = E.cd_setor) " +
											 "WHERE (A.cd_conta_plano_contas = B.cd_conta_plano_contas) " +
											 "   AND A.cd_centro_custo = C.cd_centro_custo " +
											 "   AND B.cd_conta = D.cd_conta " +
											 "   AND A.cd_conta_plano_contas = ? " +
											 "ORDER BY C.nm_centro_custo");
			pstmt.setInt(1, cdContaPlanoContas);
			ResultSet rs = pstmt.executeQuery();
			return new ResultSetMap(rs);
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.getAllCentroCusto: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ctb.ContaPlanoContasServices.getAllCentroCusto: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPlanoContas getByConta(int cdPlanoContas, int cdConta) {
		return getByConta(cdPlanoContas, cdConta, null);
	}

	public static ContaPlanoContas getByConta(int cdPlanoContas, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_conta_plano_contas WHERE cd_plano_contas=? AND cd_conta=?");
			pstmt.setInt(1, cdPlanoContas);
			pstmt.setInt(2, cdConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPlanoContas(rs.getInt("cd_conta_plano_contas"),
					rs.getInt("cd_conta"),
					rs.getInt("cd_plano_contas"),
					rs.getInt("cd_conta_superior"),
					rs.getString("nr_conta"),
					rs.getInt("nr_digito"),
					rs.getInt("tp_conta"),
					rs.getInt("tp_natureza"),
					(rs.getTimestamp("dt_inativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inativacao").getTime()),
					rs.getString("txt_observacao"),
					rs.getString("nr_conta_externa"),
					(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
					rs.getFloat("pr_depreciacao"),
					rs.getInt("lg_orcamentaria"),
					rs.getString("id_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasServices.getByConta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPlanoContasServices.getByConta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
