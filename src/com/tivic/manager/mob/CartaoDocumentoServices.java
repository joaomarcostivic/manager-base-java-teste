package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Doenca;
import com.tivic.manager.grl.DoencaDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaFichaMedicaDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.PessoaTipoDocumentacao;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoOcorrenciaDAO;
import com.tivic.manager.ptc.DocumentoOcorrenciaServices;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.DocumentoTramitacao;
import com.tivic.manager.ptc.DocumentoTramitacaoServices;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.SituacaoDocumentoDAO;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CartaoDocumentoServices {

	public static Result save(CartaoDocumento cartaoDocumento) {
		return save(cartaoDocumento, null);
	}

	public static Result save(CartaoDocumento cartaoDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (cartaoDocumento == null)
				return new Result(-1, "Erro ao salvar. CartaoDocumento é nulo");

			int retorno;
			if (cartaoDocumento.getCdCartaoDocumento() == 0) {
				retorno = CartaoDocumentoDAO.insert(cartaoDocumento, connect);
				cartaoDocumento.setCdCartaoDocumento(retorno);
			} else {
				retorno = CartaoDocumentoDAO.update(cartaoDocumento, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "CARTAODOCUMENTO",
					cartaoDocumento);
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

	public static Result save(int cdSolicitante, int cdCartaoDocumento, Documento documento,
			DocumentoTramitacao tramitacao, Cartao cartao, ResultSetMap rsmDoenca) {
		return save(cdSolicitante, cdCartaoDocumento, documento, tramitacao, cartao, rsmDoenca, null, null);
	}

	public static Result save(int cdSolicitante, int cdCartaoDocumento, Documento documento,
			DocumentoTramitacao tramitacao, Cartao cartao, ResultSetMap rsmDoenca, AuthData authData) {
		return save(cdSolicitante, cdCartaoDocumento, documento, tramitacao, cartao, rsmDoenca, authData, null);
	}

	public static Result save(int cdSolicitante, int cdCartaoDocumento, Documento documento,
			DocumentoTramitacao tramitacao, Cartao cartao, ResultSetMap rsmDoenca, AuthData authData,
			AgendaItem agenda) {
		return save(cdSolicitante, cdCartaoDocumento, documento, tramitacao, cartao, rsmDoenca, authData, agenda, null);
	}

	public static Result save(int cdSolicitante, int cdCartaoDocumento, Documento documento,
			DocumentoTramitacao tramitacao, Cartao cartao, ResultSetMap rsmDoenca, AuthData authData, AgendaItem agenda,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (cdSolicitante <= 0)
				return new Result(-1, "Erro ao salvar. Solicitante é nulo");

			if (documento == null)
				return new Result(-1, "Erro ao salvar. Documento é nulo");

			Result r;
			int stDocumentoPericia = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_EM_ANDAMENTO", 0, 0,
					connect);
			int stDocumentoDeferido = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_DEFERIDO", 0, 0,
					connect);
			int stDocumentoIndeferido = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_INDEFERIDO", 0, 0,
					connect);
			int tpOcorrenciaMudancaDoc = ParametroServices
					.getValorOfParametroAsInteger("PNE_OCORRENCIA_MUDANCA_SITUACAO", 0, 0, connect);
			int retorno = 0;
			int cdCartao = 0;

			if (documento.getCdDocumento() == 0) {
				ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
				solicitantes.add(new DocumentoPessoa(0, cdSolicitante, "SOLICITANTE"));
				if (documento.getTxtDocumento() == null) {
					documento.setTxtDocumento(" ");
				}
				r = DocumentoServices.save(documento, solicitantes, connect);
				retorno = r.getCode();
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, r.getMessage());
				}
				documento.setCdDocumento(r.getCode());
			} else {
				retorno = DocumentoDAO.update(documento, connect);
			}

			/* Salvando a agenda do documento */
			if (agenda != null) {
				agenda.setCdDocumento(documento.getCdDocumento());

				r = AgendaItemServices.save(agenda, authData, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, r.getMessage());
				}
			}
			
			/* Salvando a ocorrência do documento. */
			String txtOcorrencia = "";

			if (documento.getCdSituacaoDocumento() == stDocumentoPericia)
				txtOcorrencia = "Documento enviado para perícia";
			else if (documento.getCdSituacaoDocumento() == stDocumentoDeferido)
				txtOcorrencia = "Documento deferido";
			else if (documento.getCdSituacaoDocumento() == stDocumentoIndeferido)
				txtOcorrencia = "Documento indeferido";

			DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia();
			ocorrencia.setCdDocumento(documento.getCdDocumento());
			ocorrencia.setCdUsuario(authData.getUsuario().getCdUsuario());
			ocorrencia.setDtOcorrencia(new GregorianCalendar());
			ocorrencia.setTpVisibilidade(1);
			ocorrencia.setCdTipoOcorrencia(tpOcorrenciaMudancaDoc);
			ocorrencia.setTxtOcorrencia(txtOcorrencia);

			DocumentoOcorrenciaServices.save(ocorrencia, connect);

			if (tramitacao != null) {
				tramitacao.setCdDocumento(documento.getCdDocumento());
				r = DocumentoTramitacaoServices.save(tramitacao, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, r.getMessage());
				}
			}

			if (cartao != null) {
				if (cartao.getCdCartao() == 0) {
					cdCartao = CartaoDAO.insert(cartao, connect);
					CartaoDocumento cartaoDocumento = new CartaoDocumento();
					if (cdCartaoDocumento > 0) {
						cartaoDocumento.setCdCartaoDocumento(cdCartaoDocumento);
					}
					cartaoDocumento.setCdDocumento(documento.getCdDocumento());
					cartaoDocumento.setCdCartao(cdCartao);
					Result CartaoDocumento = CartaoDocumentoServices.save(cartaoDocumento, connect);
					cdCartaoDocumento = ((CartaoDocumento) CartaoDocumento.getObjects().get("CARTAODOCUMENTO"))
							.getCdCartaoDocumento();
					if (cdCartao <= 0 || CartaoDocumento.getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Erro ao registrar Cartão");
					}
				} else {
					retorno = CartaoDAO.update(cartao, connect);
					CartaoDocumento cartaoDocumento = CartaoDocumentoDAO.get(cdCartaoDocumento, connect);
					if (cartaoDocumento == null) {
						cartaoDocumento = new CartaoDocumento(0, documento.getCdDocumento(), retorno);
						Result objCartaoDocumento = CartaoDocumentoServices.save(cartaoDocumento, connect);
						CartaoDocumento cDocumento = (CartaoDocumento) objCartaoDocumento.getObjects()
								.get("CARTAODOCUMENTO");
						cdCartaoDocumento = cDocumento.getCdCartaoDocumento();
						if (objCartaoDocumento.getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-2, "Erro ao atualizar Cartão");
						}
					}
				}
			} else {
				CartaoDocumento cartaoDocumento = new CartaoDocumento(0, documento.getCdDocumento(), 0);
				cdCartaoDocumento = CartaoDocumentoDAO.insert(cartaoDocumento, connect);
				if (cdCartaoDocumento <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao registrar Cartão");
				}
			}

			if (rsmDoenca != null) {
				while (rsmDoenca.next()) {
					CartaoDocumento cartaoDocumento = CartaoDocumentoDAO.get(cdCartaoDocumento, connect);
					Doenca objDoenca = DoencaDAO.get(rsmDoenca.getInt("CD_DOENCA"), connect);

					CartaoDocumentoDoenca objDocumentoDoenca = CartaoDocumentoDoencaDAO
							.getDocumentoDoenca(cdCartaoDocumento, rsmDoenca.getInt("CD_DOENCA"));
					if (objDocumentoDoenca == null) {
						CartaoDocumentoDoenca doenca = new CartaoDocumentoDoenca();
						doenca.setCdDoenca(objDoenca.getCdDoenca());
						doenca.setCdCartaoDocumento(cartaoDocumento.getCdCartaoDocumento());
						doenca.setTxtDescricao(rsmDoenca.getString("TXT_DEFICIENCIA"));

						Result cartaoDocumentoDoenca = CartaoDocumentoDoencaServices.save(doenca, connect);
						if (cartaoDocumentoDoenca.getCode() <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-2, "Erro ao registrar Doenças");
						}
					}
				}
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "CARTAODOCUMENTO",
					documento);
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

	public static Result saveObs(int cdDocumento, String txtObservacao) {
		return saveObs(cdDocumento, txtObservacao, null);
	}

	public static Result saveObs(int cdDocumento, String txtObservacao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Documento documento = DocumentoDAO.get(cdDocumento);
			int retorno = 0;

			documento.setTxtDocumento(txtObservacao.trim());
			if (documento != null) {
				retorno = DocumentoDAO.update(documento, connect);
			}

			connect.commit();
			return new Result(retorno, "Observação salva com sucesso!");

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

	public static Result rejeitar(Documento documento, DocumentoTramitacao tramitacao) {
		return rejeitar(documento, tramitacao, null, null);
	}

	public static Result rejeitar(Documento documento, DocumentoTramitacao tramitacao, AuthData authData) {
		return rejeitar(documento, tramitacao, authData, null, null);
	}

	public static Result rejeitar(Documento documento, DocumentoTramitacao tramitacao, AuthData authData,
			AgendaItem agenda) {
		return rejeitar(documento, tramitacao, authData, agenda, null);
	}

	public static Result rejeitar(Documento documento, DocumentoTramitacao tramitacao, AuthData authData,
			AgendaItem agenda, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			documento = DocumentoDAO.get(documento.getCdDocumento());
			int retorno = 0;
			Result r;
			documento.setCdSituacaoDocumento(
					ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_INDEFERIDO", 0));

			int tpOcorrenciaMudancaDoc = ParametroServices
					.getValorOfParametroAsInteger("PNE_OCORRENCIA_MUDANCA_SITUACAO", 0, 0, connect);
			DocumentoOcorrencia ocorrencia = new DocumentoOcorrencia();
			ocorrencia.setCdDocumento(documento.getCdDocumento());
			ocorrencia.setCdUsuario(authData.getUsuario().getCdUsuario());
			ocorrencia.setDtOcorrencia(new GregorianCalendar());
			ocorrencia.setTpVisibilidade(1);
			ocorrencia.setCdTipoOcorrencia(tpOcorrenciaMudancaDoc);
			ocorrencia.setTxtOcorrencia("Documento indeferido");

			DocumentoOcorrenciaServices.save(ocorrencia, connect);

			if (documento != null && documento.getCdDocumento() > 0) {
				retorno = DocumentoDAO.update(documento, connect);
				if (retorno <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao tentar rejeitar CI.");
				}
			}

			if (tramitacao != null) {
				tramitacao.setCdDocumento(documento.getCdDocumento());
				r = DocumentoTramitacaoServices.save(tramitacao, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, r.getMessage());
				}
			}

			/* Salvando a agenda do documento */
			if (agenda != null) {

				r = AgendaItemServices.save(agenda, authData, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-4, r.getMessage());
				}
			}

			connect.commit();
			return new Result(1, "CI rejeitada com sucesso!");
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

	public static Result anular(Documento documento, DocumentoTramitacao tramitacao) {
		return anular(documento, tramitacao, null);
	}

	public static Result anular(Documento documento, DocumentoTramitacao tramitacao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			documento = DocumentoDAO.get(documento.getCdDocumento());
			int retorno = 0;
			Result r;
			documento.setCdSituacaoDocumento(
					ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_CANCELADO", 0));

			if (documento != null && documento.getCdDocumento() > 0) {
				retorno = DocumentoDAO.update(documento, connect);
				if (retorno <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao tentar anular CI.");
				}
			}

			if (tramitacao != null) {
				tramitacao.setCdDocumento(documento.getCdDocumento());
				r = DocumentoTramitacaoServices.save(tramitacao, connect);
				if (r.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, r.getMessage());
				}
			}

			connect.commit();
			return new Result(1, "CI anulada com sucesso!");
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

	public static Result remove(int cdCartaoDocumento) {
		return remove(cdCartaoDocumento, false, null);
	}

	public static Result remove(int cdCartaoDocumento, boolean cascade) {
		return remove(cdCartaoDocumento, cascade, null);
	}

	public static Result remove(int cdCartaoDocumento, boolean cascade, Connection connect) {
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
				retorno = CartaoDocumentoDAO.delete(cdCartaoDocumento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findAllDocumentosByAno(int ano) {
		return findAllDocumentosByAno(ano, null);
	}

	public static ResultSetMap findAllDocumentosByAno(int ano, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT COUNT(A.NR_DOCUMENTO), to_char(A.DT_PROTOCOLO, 'MM') AS NM_MES FROM ptc_documento A "
							+ "JOIN ptc_documento_pessoa B on ( A.cd_documento = B.cd_documento ) "
							+ "LEFT OUTER JOIN mob_cartao_documento C on ( A.cd_documento = C.cd_documento ) "
							+ "LEFT OUTER JOIN ptc_situacao_documento D on ( A.cd_situacao_documento = D.cd_situacao_documento ) "
							+ "LEFT OUTER JOIN mob_cartao E ON (C.cd_cartao = E.cd_cartao) "
							+ "LEFT OUTER JOIN grl_pessoa F ON (B.cd_pessoa = F.cd_pessoa) "
							+ "LEFT OUTER JOIN grl_pessoa_fisica G ON (F.cd_pessoa = G.cd_pessoa) "
							+ "WHERE A.DT_PROTOCOLO >= ? AND A.DT_PROTOCOLO <= ? AND A.cd_situacao_documento != 5 "
							+ "GROUP BY to_char(A.DT_PROTOCOLO, 'MM') ");

			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar(ano, 01, 00, 00, 00).getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(new GregorianCalendar(ano, 31, 12, 00, 00).getTimeInMillis()));

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			if (rsm.size() < 12) {
				for (int i = rsm.size() + 1; i <= 12; i++) {
					HashMap<String, Object> register = new HashMap<String, Object>();
					DecimalFormat df = new DecimalFormat("00");
					register.put("NM_MES", df.format(i));
					register.put("COUNT", 0);
					rsm.addRegister(register);
				}
			}

			return rsm;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public static ResultSetMap findAllDocumentosBySituacao() {
		return findAllDocumentosBySituacao(null);
	}

	public static ResultSetMap findAllDocumentosBySituacao(Connection connect) {
		boolean isConnectionNull = connect == null;

		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			PreparedStatement pstmt = connect
					.prepareStatement("SELECT COUNT(A.NR_DOCUMENTO), D.NM_SITUACAO_DOCUMENTO FROM ptc_documento A "
							+ "JOIN ptc_documento_pessoa B on ( A.cd_documento = B.cd_documento ) "
							+ "LEFT OUTER JOIN mob_cartao_documento C on ( A.cd_documento = C.cd_documento ) "
							+ "LEFT OUTER JOIN ptc_situacao_documento D on ( A.cd_situacao_documento = D.cd_situacao_documento ) "
							+ "LEFT OUTER JOIN mob_cartao E ON (C.cd_cartao = E.cd_cartao) "
							+ "LEFT OUTER JOIN grl_pessoa F ON (B.cd_pessoa = F.cd_pessoa) "
							+ "LEFT OUTER JOIN grl_pessoa_fisica G ON (F.cd_pessoa = G.cd_pessoa) "
							+ "WHERE  A.cd_situacao_documento != 5 " + "GROUP BY NM_SITUACAO_DOCUMENTO ");

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while (rsm.next()) {
				if (rsm.getString("NM_SITUACAO_DOCUMENTO") == null
						|| rsm.getString("NM_SITUACAO_DOCUMENTO").equals("")) {
					rsm.setValueToField("NM_SITUACAO_DOCUMENTO", "Indefinido");
				}
			}

			rsm.beforeFirst();

			return rsm;

		} catch (Exception e) {
			System.out.println("Erro!" + e.getMessage());
			return null;
		}
	}

	public static ResultSetMap findAllDocumentosEmAndamento(int cdPessoa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("B.CD_PESSOA", String.valueOf(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_SITUACAO_DOCUMENTO",
				String.valueOf(ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_EM_ANDAMENTO", 0)),
				ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}

	public static ResultSetMap getPessoaByDocumento(String nrDocumento) {
		return getPessoaByDocumento(nrDocumento, null);
	}

	public static ResultSetMap getPessoaByDocumento(String nrDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			PreparedStatement pstmt = connect.prepareStatement("SELECT B.*, C.* " + "FROM ptc_documento A "
					+ "JOIN ptc_documento_pessoa B ON (A.cd_documento = B.cd_documento) "
					+ "JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) "
					+ "JOIN grl_pessoa_fisica D ON (C.cd_pessoa = D.cd_pessoa) " + "WHERE A.nr_documento = ? LIMIT 1");

			pstmt.setString(1, nrDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			return rsm;

		} catch (Exception e) {
			System.out.println("Erro!" + e.getMessage());
			return null;
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {

		String sql = "";
		String idsDoencas = "";
		String orderBy = "";
		String formato = "YY \"ano(s)\" MM \"mes(es)\"";
		int qtLimite = 0;

		int cdSetorCemae = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_CEMAE", 0);
		int stEmAndamento = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_EM_ANDAMENTO", 0);

		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("IDS_DOENCA")) {
				idsDoencas = criterios.get(i).getValue();
				criterios.remove(i--);
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
				orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}

		sql = "SELECT A.*, B.*, C.*, D.*, E.*, F.*, G,*, I.nm_pessoa as nm_usuario, J2.cd_pessoa as cd_medico, "
				+ "TO_CHAR( AGE(dt_validade, dt_emissao), '" + formato + "')  AS nr_validade, "
				+ "(SELECT txt_ocorrencia FROM ptc_documento_ocorrencia SA where SA.cd_documento = A.cd_documento ORDER BY dt_ocorrencia DESC LIMIT 1) as TXT_ULTIMA_OCORRENCIA, "
				+ "COALESCE((SELECT COUNT(*) FROM ptc_documento_tramitacao SB WHERE SB.cd_documento = A.cd_documento AND SB.cd_setor_destino = "
				+ cdSetorCemae + " LIMIT 1)) as ST_PERICIA, "
				+ "COALESCE((SELECT COUNT(*) FROM ptc_documento SC, ptc_documento_pessoa SD WHERE SC.cd_documento = SD.cd_documento AND SD.cd_pessoa = F.cd_pessoa AND SC.cd_situacao_documento = "
				+ stEmAndamento + " LIMIT 1)) as QT_ANDAMENTO, "
				+ "(SELECT string_agg(cast(SD.cd_doenca as text), ',') FROM mob_cartao_documento_doenca SD where SD.cd_cartao_documento = A.cd_documento) as ID_DOENCAS "
				+ "FROM ptc_documento A "
				+ "JOIN ptc_documento_pessoa                         B on ( A.cd_documento = B.cd_documento ) "
				+ "JOIN mob_cartao_documento                         C on ( A.cd_documento = C.cd_documento ) "
				+ "LEFT OUTER JOIN ptc_situacao_documento            D on ( A.cd_situacao_documento = D.cd_situacao_documento ) "
				+ "LEFT OUTER JOIN mob_cartao                        E ON (C.cd_cartao = E.cd_cartao AND B.cd_pessoa = E.cd_pessoa) "
				+ "LEFT OUTER JOIN grl_pessoa                        F ON (B.cd_pessoa = F.cd_pessoa) "
				+ "LEFT OUTER JOIN grl_pessoa_fisica                 G ON (F.cd_pessoa = G.cd_pessoa) "
				+ "LEFT OUTER JOIN seg_usuario                       H ON (A.cd_usuario = H.cd_usuario) "
				+ "LEFT OUTER JOIN grl_pessoa                        I ON (I.cd_pessoa = H.cd_pessoa) "
				+ "LEFT OUTER JOIN agd_agenda_item                   J ON (B.cd_documento = J.cd_documento) "
				+ "LEFT OUTER JOIN grl_pessoa                        J2 ON (J.cd_pessoa = J2.cd_pessoa) " + "WHERE 1=1 "
				+ (!idsDoencas.equals("")
						? " AND (SELECT string_agg(cast(SD.cd_doenca as text), ',') FROM mob_cartao_documento_doenca SD where SD.cd_cartao_documento = A.cd_documento) LIKE '%"
								+ idsDoencas + "%' "
						: "");

		ResultSetMap rsm = Search.find(sql, (orderBy + (qtLimite > 0 ? " LIMIT " + qtLimite : "")), criterios,
				connect != null ? connect : Conexao.conectar(), connect == null);

		return rsm;
	}

	public static ResultSetMap findSolicitacoes(ArrayList<ItemComparator> criterios) {
		return findSolicitacoes(criterios, null);
	}

	public static ResultSetMap findSolicitacoes(ArrayList<ItemComparator> criterios, Connection connect) {
		
		try {
			int qtLimite = 0;
			int qtDeslocamento = 0;
			
			String orderBy = "ORDER BY B.dt_protocolo DESC ";
	
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento")) {
					qtDeslocamento = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
					i--;
				}
		}

			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);

			String sql = "SELECT "+ sqlLimit[0] + "A.cd_cartao_documento, B.cd_documento, B.nr_documento, B.tp_interno_externo, D.cd_situacao_documento, D.nm_situacao_documento, "
					+ "E.cd_pessoa, E.nm_pessoa, E.nr_telefone1, E.nr_celular, F.* " +	
					"FROM mob_cartao_documento  A " +
					"JOIN ptc_documento                  B ON (A.cd_documento = B.cd_documento) " +
					"JOIN ptc_documento_pessoa           C ON (B.cd_documento = C.cd_documento) " +
					"JOIN ptc_situacao_documento         D on (B.cd_situacao_documento = D.cd_situacao_documento ) " +
					"JOIN grl_pessoa                     E ON (C.cd_pessoa = E.cd_pessoa) " +
					"JOIN grl_pessoa_fisica              F ON (E.cd_pessoa = F.cd_pessoa) " +
					"WHERE 1=1 ";

			ResultSetMap rsm = Search.find(sql, (orderBy + sqlLimit[1]), criterios, 
					connect != null ? connect : Conexao.conectar(), connect == null);

			rsm.beforeFirst();

			ResultSetMap rsmTotal = Search.find(" SELECT COUNT(*)  " +
					"FROM mob_cartao_documento  A " +
					"JOIN ptc_documento                  B ON (A.cd_documento = B.cd_documento) " +
					"JOIN ptc_documento_pessoa           C ON (B.cd_documento = C.cd_documento) " +
					"JOIN ptc_situacao_documento         D on (B.cd_situacao_documento = D.cd_situacao_documento ) " +
					"JOIN grl_pessoa                     E ON (C.cd_pessoa = E.cd_pessoa) " +
					"JOIN grl_pessoa_fisica              F ON (E.cd_pessoa = F.cd_pessoa) " +
					 "WHERE 1=1 ", "", criterios, 
					 connect != null ? connect : Conexao.conectar(), connect == null);

			 while (rsm.next() ) {
				DocumentoOcorrencia ultimaOcorrencia = DocumentoOcorrenciaServices.getUltimaOcorrencia(rsm.getInt("CD_DOCUMENTO"), connect);
				if(ultimaOcorrencia!= null) {
					rsm.setValueToField("CD_OCORRENCIA", ultimaOcorrencia.getCdOcorrencia());
					rsm.setValueToField("CD_TIPO_OCORRENCIA", ultimaOcorrencia.getCdTipoOcorrencia());
					rsm.setValueToField("TXT_OCORRENCIA", ultimaOcorrencia.getTxtOcorrencia());
				}
			 }

			 if(rsmTotal.next())
					rsm.setTotal(rsmTotal.getInt("COUNT"));
			 
			rsm.beforeFirst();
			 
			return rsm;
		} catch(Exception e) {
			System.out.println("Erro! CartaoDocumentoServices.findSolicitacoes");
			e.printStackTrace(System.out);
		}
		return null;
	}
	
	public static List<Doenca> getDeficienciaByPessoa(int cdCartaoDocumento) {
		return getDeficienciaByPessoa(cdCartaoDocumento, null);
	}

	public static List<Doenca> getDeficienciaByPessoa(int cdCartaoDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento A " + 
					"JOIN mob_cartao_documento_doenca B ON (A.cd_cartao_documento = B.cd_cartao_documento) " + 
					"JOIN grl_doenca C ON (B.cd_doenca = C.cd_doenca) " + "WHERE A.cd_cartao_documento = ? ");
			pstmt.setInt(1, cdCartaoDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			return new ResultSetMapper<Doenca>(rsm, Doenca.class).toList();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<List<Doenca>> getListDoencaByDocumento(List<CartaoDocumento> cartoesDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			if(cartoesDocumento.isEmpty()) {
				return null;
			}
			
			List<List<Doenca>> doencas = new ArrayList<List<Doenca>>();
			
			for(CartaoDocumento cartaoDocumento : cartoesDocumento) {
				List<Doenca> listDoencas = CartaoDocumentoServices.getDeficienciaByPessoa(cartaoDocumento.getCdCartaoDocumento(), connect);
				
				if(listDoencas.isEmpty())
					return null;
			
				doencas.add(new ArrayList<Doenca>(listDoencas));	
			}
			
			return doencas;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static ResultSetMap getOcorrenciasByPessoa(int cdCartaoDocumento) {
		return getOcorrenciasByPessoa(cdCartaoDocumento, null);
	}
	
	public static ResultSetMap getOcorrenciasByPessoa (int cdCartaoDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}
			
			PreparedStatement pstmt = connect.prepareStatement(" SELECT A.*, B.nm_tipo_ocorrencia, C.nm_login " +
					" FROM ptc_documento_ocorrencia A " +
					" JOIN grl_tipo_ocorrencia B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia)" +
					" LEFT OUTER JOIN seg_usuario C ON (A.cd_usuario = C.cd_usuario)" +
					" JOIN mob_cartao_documento   D ON (A.cd_documento = D.cd_documento)" +
					" WHERE D.cd_cartao_documento = ? ORDER BY A.dt_ocorrencia DESC");
			pstmt.setInt(1, cdCartaoDocumento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<CartaoDocumentoDTO> getPessoaByCartaoDocumento(int cdCartaoDocumento) {
		return getPessoaByCartaoDocumento(cdCartaoDocumento, null);
	}
	
	public static List<CartaoDocumentoDTO> getPessoaByCartaoDocumento (int cdCartaoDocumento, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}
			
			PreparedStatement pstmt = connect.prepareStatement(" SELECT A.cd_cartao_documento, E.*, G.nr_cartao_sus " + 
					"					FROM mob_cartao_documento A " + 
					"					JOIN ptc_documento                  B ON (A.cd_documento = B.cd_documento) " + 
					"					JOIN ptc_documento_pessoa           C ON (B.cd_documento = C.cd_documento) " + 
					"					JOIN grl_pessoa                     D ON (C.cd_pessoa = D.cd_pessoa) " + 
					"					JOIN grl_pessoa_endereco            E ON (D.cd_pessoa = E.cd_pessoa) " + 
					"					JOIN grl_pessoa_fisica              F ON (D.cd_pessoa = F.cd_pessoa) " + 
					"					JOIN grl_pessoa_ficha_medica        G ON (F.cd_pessoa = G.cd_pessoa) " + 
					"					WHERE A.cd_cartao_documento = ? ");
			pstmt.setInt(1, cdCartaoDocumento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			List<CartaoDocumentoDTO> list = new ArrayList<CartaoDocumentoDTO>();
			
			while(rsm.next()) {
				CartaoDocumentoDTO cartaoDocumentoDTO = new CartaoDocumentoDTO();
				
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(rsm.getInt("CD_PESSOA"));
				cartaoDocumentoDTO.setPessoaEndereco(pessoaEndereco);
				
				PessoaFichaMedica pessoaFichaMedica = PessoaFichaMedicaDAO.get(rsm.getInt("CD_PESSOA"));
				cartaoDocumentoDTO.setPessoaFichaMedica(pessoaFichaMedica);
				
				CartaoDocumento cartaoDocumento = CartaoDocumentoDAO.get(rsm.getInt("CD_CARTAO_DOCUMENTO"));
				cartaoDocumentoDTO.setCartaoDocumento(cartaoDocumento);
				
				list.add(cartaoDocumentoDTO);
			}
			
			return list;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<CartaoDocumentoDTO> getPessoaByCpf(String nrCpf) {
		return getPessoaByCpf(nrCpf, null);
	}
	
	public static List<CartaoDocumentoDTO> getPessoaByCpf (String nrCpf, Connection connect) {
		boolean isConnectionNull = connect == null;
		
		try {
			
			List<CartaoDocumentoDTO> list = new ArrayList<CartaoDocumentoDTO>();
			
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT C.*, D.*, E.*, F.cd_cidade, F.nm_cidade, G.nr_cartao_sus " +
					"FROM ptc_documento A " + 
					"JOIN ptc_documento_pessoa           B ON (A.cd_documento = B.cd_documento) " + 
					"JOIN grl_pessoa                     C ON (B.cd_pessoa = C.cd_pessoa) " + 
					"JOIN grl_pessoa_endereco            D ON  (C.cd_pessoa = D.cd_pessoa) " + 
					"JOIN grl_pessoa_fisica              E ON (C.cd_pessoa = E.cd_pessoa) " + 
					"JOIN grl_cidade                     F ON (E.cd_naturalidade = F.cd_cidade) " + 
					"JOIN grl_pessoa_ficha_medica       G ON (G.cd_pessoa = E.cd_pessoa) " +
					"WHERE E.nr_cpf = ? 		LIMIT 1");
			
			pstmt.setString(1, nrCpf);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while (rsm!=null && rsm.next()) {
							
				CartaoDocumentoDTO dto = new CartaoDocumentoDTO();
				
				Cidade cidade = CidadeDAO.get(rsm.getInt("CD_CIDADE"));
				dto.setCidade(cidade);

				Pessoa pessoa = PessoaDAO.get(rsm.getInt("CD_PESSOA"));
				dto.setPessoa(pessoa);
							
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(rsm.getInt("CD_PESSOA"));
				dto.setPessoaEndereco(pessoaEndereco);
				
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(rsm.getInt("CD_PESSOA"));
				dto.setPessoaFisica(pessoaFisica);
				
				PessoaFichaMedica pessoaFichaMedica = PessoaFichaMedicaDAO.get(rsm.getInt("CD_PESSOA"));
				dto.setPessoaFichaMedica(pessoaFichaMedica);
				
				list.add(dto);
			}
			
			return list;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cartao getCartao(int cdCartaoDocumento) {
		return getCartao(cdCartaoDocumento, null);
	}
	
	public static Cartao getCartao(int cdCartaoDocumento, Connection connection) {
		boolean isConnectionNull = connection == null;
		
		try {
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(true);
			}
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.cd_cartao_documento FROM mob_cartao A " +
					 "JOIN mob_cartao_documento B ON (A.cd_cartao = B.cd_cartao) " +
					 "WHERE B.cd_cartao_documento = ?" );
			
			pstmt.setInt(1, cdCartaoDocumento);			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			Cartao cartao = new Cartao();
			if(rsm.next()) {
				cartao.setCdCartao(rsm.getInt("cd_cartao"));
				cartao.setCdPessoa(rsm.getInt("cd_pessoa"));
				cartao.setDtEmissao(rsm.getGregorianCalendar("dt_emissao"));
				cartao.setDtValidade(rsm.getGregorianCalendar("dt_validade"));
				cartao.setIdCartao(rsm.getString("id_cartao"));
				cartao.setLgAcompanhante(rsm.getInt("lg_acompanhante"));
				cartao.setNrVia(rsm.getInt("nr_via"));
				cartao.setStCartao(rsm.getInt("st_cartao"));
				cartao.setTpCartao(rsm.getInt("tp_cartao"));
				cartao.setTpVigencia(rsm.getInt("tp_vigencia"));
				return cartao;
			}
			
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSetMap findCartaoDocumento(ArrayList<ItemComparator> criterios) {
		return findCartaoDocumento(criterios, null);
	}

	public static ResultSetMap findCartaoDocumento(ArrayList<ItemComparator> criterios, Connection connect) {
		int qtLimite = 0;
		String idDoenca = null;
		
		String orderBy = "ORDER BY A.CD_DOCUMENTO DESC ";

		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
				orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			} else if (criterios.get(i).getColumn().equalsIgnoreCase("IDDOENCA")) {
				idDoenca = criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}

		String sql = " SELECT A.cd_documento, A.nr_documento, C.cd_cartao_documento, D.cd_situacao_documento, D.nm_situacao_documento, E.cd_pessoa AS cd_solicitante, E.nm_pessoa AS nm_solicitante, F.nr_rg," + 
					 " H.cd_pessoa AS cd_atendente, H.nm_pessoa AS nm_atendente, I.dt_emissao, I.dt_validade, I.cd_cartao " +
					 " FROM ptc_documento 					A " + 
					 " JOIN ptc_documento_pessoa            B ON ( A.cd_documento = B.cd_documento )" + 
					 " JOIN mob_cartao_documento           	C ON ( A.cd_documento = C.cd_documento ) " + 
					 " JOIN ptc_situacao_documento         	D ON ( A.cd_situacao_documento = D.cd_situacao_documento )" + 
					 " JOIN grl_pessoa                     	E ON ( B.cd_pessoa = E.cd_pessoa )" + 
					 " JOIN grl_pessoa_fisica              	F ON ( E.cd_pessoa = F.cd_pessoa )" + 
					 " JOIN seg_usuario						G ON ( G.cd_usuario = A.cd_usuario )" + 
					 " JOIN grl_pessoa                     	H ON ( H.cd_pessoa = G.cd_pessoa )" + 
					 " JOIN mob_cartao                     	I ON ( C.cd_cartao = I.cd_cartao )" +
					 " WHERE 1=1 " +
					 ( idDoenca != null? " AND EXISTS(SELECT * FROM grl_doenca J," +
					 		" mob_cartao_documento_doenca K WHERE J.cd_doenca = K.cd_doenca AND K.cd_cartao_documento = C.cd_cartao_documento " +
					 		" AND J.id_doenca = '" + idDoenca + "') ": "");

		ResultSetMap rsm = Search.find(sql, ( orderBy + (qtLimite > 0 ? " LIMIT " + qtLimite : "" )), criterios, 
				connect != null ? connect : Conexao.conectar(), connect == null);
				
		 while (rsm.next() ) {
			DocumentoOcorrencia ultimaOcorrencia = DocumentoOcorrenciaServices.getUltimaOcorrencia(rsm.getInt("CD_DOCUMENTO"));
			if(ultimaOcorrencia!= null) {
				rsm.setValueToField("TXT_ULTIMA_OCORRENCIA", ultimaOcorrencia.getTxtOcorrencia());
			}
		 }
		 rsm.beforeFirst();
		
		return rsm;
	}
	
	public static ResultSetMap findRelatorioSolicitacoes(ArrayList<ItemComparator> criterios) {
		return findRelatorioSolicitacoes(criterios, null);
	}

	public static ResultSetMap findRelatorioSolicitacoes(ArrayList<ItemComparator> criterios, Connection connect) {
		String idDoenca = null;

		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("ID_DOENCA")) {
				idDoenca = criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}

		String sql = " SELECT A.cd_documento, A.nr_documento, A.dt_protocolo, D.cd_situacao_documento, D.nm_situacao_documento, E.cd_pessoa AS cd_solicitante, E.nm_pessoa AS nm_solicitante, F.nr_rg," + 
					 " H.cd_pessoa AS cd_atendente, H.nm_pessoa AS nm_atendente, C.cd_cartao_documento " +
					 " FROM ptc_documento 					A " + 
					 " JOIN ptc_documento_pessoa            B ON ( A.cd_documento = B.cd_documento )" + 
					 " JOIN mob_cartao_documento           	C ON ( A.cd_documento = C.cd_documento ) " + 
					 " JOIN ptc_situacao_documento         	D ON ( A.cd_situacao_documento = D.cd_situacao_documento )" + 
					 " JOIN grl_pessoa                     	E ON ( B.cd_pessoa = E.cd_pessoa )" + 
					 " JOIN grl_pessoa_fisica              	F ON ( E.cd_pessoa = F.cd_pessoa )" + 
					 " JOIN seg_usuario						G ON ( G.cd_usuario = A.cd_usuario )" + 
					 " JOIN grl_pessoa                     	H ON ( H.cd_pessoa = G.cd_pessoa )" +
					 " WHERE 1=1 " +
					 ( idDoenca != null? " AND EXISTS(SELECT * FROM grl_doenca J," +
					 		" mob_cartao_documento_doenca K WHERE J.cd_doenca = K.cd_doenca AND K.cd_cartao_documento = C.cd_cartao_documento " +
					 		" AND J.id_doenca = '" + idDoenca + "') ": "");

		ResultSetMap rsm = Search.find(sql, " ORDER BY A.CD_DOCUMENTO ASC", criterios, 
				connect != null ? connect : Conexao.conectar(), connect == null);
				
		 while (rsm.next() ) {
			DocumentoOcorrencia ultimaOcorrencia = DocumentoOcorrenciaServices.getUltimaOcorrencia(rsm.getInt("CD_DOCUMENTO"));
			if(ultimaOcorrencia!= null) {
				rsm.setValueToField("TXT_ULTIMA_OCORRENCIA", ultimaOcorrencia.getTxtOcorrencia());
			}
			String idCids = "";
			ArrayList<ItemComparator> criterioCartao = new ArrayList<ItemComparator>();
			criterioCartao.add(new ItemComparator("cd_cartao_documento", rsm.getString("cd_cartao_documento"), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmCartaoDocumentoDoenca = CartaoDocumentoDoencaDAO.find(criterioCartao, connect);
			
			while(rsmCartaoDocumentoDoenca.next()) {
				Doenca doenca = DoencaDAO.get(rsmCartaoDocumentoDoenca.getInt("cd_doenca"), connect);
				idCids += doenca.getIdDoenca() + ", ";
			}
			rsm.setValueToField("ID_CIDS", idCids.substring(0, idCids.length()-2));
		 }
		 
		 rsm.beforeFirst();

		return rsm;
	}

	public static List<CartaoDocumentoDTO> findSolicitacoesDTO(ArrayList<ItemComparator> criterios) {
		return findSolicitacoesDTO(criterios, null);
	}

	public static List<CartaoDocumentoDTO> findSolicitacoesDTO(ArrayList<ItemComparator> criterios,
			Connection connect) {
		
		ResultSetMap rsm = findSolicitacoes(criterios);
		
		List<CartaoDocumentoDTO> list = new ArrayList<CartaoDocumentoDTO>();
		

		while (rsm.next()) {
			CartaoDocumentoDTO dto = new CartaoDocumentoDTO();

			Documento documento = DocumentoDAO.get(rsm.getInt("CD_DOCUMENTO"));
			dto.setDocumento(documento);

			Pessoa pessoa = PessoaDAO.get(rsm.getInt("CD_PESSOA"));
			dto.setPessoa(pessoa);

			SituacaoDocumento situacaoDocumento = SituacaoDocumentoDAO.get(rsm.getInt("CD_SITUACAO_DOCUMENTO"));
			dto.setSituacaoDocumento(situacaoDocumento);
			
			DocumentoOcorrencia documentoOcorrencia = DocumentoOcorrenciaDAO.get(rsm.getInt("CD_DOCUMENTO"), rsm.getInt("CD_OCORRENCIA"), rsm.getInt("CD_TIPO_OCORRENCIA"));
			dto.setDocumentoOcorrencia(documentoOcorrencia);

			list.add(dto);
			
		}
		
		 rsm.beforeFirst();
		
		 return list;

	}
}
