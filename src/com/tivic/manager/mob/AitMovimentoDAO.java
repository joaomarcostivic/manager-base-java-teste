package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class AitMovimentoDAO {

	public static int insert(AitMovimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitMovimento objeto, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String, Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String, Object>();
			keys[0].put("FIELD_NAME", "cd_movimento");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String, Object>();
			keys[1].put("FIELD_NAME", "cd_ait");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			int code = Conexao.getSequenceCode("mob_ait_movimento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMovimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_movimento (cd_movimento,"
					+ "cd_ait," 
					+ "nr_movimento," 
					+ "dt_movimento," 
					+ "nr_remessa," 
					+ "tp_status," 
					+ "tp_arquivo,"
					+ "ds_observacao," 
					+ "cd_ocorrencia," 
					+ "lg_enviado_detran," 
					+ "st_entrega," 
					+ "nr_processo,"
					+ "dt_registro_detran," 
					+ "st_recurso," 
					+ "nr_sequencial," 
					+ "nr_erro," 
					+ "dt_digitacao,"
					+ "lg_cancela_movimento," 
					+ "dt_cancelamento," 
					+ "nr_remessa_registro," 
					+ "dt_primeiro_registro,"
					+ "st_registro_detran," 
					+ "cd_processo,"
					+ "cd_usuario," 
					+ "cd_conta_receber," 
					+ "dt_publicacao_do," 
					+ "st_publicacao_do," 
					+ "dt_adesao_sne,"
					+ "st_adesao_sne,"
					+ "cd_movimento_cancelamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if (objeto.getCdAit() == 0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, objeto.getCdAit());
			pstmt.setInt(3, objeto.getNrMovimento());
			if (objeto.getDtMovimento() == null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4, new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(5, objeto.getNrRemessa());
			pstmt.setInt(6, objeto.getTpStatus());
			pstmt.setInt(7, objeto.getTpArquivo());
			pstmt.setString(8, objeto.getDsObservacao());
			pstmt.setInt(9, objeto.getCdOcorrencia());
			pstmt.setInt(10, objeto.getLgEnviadoDetran());
			pstmt.setInt(11, objeto.getStEntrega());
			pstmt.setString(12, objeto.getNrProcesso());
			if (objeto.getDtRegistroDetran() == null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13, new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(14, objeto.getStRecurso());
			pstmt.setInt(15, objeto.getNrSequencial());
			pstmt.setString(16, objeto.getNrErro());
			if (objeto.getDtDigitacao() == null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17, new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			pstmt.setInt(18, objeto.getLgCancelaMovimento());
			if (objeto.getDtCancelamento() == null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19, new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			pstmt.setInt(20, objeto.getNrRemessaRegistro());
			if (objeto.getDtPrimeiroRegistro() == null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21, new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			pstmt.setInt(22, objeto.getStRegistroDetran());
			if (objeto.getCdProcesso() == 0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23, objeto.getCdProcesso());
			if (objeto.getCdUsuario() == 0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24, objeto.getCdUsuario());
			if (objeto.getCdContaReceber() == 0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25, objeto.getCdContaReceber());
			if (objeto.getDtPublicacaoDo() == null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26, new Timestamp(objeto.getDtPublicacaoDo().getTimeInMillis()));
			pstmt.setInt(27, objeto.getStPublicacaoDo());
			if (objeto.getDtAdesaoSne() == null)
				pstmt.setNull(28, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(28, new Timestamp(objeto.getDtAdesaoSne().getTimeInMillis()));
			pstmt.setInt(29, objeto.getStAdesaoSne());
			if (objeto.getCdMovimentoCancelamento() == 0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30, objeto.getCdMovimentoCancelamento());
			pstmt.executeUpdate();
			return code;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.insert: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.insert: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitMovimento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitMovimento objeto, int cdMovimentoOld, int cdAitOld) {
		return update(objeto, cdMovimentoOld, cdAitOld, null);
	}

	public static int update(AitMovimento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitMovimento objeto, int cdMovimentoOld, int cdAitOld, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_movimento SET cd_movimento=?,"
					+ "cd_ait=?," + "nr_movimento=?," + "dt_movimento=?," + "nr_remessa=?," + "tp_status=?,"
					+ "tp_arquivo=?," + "ds_observacao=?," + "cd_ocorrencia=?," + "lg_enviado_detran=?,"
					+ "st_entrega=?," + "nr_processo=?," + "dt_registro_detran=?," + "st_recurso=?,"
					+ "nr_sequencial=?," + "nr_erro=?," + "dt_digitacao=?," + "lg_cancela_movimento=?,"
					+ "dt_cancelamento=?," + "nr_remessa_registro=?," + "dt_primeiro_registro=?,"
					+ "st_registro_detran=?," + "cd_processo=?,"
					+ "cd_usuario=?," + "cd_conta_receber=?," + "dt_publicacao_do=?," + "st_publicacao_do=?,"
					+ "dt_adesao_sne=?," + "st_adesao_sne=?," + "cd_movimento_cancelamento=?  WHERE cd_movimento=? AND cd_ait=?");
			pstmt.setInt(1, objeto.getCdMovimento());
			pstmt.setInt(2, objeto.getCdAit());
			pstmt.setInt(3, objeto.getNrMovimento());
			if (objeto.getDtMovimento() == null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4, new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setInt(5, objeto.getNrRemessa());
			pstmt.setInt(6, objeto.getTpStatus());
			pstmt.setInt(7, objeto.getTpArquivo());
			pstmt.setString(8, objeto.getDsObservacao());
			pstmt.setInt(9, objeto.getCdOcorrencia());
			pstmt.setInt(10, objeto.getLgEnviadoDetran());
			pstmt.setInt(11, objeto.getStEntrega());
			pstmt.setString(12, objeto.getNrProcesso());
			if (objeto.getDtRegistroDetran() == null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13, new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(14, objeto.getStRecurso());
			pstmt.setInt(15, objeto.getNrSequencial());
			pstmt.setString(16, objeto.getNrErro());
			if (objeto.getDtDigitacao() == null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17, new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			pstmt.setInt(18, objeto.getLgCancelaMovimento());
			if (objeto.getDtCancelamento() == null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19, new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			pstmt.setInt(20, objeto.getNrRemessaRegistro());
			if (objeto.getDtPrimeiroRegistro() == null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21, new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			pstmt.setInt(22, objeto.getStRegistroDetran());
			if (objeto.getCdProcesso() == 0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23, objeto.getCdProcesso());
			if (objeto.getCdUsuario() == 0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24, objeto.getCdUsuario());
			if (objeto.getCdContaReceber() == 0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25, objeto.getCdContaReceber());
			if (objeto.getDtPublicacaoDo() == null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26, new Timestamp(objeto.getDtPublicacaoDo().getTimeInMillis()));
			pstmt.setInt(27, objeto.getStPublicacaoDo());
			if (objeto.getDtAdesaoSne() == null)
				pstmt.setNull(28, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(28, new Timestamp(objeto.getDtAdesaoSne().getTimeInMillis()));
			pstmt.setInt(29, objeto.getStAdesaoSne());
			if (objeto.getCdMovimentoCancelamento() == 0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30, objeto.getCdMovimentoCancelamento());
			pstmt.setInt(31, cdMovimentoOld != 0 ? cdMovimentoOld : objeto.getCdMovimento());
			pstmt.setInt(32, cdAitOld != 0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.update: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.update: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMovimento, int cdAit) {
		return delete(cdMovimento, cdAit, null);
	}

	public static int delete(int cdMovimento, int cdAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("DELETE FROM mob_ait_movimento WHERE cd_movimento=? AND cd_ait=?");
			pstmt.setInt(1, cdMovimento);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.delete: " + sqlExpt);
			return (-1) * sqlExpt.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.delete: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitMovimento get(int cdMovimento, int cdAit) {
		return get(cdMovimento, cdAit, null);
	}

	public static AitMovimento get(int cdMovimento, int cdAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_movimento=? AND cd_ait=?");
			pstmt.setInt(1, cdMovimento);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new AitMovimento(rs.getInt("cd_movimento"), rs.getInt("cd_ait"), rs.getInt("nr_movimento"),
						(rs.getTimestamp("dt_movimento") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("nr_remessa"), rs.getInt("tp_status"), rs.getInt("tp_arquivo"),
						rs.getString("ds_observacao"), rs.getInt("cd_ocorrencia"), rs.getInt("lg_enviado_detran"),
						rs.getInt("st_entrega"), rs.getString("nr_processo"),
						(rs.getTimestamp("dt_registro_detran") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
						rs.getInt("st_recurso"), rs.getInt("nr_sequencial"), rs.getString("nr_erro"),
						(rs.getTimestamp("dt_digitacao") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						rs.getInt("lg_cancela_movimento"),
						(rs.getTimestamp("dt_cancelamento") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						rs.getInt("nr_remessa_registro"),
						(rs.getTimestamp("dt_primeiro_registro") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
						rs.getInt("st_registro_detran"), 
						rs.getInt("cd_processo"), rs.getInt("cd_usuario"), rs.getInt("cd_conta_receber"),
						(rs.getTimestamp("dt_publicacao_do") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_publicacao_do").getTime()),
						rs.getInt("st_publicacao_do"),
						(rs.getTimestamp("dt_adesao_sne") == null) ? null : Util.longToCalendar(rs.getTimestamp("dt_adesao_sne").getTime()),
						rs.getInt("st_adesao_sne"),
						rs.getInt("cd_movimento_cancelamento"));
			} else {
				return null;
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getByStatus(int cdAit, int tpStatus) {
		return getByStatus(cdAit, tpStatus, null);
	}
	
	public static AitMovimento getByStatus(int cdAit, int tpStatus, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait =? AND tp_status=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, tpStatus);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new AitMovimento(rs.getInt("cd_movimento"), rs.getInt("cd_ait"), rs.getInt("nr_movimento"),
						(rs.getTimestamp("dt_movimento") == null) ? null
								: Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("nr_remessa"), rs.getInt("tp_status"), rs.getInt("tp_arquivo"),
						rs.getString("ds_observacao"), rs.getInt("cd_ocorrencia"), rs.getInt("lg_enviado_detran"),
						rs.getInt("st_entrega"), rs.getString("nr_processo"),
						(rs.getTimestamp("dt_registro_detran") == null) ? null
								: Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
						rs.getInt("st_recurso"), rs.getInt("nr_sequencial"), rs.getString("nr_erro"),
						(rs.getTimestamp("dt_digitacao") == null) ? null
								: Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						rs.getInt("lg_cancela_movimento"),
						(rs.getTimestamp("dt_cancelamento") == null) ? null
								: Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						rs.getInt("nr_remessa_registro"),
						(rs.getTimestamp("dt_primeiro_registro") == null) ? null
								: Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
						rs.getInt("st_registro_detran"),
						rs.getInt("cd_processo"), rs.getInt("cd_usuario"), rs.getInt("cd_conta_receber"),
						(rs.getTimestamp("dt_publicacao_do") == null) ? null
								: Util.longToCalendar(rs.getTimestamp("dt_publicacao_do").getTime()),
						rs.getInt("st_publicacao_do"),
						(rs.getTimestamp("dt_adesao_sne") == null) ? null
								: Util.longToCalendar(rs.getTimestamp("dt_adesao_sne").getTime()),
						rs.getInt("st_adesao_sne"),
						rs.getInt("cd_movimento_cancelamento"));
			} else {
				return new AitMovimento();
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.get: " + e);
			return null;
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitMovimento> getList() {
		return getList(null);
	}

	public static ArrayList<AitMovimento> getList(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitMovimento> list = new ArrayList<AitMovimento>();
			ResultSetMap rsm = getAll(connect);
			while (rsm.next()) {
				AitMovimento obj = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.getList: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitMovimento getByAitDocumento(int cdDocumento) {
		return getByAitDocumento(cdDocumento, null);
	}

	public static AitMovimento getByAitDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_documento=?");
			pstmt.setInt(1, cdDocumento);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return registerTo(rs);
			} else {
				return null;
			}
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitMovimento registerTo(ResultSet rs) {
		try {
			return new AitMovimento(rs.getInt("cd_movimento"), rs.getInt("cd_ait"), rs.getInt("nr_movimento"),
					(rs.getTimestamp("dt_movimento") == null) ? null
							: Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
					rs.getInt("nr_remessa"), rs.getInt("tp_status"), rs.getInt("tp_arquivo"),
					rs.getString("ds_observacao"), rs.getInt("cd_ocorrencia"), rs.getInt("lg_enviado_detran"),
					rs.getInt("st_entrega"), rs.getString("nr_processo"),
					(rs.getTimestamp("dt_registro_detran") == null) ? null
							: Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
					rs.getInt("st_recurso"), rs.getInt("nr_sequencial"), rs.getString("nr_erro"),
					(rs.getTimestamp("dt_digitacao") == null) ? null
							: Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
					rs.getInt("lg_cancela_movimento"),
					(rs.getTimestamp("dt_cancelamento") == null) ? null
							: Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
					rs.getInt("nr_remessa_registro"),
					(rs.getTimestamp("dt_primeiro_registro") == null) ? null
							: Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
					rs.getInt("st_registro_detran"),
					rs.getInt("cd_processo"), rs.getInt("cd_usuario"), rs.getInt("cd_conta_receber"),
					(rs.getTimestamp("dt_publicacao_do") == null) ? null
							: Util.longToCalendar(rs.getTimestamp("dt_publicacao_do").getTime()),
					rs.getInt("st_publicacao_do"),
					(rs.getTimestamp("dt_adesao_sne") == null) ? null
							: Util.longToCalendar(rs.getTimestamp("dt_adesao_sne").getTime()),
					rs.getInt("st_adesao_sne"),
					rs.getInt("cd_movimento_cancelamento"));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoDAO.get: " + e);
			return null;
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_ait_movimento", criterios, connect != null ? connect : Conexao.conectar(),
				connect == null);
	}

}