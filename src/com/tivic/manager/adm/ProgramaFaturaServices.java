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

public class ProgramaFaturaServices {

	public static final int TP_NENHUMA 			 = 0;
	public static final int TP_DIA_FIXO 		 = 1;
	public static final int TP_SEMANAL 			 = 2;
	public static final int TP_SEMANAL_ALTERNADO = 3;
	public static final int TP_MENSAL 			 = 4;
	public static final int TP_MENSAL_ALTERNADO  = 5;
	public static final int TP_QUINZENAL 		 = 6;
	
	public static final String[] tipoFrequencia = {"Nenhuma", "Dia Fixo", "Semanal","Semanas Alternadas","Mensal", "Meses alternados", "Quinzenal"};
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	
	public static Result save(ProgramaFatura programaFatura){
		return save(programaFatura, null);
	}

	public static Result save(ProgramaFatura programaFatura, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(programaFatura==null)
				return new Result(-1, "Erro ao salvar. ProgramaFatura é nulo");

			//Garante que apenas um registro no sistema será o padrão
			if(programaFatura.getLgPadrao()==1){
				setPadrao(programaFatura.getCdProgramaFatura(), connect);
			}
			
			int retorno;
			if(programaFatura.getCdProgramaFatura()==0){
				retorno = ProgramaFaturaDAO.insert(programaFatura, connect);
				programaFatura.setCdProgramaFatura(retorno);
			}
			else {
				retorno = ProgramaFaturaDAO.update(programaFatura, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROGRAMAFATURA", programaFatura);
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
	public static Result remove(int cdProgramaFatura){
		return remove(cdProgramaFatura, false, null);
	}
	public static Result remove(int cdProgramaFatura, boolean cascade){
		return remove(cdProgramaFatura, cascade, null);
	}
	public static Result remove(int cdProgramaFatura, boolean cascade, Connection connect){
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
			retorno = ProgramaFaturaDAO.delete(cdProgramaFatura, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_programa_fatura");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_programa_fatura", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static ResultSetMap get(int cdProgramaFatura){
		return get(cdProgramaFatura, null);
	}
	
	public static ResultSetMap get(int cdProgramaFatura, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			String sql = "SELECT * FROM adm_programa_fatura WHERE cd_programa_fatura = " + cdProgramaFatura;
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que busca o registro de programa de fatura padrão do sistema
	 * @since 15/08/2014
	 * @author Gabriel
	 * @return
	 */
	public static ProgramaFatura getPadrao(){
		return getPadrao(null);
	}
	
	public static ProgramaFatura getPadrao(Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ProgramaFaturaDAO.find(criterios, connection);
			while(rsm.next()){
				return ProgramaFaturaDAO.get(rsm.getInt("cd_programa_fatura"), connection);
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
	 * Metodo que vai garantir que apenas um registro de programa de fatura do sistema será a padrão
	 * @since 15/08/2014
	 * @author Gabriel
	 * @param cdProgramaFatura
	 */
	public static void setPadrao(int cdProgramaFatura){
		setPadrao(cdProgramaFatura, null);
	}
	
	public static void setPadrao(int cdProgramaFatura, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
								
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lg_padrao", "1", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ProgramaFaturaDAO.find(criterios, connection);
			while(rsm.next()){
				ProgramaFatura programaFatura = ProgramaFaturaDAO.get(rsm.getInt("cd_programa_fatura"), connection);
				programaFatura.setLgPadrao(0);
				if(ProgramaFaturaDAO.update(programaFatura, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
				}
			}
			
			ProgramaFatura programaFatura = ProgramaFaturaDAO.get(cdProgramaFatura, connection);
			programaFatura.setLgPadrao(1);
			if(ProgramaFaturaDAO.update(programaFatura, connection) < 0){
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
