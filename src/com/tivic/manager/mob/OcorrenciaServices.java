package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.ocorrencia.TipoOcorrenciaEnum;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class OcorrenciaServices {
	public static final int TIPO_FICI = 3;

	public static Result save(Ocorrencia ocorrencia){
		return save(ocorrencia, null, null);
	}

	public static Result save(Ocorrencia ocorrencia, AuthData authData){
		return save(ocorrencia, authData, null);
	}

	public static Result save(Ocorrencia ocorrencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrencia==null)
				return new Result(-1, "Erro ao salvar. Ocorrencia é nulo");

			int retorno;
			if(ocorrencia.getCdOcorrencia()==0){
				retorno = OcorrenciaDAO.insert(ocorrencia, connect);
				ocorrencia.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaDAO.update(ocorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIA", ocorrencia);
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
	public static Result remove(Ocorrencia ocorrencia) {
		return remove(ocorrencia.getCdOcorrencia());
	}
	public static Result remove(int cdOcorrencia){
		return remove(cdOcorrencia, false, null, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade){
		return remove(cdOcorrencia, cascade, null, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, AuthData authData){
		return remove(cdOcorrencia, cascade, authData, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OcorrenciaDAO.delete(cdOcorrencia, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estï¿½ vinculado a outros e nï¿½o pode ser excluï¿½do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluï¿½do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap findOrdered(ArrayList<ItemComparator> criterios) {
		return findOrdered(criterios, null);
	}

	public static ResultSetMap findOrdered(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_ocorrencia", "ORDER BY ds_ocorrencia ASC", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		
		boolean isConnectionNull = connect == null;
		Criterios criterios = new Criterios(); 
		criterios.add("tp_ocorrencia", String.valueOf(TipoOcorrenciaEnum.OCORRENCIA_APP.getKey()), ItemComparator.EQUAL, Types.INTEGER);
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			String sql = "SELECT * FROM mob_ocorrencia";
			
			if(Util.isStrBaseAntiga()) {
				sql = "SELECT *, cod_ocorrencia as cd_ocorrencia FROM ocorrencia";
			}
			
			ResultSetMap rsm = Search.find(sql, "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);;
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

}
