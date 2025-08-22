package com.tivic.manager.crt;

import java.sql.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.GregorianCalendar;

import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;


public class EmpresaServices {

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_cidade, B.sg_estado "+
		                   "FROM GRL_EMPRESA A "+
		                   "LEFT OUTER JOIN GRL_CIDADE B ON (A.CD_CIDADE = B.CD_CIDADE)",
		                   "ORDER BY nm_fantasia", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap findEmpresaPessoa(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * "+
		                   "FROM GRL_PESSOA_EMPRESA A, GRL_PESSOA B, GRL_VINCULO C, GRL_EMPRESA	D "+
		                   "WHERE A.CD_PESSOA  = B.CD_PESSOA "+
		                   "  AND A.CD_VINCULO = C.CD_VINCULO "+
		                   "  AND A.CD_EMPRESA = D.CD_EMPRESA",
		                   "ORDER BY nm_vinculo, nm_fantasia", criterios, Conexao.conectar(), true);
	}

	public static int getCdEmpresaMatriz(int cdEmpresa)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT cd_matriz FROM grl_empresa " +
					                         "WHERE cd_empresa = "+cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getCdEmpresaMatriz: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getCdEmpresaMatriz: " + e);
			return 0;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int insertVinculoWithEmpresa(int cdEmpresa, int cdPessoa, int cdVinculo, int cdFuncao,
			int cdTabelaComissao, int stVinculo) {
			return insertVinculoWithEmpresa(cdEmpresa, cdPessoa, cdVinculo, cdFuncao, cdTabelaComissao, stVinculo, 0);
	}
	public static int insertVinculoWithEmpresa(int cdEmpresa, int cdPessoa, int cdVinculo, int cdFuncao,
			int cdTabelaComissao, int stVinculo, int stContrato) {

		Connection connect = Conexao.conectar();
		try {
			return insertVinculoWithEmpresa(cdEmpresa, cdPessoa, cdVinculo, cdFuncao, cdTabelaComissao, stVinculo, stContrato, connect);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	public static int insertVinculoWithEmpresa(int cdEmpresa, int cdPessoa, int cdVinculo, int cdFuncao,
			int cdTabelaComissao, int stVinculo, int stContrato, Connection connect) {
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM GRL_PESSOA_EMPRESA "+
		                                     "WHERE CD_EMPRESA = ? "+
		                                     "  AND CD_PESSOA  = ? "+
		                                     "  AND CD_VINCULO = ? ");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdVinculo);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				pstmt = connect.prepareStatement("UPDATE GRL_PESSOA_EMPRESA SET CD_TABELA_COMISSAO=?, ST_VINCULO=?, ST_CONTRATO=? "+
		                                         "WHERE CD_EMPRESA = ? "+
		                                         "  AND CD_PESSOA  = ? "+
		                                         "  AND CD_VINCULO = ? ");
				if(cdTabelaComissao<=0)
					pstmt.setNull(1, Types.INTEGER);
				else
					pstmt.setInt(1, cdTabelaComissao);
				pstmt.setInt(2, stVinculo);
				pstmt.setInt(3, stContrato);
				pstmt.setInt(4, cdEmpresa);
				pstmt.setInt(5, cdPessoa);
				pstmt.setInt(6, cdVinculo);
				pstmt.executeUpdate();
			}
			else	{
				pstmt = connect.prepareStatement("INSERT INTO GRL_PESSOA_EMPRESA "+
			                                     "(CD_EMPRESA, CD_PESSOA, CD_VINCULO, CD_TABELA_COMISSAO, ST_VINCULO, ST_CONTRATO) "+
			                                     "VALUES (?,?,?,?,?,?)");
				pstmt.setInt(1, cdEmpresa);
				pstmt.setInt(2, cdPessoa);
				pstmt.setInt(3, cdVinculo);
				if(cdTabelaComissao<=0)
					pstmt.setNull(4, Types.INTEGER);
				else
					pstmt.setInt(4, cdTabelaComissao);
				pstmt.setInt(5, stVinculo);
				pstmt.setInt(6, stContrato);
				pstmt.executeUpdate();
			}
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.insertVinculoWithEmpresa: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.insertVinculoWithEmpresa: " + e);
			return -1;
		}
	}

	public static int deleteVinculoWithEmpresa(int cdEmpresa, int cdPessoa, int cdVinculo) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM GRL_PESSOA_EMPRESA "+
		                                     "WHERE CD_EMPRESA = ? "+
		                                     "  AND CD_PESSOA  = ? "+
		                                     "  AND CD_VINCULO = ?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPessoa);
			pstmt.setInt(3, cdVinculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.deleteVinculoWithEmpresa: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.deleteVinculoWithEmpresa: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAcompanhamento(int tpAcompanhamento, int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			ResultSetMap rsm = new ResultSetMap();
			PreparedStatement pstmtTotal;
			if(tpAcompanhamento==0||tpAcompanhamento==1)	{
				if(tpAcompanhamento==0)
					pstmt = connect.prepareStatement("SELECT A.cd_empresa, A.nm_empresa, A.nm_fantasia "+
				    	                             "FROM grl_empresa A "+
				        	                         (cdEmpresa>0?"WHERE A.cd_empresa = ? ":""));
				else	{
					pstmt = connect.prepareStatement("SELECT A.cd_empresa, A.nm_empresa, A.nm_fantasia, "+
					                                 "       B.cd_operacao, B.nm_operacao "+
					                                 "FROM grl_empresa A, sce_tipo_operacao B "+
					                                 "WHERE 1=1 "+
					                                 (cdEmpresa>0?" AND A.cd_empresa = ? ":""));
				}
				if(cdEmpresa>0)
					pstmt.setInt(1, cdEmpresa);
				rsm = new ResultSetMap(pstmt.executeQuery());
				if(tpAcompanhamento==1)	{
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					if(cdEmpresa>0)
						criterios.add(new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmEmpresa = EmpresaServices.find(criterios);
					while(rsmEmpresa.next())	{
						HashMap<String,Object> register = new HashMap<String,Object>();
						register.put("CD_EMPRESA",  new Integer(rsmEmpresa.getInt("cd_empresa")));
						register.put("NM_EMPRESA",  rsmEmpresa.getString("nm_empresa"));
						register.put("NM_FANTASIA", rsmEmpresa.getString("nm_fantasia"));
						register.put("CD_OPERACAO", new Integer(0));
						register.put("NM_OPERACAO", "Atendimento no Balcão");
						rsm.addRegister(register);
					}
					pstmt = connect.prepareStatement(
					  "SELECT DATEPART(MM, A.dt_contrato) AS NR_MES, "+
					  "       DATEPART(YYYY, A.dt_contrato) AS NR_ANO, "+
					  "       SUM(A.vl_financiado) AS VL_FINANCIADO "+
				      "FROM sce_contrato A, sce_situacao B "+
				      "WHERE A.cd_empresa = ? "+
				      "  AND A.cd_situacao = B.cd_situacao "+
				      "  AND B.lg_ignorar <> 1 "+
				      "  AND A.dt_contrato BETWEEN ? AND ? "+
				      "  AND A.cd_operacao IS NULL "+
				      "GROUP BY DATEPART(MM, A.dt_contrato), DATEPART(YYYY, A.dt_contrato)");
				}
				// Sql de totalização
				pstmtTotal = connect.prepareStatement(
					  "SELECT DATEPART(MM, A.dt_contrato) AS NR_MES, "+
					  "       DATEPART(YYYY, A.dt_contrato) AS NR_ANO, "+
					  "       SUM(A.vl_financiado) AS VL_FINANCIADO "+
				      "FROM sce_contrato A, sce_situacao B "+
				      "WHERE A.cd_empresa = ? "+
				      "  AND A.cd_situacao = B.cd_situacao "+
				      "  AND B.lg_ignorar <> 1 "+
				      "  AND A.dt_contrato BETWEEN ? AND ? "+
				      (tpAcompanhamento==1?" AND cd_operacao = ? ":"")+
				      "GROUP BY DATEPART(MM, A.dt_contrato), DATEPART(YYYY, A.dt_contrato)");
			}
			else	{
				pstmt = connect.prepareStatement("SELECT A.cd_pessoa, nm_pessoa, nm_empresa, nm_fantasia "+
				                                 "FROM grl_pessoa A, grl_pessoa_empresa B, grl_empresa C "+
				                                 "WHERE B.cd_vinculo = ? "+
				                                 "  AND A.cd_pessoa  = B.cd_pessoa "+
				                                 "  AND B.cd_empresa = C.cd_empresa "+
				                                 (cdEmpresa>0?" AND B.cd_empresa = ? ":""));
				pstmt.setInt(1, VinculoServices.CORRETOR);
				if(cdEmpresa>0)
					pstmt.setInt(2, cdEmpresa);
				rsm = new ResultSetMap(pstmt.executeQuery());
				// Sql de totalização
				pstmtTotal = connect.prepareStatement(
					  "SELECT DATEPART(MM, A.dt_contrato) AS NR_MES, "+
					  "       DATEPART(YYYY, A.dt_contrato) AS NR_ANO, "+
					  "       SUM(A.vl_financiado) AS VL_FINANCIADO "+
				      "FROM sce_contrato A, sce_situacao B "+
				      "WHERE A.cd_agente = ? "+
				      "  AND A.cd_situacao = B.cd_situacao "+
				      "  AND B.lg_ignorar <> 1 "+
				      "  AND A.dt_contrato BETWEEN ? AND ? "+
				      "GROUP BY DATEPART(MM, A.dt_contrato), DATEPART(YYYY, A.dt_contrato)");
			}
			while(rsm.next())	{
				pstmtTotal.setInt(1, (tpAcompanhamento==2)?rsm.getInt("cd_pessoa"):rsm.getInt("cd_empresa"));
				pstmtTotal.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
				pstmtTotal.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
				ResultSetMap rsmTotal = new ResultSetMap();
				if(tpAcompanhamento!=1||rsm.getInt("cd_operacao")>0)	{
					if(tpAcompanhamento==1)
						pstmtTotal.setInt(4, rsm.getInt("cd_operacao"));
					rsmTotal = new ResultSetMap(pstmtTotal.executeQuery());
				}
				else	{
					pstmt.setInt(1, rsm.getInt("cd_empresa"));
					pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
					pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
					rsmTotal = new ResultSetMap(pstmt.executeQuery());
				}
				float vlFinanciado = 0;
				while(rsmTotal.next())	{
					String nmCampo = rsmTotal.getString("NR_MES")+"_"+rsmTotal.getString("NR_ANO");
					rsm.setValueToField("VL_"+nmCampo, new Float(rsmTotal.getFloat("VL_FINANCIADO")));
					if(vlFinanciado>0)
						rsm.setValueToField("PR_"+nmCampo, new Integer(new Float((rsmTotal.getFloat("VL_FINANCIADO") - vlFinanciado)/vlFinanciado*100).intValue()));
					vlFinanciado = rsmTotal.getFloat("VL_FINANCIADO");
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getAcompanhamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getAcompanhamentoByEmpresa: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static String getEndereco(int cdEmpresa) {
		Connection connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			ResultSetMap rsm = find(criterios);
			if(!rsm.next())
				return null;
			else
				return com.tivic.manager.util.Util.formatEndereco("", rsm.getString("nm_rua"), rsm.getString("nr_endereco"),
										   rsm.getString("nm_complemento_endereco"),
										   rsm.getString("nm_bairro"), rsm.getString("nr_cep"),
										   rsm.getString("nm_cidade"), rsm.getString("sg_estado"),
										   null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getEndereco: " + e);
			return "";
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getRelatorioFechamento(int tpAgrupamento, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdEmpresa)	{
		if(tpAgrupamento==0)
			return getRelatorioFechamentoPorParceiro(dtInicial, dtFinal, cdEmpresa);
		else if(tpAgrupamento==1)
			return getRelatorioFechamentoPorEmpresa(dtInicial, dtFinal, cdEmpresa);
		else
			return getRelatorioFechamentoPorEmpresa2(dtInicial, dtFinal, cdEmpresa);
	}

	public static ResultSetMap getRelatorioFechamentoPorParceiro(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdEmpresa)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			ResultSetMap rsmReturn = new ResultSetMap();
			// Somando Valores por PARCEIRO e OPERAÇÃO
			pstmt = connect.prepareStatement(
					"SELECT E.cd_pessoa, E.nm_pessoa, D.cd_operacao, " +
					"       SUM(vl_liberado) as vl_producao, " +
					"       SUM(B.vl_pago) AS vl_total "+
					"FROM adm_nota_fiscal A  "+
					"JOIN sce_contrato_comissao B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) "+
					"JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo " +
					"                    "+(cdEmpresa>0?" AND C.cd_empresa = "+cdEmpresa:"")+") "+
					"LEFT OUTER JOIN sce_tipo_operacao D ON (C.cd_operacao = D.cd_operacao) "+
					"JOIN grl_pessoa E ON (A.cd_pessoa = E.cd_pessoa) "+
					"WHERE A.dt_nota_fiscal BETWEEN ? AND ? " +
					"  AND B.vl_pago > 0 "+
					"GROUP BY E.cd_pessoa, E.nm_pessoa, D.cd_operacao "+
					"ORDER BY E.cd_pessoa");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			// Reduz para sumário por parceiro, colocando as operações no mesmo registro
			int cdPessoa = 0;
			float vlTotal = 0, vlProducao = 0;
			HashMap<String,Object> register = new HashMap<String,Object>();
			HashMap<String,Object> totais = new HashMap<String,Object>();
			totais.put("NM_PARCEIRO", "Valor Bruto");
			float vlTotalGeral = 0, vlProducaoGeral = 0;
			while(rsm.next())	{
				if(cdPessoa!=rsm.getInt("cd_pessoa"))	{
					if(cdPessoa>0)
						rsmReturn.addRegister(register);
					cdPessoa = rsm.getInt("cd_pessoa");
					register = new HashMap<String,Object>();
					register.put("SINAL", "+");
					register.put("NM_PARCEIRO", rsm.getString("nm_pessoa"));
					vlTotalGeral    += vlTotal;
					vlProducaoGeral += vlProducao;
					vlTotal = 0;
					vlProducao = 0;
				}
				vlTotal += rsm.getFloat("vl_total");
				vlProducao += rsm.getFloat("vl_producao");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao").toUpperCase());
				register.put(nmOperacao, rsm.getObject("vl_total"));
				register.put(nmOperacao+"_", rsm.getObject("vl_producao"));
				register.put("VL_TOTAL", new Float(vlTotal));
				register.put("VL_TOTAL_", new Float(vlProducao));
				float vlSumComissao = totais.get(nmOperacao)==null?0:((Float)totais.get(nmOperacao)).floatValue();
				float vlSumProducao = totais.get(nmOperacao+"_")==null?0:((Float)totais.get(nmOperacao+"_")).floatValue();
				vlSumComissao += rsm.getFloat("vl_total");
				vlSumProducao += rsm.getFloat("vl_producao");
				totais.put(nmOperacao, new Float(vlSumComissao));
				totais.put(nmOperacao+"_", new Float(vlSumProducao));
			}
			if(cdPessoa>0)	{
				rsmReturn.addRegister(register);
				vlTotalGeral    += vlTotal;
				vlProducaoGeral += vlProducao;
			}
			// Total Bruto
			totais.put("VL_TOTAL", new Float(vlTotalGeral));
			totais.put("VL_TOTAL_", new Float(vlProducaoGeral));
			totais.put("SINAL", "=");
			register = new HashMap<String,Object>();
			register.put("NM_PARCEIRO","");
			register.put("SINAL","=");
			rsmReturn.addRegister(register);
			rsmReturn.addRegister(totais);
			// Somando Estornos
			pstmt = connect.prepareStatement(
					 "SELECT D.cd_operacao, D.nm_operacao, SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_nota_fiscal A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdEmpresa>0?" AND C.cd_empresa = "+cdEmpresa:"")+") "+
					 "LEFT OUTER JOIN sce_tipo_operacao D ON (C.cd_operacao = D.cd_operacao) "+
					 "WHERE A.dt_nota_fiscal BETWEEN ? AND ? " +
					 "  AND B.vl_pago <= 0 "+
					 "GROUP BY D.cd_operacao, D.nm_operacao ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			vlTotal = 0;
			register = new HashMap<String,Object>();
			register.put("NM_PARCEIRO", "Estornos - Parceiros (-)");
			while(rsm.next())	{
				vlTotal += rsm.getFloat("vl_total");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao"));
				register.put(nmOperacao, rsm.getObject("vl_total"));
				register.put("VL_TOTAL", new Float(vlTotal));
			}
			if(rsm.size()>0)	{
				rsmReturn.addRegister(new HashMap<String,Object>());
				rsmReturn.addRegister(register);
			}
			// Somando bloqueios
			pstmt = connect.prepareStatement(
					 "SELECT B.cd_operacao, C.nm_operacao, SUM(A.vl_pago) AS vl_total "+
					 "FROM sce_contrato_comissao A "+
					 "JOIN sce_contrato B ON (A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
					 "                    "+(cdEmpresa>0?" AND A.cd_empresa = "+cdEmpresa:"")+") "+
					 "LEFT OUTER JOIN sce_tipo_operacao C ON (B.cd_operacao = C.cd_operacao) "+
					 "WHERE A.cd_nota_fiscal IS NULL "+
					 "  AND A.cd_conta_pagar IS NULL " +
					 "  AND A.vl_pago > 0 "+
					 "  AND A.cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo "+
					 "                                   FROM sce_contrato_comissao X " +
					 "                                   JOIN adm_nota_fiscal Y ON (Y.cd_nota_fiscal = X.cd_nota_fiscal " +
					 "                                                          AND Y.dt_nota_fiscal BETWEEN ? AND ?)) "+
					 "GROUP BY B.cd_operacao, C.nm_operacao");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			vlTotal = 0;
			register = new HashMap<String,Object>();
			register.put("SINAL","-");
			register.put("NM_PARCEIRO", "Pagamentos bloqueados");
			while(rsm.next())	{
				vlTotal += rsm.getFloat("vl_total");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao"));
				register.put(nmOperacao, rsm.getObject("vl_total"));
			}
			register.put("VL_TOTAL", new Float(vlTotal));
			rsmReturn.addRegister(register);
			// Somando Pagamentos de Agentes
			pstmt = connect.prepareStatement(
					 "SELECT D.cd_operacao, D.nm_operacao, SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_conta_pagar A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdEmpresa>0?" AND C.cd_empresa = "+cdEmpresa:"")+") "+
					 "LEFT OUTER JOIN sce_tipo_operacao D ON (C.cd_operacao = D.cd_operacao) "+
					 "WHERE A.st_conta = 1 " +
					 "  AND A.dt_pagamento BETWEEN ? AND ? " +
					 "  AND B.vl_pago > 0 "+
					 "GROUP BY D.cd_operacao, D.nm_operacao ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			vlTotal = 0;
			register = new HashMap<String,Object>();
			register.put("SINAL","-");
			register.put("NM_PARCEIRO", "Pagamentos a Agentes");
			while(rsm.next())	{
				vlTotal += rsm.getFloat("vl_total");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao"));
				register.put(nmOperacao, rsm.getObject("vl_total"));
			}
			register.put("VL_TOTAL", new Float(vlTotal));
			rsmReturn.addRegister(register);
			// Somando estornos dos Agentes
			pstmt = connect.prepareStatement(
					 "SELECT D.cd_operacao, D.nm_operacao, SUM(B.vl_pago * -1) AS vl_total "+
					 "FROM adm_conta_pagar A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdEmpresa>0?" AND C.cd_empresa = "+cdEmpresa:"")+") "+
					 "LEFT OUTER JOIN sce_tipo_operacao D ON (C.cd_operacao = D.cd_operacao) "+
					 "WHERE A.st_conta = 1 " +
					 "  AND A.dt_pagamento BETWEEN ? AND ? " +
					 "  AND B.vl_pago < 0 "+
					 "GROUP BY D.cd_operacao, D.nm_operacao ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			vlTotal = 0;
			register = new HashMap<String,Object>();
			register.put("SINAL","+");
			register.put("NM_PARCEIRO", "Estornos - Agentes (+)");
			while(rsm.next())	{
				vlTotal += rsm.getFloat("vl_total");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao"));
				register.put(nmOperacao, rsm.getObject("vl_total"));
			}
			register.put("VL_TOTAL", new Float(vlTotal));
			if(rsm.size()>0)
				rsmReturn.addRegister(register);

			return rsmReturn;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoPorParceiro: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoPorParceiro: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getRelatorioFechamentoPorEmpresa(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdFilial)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			ResultSetMap rsmReturn = new ResultSetMap();
			// Somando Valores por EMPRESA
			pstmt = connect.prepareStatement(
					 "SELECT E.cd_empresa, E.nm_empresa, D.cd_operacao, SUM(C.vl_liberado) AS vl_producao, SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_nota_fiscal A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo  "+
					 "                    "+(cdFilial>0?" AND C.cd_empresa = "+cdFilial:"")+") "+
					 "LEFT OUTER JOIN sce_tipo_operacao D ON (C.cd_operacao = D.cd_operacao) "+
					 "JOIN grl_empresa E ON (C.cd_empresa = E.cd_empresa) "+
					 "WHERE A.dt_nota_fiscal BETWEEN ? AND ? " +
					 //"  AND B.vl_pago > 0 "+
					 "GROUP BY E.cd_empresa, E.nm_empresa, D.cd_operacao "+
					 "ORDER BY E.cd_empresa");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int cdEmpresa = 0;
			float vlTotal = 0, vlProducao = 0;
			HashMap<String,Object> register = new HashMap<String,Object>();
			float vlTotalGeral = 0;
			while(rsm.next())	{
				if(cdEmpresa!=rsm.getInt("cd_empresa"))	{
					if(cdEmpresa>0)
						rsmReturn.addRegister(register);
					cdEmpresa = rsm.getInt("cd_empresa");
					register = new HashMap<String,Object>();
					register.put("SINAL", "+");
					register.put("CD_EMPRESA", rsm.getObject("cd_empresa"));
					register.put("NM_EMPRESA", rsm.getString("nm_empresa"));
					register.put("NM_LINHA", "Valor Bruto");
					vlTotalGeral += vlTotal;
					vlTotal = 0;
					vlProducao = 0;
				}
				vlTotal += rsm.getFloat("vl_total");
				vlProducao += rsm.getFloat("vl_producao");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao"));
				register.put(nmOperacao+"_", rsm.getObject("vl_producao"));
				register.put(nmOperacao, rsm.getObject("vl_total"));
				register.put("VL_TOTAL", new Float(vlTotal));
				register.put("VL_TOTAL_", new Float(vlProducao));
			}
			if(cdEmpresa>0)	{
				vlTotalGeral += vlTotal;
				rsmReturn.addRegister(register);
			}
			System.out.println("vlTotalGeral = "+vlTotalGeral);
			// Somando Estornos por Empresa
			pstmt = connect.prepareStatement(
					 "SELECT C.cd_empresa, SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_nota_fiscal A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdFilial>0?" AND C.cd_empresa = "+cdFilial:"")+") "+
					 "WHERE A.dt_nota_fiscal BETWEEN ? AND ? " +
					 "  AND B.vl_pago <= 0 "+
					 "GROUP BY C.cd_empresa ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				if(rsmReturn.locate("cd_empresa", rsm.getObject("cd_empresa")))
					rsmReturn.setValueToField("VL_ESTORNO_EMPRESA", rsm.getObject("vl_total"));
			}
			// Somando Pagamentos de Agentes
			pstmt = connect.prepareStatement(
					 "SELECT A.cd_empresa, D.nm_empresa, C.cd_operacao, SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_conta_pagar A "+
					 "JOIN sce_contrato_comissao B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdFilial>0?" AND C.cd_empresa = "+cdFilial:"")+") "+
					 "JOIN grl_empresa D ON (A.cd_empresa = D.cd_empresa) "+
					 "WHERE A.st_conta = 1 " +
					 "  AND A.dt_pagamento BETWEEN ? AND ? " +
					 //"  AND B.vl_pago > 0 "+
					 "GROUP BY A.cd_empresa, D.nm_empresa, C.cd_operacao "+
					 "ORDER BY A.cd_empresa ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			cdEmpresa = 0;
			vlTotal = 0;
			register = new HashMap<String,Object>();
			vlTotalGeral = 0;
			while(rsm.next())	{
				if(cdEmpresa!=rsm.getInt("cd_empresa"))	{
					if(cdEmpresa>0)
						rsmReturn.addRegister(register);
					cdEmpresa = rsm.getInt("cd_empresa");
					register = new HashMap<String,Object>();
					register.put("SINAL", "-");
					register.put("CD_EMPRESA", rsm.getObject("cd_empresa"));
					register.put("NM_EMPRESA", rsm.getString("nm_empresa"));
					register.put("NM_LINHA", "Pagamentos(-)");
					vlTotalGeral += vlTotal;
					vlTotal = 0;
				}
				vlTotal += rsm.getFloat("vl_total");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao"));
				register.put(nmOperacao, rsm.getObject("vl_total"));
				register.put("VL_TOTAL", new Float(vlTotal));
			}
			if(cdEmpresa>0)
				rsmReturn.addRegister(register);
			// Somando estornos dos Agentes
			pstmt = connect.prepareStatement(
					 "SELECT A.cd_empresa, SUM(B.vl_pago * -1) AS vl_total "+
					 "FROM adm_conta_pagar A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdFilial>0?" AND C.cd_empresa = "+cdFilial:"")+") "+
					 "WHERE A.st_conta = 1 " +
					 "  AND A.dt_pagamento BETWEEN ? AND ? " +
					 "  AND B.vl_pago < 0 "+
					 "GROUP BY A.cd_empresa ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())
				if(rsmReturn.locate("cd_empresa", rsm.getObject("cd_empresa")))
					rsmReturn.setValueToField("VL_ESTORNO_AGENTE", rsm.getObject("vl_total"));
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("CD_EMPRESA");
			rsmReturn.orderBy(orderBy);
			// Apurando Valor Líquido
			float vlBalcao = 0;
			cdEmpresa = 0;
			ResultSetMap rsmOperacao = TipoOperacaoDAO.getAll();
			rsm = new ResultSetMap();
			rsmReturn.beforeFirst();
			while(rsmReturn.next()){
				if(cdEmpresa!=rsmReturn.getInt("cd_empresa"))	{
					if(cdEmpresa>0)	{
						rsm.addRegister(register);
						/*register = new HashMap();
						register.put("SINAL", "+");
						register.put("CD_EMPRESA", rsmReturn.getObject("cd_empresa"));
						register.put("NM_EMPRESA", rsmReturn.getString("nm_empresa"));
						register.put("NM_LINHA", "");
						rsm.addRegister(register);*/
					}
					cdEmpresa = rsmReturn.getInt("cd_empresa");
					vlTotal   = rsmReturn.getFloat("VL_TOTAL");
					vlBalcao  = rsmReturn.getFloat("VL_BALCAO");
					register = new HashMap<String,Object>();
					register.put("SINAL", "=");
					register.put("CD_EMPRESA", rsmReturn.getObject("cd_empresa"));
					register.put("NM_EMPRESA", rsmReturn.getString("nm_empresa"));
					register.put("NM_LINHA", "Valor Líquido(=)");
					register.put("VL_TOTAL", new Float(vlTotal));
					register.put("VL_BALCAO", new Float(vlBalcao));
					rsmOperacao.beforeFirst();
					while(rsmOperacao.next())	{
						String nmCampo = "VL_"+rsmOperacao.getString("cd_operacao");
						register.put(nmCampo, new Float(rsmReturn.getFloat(nmCampo)));
					}
				}
				else	{
					rsmOperacao.beforeFirst();
					while(rsmOperacao.next())	{
						Float vlPorOperacao = (Float)register.get("VL_"+rsmOperacao.getString("cd_operacao"));
						if(vlPorOperacao==null)
							vlPorOperacao = new Float(0);
						String nmCampo = "VL_"+rsmOperacao.getString("cd_operacao");
						register.put(nmCampo, new Float(vlPorOperacao.floatValue() - rsmReturn.getFloat(nmCampo)));
					}
					Float vlPorOperacao = (Float)register.get("VL_TOTAL");
					if(vlPorOperacao==null)
						vlPorOperacao = new Float(0);
					register.put("VL_TOTAL", new Float(vlPorOperacao.floatValue() - rsmReturn.getFloat("vl_total")));
				}
				rsm.addRegister(rsmReturn.getRegister());
			}
			if(cdEmpresa>0)	{
				rsm.addRegister(register);
				register = new HashMap<String,Object>();
				register.put("SINAL", "+");
				register.put("CD_EMPRESA", rsmReturn.getObject("cd_empresa"));
				register.put("NM_EMPRESA", rsmReturn.getObject("nm_empresa"));
				rsm.addRegister(register);
			}
			rsm.orderBy(orderBy);
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoByEmpresa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoByEmpresa: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getRelatorioFechamentoPorEmpresa2(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdFilial)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			ResultSetMap rsmReturn = new ResultSetMap();
			// Somando Valores por EMPRESA
			pstmt = connect.prepareStatement(
					 "SELECT E.cd_empresa, E.nm_empresa, D.cd_operacao, SUM(C.vl_liberado) AS vl_producao, SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_nota_fiscal A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo) "+
					 "LEFT OUTER JOIN sce_tipo_operacao D ON (C.cd_operacao = D.cd_operacao) "+
					 "JOIN grl_empresa E ON (C.cd_empresa = E.cd_empresa) "+
					 "WHERE A.dt_nota_fiscal BETWEEN ? AND ? " +
					 //"  AND B.vl_pago > 0 "+
					 "GROUP BY E.cd_empresa, E.nm_empresa, D.cd_operacao "+
					 "ORDER BY E.cd_empresa");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//
			// Somando Pagamentos de Agentes
			pstmt = connect.prepareStatement(
					 "SELECT SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_conta_pagar A "+
					 "JOIN sce_contrato_comissao B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo) "+
					 "JOIN grl_empresa D ON (A.cd_empresa = D.cd_empresa) "+
					 "WHERE A.st_conta = 1 " +
					 "  AND A.dt_pagamento BETWEEN ? AND ? " +
					 //"  AND B.vl_pago > 0 "+
					 "  AND C.cd_operacao = ? " +
					 "  AND C.cd_empresa  = ? ");
			//
			int cdEmpresa = 0;
			float vlTotal = 0, vlProducao = 0;
			HashMap<String,Object> register = new HashMap<String,Object>();
			float vlTotalGeral = 0;
			while(rsm.next())	{
				if(cdEmpresa!=rsm.getInt("cd_empresa"))	{
					if(cdEmpresa>0)
						rsmReturn.addRegister(register);
					cdEmpresa = rsm.getInt("cd_empresa");
					register = new HashMap<String,Object>();
					register.put("CD_EMPRESA", rsm.getObject("cd_empresa"));
					register.put("NM_EMPRESA", rsm.getString("nm_empresa"));
					register.put("NM_LINHA", "Valor Bruto");
					vlTotalGeral += vlTotal;
					vlTotal = 0;
					vlProducao = 0;
				}
				vlTotal += rsm.getFloat("vl_total");
				vlProducao += rsm.getFloat("vl_producao");
				String nmOperacao = "VL_"+(rsm.getInt("cd_operacao")==0?"BALCAO":rsm.getString("cd_operacao"));
				register.put(nmOperacao+"_", rsm.getObject("vl_producao"));
				register.put(nmOperacao, rsm.getObject("vl_total"));
				// Somando pagamentos por Operação
				pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				pstmt.setInt(3, rsm.getInt("cd_operacao"));
				pstmt.setInt(4, rsm.getInt("cd_empresa"));
				ResultSet rsPag = pstmt.executeQuery();
				float vlPagamento = 0;
				if(rsPag.next())
					vlPagamento = rsPag.getFloat(1);
				register.put(nmOperacao+"_P", new Float(vlPagamento));
				register.put(nmOperacao+"_S", new Float(rsm.getFloat("vl_total")-vlPagamento));
				register.put("VL_TOTAL", new Float(vlTotal));
				register.put("VL_TOTAL_", new Float(vlProducao));
			}
			if(cdEmpresa>0)	{
				vlTotalGeral += vlTotal;
				rsmReturn.addRegister(register);
			}
			System.out.println("vlTotalGeral = "+vlTotalGeral);
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("CD_EMPRESA");
			rsmReturn.orderBy(orderBy);
			return rsmReturn;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoByEmpresa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoByEmpresa: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getRelatorioFechamentoPorOperacao(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdEmpresa)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Somando Valores por EMPRESA
			pstmt = connect.prepareStatement(
					 "SELECT D.cd_operacao, D.nm_operacao, SUM(C.vl_liberado) AS vl_producao, SUM(B.vl_pago) AS vl_comissao "+
					 "FROM adm_nota_fiscal A  "+
					 "JOIN sce_contrato_comissao B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdEmpresa>0?" AND C.cd_empresa = "+cdEmpresa:"")+") "+
				     "LEFT OUTER JOIN sce_tipo_operacao D ON (C.cd_operacao = D.cd_operacao) "+
					 "WHERE A.dt_nota_fiscal BETWEEN ? AND ? " +
					 "  AND B.vl_pago > 0 "+
					 "GROUP BY D.cd_operacao, D.nm_operacao "+
					 "ORDER BY D.nm_operacao");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			// Somando bloqueios
			rsm.beforeFirst();
			while(rsm.next())	{
				pstmt = connect.prepareStatement(
						 "SELECT SUM(A.vl_pago) AS vl_total "+
						 "FROM sce_contrato_comissao A "+
						 "JOIN sce_contrato B ON (A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
						 "                    "+(cdEmpresa>0?" AND A.cd_empresa = "+cdEmpresa:"")+") "+
						 "LEFT OUTER JOIN sce_tipo_operacao C ON (B.cd_operacao = C.cd_operacao) "+
						 "WHERE A.cd_nota_fiscal IS NULL "+
						 "  AND A.cd_conta_pagar IS NULL " +
						 "  AND A.vl_pago > 0 " +
						 "  AND A.cd_contrato_emprestimo IN (SELECT cd_contrato_emprestimo "+
						 "                                   FROM sce_contrato_comissao X " +
						 "                                   JOIN adm_nota_fiscal Y ON (Y.cd_nota_fiscal = X.cd_nota_fiscal " +
						 "                                                          AND Y.dt_nota_fiscal BETWEEN ? AND ?)) "+
				 		 "  AND B.cd_operacao "+(rsm.getInt("cd_operacao")>0?" = ? ":"IS NULL"));
				// Somando pagamentos por Operação
				pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				if(rsm.getInt("cd_operacao")>0)
					pstmt.setInt(3, rsm.getInt("cd_operacao"));
				ResultSet rsBloq = pstmt.executeQuery();
				float vlBloqueado = 0;
				if(rsBloq.next())
					vlBloqueado = rsBloq.getFloat(1);
				rsm.setValueToField("vl_bloqueado", new Float(vlBloqueado));
			}
			// Somando Pagamentos de Agentes
			pstmt = connect.prepareStatement(
					 "SELECT SUM(B.vl_pago) AS vl_total "+
					 "FROM adm_conta_pagar A "+
					 "JOIN sce_contrato_comissao B ON (A.cd_conta_pagar = B.cd_conta_pagar) "+
					 "JOIN sce_contrato C ON (B.cd_contrato_emprestimo = C.cd_contrato_emprestimo "+
					 "                    "+(cdEmpresa>0?" AND C.cd_empresa = "+cdEmpresa:"")+") "+
					 "JOIN grl_empresa D ON (A.cd_empresa = D.cd_empresa) "+
					 "WHERE A.st_conta = 1 " +
					 "  AND A.dt_pagamento BETWEEN ? AND ? " +
					 "  AND C.cd_operacao = ? ");
			//
			rsm.beforeFirst();
			while(rsm.next())	{
				// Somando pagamentos por Operação
				pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				pstmt.setInt(3, rsm.getInt("cd_operacao"));
				ResultSet rsPag = pstmt.executeQuery();
				float vlPagamento = 0;
				if(rsPag.next())
					vlPagamento = rsPag.getFloat(1);
				rsm.setValueToField("VL_PAGAMENTO", new Float(vlPagamento));
				rsm.setValueToField("VL_SALDO", new Float(rsm.getFloat("vl_comissao")-rsm.getFloat("vl_bloqueado")-vlPagamento));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoByOperacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getRelatorioFechamentoByOperacao: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllMatriz() {
		return getAllMatriz(null);
	}

	public static ResultSetMap getAllMatriz(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM GRL_EMPRESA WHERE lg_matriz = 1");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getAllMatriz: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaServices.getAllMatriz: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}