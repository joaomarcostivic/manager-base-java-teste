package com.tivic.manager.agd;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;


public class AssuntoArquivoServices {

	public static Arquivo insert(Arquivo arquivo, int cdAssunto, int cdUsuario) {
		return insert(arquivo, new AssuntoArquivo(cdAssunto, 0 /*cdArquivo*/, cdUsuario), null);
	}

	public static Arquivo insert(Arquivo arquivo, AssuntoArquivo objeto) {
		return insert(arquivo, objeto, null);
	}

	public static Arquivo insert(Arquivo arquivo, AssuntoArquivo objeto, Connection connection){
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
				return null;
			}
			
			objeto.setCdArquivo(cdArquivo);
			if (AssuntoArquivoDAO.insert(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			
			if (isConnectionNull)
				connection.commit();

			arquivo.setCdArquivo(cdArquivo);
			arquivo.setBlbArquivo(null);
			return arquivo;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoArquivoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Arquivo update(Arquivo arquivo, int cdAssunto, int cdUsuario) {
		return update(arquivo, new AssuntoArquivo(cdAssunto, arquivo.getCdArquivo(), cdUsuario), null);
	}

	public static Arquivo update(Arquivo arquivo, AssuntoArquivo objeto) {
		return update(arquivo, objeto, null);
	}

	public static Arquivo update(Arquivo arquivo, AssuntoArquivo objeto, Connection connection){
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
				return null;
			}
			
			if (AssuntoArquivoDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			
			if (isConnectionNull)
				connection.commit();

			arquivo.setBlbArquivo(null);
			return arquivo;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoArquivoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int delete(int cdAssunto, int cdArquivo) {
		return delete(cdAssunto, cdArquivo, null);
	}

	public static int delete(int cdAssunto, int cdArquivo, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			if (AssuntoArquivoDAO.delete(cdAssunto, cdArquivo, connection) <= 0) {
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
			System.err.println("Erro! AssuntoArquivoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
