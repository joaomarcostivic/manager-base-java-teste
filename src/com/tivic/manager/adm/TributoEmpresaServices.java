package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.ResultSet;

import com.tivic.sol.connection.Conexao;


public class TributoEmpresaServices {

	public static int insert(Tributo tributo, TributoEmpresa tributoEmpresa) {
		return insert(tributo, tributoEmpresa, null);
	}

	public static int insert(Tributo tributo, TributoEmpresa tributoEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			ResultSet rs = connection.prepareStatement("SELECT MAX(nr_ordem_calculo) FROM adm_tributo WHERE nr_ordem_calculo IS NOT NULL ").executeQuery();
			int nrOrdemCalculo = rs.next() ? rs.getInt(1)+1 : 1;
			tributo.setNrOrdemCalculo(nrOrdemCalculo);
			
			int cdTributo = TributoDAO.insert(tributo, connection);
			if (cdTributo <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (tributoEmpresa.getCdEmpresa() > 0) {
				tributoEmpresa.setCdTributo(cdTributo);
				if (TributoEmpresaDAO.insert(tributoEmpresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}				
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return cdTributo;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int update(Tributo tributo, TributoEmpresa tributoEmpresa) {
		return update(tributo, tributoEmpresa, null);
	}

	public static int update(Tributo tributo, TributoEmpresa tributoEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			if (TributoDAO.update(tributo, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (tributoEmpresa.getCdEmpresa() > 0) {
				boolean isTributoEmpresaInsert = TributoEmpresaDAO.get(tributo.getCdTributo(), tributoEmpresa.getCdEmpresa(), connection)!=null;
				if (!isTributoEmpresaInsert && TributoEmpresaDAO.insert(tributoEmpresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;	
				}
				else if (TributoEmpresaDAO.update(tributoEmpresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;					
				}				
			}

			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaServices.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static int delete(int cdTributo, int cdEmpresa) {
		return delete(cdTributo, cdEmpresa, null);
	}

	public static int delete(int cdTributo, int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			if (TributoEmpresaDAO.delete(cdTributo, cdEmpresa, connection) <= 0) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);				
			}
			
			if (TributoDAO.delete(cdTributo, connection) <= 0) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);				
			}

			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoEmpresaServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
