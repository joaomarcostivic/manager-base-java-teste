package com.tivic.manager.crt;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.sql.*;
import java.util.ArrayList;

import com.tivic.manager.adm.*;
import com.tivic.manager.conexao.*;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;


public class ContaReceberServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		String sql = "SELECT A.*, B.nm_empresa, C.nm_pessoa "+
		             "FROM adm_conta_pagar A "+
		             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
		             "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) ";
		return Search.find(sql, "ORDER BY dt_vencimento, nm_empresa, nm_pessoa", criterios, Conexao.conectar(), true, false);
	}

	public static ResultSetMap findWithComissao(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			String sql = "SELECT A.*, B.nm_empresa, C.nm_pessoa, "+
						 "       F.cd_comissao, F.vl_comissao, F.vl_pago AS vl_pago_comissao, F.pr_aplicacao, "+
						 "       G.cd_contrato_emprestimo, G.dt_contrato, G.qt_parcelas AS qt_parcelas_contrato, "+
						 "       G.dt_pagamento, G.vl_financiado, G.vl_liberado, "+
						 "       H.nm_produto, I.nm_pessoa AS nm_contratante, "+
						 "       J.nm_pessoa AS nm_parceiro "+
			             "FROM adm_conta_receber A "+
			             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
			             "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) "+
			             "JOIN sce_contrato_comissao F ON (A.cd_conta_receber = F.cd_conta_receber) "+
			             "JOIN sce_contrato G ON (F.cd_contrato_emprestimo = G.cd_contrato_emprestimo) "+
			             "JOIN sce_produto  H ON (G.cd_produto = H.cd_produto ) "+
			             "JOIN grl_pessoa   I ON (G.cd_contratante = I.cd_pessoa)"+
			             "JOIN grl_pessoa   J ON (H.cd_instituicao_financeira = J.cd_pessoa)";
			ResultSetMap rsm = Search.find(sql, "ORDER BY A.dt_vencimento, B.nm_empresa, C.nm_pessoa", criterios, Conexao.conectar(), true);
			int cdContaReceber = 0;
			while(rsm.next())
				if(rsm.getFloat("vl_abatimento")>0 && cdContaReceber!=rsm.getInt("cd_conta_receber")){
					cdContaReceber = rsm.getInt("cd_conta_receber");
					pstmt = connect.prepareStatement("SELECT * FROM sce_adiantamento_pagamento A, sce_adiantamento B "+
							                         "WHERE A.cd_conta_receber = ? "+
							                         "  AND A.cd_adiantamento = B.cd_adiantamento");
					pstmt.setInt(1, rsm.getInt("cd_conta_receber"));
					ResultSet rs = pstmt.executeQuery();
					while(rs.next()){
						HashMap<String,Object> register = new HashMap<String,Object>();
						register.put("CD_CONTA_PAGAR", new Integer(rsm.getInt("cd_conta_receber")));
						register.put("CD_ADIANTAMENTO", new Integer(rsm.getInt("cd_adiantamento")));
						register.put("NM_EMPRESA", rsm.getString("NM_EMPRESA"));
						register.put("NM_PESSOA", rsm.getString("NM_PESSOA"));
						register.put("NM_PARCEIRO", "");
						register.put("NM_PRODUTO", "");
						register.put("TP_COMISSAO", new Integer(-1));
						register.put("VL_PAGO_COMISSAO", new Float(rs.getFloat("VL_PAGO")*(-1)));
						register.put("DT_CONTRATO", rs.getTimestamp("DT_ADIANTAMENTO"));
						register.put("VL_LIBERADO", new Float(rs.getFloat("VL_ADIANTAMENTO")));
						register.put("PR_APLICACAO", new Float(0));
						register.put("QT_PARCELAS_CONTRATO", new Integer(rs.getInt("QT_PARCELAS")));
						register.put("NM_CONTRATANTE", "Desconto de Adiantamento");
						rsm.addRegister(register);
					}
				}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.findWithComissao: " +  e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		String group  = "";
		String fields = " A.*, B.nm_empresa, C.nm_pessoa ";

		for(int i=0; i<groupBy.size();i++)	{
			String nmToGroup = (String)groupBy.get(i);
			String nmToField = (String)groupBy.get(i);
			if(nmToGroup.toUpperCase().indexOf("AS")>=0)	{
				nmToGroup = nmToGroup.substring(0, nmToGroup.toUpperCase().indexOf("AS"));
				nmToField = nmToField.substring(nmToField.toUpperCase().indexOf("AS")+3, nmToField.length()).trim();
			}
			//
			if(nmToField.equals("NM_EMPRESA"))	{
				nmToField = " A.cd_empresa, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " A.cd_empresa, "+nmToGroup;
			}
			else if(nmToField.equals("NM_PESSOA"))	{
				nmToField = " A.cd_pessoa, "+nmToGroup+" AS "+nmToField;
				nmToGroup = " A.cd_pessoa, "+nmToGroup;
			}
			else
				nmToField = nmToGroup+" AS "+nmToField;
			if(i==0)	{
				group  = "GROUP BY "+nmToGroup;
				fields = nmToField;
			}
			else	{
				fields = fields+", "+nmToField;
				group  = group+", "+nmToGroup;
			}
		}
		fields = groupBy.size()==0 ? fields : fields + ", COUNT(*) AS QT_CONTA, SUM(vl_conta) AS vl_conta,  "+
		                                               "  SUM(vl_abatimento) AS vl_abatimento ";


		String sql = "SELECT "+fields+" "+
		             "FROM adm_conta_receber A "+
		             "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
	    	         "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) ";
		return Search.find(sql, group, criterios, Conexao.conectar(), true, false);
	}



	public static int atualizaValoresConta(int cdContaReceber, Connection connect) {
		PreparedStatement pstmt;
		try	{
			float vlConta = 0;
			float vlAbatimento = 0;
			// Somando comissões
			pstmt = connect.prepareStatement(
					 "SELECT SUM(vl_recebido) FROM sce_contrato_comissao "+
					 "WHERE cd_conta_receber = ? "+
					 "  AND vl_recebido > 0");
			pstmt.setInt(1, cdContaReceber);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				vlConta = rs.getFloat(1);
			// Somando estornos
			pstmt = connect.prepareStatement(
					 "SELECT SUM(vl_pago) FROM sce_contrato_comissao "+
					 "WHERE cd_conta_receber = ? "+
					 "  AND vl_pago < 0");
			pstmt.setInt(1, cdContaReceber);
			rs = pstmt.executeQuery();
			if(rs.next())
				vlAbatimento = (rs.getFloat(1) * -1);
			// Somando Adiantamentos
			pstmt = connect.prepareStatement(
					 "SELECT SUM(vl_pago) FROM sce_adiantamento_pagamento "+
					 "WHERE cd_conta_receber = ? ");
			pstmt.setInt(1, cdContaReceber);
			rs = pstmt.executeQuery();
			if(rs.next())
				vlAbatimento += rs.getFloat(1);
			// Atualizando valor da conta
			if(vlConta==0 && vlAbatimento==0)	{
				pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber WHERE cd_conta_receber = ? ");
				pstmt.setInt(1, cdContaReceber);
			}
			else	{
				pstmt = connect.prepareStatement(
						 "UPDATE adm_conta_receber SET vl_conta = ?, vl_abatimento = ? "+
						 "WHERE cd_conta_receber = ? ");
				pstmt.setFloat(1, vlConta);
				pstmt.setFloat(2, vlAbatimento);
				pstmt.setInt(3, cdContaReceber);
			}
			pstmt.executeUpdate();
			pstmt.close();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.setContaReceber: " +  e);
			return -1;
		}
	}

	public static ResultSetMap getItensOfPlanilha(int cdContaReceber) {
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
						 "WHERE B.cd_conta_receber = ? ";
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdContaReceber);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberServices.getItensOfPlanilha: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap importTabela(byte[] content, int tpTabela, int cdContaReceber, int cdSituacaoContrato,
			 								int cdSituacaoComissaoEmpresa, int cdSituacaoComissaoAgente, GregorianCalendar dtMinima)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			connect.setAutoCommit(false);
			ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber);
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromByte(content, "\t", true);
			ResultSetMap rsmLog = new ResultSetMap();
			int count = 0;
			int erros = 0;
			int nrLinha = 0;
			while(rsm.next())	{
				nrLinha++;
				HashMap<String,Object> regLog = new HashMap<String,Object>();
				String nrCpf = "00000000000";
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
				String nmCliente = "", nrContrato = "";
				GregorianCalendar dtPagamento = contaReceber.getDtRecebimento();
				GregorianCalendar dtContrato = contaReceber.getDtRecebimento();
				int qtParcelas = 0;
				float vlPagoComissao=0, vlLiberado=0, prComissao=0;
				switch(tpTabela)	{
					case 0: // BMC
						nmCliente = rsm.getString("CLIENTE");
						if(nmCliente!=null)
							nmCliente = nmCliente.substring(12, nmCliente.length());
						nrContrato = rsm.getString("CONTRATO");
						String parcelas = rsm.getString("PLANO");
						if(parcelas!=null && parcelas.indexOf("-")>=0)
							qtParcelas = Integer.parseInt(parcelas.substring(0, parcelas.indexOf("-")).trim());
						vlLiberado = Float.parseFloat(rsm.getString("VALOR").replaceAll("\\.","").replaceAll(",",".").replaceAll("R$","").trim());
						vlPagoComissao = Float.parseFloat(rsm.getString("COMISSAO").replaceAll("\\.","").replaceAll(",",".").replaceAll("R$","").trim());
						prComissao 	   = Float.parseFloat(rsm.getString("PERCENTUAL").replaceAll("\\.","").replaceAll(",",".").replaceAll("%","").trim());
						if(Util.stringToCalendar(rsm.getString("DATA"))!=null)	{
							dtPagamento = Util.stringToCalendar(rsm.getString("DATA"));
							dtContrato = Util.stringToCalendar(rsm.getString("DATA"));
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
				// Filtros Parceiro, Quantidade de Parcelas, Cpf e Data da Operação
				String sql = "SELECT A.cd_contrato_emprestimo, B.cd_comissao, B.vl_comissao, B.pr_aplicacao, B.cd_conta_receber "+
                             "FROM sce_contrato A, sce_contrato_comissao B, grl_pessoa C, "+
                             "     sce_produto D "+
                             "WHERE A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
                             "  AND A.cd_contratante         = C.cd_pessoa "+
                             "  AND A.cd_produto             = D.cd_produto "+
                             "  AND B.cd_pessoa IS NULL "+
                             "  AND C.nr_cpf_cnpj   = ? "+
                             "  AND D.cd_instituicao_financeira = ? "+
                             "  AND A.qt_parcelas  = ? "+
                             "  AND A.dt_contrato >= ? "+
							 "  AND (B.cd_conta_receber IS NULL OR B.cd_conta_receber = ?) ";
				pstmt = connect.prepareStatement(sql);
				pstmt.setString(1, nrCpf);
				pstmt.setInt(2, contaReceber.getCdPessoa());
				pstmt.setInt(3, qtParcelas);
				pstmt.setTimestamp(4, new Timestamp(dtMinima.getTimeInMillis()));
				pstmt.setInt(5, cdContaReceber);
				ResultSetMap rsmComissoes = new ResultSetMap(pstmt.executeQuery());
				if(rsmComissoes.size()>1)	{
					// Acrescenta
					pstmt = connect.prepareStatement(sql + "  AND A.vl_financiado = ? ");
					pstmt.setString(1, nrCpf);
					pstmt.setInt(2, contaReceber.getCdPessoa());
					pstmt.setInt(3, qtParcelas);
					pstmt.setTimestamp(4, new Timestamp(dtMinima.getTimeInMillis()));
					pstmt.setInt(5, cdContaReceber);
					pstmt.setFloat(6, vlLiberado);
					rsmComissoes = new ResultSetMap(pstmt.executeQuery());
				}
				// Se conseguiu encontrar apenas 1 contrato ou comissão
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
					if(rsmComissoes.getInt("cd_conta_receber")!=cdContaReceber)
						ContratoServices.confirmarContrato(cdContaReceber, rsmComissoes.getInt("cd_contrato_emprestimo"),rsmComissoes.getInt("cd_comissao"),
												cdSituacaoContrato, cdSituacaoComissaoEmpresa, cdSituacaoComissaoAgente,
												vlLiberado, prComissao, vlPagoComissao, dtPagamento, nrContrato, connect);
					count++;
				}
				else	{
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
					if(rsmComissoes.size()==0)
						regLog.put("NM_MENSAGEM", "Proposta não localizada");
					else
						regLog.put("NM_MENSAGEM", rsmComissoes.size()+" Propostas encontradas o mesmo cpf.");
					rsmLog.addRegister(regLog);
				}
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
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.importTabelaBMC: " +  e);
			HashMap<String,Object> regLog = new HashMap<String,Object>();
			regLog.put("NM_MENSAGEM", "Erro! ContratoServices.importTabelaBMC: " +  e);
			ResultSetMap rsmLog = new ResultSetMap();
			rsmLog.addRegister(regLog);
			return rsmLog;
		}
	}
}