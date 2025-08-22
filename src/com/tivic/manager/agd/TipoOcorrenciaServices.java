package com.tivic.manager.agd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

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
				return new Result(-1, "Erro ao salvar. TipoOcorrencia é nulo");

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
			pstmt = connect.prepareStatement("SELECT A.cd_tipo_ocorrencia, B.nm_tipo_ocorrencia "
											+ " FROM agd_tipo_ocorrencia A"
											+ " JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia)"
											+ " ORDER BY B.nm_tipo_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaServices.getAll: " + sqlExpt);
			return null;
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find(
				  " SELECT A.cd_tipo_ocorrencia, B.nm_tipo_ocorrencia "
				+ " FROM agd_tipo_ocorrencia A"
				+ " JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia=B.cd_tipo_ocorrencia)", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
