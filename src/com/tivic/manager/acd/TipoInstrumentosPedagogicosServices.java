package com.tivic.manager.acd;

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

public class TipoInstrumentosPedagogicosServices {

	public static Result save(TipoInstrumentosPedagogicos tipoInstrumentosPedagogicos){
		return save(tipoInstrumentosPedagogicos, null, null);
	}

	public static Result save(TipoInstrumentosPedagogicos tipoInstrumentosPedagogicos, AuthData authData){
		return save(tipoInstrumentosPedagogicos, authData, null);
	}

	public static Result save(TipoInstrumentosPedagogicos tipoInstrumentosPedagogicos, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoInstrumentosPedagogicos==null)
				return new Result(-1, "Erro ao salvar. TipoInstrumentosPedagogicos é nulo");

			int retorno;
			if(tipoInstrumentosPedagogicos.getCdTipoInstrumentosPedagogicos()==0){
				retorno = TipoInstrumentosPedagogicosDAO.insert(tipoInstrumentosPedagogicos, connect);
				tipoInstrumentosPedagogicos.setCdTipoInstrumentosPedagogicos(retorno);
			}
			else {
				retorno = TipoInstrumentosPedagogicosDAO.update(tipoInstrumentosPedagogicos, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOINSTRUMENTOSPEDAGOGICOS", tipoInstrumentosPedagogicos);
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
	public static Result remove(TipoInstrumentosPedagogicos tipoInstrumentosPedagogicos) {
		return remove(tipoInstrumentosPedagogicos.getCdTipoInstrumentosPedagogicos());
	}
	public static Result remove(int cdTipoInstrumentosPedagogicos){
		return remove(cdTipoInstrumentosPedagogicos, false, null, null);
	}
	public static Result remove(int cdTipoInstrumentosPedagogicos, boolean cascade){
		return remove(cdTipoInstrumentosPedagogicos, cascade, null, null);
	}
	public static Result remove(int cdTipoInstrumentosPedagogicos, boolean cascade, AuthData authData){
		return remove(cdTipoInstrumentosPedagogicos, cascade, authData, null);
	}
	public static Result remove(int cdTipoInstrumentosPedagogicos, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoInstrumentosPedagogicosDAO.delete(cdTipoInstrumentosPedagogicos, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_instrumentos_pedagogicos");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoInstrumentosPedagogicosServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_instrumentos_pedagogicos", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}