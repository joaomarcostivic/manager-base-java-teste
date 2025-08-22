package com.tivic.manager.srh;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

import com.tivic.manager.seg.AuthData;

public class SindicatoServices {

	public static final int PERC_SAL_BASE = 0;
	public static final int QUANTIDADE_DIAS = 1;
	public static final int PERC_SAL_MINIMO = 2;
	public static final int VALOR_FIXO = 3;

	public static Result save(Sindicato sindicato) {
		return save(sindicato, null, null);
	}

	public static Result save(Sindicato sindicato, AuthData authData) {
		return save(sindicato, authData, null);
	}

	public static Result save(Sindicato sindicato, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (sindicato == null)
				return new Result(-1, "Erro ao salvar. Sindicato é nulo");

			int retorno;
			if (sindicato.getCdSindicato() == 0) {
				retorno = SindicatoDAO.insert(sindicato, connect);
				sindicato.setCdSindicato(retorno);
			} else {
				retorno = SindicatoDAO.update(sindicato, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "SINDICATO",
					sindicato);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(int cdSindicato) {
		return remove(cdSindicato, false, null, null);
	}

	public static Result remove(int cdSindicato, boolean cascade) {
		return remove(cdSindicato, cascade, null, null);
	}

	public static Result remove(int cdSindicato, boolean cascade, AuthData authData) {
		return remove(cdSindicato, cascade, authData, null);
	}

	public static Result remove(int cdSindicato, boolean cascade, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if (cascade) {
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if (!cascade || retorno > 0)
				retorno = SindicatoDAO.delete(cdSindicato, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_sindicato");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SindicatoServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SindicatoServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM srh_sindicato", criterios, connect != null ? connect : Conexao.conectar(),
				connect == null);
	}

}