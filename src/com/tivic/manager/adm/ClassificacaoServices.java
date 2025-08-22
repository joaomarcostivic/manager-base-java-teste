package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class ClassificacaoServices {

	
	public static Result save(Classificacao classificacao){
		return save(classificacao, null);
	}

	public static Result save(Classificacao classificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(classificacao==null)
				return new Result(-1, "Erro ao salvar. Classificacao é nulo");

			//Garante que apenas um registro no sistema será o padrão
			if(classificacao.getLgPadrao()==1){
				setPadrao(classificacao.getCdClassificacao(), connect);
			}
			
			
			int retorno;
			if(classificacao.getCdClassificacao()==0){
				retorno = ClassificacaoDAO.insert(classificacao, connect);
				classificacao.setCdClassificacao(retorno);
			}
			else {
				retorno = ClassificacaoDAO.update(classificacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CLASSIFICACAO", classificacao);
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
	
	
	public static Result insert(Classificacao objeto) {
		return insert(objeto, null);
	}

	public static Result insert(Classificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int code = ClassificacaoDAO.insert(objeto, connect);
			if(code <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar inserir classificacao");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(code, "Classificação inserida com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.insert: " +  e);
			return new Result(-1, "Erro ao tentar inserir classificacao");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result update(Classificacao objeto) {
		return update(objeto, null);
	}

	public static Result update(Classificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(ClassificacaoDAO.update(objeto, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar atualizar classificacao");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Classificação atualizada com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.update: " +  e);
			return new Result(-1, "Erro ao tentar atualizar classificacao");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdClassificacao){
		return remove(cdClassificacao, false, null);
	}
	public static Result remove(int cdClassificacao, boolean cascade){
		return remove(cdClassificacao, cascade, null);
	}
	public static Result remove(int cdClassificacao, boolean cascade, Connection connect){
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
			retorno = ClassificacaoDAO.delete(cdClassificacao, connect);
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
	
	public static Result delete(int cdClassificacao) {
		return delete(cdClassificacao, null);
	}

	public static Result delete(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(ClassificacaoDAO.delete(cdClassificacao, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar deletar classificacao");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Classificacao deletada com sucesso!");
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.delete: " +  e);
			return new Result(-1, "Erro ao tentar deletar classificacao");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(int cdClassificacao) {
		return find(cdClassificacao, null);
	}
	
	public static ResultSetMap find(int cdClassificacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_classificacao", "" + cdClassificacao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			return rsm;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find() {
		return find(new ArrayList<ItemComparator>());
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT A.*, B.cd_cobranca, B.nm_cobranca FROM adm_classificacao A " +
					"		LEFT OUTER JOIN adm_cobranca B ON (A.cd_cobranca = B.cd_cobranca) " +
					"		WHERE 1 = 1 ";
			
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.nm_classificacao ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			return rsm;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
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
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que busca o registro de classificação de cliente padrão do sistema
	 * @since 15/08/2014
	 * @author Gabriel
	 * @return
	 */
	public static Classificacao getPadrao(){
		return getPadrao(null);
	}
	
	public static Classificacao getPadrao(Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ClassificacaoDAO.find(criterios, connection);
			while(rsm.next()){
				return ClassificacaoDAO.get(rsm.getInt("cd_classificacao"), connection);
			}
			
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	/**
	 * Metodo que vai garantir que apenas um registro de classificacao de cliente do sistema será a padrão
	 * @since 15/08/2014
	 * @author Gabriel
	 * @param cdClassificacao
	 */
	public static void setPadrao(int cdClassificacao){
		setPadrao(cdClassificacao, null);
	}
	
	public static void setPadrao(int cdClassificacao, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ClassificacaoDAO.find(criterios, connection);
			while(rsm.next()){
				Classificacao classificacao = ClassificacaoDAO.get(rsm.getInt("cd_classificacao"), connection);
				classificacao.setLgPadrao(0);
				if(ClassificacaoDAO.update(classificacao, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
				}
			}
			
			Classificacao classificacao = ClassificacaoDAO.get(cdClassificacao, connection);
			classificacao.setLgPadrao(1);
			if(ClassificacaoDAO.update(classificacao, connection) < 0){
				if(isConnectionNull)
					Conexao.rollback(connection);
			}
			
			
			if(isConnectionNull)
				connection.commit();
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
