package com.tivic.manager.adm;

import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;

import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

public class PlanoPagtoDocumentoSaidaServices {

	public static int insert(PlanoPagtoDocumentoSaida objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoPagtoDocumentoSaida objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM adm_plano_pagto_documento_saida " +
					"WHERE cd_documento_saida = "+objeto.getCdDocumentoSaida()+
					"  AND cd_forma_pagamento = "+objeto.getCdFormaPagamento()+
					"  AND cd_plano_pagamento = "+objeto.getCdPlanoPagamento()).executeQuery();
			if(rs.next())	{
				objeto.setVlPagamento(objeto.getVlPagamento() + rs.getFloat("vl_pagamento"));
				return PlanoPagtoDocumentoSaidaDAO.update(objeto, connect);
			}
			return PlanoPagtoDocumentoSaidaDAO.insert(objeto, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getSaidasByPagamento(int cdConta, int cdFormaPagamento, GregorianCalendar dtMovimento, int tpFormaPagamento,int cdTurno){
		return getSaidasByPagamento(cdConta, cdFormaPagamento, dtMovimento, tpFormaPagamento, cdTurno, null);
	}
	
	public static ResultSetMap getSaidasByPagamento(int cdConta, int cdFormaPagamento, GregorianCalendar dtMovimento, int tpFormaPagamento, int cdTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			dtMovimento.set(Calendar.MILLISECOND, 0);
			// Pagamentos / Despesas
			if(cdFormaPagamento == -10) {
				String sql = "SELECT A.vl_pago AS vl_pagamento, A.*, B.ds_historico, C.dt_vencimento, C.vl_conta, D.nm_pessoa AS nm_cliente, " +
						     "       D.nm_pessoa, F.nm_pessoa AS nm_usuario " +
						     "FROM adm_movimento_conta_pagar A "+
							 "JOIN adm_movimento_conta       B ON (A.cd_conta           = B.cd_conta "+
							 "                                 AND A.cd_movimento_conta = B.cd_movimento_conta) "+
							 "JOIN adm_conta_pagar           C ON (A.cd_conta_pagar     = C.cd_conta_pagar) "+
							 "LEFT OUTER JOIN grl_pessoa     D ON (C.cd_pessoa          = D.cd_pessoa) " +
							 "LEFT OUTER JOIN seg_usuario    E ON (B.cd_usuario         = E.cd_usuario) " +
							 "LEFT OUTER JOIN grl_pessoa     F ON (F.cd_pessoa          = E.cd_pessoa) "+
							 "WHERE B.cd_conta = "+cdConta+
							 "  AND B.cd_turno = "+cdTurno+
							 "  AND CAST(B.dt_movimento AS DATE) = ? ";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				return new ResultSetMap(pstmt.executeQuery());
			}
			// Descontos
			if(cdFormaPagamento == -20) {
				String sql = "SELECT B.vl_desconto, D.nm_pessoa, D.nm_pessoa AS nm_cliente," +
						     "       ((B.vl_desconto / B.vl_total_documento) * 100) AS pr_desconto," +
						     "       B.nr_documento_saida, F.nm_pessoa AS nm_usuario, B.vl_desconto AS vl_pagamento, B.cd_documento_saida," +
						     "       (B.vl_total_documento) AS vl_liquido," +
						     "       (B.vl_total_documento + B.vl_desconto - B.vl_acrescimo) AS vl_total_documento "+
							 "FROM alm_documento_saida B "+
							 "LEFT OUTER JOIN grl_pessoa  D ON (B.cd_cliente   = D.cd_pessoa) "+
							 "LEFT OUTER JOIN seg_usuario E ON (B.cd_digitador = E.cd_usuario) " +
							 "LEFT OUTER JOIN grl_pessoa  F ON (F.cd_pessoa    = E.cd_pessoa) "+
							 "WHERE B.cd_turno                         = " +cdTurno+
							 "  AND B.cd_conta                         = " +cdConta+
							 "  AND CAST(B.dt_documento_saida AS DATE) = ? " +
							 "  AND B.vl_desconto                      > 0 " +
							 "  AND B.st_documento_saida               = "+DocumentoSaidaServices.ST_CONCLUIDO+
							 "  and B.vl_total_documento               > 0";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				return new ResultSetMap(pstmt.executeQuery());
			}
			// Descontos Especiais
			if(cdFormaPagamento == -30) {
				ContaFinanceira conta = ContaFinanceiraDAO.get(cdConta, connect);
				int cdTabelaPreco  = 0;
				int cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, conta!=null ? conta.getCdEmpresa() : 0);
				TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connect);
				if(tipoOperacao!=null)
					cdTabelaPreco = tipoOperacao.getCdTabelaPreco();
				String sql = " SELECT B.cd_documento_saida, B.dt_documento_saida, B.vl_total_documento, D.nm_pessoa, B.nr_documento_saida, " +
						     "        F.nm_pessoa AS nm_usuario, A.vl_unitario, A.qt_saida, " +
						     "       (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
							 "        WHERE PR.cd_produto_servico = A.cd_produto_servico "+
							 "          AND PR.cd_tabela_preco      = "+cdTabelaPreco+
							 "          AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                   AND PR.cd_produto_servico = A.cd_produto_servico "+
							 "                   AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL " +
							  "                  AND PR.dt_termino_validade = (SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							  "                                                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							  "                                                   AND PR.cd_produto_servico = A.cd_produto_servico "+
							  "                                                   AND PR.cd_tabela_preco      = "+cdTabelaPreco+")) OR "+
							 "               ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                 WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                   AND PR.cd_produto_servico  = A.cd_produto_servico "+
							 "                   AND PR.cd_tabela_preco     = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) AS vl_preco_tabela "+
							 "FROM alm_documento_saida_item A, alm_documento_saida B "+
						     "LEFT OUTER JOIN grl_pessoa  D ON (B.cd_cliente   = D.cd_pessoa) "+
							 "LEFT OUTER JOIN seg_usuario E ON (B.cd_digitador = E.cd_usuario) " +
							 "LEFT OUTER JOIN grl_pessoa  F ON (F.cd_pessoa    = E.cd_pessoa) "+
							 "WHERE CAST(B.dt_documento_saida AS DATE) = ? "+
							 "  AND A.cd_documento_saida               = B.cd_documento_saida "+
							 "  AND B.cd_turno                         = "+cdTurno+
							 "  AND A.vl_unitario < (SELECT vl_preco FROM adm_produto_servico_preco PR "+ 
							 "                       WHERE PR.cd_produto_servico = A.cd_produto_servico "+
							 "                         AND PR.cd_tabela_preco      = "+cdTabelaPreco+
							 "                         AND (((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
							 "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NOT NULL " +
							  "                        AND PR.dt_termino_validade = (SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							  "                                                      WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							  "                                                        AND PR.cd_produto_servico = A.cd_produto_servico "+
							  "                                                        AND PR.cd_tabela_preco      = "+cdTabelaPreco+")) OR "+
							 "                              ((SELECT MIN(dt_termino_validade) FROM adm_produto_servico_preco PR "+
							 "                                WHERE PR.dt_termino_validade > B.dt_documento_saida "+
							 "                                  AND PR.cd_produto_servico = A.cd_produto_servico "+
							 "                                  AND PR.cd_tabela_preco      = "+cdTabelaPreco+") IS NULL AND PR.dt_termino_validade IS NULL))) ";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				ResultSet rs = pstmt.executeQuery();
				ResultSetMap rsm = new ResultSetMap();
				float vlDesconto           = 0;
				int   cdDocumentoSaida     = 0;
				HashMap<String,Object> reg = new HashMap<String,Object>();
				while(rs.next()) {
					//
					if(cdDocumentoSaida!=rs.getInt("cd_documento_saida")) {
						if(cdDocumentoSaida > 0) {
							reg.put("VL_PAGAMENTO", vlDesconto);
							reg.put("VL_DESCONTO", vlDesconto);
							reg.put("PR_DESCONTO", vlDesconto / ((Float)reg.get("VL_TOTAL_DOCUMENTO") + vlDesconto));
							rsm.addRegister(reg);
							reg = new HashMap<String,Object>();
						}
						// Reset
						cdDocumentoSaida = rs.getInt("cd_documento_saida");
						vlDesconto       = 0;
						reg.put("NR_DOCUMENTO_SAIDA", rs.getObject("nr_documento_saida"));
						reg.put("CD_DOCUMENTO_SAIDA", rs.getObject("cd_documento_saida"));
						reg.put("DT_DOCUMENTO_SAIDA", rs.getObject("dt_documento_saida"));
						reg.put("VL_TOTAL_DOCUMENTO", rs.getFloat("vl_total_documento"));
						reg.put("VL_LIQUIDO", rs.getFloat("vl_total_documento"));
						reg.put("NM_PESSOA", rs.getObject("nm_pessoa"));
						reg.put("NM_USUARIO", rs.getObject("nm_usuario"));
					}
					float vlTotalItem    = rs.getFloat("vl_unitario") * rs.getFloat("qt_saida");
					float vlTotalItemTab = (rs.getFloat("vl_preco_tabela") * rs.getFloat("qt_saida"));
					float vlDescontoItem = vlTotalItemTab - vlTotalItem;
					//
					vlDesconto += vlDescontoItem;
				}
				if(cdDocumentoSaida > 0) {
					reg.put("VL_DESCONTO", vlDesconto);
					reg.put("VL_PAGAMENTO", vlDesconto);
					reg.put("PR_DESCONTO", vlDesconto / ((Float)reg.get("VL_TOTAL_DOCUMENTO") + vlDesconto));
					rsm.addRegister(reg);
					reg = new HashMap<String,Object>();
				}
				rsm.beforeFirst();
				return rsm;
			}
			// 
			String sql = "SELECT FP.nm_forma_pagamento, PPDS.vl_pagamento, PPDS.vl_acrescimo, CR.vl_conta as vl_movimento, " +
					     "       MCR.vl_recebido, CR.ds_historico, CR.dt_recebimento as dt_movimento, TC.nm_emissor, TC.dt_vencimento, " +
					     "       TC.nr_documento, TC.ds_observacao, PU.nm_pessoa as nm_usuario, PPDS.cd_documento_saida, CF.nm_conta, " +
					     "       MC.ds_historico as ds_movimento, MC.dt_movimento, DS.nr_documento_saida, " +
					     "       (SELECT SUM(DSI.vl_desconto)  " +
					     "        FROM alm_documento_saida_item     DSI " +
						 "        WHERE DSI.cd_documento_saida = DS.cd_documento_saida " +
						 "          AND DS.cd_turno = " +cdTurno+
						 "          AND DS.cd_conta = " +cdConta+") AS vl_desconto " +
						 "FROM adm_plano_pagto_documento_saida        PPDS " +
						 "JOIN alm_documento_saida                    DS  ON (PPDS.cd_documento_saida = DS.cd_documento_saida) " +
						 "JOIN adm_forma_pagamento                    FP  ON (PPDS.cd_forma_pagamento = FP.cd_forma_pagamento) " +
						 "LEFT OUTER JOIN adm_conta_receber           CR  ON (CR.cd_documento_saida   = DS.cd_documento_saida" +
						 "                                                AND CR.cd_forma_pagamento   = "+cdFormaPagamento+") " +
						 "LEFT OUTER JOIN adm_movimento_conta_receber MCR ON (CR.cd_conta_receber     = MCR.cd_conta_receber) " +
						 "LEFT OUTER JOIN adm_movimento_conta         MC  ON (MCR.cd_movimento_conta  = MC.cd_movimento_conta " +
						 "                                                AND MCR.cd_conta            = MC.cd_conta) " +
						 "LEFT OUTER JOIN adm_titulo_credito          TC  ON (CR.cd_conta_receber     = TC.cd_conta_receber) " +
						 "LEFT OUTER JOIN seg_usuario                 U   ON (PPDS.cd_usuario         = U.cd_usuario) " +
						 "LEFT OUTER JOIN grl_pessoa                  PU  ON (U.cd_pessoa             = PU.cd_pessoa) " +
						 "LEFT OUTER JOIN adm_conta_financeira        CF  ON (CR.cd_conta             = CF.cd_conta)" +
						 "WHERE PPDS.cd_forma_pagamento = " +cdFormaPagamento+
						 "  AND DS.cd_conta             = " +cdConta+
						 "  AND DS.cd_turno             = " +cdTurno+
						 "  AND CAST(DS.dt_documento_saida AS DATE) = ? ";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
