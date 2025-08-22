package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.mail.Address;
import com.tivic.manager.util.mail.Authenticator;
import com.tivic.manager.util.mail.Mailer;
import com.tivic.manager.util.mail.enums.SecurityType;
import com.tivic.manager.util.mail.providers.MailerProvider;

public class BoatDeclaranteServices {

	public static final int TP_CONDUTOR = 1;
	public static final int TP_CONDUTOR_E_PROPRIETARIO = 2;
	public static final int TP_PROPRIETARIO = 3;
	public static final int TP_TERCEIRO_ENVOLVIDO = 4;

	public static Result save(BoatDeclarante boatDeclarante) {
		return save(boatDeclarante, null, null);
	}

	public static Result save(BoatDeclarante boatDeclarante, AuthData authData) {
		return save(boatDeclarante, authData, null);
	}

	public static Result save(BoatDeclarante boatDeclarante, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (boatDeclarante == null)
				return new Result(-1, "Erro ao salvar. BoatDeclarante é nulo");

			int retorno;
			if (BoatDeclaranteDAO.get(boatDeclarante.getCdBoat(), boatDeclarante.getCdDeclarante(), connect) == null) {
				retorno = BoatDeclaranteDAO.insert(boatDeclarante, connect);
//				boatDeclarante.setCdBoat(retorno);
			} else {
				retorno = BoatDeclaranteDAO.update(boatDeclarante, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "BOATDECLARANTE",
					boatDeclarante);
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

	public static Result remove(BoatDeclarante boatDeclarante) {
		return remove(boatDeclarante.getCdBoat(), boatDeclarante.getCdDeclarante());
	}

	public static Result remove(int cdBoat, int cdDeclarante) {
		return remove(cdBoat, cdDeclarante, false, null, null);
	}

	public static Result remove(int cdBoat, int cdDeclarante, boolean cascade) {
		return remove(cdBoat, cdDeclarante, cascade, null, null);
	}

	public static Result remove(int cdBoat, int cdDeclarante, boolean cascade, AuthData authData) {
		return remove(cdBoat, cdDeclarante, cascade, authData, null);
	}

	public static Result remove(int cdBoat, int cdDeclarante, boolean cascade, AuthData authData, Connection connect) {
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
				retorno = BoatDeclaranteDAO.delete(cdBoat, cdDeclarante, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_declarante");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatDeclaranteServices.getAll: " + e);
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
		return Search.find(
				"SELECT * FROM mob_boat_declarante A, mob_declarante B, mob_declarante_endereco C WHERE A.cd_declarante = B.cd_declarante AND A.cd_declarante = C.cd_declarante",
				criterios, connect != null ? connect : Conexao.conectar());
	}

}