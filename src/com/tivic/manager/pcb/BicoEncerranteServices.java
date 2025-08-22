package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.ContaFechamento;
import com.tivic.manager.adm.ContaFechamentoDAO;
import com.tivic.manager.adm.ContaFechamentoServices;
import com.tivic.manager.adm.ContaFinanceira;
import com.tivic.manager.adm.ContaFinanceiraDAO;
import com.tivic.manager.adm.TipoOperacao;
import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.manager.adm.TurnoServices;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

@DestinationConfig(enabled = false)
public class BicoEncerranteServices {

	public static Result iniciarFechamento(int cdConta, int cdFechamento, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Dados do fechamento anterior
			ContaFechamento fechamento     = ContaFechamentoDAO.get(cdConta, cdFechamento, connect);
			GregorianCalendar dtFechamento = fechamento.getDtFechamento();
			if(dtFechamento == null)
				dtFechamento = new GregorianCalendar();
			dtFechamento.set(Calendar.HOUR, 0);
			dtFechamento.set(Calendar.MINUTE, 0);
			dtFechamento.set(Calendar.SECOND, 0);
			dtFechamento.set(Calendar.MILLISECOND, 0);
			// Tabela de Preço
			int cdTabelaPrecoPadrao   = 0;
			int cdTipoOperacao        = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
			TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connect);
			if(tipoOperacao!=null)
				cdTabelaPrecoPadrao = tipoOperacao.getCdTabelaPreco();
			// Preço
			PreparedStatement pstmtPreco = connect.prepareStatement("SELECT A.*, B.id_tabela_preco " +
					                                                "FROM adm_produto_servico_preco A, adm_tabela_preco B " +
					                                                "WHERE A.cd_tabela_preco  = B.cd_tabela_preco " +
					                                                "  AND (A.dt_termino_validade IS NULL OR A.dt_termino_validade > ?)" +
					                                                "  AND A.cd_produto_servico = ? " +
					                                                "  AND A.cd_tabela_preco    = ? " +
					                                                "ORDER BY A.dt_termino_validade ");
			//
			ResultSetMap rsm = BicoServices.getAll(cdEmpresa, connect);
			while(rsm.next())	{
				int cdBico           = rsm.getInt("cd_bico");
				int cdTanque         = rsm.getInt("cd_tanque");
				int cdProdutoServico = rsm.getInt("cd_produto_servico");
				int cdTabelaPreco    = rsm.getInt("cd_tabela_preco");
				BicoEncerrante encerrante = BicoEncerranteDAO.get(cdConta, cdFechamento, cdBico, connect);
				if (encerrante==null)	{
					// Encerrante Inicial
					float qtEncerranteInicial = getEncerranteInicial(cdBico, cdConta, dtFechamento, fechamento.getCdTurno(), connect);
					float qtEncerranteFinal   = qtEncerranteInicial;
					float vlAfericao=0, qtLitros=0, vlPreco=0, vlTotal=0;
					PreparedStatement pstmt = connect.prepareStatement("SELECT SUM(qt_saida) AS qt_saida " +
							                                           "FROM alm_documento_saida_item DSI, alm_documento_saida DS " +
																       "WHERE DSI.cd_documento_saida = DS.cd_documento_saida " +
																       "  AND DSI.cd_bico            = " +cdBico+
																       "  AND DS.cd_turno            = "+fechamento.getCdTurno()+
																       "  AND CAST(DS.dt_documento_saida AS DATE) = ? " +
																       "  AND DS.st_documento_saida  = 1 ");
					pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
					ResultSet rs = pstmt.executeQuery();
					if(rs.next() && false) {
						qtLitros = rs.getFloat("qt_saida");
						// Buscando o Preço
						pstmtPreco.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
						pstmtPreco.setInt(2, cdProdutoServico);
						pstmtPreco.setInt(3, cdTabelaPreco>0 ? cdTabelaPreco : cdTabelaPrecoPadrao);
						ResultSet rsPreco = pstmtPreco.executeQuery();
						if(rsPreco.next())
							vlPreco = rsPreco.getFloat("vl_preco");
						// Calculando Totais
						vlTotal            = qtLitros * vlPreco;
						qtEncerranteFinal += qtLitros;
					}	
					// Gerando e gravando os dados do encerrante	
					encerrante = new BicoEncerrante(cdBico, qtEncerranteInicial, qtEncerranteFinal, vlAfericao, qtLitros, vlPreco, vlTotal, cdConta, cdFechamento, cdTanque, cdProdutoServico);
					BicoEncerranteDAO.insert(encerrante, connect);
				}
			}
			
			return new Result(1);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Falha ao tentar incluir encerrantes no fechamento!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getEncerrantesOf(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno, int cdTabelaPreco) {
		return getEncerrantesOf(cdConta, cdFechamento, dtFechamento, cdTurno, cdTabelaPreco, null);
	}

