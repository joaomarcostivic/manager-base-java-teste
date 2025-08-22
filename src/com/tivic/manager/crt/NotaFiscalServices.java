package com.tivic.manager.crt;

import java.sql.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.conexao.*;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;


public class NotaFiscalServices {
	public static final int ST_LANCAMENTO = 0;
	public static final int ST_CONCLUIDA = 1;
	public static final int ST_CONFERIDA = 2;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.NM_EMPRESA, C.NM_PESSOA "+
		                   "FROM ADM_NOTA_FISCAL A, GRL_EMPRESA B, GRL_PESSOA C "+
		                   "WHERE A.CD_PESSOA  = C.CD_PESSOA "+
		                   "  AND A.CD_EMPRESA = B.CD_EMPRESA ",
		                   " ORDER BY A.DT_NOTA_FISCAL, B.NM_EMPRESA, C.NM_PESSOA", criterios, Conexao.conectar(), true);
	}
	public static ResultSetMap findWithTotal(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.NM_EMPRESA, C.NM_PESSOA, "+
						   " (SELECT SUM(vl_pago) FROM sce_contrato_comissao D "+
						   "  WHERE D.cd_nota_fiscal = A.cd_nota_fiscal) AS vl_lancado, "+
						   " (SELECT COUNT(*) FROM sce_contrato_comissao D "+
						   "  WHERE D.cd_nota_fiscal = A.cd_nota_fiscal) AS qt_lancada "+
		                   "FROM ADM_NOTA_FISCAL A, GRL_EMPRESA B, GRL_PESSOA C "+
		                   "WHERE A.CD_PESSOA  = C.CD_PESSOA "+
		                   "  AND A.CD_EMPRESA = B.CD_EMPRESA ",
		                   "ORDER BY DT_NOTA_FISCAL, NM_EMPRESA, NM_PESSOA", criterios, Conexao.conectar(), true);
	}
	public static ResultSetMap findWithTotalOrderByPessoa(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.NM_EMPRESA, C.NM_PESSOA, "+
						   " (SELECT SUM(vl_pago) FROM sce_contrato_comissao D "+
						   "  WHERE D.cd_nota_fiscal = A.cd_nota_fiscal) AS vl_lancado, "+
						   " (SELECT COUNT(*) FROM sce_contrato_comissao D "+
						   "  WHERE D.cd_nota_fiscal = A.cd_nota_fiscal) AS qt_lancada "+
		                   "FROM ADM_NOTA_FISCAL A, GRL_EMPRESA B, GRL_PESSOA C "+
		                   "WHERE A.CD_PESSOA  = C.CD_PESSOA "+
		                   "  AND A.CD_EMPRESA = B.CD_EMPRESA ",
		                   "ORDER BY NM_EMPRESA, NM_PESSOA, DT_NOTA_FISCAL", criterios, Conexao.conectar(), true);
	}
	public static ResultSetMap findWithGroupedByEmpresa(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.cd_nota_fiscal, A.nr_nota_fiscal, A.dt_nota_fiscal, A.vl_nota_fiscal, "+
		                   "       B.nm_pessoa, D.nm_empresa, D.pr_iss, SUM(vl_pago) AS vl_pago, " +
		                   "       COUNT(*) qt_comissoes, (SUM(vl_pago) * D.pr_iss / 100) AS vl_iss "+
		                   "FROM adm_nota_fiscal A, grl_pessoa B, sce_contrato_comissao C, grl_empresa D "+
		                   "WHERE A.cd_pessoa      = B.cd_pessoa "+
		                   "  AND A.cd_nota_fiscal = C.cd_nota_fiscal "+
		                   "  AND C.cd_empresa     = D.cd_empresa ",
		                   "GROUP BY A.cd_nota_fiscal, A.nr_nota_fiscal, A.dt_nota_fiscal, A.vl_nota_fiscal, "+
		                   "         B.nm_pessoa, D.nm_empresa, D.pr_iss "+
		                   "ORDER BY A.dt_nota_fiscal, B.nm_pessoa, D.nm_empresa", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap getItensOfNota(int cdNotaFiscal) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT A.cd_contrato_emprestimo, A.dt_contrato, A.vl_financiado, C.nm_empresa, "+
			             "       D.nm_pessoa, D.nr_cpf_cnpj, E.nm_produto, F.qt_parcelas, G.nm_situacao, "+
			             "       H.nm_operacao, B.vl_pago AS vl_pago_comissao, B.vl_comissao, B.pr_aplicacao, "+
			             "       B.cd_comissao "+
		                 "FROM sce_contrato          A "+
						 "JOIN sce_contrato_comissao B ON (A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
						 "                             AND B.cd_pessoa IS NULL) "+
						 "JOIN grl_empresa       C ON (A.cd_empresa = C.cd_empresa) "+
						 "JOIN grl_pessoa        D ON (A.cd_contratante = D.cd_pessoa) "+
						 "JOIN sce_produto       E ON (A.cd_produto = E.cd_produto) "+
						 "JOIN sce_produto_plano F ON (A.cd_produto = F.cd_produto AND A.cd_plano = F.cd_plano) "+
						 "JOIN sce_situacao      G ON (A.cd_situacao = G.cd_situacao) "+
						 "LEFT OUTER JOIN sce_tipo_operacao H ON (A.cd_operacao = H.cd_operacao)"+
						 "WHERE B.cd_nota_fiscal = ? ";
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdNotaFiscal);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.getItensOfNota: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap importTabela(byte[] content, int tpTabela, int cdNotaFiscal, int cdSituacaoContrato,
										 int cdSituacaoComissaoEmpresa, int cdSituacaoComissaoAgente, GregorianCalendar dtMinima)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap rsmLog = new ResultSetMap();
		try {
			NotaFiscal notaFiscal = NotaFiscalDAO.get(cdNotaFiscal, connect);
			connect.setAutoCommit(false);
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromByte(content, "\t", true);
			int count = 0;
			int erros = 0;
			int nrLinha = 0;
			while(rsm.next())	{
				nrLinha++;
				if(nrLinha%10==0 && nrLinha>0)	{
					connect.commit();
					Conexao.desconectar(connect);
					connect = Conexao.conectar();
					connect.setAutoCommit(false);
				}
				System.out.println("ContratoServices.importTabela: "+nrLinha);
				HashMap<String,Object> regLog = new HashMap<String,Object>();
				String nrCpf = "00000000000";
				try	{
					switch(tpTabela)	{
						case 0: // BMC
							nrCpf = rsm.getString("CLIENTE");
							if(nrCpf!=null)
								nrCpf = nrCpf.substring(0,11);
						   	break;
						case 1: // BGN e GE
						case 2:
							nrCpf = rsm.getString("CPF");
							break;
					}
					if(nrCpf==null)
						continue;
					nrCpf=nrCpf.replaceAll("\\.","").replaceAll("-","");
					nrCpf = "00000000000".substring(0,11-nrCpf.length())+nrCpf;
					if(nrCpf==null || nrCpf.equals("00000000000"))
						continue;
					if(!Util.isCpfValido(nrCpf))	{
						regLog.put("NR_LINHA", new Integer(nrLinha));
						regLog.put("NM_CLIENTE", nrCpf);
						regLog.put("NM_MENSAGEM", "Cpf Inválido");
						rsmLog.addRegister(regLog);
						erros++;
						continue;
					}
				}
				catch(Exception e)	{
					regLog.put("NR_LINHA", new Integer(nrLinha));
					regLog.put("NM_CLIENTE", nrCpf);
					regLog.put("NM_MENSAGEM", "Erro ao tentar encontrar CPF. ERRO: "+e.getMessage());
					rsmLog.addRegister(regLog);
					erros++;
					continue;
				}
				String nmCliente = "", nrContrato = "";
				GregorianCalendar dtPagamento = notaFiscal.getDtNotaFiscal();
				GregorianCalendar dtContrato = notaFiscal.getDtNotaFiscal();
				int qtParcelas = 0;
				float vlPagoComissao=0, vlLiberado=0, prComissao=0;
				try	{
					switch(tpTabela)	{
						case 0: // BMC
							nmCliente = rsm.getString("CLIENTE");
							if(nmCliente!=null)
								nmCliente = nmCliente.substring(14, nmCliente.length());
							nrContrato = rsm.getString("CONTRATO");
							String parcelas = rsm.getString("PLANO");
							if(parcelas!=null && parcelas.indexOf("-")>=0)
								qtParcelas = Integer.parseInt(parcelas.substring(0, parcelas.indexOf("-")).trim());
							vlLiberado = Float.parseFloat(rsm.getString("VALOR").replaceAll("\\.","").replaceAll(",",".").replaceAll("R$","").trim());
							vlPagoComissao = Float.parseFloat(rsm.getString("COMISSAO").replaceAll("\\.","").replaceAll(",",".").replaceAll("R$","").trim());
							prComissao 	   = Float.parseFloat(rsm.getString("PERCENTUAL").replaceAll("\\.","").replaceAll(",",".").replaceAll("%","").trim());
							if(Util.stringToCalendar(rsm.getString("DATA"))!=null)	{
								dtPagamento = Util.stringToCalendar(rsm.getString("DATA"));
								dtContrato  = Util.stringToCalendar(rsm.getString("DATA"));
							}
						break;
						case 1: // BGN
							nmCliente	   = rsm.getString("CLIENTE");
							nrContrato     = rsm.getString("OPERACAO").trim();
							qtParcelas     = Integer.parseInt(rsm.getString("QTD PARC").trim());
							vlLiberado     = Float.parseFloat(rsm.getString("VLR LIB").replaceAll("\\.","").replaceAll(",",".").replaceAll("R$","").trim());
							vlPagoComissao = Float.parseFloat(rsm.getString("COMISSAO").replaceAll("\\.","").replaceAll(",",".").replaceAll("R$","").trim());
							prComissao 	   = Float.parseFloat(rsm.getString("PERC").replaceAll("\\.","").replaceAll(",",".").replaceAll("%","").trim());
							if(Util.stringToCalendar(rsm.getString("DT BASE"))!=null)	{
								dtPagamento = Util.stringToCalendar(rsm.getString("DT BASE"));
								dtContrato  = Util.stringToCalendar(rsm.getString("DT BASE"));
							}
						break;
						case 2: // GE
							nmCliente	   = rsm.getString("CLIENTE");
							nrContrato     = rsm.getString("CONTRATO").trim();
							qtParcelas     = Integer.parseInt(rsm.getString("QTD. PARC.").trim());
							vlLiberado     = Float.parseFloat(rsm.getString("VL. LIBERADO").replaceAll("\\.","").replaceAll(",",".").replaceAll("[R$]","").trim());
							vlPagoComissao = Float.parseFloat(rsm.getString("COMISS. R$").replaceAll("\\.","").replaceAll(",",".").replaceAll("[R$]","").trim());
							prComissao 	   = Float.parseFloat(rsm.getString("COMISS. %").replaceAll("\\.","").replaceAll(",",".").replaceAll("%","").trim());
							if(Util.stringToCalendar(rsm.getString("DT. PAGTO COM"))!=null)
								dtPagamento = Util.stringToCalendar(rsm.getString("DT. PAGTO COM"));
							if(Util.stringToCalendar(rsm.getString("DT. CONTRATO"))!=null)
								dtContrato  = Util.stringToCalendar(rsm.getString("DT. CONTRATO"));
						break;
					}
				}
				catch(Exception e)	{
					regLog.put("NR_LINHA", new Integer(nrLinha));
					regLog.put("NM_CLIENTE", nrCpf);
					regLog.put("NM_MENSAGEM", "Erro ao tentar separar campos. ERRO: "+e.getMessage());
					rsmLog.addRegister(regLog);
					erros++;
					continue;
				}
				if(dtPagamento.get(Calendar.YEAR)<2000)
					dtPagamento.set(Calendar.YEAR, dtPagamento.get(Calendar.YEAR)+2000);
				if(dtContrato.get(Calendar.YEAR)<2000)
					dtContrato.set(Calendar.YEAR, dtContrato.get(Calendar.YEAR)+2000);
				// Filtros Parceiro, Quantidade de Parcelas, Cpf e Data da Operação
				String sql = "SELECT A.cd_contrato_emprestimo, B.cd_comissao, B.vl_comissao, B.pr_aplicacao, B.cd_nota_fiscal "+
                             "FROM sce_contrato A, sce_contrato_comissao B, grl_pessoa C, "+
                             "     sce_produto D "+
                             "WHERE A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
                             "  AND A.cd_contratante         = C.cd_pessoa "+
                             "  AND A.cd_produto             = D.cd_produto "+
                             "  AND B.cd_pessoa IS NULL "+
                             "  AND C.nr_cpf_cnpj   = \'"+nrCpf+"\'"+
                             "  AND D.cd_instituicao_financeira = "+notaFiscal.getCdPessoa()+
                             "  AND A.dt_contrato >= ? "+
							 "  AND ((B.cd_nota_fiscal IS NULL AND A.qt_parcelas = "+qtParcelas+") "+
							 "     OR B.cd_nota_fiscal = "+cdNotaFiscal+")";
				pstmt = connect.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(dtMinima.getTimeInMillis()));
				ResultSetMap rsmComissoes = new ResultSetMap(pstmt.executeQuery());
				if(rsmComissoes.size()>1)	{
					// Acrescenta
					pstmt = connect.prepareStatement(sql + "  AND A.vl_financiado = "+vlLiberado+
							                               "  AND A.qt_parcelas = "+qtParcelas);
					pstmt.setTimestamp(1, new Timestamp(dtMinima.getTimeInMillis()));
					rsmComissoes = new ResultSetMap(pstmt.executeQuery());
				}
				// Se conseguiu encontrar apenas 1 contrato ou comissão
				int ret = 0;
				if(rsmComissoes.size()==1)	{
					rsmComissoes.first();
					/*
					if(prComissao<rsmComissoes.getFloat("pr_aplicacao"))	{
						regLog.put("NR_LINHA", new Integer(nrLinha));
						regLog.put("NM_MENSAGEM", "ALERTA - Percentual Diferente");
						regLog.put("NR_CPF", nrCpf);
						regLog.put("NM_CLIENTE", nmCliente);
						regLog.put("DT_CONTRATO", dtContrato);
						regLog.put("QT_PARCELAS", new Integer(qtParcelas));
						regLog.put("VL_LIBERADO", new Float(vlLiberado));
						regLog.put("PR_APLICACAO", new Float(prComissao));
						regLog.put("VL_COMISSAO", new Float(vlPagoComissao));
						rsmLog.addRegister(regLog);
					}
					*/
					ret = 1;
					if(rsmComissoes.getInt("cd_nota_fiscal")!=cdNotaFiscal)	{
						ret = com.tivic.manager.crt.ContratoServices.confirmarContrato(cdNotaFiscal, rsmComissoes.getInt("cd_contrato_emprestimo"),
												rsmComissoes.getInt("cd_comissao"),
												cdSituacaoContrato, cdSituacaoComissaoEmpresa, cdSituacaoComissaoAgente,
												vlLiberado, prComissao, vlPagoComissao, dtPagamento, nrContrato, null);
					}
				}
				if(ret<=0)	{
					erros++;
					regLog.put("NR_LINHA", new Integer(nrLinha));
					regLog.put("NR_CPF", nrCpf);
					regLog.put("NM_CLIENTE", nmCliente);
					regLog.put("DT_CONTRATO", dtContrato);
					regLog.put("NR_CONTRATO", nrContrato);
					regLog.put("QT_PARCELAS", new Integer(qtParcelas));
					regLog.put("VL_LIBERADO", new Float(vlLiberado));
					regLog.put("PR_APLICACAO", new Float(prComissao));
					regLog.put("VL_COMISSAO", new Float(vlPagoComissao));
					if(ret < 0)
						regLog.put("NM_MENSAGEM", "Problema ao tentar confirmar comissão");
					else if(rsmComissoes.size()==0)
						regLog.put("NM_MENSAGEM", "Proposta não localizada");
					else
						regLog.put("NM_MENSAGEM", rsmComissoes.size()+" Propostas encontradas o mesmo cpf.");
					rsmLog.addRegister(regLog);
				}
				else
					count++;
			}
			connect.commit();
			//
			HashMap<String,Object> regLog = new HashMap<String,Object>();
			regLog.put("NM_MENSAGEM", erros+" registro(s) com erro");
			rsmLog.addRegister(regLog);
			//
			regLog = new HashMap<String,Object>();
			regLog.put("NM_MENSAGEM", count+" registro(s) corretos");
			rsmLog.addRegister(regLog);
			//
			regLog = new HashMap<String,Object>();
			regLog.put("NM_MENSAGEM", "Total de registros: "+(erros+count));
			rsmLog.addRegister(regLog);
			System.out.println(count+" registro(s) corretos, "+erros+" registro(s) com erro");
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.importTabela: " +  e);
			HashMap<String,Object> regLog = new HashMap<String,Object>();
			regLog.put("NM_MENSAGEM", "Erro! ContratoServices.importTabela: " +  e);
			rsmLog.addRegister(regLog);
		}
		finally	{
			Conexao.desconectar(connect);
		}
		return rsmLog;
	}

	public static int verificaSituacao(int cdNotaFiscal) {
		return verificaSituacao(cdNotaFiscal, null);
	}
	public static int verificaSituacao(int cdNotaFiscal, Connection connect) {
		PreparedStatement pstmt;
		boolean desconectar = connect==null;
		if(desconectar)
			connect = Conexao.conectar();
		try {
			NotaFiscal notaFiscal = NotaFiscalDAO.get(cdNotaFiscal);
			pstmt = connect.prepareStatement(
					"SELECT count(*), SUM(vl_pago) "+
					"FROM sce_contrato_comissao  "+
					"WHERE cd_nota_fiscal = ?");
			pstmt.setInt(1, cdNotaFiscal);
			ResultSet rs = pstmt.executeQuery();
			if(notaFiscal!=null && rs.next() && notaFiscal.getQtItens()==rs.getInt(1) && (notaFiscal.getVlTotalNota()-rs.getFloat(2)<=0.01) && !(notaFiscal.getVlTotalNota()-rs.getFloat(2)<=-0.01))	{
				pstmt = connect.prepareStatement("UPDATE adm_nota_fiscal SET st_nota_fiscal = ? WHERE cd_nota_fiscal = ? ");
				pstmt.setInt(1, NotaFiscalServices.ST_CONFERIDA);
				pstmt.setInt(2, cdNotaFiscal);
				pstmt.executeUpdate();
				return ST_CONFERIDA;
			}
			return ST_LANCAMENTO;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.verificaSituacao: " +  e);
			return ST_LANCAMENTO;
		}
		finally{
			if(desconectar)
				Conexao.desconectar(connect);
		}
	}
	public static int substituirNota(int cdNotaFiscalOrigem, int cdNotaFiscalDestino) {
		PreparedStatement pstmt;
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			// Buscando nota fiscal de Origem
			NotaFiscal notaFrom = NotaFiscalDAO.get(cdNotaFiscalOrigem, connect);
			NotaFiscal notaTo = NotaFiscalDAO.get(cdNotaFiscalDestino, connect);
			if(notaFrom==null)	{
				System.err.println("NotaFiscalServices.substituirNota: Nota Fiscal de Origem inválida! cdNotaFiscalOrigem = "+cdNotaFiscalOrigem);
				return -10;
			}
			if(notaTo==null)	{
				System.err.println("NotaFiscalServices.substituirNota: Nota Fiscal de Destino inválida! cdNotaFiscalDestino = "+cdNotaFiscalDestino);
				return -20;
			}
			int cdTipoDocumento = 0;
			ContaReceber contaReceber = new ContaReceber(0, notaFrom.getCdPessoa(), notaFrom.getCdEmpresa(), 0 /*cdContrato*/,
														 0 /*cdContaOrigem*/, notaTo.getCdNotaFiscal() /*cdDocumentoSaida*/,
														 0 /*cdContaCarteira*/, 0 /*cdConta*/, 0 /*cdFrete*/, String.valueOf(notaFrom.getNrNotaFiscal()) /*nrDocumento*/,
														 null /*idContaReceber*/, 0 /*nrParcela*/, null /*nrReferencia*/, cdTipoDocumento,
														 null /*dsHistorico*/, notaFrom.getDtNotaFiscal() /*dtVencimento*/, notaFrom.getDtEmissao(),
														 null /*dtRecebimento*/, null /*dtProrrogacao*/,
														 Double.valueOf( Float.toString(notaFrom.getVlNotaFiscal())),
														 0.0d /*vlAbatimento*/, 0.0d /*vlAcrescimo*/, 0.0d /*vlRecebido*/, com.tivic.manager.adm.ContaReceberServices.ST_EM_ABERTO,
														 0 /*tpFrequencia*/, 0 /*qtParcelas*/, 0 /*tpContaReceber*/, 0  /*cdNegociacao*/,
														 "" /*txtObservacao*/, 0 /*cdPlanoPagamento*/, 0 /*cdFormaPagamento*/,
														 new GregorianCalendar(), notaFrom.getDtNotaFiscal(), 0/*cdTurno*/,
														 0.0d/*prJuros*/, 0.0d/*prMulta*/, 0/*lgProtesto*/);
			int cdContaReceber = com.tivic.manager.adm.ContaReceberDAO.insert(contaReceber, connect);
			if(cdContaReceber<=0)	{
				System.err.println("NotaFiscalServices.substituirNota: Não foi possível criar Conta Receber!");
				return -30;
			}
			// Alterando Comissão
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato_comissao SET cd_conta_receber = ?, cd_nota_fiscal = ? "+
					"WHERE cd_nota_fiscal = ?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdNotaFiscalDestino);
			pstmt.setInt(3, cdNotaFiscalOrigem);
			pstmt.executeUpdate();
			// Excluindo nota de origem
			NotaFiscalDAO.delete(notaFrom.getCdNotaFiscal(), connect);
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.substituirNota: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int getCdFechamento(int cdNotaFiscal) {
		PreparedStatement pstmt;
		Connection connect = Conexao.conectar();
		try {
			pstmt = connect.prepareStatement(
					"SELECT cd_fechamento FROM adm_nota_fiscal "+
					"WHERE cd_nota_fiscal = ?");
			pstmt.setInt(1, cdNotaFiscal);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			return 0;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalServices.substituirNota: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
}