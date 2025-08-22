package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.mob.pagamento.AitPagamentoArquivo;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class AitPagamentoServices {

	public static final int PAGO_VIA_RENAINF = 0;
	public static final int PAGO_VIA_BOLETO = 1;

	public static Result save(AitPagamento aitPagamento) {
		return save(aitPagamento, null, null);
	}

	public static Result save(AitPagamento aitPagamento, AuthData authData) {
		return save(aitPagamento, authData, null);
	}

	public static Result save(AitPagamento aitPagamento, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (aitPagamento == null)
				return new Result(-1, "Erro ao salvar. AitPagamento é nulo");

			int retorno;
			if (aitPagamento.getCdPagamento() == 0) {
				retorno = AitPagamentoDAO.insert(aitPagamento, connect);
				aitPagamento.setCdPagamento(retorno);
			} else {
				retorno = AitPagamentoDAO.update(aitPagamento, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "AITPAGAMENTO",
					aitPagamento);
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

	public static Result save(ArquivoBancoDTO arquivoBanco) {
		return save(arquivoBanco, null);
	}

	public static Result save(ArquivoBancoDTO arquivoBanco, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Arquivo arquivo = new Arquivo();
			arquivo.setCdArquivo(0);
			arquivo.setNmArquivo("Retorno_" + arquivoBanco.getIdBanco() + "_"
					+ Util.formatDate(Util.getDataAtual(), "DD-MM-YYYY") + ".RET");
			arquivo.setBlbArquivo(Util.convertBase64(arquivoBanco.getArquivo()));
			arquivo.setDtArquivamento(new GregorianCalendar());
			arquivo.setDtCriacao(new GregorianCalendar());
			Result r = ArquivoServices.save(arquivo, null, connect);

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(),
					r.getCode() <= 0 ? "Erro ao salvar o arquivo." : "Arquivo salvo com sucesso");
		} catch (Exception e) {
			return null;
		}
	}

	public static List<RetornoBancoDTO> upload(ArquivoBancoDTO arquivoBanco, int cdUsuario)
			throws Exception, ValidacaoException {
		return upload(arquivoBanco, cdUsuario, null);
	}

	public static List<RetornoBancoDTO> upload(ArquivoBancoDTO arquivoBanco, int cdUsuario, Connection connect)
			throws Exception, ValidacaoException {
		
		List<RetornoBancoDTO> retornoBancoDTO = new ArrayList<RetornoBancoDTO>();
		retornoBancoDTO =  new AitPagamentoArquivo().lerArquivo(arquivoBanco, cdUsuario);
		return retornoBancoDTO; 
	}

	public static Result savePagamento(AitPagamento aitPagamento, int cdUsuario) {
		return savePagamento(aitPagamento, cdUsuario, null);
	}

	public static Result savePagamento(AitPagamento aitPagamento, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			if (aitPagamento == null)
				return new Result(-1, "Erro ao salvar. AitPagamento é nulo");

			int retorno = 0;
			if (aitPagamento.getCdPagamento() == 0) {
				retorno = AitPagamentoDAO.insert(aitPagamento, connect);

				AitMovimento movimentoPagamento = new AitMovimento(0, aitPagamento.getCdAit(), 0,
						aitPagamento.getDtPagamento(), 0, AitMovimentoServices.MULTA_PAGA, 0, null, 0,
						AitMovimentoServices.NAO_ENVIADO, 0, null, null, 0, 0, null, new GregorianCalendar(), 0, null,
						0, null, 0, 0, cdUsuario, 0, null, 0, null, 0, 0);
				// Lança o movimento do pagamento
				int rMovimento = AitMovimentoDAO.insert(movimentoPagamento, connect);

				movimentoPagamento.setNrMovimento(rMovimento);
				// Atualiza o nr do movimento
				AitMovimentoDAO.update(movimentoPagamento, connect);
				Ait ait = AitDAO.get(aitPagamento.getCdAit(), connect);

				// Atualizar AIT
				atualizarAIT(ait, aitPagamento.getTpPagamento());
				atualizarPagamento(movimentoPagamento.getCdMovimento(), aitPagamento);
			} else {
				retorno = AitPagamentoDAO.update(aitPagamento, connect);
			}

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "AITPAGAMENTO",
					aitPagamento);
		} catch (Exception e) {
			System.out.println("Exception e == " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap get() {
		return get(null);
	}

	public static ResultSetMap get(Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT B.nr_ait, B.nr_placa, B.nr_controle, A.* FROM mob_ait_pagamento A"
					+ " INNER JOIN mob_ait B ON B.cd_ait = A.cd_ait" + " ORDER BY dt_pagamento DESC LIMIT 500";
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}

		catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoServices.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoServices.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getPagamento(int cdAit) {
		return getPagamento(cdAit, null);
	}

	public static ResultSetMap getPagamento(int cdAit, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt;
			ResultSetMap rsm;
			String sql = "SELECT A.* FROM mob_ait_pagamento A" + " WHERE cd_ait =?";
			pstmt = connect.prepareStatement(sql);
			rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}

		catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoServices.get: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoServices.get: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static Result atulizarMovimento(Ait ait, AitMovimento aitMovimento, Connection connect) {
		ait.setCdMovimentoAtual(aitMovimento.getCdMovimento());
		Result r = AitServices.save(ait, null, connect);
		return r;
	}

	private static void verificarPagamentoError(String tPago, String tNotExist) throws ValidacaoException {
		if (tPago.length() > 34 && tNotExist.length() > 37) {
			throw new ValidacaoException(tPago + " & " + tNotExist);
		} else if (tPago.length() > 34) {
			throw new ValidacaoException(tPago);
		} else if (tNotExist.length() > 37) {
			throw new ValidacaoException(tNotExist);
		}
	}

	private static void atualizarAIT(Ait ait, int tpPagamento) {
		atualizarAIT(null, ait, tpPagamento);
	}

	private static void atualizarAIT(String nrCodigoBarras, Ait ait, int tpPagamento) {
		ait.setLgDetranFebraban(tpPagamento);
		ait.setNrCodigoBarras(nrCodigoBarras);
		AitDAO.update(ait);
	}
	
	private static void atualizarPagamento(int cdMovimento, AitPagamento aitPagamento) {
		aitPagamento.setCdMovimento(cdMovimento);
		AitPagamentoDAO.update(aitPagamento);
	}
}
