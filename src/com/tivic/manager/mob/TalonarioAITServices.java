package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.validation.Validators;

public class TalonarioAITServices {

	/*
	 * Situação do Talão AIT
	 */
	public static final int ST_INATIVA = 0;
	public static final int ST_ATIVA = 1;
	public static final int ST_CONCLUIDO = 2;
	public static final int ST_CONFERIDO = 3;
	public static final int ST_DIVERGENTE = 4;

	/*
	 * Tipo do Talão AIT
	 * 
	 * OBSERVAÇÃO: Manter consistente como o com.tivic.manager.str.TalonarioServices
	 */
	public static int TP_TALONARIO_transporte = 0;
	public static int TP_TALONARIO_ELETRONICO_transporte = 1;
	public static int TP_TALONARIO_TRANSPORTE = 2;
	public static int TP_TALONARIO_ELETRONICO_TRANSPORTE = 3;
	public static int TP_TALONARIO_ADM_MANUAL = 4;
	public static int TP_TALONARIO_BOAT = 5;
	public static int TP_TALONARIO_ELETRONICO_BOAT = 6;
	public static int TP_TALONARIO_RRD = 7;
	public static int TP_TALONARIO_ELETRONICO_RRD = 8;
	public static int TP_TALONARIO_TRRAV = 9;
	public static int TP_TALONARIO_ELETRONICO_TRRAV = 10;
	public static int TP_TALONARIO_TRANSPORTE_NIC = 11;

	public static final String[] tipoTalaoAit = { "Trânsito - Manual", "Trânsito - Eletrônico", "Transporte - Manual",
			"Transporte - Eletrônico", "Adm. Trânsito - Manual", "BOAT - Manual", "BOAT - Eletrônico", "RRD - Manual",
			"RRD - Eletrônico", "TRRAV - Manual", "TRRAV - Eletrônico", "NIC - Manual" };

	public static Result save(TalonarioAIT talonarioAIT) {
		return save(talonarioAIT, false, null);
	}

	public static Result save(TalonarioAIT talonarioAIT, Boolean importacao) {
		return save(talonarioAIT, importacao, null);
	}

