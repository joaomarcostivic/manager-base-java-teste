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

public class InstituicaoProfissionaisEscolaresServices {

	public static Result save(InstituicaoProfissionaisEscolares instituicaoProfissionaisEscolares){
		return save(instituicaoProfissionaisEscolares, null, null);
	}

	public static Result save(InstituicaoProfissionaisEscolares instituicaoProfissionaisEscolares, AuthData authData){
		return save(instituicaoProfissionaisEscolares, authData, null);
	}

	public static Result save(InstituicaoProfissionaisEscolares instituicaoProfissionaisEscolares, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoProfissionaisEscolares==null)
				return new Result(-1, "Erro ao salvar. InstituicaoProfissionaisEscolares é nulo");

			int retorno;
			if(InstituicaoProfissionaisEscolaresDAO.get(instituicaoProfissionaisEscolares.getCdInstituicao(), instituicaoProfissionaisEscolares.getCdTipoProfissionaisEscolares(), instituicaoProfissionaisEscolares.getCdPeriodoLetivo(), connect)==null){
				retorno = InstituicaoProfissionaisEscolaresDAO.insert(instituicaoProfissionaisEscolares, connect);
				instituicaoProfissionaisEscolares.setCdInstituicao(retorno);
			}
			else {
				retorno = InstituicaoProfissionaisEscolaresDAO.update(instituicaoProfissionaisEscolares, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOPROFISSIONAISESCOLARES", instituicaoProfissionaisEscolares);
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
	public static Result remove(InstituicaoProfissionaisEscolares instituicaoProfissionaisEscolares) {
		return remove(instituicaoProfissionaisEscolares.getCdInstituicao(), instituicaoProfissionaisEscolares.getCdTipoProfissionaisEscolares(), instituicaoProfissionaisEscolares.getCdPeriodoLetivo());
	}
	public static Result remove(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo){
		return remove(cdInstituicao, cdTipoProfissionaisEscolares, cdPeriodoLetivo, false, null, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo, boolean cascade){
		return remove(cdInstituicao, cdTipoProfissionaisEscolares, cdPeriodoLetivo, cascade, null, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo, boolean cascade, AuthData authData){
		return remove(cdInstituicao, cdTipoProfissionaisEscolares, cdPeriodoLetivo, cascade, authData, null);
	}
	public static Result remove(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = InstituicaoProfissionaisEscolaresDAO.delete(cdInstituicao, cdTipoProfissionaisEscolares, cdPeriodoLetivo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_profissionais_escolares");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_profissionais_escolares", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}