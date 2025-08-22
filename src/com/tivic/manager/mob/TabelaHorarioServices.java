package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TabelaHorarioServices {
	
	
	public static final String[] situacaoTabelaHorario = {"Inativa","Ativa"};
	public static final String[] tipoTabelaHorario = {"Normal","Reforço","Feriado","Dias Específicos(Semana/Mês)","Datas Especiais" };
	
	public static final int ST_INATIVA            = 0;
	public static final int ST_ATIVA              = 1;
	
	public static final int TP_DIA_UTIL           = 0;
	public static final int TP_SABADO             = 1;
	public static final int TP_DOMINGO            = 2;
	public static final int TP_ESPECIAL  	 	  = 3;
	
//	public static final int TP_NORMAL             = 0;
//	public static final int TP_REFORCO            = 1;
//	public static final int TP_FERIADO            = 2;
//	public static final int TP_DIAS_ESPECIFICOS   = 3;
//	public static final int TP_DATAS_ESPECIAIS    = 4;
	
	public static Result save(TabelaHorario tabelaHorario){
		return save(tabelaHorario, null);
	}

	public static Result save(TabelaHorario tabelaHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabelaHorario==null)
				return new Result(-1, "Erro ao salvar. TabelaHorario é nulo");

			int retorno;
			if(tabelaHorario.getCdTabelaHorario()==0){
				retorno = TabelaHorarioDAO.insert(tabelaHorario, connect);
				tabelaHorario.setCdTabelaHorario(retorno);
			}
			else {
				retorno = TabelaHorarioDAO.update(tabelaHorario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELAHORARIO", tabelaHorario);
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
	
	public static HashMap<String, Object> getSyncData() {
		return getSyncData(null, null);
	}
	
	public static HashMap<String, Object> getSyncData(ArrayList<TabelaHorario> tabelaHorario) {
		return getSyncData(tabelaHorario, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<TabelaHorario> tabelaHorario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tabelaHorario != null && tabelaHorario.size() > 0) {
				for(TabelaHorario tH : tabelaHorario) {
					Result save = save(tH, connect);
					
					if(save.getCode() <= 0) {
						throw new Exception("Não foi possível completar a sincronização");
					}
				}
				
				connect.commit();
			}
			
			
			String sql = "SELECT * FROM mob_tabela_horario "
					+ "WHERE st_tabela_horario = 1";	
			
			pstmt1 = connect.prepareStatement(sql);
			
			sql = "SELECT DISTINCT ON (A.cd_tabela_horario, A.cd_linha, A.cd_horario, A.cd_rota, A.cd_trecho) A.* " +
					  "FROM mob_horario A, mob_linha B, mob_concessao C" +
					  " WHERE A.cd_linha = B.cd_linha" + 
					  "  AND B.cd_concessao = C.cd_concessao" + 
					  "  AND C.st_concessao = 1";
			pstmt2 = connect.prepareStatement(sql);
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("TabelaHorario", Util.resultSetToArrayList(pstmt1.executeQuery()));
			register.put("Horario", Util.resultSetToArrayList(pstmt2.executeQuery()));
			
			return register;
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

	
	public static HashMap<String, Object> getSync() {
		return getSync(null);
	}

	public static HashMap<String, Object> getSync(Connection connect) {	
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		try {			
			
			String sql = "SELECT * FROM mob_tabela_horario "
					+ "WHERE st_tabela_horario = 1";			
			pstmt1 = connect.prepareStatement(sql);
			
			sql = "SELECT DISTINCT ON (A.cd_tabela_horario, A.cd_linha, A.cd_horario, A.cd_rota, A.cd_trecho) A.* " +
				  "FROM mob_horario A, mob_linha B, mob_concessao C" +
				  " WHERE A.cd_linha = B.cd_linha" + 
				  "  AND B.cd_concessao = C.cd_concessao" + 
				  "  AND C.st_concessao = 1";
			pstmt2 = connect.prepareStatement(sql);

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("TabelaHorario", Util.resultSetToArrayList(pstmt1.executeQuery()));
			register.put("Horario", Util.resultSetToArrayList(pstmt2.executeQuery()));
			
			return register;
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
	
	public static Result remove(int cdTabelaHorario, int cdLinha){
		return remove(cdTabelaHorario, cdLinha, false, null);
	}
	public static Result remove(int cdTabelaHorario, int cdLinha, boolean cascade){
		return remove(cdTabelaHorario, cdLinha, cascade, null);
	}
	public static Result remove(int cdTabelaHorario, int cdLinha, boolean cascade, Connection connect){
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
			retorno = TabelaHorarioDAO.delete(cdTabelaHorario, cdLinha, connect);
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
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
						
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_concessao, B.cd_linha, B.nr_linha, C.cd_rota, C.nm_rota " +
											"FROM mob_tabela_horario A "+
											"LEFT OUTER JOIN mob_linha B ON (A.cd_linha = B.cd_linha) "+
											"LEFT OUTER JOIN mob_linha_rota C ON (A.cd_linha = C.cd_linha AND A.cd_rota = C.cd_rota ) " +
											"ORDER BY B.nr_linha, A.nr_tabela_horario, C.cd_rota");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	
	public static ResultSetMap getCount(ArrayList<ItemComparator> criterios) {
		return getCount(criterios, null);
	}

	public static ResultSetMap getCount(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			Criterios    crt = new Criterios();
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					continue;
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
					continue;
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY"))
					continue;
				else
					crt.add(criterios.get(i));
					
			}
			
			return Search.find("SELECT COUNT(*) FROM mob_tabela_horario A ", "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioServices.getCount: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Criterios    crt = new Criterios();
			int qtLimite = 0;
			int qtDeslocamento = 0;
			String orderBy = "";
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
					qtDeslocamento = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY"))
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				else
					crt.add(criterios.get(i));
					
			}

			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);
			
			ResultSetMap rsm = Search.find("SELECT " + sqlLimit[0] + " A.*, B.*, C.* FROM mob_tabela_horario A " + 
					"LEFT OUTER JOIN mob_linha B ON (A.cd_linha = B.cd_linha) " +
					"LEFT OUTER JOIN mob_concessao_veiculo C ON (A.cd_concessao_veiculo = C.cd_concessao_veiculo) ", 
					orderBy + " " + sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);

			while(rsm.next()) {
				ResultSetMap rsmRota = TabelaHorarioRotaServices.getAllByTabelaLinha(rsm.getInt("cd_linha"), rsm.getInt("cd_tabela_horario"));
				while(rsmRota.next()) {
					if(rsmRota.getInt("TP_ROTA") == LinhaRotaServices.TP_IDA) {
						rsm.setValueToField("linhaRotaIda", rsmRota.getRegister());
					}else if(rsmRota.getInt("TP_ROTA") == LinhaRotaServices.TP_VOLTA) {
						rsm.setValueToField("linhaRotaVolta", rsmRota.getRegister());
					}
				}
			}
			 
			rsm.beforeFirst();
			
			ResultSetMap rsmTotal = Search.find("SELECT COUNT(*) FROM mob_tabela_horario A ", "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			if(rsmTotal.next())
				rsm.setTotal(rsmTotal.getInt("COUNT"));

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaHorarioServices.find: " + e);
			return null;
		}finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	


}
