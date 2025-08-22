package com.tivic.manager.grl;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;

import sol.util.Result;

public class ConteinerArquivoServices {

	public static Result insert(Arquivo arquivo, int cdConteiner) {
		return insert(arquivo, new ConteinerArquivo(cdConteiner, 0), null);
	}

	public static Result insert(Arquivo arquivo, ConteinerArquivo objeto) {
		return insert(arquivo, objeto, null);
	}

	public static Result insert(Arquivo arquivo, ConteinerArquivo objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			arquivo.setDtArquivamento(new GregorianCalendar());
			int cdArquivo = ArquivoDAO.insert(arquivo, connection);
			if (cdArquivo <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao incluir arquivo!");
			}
			
			objeto.setCdArquivo(cdArquivo);
			if (ConteinerArquivoDAO.insert(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao incluir arquivo no conteiner!");
			}
			
			if (isConnectionNull)
				connection.commit();

			arquivo.setCdArquivo(cdArquivo);
			
			Result result = new Result(cdArquivo);
			result.addObject("arquivo", arquivo);
			result.addObject("qtBytes", arquivo.getBlbArquivo()==null ? 0 : arquivo.getBlbArquivo().length);
			arquivo.setBlbArquivo(null);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir arquivo no conteiner!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result update(Arquivo arquivo, int cdConteiner) {
		return update(arquivo, new ConteinerArquivo(cdConteiner, arquivo.getCdArquivo()), null);
	}

	public static Result update(Arquivo arquivo, ConteinerArquivo objeto) {
		return update(arquivo, objeto, null);
	}

	public static Result update(Arquivo arquivo, ConteinerArquivo objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			arquivo.setDtArquivamento(new GregorianCalendar());
			if (ArquivoDAO.update(arquivo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar dados do arquivo!");
			}
			
			if (ConteinerArquivoDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erro ao atualizar dados do arquivo no conteiner!");
			}
			
			if (isConnectionNull)
				connection.commit();

			Result result = new Result(1);
			result.addObject("arquivo", arquivo);
			result.addObject("qtBytes", arquivo.getBlbArquivo()==null ? 0 : arquivo.getBlbArquivo().length);
			arquivo.setBlbArquivo(null);
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int delete(int cdConteiner, int cdArquivo) {
		return delete(cdConteiner, cdArquivo, null);
	}

	public static int delete(int cdConteiner, int cdArquivo, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			if (ConteinerArquivoDAO.delete(cdConteiner, cdArquivo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (ArquivoDAO.delete(cdArquivo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
