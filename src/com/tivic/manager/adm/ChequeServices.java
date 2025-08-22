package com.tivic.manager.adm;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;
import sol.util.DateServices;

public class ChequeServices {
	public static final int NAO_EMITIDO = 0; 
	public static final int EMITIDO = 1;
//	+ MOV NÃO COMPESADA = ESTORNA AS MOVIMENTAÇÕES(COMPENSANDO TODAS)
	public static final int IMPRESSO = 2;
	public static final int CANCELADO = 3;
	public static final int COMPENSADO = 4;
	public static final int APRESENTADO_1 = 5; 
	public static final int APRESENTADO_2 = 6;
	public static final int CONTRA_ORDEM = 7;
	public static final int RESGATADO = 8;

	public static String[] situacaoCheque = { "Não Emitido", "Emitido",
			"Não Compensado", "Cancelado", "Compensado", "1ª Apresentação",
			"2ª Apresentação", "Contra-Ordem", "Resgatado" };

	public static final int ERR_CANC_CHEQUE_NAO_EMITIDO = -10;
	public static final int ERR_FILA_IMP_CHEQUE_COMP = -20;
	
	
	public static Result save(Cheque cheque){
		return save(cheque, null);
	}

	public static Result save(Cheque cheque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cheque==null)
				return new Result(-1, "Erro ao salvar. Cheque é nulo");
			
			cheque.setNrCheque( Util.fillNum( Integer.parseInt(cheque.getNrCheque()), 6));
			//verifica se existe um cheque com mesma numeração
			ResultSet rsCheque = connect.prepareStatement(
										" SELECT * FROM ADM_CHEQUE "+
										"	WHERE NR_CHEQUE = '"+cheque.getNrCheque()+"'"+
										"	AND CD_CONTA = "+cheque.getCdConta()+
										"	AND CD_CHEQUE <> "+cheque.getCdCheque()
												).executeQuery(); 
			if( rsCheque.next() )
				return new Result(-1, "Cheque nº "+ cheque.getNrCheque()+" já está cadastrado para a conta informada.");
			
