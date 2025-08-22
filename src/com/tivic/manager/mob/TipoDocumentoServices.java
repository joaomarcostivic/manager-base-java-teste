package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class TipoDocumentoServices {

	public static Result save(TipoDocumento tipoDocumento){
		return save(tipoDocumento, null, null);
	}

	public static Result save(TipoDocumento tipoDocumento, AuthData authData){
		return save(tipoDocumento, authData, null);
	}

	public static Result save(TipoDocumento tipoDocumento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoDocumento==null)
				return new Result(-1, "Erro ao salvar. TipoDocumento é nulo");

			int retorno;
			if(tipoDocumento.getCdTipoDocumento()==0){
				retorno = TipoDocumentoDAO.insert(tipoDocumento, connect);
				tipoDocumento.setCdTipoDocumento(retorno);
			}
			else {
				retorno = TipoDocumentoDAO.update(tipoDocumento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPODOCUMENTO", tipoDocumento);
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
	public static Result remove(TipoDocumento tipoDocumento) {
		return remove(tipoDocumento.getCdTipoDocumento());
	}
	public static Result remove(int cdTipoDocumento){
		return remove(cdTipoDocumento, false, null, null);
	}
	public static Result remove(int cdTipoDocumento, boolean cascade){
		return remove(cdTipoDocumento, cascade, null, null);
	}
	public static Result remove(int cdTipoDocumento, boolean cascade, AuthData authData){
		return remove(cdTipoDocumento, cascade, authData, null);
	}
	public static Result remove(int cdTipoDocumento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoDocumentoDAO.delete(cdTipoDocumento, connect);
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
			pstmt = connect.prepareStatement("SELECT B.* FROM mob_tipo_documento A, gpn_tipo_documento B WHERE A.cd_tipo_documento = B.cd_tipo_documento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoServices.getAll: " + e);
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
		return Search.find("SELECT B.* FROM mob_tipo_documento A, gpn_tipo_documento B WHERE A.cd_tipo_documento = B.cd_tipo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}