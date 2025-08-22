package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.Util;

public class InfracaoTransporteServices {

	public static Result save(InfracaoTransporte infracaoTransporte) {
		return save(infracaoTransporte, null);
	}

	public static Result save(InfracaoTransporte infracaoTransporte, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (infracaoTransporte == null)
				return new Result(-1, "Erro ao salvar. InfracaoTransporte é nulo");

			int retorno;
			if (infracaoTransporte.getCdInfracao() == 0) {
				retorno = InfracaoTransporteDAO.insert(infracaoTransporte, connect);
				infracaoTransporte.setCdInfracao(retorno);
			} else {
				retorno = InfracaoTransporteDAO.update(infracaoTransporte, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "INFRACAOTRANSPORTE",
					infracaoTransporte);
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

	public static Result remove(int cdInfracao) {
		return remove(cdInfracao, false, null);
	}

	public static Result remove(int cdInfracao, boolean cascade) {
		return remove(cdInfracao, cascade, null);
	}

	public static Result remove(int cdInfracao, boolean cascade, Connection connect) {
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
				retorno = InfracaoTransporteDAO.delete(cdInfracao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_infracao_transporte");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoTransporteServices.getAll: " + e);
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

		boolean isConnectionNull = connect == null;

		String limit = "";
		Criterios crt = new Criterios();

		String keyword = "";

		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT " + criterios.get(i).getValue();
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("keyword")) {
				if (Util.isNumber(criterios.get(i).getValue()))
					keyword = criterios.get(i).getValue();
			} else {
				crt.add(criterios.get(i));
			}
		}

		String sql = " SELECT * FROM mob_infracao_transporte ";

		if (!keyword.equals("")) {
			if (Util.isNumber(keyword))
				sql += " WHERE ((UPPER(ds_infracao) LIKE '%" + keyword + "%' OR CAST(nr_infracao as VARCHAR) LIKE '%"
						+ keyword + "%')) ";
			else
				crt.add(new ItemComparator("ds_infracao", keyword, ItemComparator.LIKE_ANY, Types.VARCHAR));
		}

		return Search.find(sql, " ORDER BY nr_infracao " + limit, crt, (isConnectionNull ? Conexao.conectar() : connect),
				isConnectionNull);

	}

	public static HashMap<String, Object> getSyncData(ArrayList<InfracaoTransporte> talonario) {
		return getSyncData(talonario, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<InfracaoTransporte> talonario, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			String sql = "SELECT * FROM mob_infracao_transporte ORDER BY cd_infracao";

			pstmt = connect.prepareStatement(sql);

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("InfracaoTransporte", Util.resultSetToArrayList(pstmt.executeQuery()));

			return register;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