	public static ResultSetMap getEncerrantesOf(int cdConta, int cdFechamento, GregorianCalendar dtFechamento, int cdTurno, int cdTabelaPreco, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
			if (cdFechamento <= 0)	{
				ResultSet rs = connect.prepareStatement("SELECT cd_fechamento FROM adm_conta_fechamento " +
														"WHERE cd_conta      = "+cdConta+
														"  AND cd_turno      = "+conta.getCdTurno()+
														"  AND dt_fechamento IS NULL").executeQuery();
				if(rs.next()) 
					cdFechamento = rs.getInt("cd_fechamento");
			}
			else	{
				if(dtFechamento==null) {
					ContaFechamento fechamento = ContaFechamentoDAO.get(cdConta, cdFechamento, connect);
					dtFechamento = fechamento.getDtFechamento(); 
				}
			}
			//
			if(dtFechamento!=null)	{
				dtFechamento.set(Calendar.HOUR, 0);
				dtFechamento.set(Calendar.MINUTE, 0);
				dtFechamento.set(Calendar.SECOND, 0);
				dtFechamento.set(Calendar.MILLISECOND, 0);
			}
			if(cdTabelaPreco<=0) {
				int cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, conta.getCdEmpresa());
				TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connect);
				if(tipoOperacao!=null)
					cdTabelaPreco = tipoOperacao.getCdTabelaPreco();
			}
			
			String sql = "SELECT BI.cd_bico, BI.id_bico, BI.nr_ordem, BI.st_bico, BI.dt_instalacao, BI.dt_exclusao, BI.dt_cadastro, BI.dt_ultima_alteracao, "+ 
                         "       BI.nr_casas_inteiras, BI.nr_casas_decimais, BI.qt_encerrante_inicial AS qt_encerrante_inicial_geral, "+
                         "       BI.vl_encerrante_inicial, BI.txt_observacao, BI.cd_bomba, BC.cd_tanque, BI.cd_tabela_preco, " +
					     "       BC.cd_conta, BC.cd_fechamento, BC.qt_encerrante_inicial, BC.qt_encerrante_final, BC.vl_afericao, " +
					     "       BC.qt_litros, BC.vl_preco, BC.vl_total, BO.nm_bomba, BO.id_bomba, BO.nr_ilha, L.nm_local_armazenamento AS nm_tanque, " +
					     "       PS.nm_produto_servico AS nm_combustivel, PSE.qt_precisao_custo, " +
					     // Dados do produto
					     "       BC.cd_combustivel, BC.cd_combustivel AS cd_produto_servico, PS.nm_produto_servico, PS.txt_especificacao, PS.txt_dado_tecnico, PS.id_produto_servico, " +
					     // Somando vendas
					     "      (SELECT SUM(qt_saida) FROM alm_documento_saida_item DSI, alm_documento_saida DS " +
					     "       WHERE DSI.cd_documento_saida = DS.cd_documento_saida " +
					     "         AND DSI.cd_bico            = BI.cd_bico " +
					     "         AND DS.cd_turno            = "+cdTurno+
					     "         AND CAST(DS.dt_documento_saida AS DATE) = ? " +
					     "         AND DS.st_documento_saida  = 1) AS qt_vendida " +
					     "FROM pcb_bico BI " +
					     "LEFT OUTER JOIN pcb_bico_encerrante        BC  ON (BC.cd_bico                = BI.cd_bico " +
					     "                                               AND BC.cd_conta               = "+cdConta+
					     "                                               AND BC.cd_fechamento          = "+cdFechamento+") " +
					     "JOIN alm_local_armazenamento               L   ON (L.cd_empresa              = "+conta.getCdEmpresa()+
					     "                                               AND ((BC.cd_tanque IS NOT NULL AND L.cd_local_armazenamento  = BC.cd_tanque) " +
					     "                                                OR  (BC.cd_tanque IS NULL     AND L.cd_local_armazenamento  = BI.cd_tanque))) "+
					     "JOIN pcb_tanque                            T    ON (T.cd_tanque              = L.cd_local_armazenamento) " +
					     "JOIN pcb_bombas                            BO   ON (BO.cd_bomba              = BI.cd_bomba) " +
					     "JOIN grl_produto_servico                   PS   ON ((BC.cd_combustivel IS NOT NULL AND BC.cd_combustivel    = PS.cd_produto_servico) " +
					     "                                                OR  (BC.cd_combustivel IS NULL     AND T.cd_produto_servico = PS.cd_produto_servico)) "+
					     "JOIN grl_produto_servico_empresa           PSE  ON (PSE.cd_produto_servico   = PS.cd_produto_servico " +
					     "                                                AND PSE.cd_empresa           = "+conta.getCdEmpresa()+") "+
					     "ORDER BY BI.nr_ordem" ;
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rsmEncerrantes = new ResultSetMap(pstmt.executeQuery());
			// Desconto
			/*
			PreparedStatement pstmtDesconto = connect.prepareStatement("SELECT SUM(DSI.vl_desconto) AS vl_total_desconto, SUM(DSI.vl_acrescimo) AS vl_total_acrescimo " +
					                                                   "FROM alm_documento_saida_item     DSI " +
					                                                   "JOIN alm_documento_saida DS ON (DS.cd_documento_saida = DSI.cd_documento_saida) "+
					                                                   "WHERE DS.cd_turno  =   " +cdTurno+
					                                                   "  AND DSI.cd_bico  = ? "+
					                        						   "  AND CAST(DS.dt_documento_saida AS DATE) = ? ");
			// Vendas com preços diferenciados
			PreparedStatement pstmtDiferenciados = connect.prepareStatement("SELECT SUM(qt_saida) AS qt_total, SUM(qt_saida * vl_unitario) AS vl_total " +
														                    "FROM alm_documento_saida_item     DSI " +
														                    "JOIN alm_documento_saida DS ON (DS.cd_documento_saida = DSI.cd_documento_saida) "+
							                                                "WHERE DS.cd_turno     = " +cdTurno+
							                                                "  AND DSI.cd_bico     = ? " +
							                                                "  AND DSI.vl_unitario <> ? "+
							                        						"  AND CAST(DS.dt_documento_saida AS DATE) = ? ");
			*/
			// Preço
			PreparedStatement pstmtPreco = connect.prepareStatement("SELECT A.*, B.id_tabela_preco FROM adm_produto_servico_preco A, adm_tabela_preco B " +
					                                                "WHERE A.cd_tabela_preco  = B.cd_tabela_preco " +
					                                                "  AND (A.dt_termino_validade IS NULL OR A.dt_termino_validade > ?)" +
					                                                "  AND A.cd_produto_servico = ? " +
					                                                "  AND A.cd_tabela_preco    = ? " +
					                                                "ORDER BY A.dt_termino_validade");
			while(rsmEncerrantes.next())	{
				if(rsmEncerrantes.getFloat("vl_preco") <= 0) {
					// Conferindo e atualizando preço
					pstmtPreco.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
					pstmtPreco.setInt(2, rsmEncerrantes.getInt("cd_produto_servico"));
					pstmtPreco.setInt(3, rsmEncerrantes.getInt("cd_tabela_preco")>0 ? rsmEncerrantes.getInt("cd_tabela_preco") : cdTabelaPreco);
					ResultSet rsPreco = pstmtPreco.executeQuery();
					if(rsPreco.next()) {
						BicoEncerrante encerrante = BicoEncerranteDAO.get(cdConta, cdFechamento, rsmEncerrantes.getInt("cd_bico"), connect);
						if(encerrante!=null) {
							encerrante.setVlPreco(rsPreco.getFloat("vl_preco"));
							BicoEncerranteDAO.update(encerrante, connect);
						}
						//
						rsmEncerrantes.setValueToField("VL_PRECO", rsPreco.getFloat("vl_preco"));	
						rsmEncerrantes.setValueToField("VL_PRECO_TABELA", rsPreco.getFloat("vl_preco"));	
						rsmEncerrantes.setValueToField("ID_PRECO_TABELA", rsPreco.getString("id_tabela_preco"));	
					}
				}
				else
					rsmEncerrantes.setValueToField("VL_PRECO_TABELA", rsmEncerrantes.getFloat("vl_preco"));
				if(rsmEncerrantes.getFloat("vl_total") < 0.0001)
					rsmEncerrantes.setValueToField("VL_TOTAL", 0);
				// Pegando o último encerrante
				if(rsmEncerrantes.getFloat("qt_encerrante_inicial") <= 0)	{	
					float qtEncerranteInicial = getEncerranteInicial(rsmEncerrantes.getInt("cd_bico"), cdConta, dtFechamento, cdTurno, connect);
					
					rsmEncerrantes.setValueToField("qt_encerrante_inicial", qtEncerranteInicial);
				}
				// Atualizando Valor Total
				if(rsmEncerrantes.getFloat("vl_total") <= 0 && (rsmEncerrantes.getFloat("qt_encerrante_final")>rsmEncerrantes.getFloat("qt_encerrante_inicial"))) {
					/*
					// Desconto
					float vlTotalDesconto = 0, vlTotalAcrescimo = 0;
					pstmtDesconto.setInt(1, rsmEncerrantes.getInt("cd_bico"));
					pstmtDesconto.setTimestamp(2, new Timestamp(dtFechamento.getTimeInMillis()));
					ResultSet rs = pstmtDesconto.executeQuery();
					rs.next(); // Somas sempre retornam resultados
					vlTotalDesconto  = rs.getFloat("vl_total_desconto");
					vlTotalAcrescimo = rs.getFloat("vl_total_acrescimo");
					// Somando produtos vendidos com preço diferenciado
					float vlTotalDiferenciado = 0, qtTotalDiferenciado = 0;
					pstmtDiferenciados.setInt(1, rsmEncerrantes.getInt("cd_bico"));
					pstmtDiferenciados.setFloat(2, rsmEncerrantes.getFloat("vl_preco"));
					pstmtDiferenciados.setTimestamp(3, new Timestamp(dtFechamento.getTimeInMillis()));
					rs = pstmtDiferenciados.executeQuery();
					rs.next();
					qtTotalDiferenciado = rs.getFloat("qt_total");
					vlTotalDiferenciado = rs.getFloat("vl_total");
					float vlTotal = ((rsmEncerrantes.getFloat("qt_litros")-qtTotalDiferenciado) * rsmEncerrantes.getFloat("vl_preco")) -
							        vlTotalDesconto + vlTotalAcrescimo + vlTotalDiferenciado;
					*/ 
					float vlTotal = rsmEncerrantes.getFloat("qt_litros") * rsmEncerrantes.getFloat("vl_preco");
					rsmEncerrantes.setValueToField("VL_TOTAL", vlTotal);	
					// Atualizando
					BicoEncerrante encerrante = BicoEncerranteDAO.get(cdConta, cdFechamento, rsmEncerrantes.getInt("cd_bico"), connect);
					if(encerrante!=null) {
						encerrante.setVlTotal(vlTotal);
						BicoEncerranteDAO.update(encerrante, connect);
					}
				}
			}
			//
			rsmEncerrantes.beforeFirst();
			return rsmEncerrantes;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result setAfericao(int cdConta, int cdTurno, int cdBico, int cdDocumentoSaida, int cdUsuario)	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			/*
			 *  Busca o valor da aferição e cancela o documento de saída 
			 */
			float vlAfericao = 0;
			ResultSetMap rsm = DocumentoSaidaServices.getAllItens(cdDocumentoSaida, connect);
			if(rsm.next())
				vlAfericao = rsm.getFloat("qt_saida");
			// Cancela o abastecimento pendente
			DocumentoSaida saida = DocumentoSaidaDAO.get(cdDocumentoSaida, connect);
			saida.setCdTurno(cdTurno);
			saida.setStDocumentoSaida(DocumentoSaidaServices.TP_AFERICAO); 
			saida.setTxtObservacao("Marcado como aferição do Bico no turno.");
			DocumentoSaidaDAO.update(saida, connect);
			
			Result result = DocumentoSaidaServices.cancelarSaida(cdDocumentoSaida, cdUsuario, false /*deleteItens*/, connect);
			if(result.getCode() <= 0) {
				result.setMessage(result.getMessage() + " Ao tentar cancelar o abastecimento pendente.");
				Conexao.rollback(connect);
				return result;
			}
			GregorianCalendar dtFechamento = saida.getDtDocumentoSaida();
			dtFechamento.set(Calendar.HOUR, 0);
			dtFechamento.set(Calendar.MINUTE, 0);
			dtFechamento.set(Calendar.SECOND, 0);
			dtFechamento.set(Calendar.MILLISECOND, 0);
			/*
			 *  Pesquisa FECHAMENTO
			 */
			int cdFechamento = 0;
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_fechamento FROM adm_conta_fechamento " +
											                   "WHERE cd_conta      = "+cdConta+
															   "  AND cd_turno      = "+cdTurno+
											                   "  AND dt_fechamento = ? ");
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				cdFechamento = rs.getInt("cd_fechamento");
			else { // Se não existir procura fechamento
				// Procura um pré-fechamento
				rs = connect.prepareStatement("SELECT cd_fechamento FROM adm_conta_fechamento " +
                                              "WHERE cd_conta      = "+cdConta+
                                              "  AND cd_turno      = "+cdTurno +
                        					  "  AND dt_fechamento IS NULL").executeQuery();
				if(rs.next())
					cdFechamento = rs.getInt("cd_fechamento");
				// A data de fechamento nula, é o que caracteriza o pré-fechamento
				else	{
					ContaFechamento fechamento = new ContaFechamento(cdConta, cdFechamento, null /*dtFechamento*/, 0/*cdSupervisor*/, 0/*cdResponsavel*/,
			                                                         0.0/*vlFechamento*/, 0.0/*vlTotalEntradas*/, 0.0/*vlTotalSaidas*/, ""/*txtObservacao*/,
			                                                         0.0/*vlSaldoAnterior*/, cdTurno, ContaFechamentoServices.ST_EM_ANDAMENTO);
					cdFechamento = ContaFechamentoDAO.insert(fechamento, connect);
					if(cdFechamento <= 0) {
						result = new Result(cdFechamento, "Falha ao tentar iniciar fechamento para atribuir aferição!");
						Conexao.rollback(connect);
						return result;
					}
					//
					ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
					result = iniciarFechamento(cdConta, cdFechamento, conta.getCdEmpresa(), connect);
					if(result.getCode() <= 0)	{
						result.setMessage(result.getMessage() + " Falha ao tentar iniciar fechamento para atribuir aferição!");
						Conexao.rollback(connect);
						return result;
					}
				}
			}
			//
			BicoEncerrante encerrante = BicoEncerranteDAO.get(cdConta, cdFechamento, cdBico, connect);
			if(encerrante.getVlAfericao() > 0) {
				Conexao.rollback(connect);
				return new Result(-1, "A afericao desse bico ja foi informada!");
			}
			encerrante.setVlAfericao(vlAfericao);
			result = new Result(BicoEncerranteDAO.update(encerrante, connect));
			if(result.getCode() <= 0) {
				Conexao.rollback(connect);
				result.setMessage("Falha ao atribuir aferição ao bico!");
			}
			else
				connect.commit();
				
			return result;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Falha ao tentar gravar aferição!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result setEncerrante(int cdConta, int cdFechamento, int cdBico, float qtEncerranteFinal, float vlAfericao)	{
		return setEncerrante(cdConta, cdFechamento, cdBico, -1/*qtEncerranteInicial*/, qtEncerranteFinal, vlAfericao, null);
	}
	
	public static Result setEncerrante(int cdConta, int cdFechamento, int cdBico, float qtEncerranteInicial, float qtEncerranteFinal, float vlAfericao)	{
		return setEncerrante(cdConta, cdFechamento, cdBico, qtEncerranteInicial, qtEncerranteFinal, vlAfericao, null);
	}
	
	public static Result setEncerrante(int cdConta, int cdFechamento, int cdBico, float qtEncerranteInicial, float qtEncerranteFinal, float vlAfericao, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Pesquisa e cria pré-fechamento
			if(cdFechamento <= 0)	{
				ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
				//
				ResultSet rs = connect.prepareStatement("SELECT cd_fechamento FROM adm_conta_fechamento " +
						                                "WHERE cd_conta      = "+cdConta+
														"  AND cd_turno      = "+conta.getCdTurno()+
						                                "  AND dt_fechamento IS NULL").executeQuery();
				if(rs.next())
					cdFechamento = rs.getInt("cd_fechamento");
				// A data de fechamento nula, é o que caracteriza o pré-fechamento
				else	{
					ContaFechamento fechamento = new ContaFechamento(cdConta,cdFechamento, null /*dtFechamento*/, 0/*cdSupervisor*/, 0/*cdResponsavel*/,
			                                                         0.0/*vlFechamento*/, 0.0/*vlTotalEntradas*/, 0.0/*vlTotalSaidas*/, ""/*txtObservacao*/,
			                                                         0.0/*vlSaldoAnterior*/, conta.getCdTurno(), ContaFechamentoServices.ST_EM_ANDAMENTO);
					cdFechamento = ContaFechamentoDAO.insert(fechamento, connect);
					if(cdFechamento > 0)
						iniciarFechamento(cdConta, cdFechamento, conta.getCdEmpresa(), connect);
				}
			}
			//
			BicoEncerrante encerrante = BicoEncerranteDAO.get(cdConta, cdFechamento, cdBico, connect);
			// Atualizando valores
			if (qtEncerranteInicial > 0)
				encerrante.setQtEncerranteInicial(qtEncerranteInicial);
			// Verificando relação entre encerrante inicial e final
			if((encerrante.getQtEncerranteInicial()+vlAfericao) > qtEncerranteFinal)
				return new Result(-1, "Valor Invalido! O Encerrante informado esta menor do que o encerrante inicial + a afericao!");
			//
			encerrante.setQtEncerranteFinal(qtEncerranteFinal);
			encerrante.setVlAfericao(vlAfericao);
			encerrante.setQtLitros(qtEncerranteFinal - encerrante.getQtEncerranteInicial() - vlAfericao);
			encerrante.setVlTotal(encerrante.getQtLitros() * encerrante.getVlPreco());
			return new Result(BicoEncerranteDAO.update(encerrante, connect));
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Falha ao tentar gravar dados do encerrante e da aferição!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result lancarEncerrantes(ArrayList<BicoEncerrante> al)	{
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			//
			for(int i=0;i<=al.size()-1;i++)	{
				BicoEncerrante objeto     = (BicoEncerrante) al.get(i);
				//
				BicoEncerrante encerrante = BicoEncerranteDAO.get(objeto.getCdConta(), objeto.getCdFechamento(), objeto.getCdBico(), connect);
				int ret = 0; 
				if(encerrante==null)
					ret = BicoEncerranteDAO.insert(objeto, connect);
				else
					ret = BicoEncerranteDAO.update(objeto, connect);
				//
				if(ret<=0) {
					Conexao.rollback(connect);
					return new Result(ret, "Falha ao tentar inserir/atualizar os dados do bico!");
				}
			}
			// Gravando informações
			connect.commit();
			//
			return new Result(1, "Encerrante(s) inserido(s) com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new Result(-1, "Falha ao tentar lançar medição física!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLeituraBicos(int cdFechamento){
		return getLeituraBicos(cdFechamento,null);
	}
	
	public static ResultSetMap getLeituraBicos(int cdFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT BE.*, PS.nm_produto_servico, PS.cd_produto_servico, B.id_bico, B.nr_casas_decimais, B.nr_casas_inteiras " +
						 "FROM pcb_bico_encerrante BE " +
						 "JOIN pcb_bico             B ON (BE.cd_bico           = B.cd_bico) " +
						 "JOIN pcb_tanque           T ON ((BE.cd_tanque IS NOT NULL AND T.cd_tanque = BE.cd_tanque) " +
					     "                             OR (BE.cd_tanque IS NULL     AND T.cd_tanque = B.cd_tanque)) "+
						 "JOIN grl_produto_servico PS ON (BE.cd_combustivel    = PS.cd_produto_servico) " +
						 "WHERE BE.cd_fechamento = "+ cdFechamento+
						 " ORDER BY B.nr_ordem ";
			//Util.formatNumber(number, mask)
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				String mask           = "";
				for(int i = 0;i<rsm.getInt("nr_casas_inteiras");i++){
					mask += "#";
				}
				for(int i = 0;i<rsm.getInt("nr_casas_decimais");i++){
					if(i==0)
						mask += ".";
			    	mask += "0";
				}
				rsm.setValueToField("QT_ENCERRANTE_FINAL_M",Util.formatNumber(rsm.getFloat("QT_ENCERRANTE_FINAL"), mask));
				rsm.setValueToField("QT_ENCERRANTE_INICIAL_M",Util.formatNumber(rsm.getFloat("QT_ENCERRANTE_INICIAL"), mask));
				rsm.setValueToField("QT_LITROS_M",Util.formatNumber(rsm.getFloat("QT_LITROS"), mask));
			}
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRelatorioConsolidacaoLeituraBicos(ResultSetMap rsmFechamentos){
		return getRelatorioConsolidacaoLeituraBicos(rsmFechamentos, null);
	}
	
	public static ResultSetMap getRelatorioConsolidacaoLeituraBicos(ResultSetMap rsmFechamentos, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmEncerrantes = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while(rsmFechamentos.next()){
				ResultSetMap rsmTmpEncerrantes = BicoEncerranteServices.getLeituraBicos(rsmFechamentos.getInt("CD_FECHAMENTO"));
				rsmTmpEncerrantes.beforeFirst();
				while( rsmTmpEncerrantes.next() ){
					String mask           = "";
					for(int i = 0;i<rsmTmpEncerrantes.getInt("nr_casas_inteiras");i++){
						mask += "#";
					}
					for(int i = 0;i<rsmTmpEncerrantes.getInt("nr_casas_decimais");i++){
						if(i==0)
							mask += ".";
				    	mask += "0";
					}
					boolean hasRegister = false;
					while( rsmEncerrantes.next() ){
						if( rsmEncerrantes.getInt("CD_PRODUTO_SERVICO") == rsmTmpEncerrantes.getInt("CD_PRODUTO_SERVICO") 
							&& rsmEncerrantes.getDouble("CD_BICO") == rsmTmpEncerrantes.getDouble("CD_BICO")  	){
							//CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO NO RSM
							Double vlAfericao = rsmEncerrantes.getDouble("VL_AFERICAO")+rsmTmpEncerrantes.getDouble("VL_AFERICAO");
							
							Double qtFechamento = Double.parseDouble( rsmTmpEncerrantes.getString("QT_ENCERRANTE_FINAL_M").replace(",", ".") );
							Double qtTotal = Double.parseDouble( rsmEncerrantes.getString("QT_LITROS_M").replace(",", ".") )+Double.parseDouble( rsmTmpEncerrantes.getString("QT_LITROS_M").replace(",", ".") );
							
							rsmEncerrantes.setValueToField("VL_AFERICAO", vlAfericao);
							rsmEncerrantes.setValueToField("QT_ENCERRANTE_FINAL_M", Util.formatNumber( qtFechamento, mask) );
							rsmEncerrantes.setValueToField("QT_LITROS_M", Util.formatNumber(  qtTotal, mask) );
							hasRegister = true;
						}
					}
					if(!hasRegister){
						rsmEncerrantes.addRegister( rsmTmpEncerrantes.getRegister() );
					}
					rsmEncerrantes.beforeFirst();
				}
			}
			
			return rsmEncerrantes;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	private static float getEncerranteInicial(int cdBico, int cdConta, GregorianCalendar dtFechamento, int cdTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Dados do fechamento anterior
			if(dtFechamento == null)
				dtFechamento = new GregorianCalendar();
			int cdFechamentoAnterior = ContaFechamentoServices.getCdFechamentoAnterior(cdConta, dtFechamento, cdTurno, connect);
			// Pega o encerrante final do turno anterior
			ResultSet rs = connect.prepareStatement("SELECT qt_encerrante_final FROM pcb_bico_encerrante " +
								                    "WHERE cd_fechamento = "+cdFechamentoAnterior+
								                    "  AND cd_bico       = "+cdBico).executeQuery();
			if(rs.next() && rs.getFloat("qt_encerrante_final")>0)
				return rs.getFloat("qt_encerrante_final");
			// Se não encontrou o fechamento do turno anterior, então passa o encerrante inicial do bico
			Bico bico = BicoDAO.get(cdBico, connect);
			return bico.getQtEncerranteInicial();
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -1;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getVendasByEncerrantes(int cdEmpresa, int cdConta, int cdTurno, GregorianCalendar dtFechamento)	{
		return getVendasByEncerrantes(cdEmpresa, cdConta, cdTurno, dtFechamento, null);
	}
	
	
	public static ResultSetMap getVendasByEncerrantes(int cdEmpresa, int cdConta, int cdTurno, GregorianCalendar dtFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {

			if (isConnectionNull)
				connect = Conexao.conectar();
			
			dtFechamento.set(Calendar.HOUR, 0);
			dtFechamento.set(Calendar.MINUTE, 0);
			dtFechamento.set(Calendar.SECOND, 0);
			dtFechamento.set(Calendar.MILLISECOND, 0);
			//
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento A, pcb_bico_encerrante B " +
					                                    	   "WHERE A.cd_fechamento = B.cd_fechamento " +
					                                    	   "  AND A.cd_conta      = "+cdConta+
					                                    	   "  AND A.cd_turno      = "+cdTurno+
					                                    	   "  AND (A.dt_fechamento = ? OR (A.dt_fechamento IS NULL AND qt_litros > 0)) ");
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			if(!pstmt.executeQuery().next()) {
				GregorianCalendar dtFinal = (GregorianCalendar)dtFechamento.clone();
				dtFinal.set(Calendar.HOUR, 0);
				dtFinal.set(Calendar.MINUTE, 0);
				dtFinal.set(Calendar.SECOND, 0);
				dtFinal.set(Calendar.MILLISECOND, 0);

				int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
				ResultSetMap rsm = com.tivic.manager.alm.DocumentoSaidaItemServices.findSaidaAndItensByProduto(cdTurno, cdEmpresa, dtFechamento, dtFinal, cdGrupoCombustivel, true, connect);
				while(rsm.next()) {
					rsm.setValueToField("VL_PRECO", rsm.getFloat("VL_UNITARIO"));
					rsm.setValueToField("QT_LITROS", rsm.getFloat("QT_SAIDA"));
					rsm.setValueToField("VL_AFERICAO", 0);
				}
				rsm.beforeFirst();
				return rsm;
			}
			// Calculando dia seguinte.
			GregorianCalendar dtDiaSeguinte = (GregorianCalendar)dtFechamento.clone();
			dtDiaSeguinte.add(Calendar.DATE, 1);
			
			PreparedStatement pstmtMedicaoFisica = connect.prepareStatement("SELECT SUM(MF.qt_volume) AS qt_volume FROM pcb_medicao_fisica MF " +
															 				"JOIN pcb_tanque           T  ON (MF.cd_tanque     = T.cd_tanque) " +
															 				"JOIN adm_conta_fechamento CF ON (MF.cd_fechamento = CF.cd_fechamento) " +
															 				"WHERE T.cd_produto_servico           = ? " +
															 				"  AND CAST(CF.dt_fechamento AS DATE) = ? " +
															 				"  AND MF.qt_volume                   > 0 " +
															 				"  AND CF.cd_turno                    = "+TurnoServices.getPrimeiroTurno());
			
			PreparedStatement pstmtVendasDia = connect.prepareStatement("SELECT SUM(qt_litros) AS qt_litros, SUM(vl_afericao) AS vl_afericao " +
																 	    "FROM pcb_bico_encerrante BE " +
																 	    "JOIN pcb_bico             B  ON (BE.cd_bico           = B.cd_bico) " +
																 	    "JOIN adm_conta_fechamento CF ON (BE.cd_fechamento     = CF.cd_fechamento) " +
																 	    "JOIN grl_produto_servico  PS ON (BE.cd_combustivel    = PS.cd_produto_servico) " +
																 	    "WHERE CF.cd_conta                    = "+cdConta +
																 	    "  AND BE.cd_combustivel              = ?" +
																 	    "  AND CAST(CF.dt_fechamento AS DATE) = ?");
			
			PreparedStatement pstmtEntradaDia = connect.prepareStatement("SELECT SUM(A.qt_entrada) AS QT_ENTRADA, " +
															   			 "       SUM(A.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (A.qt_entrada/C.qt_entrada))) AS vl_entrada " +
															   			 "FROM alm_entrada_local_item A, alm_documento_entrada B, alm_documento_entrada_item C " +
															   			 "WHERE A.cd_empresa           = "+cdEmpresa+
															   			 "  AND A.cd_documento_entrada = C.cd_documento_entrada " +
															   			 "  AND A.cd_produto_servico   = C.cd_produto_servico " +
															   			 "  AND A.cd_empresa           = C.cd_empresa " +
															   			 "  AND A.cd_documento_entrada = B.cd_documento_entrada " +
															   			 "  AND B.tp_entrada      NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
															   			 "  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
															   			 "  AND C.qt_entrada           > 0 " +
															   			 "  AND CAST(B.dt_documento_entrada AS DATE) = ? " +
															   			 "  AND C.cd_produto_servico = ?");
			
			PreparedStatement pstmtVendasMes = connect.prepareStatement("SELECT SUM(qt_litros) AS qt_litros, SUM(vl_afericao) AS vl_afericao " +
													 	  		        "FROM pcb_bico_encerrante  BE " +
													 	  		        "JOIN pcb_bico             B  ON (BE.cd_bico        = B.cd_bico) " +
													 	  		        "JOIN adm_conta_fechamento CF ON (BE.cd_fechamento  = CF.cd_fechamento) " +
													 	  		        "JOIN grl_produto_servico  PS ON (BE.cd_combustivel = PS.cd_produto_servico) " +
													 	  		        "WHERE CAST(CF.dt_fechamento AS DATE) <= ? " +
													 	  		        " AND  CF.cd_conta                     = "+cdConta +
													 	  		        " AND  BE.cd_combustivel               = ? ");
			// Busca	
			String sql = "SELECT PS.cd_produto_servico, PS.nm_produto_servico, BE.vl_preco, PSE.qt_precisao_custo, " +
					     "       SUM(qt_litros) AS qt_litros, " +
					     "       SUM(vl_afericao) AS vl_afericao  " +
					 	 "FROM pcb_bico_encerrante BE " +
					 	 "JOIN pcb_bico                    B   ON (BE.cd_bico            = B.cd_bico) " +
					 	 "JOIN adm_conta_fechamento        CF  ON (BE.cd_fechamento      = CF.cd_fechamento) " +
					 	 "JOIN pcb_tanque                  T   ON (BE.cd_tanque          = T.cd_tanque) " +
					 	 "JOIN grl_produto_servico         PS  ON (BE.cd_combustivel     = PS.cd_produto_servico) " +
					 	 "JOIN grl_produto_servico_empresa PSE ON (PS.cd_produto_servico = PSE.cd_produto_servico " +
					 	 "                                     AND PSE.cd_empresa="+cdEmpresa+") " +
					 	 "WHERE CF.cd_conta                    = "+cdConta +
					 	 "  AND CF.cd_turno                    = "+cdTurno +
					 	 "  AND CAST(CF.dt_fechamento AS DATE) = ? " +
					 	 "GROUP BY PS.cd_produto_servico,PS.nm_produto_servico,BE.vl_preco,PSE.qt_precisao_custo  " +
					 	 "HAVING SUM(qt_litros) > 0" +
					 	 "ORDER BY nm_produto_servico";
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			HashMap<Integer,Integer> produto = new HashMap<Integer,Integer>();  
			while(rsm.next())	{
				// ALGUMAS INFORMAÇÕES NÃO PRECISAM APARECER EM DUPLICIDADE
				if(produto.get(rsm.getInt("CD_PRODUTO_SERVICO"))==null) {
					/*
					 * Medição Física do Dia
					 */
					pstmtMedicaoFisica.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
					pstmtMedicaoFisica.setTimestamp(2, new Timestamp(dtFechamento.getTimeInMillis()));
					ResultSet rsMedicaoDia = pstmtMedicaoFisica.executeQuery();
					if(rsMedicaoDia.next())
						rsm.setValueToField("QT_ESTOQUE_INICIAL", rsMedicaoDia.getFloat("QT_VOLUME"));
					/*
					 * Entradas do Dia
					 */
					pstmtEntradaDia.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
					pstmtEntradaDia.setInt(2, rsm.getInt("CD_PRODUTO_SERVICO"));
					ResultSet rsEntradaDia = pstmtEntradaDia.executeQuery();
					if(rsEntradaDia.next())
						rsm.setValueToField("QT_COMPRA", rsEntradaDia.getFloat("QT_ENTRADA"));
					/*
					 * Vendas do Dia
					 */
					pstmtVendasDia.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
					pstmtVendasDia.setTimestamp(2, new Timestamp(dtFechamento.getTimeInMillis()));
					ResultSet rsVendaDia = pstmtVendasDia.executeQuery();
					if(rsVendaDia.next())
						rsm.setValueToField("QT_VENDAS_DIA",rsVendaDia.getFloat("QT_LITROS"));
					/*
					 * VENDAS DO MÊS
					 */
					pstmtVendasMes.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
					pstmtVendasMes.setInt(2, rsm.getInt("CD_PRODUTO_SERVICO"));
					ResultSet rsVendaMes = pstmtVendasMes.executeQuery();
					if(rsVendaMes.next())
						rsm.setValueToField("QT_SAIDA_MES", rsVendaMes.getFloat("QT_LITROS"));
					/*
					 * Medição do Dia Seguinte
					 */
					pstmtMedicaoFisica.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
					pstmtMedicaoFisica.setTimestamp(2, new Timestamp(dtDiaSeguinte.getTimeInMillis()));
					rsMedicaoDia = pstmtMedicaoFisica.executeQuery();
					if(rsMedicaoDia.next())
						rsm.setValueToField("QT_MEDICAO_FINAL", rsMedicaoDia.getFloat("QT_VOLUME"));
				}
				/*
				 * Calculando estoque FINAL
				 */
				rsm.setValueToField("QT_ESTOQUE_FINAL", rsm.getFloat("QT_ESTOQUE_INICIAL") + rsm.getFloat("QT_ENTRADA") - rsm.getFloat("QT_VENDAS_DIA"));
				if(rsm.getFloat("QT_ESTOQUE_FINAL")==0)
					rsm.setValueToField("QT_ESTOQUE_FINAL", null);
				produto.put(rsm.getInt("CD_PRODUTO_SERVICO"), rsm.getInt("CD_PRODUTO_SERVICO"));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getRelatorioConsolidacaoVendasBicos(ResultSetMap rsmFechamentos, int cdEmpresa){
		return getRelatorioConsolidacaoVendasBicos(rsmFechamentos, cdEmpresa, null);
	}
	public static ResultSetMap getRelatorioConsolidacaoVendasBicos(ResultSetMap rsmFechamentos, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmVendasBicos = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			while(rsmFechamentos.next()){
				System.out.println( "movimento :"+rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO") );
				ResultSetMap rsmTmpVendasBicos = getVendasByEncerrantes( cdEmpresa, rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_TURNO"), rsmFechamentos.getGregorianCalendar("DT_FECHAMENTO"));
				rsmTmpVendasBicos.beforeFirst();
				while( rsmTmpVendasBicos.next() ){
					boolean hasRegister = false;
					while( rsmVendasBicos.next() ){
						if( rsmVendasBicos.getInt("CD_PRODUTO_SERVICO") == rsmTmpVendasBicos.getInt("CD_PRODUTO_SERVICO")){
							//CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO NO RSM
							Double qtLitros = rsmVendasBicos.getDouble("QT_LITROS")+rsmTmpVendasBicos.getDouble("QT_LITROS");
							Float qtEstoqueInicial = rsmVendasBicos.getFloat("QT_ESTOQUE_INICIAL")+rsmTmpVendasBicos.getFloat("QT_ESTOQUE_INICIAL");
							Float qtVendasDia = rsmVendasBicos.getFloat("QT_VENDAS_DIA")+rsmTmpVendasBicos.getFloat("QT_VENDAS_DIA");
							Float qtCompra = rsmVendasBicos.getFloat("QT_COMPRA")+rsmTmpVendasBicos.getFloat("QT_COMPRA");
							Float qtMedicaoFinal = rsmVendasBicos.getFloat("QT_MEDICAO_FINAL")+rsmTmpVendasBicos.getFloat("QT_MEDICAO_FINAL");
							Float qtSaidaMes = rsmVendasBicos.getFloat("QT_SAIDA_MES")+rsmTmpVendasBicos.getFloat("QT_SAIDA_MES");
							
							rsmVendasBicos.setValueToField("QT_LITROS", qtLitros);
							rsmVendasBicos.setValueToField("QT_ESTOQUE_INICIAL", qtEstoqueInicial);
							rsmVendasBicos.setValueToField("QT_VENDAS_DIA", qtVendasDia);
							rsmVendasBicos.setValueToField("QT_COMPRA", qtCompra);
							rsmVendasBicos.setValueToField("QT_MEDICAO_FINAL", qtMedicaoFinal);
							rsmVendasBicos.setValueToField("QT_SAIDA_MES", qtSaidaMes);
							hasRegister = true;
						}
					}
					if(!hasRegister){
						rsmVendasBicos.addRegister( rsmTmpVendasBicos.getRegister() );
					}
					rsmVendasBicos.beforeFirst();
				}
			}
			return rsmVendasBicos;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
