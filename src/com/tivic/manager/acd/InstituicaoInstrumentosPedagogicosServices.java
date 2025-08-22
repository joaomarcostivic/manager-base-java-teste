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

public class InstituicaoInstrumentosPedagogicosServices {

	public static Result save(InstituicaoInstrumentosPedagogicos instituicaoInstrumentosPedagogicos){
		return save(instituicaoInstrumentosPedagogicos, null, null);
	}

	public static Result save(InstituicaoInstrumentosPedagogicos instituicaoInstrumentosPedagogicos, AuthData authData){
		return save(instituicaoInstrumentosPedagogicos, authData, null);
	}

	public static Result save(InstituicaoInstrumentosPedagogicos instituicaoInstrumentosPedagogicos, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoInstrumentosPedagogicos==null)
				return new Result(-1, "Erro ao salvar. InstituicaoInstrumentosPedagogicos é nulo");

			int retorno;
			if(instituicaoInstrumentosPedagogicos.getCdInstituicao()==0){
				retorno = InstituicaoInstrumentosPedagogicosDAO.insert(instituicaoInstrumentosPedagogicos, connect);
				instituicaoInstrumentosPedagogicos.setCdInstituicao(retorno);
			}
			else {
				retorno = InstituicaoInstrumentosPedagogicosDAO.update(instituicaoInstrumentosPedagogicos, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOINSTRUMENTOSPEDAGOGICOS", instituicaoInstrumentosPedagogicos);
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
	public static Result remove(InstituicaoInstrumentosPedagogicos instituicaoInstrumentosPedagogicos) {
		return remove(instituicaoInstrumentosPedagogicos.getCdInstituicao(), instituicaoInstrumentosPedagogicos.getCdTipoInstrumentosPedagogicos(), instituicaoInstrumentosPedagogicos.getCdPeriodoLetivo());
	}
	public static Result remove(int cdInstituicao, int cdTipoInstrumentosPedagogicos, int cdPeriodoLetivo){
		return remove(cdInstituicao, cdTipoInstrumentosPedagogicos, cdPeriodoLetivo, false, null, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoInstrumentosPedagogicos, int cdPeriodoLetivo, boolean cascade){
		return remove(cdInstituicao, cdTipoInstrumentosPedagogicos, cdPeriodoLetivo, cascade, null, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoInstrumentosPedagogicos, int cdPeriodoLetivo, boolean cascade, AuthData authData){
		return remove(cdInstituicao, cdTipoInstrumentosPedagogicos, cdPeriodoLetivo, cascade, authData, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoInstrumentosPedagogicos, int cdPeriodoLetivo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = InstituicaoInstrumentosPedagogicosDAO.delete(cdInstituicao, cdTipoInstrumentosPedagogicos, cdPeriodoLetivo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_instrumentos_pedagogicos");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoInstrumentosPedagogicosServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoInstrumentosPedagogicosServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_instrumentos_pedagogicos", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}