	public static Result save(TalonarioAIT talonarioAIT, Boolean importacao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (talonarioAIT == null && !importacao)
				return new Result(-1, "Erro ao salvar. TalonarioAIT é nulo");

			if (talonarioAIT.getNrFinal() < talonarioAIT.getNrInicial() && !importacao) {
				return new Result(-2, "Intervalo do n° das AITs inválido.");
			}

			if (talonarioAIT.getStTalao() != ST_INATIVA && talonarioAIT.getDtEntrega() == null && !importacao) {
				return new Result(-3, "A Data de Entrega do talão deve ser preenchida");
			}

			if (talonarioAIT.getDtEntrega() != null && talonarioAIT.getCdAgente() == 0 && !importacao) {
				return new Result(-4,
						"Não é permitido salvar um talão com Data de Entrega preenchida sem um Agente vinculado");
			}
			/*
			 * Chamadas para funções que comparam os talonários do banco
			 */
			ResultSetMap rsmNrInicial = TalonarioAITServices.verificarNrInicial(talonarioAIT);
			ResultSetMap rsmNrFinal = TalonarioAITServices.verificarNrFinal(talonarioAIT);
			ResultSetMap rsmNrtalao = TalonarioAITServices.verificarNrTalaoRepetido(talonarioAIT);

			int retorno;
			if (talonarioAIT.getCdTalao() == 0 || importacao) {

				/*
				 * Validações para uma novo talonário AIT
				 */
				if ((rsmNrInicial.getLines().size() != 0 || rsmNrFinal.getLines().size() != 0)
						&& rsmNrtalao.getLines().size() != 0 && !importacao) {
					return new Result(-5, "Intervalo do n° das AITs inválido. O N° do talão informado já está em uso");
				}

				if ((rsmNrInicial.getLines().size() != 0 || rsmNrFinal.getLines().size() != 0) && !importacao) {
					return new Result(-6, "Intervalo do n° das AITs inválido.");
				}

				if (rsmNrtalao.getLines().size() != 0 && !importacao) {
					return new Result(-7, "O N° do talão informado já está em uso");
				}

				/*
				 * Salva o novo talonário
				 */
				if (talonarioAIT.getCdAgente() != 0) {
					talonarioAIT.setStTalao(ST_ATIVA);
				}
				retorno = TalonarioAITDAO.insert(talonarioAIT, connect);
				talonarioAIT.setCdTalao(retorno);
			} else {
				/*
				 * Validações para uma edição de talonário AIT
				 */
				rsmNrtalao.next();

				if ((rsmNrInicial.getLines().size() != 0 || rsmNrFinal.getLines().size() != 0)
						&& rsmNrtalao.getLines().size() != 1 && rsmNrtalao.getInt("CD_TALAO") != talonarioAIT.getCdTalao()) {
					return new Result(-8, "Intervalo do n° das AITs inválido. O N° do talão informado já está em uso");
				}

				if ((rsmNrInicial.getLines().size() != 0 || rsmNrFinal.getLines().size() != 0)
						&& rsmNrtalao.getInt("CD_TALAO") != talonarioAIT.getCdTalao()) {
					return new Result(-9, "Intervalo do n° das AITs inválido.");
				}

				if (rsmNrtalao.getLines().size() != 0 && rsmNrtalao.getInt("CD_TALAO") != talonarioAIT.getCdTalao()) {
					return new Result(-10, "O N° do talão informado já está em uso");
				}

				/*
				 * Atualiza o novo talonário
				 */
				if (talonarioAIT.getCdAgente() != 0) {
					talonarioAIT.setStTalao(ST_ATIVA);
				}
				retorno = TalonarioAITDAO.update(talonarioAIT, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "TALONARIOAIT",
					talonarioAIT);
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

	public static Result remove(int cdTalao) {
		return remove(cdTalao, false, null);
	}

	public static Result remove(int cdTalao, boolean cascade) {
		return remove(cdTalao, cascade, null);
	}

	public static Result remove(int cdTalao, boolean cascade, Connection connect) {
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
				retorno = TalonarioAITDAO.delete(cdTalao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_talonario");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getMaiorNumeracaoTransporte() {
		return getMaiorNumeracaoTransporte(null);
	}

	public static ResultSetMap getMaiorNumeracaoTransporte(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			pstmt = connect
					.prepareStatement("SELECT max(nr_final)+1 AS nr_inicial, max(nr_talao)+1 AS nr_talao FROM mob_talonario "
							+ " WHERE tp_talao = " + TP_TALONARIO_ELETRONICO_TRANSPORTE);

			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findNrTalaoDuplicado(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {

		String sqlTaloes = "";

		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("ST_TALAO") && criterios.get(i).getValue().equals("-1")) {
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("TP_TALAO")
					&& criterios.get(i).getValue().equals("-1")) {
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("lgColetivoUrbano")) {
				sqlTaloes = " AND (TP_TALAO = " + TP_TALONARIO_ELETRONICO_TRANSPORTE + " OR TP_TALAO = "
						+ TP_TALONARIO_TRANSPORTE + ") ";
				criterios.remove(i);
				i--;
				continue;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("lgTaxi")) {
				sqlTaloes = " AND (TP_TALAO = " + TP_TALONARIO_ELETRONICO_TRANSPORTE + " OR TP_TALAO = "
						+ TP_TALONARIO_TRANSPORTE_NIC + ") ";
				criterios.remove(i);
				i--;
				continue;
			}
		}

		String sql = " SELECT A.*,B.*, B.nm_agente AS NM_AGENTE " + " FROM mob_talonario A "
				+ " LEFT OUTER JOIN mob_agente B on ( A.cd_agente = B.cd_agente ) " + " WHERE 1 = 1 " + sqlTaloes;

		ResultSetMap rsm = Search.find(sql, " GROUP BY A.tp_talao, A.cd_talao, B.cd_agente ORDER BY nr_talao ", criterios,
				connect != null ? connect : Conexao.conectar(), connect == null);
		return rsm;
	}

	public static ResultSetMap verificarNrInicial(TalonarioAIT talonarioAIT) {
		Connection connect = Conexao.conectar();
		try {
			int nrInicial = talonarioAIT.getNrInicial();
			String sql;

			if (talonarioAIT.getTpTalao() == TP_TALONARIO_ELETRONICO_TRANSPORTE
					|| talonarioAIT.getTpTalao() == TP_TALONARIO_TRANSPORTE) {
				sql = " SELECT * FROM mob_talonario " + " WHERE " + nrInicial
						+ " BETWEEN nr_inicial AND nr_final AND ( tp_talao = " + TP_TALONARIO_ELETRONICO_TRANSPORTE
						+ " OR tp_talao = " + TP_TALONARIO_TRANSPORTE + ")";
			} else {
				sql = " SELECT * FROM mob_talonario " + " WHERE " + nrInicial
						+ " BETWEEN nr_inicial AND nr_final AND tp_talao = " + talonarioAIT.getTpTalao();
			}

			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap verificarNrFinal(TalonarioAIT talonarioAIT) {
		Connection connect = Conexao.conectar();
		try {
			int nrFinal = talonarioAIT.getNrFinal();
			String sql;

			if (talonarioAIT.getTpTalao() == TP_TALONARIO_ELETRONICO_TRANSPORTE
					|| talonarioAIT.getTpTalao() == TP_TALONARIO_TRANSPORTE) {
				sql = " SELECT * FROM mob_talonario " + " WHERE " + nrFinal
						+ " BETWEEN nr_inicial AND nr_final AND ( tp_talao = " + TP_TALONARIO_ELETRONICO_TRANSPORTE
						+ " OR tp_talao = " + TP_TALONARIO_TRANSPORTE + ")";
			} else {
				sql = " SELECT * FROM mob_talonario " + " WHERE " + nrFinal
						+ " BETWEEN nr_inicial AND nr_final AND tp_talao = " + talonarioAIT.getTpTalao();
			}

			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap verificarNrTalaoRepetido(TalonarioAIT talonarioAIT) {
		Connection connect = Conexao.conectar();
		try {

			String sql = "SELECT * FROM mob_talonario " + " WHERE nr_talao = " + talonarioAIT.getNrTalao()
					+ " AND tp_talao = " + talonarioAIT.getTpTalao();

			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap agenteVinculadoTalao(String nrAit) {
		return agenteVinculadoTalao(nrAit, -1);
	}

	public static ResultSetMap agenteVinculadoTalao(String nrAit, int tpTalao) {
		Connection connect = Conexao.conectar();
		try {

			String sql = " SELECT A.*, B.* " + " FROM mob_talonario A "
					+ " LEFT OUTER JOIN mob_agente B on ( A.cd_agente = B.cd_agente) " + " WHERE (" + nrAit
					+ " BETWEEN A.nr_inicial AND A.nr_final ) AND A.st_talao = " + ST_ATIVA
					+ (tpTalao == -1 ? "" : "AND A.tp_talao = " + tpTalao);

			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap validarAplicacaoAit(String nrAit, int tpTalao) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisa somente os talonário com status de Ativo(Entregue)
			return new ResultSetMap(connect.prepareStatement(" SELECT A.*, B.* " + " FROM mob_talonario A "
					+ " LEFT OUTER JOIN mob_agente B on ( A.cd_agente = B.cd_agente) " + " WHERE " + nrAit
					+ " BETWEEN A.nr_inicial AND A.nr_final AND tp_talao = " + tpTalao + " AND A.st_talao = " + ST_ATIVA)
					.executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap validarAgenteTalaoAit(String nrAit, int cdAgente, int tpTalao) {
		Connection connect = Conexao.conectar();
		try {
			// Pesquisa somente os talonário com status de Ativo(Entregue) que pertence ao
			// agente do parâmetro
			return new ResultSetMap(connect.prepareStatement(" SELECT A.*, B.* " + " FROM mob_talonario A "
					+ " LEFT OUTER JOIN mob_agente B on ( A.cd_agente = B.cd_agente) " + " WHERE " + nrAit
					+ " BETWEEN A.nr_inicial AND A.nr_final AND tp_talao = " + tpTalao + " AND A.st_talao = " + ST_ATIVA
					+ " AND A.cd_agente = " + cdAgente).executeQuery());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static int[] getGrupoTalao(int tpTalonario) {

		int[] transporte = { 2, 3 };

		if (contains(transporte, tpTalonario)) {
			return transporte;
		}

		return null;

	}

	private static boolean contains(int[] array, int obj) {

		boolean result = false;

		for (int i : array) {
			if (i == obj) {
				result = true;
				break;
			}
		}

		return result;
	}
	
	private static Validators<?> getSaveValidators(Talonario talonario) {		
		if(talonario.getCdTalao() > 0) {
			System.out.println("UPDATE");
			return new Validators<Talonario>(talonario).put(new TalonarioUpdateValidator());
		} else {
			return new Validators<Talonario>(talonario).put(new TalonarioInsertValidator());
		}
	}
	

}
