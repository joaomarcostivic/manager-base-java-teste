package com.tivic.manager.srh;

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

public class VinculoEmpregaticioServices {
	
	public static final int rjCLT = 0;
	public static final int rjCONTRATO = 1;
	public static final int rjESTATUTARIO = 2;
	
	public static Result save(VinculoEmpregaticio vinculoEmpregaticio) {
		return save(vinculoEmpregaticio, null, null);
	}

	public static Result save(VinculoEmpregaticio vinculoEmpregaticio, AuthData authData) {
		return save(vinculoEmpregaticio, authData, null);
	}

	public static Result save(VinculoEmpregaticio vinculoEmpregaticio, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (vinculoEmpregaticio == null)
				return new Result(-1, "Erro ao salvar. VinculoEmpregaticio é nulo");

			int retorno;
			if (vinculoEmpregaticio.getCdVinculoEmpregaticio() == 0) {
				retorno = VinculoEmpregaticioDAO.insert(vinculoEmpregaticio, connect);
				vinculoEmpregaticio.setCdVinculoEmpregaticio(retorno);
			} else {
				retorno = VinculoEmpregaticioDAO.update(vinculoEmpregaticio, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...",
					"VINCULOEMPREGATICIO", vinculoEmpregaticio);
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

	public static Result remove(int cdVinculoEmpregaticio) {
		return remove(cdVinculoEmpregaticio, false, null, null);
	}

	public static Result remove(int cdVinculoEmpregaticio, boolean cascade) {
		return remove(cdVinculoEmpregaticio, cascade, null, null);
	}

	public static Result remove(int cdVinculoEmpregaticio, boolean cascade, AuthData authData) {
		return remove(cdVinculoEmpregaticio, cascade, authData, null);
	}

	public static Result remove(int cdVinculoEmpregaticio, boolean cascade, AuthData authData, Connection connect) {
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
				retorno = VinculoEmpregaticioDAO.delete(cdVinculoEmpregaticio, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_vinculo_empregaticio");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_vinculo_empregaticio", criterios,
				connect != null ? connect : Conexao.conectar(), connect == null);
	}

}