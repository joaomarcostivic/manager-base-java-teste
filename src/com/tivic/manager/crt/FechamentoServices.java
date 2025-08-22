package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashMap;

import com.tivic.manager.adm.ContaPagar;
import com.tivic.manager.adm.ContaPagarDAO;
import com.tivic.manager.conexao.*;
import com.tivic.manager.grl.*;
import com.tivic.sol.connection.Conexao;


public class FechamentoServices {
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_empresa "+
		                   "FROM sce_fechamento A " +
		                   "LEFT OUTER JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa)", "ORDER BY dt_fechamento", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap getNotasOfFechamento(int cdFechamento, boolean groupedByEmpresa) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_fechamento", String.valueOf(cdFechamento), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			if(groupedByEmpresa)
				return NotaFiscalServices.findWithGroupedByEmpresa(criterios);
			else
				return NotaFiscalServices.findWithTotalOrderByPessoa(criterios);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.getNotasOfFechamento: " +  e);
			return null;
		}
	}

	public static ResultSetMap getNotasOutOfFechamento(int cdEmpresa) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_FECHAMENTO", "0", ItemComparator.ISNULL, java.sql.Types.INTEGER));
			criterios.add(new ItemComparator("ST_NOTA_FISCAL", String.valueOf(NotaFiscalServices.ST_CONFERIDA), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_EMPRESA", String.valueOf(cdEmpresa), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			return NotaFiscalServices.find(criterios);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.getNotasOutOfFechamento: " +  e);
			return null;
		}
	}

	public static int insertNotaInFechamento(int cdFechamento, int cdNotaFiscal)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Verificando plano por produto
			pstmt = connect.prepareStatement("UPDATE adm_nota_fiscal SET cd_fechamento = ? "+
			                                 "WHERE cd_nota_fiscal = ?");
			pstmt.setInt(1, cdFechamento);
			pstmt.setInt(2, cdNotaFiscal);
			pstmt.executeUpdate();
			return cdNotaFiscal;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.insertNotaInFechamento: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.insertNotaInFechamento: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteNotaFromFechamento(int cdNotaFiscal)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Verificando plano por produto
			pstmt = connect.prepareStatement("UPDATE adm_nota_fiscal SET cd_fechamento = null "+
			                                 "WHERE cd_nota_fiscal = ?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.executeUpdate();
			return cdNotaFiscal;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.deleteNotaFromFechamento: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.deleteNotaFromFechamento: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteAllContasOfFechamento(int cdFechamento)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Verificando plano por produto
			pstmt = connect.prepareStatement("SELECT cd_conta_pagar FROM adm_conta_pagar "+
			                                 "WHERE cd_fechamento = ? " +
			                                 "  AND st_conta <> 1");
			pstmt.setInt(1, cdFechamento);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
				deleteContaAPagar(rs.getInt(1));
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.deleteAllContasOfFechamento: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.deleteAllContasOfFechamento: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getBloqueiosDesbloqueios(int cdFechamento)	{
		Connection connect = Conexao.conectar();
		try {
			// Comissões que foram pagas nesse fechamento mas que são de fechamentos anteriores
			String sql = "SELECT \'Desbloqueios\' AS tp_registro, A.*, B.nm_empresa, C.nm_pessoa, D.dt_contrato, D.dt_pagamento, D.vl_financiado, "+
					     "       D.vl_liberado, D.qt_parcelas, D.cd_documento, D.cd_agente, D.cd_atendente, "+
					     "       E.nm_pessoa AS nm_contratante, E.nr_cpf_cnpj, F.nm_produto, G.nm_pessoa AS nm_parceiro "+
						 "FROM sce_contrato_comissao A, grl_empresa B, grl_pessoa C, sce_contrato D, "+
						 "     grl_pessoa E, sce_produto F, grl_pessoa G, adm_conta_pagar H "+
						 "WHERE A.cd_conta_pagar = H.cd_conta_pagar " +
						 "  AND A.vl_pago > 0 " +
						 "  AND H.cd_fechamento  = "+cdFechamento+
						 "  AND A.cd_contrato_emprestimo = D.cd_contrato_emprestimo "+
						 "  AND D.cd_contratante = E.cd_pessoa "+
						 "  AND D.cd_produto     = F.cd_produto "+
						 "  AND F.cd_instituicao_financeira = G.cd_pessoa "+
						 "  AND A.cd_empresa     = B.cd_empresa "+
						 "  AND A.cd_pessoa      = C.cd_pessoa "+
						 "  AND A.cd_contrato_emprestimo NOT IN (SELECT cd_contrato_emprestimo "+
						 "                                       FROM sce_contrato_comissao X, adm_nota_fiscal Y "+
						 "                                       WHERE Y.cd_fechamento = "+cdFechamento+
						 "                                         AND Y.cd_nota_fiscal = X.cd_nota_fiscal) "+
						 "ORDER BY C.nm_pessoa, B.nm_empresa, G.nm_pessoa, F.nm_produto, E.nm_pessoa ";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			// Comissões liberadas nesse fechamento que ainda não foram pagas ou foram pagas em outros fechamentos
			sql= "SELECT \'Bloqueios   \' AS tp_registro, A.*, B.nm_empresa, C.nm_pessoa, D.dt_contrato, D.dt_pagamento, D.vl_financiado, "+
			     "       D.vl_liberado, D.qt_parcelas, D.cd_documento, D.cd_agente, D.cd_atendente, "+
			     "       E.nm_pessoa AS nm_contratante, E.nr_cpf_cnpj, F.nm_produto, G.nm_pessoa AS nm_parceiro "+
				 "FROM sce_contrato_comissao A, grl_empresa B, grl_pessoa C, sce_contrato D, "+
				 "     grl_pessoa E, sce_produto F, grl_pessoa G "+
				 "WHERE A.cd_nota_fiscal IS NULL "+
				 "  AND A.cd_conta_pagar IS NULL " +
				 "  AND A.vl_pago > 0 "+
				 "  AND A.cd_contrato_emprestimo = D.cd_contrato_emprestimo "+
				 "  AND D.cd_contratante = E.cd_pessoa "+
				 "  AND D.cd_produto = F.cd_produto "+
				 "  AND F.cd_instituicao_financeira = G.cd_pessoa "+
				 "  AND A.cd_empresa = B.cd_empresa "+
				 "  AND A.cd_pessoa  = C.cd_pessoa "+
				 "  AND A.cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo "+
				 "                                   FROM sce_contrato_comissao X, adm_nota_fiscal Y "+
				 "                                   WHERE Y.cd_fechamento = "+cdFechamento+
				 "                                     AND Y.cd_nota_fiscal = X.cd_nota_fiscal) "+
				 "ORDER BY C.nm_pessoa, B.nm_empresa, G.nm_pessoa, F.nm_produto, E.nm_pessoa ";
			ResultSetMap rsm2 = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			while(rsm2.next())
				rsm.addRegister(rsm2.getRegister());
            return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.getBloqueiosDesbloqueios: " +  e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap[] getComissoesOfFechamento(int cdFechamento, int cdEmpresa,
			int cdVinculo, int cdSituacaoComissao, boolean resumo)
	{
		return getComissoesOfFechamento(cdFechamento, cdEmpresa, cdVinculo, cdSituacaoComissao, resumo, 0);
	}
	public static ResultSetMap[] getComissoesOfFechamento(int cdFechamento, int cdEmpresa,
		int cdVinculo, int cdSituacaoComissao, boolean resumo, int cdPessoa)
	{
		Connection connect = Conexao.conectar();
		try {
			// Pegando lista de responsáveis pelo protocolo
			String matrizes = "";
			ResultSet rs = connect.prepareStatement("SELECT cd_empresa FROM grl_empresa " +
					                                "WHERE lg_matriz = 1").executeQuery();
			while(rs.next())
				matrizes += (matrizes.equals("") ? "" : ",") + rs.getString("cd_empresa");
			// Descobrindo a empresa do fechamento
			int cdEmpresaFechamento = 0;
			Fechamento fechamento = FechamentoDAO.get(cdFechamento);
			cdEmpresaFechamento = fechamento.getCdEmpresa();
			String sql = "";
			if(resumo)	{
				sql= "SELECT B.cd_empresa, B.nm_empresa, C.cd_pessoa, C.nm_pessoa, "+
		             "       SUM(vl_pago) AS vl_total, COUNT(*) as qt_total "+
					 "FROM sce_contrato_comissao A, grl_empresa B, grl_pessoa C "+
					 "WHERE A.cd_nota_fiscal IS NULL "+
					 "  AND A.cd_conta_pagar IS NULL "+
					 "  AND A.cd_empresa = B.cd_empresa "+
					 "  AND A.cd_pessoa  = C.cd_pessoa "+
					 (cdPessoa>0?" AND A.cd_pessoa = "+cdPessoa:"")+
					 (cdEmpresa>0?" AND A.cd_empresa = "+cdEmpresa : "")+
					 "  AND A.cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo "+
					 "                                   FROM sce_contrato_comissao X, adm_nota_fiscal Y "+
					 "                                   WHERE Y.cd_fechamento IN (SELECT W.cd_fechamento FROM sce_fechamento W " +
					 "                                                             WHERE W.cd_empresa = "+cdEmpresaFechamento+") "+
					 "                                     AND Y.cd_nota_fiscal = X.cd_nota_fiscal) "+
					 "GROUP BY B.cd_empresa, B.nm_empresa, C.cd_pessoa, C.nm_pessoa "+
					 "ORDER BY B.nm_empresa, C.nm_pessoa  ";
			}
			else	{
				sql= "SELECT A.*, B.nm_empresa, C.nm_pessoa, D.dt_contrato, D.dt_pagamento, D.vl_financiado, "+
				     "       D.vl_liberado, D.qt_parcelas, D.cd_documento, D.cd_agente, D.cd_atendente, "+
				     "       E.nm_pessoa AS nm_contratante, E.nr_cpf_cnpj, F.nm_produto, G.nm_pessoa AS nm_parceiro "+
					 "FROM sce_contrato_comissao A, grl_empresa B, grl_pessoa C, sce_contrato D, "+
					 "     grl_pessoa E, sce_produto F, grl_pessoa G "+
					 "WHERE A.cd_nota_fiscal IS NULL "+
					 "  AND A.cd_conta_pagar IS NULL "+
					 "  AND A.cd_contrato_emprestimo = D.cd_contrato_emprestimo "+
					 "  AND D.cd_contratante = E.cd_pessoa "+
					 "  AND D.cd_produto = F.cd_produto "+
					 "  AND F.cd_instituicao_financeira = G.cd_pessoa "+
					 "  AND A.cd_empresa = B.cd_empresa "+
					 "  AND A.cd_pessoa  = C.cd_pessoa "+
					 (cdPessoa>0?" AND A.cd_pessoa = "+cdPessoa:"")+
					 (cdEmpresa>0?" AND A.cd_empresa = "+cdEmpresa : "")+
					 "  AND A.cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo "+
					 "                                   FROM sce_contrato_comissao X, adm_nota_fiscal Y "+
					 "                                   WHERE Y.cd_fechamento IN (SELECT W.cd_fechamento FROM sce_fechamento W " +
					 "                                                             WHERE W.cd_empresa = "+cdEmpresaFechamento+") "+
					 "                                     AND Y.cd_nota_fiscal = X.cd_nota_fiscal) "+
					 "ORDER BY C.nm_pessoa, B.nm_empresa, G.nm_pessoa, F.nm_produto, E.nm_pessoa ";
			}
			System.out.println(sql);
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			ResultSetMap rsmBloqueio = new ResultSetMap();
			ResultSetMap rsmRetorno  = new ResultSetMap();
			if(resumo)	{
				while(rsm.next()){
					sql = "SELECT SUM(vl_pago) AS vl_total, COUNT(*) as qt_total "+
					 	  "FROM sce_contrato_comissao A, sce_contrato B, ptc_documento C "+
				 		  "WHERE A.cd_nota_fiscal IS NULL "+
			 			  "  AND A.cd_conta_pagar IS NULL " +
						  "  AND A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
						  "  AND B.cd_documento = C.cd_documento " +
						  "  AND A.vl_pago > 0 "+
						  (cdEmpresa>0?" AND A.cd_empresa = "+cdEmpresa : "")+
						  "  AND A.cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo "+
						  "                                   FROM sce_contrato_comissao X, adm_nota_fiscal Y "+
						  "                                   WHERE Y.cd_fechamento  = "+cdFechamento+
						  "                                     AND Y.cd_nota_fiscal = X.cd_nota_fiscal) "+
						  "  AND A.cd_empresa = "+rsm.getInt("cd_empresa")+
						  "  AND A.cd_pessoa  = "+rsm.getInt("cd_pessoa")+
						  "  AND NOT EXISTS (SELECT * FROM ptc_tramitacao_documento D " +
                          "                  WHERE D.cd_documento = C.cd_documento " +
                          "                    AND D.cd_empresa_destino IN ("+matrizes+")) " +
                          "  AND C.cd_empresa_atual NOT IN ("+matrizes+")";
					rs = connect.prepareStatement(sql).executeQuery();
					if(rs.next())	{
						rsm.setValueToField("QT_TOTAL", new Integer(rsm.getInt("qt_total")-rs.getInt("qt_total")));
						rsm.setValueToField("VL_TOTAL", new Float(rsm.getFloat("vl_total")-rs.getFloat("vl_total")));
						rsm.setValueToField("QT_BLOQUEADO", new Integer(rs.getInt("qt_total")));
						rsm.setValueToField("VL_BLOQUEADO", new Float(rs.getFloat("vl_total")));
					}
				}
				rsmRetorno = rsm;
			}
			else	{
				while(rsm.next()){
					if(connect.prepareStatement("SELECT * FROM ptc_documento A " +
				                                "WHERE A.cd_documento = "+rsm.getInt("cd_documento")+
				      						  	"  AND NOT EXISTS (SELECT * FROM ptc_tramitacao_documento D " +
				      						  	"                  WHERE D.cd_documento = A.cd_documento " +
				                              	"                    AND D.cd_empresa_destino IN ("+matrizes+")) " +
				                               	"  AND A.cd_empresa_atual NOT IN ("+matrizes+")").executeQuery().next())	{
						rsmBloqueio.addRegister(rsm.getRegister());
					}
					else
						rsmRetorno.addRegister(rsm.getRegister());
				}
			}
			rsmRetorno.beforeFirst();
			rsmBloqueio.beforeFirst();
            return new ResultSetMap[] {rsmRetorno, rsmBloqueio};
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.getNotasOutOfFechamento: " +  e);
			return new ResultSetMap[] {null, null};
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static int insertContaAPagar(int cdFechamento, int cdSituacaoComissao, int cdEmpresa, int cdPessoa,
		float vlConta, float vlAbatimento, GregorianCalendar dtVencimento)
	{
		Connection connect = Conexao.conectar();
		try	{
			return insertContaAPagar(cdFechamento, cdSituacaoComissao, cdEmpresa, cdPessoa,
									 vlConta, vlAbatimento, dtVencimento, connect, null);
		}
		finally	{
			Conexao.desconectar(connect);
		}

	}

	public static int insertContaAPagar(int cdFechamento, int cdSituacaoComissao, int cdEmpresa, int cdPessoa,
		float vlConta, float vlAbatimento, GregorianCalendar dtVencimento, Connection connect, HashMap<Object,Object> estornos)
	{
		PreparedStatement pstmt;
		try {
			int cdComissaoFila = cdSituacaoComissao;
			if (ParametroServices.getValorOfParametro("CD_COMISSAO_FILA")!=null)
				cdComissaoFila = new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_FILA")).intValue();
			int cdContaBancaria = PessoaServices.getCdContaBancaria(cdPessoa);
			connect.setAutoCommit(false);
			GregorianCalendar dtEmissao = new GregorianCalendar();
			dtEmissao.set(Calendar.HOUR_OF_DAY, 0);
			dtEmissao.set(Calendar.MINUTE, 0);
			dtEmissao.set(Calendar.SECOND, 0);
			int cdEmpresaFechamento = 0;
			Fechamento fechamento = FechamentoDAO.get(cdFechamento);
			cdEmpresaFechamento = fechamento.getCdEmpresa();
			// Busca conta do mesmo fechamento ainda em aberto
			/*
			ResultSet rs = connect.prepareStatement("SELECT cd_conta_pagar FROM adm_conta_pagar "+
													"WHERE st_conta       = 0 "+
													"  AND cd_empresa     = "+cdEmpresa+
													"  AND cd_fechamento  = "+cdFechamento+
													"  AND cd_pessoa      = "+cdPessoa).executeQuery();

			int cdContaPagar = 0;
			if(rs.next())
				cdContaPagar = rs.getInt("cd_conta_pagar");
			else	{*/
			int cdTipoDocumento = 0;
			int cdContaPadrao   = 0;
			ContaPagar conta = new ContaPagar(0 /*cdContaPagar*/, 0 /*cdContrato*/, cdPessoa, cdEmpresa, 0 /*cdContaOrigem*/,
											  0 /*cdDocumentoEntrada*/, cdContaPadrao, cdContaBancaria, dtVencimento, dtEmissao,
											  null /*dtPagamento*/, null /*dtAutorizacao*/, null /*nrDocumento*/, null /*nrReferencia*/,
											  0 /*nrParcela*/, cdTipoDocumento, vlConta, 0 /*vlAbatimento*/, 0 /*vlAcrescimo*/,
											  0 /*vlPago*/, "" /*Histórico*/, com.tivic.manager.adm.ContaPagarServices.ST_EM_ABERTO,
											  0 /*lgAutorizado*/, 0 /*tpFrequencia*/, 0 /*qtParcelas*/, 0 /*vlBaseAutorizacao*/,
											  0 /*cdViagem*/, 0 /*cdManutencao*/, null /*txtObservacao*/,
											  new GregorianCalendar(), dtVencimento, 0/*cdTurno*/);
			int cdContaPagar = ContaPagarDAO.insert(conta, connect);
			//}
			if(cdContaPagar>0)	{
				ResultSetMap rsm = getComissoesOfFechamento(cdFechamento, cdEmpresa, 0/*cdVinculo*/, cdSituacaoComissao, false /*resumo*/, cdPessoa)[0];
				rsm.beforeFirst();
				// Somando conta
				vlConta = 0;
				while(rsm.next())	{
					if(rsm.getFloat("vl_pago")<=0)
						continue;
					pstmt = connect.prepareStatement(
								 "UPDATE sce_contrato_comissao "+
								 "SET cd_conta_pagar = "+cdContaPagar+", dt_previsao = ?, cd_situacao = "+cdComissaoFila+
								 " WHERE cd_nota_fiscal IS NULL "+
								 "  AND cd_conta_pagar IS NULL "+
								 "  AND cd_empresa     = "+cdEmpresa+
								 "  AND cd_pessoa      = "+cdPessoa+
								 "  AND cd_comissao    = "+rsm.getInt("cd_comissao")+
								 "  AND cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo "+
								 "                                 FROM sce_contrato_comissao X, adm_nota_fiscal Y "+
								 "                                 WHERE Y.cd_fechamento IN (SELECT W.cd_fechamento FROM sce_fechamento W " +
								 "                                                           WHERE W.cd_empresa = "+cdEmpresaFechamento+") "+
								 "                                   AND Y.cd_nota_fiscal = X.cd_nota_fiscal) ");
					pstmt.setTimestamp(1, new Timestamp(dtVencimento.getTimeInMillis()));
					pstmt.executeUpdate();
					vlConta += rsm.getFloat("vl_pago");
				}
				if(vlAbatimento > vlConta)
					vlAbatimento = vlConta;
				// Estorno
				for(int i=0; estornos!=null && i<estornos.size(); i++)	{
					Integer cdComissao = (Integer)estornos.keySet().toArray()[i];
					Float vlEstorno = (Float)estornos.values().toArray()[i];
					if(vlAbatimento >= Math.abs(vlEstorno.floatValue()))	{
						vlAbatimento -= Math.abs(vlEstorno.floatValue());
						pstmt = connect.prepareStatement("UPDATE sce_contrato_comissao "+
														 "SET cd_conta_pagar = "+cdContaPagar+", dt_previsao = ?, cd_situacao = "+cdComissaoFila+
														 " WHERE cd_comissao = "+cdComissao.intValue());
						pstmt.setTimestamp(1, new Timestamp(dtVencimento.getTimeInMillis()));
						pstmt.executeUpdate();
					}
				}
				// Lança abatimento
				if(vlAbatimento>0&&cdContaPagar>0)
					AdiantamentoServices.setPagamento(cdEmpresa, cdPessoa, vlAbatimento, cdContaPagar, connect);
				// Garante que o valor da conta nunca será diferente
				ContaPagarServices.atualizaValoresConta(cdContaPagar, connect);
				connect.commit();
			}
			else
				connect.rollback();
			if(cdContaBancaria<=0)
				return -10;
			else
				return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.insertContaAPagar: " +  e);
			return -1;
		}
	}

	public static ResultSetMap getContasOfFechamento(int cdFechamento) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_empresa, C.nm_pessoa, D.*, E.nr_banco, E.nm_banco "+
					"FROM adm_conta_pagar A "+
					"JOIN grl_empresa B ON (B.cd_empresa = A.cd_empresa) "+
					"JOIN grl_pessoa  C ON (C.cd_pessoa = A.cd_pessoa) "+
					"LEFT OUTER JOIN grl_pessoa_conta_bancaria D ON (D.cd_pessoa = A.cd_pessoa "+
					"                                            AND D.cd_conta_bancaria = A.cd_conta_bancaria) "+
					"LEFT OUTER JOIN grl_banco                 E ON (E.cd_banco = D.cd_banco) "+
			        "WHERE A.cd_fechamento = ?");
			pstmt.setInt(1, cdFechamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.getContasOfFechamento: " +  e);
			return null;
		}
	}

	public static int deleteContasOfFechamento(int cdFechamento)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
            pstmt = connect.prepareStatement(
       			 	 "SELECT cd_conta_pagar FROM adm_conta_pagar "+
					 "WHERE cd_fechamento = ? " +
					 "  AND st_conta = 0");
            pstmt.setInt(1, cdFechamento);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            	deleteContaAPagar(rs.getInt("cd_conta_pagar"));
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.deleteContasOfFechamento: " +  e);
			return -1;
		}
	}

	public static int deleteContaAPagar(int cdContaPagar)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
            pstmt = connect.prepareStatement(
       			 	 "SELECT * FROM adm_conta_pagar "+
					 "WHERE cd_conta_pagar = ? " +
					 "  AND st_conta = 1");
            pstmt.setInt(1, cdContaPagar);
            if(pstmt.executeQuery().next())
            	return -10;
			int cdSituacao = 0;
			if (ParametroServices.getValorOfParametro("CD_SITUACAO_CONFIRMADA")!=null)
				cdSituacao = new Integer(ParametroServices.getValorOfParametro("CD_SITUACAO_CONFIRMADA")).intValue();
			connect.setAutoCommit(false);
			// Apagando vinculo de comissoes com a conta
			pstmt = connect.prepareStatement(
					"UPDATE sce_contrato_comissao SET cd_conta_pagar = null, dt_pagamento = null, cd_situacao = ? "+
					"WHERE cd_conta_pagar = ? ");
			pstmt.setInt(1, cdSituacao);
			pstmt.setInt(2, cdContaPagar);
			pstmt.executeUpdate();
			// Apagando pagamento de adiantamentos
            pstmt = connect.prepareStatement(
            			 "UPDATE sce_adiantamento SET st_adiantamento = ? "+
						 "WHERE cd_adiantamento IN (SELECT cd_adiantamento FROM sce_adiantamento_pagamento " +
						 "                          WHERE cd_conta_pagar = ?)");
			pstmt.setInt(1, AdiantamentoServices.ST_PAGO);
			pstmt.setInt(2, cdContaPagar);
			pstmt.executeUpdate();
            pstmt = connect.prepareStatement("DELETE FROM sce_adiantamento_pagamento "+
				                             "WHERE cd_conta_pagar = ?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.executeUpdate();
			// Apagando conta a pagar
			ContaPagarDAO.delete(cdContaPagar, connect);
			connect.commit();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.deleteContaAPagar: " +  e);
			return -1;
		}
	}

	public static ResultSetMap gerarContasAPagar(int cdFechamento, int cdEmpresa, int cdVinculo, int cdSituacaoComissao,
				GregorianCalendar dtVencimento)
	{
		Connection connect = Conexao.conectar();
		ResultSetMap rsmLog = new ResultSetMap();
		try {
			// Preparando data de vencimento
			if(dtVencimento==null)	{
				dtVencimento = new GregorianCalendar();
				dtVencimento.set(Calendar.HOUR_OF_DAY, 0);
				dtVencimento.set(Calendar.MINUTE, 0);
				dtVencimento.set(Calendar.SECOND, 0);
				dtVencimento.add(Calendar.DAY_OF_MONTH, 1);
			}
			// Buscando todas as contas sumarizadas
			ResultSetMap rsmTotais = getComissoesOfFechamento(cdFechamento, cdEmpresa, cdVinculo, cdSituacaoComissao, true)[0];
			while(rsmTotais.next())	{
				if(rsmTotais.getFloat("vl_total")<=0 || rsmTotais.getFloat("vl_bloqueado")>0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("NM_COLABORADOR", rsmTotais.getString("NM_PESSOA"));
					if(rsmTotais.getFloat("vl_total")<=0 && rsmTotais.getFloat("vl_bloqueado")>0)
						register.put("NM_MENSAGEM", "Todas as comissões bloqueadas.");
					else if(rsmTotais.getFloat("vl_bloqueado")>0)
						register.put("NM_MENSAGEM", "Comissões bloqueadas.");
					else
						register.put("NM_MENSAGEM", "Valor da comissão zerado.");
					rsmLog.addRegister(register);
					if(rsmTotais.getFloat("vl_total")<=0)
						continue;
				}
				// Verificando adiantamentos
				ResultSetMap rsm = AdiantamentoServices.getAdiantamentoEmAberto(rsmTotais.getInt("cd_empresa"),
																				rsmTotais.getInt("cd_pessoa"), true);
				float vlAbatimento = 0;
				while(rsm.next())	{
					float vlSaldo   = rsm.getFloat("vl_adiantamento") - rsm.getFloat("vl_pago");
					float vlParcela = rsm.getFloat("vl_adiantamento") / rsm.getInt("qt_parcelas");
					vlParcela 		= vlParcela>vlSaldo?vlSaldo:vlParcela;
					vlAbatimento   += vlParcela;
				}
				// Verificando estornos
				HashMap<Object,Object> estornos = new HashMap<Object,Object>();
				if(vlAbatimento < rsmTotais.getFloat("vl_total"))	{
					rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM sce_contrato_comissao " +
							                                        "WHERE cd_pessoa = "+rsmTotais.getInt("cd_pessoa")+
							                                        "  AND cd_empresa = "+rsmTotais.getInt("cd_empresa")+
							                                        "  AND cd_conta_pagar IS NULL " +
							                                        "  AND vl_pago < 0").executeQuery());
					while(rsm.next())	{
						if((rsmTotais.getFloat("vl_total")-vlAbatimento) > Math.abs(rsm.getFloat("vl_pago")))	{
							vlAbatimento += Math.abs(rsm.getFloat("vl_pago"));
							estornos.put(rsm.getObject("cd_comissao"), new Float(rsm.getFloat("vl_pago")));
						}
					}
				}
				// Inserindo contas e pagamento de adiantamentos
				vlAbatimento = vlAbatimento<=rsmTotais.getFloat("vl_total")? vlAbatimento : rsmTotais.getFloat("vl_total");

				int cdRetorno = insertContaAPagar(cdFechamento, cdSituacaoComissao, rsmTotais.getInt("cd_empresa"), rsmTotais.getInt("cd_pessoa"),
								  				  rsmTotais.getFloat("vl_total"), vlAbatimento, dtVencimento, connect, estornos);
				if(cdRetorno < 0)	{
					HashMap<String,Object> register = new HashMap<String,Object>();
					register.put("NM_COLABORADOR", rsmTotais.getString("NM_PESSOA"));
					register.put("NM_MENSAGEM", cdRetorno==-10?"Colaborador sem conta bancária.":"Não foi possível gerar conta.");
					rsmLog.addRegister(register);
				}
			}
			return rsmLog;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FechamentoServices.gerarContasAPagar: " +  e);
			HashMap<String,Object> register = new HashMap<String,Object>();
			register.put("NM_MENSAGEM", "Erro desconhecido, entre em contato com o suporte.");
			rsmLog.addRegister(register);
			return rsmLog;
		}
	}
}