			int retorno;
			if(cheque.getCdCheque()==0){
				retorno = ChequeDAO.insert(cheque, connect);
				cheque.setCdCheque(retorno);
			}
			else {
				retorno = ChequeDAO.update(cheque, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CHEQUE", cheque);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insertFaixa(int cdConta, int nrChequeInicial, int nrChequeFinal){
		return insertFaixa(cdConta, nrChequeInicial, nrChequeFinal, null);
	}
	
	public static int insertFaixa(int cdConta, int nrChequeInicial,
			int nrChequeFinal, String idTalao) {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT * FROM adm_cheque "
							+ "WHERE cd_conta  = " + cdConta
							+ "  AND nr_cheque = ?");
			for (int i = nrChequeInicial; i <= nrChequeFinal; i++) {
				pstmt.setString(1, Util.fillNum(i, 6));
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					Cheque cheque = new Cheque(0, cdConta, Util.fillNum(i, 6),
							null, new GregorianCalendar(), null, idTalao,
							NAO_EMITIDO, "");
					ChequeDAO.insert(cheque, connect);
				}else{
					Conexao.rollback(connect);
					return -2;
				}
			}
			connect.commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.insert: " + e);
			return -1;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdCheque){
		return remove(cdCheque, false, null);
	}
	public static Result remove(int cdCheque, boolean cascade){
		return remove(cdCheque, cascade, null);
	}
	public static Result remove(int cdCheque, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = ChequeDAO.delete(cdCheque, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static int deleteFaixa(int cdConta, int nrChequeInicial,
			int nrChequeFinal) {
		Connection connect = Conexao.conectar();
		try {
			if (cdConta <= 0)
				return -1;
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM adm_cheque " + "WHERE cd_conta = " + cdConta
							+ "  AND nr_cheque >= \'" + Util.fillNum(nrChequeInicial, 6) + "\' "
							+ "  AND nr_cheque <= \'" + Util.fillNum(nrChequeFinal,6) + "\' "
							+ "  AND st_cheque IN (" + CANCELADO + ","
							+ NAO_EMITIDO + ")").executeQuery();
			while (rs.next())
				ChequeDAO.delete(rs.getInt("cd_cheque"), connect);
			return 1;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeDAO.insert: " + e);
			return -1;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static Result cancelarCheque(int cdCheque) {
		return setSituacaoCheque(cdCheque, CANCELADO, 0, null);
	}

	public static Cheque getNextCheque(int cdConta) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT * FROM adm_cheque A "
							+ "WHERE A.cd_conta  = "
							+ cdConta
							+ "  AND A.nr_cheque = (SELECT MIN(nr_cheque) FROM adm_cheque B "
							+ "                     WHERE A.cd_conta = B.cd_conta "
							+ "                       AND B.st_cheque = "
							+ NAO_EMITIDO + ")");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return new Cheque(rs.getInt("cd_cheque"),
						rs.getInt("cd_conta"), rs.getString("nr_cheque"),
						(rs.getTimestamp("dt_emissao") == null) ? null : Util
								.longToCalendar(rs.getTimestamp("dt_emissao")
										.getTime()),
						(rs.getTimestamp("dt_liberacao") == null) ? null : Util
								.longToCalendar(rs.getTimestamp("dt_liberacao")
										.getTime()),
						(rs.getTimestamp("dt_impressao") == null) ? null : Util
								.longToCalendar(rs.getTimestamp("dt_impressao")
										.getTime()), rs.getString("id_talao"),
						rs.getInt("st_cheque"), rs.getString("ds_observacao"));
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.getNextCheque: " + e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelarCheques(int[] cheques, int cdUsuario) {
		return cancelarCheques(cheques, cdUsuario, null);
	}
	public static Result cancelarCheques(int[] cheques, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result r;
			for( int i=0; i<cheques.length;i++ ){
				
				r = setSituacaoCheque(cheques[i], CANCELADO, cdUsuario, connect);
				if( r.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-3, "Erro ao cancelar cheques!!");
				}
			}
			connect.commit();
			return new Result(1,"Cheque Cancelados com sucesso!");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.setSituacaoCheque: " + e);
			return null;
		} finally {
			if( isConnectionNull )
				Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelarTalao( int cdConta, String idTalao, int cdUsuario) {
		return cancelarTalao(cdConta, idTalao, cdUsuario, null);
	}
	public static Result cancelarTalao(int cdConta, String idTalao, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSetMap rsmCheques = new ResultSetMap( connect.prepareStatement(
					" SELECT * FROM ADM_CHEQUE "+
					" WHERE cd_conta = "+cdConta+
					" AND id_talao = '"+idTalao+"'"+
					" AND ST_CHEQUE = "+NAO_EMITIDO ).executeQuery() );
			rsmCheques.beforeFirst();
			int [] cheques = new int[rsmCheques.size()];
			while( rsmCheques.next() ){
				cheques[rsmCheques.getPointer()] = rsmCheques.getInt("CD_CHEQUE");
			}
			
			Result r = cancelarCheques(cheques, cdUsuario, connect);
			if( r.getCode() <= 0 ){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Erro ao cancelar Talão!");
			}
			return new Result(1,"Talão cancelado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.setSituacaoCheque: " + e);
			return null;
		} finally {
			if( isConnectionNull )
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> getNextChequeAsResultSet(int cdConta) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT * FROM adm_cheque A "
							+ "WHERE A.cd_conta  = "
							+ cdConta
							+ "  AND A.nr_cheque = (SELECT MIN(nr_cheque) FROM adm_cheque B "
							+ "                     WHERE A.cd_conta = B.cd_conta "
							+ "                       AND B.st_cheque = "
							+ NAO_EMITIDO + ")");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm.next() ? rsm.getRegister() : null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.getNextCheque: " + e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public static Result verificaSituacaoCheque(int cdCheque, int stCheque,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			int cdEmpresa = 0;
			ResultSet rs = connect.prepareStatement(
					"SELECT cd_empresa FROM adm_conta_financeira A, adm_cheque B "
							+ "WHERE A.cd_conta = B.cd_conta "
							+ "  AND B.cd_cheque = " + cdCheque).executeQuery();
			if (rs.next())
				cdEmpresa = rs.getInt("cd_empresa");
			boolean lgImprimeCheque = ParametroServices
					.getValorOfParametroAsInteger("LG_IMPRESSAO_CHEQUE", 0,
							cdEmpresa, connect) == 1;
			// Se não for um cheque válido retorna como operação bem sucedida
			if (cdCheque == 0)
				return new Result(1);
			if (isConnectionNull)
				connect = Conexao.conectar();
			Cheque cheque = ChequeDAO.get(cdCheque, connect);
			int newSituacao = stCheque;
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
					"SELECT * FROM adm_cheque A, adm_movimento_conta B "
							+ "WHERE B.cd_cheque = " + cdCheque
							+ "  AND A.cd_conta  = B.cd_conta"
							+ "  AND A.cd_cheque = B.cd_cheque ")
					.executeQuery());
			// Se não existir movimento relacionado ao cheque ele só pode estar
			// ou cancelado ou não emitido
			if (rsm.size() == 0)
				newSituacao = cheque.getStCheque() == CANCELADO ? CANCELADO
						: NAO_EMITIDO;
			/*
			 * Caso haja movimentação relacionada ao cheque executa outras
			 * verificações Situações como CONTRA-ORGEM e RESGATE são definidas
			 * pelo usuário e não devem ser alteradas pelo sistema
			 */
			else if (stCheque != CONTRA_ORDEM && stCheque != RESGATADO) {
				int qtEstornos = 0;
				boolean hasCompensacao = false, hasCancelamento = false;
				while (rsm.next()) {
					qtEstornos += rsm.getInt("tp_movimento") == MovimentoContaServices.CREDITO ? 1
							: 0;
					int stMov = rsm.getInt("st_movimento");
					hasCancelamento = hasCancelamento
							|| rsm.getInt("st_movimento") == MovimentoContaServices.ST_CANCELADO;
					if (rsm.getInt("tp_movimento") == MovimentoContaServices.DEBITO)
						hasCompensacao = hasCompensacao
								|| (stMov == MovimentoContaServices.ST_COMPENSADO)
								|| // Compensado
								(stMov == MovimentoContaServices.ST_LIQUIDADO)
								|| // Liquidado
								(stMov == MovimentoContaServices.ST_CONCILIADO); // Conciliado
				}
				// Se tiver compensação e não tiver estorno
				if (hasCompensacao && qtEstornos == 0)
					newSituacao = COMPENSADO;
				// Se tiver cancelamento e for um único lançamento
				else if (hasCancelamento && rsm.size() == 1)
					newSituacao = CANCELADO;
				// Primeira apresentação
				else if (qtEstornos == 1 && rsm.size() > 1)
					newSituacao = APRESENTADO_1;
				// Segunda apresentação
				else if (qtEstornos == 2)
					newSituacao = APRESENTADO_2;
				else {
					if (!lgImprimeCheque)
						newSituacao = IMPRESSO;
					else
						newSituacao = (stCheque == EMITIDO || cheque
								.getStCheque() == EMITIDO) ? EMITIDO : IMPRESSO;
				}
			}
			Result ret = setSituacaoCheque(cdCheque, newSituacao, 0, connect);
			if (ret.getCode() <= 0)
				return ret;
			return new Result(newSituacao);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1,
					"Erro ao tentar verificar situação do cheque!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result substituirCheque(int cdConta, int cdMovimentoConta,
			int cdFormaPagamento, GregorianCalendar dtMovimento,
			String dsHistorico, int cdNovoCheque, int cdUsuario,
			boolean lgResgatado) {
		return substituirCheque(cdConta, cdMovimentoConta, cdFormaPagamento,
				dtMovimento, dsHistorico, cdNovoCheque, cdUsuario, lgResgatado,
				null);
	}

	public static Result substituirCheque(int cdConta, int cdMovimentoConta,
			int cdFormaPagamento, GregorianCalendar dtMovimento,
			String dsHistorico, int cdNovoCheque, int cdUsuario,
			boolean lgResgatado, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			boolean lgImprimeCheque = ParametroServices
					.getValorOfParametroAsInteger("LG_IMPRESSAO_CHEQUE", 0, 0,
							connect) == 1;
			// Se não for um cheque válido retorna como operação bem sucedida
			if (cdConta == 0 || cdMovimentoConta == 0 || cdFormaPagamento == 0
					|| dtMovimento == null)
				return new Result(-10,
						"Cheque, forma de pagamento ou data do movimento não informado!");
			// Verifica a nova forma de pagamento
			FormaPagamento formaPag = FormaPagamentoDAO.get(cdFormaPagamento,
					connect);
			if (formaPag.getTpFormaPagamento() == FormaPagamentoServices.TITULO_CREDITO
					&& cdNovoCheque <= 0)
				return new Result(-10, "Novo cheque não informado!");
			if (formaPag.getTpFormaPagamento() != FormaPagamentoServices.TITULO_CREDITO
					&& cdNovoCheque > 0)
				return new Result(-10, "A forma de pagamento informada ("
						+ formaPag.getNmFormaPagamento()
						+ ") não permite a informação de cheque!");
			// Verifica se o movimento corresponde a um cheque
			MovimentoConta movimento = MovimentoContaDAO.get(cdMovimentoConta,
					cdConta, connect);
			if (movimento == null || movimento.getCdCheque() == 0) {
				com.tivic.manager.util.Util.registerLog(new Exception(
						"Código do movimento inválido! cdConta=" + cdConta
								+ ", cdMovimentoConta=" + cdMovimentoConta
								+ "\n movimento: " + movimento));
				return new Result(-10, "Código do movimento inválido!");
			}
			//
			connect.setAutoCommit(false);
			Result result = null;
			if (lgResgatado) {
				// Cancelamento movimento
				result = MovimentoContaServices.cancelarMovimento(cdConta,
						cdMovimentoConta, cdUsuario, RESGATADO, connect);
				if (result.getCode() <= 0) {
					Conexao.rollback(connect);
					return result;
				}
			} else
				result = setSituacaoCheque(movimento.getCdCheque(),
						CONTRA_ORDEM);
			if (result.getCode() <= 0) {
				Conexao.rollback(connect);
				return result;
			}
			// Buscando informações do movimento de origem
			// Pagamentos
			ResultSetMap rsmPagamentos = MovimentoContaPagarServices
					.getPagamentoOfMovimento(cdConta, cdMovimentoConta, connect);
			ArrayList<MovimentoContaPagar> movimentoConta = new ArrayList<MovimentoContaPagar>();
			while (rsmPagamentos.next())
				movimentoConta.add(new MovimentoContaPagar(cdConta,
						0 /* cdMovimentoConta */, rsmPagamentos
								.getInt("cd_conta_pagar"), rsmPagamentos
								.getFloat("vl_pago"), rsmPagamentos
								.getFloat("vl_multa"), rsmPagamentos
								.getFloat("vl_juros"), rsmPagamentos
								.getFloat("vl_desconto")));
			// Categorias
			ResultSetMap rsmCategorias = MovimentoContaCategoriaServices
					.getCategoriaOfMovimento(cdConta, cdMovimentoConta, connect);
			ArrayList<MovimentoContaCategoria> categorias = new ArrayList<MovimentoContaCategoria>();
			while (rsmCategorias.next())
				categorias.add(new MovimentoContaCategoria(cdConta,
						0 /* cdMovimentoConta */, rsmCategorias
								.getInt("cd_categoria_economica"),
						rsmCategorias.getFloat("vl_movimento_categoria"),
						0 /* cdMovimentoContaCategoria */, rsmCategorias
								.getInt("cd_conta_pagar"), rsmCategorias
								.getInt("cd_conta_receber"), rsmCategorias
								.getInt("tp_movimento"), rsmCategorias
								.getInt("cd_centro_custo")));

			// Preparando o novo movimento
			movimento.setCdMovimentoConta(0);
			movimento.setCdContaOrigem(cdConta);
			movimento.setCdMovimentoOrigem(cdMovimentoConta);
			movimento.setDtMovimento(dtMovimento);
			movimento.setCdCheque(cdNovoCheque);
			movimento.setCdUsuario(cdUsuario);
			movimento.setDsHistorico(dsHistorico);
			movimento.setCdFormaPagamento(cdFormaPagamento);
			movimento.setNrDocumento("S: " + movimento.getNrDocumento());
			result = MovimentoContaServices.insert(movimento, movimentoConta,
					categorias, lgImprimeCheque, connect);

			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				result.setMessage("Falha ao substituir cheque! \n"
						+ result.getMessage());
				return result;
			}
			if (isConnectionNull)
				connect.commit();
			return result;
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1,
					"Erro ao tentar verificar situação do cheque!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cheque objeto) {
		Connection connect = Conexao.conectar();
		Result ret = setSituacaoCheque(objeto.getCdCheque(),
				objeto.getStCheque(), 0, connect);
		if (ret.getCode() > 0)
			ret.setCode(ChequeDAO.update(objeto, connect));
		return ret.getCode();
	}
	
	public static Result setSituacaoCheque(	Cheque cheque, int stCheque,
			int cdUsuario) {
		
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			
			if( cheque.getCdCheque() == 0 ){
				return new Result(-1,"Cheque não informado!" );
			}
			/**
			 * Atualiza as observações do cheque
			 */
			save(cheque, connect);
			/**
			 * Cancela o cheque e possíveis movimentações vinculadas
			 */
			return setSituacaoCheque(cheque.getCdCheque(), stCheque, cdUsuario, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.setSituacaoCheque: " + e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Result setSituacaoCheque(int cdCheque, int stCheque,
			int cdUsuario) {
		return setSituacaoCheque(cdCheque, stCheque, cdUsuario, null);
	}

	public static Result setSituacaoCheque(int cdCheque, int stCheque) {
		return setSituacaoCheque(cdCheque, stCheque, 0, null);
	}

	private static Result setSituacaoCheque(int cdCheque, int stCheque,
			int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			Cheque cheque = ChequeDAO.get(cdCheque, connect);
			// Se a situação for igual sai
			if ((cheque.getStCheque() == stCheque)
					|| (stCheque == CANCELADO && cheque.getStCheque() == CONTRA_ORDEM))
				return new Result(1);
			/**
			 *  Só pode cancelar um cheque não emitido
			 *  OU
			 *  Cheque emitido com movimentação não compensada
			 *  	gerando um estorno destas movimentações
			 */
			if (stCheque == CANCELADO && cheque.getStCheque() != NAO_EMITIDO) {
				
				//Cancelamento nao emitido
				ResultSet rs = connect.prepareStatement(
						"SELECT * FROM adm_movimento_conta "
								+ "WHERE cd_cheque = " + cdCheque)
						.executeQuery();
				if( rs.next() ){
					// estorna movimentos
					if( rs.getInt("st_movimento") != MovimentoContaServices.ST_CANCELADO 
						&& rs.getInt("st_movimento") != MovimentoContaServices.ST_NAO_COMPENSADO ){
						return new Result(ERR_CANC_CHEQUE_NAO_EMITIDO,
								"Não é permitido cancela um cheque emitido!");
						
					}
					MovimentoContaServices.setSituacaoMovimento(rs.getInt("CD_CONTA"), rs.getInt("CD_MOVIMENTO_CONTA"), MovimentoContaServices.ST_CANCELADO);
				}
			}
			// Só pode colocar na fila de impressão cheque ainda não compensado
			if ((stCheque == EMITIDO)
					&& (cheque.getStCheque() == COMPENSADO || cheque
							.getStCheque() == CANCELADO))
				return new Result(ERR_FILA_IMP_CHEQUE_COMP,
						"Não é possível enviar um cheque já compensado para a fila de impressão!");
			// Data de emissão
			if ((stCheque == EMITIDO || stCheque == CANCELADO)
					|| (stCheque == IMPRESSO && cheque.getDtEmissao() == null))
				cheque.setDtEmissao(new GregorianCalendar());
			// Data de impressao
			if (stCheque == IMPRESSO
					|| (stCheque == COMPENSADO && cheque.getDtImpressao() == null))
				cheque.setDtImpressao(new GregorianCalendar());
			// Zera data de emissão e impressão
			if (stCheque == NAO_EMITIDO) {
				cheque.setDtEmissao(null);
				cheque.setDtImpressao(null);
			}
			// Verifica resgate de cheque
			if (stCheque == RESGATADO) {
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
						"SELECT * FROM adm_cheque A, adm_movimento_conta B "
								+ "WHERE B.cd_cheque = " + cdCheque
								+ "  AND A.cd_conta  = B.cd_conta"
								+ "  AND A.cd_cheque = B.cd_cheque "
								+ "  AND B.st_movimento IN ("
								+ MovimentoContaServices.ST_COMPENSADO + ","
								+ MovimentoContaServices.ST_CONCILIADO + ") "
								+ "ORDER BY B.dt_movimento").executeQuery());
				int qtEstornos = 0, qtDebitos = 0;
				while (rsm.next()) {
					qtEstornos += rsm.getInt("tp_movimento") == MovimentoContaServices.CREDITO ? 1
							: 0;
					qtDebitos += rsm.getInt("tp_movimento") == MovimentoContaServices.DEBITO ? 1
							: 0;
				}
				// Só permite se não tiver compensado ou mesmo se estiver
				// estornado
				if (qtEstornos != qtDebitos
						&& cheque.getStCheque() != CONTRA_ORDEM)
					return new Result(
							-1,
							"Não existe o estorno ou não existe nenhum lançamento para o este cheque, "
									+ "não é possível marcar como resgatado! Débitos: "
									+ qtDebitos + ", Estornos: " + qtEstornos);
			}
			// Contra-Ordem
			if (stCheque == CONTRA_ORDEM) {
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
						"SELECT * FROM adm_cheque A, adm_movimento_conta B "
								+ "WHERE B.cd_cheque = " + cdCheque
								+ "  AND A.cd_conta  = B.cd_conta"
								+ "  AND A.cd_cheque = B.cd_cheque ")
						.executeQuery());
				int qtEstornos = 0, qtDebitos = 0;
				while (rsm.next()) {
					qtEstornos += rsm.getInt("tp_movimento") == MovimentoContaServices.CREDITO ? 1
							: 0;
					qtDebitos += rsm.getInt("tp_movimento") == MovimentoContaServices.DEBITO ? 1
							: 0;
				}
				if (qtDebitos <= 0)
					return new Result(-1,
							"Cheque não emitido, impossível registrar contra-ordem!");
				if (qtEstornos > 1)
					return new Result(
							-1,
							"Este cheque já foi apresentado duas vezes, não é mais possível emitir contra-ordem!");
				if (qtDebitos > qtEstornos && qtDebitos == 1) {
					rsm = new ResultSetMap(connect.prepareStatement(
							"SELECT * FROM adm_cheque A, adm_movimento_conta B "
									+ "WHERE B.cd_cheque = " + cdCheque
									+ "  AND A.cd_conta  = B.cd_conta"
									+ "  AND A.cd_cheque = B.cd_cheque "
									+ "  AND B.tp_movimento = "
									+ MovimentoContaServices.DEBITO)
							.executeQuery());
					rsm.last();
					if (rsm.size() > 0) {
						if (rsm.getInt("st_movimento") == MovimentoContaServices.ST_COMPENSADO
								|| rsm.getInt("st_movimento") == MovimentoContaServices.ST_CONCILIADO)
							return new Result(
									-1,
									"O movimento já foi compensado, você deve lançar o estorno antes de emitir contra-ordem!");
						connect.prepareStatement(
								"UPDATE adm_cheque SET st_cheque = "
										+ CONTRA_ORDEM + " WHERE cd_cheque = "
										+ cdCheque).executeUpdate();
						Result result = MovimentoContaServices
								.cancelarMovimento(rsm.getInt("cd_conta"),
										rsm.getInt("cd_movimento_conta"),
										cdUsuario, -1, connect);
						if (result.getCode() <= 0) {
							Conexao.rollback(connect);
							return result;
						}
					} else
						return new Result(
								-1,
								"Não foi possível registrar contra-ordem, movimentação de débito não localizada!");
				}
			}
			// Situação
			cheque.setStCheque(stCheque);
			return new Result(ChequeDAO.update(cheque, connect), "Cheque atualizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.setSituacaoCheque: " + e);
			return new Result(-1,
					"Erro ao tentar alterar a situação do cheque", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static ResultSetMap getFilaImpressao(int cdConta) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect
					.prepareStatement("SELECT A.*, B.cd_conta, B.cd_movimento_conta, B.dt_movimento, "
							+ "       B.vl_movimento , B.dt_deposito, B.ds_historico "
							+ "FROM adm_cheque A "
							+ "LEFT OUTER JOIN adm_movimento_conta B ON (A.cd_cheque = B.cd_cheque) "
							+ "WHERE A.cd_conta = "
							+ cdConta
							+ "  AND A.st_cheque = " + EMITIDO);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()) {
				String nmPessoa = rsm.getString("nm_pessoa");
				if (rsm.getInt("cd_movimento_conta") > 0 && nmPessoa == null) {
					ResultSet rs = connect
							.prepareStatement(
									"SELECT nm_pessoa "
											+ "FROM adm_movimento_conta_pagar A "
											+ "JOIN adm_conta_pagar B ON (A.cd_conta_pagar = B.cd_conta_pagar) "
											+ "JOIN grl_pessoa      C ON (B.cd_pessoa = C.cd_pessoa) "
											+ "WHERE A.cd_movimento_conta = "
											+ rsm.getInt("cd_movimento_conta")
											+ "  AND A.cd_conta           = "
											+ rsm.getInt("cd_conta"))
							.executeQuery();
					nmPessoa = "";
					while (rs.next())
						nmPessoa += (nmPessoa.equals("") ? "" : ", ")
								+ rs.getString("nm_pessoa");
				}
				rsm.setValueToField("NM_PESSOA", nmPessoa);
				rsm.setValueToField("DS_EXTENSO",
						Util.formatExtenso(rsm.getFloat("vl_movimento"), true));
				
				String dsExtenso1 = Util.formatExtenso(rsm.getDouble("vl_movimento"), true);
				String dsExtenso2 = Util.fill("", 62, '*', 'D')+" ).";
				if( dsExtenso1.length() > 60 ){
					dsExtenso2 = Util.fill( dsExtenso1.substring(60), 62, '*', 'D');
					dsExtenso1 = dsExtenso1.substring(0, 60);
				}else{
					dsExtenso1 = Util.fill( dsExtenso1, 60, '*', 'D');
				}
				rsm.setValueToField("DS_EXTENSO_1", dsExtenso1.toUpperCase());
				rsm.setValueToField("DS_EXTENSO_2", dsExtenso2.toUpperCase());
				rsm.setValueToField("DS_NOMINAL", Util.fill(nmPessoa, 62, '*', 'D').toUpperCase());
				
			}
			rsm.beforeFirst();
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.getFilaImpressao: " + e);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTaloes(int cdConta) {
		return getTaloes(cdConta, null);
	}
	public static ResultSetMap getTaloes(int cdConta , Connection connect){
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement(
					" select id_talao, count(cd_cheque) from adm_cheque "+
					" where cd_conta = "+cdConta+
					" and id_talao <> '' "+
					" group by id_talao "
				).executeQuery());
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.getTaloes: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getTalaoAbaixoEstoqueMinimo(int cdEmpresa) {
		return getTalaoAbaixoEstoqueMinimo(cdEmpresa, null);
	}
	public static ResultSetMap getTalaoAbaixoEstoqueMinimo(int cdEmpresa , Connection connect){
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int qtEstoqueMinimo = ParametroServices.getValorOfParametroAsInteger("QT_ESTOQUE_MINIMO_CHEQUE", 5);
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement("SELECT * FROM ( "+
							" SELECT id_talao, count( cd_cheque ) as qt_cheque, nm_conta "+
							" FROM ADM_CHEQUE A "+
							" JOIN ADM_CONTA_FINANCEIRA B ON ( A.CD_CONTA = B.CD_CONTA ) "+ 
							" WHERE ST_CHEQUE = "+NAO_EMITIDO+
							" AND ID_TALAO IS NOT NULL AND length(ID_TALAO) >= 1 "+
							" AND B.CD_EMPRESA =  "+cdEmpresa+
							" GROUP BY ID_TALAO, B.nm_conta ) AS SUB "+
							" WHERE SUB.qt_cheque > 0 AND SUB.qt_cheque <= "+qtEstoqueMinimo).executeQuery());
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.getTalaoEstoqueMinimo: " + e);
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
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ResultSetMap rsm = Search.find("SELECT A.*, C.nr_conta, C.nr_dv, G.nr_agencia,"+
					" H.nr_banco, H.nm_banco, I.*, J.* "+
					" FROM adm_cheque A "+
					" LEFT OUTER JOIN adm_conta_financeira     C ON (A.cd_conta     = C.cd_conta) "+
					" LEFT OUTER JOIN grl_agencia              G ON (C.cd_agencia   = G.cd_agencia) "+
					" LEFT OUTER JOIN grl_banco                H ON (G.cd_banco     = H.cd_banco) "+
					" LEFT OUTER JOIN adm_movimento_conta      I ON (A.cd_cheque    = I.cd_cheque) "+
					" LEFT OUTER JOIN SEG_USUARIO              J ON (I.cd_usuario   = J.cd_usuario) ",
					" ORDER BY ID_TALAO, NR_CHEQUE ASC ",
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			String dsFavorecido = "";
			while( rsm != null && rsm.next() ){
				dsFavorecido = "";
				ResultSet rs = connect.prepareStatement(
										" SELECT NM_PESSOA "+
										" FROM adm_movimento_conta_pagar A "+
										" JOIN adm_conta_pagar B on ( A.cd_conta_pagar = B.cd_conta_pagar )"+
										" JOIN grl_pessoa C on ( B.cd_pessoa = C.cd_pessoa )"+
										" WHERE A.cd_conta = "+rsm.getInt("CD_CONTA")+
										" AND A.cd_movimento_conta = "+rsm.getInt("CD_MOVIMENTO_CONTA")
									).executeQuery();
				
				while( rs.next() )
					dsFavorecido +=	rs.getString("NM_PESSOA")+",";				
				if( dsFavorecido.length() > 1 )	
					rsm.setValueToField("NM_FAVORECIDO", dsFavorecido.substring(0, dsFavorecido.length()-1 ));
			}
			
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.find: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findMovimentoCheques(ArrayList<ItemComparator> criterios) {
		return findMovimentoCheques(criterios, null);
	}
	
	public static ResultSetMap findMovimentoCheques(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT A.*, C.nr_conta, C.nr_dv, G.nr_agencia, H.nr_banco, H.nm_banco "
					+ " FROM adm_cheque A "
					+ " LEFT OUTER JOIN adm_conta_financeira C ON (A.cd_conta   = C.cd_conta) "
					+ " LEFT OUTER JOIN grl_agencia          G ON (C.cd_agencia = G.cd_agencia) "
					+ " LEFT OUTER JOIN grl_banco            H ON (G.cd_banco   = H.cd_banco) "
					+ " WHERE 1=1 ";
			int i = 0;
			String nmPessoa = "";
			GregorianCalendar dtInicial = null, dtFinal = null;
			boolean lgEmitidos = false;
			while (i < criterios.size()) {
				if (criterios.get(i).getColumn().toLowerCase().indexOf("st_cheque") >= 0) {
					int filtroSituacao = Integer.parseInt(criterios.get(i).getValue().trim());
					lgEmitidos = (filtroSituacao == EMITIDO || filtroSituacao == IMPRESSO || filtroSituacao == COMPENSADO);
				} else if (criterios.get(i).getColumn().toLowerCase().indexOf("nm_pessoa") >= 0) {
					nmPessoa = criterios.get(i).getValue().trim();
					criterios.remove(i);
					i = i > 1 ? i - 1 : 0;
					continue;
				} else if (criterios.get(i).getColumn().toLowerCase().indexOf("dt_movimento") >= 0) {
					if (criterios.get(i).getTypeComparation() == ItemComparator.MINOR_EQUAL)
						dtFinal = Util.stringToCalendar(criterios.get(i).getValue());
					else
						dtInicial = Util.stringToCalendar(criterios.get(i).getValue());
					criterios.remove(i);
					i = i > 1 ? i - 1 : 0;
					continue;
				}
				i++;
			}
			//
			if (!nmPessoa.equals("") || dtFinal != null || dtInicial != null)
				sql += " AND EXISTS (SELECT * FROM adm_movimento_conta B "
						+ (!nmPessoa.equals("") ? ", adm_movimento_conta_pagar D, adm_conta_pagar E, grl_pessoa F "
								: "")
						+ "             WHERE A.cd_cheque = B.cd_cheque "
						+ "               AND A.cd_conta  = B.cd_conta "
						+ (dtInicial != null ? " AND B.dt_movimento >= ? " : "")
						+ (dtFinal != null ? " AND B.dt_movimento <= ? " : "")
						+ (!nmPessoa.equals("") ? " AND B.cd_conta           = D.cd_conta "
								+ " AND B.cd_movimento_conta = D.cd_movimento_conta "
								+ " AND D.cd_conta_pagar = E.cd_conta_pagar "
								+ " AND E.cd_pessoa      = F.cd_pessoa "
								+ " AND F.nm_pessoa LIKE \'%"
								+ nmPessoa.toUpperCase() + "%\' "
								: "") + ")";
			//
			ArrayList<GregorianCalendar> datas = new ArrayList<GregorianCalendar>();
			for (int l = 0; l < criterios.size(); l++) {
				ItemComparator it = criterios.get(l);
				if (it.getTypeColumn() == Types.TIMESTAMP) {
					sql += " AND " + it.getColumn() + " "
							+ it.getOperatorComparation() + " ? ";
					datas.add(Util.stringToCalendar(it.getValue()));
				} else if (it.getTypeColumn() == Types.VARCHAR)
					sql += " AND " + it.getColumn() + " "
							+ it.getOperatorComparation() + " \'"
							+ it.getValue() + "\'";
				else
					sql += " AND " + it.getColumn() + " "
							+ it.getOperatorComparation() + " " + it.getValue();
			}
			sql += " ORDER BY A.nr_cheque ASC ";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			int index = 0;
			if (dtInicial != null) {
				index++;
				pstmt.setTimestamp(index,
						Util.convCalendarToTimestamp(dtInicial));
			}
			if (dtFinal != null) {
				index++;
				pstmt.setTimestamp(index, Util.convCalendarToTimestamp(dtFinal));
			}
			for (int l = 0; l < datas.size(); l++) {
				index++;
				pstmt.setTimestamp(index,
						Util.convCalendarToTimestamp(datas.get(l)));
			}
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while (rsm.next()) {
				int stCheque = rsm.getInt("st_cheque");
				if (stCheque != CONTRA_ORDEM || stCheque != RESGATADO) {
					Result result = verificaSituacaoCheque(
							rsm.getInt("cd_cheque"), stCheque, connect);
					if (result.getCode() > 0)
						rsm.setValueToField("st_cheque", result.getCode());
				}
				// Dados dos lançamentos
				ResultSet rs = connect.prepareStatement(
						"SELECT * FROM adm_movimento_conta "
								+ "WHERE cd_cheque = "
								+ rsm.getInt("cd_cheque")
								+ "  AND cd_conta  = " + rsm.getInt("cd_conta")
								+ " ORDER BY dt_movimento").executeQuery();
				int qtEstornos = 0, qtDebitos = 0;
				int cdContaPagar = 0;
				while (rs.next()) {
					qtEstornos += rs.getInt("tp_movimento") == MovimentoContaServices.CREDITO ? 1
							: 0;
					qtDebitos += rs.getInt("tp_movimento") == MovimentoContaServices.DEBITO ? 1
							: 0;
					// Dados da conta paga
					if (qtDebitos == 1) {
						rsm.setValueToField("CD_MOVIMENTO_CONTA",
								rs.getInt("cd_movimento_conta"));
						rsm.setValueToField("DT_MOVIMENTO",
								rs.getTimestamp("dt_movimento"));
						rsm.setValueToField("DT_DEPOSITO",
								rs.getTimestamp("dt_deposito"));
						rsm.setValueToField("VL_MOVIMENTO",
								rs.getFloat("vl_movimento"));
						rsm.setValueToField("DS_HISTORICO",
								rs.getString("ds_historico"));
						ResultSet rsPessoa = connect
								.prepareStatement(
										"SELECT nm_pessoa, A.cd_conta_pagar "
												+ "FROM adm_movimento_conta_pagar A "
												+ "JOIN adm_conta_pagar B ON (A.cd_conta_pagar = B.cd_conta_pagar) "
												+ "JOIN grl_pessoa      C ON (B.cd_pessoa = C.cd_pessoa) "
												+ "WHERE A.cd_movimento_conta = "
												+ rs.getInt("cd_movimento_conta")
												+ "  AND A.cd_conta           = "
												+ rs.getInt("cd_conta"))
								.executeQuery();
						nmPessoa = "";
						cdContaPagar = 0;
						while (rsPessoa.next()) {
							if (cdContaPagar == 0
									&& rsPessoa.getInt("cd_conta_pagar") > 0)
								cdContaPagar = rsPessoa
										.getInt("cd_conta_pagar");
							else if (cdContaPagar > 0
									&& rsPessoa.getInt("cd_conta_pagar") != cdContaPagar)
								cdContaPagar = 0;
							nmPessoa += (nmPessoa.equals("") ? "" : ", ")
									+ rsPessoa.getString("nm_pessoa");
						}
					}
					if (rsm.getInt("tp_movimento") == MovimentoContaServices.DEBITO)
						rsm.setValueToField("DT_ULTIMA_APRESENTACAO",
								rs.getTimestamp("dt_movimento"));
				}
				rsm.setValueToField("QT_APRESENTACAO", qtDebitos);
				rsm.setValueToField("QT_ESTORNO", qtEstornos);
				rsm.setValueToField("NM_PESSOA", nmPessoa);
				rsm.setValueToField("CD_CONTA_PAGAR", cdContaPagar);
			}

			if (lgEmitidos || dtFinal != null || dtInicial != null) {
				ArrayList<String> orderBy = new ArrayList<String>();
				orderBy.add("cd_conta");
				orderBy.add("dt_movimento");
				orderBy.add("nr_cheque");
				rsm.orderBy(orderBy);
			}
			rsm.beforeFirst();
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.find: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findFreeCheque(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT * FROM adm_cheque A "
					+ "WHERE NOT EXISTS (SELECT * FROM adm_movimento_conta B "
					+ "                  WHERE A.cd_cheque = B.cd_cheque) "
					+ "  AND A.st_cheque = " + NAO_EMITIDO;

			return Search.find(sql, "ORDER BY A.nr_cheque", criterios,
					Conexao.conectar(), true);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ChequeServices.findFreeCheque: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result negociacaoCheque(
			ArrayList<MovimentoConta> chequesOrigem,
			ArrayList<MovimentoConta> novosCheques, float vlJuros, int cdUsuario) {
		return negociacaoCheque(chequesOrigem, novosCheques, vlJuros,
				cdUsuario, null);
	}

	public static Result negociacaoCheque(
			ArrayList<MovimentoConta> chequesOrigem,
			ArrayList<MovimentoConta> novosCheques, float vlJuros,
			int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			connect.setAutoCommit(false);
			/*
			 * Calculando valores de origem para conferência
			 */
			float vlTotalOrigem = 0;
			for (int i = 0; i < chequesOrigem.size(); i++) {
				MovimentoConta movimento = MovimentoContaDAO.get(chequesOrigem
						.get(i).getCdMovimentoConta(), chequesOrigem.get(i)
						.getCdConta());
				vlTotalOrigem += movimento.getVlMovimento();
			}
			/*
			 * Calculando valores de destino para conferência
			 */
			float vlTotalDestino = 0;
			for (int i = 0; i < novosCheques.size(); i++) {
				vlTotalDestino += novosCheques.get(i).getVlMovimento();
			}
			if (vlTotalDestino - vlTotalOrigem - vlJuros - 0.1 > 0) {
			}
			/*
			 * Cancelando os movimentos de origem
			 */
			ArrayList<MovimentoContaPagar> pagamentos = new ArrayList<MovimentoContaPagar>();
			for (int i = 0; i < chequesOrigem.size(); i++) {
				MovimentoConta movimento = MovimentoContaDAO.get(chequesOrigem
						.get(i).getCdMovimentoConta(), chequesOrigem.get(i)
						.getCdConta());
				cancelarCheque(movimento.getCdCheque());
				// Armazenamento dos pagamentos
				ResultSetMap rsmPags = MovimentoContaPagarServices
						.getPagamentoOfMovimento(chequesOrigem.get(i)
								.getCdConta(), chequesOrigem.get(i)
								.getCdMovimentoConta(), connect);
				while (rsmPags.next()) {
					MovimentoContaPagar pag = MovimentoContaPagarDAO.get(
							rsmPags.getInt("cd_conta"),
							rsmPags.getInt("cd_movimento_conta"),
							rsmPags.getInt("cd_conta_pagar"), connect);
					pagamentos.add(pag);
				}
			}
			/*
			 * Lançando novos movimentos
			 */
			float vlJurosPorUnidade = (vlJuros / vlTotalOrigem);
			for (int i = 0; i < novosCheques.size(); i++) {
				// ArrayList<MovimentoContaCategoria> categorias = new
				// ArrayList<MovimentoContaCategoria>();
				ArrayList<MovimentoContaPagar> pags = new ArrayList<MovimentoContaPagar>();
				MovimentoConta movimento = novosCheques.get(i);
				double vlMovimento = movimento.getVlMovimento()
						- (vlJurosPorUnidade * movimento.getVlMovimento());
				// Rateio de pagamentos e categorias
				while (vlMovimento > 0 && pagamentos.size() > 0) {
					MovimentoContaPagar pagamento = pagamentos.get(0);
					if (vlMovimento + 0.01 > pagamentos.get(0).getVlPago())
						pagamentos.remove(0);

					pagamento
							.setVlJuros(pagamento.getVlJuros()
									+ vlJurosPorUnidade
									* pagamentos.get(0).getVlPago());
					pagamento
							.setVlPago(pagamento.getVlPago()
									+ vlJurosPorUnidade
									* pagamentos.get(0).getVlPago());
					pags.add(pagamentos.get(0));
					/*
					 * ResultSetMap rsmCategorias =
					 * ContaPagarCategoriaServices.getCategoriaOfContaPagar(,
					 * connect) categorias.add(new
					 * MovimentoContaCategoria(movimento.getCdConta(), 0
					 * /*cdMovimentoConta*,
					 * rsmCategorias.getInt("cd_categoria_economica"),
					 * rsmCategorias.getFloat("vl_movimento_categoria"), 0
					 * /*cdMovimentoContaCategoria*,
					 * rsmCategorias.getInt("cd_conta_pagar"),
					 * rsmCategorias.getInt("cd_conta_receber"),
					 * rsmCategorias.getInt("tp_movimento"));
					 */

				}
			}
			return new Result(1);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1,
					"Erro ao tentar lançar negociação de cheque!", e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result gerarRelatorioCheques(int cdEmpresa,
			ArrayList<ItemComparator> crt, GregorianCalendar dtInicial,
			GregorianCalendar dtFinal, int tpRelatorio, int tpOrdemSaida) {
		boolean isConnectionNull = true;
		Connection connection = null;

		String devolvidos = tpRelatorio == 0 ? " AND (A.st_titulo = 5 OR A.st_titulo = 6) "
				: null;
		String avista = tpRelatorio == 1 ? " AND (A.tp_emissao = 0) " : null;
		String aprazo = tpRelatorio == 2 ? " AND (A.tp_emissao = 1) " : null;

		String data = (tpOrdemSaida == 0 ? "I.DT_VENCIMENTO" : tpOrdemSaida == 1 ? "I.DT_EMISSAO" : "I.DT_RECEBIMENTO");
		
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection
					.getAutoCommit());

			String strDtInicial = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(dtInicial.getTime());
			String strDtFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(dtFinal.getTime());

			String sql = " SELECT A.*, C.*, G.nr_agencia, H.nr_banco, I.*, M.id_turno AS  cd_turno_recebimento, K.nr_cpf, L.nr_cnpj, J.nm_pessoa, M.id_turno, O.dt_movimento, P.id_turno AS cd_turno_movimento, J.TXT_OBSERVACAO "
					+ " FROM adm_titulo_credito A "
					+ " LEFT OUTER JOIN adm_conta_financeira 		C ON (A.cd_conta           =   C.cd_conta) "
					+ " LEFT OUTER JOIN grl_agencia          		G ON (C.cd_agencia         =   G.cd_agencia) "
					+ " LEFT OUTER JOIN grl_banco          			H ON (G.cd_banco           =   H.cd_banco) "
					+ " LEFT OUTER JOIN adm_conta_receber 			I ON (A.cd_conta_receber   =   I.cd_conta_receber) "
					+ " LEFT OUTER JOIN grl_pessoa        			J ON (I.cd_pessoa          =   J.cd_pessoa) "
					+ " LEFT OUTER JOIN grl_pessoa_fisica   			K ON (K.cd_pessoa          =   J.cd_pessoa) "
					+ " LEFT OUTER JOIN grl_pessoa_juridica  		L ON (L.cd_pessoa          =   J.cd_pessoa) "
					+ " LEFT OUTER JOIN adm_turno            		M ON (M.cd_turno  		   =   C.cd_turno) "
					+ " LEFT OUTER JOIN adm_movimento_conta_receber  N ON (I.cd_conta_receber   =   N.cd_conta_receber) "
					+ " LEFT OUTER JOIN adm_movimento_conta          O ON (N.cd_movimento_conta =   O.cd_movimento_conta AND N.cd_conta = O.cd_conta)"
					+ " LEFT OUTER JOIN adm_turno            		P ON (O.cd_turno  		   =   P.cd_turno) "
					+ " WHERE 1=1 "
					+ " AND  "+data+" >= '"	+ strDtInicial + "'"
					+ " AND  "+data+" <= '"	+  strDtFinal  + "'"
					+ " AND  A.cd_tipo_documento = 1 "
					+ (devolvidos == null ? "" : devolvidos)
					+ (avista == null ? "" : avista)
					+ (aprazo == null ? "" : aprazo) + "";

			ResultSetMap rsm = Search.find(sql, "", crt,
					connection != null ? connection : Conexao.conectar(),
					connection == null);

			while (rsm.next()) {

				if (rsm.getString("NR_CPF") != null
						&& !rsm.getString("NR_CPF").equals("")) {
					rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("NR_CPF"));
				} else {
					rsm.setValueToField("NR_CPF_CNPJ", rsm.getString("NR_CNPJ"));
				}

				if (aprazo != null) {
					rsm.setValueToField(
							"DIAS_PRAZO",
							DateServices.countDaysBetween(
									rsm.getGregorianCalendar("dt_emissao"),
									rsm.getGregorianCalendar("dt_vencimento")));
				}
				
				if(tpOrdemSaida == 0){
					rsm.setValueToField("DT_AGRUPAMENTO_DIARIO", rsm.getTimestamp("DT_VENCIMENTO"));
				}
				else if(tpOrdemSaida == 1){
					rsm.setValueToField("DT_AGRUPAMENTO_DIARIO", rsm.getTimestamp("DT_EMISSAO"));
				}
				
				else{
					rsm.setValueToField("DT_AGRUPAMENTO_DIARIO", rsm.getTimestamp("DT_RECEBIMENTO"));
				}
				
				rsm.setValueToField(
						"TOTAL_MES",
						"TOTAL DE "
								+ Util.formatDate(
										rsm.getGregorianCalendar("DT_AGRUPAMENTO_DIARIO"),
										"MM/yyyy"));

				
				rsm.setValueToField(
						"TOTAL_DIA",
						"TOTAL DE "
								+ Util.formatDate(
										rsm.getGregorianCalendar("DT_AGRUPAMENTO_DIARIO"),
										"dd/MM/yyyy"));

			}

			rsm.beforeFirst();

			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_AGRUPAMENTO_DIARIO");
			rsm.orderBy(fields);
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtInicial", Util.convCalendarString(dtInicial));
			param.put("dtFinal", Util.convCalendarString(dtFinal));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}