package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoOcorrenciaServices {
	
	public static Result save(TipoOcorrencia tipoOcorrencia){
		return save(tipoOcorrencia, null);
	}
	
	public static Result save(TipoOcorrencia tipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoOcorrencia==null)
				return new Result(-1, "Erro ao salvar. Tipo de ocorrência é nulo");
			
			int retorno;
			if(tipoOcorrencia.getCdTipoOcorrencia()==0){
				retorno = TipoOcorrenciaDAO.insert(tipoOcorrencia, connect);
				tipoOcorrencia.setCdTipoOcorrencia(retorno);
			}
			else {
				retorno = TipoOcorrenciaDAO.update(tipoOcorrencia, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOOCORRENCIA", tipoOcorrencia);
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
	
	public static Result remove(int cdTipoOcorrencia){
		return remove(cdTipoOcorrencia, false, null);
	}
	
	public static Result remove(int cdTipoOcorrencia, boolean cascade){
		return remove(cdTipoOcorrencia, cascade, null);
	}
	
	public static Result remove(int cdTipoOcorrencia, boolean cascade, Connection connect){
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
				retorno = TipoOcorrenciaDAO.delete(cdTipoOcorrencia, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de ocorrência está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de ocorrência excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de ocorrência!");
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
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.cd_tipo_ocorrencia AS lg_relacionamento, " +
					"B.tp_acao, C.cd_tipo_ocorrencia AS lg_administrativo, D.cd_tipo_ocorrencia AS lg_academico, " +
					"E.cd_tipo_ocorrencia AS lg_planejamento " +
					"FROM grl_tipo_ocorrencia A " +
					"LEFT OUTER JOIN crm_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia) " +
					"LEFT OUTER JOIN adm_tipo_ocorrencia C ON (A.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) " +
					"LEFT OUTER JOIN acd_tipo_ocorrencia D ON (A.cd_tipo_ocorrencia = D.cd_tipo_ocorrencia) " +
					"LEFT OUTER JOIN agd_tipo_ocorrencia E ON (A.cd_tipo_ocorrencia = E.cd_tipo_ocorrencia) ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nm_tipo_ocorrencia");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoOcorrencia tipoOcor, ArrayList<TipoOcorrencia> tiposOcorrencia) {
		return update(tipoOcor, tiposOcorrencia, null);
	}

	public static int update(TipoOcorrencia tipoOcor, ArrayList<TipoOcorrencia> tiposOcorrencia, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			if (TipoOcorrenciaDAO.update(tipoOcor, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			for (int i=0; tiposOcorrencia!=null && i<tiposOcorrencia.size(); i++)
				tiposOcorrencia.get(i).setCdTipoOcorrencia(tipoOcor.getCdTipoOcorrencia());

			String[] tables = {"acd_tipo_ocorrencia",
					"adm_tipo_ocorrencia",
					"agd_tipo_ocorrencia",
					"crm_tipo_ocorrencia"};
			for (int i=0; i<tables.length; i++) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM " + tables[i] + " " +
						"WHERE cd_tipo_ocorrencia = ?");
				pstmt.setInt(1, tipoOcor.getCdTipoOcorrencia());
				pstmt.execute();
			}

			for (int i=0; tiposOcorrencia!=null && i<tiposOcorrencia.size(); i++) {
				if (tiposOcorrencia.get(i) instanceof com.tivic.manager.acd.TipoOcorrencia) {
					if (com.tivic.manager.acd.TipoOcorrenciaDAO.update((com.tivic.manager.acd.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else if (tiposOcorrencia.get(i) instanceof com.tivic.manager.adm.TipoOcorrencia) {
					if (com.tivic.manager.adm.TipoOcorrenciaDAO.update((com.tivic.manager.adm.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else if (tiposOcorrencia.get(i) instanceof com.tivic.manager.agd.TipoOcorrencia) {
					if (com.tivic.manager.agd.TipoOcorrenciaDAO.update((com.tivic.manager.agd.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else if (tiposOcorrencia.get(i) instanceof com.tivic.manager.crm.TipoOcorrencia) {
					if (com.tivic.manager.crm.TipoOcorrenciaDAO.update((com.tivic.manager.crm.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int insert(TipoOcorrencia tipoOcor, ArrayList<TipoOcorrencia> tiposOcorrencia) {
		return insert(tipoOcor, tiposOcorrencia, null);
	}

	public static int insert(TipoOcorrencia tipoOcor, ArrayList<TipoOcorrencia> tiposOcorrencia, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdTipoOcorrencia = 0;
			if ((cdTipoOcorrencia = TipoOcorrenciaDAO.insert(tipoOcor, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			for (int i=0; tiposOcorrencia!=null && i<tiposOcorrencia.size(); i++)
				tiposOcorrencia.get(i).setCdTipoOcorrencia(cdTipoOcorrencia);

			for (int i=0; tiposOcorrencia!=null && i<tiposOcorrencia.size(); i++) {
				if (tiposOcorrencia.get(i) instanceof com.tivic.manager.acd.TipoOcorrencia) {
					if (com.tivic.manager.acd.TipoOcorrenciaDAO.update((com.tivic.manager.acd.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else if (tiposOcorrencia.get(i) instanceof com.tivic.manager.adm.TipoOcorrencia) {
					if (com.tivic.manager.adm.TipoOcorrenciaDAO.update((com.tivic.manager.adm.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else if (tiposOcorrencia.get(i) instanceof com.tivic.manager.agd.TipoOcorrencia) {
					if (com.tivic.manager.agd.TipoOcorrenciaDAO.update((com.tivic.manager.agd.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else if (tiposOcorrencia.get(i) instanceof com.tivic.manager.crm.TipoOcorrencia) {
					if (com.tivic.manager.crm.TipoOcorrenciaDAO.update((com.tivic.manager.crm.TipoOcorrencia)tiposOcorrencia.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			return cdTipoOcorrencia;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdTipoOcorrencia) {
		return delete(cdTipoOcorrencia, null);
	}

	public static int delete(int cdTipoOcorrencia, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			String[] tables = {"acd_tipo_ocorrencia",
					"adm_tipo_ocorrencia",
					"agd_tipo_ocorrencia",
					"crm_tipo_ocorrencia"};
			for (int i=0; i<tables.length; i++) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM " + tables[i] + " " +
						"WHERE cd_tipo_ocorrencia = ?");
				pstmt.setInt(1, cdTipoOcorrencia);
				pstmt.execute();
			}

			if (TipoOcorrenciaDAO.delete(cdTipoOcorrencia, connection) <= 0) {
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
			System.err.println("Erro! TipoOcorrenciaServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}


	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_tipo_ocorrencia", "ORDER BY nm_tipo_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static TipoOcorrencia getById(String idTipoOcorrencia) {
		return getById(idTipoOcorrencia, null);
	}

	public static TipoOcorrencia getById(String idTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_ocorrencia WHERE id_tipo_ocorrencia=?");
			pstmt.setString(1, idTipoOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return TipoOcorrenciaDAO.get(rs.getInt("cd_tipo_ocorrencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}