package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class EscolaridadeServices {
	
	public static final String ID_FUNDAMENTAL_INCOMPLETO = "1";
	public static final String ID_FUNDAMENTAL_COMPLETO = "2";
	public static final String ID_ENSINO_SUPERIOR = "6";
	public static final String ID_MEDIO_TECNICO = "73";
	public static final String ID_MEIO_MAGISTERIO = "72";
	public static final String ID_MEDIO_INDIGENA = "74";
	public static final String ID_MEDIO = "71";
	
	
	public static Result save(Escolaridade escolaridade){
		return save(escolaridade, null);
	}
	
	public static Result save(Escolaridade escolaridade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(escolaridade==null)
				return new Result(-1, "Erro ao salvar. Escolaridade é nulo");
			
			int retorno;
			if(escolaridade.getCdEscolaridade()==0){
				retorno = EscolaridadeDAO.insert(escolaridade, connect);
				escolaridade.setCdEscolaridade(retorno);
			}
			else {
				retorno = EscolaridadeDAO.update(escolaridade, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ESCOLARIDADE", escolaridade);
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
	
	public static Result remove(int cdEscolaridade){
		return remove(cdEscolaridade, false, null);
	}
	
	public static Result remove(int cdEscolaridade, boolean cascade){
		return remove(cdEscolaridade, cascade, null);
	}
	
	public static Result remove(int cdEscolaridade, boolean cascade, Connection connect){
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
				retorno = EscolaridadeDAO.delete(cdEscolaridade, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta escolaridade está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Escolaridade excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir escolaridade!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_escolaridade ORDER BY id_escolaridade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EscolaridadeDAO.getAll: " + e);
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
		
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap _rsm = Search.find("SELECT * FROM grl_escolaridade", "ORDER BY id_escolaridade"+limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return _rsm;
	}
	
	
	public static int init()	{
		if(EscolaridadeDAO.getAll().size()>0){
			return 1;
		}
		
		String[] escolaridade = new String[] {"01-Não sabe ler/escrever",
				 							  "02-Alfabetizado",
				 							  "03-Nível Fundamental Incompleto",
				 							  "04-Nível Fundamental Completo",
				 							  "05-Nível Médio Incompleto",
				 							  "06-Nível Médio Completo",
				 							  "07-Nível Superior Incompleto",
				 							  "08-Nível Superior Completo",
				 							  "09-Especialização/Residência",
				 							  "10-Mestrado",
				 							  "11-Doutorado"};
		
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// 
			pstmt = connect.prepareStatement("SELECT * " +
			           						 "FROM grl_escolaridade " +
			           						 "WHERE id_escolaridade = ?");
			for(int i=0; i<escolaridade.length; i++)	{
				pstmt.setString(1, escolaridade[i].substring(0, 2));
				if(!pstmt.executeQuery().next())	{
					Escolaridade esc = new Escolaridade(0, escolaridade[i], escolaridade[i].substring(0, 2));
					EscolaridadeDAO.insert(esc, connect);
				}
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaFgts.init: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static Escolaridade getById(String idEscolaridade) {
		return getById(idEscolaridade, null);
	}

	public static Escolaridade getById(String idEscolaridade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_escolaridade WHERE id_escolaridade=?");
			pstmt.setString(1, idEscolaridade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return EscolaridadeDAO.get(rs.getInt("cd_escolaridade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EscolaridadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EscolaridadeDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}