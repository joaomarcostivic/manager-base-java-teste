package com.tivic.manager.hlp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

@DestinationConfig(enabled = false)
public class TopicoServices {

	public static ResultSetMap getAll() {
		return getAll(null, 0, null);
	}

	public static ResultSetMap getAll(int idTopicoExclude) {
		return getAll(null, idTopicoExclude, null);
	}

	public static ResultSetMap getAll(ResultSetMap rsmParent, int idTopicoExclude, Connection connect) {
		boolean isConnectNull = connect == null;
		try {
			if (isConnectNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt;
			if (rsmParent == null) {
				String sql =
						" SELECT A.*," +
						"        B.nm_login AS nm_login_criacao," +
						"        C.nm_login AS nm_login_alteracao," +
						"        D.nm_login AS nm_login_revisao" +
						" FROM hlp_topico A" +
						"      LEFT JOIN seg_usuario B ON (A.cd_usuario_criacao = B.cd_usuario)" +
						"      LEFT JOIN seg_usuario C ON (A.cd_usuario_alteracao = C.cd_usuario)" +
						"      LEFT JOIN seg_usuario D ON (A.cd_usuario_revisao = D.cd_usuario)" +
						" WHERE cd_topico <> ?" +
						"       AND cd_topico_superior IS NULL" +
						" ORDER BY nr_ordem";

				pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, idTopicoExclude);
				rsmParent = new ResultSetMap(pstmt.executeQuery());
				getAll(rsmParent, idTopicoExclude, connect);
			}
			else {
				if (rsmParent.next()) {
					String sql =
						" SELECT A.*," +
						"        B.nm_login AS nm_login_criacao," +
						"        C.nm_login AS nm_login_alteracao," +
						"        D.nm_login AS nm_login_revisao" +
						" FROM hlp_topico A" +
						"      LEFT JOIN seg_usuario B ON (A.cd_usuario_criacao = B.cd_usuario)" +
						"      LEFT JOIN seg_usuario C ON (A.cd_usuario_alteracao = C.cd_usuario)" +
						"      LEFT JOIN seg_usuario D ON (A.cd_usuario_revisao = D.cd_usuario)" +
						" WHERE cd_topico <> ?" +
						"       AND cd_topico_superior = ?" +
						" ORDER BY nr_ordem";

					pstmt = connect.prepareStatement(sql);
					pstmt.setInt(1, idTopicoExclude);
					pstmt.setInt(2, rsmParent.getInt("cd_topico", 0));
					ResultSetMap rsmChild = new ResultSetMap(pstmt.executeQuery());
					rsmParent.setValueToField("subResultSetMap", rsmChild);
					getAll(rsmChild, idTopicoExclude, connect);
					getAll(rsmParent, idTopicoExclude, connect);
				}
			}

			if (isConnectNull)
				connect.commit();

			return rsmParent;
		}
		catch (SQLException e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insert (Topico topico) {
		return insert(topico, null);
	}

	public static int insert (Topico topico, Connection connect) {
		boolean isConnectNull = connect == null;
		try {
			if (isConnectNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int nrOrdem;
			if (topico.getCdTopicoSuperior()>0) {
				if (topico.getNrOrdem() >0)
					nrOrdem = topico.getNrOrdem();
				else {
					PreparedStatement pstmt = connect.prepareStatement("SELECT MAX(nr_ordem) FROM hlp_topico WHERE cd_topico_superior = ?");
					pstmt.setInt(1, topico.getCdTopicoSuperior());
					ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
					nrOrdem = rsm.next() ? rsm.getInt("max", 0)+1 : 1;
				}

				PreparedStatement pstmtUp = connect.prepareStatement("UPDATE HLP_TOPICO SET nr_ordem = ? WHERE cd_topico_superior = ?");
				pstmtUp.setInt(1, nrOrdem);
				pstmtUp.setInt(2, topico.getCdTopicoSuperior());
				pstmtUp.executeUpdate();
			}
			else {
				if (topico.getNrOrdem() >0)
					nrOrdem = topico.getNrOrdem();
				else {
					PreparedStatement pstmt = connect.prepareStatement("SELECT MAX(nr_ordem) FROM hlp_topico WHERE cd_topico_superior IS NULL");
					ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
					nrOrdem = rsm.next() ? rsm.getInt("nr_ordem", 0)+1 : 1;
				}

				PreparedStatement pstmtUp = connect.prepareStatement("UPDATE HLP_TOPICO SET nr_ordem = ? WHERE cd_topico_superior IS NULL");
				pstmtUp.setInt(1, nrOrdem);
				pstmtUp.executeUpdate();
			}

			topico.setNrOrdem(nrOrdem);
			int cdTopico = TopicoDAO.insert(topico);

			if (isConnectNull)
				connect.commit();

			return cdTopico;
		}
		catch (SQLException e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoServices.insert: " + e);
			return -1;
		}
	}

	public static int update (Topico topico) {
		return update(topico, null);
	}

	public static int update (Topico topico, Connection connect) {
		boolean isConnectNull = connect == null;
		try {
			if (isConnectNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int nrOrdem;
			if (topico.getCdTopicoSuperior()>0) {
				if (topico.getNrOrdem() >0)
					nrOrdem = topico.getNrOrdem();
				else {
					PreparedStatement pstmt = connect.prepareStatement("SELECT MAX(nr_ordem) FROM hlp_topico WHERE cd_topico_superior = ?");
					pstmt.setInt(1, topico.getCdTopicoSuperior());
					ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
					nrOrdem = rsm.next() ? rsm.getInt("max", 0)+1 : 1;
				}

				PreparedStatement pstmtUp = connect.prepareStatement("UPDATE HLP_TOPICO SET nr_ordem = ? WHERE cd_topico_superior = ?");
				pstmtUp.setInt(1, nrOrdem);
				pstmtUp.setInt(2, topico.getCdTopicoSuperior());
				pstmtUp.executeUpdate();
			}
			else {
				if (topico.getNrOrdem() >0)
					nrOrdem = topico.getNrOrdem();
				else {
					PreparedStatement pstmt = connect.prepareStatement("SELECT MAX(nr_ordem) FROM hlp_topico WHERE cd_topico_superior IS NULL");
					ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
					nrOrdem = rsm.next() ? rsm.getInt("nr_ordem", 0)+1 : 1;
				}

				PreparedStatement pstmtUp = connect.prepareStatement("UPDATE HLP_TOPICO SET nr_ordem = ? WHERE cd_topico_superior IS NULL");
				pstmtUp.setInt(1, nrOrdem);
				pstmtUp.executeUpdate();
			}

			topico.setNrOrdem(nrOrdem);
			int cdTopico = TopicoDAO.update(topico);

			if (isConnectNull)
				connect.commit();

			return cdTopico;
		}
		catch (SQLException e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoServices.insert: " + e);
			return -1;
		}
	}

	public static int save (Topico topico) {
		return save(topico, null);
	}

	public static int save (Topico topico, Connection connect) {
		if (topico.getCdTopico() > 0)
			return update(topico, connect);
		else
			return insert(topico, connect);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectNull = connect == null;
		try {
			if (isConnectNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			String sql =
					" SELECT A.*," +
					"        B.nm_login AS nm_login_criacao," +
					"        C.nm_login AS nm_login_alteracao," +
					"        D.nm_login AS nm_login_revisao" +
					" FROM hlp_topico A" +
					"      LEFT JOIN seg_usuario B ON (A.cd_usuario_criacao = B.cd_usuario)" +
					"      LEFT JOIN seg_usuario C ON (A.cd_usuario_alteracao = C.cd_usuario)" +
					"      LEFT JOIN seg_usuario D ON (A.cd_usuario_revisao = D.cd_usuario)";

			ResultSetMap rsm = Search.find(sql, " ORDER BY nm_titulo ", criterios, connect, false, true);

			if (isConnectNull)
				connect.commit();

			return rsm;
		}
		catch (SQLException e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectNull)
				Conexao.desconectar(connect);
		}
	}
}
