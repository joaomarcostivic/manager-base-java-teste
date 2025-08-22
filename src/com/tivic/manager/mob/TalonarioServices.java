package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.manager.validation.Validators;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TalonarioServices {

	public static int TP_TALONARIO_TRANSITO = 0;
	public static int TP_TALONARIO_ELETRONICO_TRANSITO = 1;
	public static int TP_TALONARIO_TRANSPORTE = 2;
	public static int TP_TALONARIO_ELETRONICO_TRANSPORTE = 3;
	public static int TP_TALONARIO_NIC = 4;
	public static int TP_TALONARIO_BOAT = 5;
	public static int TP_TALONARIO_ELETRONICO_BOAT = 6;
	public static int TP_TALONARIO_RRD = 7;
	public static int TP_TALONARIO_ELETRONICO_RRD = 8;
	public static int TP_TALONARIO_TRRAV = 9;
	public static int TP_TALONARIO_ELETRONICO_TRRAV = 10;
	public static int TP_TALONARIO_TRANSPORTE_NIC = 11;

	public static int TP_TALONARIO_VIDEO_MONITORAMENTO = 12;
	public static int TP_TALONARIO_RADAR_ESTATICO = 13;
	public static int TP_TALONARIO_RADAR_FIXO = 14;
	public static int TP_TALONARIO_ZONA_AZUL = 15;

	public static int ST_TALAO_INATIVO = 0;
	public static int ST_TALAO_ATIVO = 1;
	public static int ST_TALAO_CONCLUIDO = 2;
	public static int ST_TALAO_CONFERIDO = 3;
	public static int ST_TALAO_DIVERGENTE = 4;
	public static int ST_TALAO_PENDENTE = 5;

	public static Result save(Talonario talonario) {
		return save(talonario, null, null);
	}

	public static Result save(Talonario talonario, AuthData authData) {
		return save(talonario, authData, null);
	}

	public static Result save(Talonario talonario, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (talonario == null)
				return new Result(-1, "Erro ao salvar. Talonario é nulo");

			if (Util.isStrBaseAntiga()) {
				return com.tivic.manager.str.TalonarioServices.save(toStrTalonario(talonario), authData, connect);
			}

			int retorno;
			if (talonario.getCdTalao() == 0) {
				retorno = TalonarioDAO.insert(talonario, connect);
				talonario.setCdTalao(retorno);
			} else {
				Talonario temp = TalonarioDAO.get(talonario.getCdTalao());

				if (temp.getCdAgente() <= 0) {
					retorno = TalonarioDAO.update(talonario, connect);
				} else {
					retorno = -1;
					return new Result(-1, "Erro ao salvar. Talonário já tem agente, não pode ser editado.");
				}
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "TALONARIO",
					talonario);

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

	public static int updateNrUltimoAit(Talonario talonario, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt;

			pstmt = connect.prepareStatement("UPDATE mob_talonario SET " + "nr_ultimo_ait=? WHERE cd_talao=?");

			pstmt.setInt(1, talonario.getNrUltimoAit());
			pstmt.setInt(2, talonario.getCdTalao());
			pstmt.executeUpdate();
			return 1;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioServices.updateNrUltimoAit: " + e);
			return -1;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result save(Talonario talonario, AuthData authData, Validators<?> validators, Connection connect)
			throws Exception, ValidationException, SQLException {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (talonario == null)
				throw new ValidationException("Talonário é nulo !");

			if (Util.isStrBaseAntiga()) {
				return com.tivic.manager.str.TalonarioServices.save(toStrTalonario(talonario), authData, connect);
			}

			int retorno;

			Optional<String> error = validators.validateAll();
			if (error.isPresent())
				throw new ValidationException("Erro ao salvar: " + error.get());

			if (talonario.getCdTalao() == 0) {
				retorno = TalonarioDAO.insert(talonario, connect);
				if(retorno <= 0) {
					throw new Exception("Erro ao inserir Talonário");
				}
				connect.commit();
				talonario.setCdTalao(retorno);
			} else {
				retorno = TalonarioDAO.update(talonario, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "TALONARIO",
					talonario);

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

	public static Validators<?> getSaveTransitoValidators(Talonario talonario) {
		if (talonario.getCdTalao() > 0) {
			System.out.println("UPDATE");
					if(talonario.getTpTalao() != TipoTalaoEnum.TP_TALONARIO_TRANSITO.getKey()) {
						return new Validators<Talonario>(talonario)
						.put(new AgenteTransitoValidator())
						.put(new TalonarioUpdateValidator());
					}
					return new Validators<Talonario>(talonario)
					.put(new TalonarioUpdateValidator());
		} else {
			if(talonario.getTpTalao() != TipoTalaoEnum.TP_TALONARIO_TRANSITO.getKey()) {
				return new Validators<Talonario>(talonario)
				.put(new AgenteTransitoValidator())
				.put(new TalonarioUpdateValidator());
			}
			return new Validators<Talonario>(talonario)
					.put(new TalonarioInsertValidator());
		}
	}

	public static Validators<?> getSaveTransproteValidators(Talonario talonario) {
		if (talonario.getCdTalao() > 0) {
			System.out.println("UPDATE");
			return new Validators<Talonario>(talonario)
					.put(new TalonarioTransporteValidator())
					.put(new AgenteTransporteValidator())
					.put(new TalonarioUpdateValidator());
		} else {
			return new Validators<Talonario>(talonario)
					.put(new TalonarioTransporteValidator())
					.put(new AgenteTransporteValidator())
					.put(new TalonarioInsertValidator());
		}
	}

	public static Result remove(Talonario talonario) {
		return remove(talonario.getCdTalao());
	}

	public static Result remove(int cdTalao) {
		return remove(cdTalao, false, null, null);
	}

	public static Result remove(int cdTalao, boolean cascade) {
		return remove(cdTalao, cascade, null, null);
	}

	public static Result remove(int cdTalao, boolean cascade, AuthData authData) {
		return remove(cdTalao, cascade, authData, null);
	}

	public static Result remove(int cdTalao, boolean cascade, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (Util.isStrBaseAntiga()) {
				return com.tivic.manager.str.TalonarioServices.remove(cdTalao, cascade, authData, connect);
			}

			int retorno = 0;
			if (cascade) {
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if (!cascade || retorno > 0)
				retorno = TalonarioDAO.delete(cdTalao, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estï¿½ vinculado a outros e nï¿½o pode ser excluï¿½do!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluï¿½do com sucesso!");
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
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); // Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
//			boolean lgGctPadrao = Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");

			if (lgBaseAntiga) {
				pstmt = connect.prepareStatement("SELECT A.*, A.cod_talao AS cd_talao, "
						+ " B.nm_agente, B.cod_agente AS cd_agente " + " FROM talonario A"
						+ " LEFT OUTER JOIN agente B ON (A.cod_agente = B.cod_agente)" + " ORDER BY A.nr_talao DESC");
			} else {
				pstmt = connect.prepareStatement("SELECT A.*, B.nm_agente " + " FROM mob_talonario A"
						+ " LEFT OUTER JOIN mob_agente B ON (A.cd_agente = B.cd_agente)");
			}

			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioServices.getAll: " + e);
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
		if (ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA")) {
			return Search.find(
					" SELECT A.*, A.cod_talao AS cd_talao, " + " B.nm_agente, B.cod_agente AS cd_agente "
							+ " FROM talonario A " + " LEFT OUTER JOIN agente B ON (A.cod_agente = B.cod_agente) ",
					" ORDER BY A.dt_entrega DESC ", criterios, connect != null ? connect : Conexao.conectar(),
					connect == null);
		} else {
			return Search.find(
					" SELECT A.*, " + " B.nm_agente " + " FROM mob_talonario A "
							+ " LEFT OUTER JOIN mob_agente B ON (A.cd_agente = B.cd_agente)",
					" ORDER BY A.dt_entrega DESC ", criterios, connect != null ? connect : Conexao.conectar(),
					connect == null);
		}
	}

//	public static int updateNrUltimoAit(Talonario talonario, Connection connect) {	
//		boolean isConnectionNull = connect==null;
//		try {
//			if (isConnectionNull)
//				connect = Conexao.conectar();
//
//			PreparedStatement pstmt;
//
//			pstmt = connect.prepareStatement("UPDATE mob_talonario SET " + 
//		      		   "nr_ultimo_ait=? WHERE cd_talao=?");
//			
//			pstmt.setInt(1,talonario.getNrUltimoAit());
//			pstmt.setInt(2, talonario.getCdTalao());
//			pstmt.executeUpdate();
//			return 1;
//		}
//		catch(Exception e){
//			e.printStackTrace(System.out);
//			System.err.println("Erro! TalonarioServices.updateNrUltimoAit: " +  e);
//			return -1;
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}

	public static Result getTalonariosMonitoramentoByAgente(Agente agente) {
		return getTalonariosMonitoramentoByAgente(agente, null);
	}

	public static Result getTalonariosMonitoramentoByAgente(Agente agente, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			boolean lgGctPadrao = ManagerConf.getInstance().getAsBoolean("GCT_PADRAO"); // Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
			ArrayList<Talonario> talonarios = new ArrayList<Talonario>();

			String sql = "";

			if (Util.isStrBaseAntiga()) {
				sql = "SELECT * FROM talonario " + "WHERE cod_agente = ? " + "  AND ( tp_talao = "
						+ TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_RADAR_ESTATICO + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_RADAR_FIXO + ")";

				sql += "  AND st_talao = " + TalonarioServices.ST_TALAO_ATIVO +
				// " AND ? <= nr_final " +
						" ORDER BY dt_entrega";
			} else {
				sql = "SELECT * FROM mob_talonario " + "WHERE cd_agente = ? " + "  AND (tp_talao = "
						+ " 	    OR tp_talao = " + TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO
						+ " 	    OR tp_talao = " + TalonarioServices.TP_TALONARIO_RADAR_ESTATICO + " 	    OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_RADAR_FIXO + ")" + "  AND st_talao = "
						+ TalonarioServices.ST_TALAO_ATIVO + " ORDER BY dt_entrega";
			}

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, agente.getCdAgente());

			ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());

			while (rsmTalonario.next()) {
				int nrUltimoAitTalao = 0;

				if (Util.isStrBaseAntiga()) {
					if (lgGctPadrao) {
						sql = "SELECT * FROM AIT WHERE cod_agente = ?";
					} else {
						// Pesquisa o último ait
						if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO
								|| rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO) {
							sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM AIT WHERE nr_ait >=  ? AND nr_ait <= ?";
						}
					}
				} else {
					if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_RADAR_ESTATICO) {
						sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
					} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_RADAR_FIXO) {
						sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
					}
				}

				PreparedStatement pstmt2 = connect.prepareStatement(sql);
				if (lgGctPadrao) {
					pstmt2.setInt(1, agente.getCdAgente());
				} else {
					pstmt2.setInt(1, rsmTalonario.getInt("NR_INICIAL"));
					pstmt2.setInt(2, rsmTalonario.getInt("NR_FINAL"));
				}
				ResultSet rsNr = pstmt2.executeQuery();

				// Ordena e pega o maior nr da AIT Transporte ou No caso do ait no Padrão GCT
				List<Integer> aitsAgente = new ArrayList<Integer>();
				if (Util.isStrBaseAntiga() && lgGctPadrao) {
					if (rsNr.next()) {
						String nrAitTexto;
						Integer nrAit;
						do {
							nrAitTexto = rsNr.getString("NR_AIT");
							nrAitTexto = nrAitTexto.replaceAll("[^0-9]*", "");
							nrAit = Integer.parseInt(nrAitTexto);

							if (nrAit >= rsmTalonario.getInt("NR_INICIAL") && nrAit <= rsmTalonario.getInt("NR_FINAL"))
								aitsAgente.add(nrAit);
						} while (rsNr.next());

						Collections.sort(aitsAgente);

						// Caso em que não há nehum ait registrada dentro da faixa desse talão
						if (aitsAgente.size() == 0) {
							nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL") - 1;
						} else {
							// Caso que pega a última ait registrada desse talão
							nrUltimoAitTalao = aitsAgente.get(aitsAgente.size() - 1);
						}

						if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO
								&& nrUltimoAitTalao == 0)
							nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL") - 1;
					}
				
				}

				rsNr.close();
				// SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
				if (nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL"))
					continue;

				Talonario talao;
				talao = new Talonario();
				
				talao.setCdTalao(rsmTalonario.getInt(Util.isStrBaseAntiga() ? "COD_TALAO" :"CD_TALAO"));
				talao.setCdAgente(rsmTalonario.getInt(Util.isStrBaseAntiga() ? "COD_TALAO" : "CD_AGENTE"));
				talao.setNrInicial(rsmTalonario.getInt("NR_INICIAL"));
				talao.setNrFinal(rsmTalonario.getInt("NR_FINAL"));
				talao.setDtEntrega((rsmTalonario.getTimestamp("DT_ENTREGA") == null) ? null
						: Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()));
				talao.setDtDevolucao((rsmTalonario.getTimestamp("DT_DEVOLUCAO") == null) ? null
						: Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()));
				talao.setStTalao(rsmTalonario.getInt("ST_TALAO"));
				talao.setNrTalao(rsmTalonario.getInt("NR_TALAO"));
				talao.setTpTalao(rsmTalonario.getInt("TP_TALAO"));
				talao.setNrUltimoAit(nrUltimoAitTalao);

				talonarios.add(talao);
				
			}

			if (talonarios.size() == 0) {
				return new Result(-5, "Nenhum talão distribuído para o agente. Entre em contato com a administração.");
			}

			return new Result(1, "Sucesso!", "TALOES", talonarios);
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

	public static Result getTalonariosByAgente(int cdAgente) {
		Agente agente = AgenteDAO.get(cdAgente);
		return getTalonariosByAgente(agente, null);
	}

	public static Result getTalonariosByAgente(Agente agente) {
		return getTalonariosByAgente(agente, null);
	}

	public static Result getTalonariosByAgente(Agente agente, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			boolean lgGctPadrao = ManagerConf.getInstance().getAsBoolean("GCT_PADRAO"); // Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
			ArrayList<Talonario> talonarios = new ArrayList<Talonario>();

			String sql = "";

			if (Util.isStrBaseAntiga()) {
				sql = "SELECT *, cod_talao as cd_talao, cod_agente as cd_agente FROM talonario " + "WHERE cod_agente = ? "
						+ "  AND ( tp_talao = ";

				sql += TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ELETRONICO_RRD + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_RADAR_ESTATICO + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ZONA_AZUL + " OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_RADAR_FIXO + ")";

				sql += "  AND st_talao = " + TalonarioServices.ST_TALAO_ATIVO +
				// " AND ? <= nr_final " +
						" ORDER BY dt_entrega";
			} else {
				sql = "SELECT * FROM mob_talonario " + "WHERE cd_agente = ? " + "  	AND (tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO + " 	OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT + " 	    OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV + " 	    OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ELETRONICO_RRD + " 	    OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO + " 	OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_RADAR_ESTATICO + " 	    OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_RADAR_FIXO + "  			OR tp_talao = "
						+ TalonarioServices.TP_TALONARIO_ZONA_AZUL + ")" + "  		AND st_talao = "
						+ TalonarioServices.ST_TALAO_ATIVO + " ORDER BY dt_entrega";
			}

			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, agente.getCdAgente());

			ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());

			while (rsmTalonario.next()) {
				int nrUltimoAitTalao = 0;
				System.out.println(
						"\t===========================================================================================================");
				System.out.println("\tVerificando talão tipo " + rsmTalonario.getInt("TP_TALAO") + " | "
						+ rsmTalonario.getInt("NR_INICIAL") + " - " + rsmTalonario.getInt("NR_FINAL"));
				System.out.println(
						"\t===========================================================================================================");
				// Ultimo AIT do talonario
				if (Util.isStrBaseAntiga()) {
					if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE)
						sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM AIT_TRANSPORTE WHERE cod_agente = ? AND nr_ait >=  ? AND nr_ait <= ?";
					else {

						// Pesquisa o último rdd
						if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD) {
							sql = "SELECT MAX(nr_rrd) as nr_ultimo_rrd FROM mob_rrd WHERE nr_rrd >=  ? AND nr_rrd <= ?";
						} // Pesquisa o último trrav
						else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV) {
							sql = "SELECT MAX(nr_trrav) as nr_ultimo_trrav FROM mob_trrav  WHERE nr_trrav >=  ? AND nr_trrav <= ?";
						} // BOAT
						else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT) {
							sql = "SELECT MAX(nr_boat) as nr_ultimo_boat FROM mob_boat WHERE nr_boat >=  ? AND nr_boat <= ?";
						} // AIT
						else {
							if (lgGctPadrao) {
								sql = "SELECT * FROM AIT WHERE cod_agente = ?";
							} else {
								// Pesquisa o último ait
								if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO
										|| rsmTalonario
												.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO) {
									sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM AIT WHERE nr_ait >=  ? AND nr_ait <= ?";								}
							}
						}

					}
				} else {
					if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) {
						sql = "SELECT * FROM mob_ait_transporte WHERE nr_ait >= ? AND nr_ait <= ? AND cd_agente = ? ";
					} else {
						if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO) {
							sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
						}
						if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO) {
							sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD) {
							sql = "SELECT MAX(nr_rrd) as nr_ultimo_rrd FROM mob_rrd WHERE nr_rrd >=  ? AND nr_rrd <= ?";
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV) {
							sql = "SELECT MAX(nr_trrav) as nr_ultimo_trrav FROM mob_trrav WHERE nr_trrav >=  ? AND nr_trrav <= ?";
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT) {
							sql = "SELECT MAX(nr_boat) as nr_ultimo_boat FROM mob_boat WHERE nr_boat >=  ? AND nr_boat <= ?";
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_RADAR_ESTATICO) {
							sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_RADAR_FIXO) {
							sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ZONA_AZUL) {
							sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
						}
					}
				}

				PreparedStatement pstmt2 = connect.prepareStatement(sql);

				if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) {
					pstmt2.setString(1, rsmTalonario.getString("NR_INICIAL"));
					pstmt2.setString(2, rsmTalonario.getString("NR_FINAL"));
					pstmt2.setInt(3, agente.getCdAgente());
				} else {
					if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO
							&& lgGctPadrao) {
						pstmt2.setInt(1, agente.getCdAgente());
					} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO
							&& lgGctPadrao) {
						pstmt2.setInt(1, agente.getCdAgente());
					} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_RADAR_FIXO && lgGctPadrao) {
						pstmt2.setInt(1, agente.getCdAgente());
					} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_RADAR_ESTATICO
							&& lgGctPadrao) {
						pstmt2.setInt(1, agente.getCdAgente());
					} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ZONA_AZUL
							&& lgGctPadrao) {
						pstmt2.setInt(1, agente.getCdAgente());
					} else {
						pstmt2.setInt(1, rsmTalonario.getInt("NR_INICIAL"));
						pstmt2.setInt(2, rsmTalonario.getInt("NR_FINAL"));
					}

				}

				ResultSet rsNr = pstmt2.executeQuery();

				// Ordena e pega o maior nr da AIT Transporte ou No caso do ait no Padrão GCT
				List<Integer> aitsAgente = new ArrayList<Integer>();
				if ((rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE)
						|| (Util.isStrBaseAntiga() && lgGctPadrao)) {

					if (rsNr.next()) {

						if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD) {
							nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_RRD") == 0 ? rsmTalonario.getInt("NR_INICIAL") - 1
									: rsNr.getInt("NR_ULTIMO_RRD");
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT) {
							nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_BOAT") == 0 ? rsmTalonario.getInt("NR_INICIAL") - 1
									: rsNr.getInt("NR_ULTIMO_BOAT");
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV) {
							nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_TRRAV") == 0 ? rsmTalonario.getInt("NR_INICIAL") - 1
									: rsNr.getInt("NR_ULTIMO_TRRAV");
						} else {

							String nrAitTexto;
							Integer nrAit;
							do {
								nrAitTexto = rsNr.getString("NR_AIT");
								nrAitTexto = nrAitTexto.replaceAll("[^0-9]*", "");
								nrAit = Integer.parseInt(nrAitTexto);

								if (nrAit >= rsmTalonario.getInt("NR_INICIAL") && nrAit <= rsmTalonario.getInt("NR_FINAL"))
									aitsAgente.add(nrAit);
							} while (rsNr.next());

							Collections.sort(aitsAgente);

							// Caso em que não há nehum ait registrada dentro da faixa desse talão
							if (aitsAgente.size() == 0) {
								nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL") - 1;
							}
							// Caso que pega a última ait registrada desse talão
							else {
								nrUltimoAitTalao = aitsAgente.get(aitsAgente.size() - 1);
							}

							if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE
									&& nrUltimoAitTalao == 0)
								nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL") - 1;

							if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO
									&& nrUltimoAitTalao == 0)
								nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL") - 1;

							System.out.println("nrUltimoAitTalao: " + nrUltimoAitTalao);
						}
					} else {
						nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL") - 1;// rsmTalonario.getInt("NR_FINAL");
					}

				} else {
					if (rsNr.next()) {
						if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD) {
							nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_RRD") == 0 ? rsmTalonario.getInt("NR_INICIAL") - 1
									: rsNr.getInt("NR_ULTIMO_RRD");
						} else if (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT) {
							nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_BOAT") == 0 ? rsmTalonario.getInt("NR_INICIAL") - 1
									: rsNr.getInt("NR_ULTIMO_RRD");
						} else {
							nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_AIT") == 0 ? rsmTalonario.getInt("NR_INICIAL") - 1
									: rsNr.getInt("NR_ULTIMO_AIT");
						}
					} else {
						nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL") - 1;// rsmTalonario.getInt("NR_FINAL");
					}
				}

				rsNr.close();

				// SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
				if (nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL"))
					continue;

				Talonario talao;

				talao = new Talonario();
				talao.setCdTalao(rsmTalonario.getInt("CD_TALAO"));
				talao.setCdAgente(rsmTalonario.getInt("CD_AGENTE"));
				talao.setNrInicial(rsmTalonario.getInt("NR_INICIAL"));
				talao.setNrFinal(rsmTalonario.getInt("NR_FINAL"));
				talao.setDtEntrega((rsmTalonario.getTimestamp("DT_ENTREGA") == null) ? null
						: Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()));
				talao.setDtDevolucao((rsmTalonario.getTimestamp("DT_DEVOLUCAO") == null) ? null
						: Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()));
				talao.setStTalao(rsmTalonario.getInt("ST_TALAO"));
				talao.setNrTalao(rsmTalonario.getInt("NR_TALAO"));
				talao.setTpTalao(rsmTalonario.getInt("TP_TALAO"));
				talao.setSgTalao(rsmTalonario.getString("SG_TALAO"));
				talao.setNrUltimoAit(nrUltimoAitTalao);

				talonarios.add(talao);
			}

			if (talonarios.size() == 0) {
				return new Result(-5, "Nenhum talão distribuído para o agente. Entre em contato com a administração.");
			}

			return new Result(1, "Sucesso!", "TALOES", talonarios);
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

	public synchronized static Talonario getTalaoRadar(Agente agente, com.tivic.manager.grl.equipamento.Equipamento equipamento,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			Result talonarios = TalonarioServices.getTalonariosByAgente(agente, connect);
			@SuppressWarnings("unchecked")
			ArrayList<Talonario> taloes = (ArrayList<Talonario>) talonarios.getObjects().get("TALOES");

			if (taloes.size() == 0)
				return null;

			Talonario talao = new Talonario();

			if (equipamento.getTpEquipamento() == EquipamentoServices.RADAR_FIXO ||
				equipamento.getTpEquipamento() == EquipamentoServices.DETECTOR) {
				for (Talonario t : taloes) {
					if (t.getNrInicial() > 0 && t.getNrUltimoAit() <= t.getNrFinal()
							&& t.getTpTalao() == TalonarioServices.TP_TALONARIO_RADAR_FIXO) {
						talao = t;
						break;
					}
				}
			}

			if (equipamento.getTpEquipamento() == EquipamentoServices.CAMERA) {
				for (Talonario t : taloes) {
					if (t.getNrInicial() > 0 && t.getNrUltimoAit() <= t.getNrFinal()
							&& t.getTpTalao() == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO) {
						talao = t;
						break;
					}
				}
			}

			if (equipamento.getTpEquipamento() == EquipamentoServices.RADAR_ESTATICO) {
				for (Talonario t : taloes) {
					if (t.getNrInicial() > 0 && t.getNrUltimoAit() <= t.getNrFinal()
							&& t.getTpTalao() == TalonarioServices.TP_TALONARIO_RADAR_ESTATICO) {
						talao = t;
						break;
					}
				}
			}
			
			if (talao.getNrUltimoAit() <= 0) {
				return null;
			}

			return talao;
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public synchronized static Talonario getTalaoMonitoramento(int cdAgente, Connection connect) {
		Agente agente = AgenteDAO.get(cdAgente);
		return getTalaoMonitoramento(agente, connect);
	}

	public synchronized static Talonario getTalaoMonitoramento(Agente agente, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			Result talonarios = TalonarioServices.getTalonariosMonitoramentoByAgente(agente, connect);
			@SuppressWarnings("unchecked")
			ArrayList<Talonario> taloes = (ArrayList<Talonario>) talonarios.getObjects().get("TALOES");

			if (taloes == null || taloes.size() == 0)
				return null;

			Talonario talao = new Talonario();

			Collections.sort(taloes, Comparator.comparing(Talonario::getNrInicial).thenComparing(Talonario::getNrInicial));

			for (Talonario t : taloes) {
				if (t.getNrInicial() > 0 && t.getNrUltimoAit() <= t.getNrFinal()
						&& t.getTpTalao() == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO
						&& t.getStTalao() == TalonarioServices.ST_TALAO_ATIVO) {
					talao = t;
					talao.setNrUltimoAit(talao.getNrUltimoAit() == 0 ? talao.getNrInicial() : talao.getNrUltimoAit());
					break;
				}
			}

			return talao;
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> getSyncData(ArrayList<Talonario> talonario, int cdAgente) {
		return getSyncData(talonario, cdAgente, null);
	}

	public static HashMap<String, Object> getSyncData(ArrayList<Talonario> talonario, int cdAgente, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			String sql = "SELECT * FROM mob_talonario WHERE 1=1"
					+ (cdAgente > 0 ? "AND cd_agente = ?" : "" + " ORDER BY cd_talao");

			if (Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM mob_tabela_horario";

			pstmt = connect.prepareStatement(sql);

			if (cdAgente > 0) {
				pstmt.setInt(1, cdAgente);
			}

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("Talonario", Util.resultSetToArrayList(pstmt.executeQuery()));

			return register;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Converte mob.Talonario para str.Talonario
	 * 
	 * @param talonario
	 * @return
	 */
	public static com.tivic.manager.str.Talonario toStrTalonario(Talonario talonario) {
		return new com.tivic.manager.str.Talonario(talonario.getCdTalao(), talonario.getCdAgente(),
				talonario.getNrInicial(), talonario.getNrFinal(), talonario.getDtEntrega(), talonario.getDtDevolucao(),
				talonario.getStTalao(), talonario.getNrTalao(), talonario.getTpTalao(), talonario.getSgTalao(),
				talonario.getNrUltimoAit());
	}

	public static Result getUltimoAit(int tpTalonario) {
		return getUltimoAit(tpTalonario, null);
	}

	public static Result getUltimoAit(int tpTalonario, Connection connect) {
		boolean isConnectionNull = (connect == null);

		if (isConnectionNull)
			connect = Conexao.conectar();

		int[] gpTalao = getGrupoTalao(tpTalonario);

		try {
			String tipos = "(";
			for(int i = 0; i < gpTalao.length ; i++) {
				tipos += "\'" + gpTalao[i] + "\'";
				
				if(gpTalao.length - i != 1)
					tipos += ", ";
			}
			tipos += ")";
			
			String sql = "SELECT max(nr_final) as ULTIMO_TALAO FROM MOB_TALONARIO WHERE TP_TALAO IN " + tipos;		
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			if (rsm.next()) {
				return new Result(rsm.getInt("ULTIMO_TALAO") + 1);
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Talonario ativarTalao(Talonario talonario) throws Exception {
		return ativarTalao(talonario, null);
	}

	public static Talonario ativarTalao(Talonario talonario, Connection connect) throws Exception {
		boolean isConnectionNull = (connect == null);

		if (isConnectionNull) {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
		}

		try {
			Optional<String> validacao = validateAtivacao(talonario);

			if (!validacao.isPresent()) {
				talonario.setStTalao(ST_TALAO_ATIVO);
				TalonarioDAO.update(talonario);
				connect.commit();
			} else {
				throw new Exception(validacao.get());
			}

			return talonario;

		} finally {
			if (isConnectionNull) {
				Conexao.desconectar(connect);
			}
		}

	}

	private static Optional<String> validateAtivacao(Talonario talonario) {

		if (talonario.getDtEntrega() != null && talonario.getCdAgente() <= 0) {
			return Optional.of("Não é permitido salvar um talão com Data de Entrega preenchida sem um Agente vinculado.");
		}

		if (talonario.getCdAgente() > 0 && talonario.getDtEntrega() == null) {
			return Optional.of("Não é permitido salvar um talão com um agente vinculado mas sem data de entrega.");
		}

		return Optional.empty();
	}

	private static boolean validTipo(int tpNew, int tpOut) {
		boolean match = false;

		int[] transito = { 0, 1, 11, 12, 13, 14 };
		int[] transporte = { 2, 3 };
		int[] boat = { 5, 6 };
		int[] rrd = { 7, 8 };
		int[] trrav = { 9, 10 };

		if (contains(transito, tpNew)) {
			if (contains(transito, tpOut)) {
				match = true;
			}
		}

		if (contains(transporte, tpNew)) {
			if (contains(transporte, tpOut)) {
				match = true;
			}
		}

		if (contains(boat, tpNew)) {
			if (contains(boat, tpOut)) {
				match = true;
			}
		}

		if (contains(rrd, tpNew)) {
			if (contains(rrd, tpOut)) {
				match = true;
			}
		}

		if (contains(trrav, tpNew)) {
			if (contains(trrav, tpOut)) {
				match = true;
			}
		}

		return match;
	}

	public static int[] getGrupoTalao(int tpTalonario) {

		int[] transito = { 0, 1, 4, 12, 13, 14, 15 };
		int[] transporte = { 2, 3 };
		int[] boat = { 5, 6 };
		int[] rrd = { 7, 8 };
		int[] trrav = { 9, 10 };

		if (contains(transito, tpTalonario)) {
			return transito;
		}

		if (contains(transporte, tpTalonario)) {
			return transporte;
		}

		if (contains(boat, tpTalonario)) {
			return boat;
		}

		if (contains(rrd, tpTalonario)) {
			return rrd;
		}

		if (contains(trrav, tpTalonario)) {
			return trrav;
		}

		return null;

	}

	private static boolean between(int mn, int fn, int sn) {
		if (mn >= fn && mn <= sn) {
			return true;
		} else {
			return false;
		}
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

}
