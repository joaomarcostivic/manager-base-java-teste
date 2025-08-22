package com.tivic.manager.evt;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class EventoServices {
	
	public static final String[] tipoEvento = {
			"Principal", // 1
			"Palestra", // 2
			"Mesa Redonda", // 3
			"Oficina", // 4
			"Workshop", // 5,
			"Conferencista" // 6
	};

	public static Result save(Evento evento){
		return save(evento, null);
	}

	public static Result save(Evento evento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(evento==null)
				return new Result(-1, "Erro ao salvar. Evento é nulo");

			int retorno;
			if(evento.getCdEvento()==0){
				retorno = EventoDAO.insert(evento, connect);
				evento.setCdEvento(retorno);
			}
			else {
				retorno = EventoDAO.update(evento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EVENTO", evento);
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
	public static Result remove(int cdEvento){
		return remove(cdEvento, false, null);
	}
	public static Result remove(int cdEvento, boolean cascade){
		return remove(cdEvento, cascade, null);
	}
	public static Result remove(int cdEvento, boolean cascade, Connection connect){
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
			retorno = EventoDAO.delete(cdEvento, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, D.nm_evento as nm_evento_principal FROM evt_evento A "
					+ "LEFT OUTER JOIN evt_local B ON (A.cd_local = B.cd_local) "
					+ "LEFT OUTER JOIN grl_pessoa C ON (A.cd_facilitador = C.cd_pessoa) "
					+ "LEFT OUTER JOIN evt_evento D ON (A.cd_evento_principal = D.cd_evento) "
					);
			
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				while(rsm.next()){
					if (rsm.getGregorianCalendar("DT_INICIAL") == null || rsm.getGregorianCalendar("DT_EVENTO") != null) {
		        		 rsm.setValueToField("DT_INICIAL", rsm.getTimestamp("DT_EVENTO"));
		        		 rsm.setValueToField("HR_INICIAL", rsm.getDateFormat("DT_EVENTO", "HH:mm"));
		        	 }
		        	 
		        	 if (rsm.getTimestamp("DT_INICIAL") != null) {
		        		 rsm.setValueToField("HR_INICIAL", rsm.getDateFormat("DT_INICIAL", "HH:mm"));
		        	 }
		        	 
		        	 if (rsm.getGregorianCalendar("DT_FINAL") != null) {
		        		 rsm.setValueToField("HR_FINAL", rsm.getDateFormat("DT_FINAL", "HH:mm"));
		        	 }
				}
				
				return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllSubEventosByEventoPrincipal(int cdEvento) {
		return getAllSubEventosByEventoPrincipal(cdEvento, null);
	}

	public static ResultSetMap getAllSubEventosByEventoPrincipal(int cdEvento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM evt_evento A LEFT OUTER JOIN evt_local B ON (A.cd_local = B.cd_local) WHERE A.cd_evento_principal = ?");
			pstmt.setInt(1, cdEvento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoServices.getAllSubEventosByEventoPrincipal: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoServices.getAllSubEventosByEventoPrincipal: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAllFacilitadores(ArrayList<ItemComparator> criterios) {
		return findAllFacilitadores(criterios, null);
	}
	
	public static ResultSetMap findAllFacilitadores(ArrayList<ItemComparator> criterios, Connection connect) {
		try {

			String sql = "SELECT  A.*, B.* "
					   + "FROM grl_pessoa A "
					   + "JOIN evt_evento B ON (A.cd_pessoa = B.cd_facilitador)  " 
					   + "LEFT OUTER JOIN grl_pessoa_empresa C ON (A.cd_pessoa = C.cd_pessoa) "
					   + "WHERE 1=1 ";
			
	        ResultSetMap rsm = Search.find(sql, criterios,  connect != null ? connect : Conexao.conectar());
	        
	        while (rsm.next()) {
	        	if(rsm.getInt("TP_EVENTO") == 2){
	        		rsm.setValueToField("NM_PARTICIPACAO_CERTIFICADO", "como palestrante");
	        	} else if(rsm.getInt("TP_EVENTO") == 3){
	        		rsm.setValueToField("NM_PARTICIPACAO_CERTIFICADO", "de uma mesa redonda");
	        	} else if(rsm.getInt("TP_EVENTO") == 4){
	        		rsm.setValueToField("NM_PARTICIPACAO_CERTIFICADO", "como oficineiro(a)");	        		
	        	} else if(rsm.getInt("TP_EVENTO") == 5){
	        		rsm.setValueToField("NM_PARTICIPACAO_CERTIFICADO", "como ministrante de workshop");	 
	        	} else if(rsm.getInt("TP_EVENTO") == 6){
	        		rsm.setValueToField("NM_PARTICIPACAO_CERTIFICADO", "como conferencista");	 
	        	}
	        	
	        }
	        
	        rsm.beforeFirst();
	        return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoServices.getAll: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
    }
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			String sql = "SELECT A.*, B.*, C.NM_EVENTO AS NM_EVENTO_PRINCIPAL "
					   + "FROM evt_evento A "
					   + "LEFT OUTER JOIN grl_pessoa B ON (A.cd_facilitador = B.cd_pessoa) "
					   + "LEFT OUTER JOIN evt_evento C ON (A.cd_evento_principal = C.cd_evento) ";
			
	        ResultSetMap rsm = Search.find(sql, "", criterios, (connect != null ? connect : Conexao.conectar()), true);

	        while (rsm.next()) {	        		        	
//	        	ResultSetMap eventos = EventoPessoaServices.findCadastrosByEvento(rsm.getInt("CD_EVENTO"), 0, connect);
//	        	
//	        	if(eventos != null && eventos.size() >= rsm.getInt("QT_VAGAS")){
//	        		rsm.setValueToField("LG_ATIVADO", 0);
//	        	}
//	        	
//	            if (rsm.getString("NM_PESSOA") == null || rsm.getString("NM_PESSOA").equals("")) {
//	                rsm.setValueToField("NM_PESSOA", "NÃO DEFINIDO");
//	                rsm.setValueToField("TXT_OBSERVACAO", "NÃO DEFINIDO");
//	            }
	        	 if (rsm.getGregorianCalendar("DT_INICIAL") == null || rsm.getGregorianCalendar("DT_EVENTO") != null) {
	        		 rsm.setValueToField("DT_INICIAL", rsm.getGregorianCalendar("DT_EVENTO"));
	        		 rsm.setValueToField("HR_INICIAL", rsm.getDateFormat("DT_EVENTO", "HH:mm"));
	        	 }
	        	 
	        	 if (rsm.getGregorianCalendar("DT_INICIAL") != null) {
	        		 rsm.setValueToField("HR_INICIAL", rsm.getDateFormat("DT_INICIAL", "HH:mm"));
	        	 }
	        	 
	        	 if (rsm.getGregorianCalendar("DT_FINAL") != null) {
	        		 rsm.setValueToField("HR_FINAL", rsm.getDateFormat("DT_FINAL", "HH:mm"));
	        	 }
	        }
	        
	        rsm.beforeFirst();
	        return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoServices.getAll: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
    }

}
