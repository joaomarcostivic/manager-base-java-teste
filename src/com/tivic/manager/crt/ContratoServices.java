package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;

import com.tivic.manager.adm.*;
import com.tivic.manager.conexao.*;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.*;
import com.tivic.sol.connection.Conexao;


public class ContratoServices {
	public static final int PRODUTO_SEM_CONFIGURACAO = -10;
	public static final int DUPLICADO = -20;
	public static final int DUPLICADO_COM_ESPONTANEO = -30;
	public static final int AGENTE_OBRIGATORIO = -40;
	public static final int OPERACAO_OBRIGATORIA = -50;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_produto, B.cd_instituicao_financeira, "+
						   "       D.nm_situacao, E.nm_pessoa AS nm_cliente, "+
						   "       E.nm_pessoa AS nm_contratante, E.nr_cpf_cnpj, G.nm_operacao, I.nm_empresa "+
		                   "FROM sce_contrato A "+
						   "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
						   "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
						   "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
						   "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
						   "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
						   "JOIN grl_empresa I ON (A.cd_empresa = I.cd_empresa)", criterios, Conexao.conectar());
	}
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_produto, B.cd_instituicao_financeira, B.cd_instituicao_financeira AS cd_parceiro, "+
				           "       C.qt_parcelas AS qt_parcelas_plano, C.vl_tac AS vl_tac_plano, C.pr_juros AS pr_juros_plano, "+
				           "       D.nm_situacao, F.nm_pessoa AS nm_parceiro, G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa, " +
				           "       E.nm_pessoa AS nm_contratante, E.nm_pessoa AS nm_cliente, "+
				           "       E.nr_cpf_cnpj, E.nr_telefone, E.nr_telefone2, E.nr_celular, "+
				           "       E.nm_bairro, E.nm_rua, E.cd_cidade_endereco, E.nr_endereco, E.nm_complemento_endereco, "+
				           "       E.nr_cep, E.dt_nascimento, E.nr_rg, E.nm_nacionalidade, E.nm_pai, E.nm_mae, E.st_estado_civil, "+
				           "       E.tp_sexo, E.nm_email, E.sg_orgao_rg, E.sg_uf_rg, E.st_cadastro, "+
						   "       J.nm_cidade AS nm_cidade_endereco, L.nm_pessoa AS nm_atendente, M.nm_orgao," +
						   "       N.dt_documento, N.st_documento, O.nm_pessoa AS nm_pessoa_atual "+
		                   "FROM sce_contrato A "+
						   "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
						   "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
						   "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
						   "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
						   "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
						   "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
						   "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
						   "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
						   "LEFT OUTER JOIN grl_cidade J ON (E.cd_cidade_endereco = J.cd_cidade)"+
						   "LEFT OUTER JOIN grl_pessoa L ON (A.cd_atendente = L.cd_pessoa) "+
						   "LEFT OUTER JOIN sce_orgao  M ON (A.cd_orgao = M.cd_orgao) " +
						   "LEFT OUTER JOIN ptc_documento N ON (A.cd_documento = N.cd_documento)" +
						   "LEFT OUTER JOIN grl_pessoa O ON (N.cd_pessoa_atual = O.cd_pessoa)",
						   criterios, Conexao.conectar());
	}

	public static ResultSetMap findContratoAndComissao(ArrayList<ItemComparator> criterios) {
		String sql =
			   "SELECT A.*, B.nm_produto, B.cd_instituicao_financeira, B.cd_instituicao_financeira AS cd_parceiro, "+
	           "       D.nm_situacao, "+
	           "       E.nm_pessoa AS nm_contratante, E.nm_pessoa AS nm_cliente, "+
	           "       E.nr_cpf_cnpj, E.nr_telefone, E.nr_telefone2, E.nr_celular, "+
	           "       E.nm_bairro, E.nm_rua, E.cd_cidade_endereco, E.nr_endereco, E.nm_complemento_endereco, "+
	           "       E.nr_cep, E.dt_nascimento, E.nr_rg, E.nm_nacionalidade, E.nm_pai, E.nm_mae, E.st_estado_civil, "+
	           "       E.tp_sexo, E.nm_email, E.sg_orgao_rg, E.sg_uf_rg, E.st_cadastro, "+
			   "       F.nm_pessoa AS nm_parceiro, "+
			   "       G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa, "+
			   "       J.nm_cidade AS nm_cidade_endereco, L.nm_pessoa AS nm_atendente, M.nm_orgao, "+
			   " 	   A1.cd_comissao, A1.vl_comissao, A1.vl_pago, N.nm_situacao AS nm_situacao_comissao, O.nm_motivo, "+
			   "       P.dt_pagamento AS dt_pagamento_conta "+
			   "FROM sce_contrato A "+
			   "JOIN sce_contrato_comissao A1 ON (A1.cd_contrato_emprestimo = A.cd_contrato_emprestimo) "+
			   "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
			   "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
			   "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
			   "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
			   "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
			   "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
			   "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
			   "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
			   "LEFT OUTER JOIN grl_cidade J ON (E.cd_cidade_endereco = J.cd_cidade)"+
			   "LEFT OUTER JOIN grl_pessoa L ON (A.cd_atendente = L.cd_pessoa) "+
			   "LEFT OUTER JOIN sce_orgao  M ON (A.cd_orgao = M.cd_orgao) "+
		   	   "LEFT OUTER JOIN sce_situacao N ON (A1.cd_situacao = N.cd_situacao) "+
		   	   "LEFT OUTER JOIN sce_motivo O ON (A.cd_motivo = O.cd_motivo) "+
		   	   "LEFT OUTER JOIN adm_conta_pagar P ON (A1.cd_conta_pagar = P.cd_conta_pagar) ";
		return Search.find(sql, criterios, Conexao.conectar());
	}

	public static ResultSetMap findContratoAndComissao(String nrCpfCnpj, String nmPessoa, int cdNotaFiscal) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try	{
			nrCpfCnpj = nrCpfCnpj==null ? "" : nrCpfCnpj.trim();
			nmPessoa = nmPessoa==null ? "" : nmPessoa.trim();
			String sql =
			   "SELECT A.*, B.nm_produto, B.cd_instituicao_financeira, B.cd_instituicao_financeira AS cd_parceiro, "+
	           "       D.nm_situacao, "+
	           "       E.nm_pessoa AS nm_contratante, E.nm_pessoa AS nm_cliente, "+
	           "       E.nr_cpf_cnpj, E.nr_telefone, E.nr_telefone2, E.nr_celular, "+
	           "       E.nm_bairro, E.nm_rua, E.cd_cidade_endereco, E.nr_endereco, E.nm_complemento_endereco, "+
	           "       E.nr_cep, E.dt_nascimento, E.nr_rg, E.nm_nacionalidade, E.nm_pai, E.nm_mae, E.st_estado_civil, "+
	           "       E.tp_sexo, E.nm_email, E.sg_orgao_rg, E.sg_uf_rg, E.st_cadastro, "+
			   "       F.nm_pessoa AS nm_parceiro, A1.cd_comissao, "+
			   "       G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa, "+
			   "       J.nm_cidade AS nm_cidade_endereco, L.nm_pessoa AS nm_atendente, M.nm_orgao "+
			   "FROM sce_contrato A "+
			   "JOIN sce_contrato_comissao A1 ON (A1.cd_contrato_emprestimo = A.cd_contrato_emprestimo) "+
			   "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
			   "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
			   "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
			   "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
			   "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
			   "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
			   "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
			   "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
			   "LEFT OUTER JOIN grl_cidade J ON (E.cd_cidade_endereco = J.cd_cidade)"+
			   "LEFT OUTER JOIN grl_pessoa L ON (A.cd_atendente = L.cd_pessoa) "+
			   "LEFT OUTER JOIN sce_orgao  M ON (A.cd_orgao = M.cd_orgao) "+
			   "WHERE A1.cd_pessoa IS NULL "+
			   "  AND (A1.cd_nota_fiscal IS NULL OR A1.cd_nota_fiscal = ?) "+
			   (!nrCpfCnpj.equals("")? " AND E.nr_cpf_cnpj = ? " : "")+
			   (!nmPessoa.equals("") ? " AND E.nm_pessoa LIKE ? ": "");
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdNotaFiscal);
			if(!nrCpfCnpj.equals(""))
				pstmt.setString(2, nrCpfCnpj);
			if(!nmPessoa.equals(""))
				pstmt.setString(nrCpfCnpj.equals("")? 2 : 3, nmPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findCompletoAndComissao: " +  e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findProtocolo(ArrayList<ItemComparator> criterios) {
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getTypeComparation()==ItemComparator.MINOR_EQUAL)
				criterios.get(i).setValue(criterios.get(i).getValue()+" 23:59");
			if(criterios.get(i).getTypeComparation()==ItemComparator.GREATER_EQUAL)
				criterios.get(i).setValue(criterios.get(i).getValue()+" 00:00");
			criterios.set(i, criterios.get(i));
		}
		return Search.find("SELECT A.*, B.nm_produto, B.cd_instituicao_financeira, "+
		                   "       D.nm_situacao, F.nm_pessoa AS nm_parceiro, "+
						   "       G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa, "+
				           "       E.nm_pessoa AS nm_contratante, E.nm_pessoa AS nm_cliente, "+
				           "       E.nr_cpf_cnpj, E.nr_telefone, E.nr_telefone2, E.nr_celular, "+
				           "       E.nm_bairro, E.nm_rua, E.cd_cidade_endereco, E.nr_endereco, E.nm_complemento_endereco, "+
				           "       E.nr_cep, E.dt_nascimento, E.nr_rg, E.nm_nacionalidade, E.nm_pai, E.nm_mae, E.st_estado_civil, "+
				           "       E.tp_sexo, E.nm_email, E.sg_orgao_rg, E.sg_uf_rg, E.st_cadastro, "+
						   "       J.nm_cidade AS nm_cidade_endereco "+
		                   "FROM sce_contrato A "+
						   "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
						   "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
						   "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
						   "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
						   "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
						   "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
						   "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
						   "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
						   "LEFT OUTER JOIN grl_cidade J ON (E.cd_cidade_endereco = J.cd_cidade)", criterios, Conexao.conectar());
	}

	public static ResultSetMap findProducaoAtendente(ArrayList<sol.dao.ItemComparator> criterios, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdEmpresa) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT A.dt_contrato, A.vl_liberado, A.qt_parcelas, A.cd_empresa, " +
					     "  B.nm_produto, B.cd_instituicao_financeira, "+
				         "  D.nm_situacao, F.nm_pessoa AS nm_parceiro, "+
				         "  E.nm_pessoa AS nm_contratante, E.nm_pessoa AS nm_cliente, "+
				         "  E.nr_cpf_cnpj, G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa, "+
					     "  L.nm_pessoa AS nm_atendente, M.nr_conta, M.nr_dv, M.nr_agencia, "+
					     "  M.tp_operacao, M.nr_cpf_cnpj AS nr_cpf_cnpj_titular, M.nm_titular, " +
					     "  N.nr_banco, N.nm_banco "+
				         "FROM sce_contrato A "+
						 "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
						 "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
						 "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
						 "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
						 "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
						 "JOIN grl_pessoa        L ON (A.cd_atendente = L.cd_pessoa)"+
			             "LEFT OUTER JOIN grl_pessoa_conta_bancaria M ON (A.cd_atendente = M.cd_pessoa "+
			             "                                            AND M.st_conta = 1) "+
			             "LEFT OUTER JOIN grl_banco         N ON (M.cd_banco = N.cd_banco) "+
						 "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
						 "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
						 "WHERE (A.cd_operacao IS NULL OR G.lg_comissao_atendente = 1) " +
						 "  AND A.lg_expontaneo = 0 " +
						 "  AND A.cd_contrato_emprestimo IN " +
						 "   		   (SELECT cd_contrato_emprestimo " +
						 "              FROM sce_contrato_comissao X, adm_nota_fiscal Y " +
						 "              WHERE X.cd_nota_fiscal = Y.cd_nota_fiscal " +
						 "                AND Y.dt_nota_fiscal BETWEEN ? AND ?) " +
						 " ORDER BY I.nm_empresa, L.nm_pessoa";
			pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ResultSetMap rsmReturn = new ResultSetMap();
			while(cdEmpresa>0 && rsm.next())	{
				if(rsm.getInt("cd_empresa")==cdEmpresa)
					rsmReturn.addRegister(rsm.getRegister());
			}
			rsmReturn.beforeFirst();
			return cdEmpresa>0 ? rsmReturn : rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findProducaoAtendente: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	@SuppressWarnings("unchecked")
	public static ResultSetMap findCompletoWithComissao(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String group  = "";
			String fields = " A.cd_contrato_emprestimo,  A.cd_contratante,  A.cd_agente,  A.cd_empresa,  A.cd_produto, "+
							" A.cd_plano, A.qt_parcelas, A.vl_parcelas, A.dt_pagamento, A.vl_financiado, A.vl_tac, "+
							" A.vl_liberado, A.dt_contrato, A.cd_operacao, A.cd_motivo, A.cd_atendente, A.lg_expontaneo, "+
							" A.cd_pessoa, A.cd_orgao, A.nr_inscricao, A.cd_tabela_comissao, A.dt_cadastro, A.dt_protocolo, "+
							" A.dt_pagamento_empresa, A.dt_pagamento_agente, "+
							" B.nm_produto, B.cd_instituicao_financeira, "+
		                    " D.nm_situacao, F.nm_pessoa AS nm_parceiro, "+
		     	            " E.nm_pessoa AS nm_contratante, E.nm_pessoa AS nm_cliente, "+
		    	            " E.nr_cpf_cnpj, G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa, "+
						    " J.nm_cidade AS nm_cidade_endereco, L.nm_pessoa AS nm_atendente ";

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
				else if(nmToField.equals("NM_CLIENTE"))	{
					nmToField = " A.cd_contratante, "+nmToGroup+" AS "+nmToField;
					nmToGroup = " A.cd_contratante, "+nmToGroup;
				}
				else if(nmToField.equals("NM_PARCEIRO"))	{
					nmToField = " B.cd_instituicao_financeira, "+nmToGroup+" AS "+nmToField;
					nmToGroup = " B.cd_instituicao_financeira, "+nmToGroup;
				}
				else if(nmToField.equals("NM_PRODUTO"))	{
					nmToField = " A.cd_produto, "+nmToGroup+" AS "+nmToField;
					nmToGroup = " A.cd_produto, "+nmToGroup;
				}
				else if(nmToField.equals("NM_AGENTE"))	{
					nmToField = " A.cd_agente, "+nmToGroup+" AS "+nmToField;
					nmToGroup = " A.cd_agente, "+nmToGroup;
				}
				else if(nmToField.equals("NM_ATENDENTE"))	{
					nmToField = " A.cd_atendente, "+nmToGroup+" AS "+nmToField;
					nmToGroup = " A.cd_atendente, "+nmToGroup;
				}
				else if(nmToField.equals("NM_OPERACAO"))	{
					nmToField = " A.cd_operacao, "+nmToGroup+" AS "+nmToField;
					nmToGroup = " A.cd_operacao, "+nmToGroup;
				}
				else if(nmToField.equals("NM_SITUACAO"))	{
					nmToField = " A.cd_situacao, "+nmToGroup+" AS "+nmToField;
					nmToGroup = " A.cd_situacao, "+nmToGroup;
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
			fields = groupBy.size()==0 ? fields : fields + ", COUNT(*) AS QT_CONTRATO, SUM(vl_financiado) AS vl_financiado, "+
			                                               "  SUM(vl_liberado) AS vl_liberado ";
			String sql = "SELECT "+fields+" "+
			             "FROM sce_contrato A "+
						 "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
						 "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
						 "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
						 "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
						 "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
						 "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
						 "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
						 "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
						 "LEFT OUTER JOIN grl_cidade J ON (E.cd_cidade_endereco = J.cd_cidade) "+
						 "LEFT OUTER JOIN grl_pessoa L ON (A.cd_atendente = L.cd_pessoa)";
			//System.out.println(sql+" "+group);
			ResultSetMap rsm = Search.find(sql, group, criterios, Conexao.conectar(), true);
			ResultSetMetaData rsmd = null;
			if(!group.equals(""))
				rsmd = connect.prepareStatement(sql + "WHERE 1 <> 1 "+group).executeQuery().getMetaData();
			pstmt = connect.prepareStatement(
				"SELECT A.cd_pessoa, SUM(vl_comissao) AS vl_comissao, SUM(vl_pago) AS vl_pago, "+
				"       AVG(pr_aplicacao) AS pr_aplicacao "+
				"FROM sce_contrato_comissao A "+
				"WHERE cd_contrato_emprestimo = ? "+
				"GROUP BY A.cd_pessoa");
			while(rsm.next())	{
				ResultSetMap rsmComissao = new ResultSetMap();
				float vlComissaoEmpresa = 0, vlComissaoAgente = 0,
					  vlPagoEmpresa = 0, vlPagoAgente = 0;
				if(group.equals(""))	{
					pstmt.setInt(1, rsm.getInt("cd_contrato_emprestimo"));
					rsmComissao = new ResultSetMap(pstmt.executeQuery());
				}
				else	{
					sql = "SELECT X.cd_pessoa, SUM(vl_comissao) AS vl_comissao, SUM(vl_pago) AS vl_pago, "+
						  "       AVG(pr_aplicacao) AS pr_aplicacao "+
						  "FROM sce_contrato_comissao X "+
						  "JOIN sce_contrato A ON (A.cd_contrato_emprestimo = X.cd_contrato_emprestimo)"+
						  "JOIN sce_produto  B ON (A.cd_produto = B.cd_produto) "+
						  "JOIN grl_pessoa   E ON (A.cd_contratante = E.cd_pessoa) "+
						  "WHERE 1=1 ";
					ArrayList<ItemComparator> crt = (ArrayList<ItemComparator>)criterios.clone();
					for (int i=0; i<rsmd.getColumnCount()-3; i++) {
						if(rsmd.getColumnName(i+1).toUpperCase().indexOf("CD_")>=0)	{
							if(rsmd.getColumnName(i+1).toLowerCase().equals("cd_instituicao_financeira"))
								crt.add(new ItemComparator("B."+rsmd.getColumnName(i+1), rsm.getString(rsmd.getColumnName(i+1)), ItemComparator.EQUAL, Types.INTEGER));
							else
								crt.add(new ItemComparator("A."+rsmd.getColumnName(i+1), rsm.getString(rsmd.getColumnName(i+1)), ItemComparator.EQUAL, Types.INTEGER));
						}
						else if(rsmd.getColumnName(i+1).toUpperCase().indexOf("DT_")>=0)
							crt.add(new ItemComparator("A."+rsmd.getColumnName(i+1), Util.formatDateTime(rsm.getGregorianCalendar(rsmd.getColumnName(i+1)), "dd/MM/yyyy"),
									ItemComparator.EQUAL, Types.TIMESTAMP));
					}
					rsmComissao = Search.find(sql, "GROUP BY X.cd_pessoa", crt, Conexao.conectar(), true);
				}
				float prComissao = 0;
				while(rsmComissao.next())	{
					prComissao = rsmComissao.getFloat("pr_aplicacao");
					if(rsmComissao.getInt("cd_pessoa")==0)	{
						vlComissaoEmpresa += rsmComissao.getFloat("vl_comissao");
						vlPagoEmpresa += rsmComissao.getFloat("vl_pago");
					}
					else	{
						vlComissaoAgente += rsmComissao.getFloat("vl_comissao");
						vlPagoAgente += rsmComissao.getFloat("vl_pago");
					}
				}
				rsm.setValueToField("vl_comissao_empresa", new Float(vlComissaoEmpresa));
				rsm.setValueToField("vl_pago_empresa", new Float(vlPagoEmpresa));
				rsm.setValueToField("vl_comissao_agente", new Float(vlComissaoAgente));
				rsm.setValueToField("vl_pago_agente", new Float(vlPagoAgente));
				rsm.setValueToField("PR_APLICACAO", new Float(prComissao));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findCompletoWithComissao: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		System.out.println("println teste");
		Connection connect = Conexao.conectar();
		try {
			String groups = "GROUP BY A.cd_contrato_emprestimo, A.qt_parcelas, A.vl_parcelas, A.vl_financiado, "+
							" A.vl_liberado, A.dt_contrato, A.lg_expontaneo, B.nm_produto, D.nm_situacao, F.nm_pessoa, "+
		     	            " E.nm_pessoa, E.nm_pessoa, E.nr_cpf_cnpj, G.nm_operacao, H.nm_pessoa, I.nm_empresa, "+
						    " J.nm_cidade, L.nm_pessoa, P.nm_orgao ";

			String fields = " A.cd_contrato_emprestimo, A.qt_parcelas, A.vl_parcelas, A.vl_financiado, "+
							" A.vl_liberado, A.dt_contrato, A.lg_expontaneo, "+
							" B.nm_produto, D.nm_situacao, F.nm_pessoa AS nm_parceiro, "+
		     	            " E.nm_pessoa AS nm_contratante, E.nm_pessoa AS nm_cliente, "+
		    	            " E.nr_cpf_cnpj, G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa, "+
						    " J.nm_cidade AS nm_cidade_endereco, L.nm_pessoa AS nm_atendente, P.nm_orgao, " +
						    /* comissão da empresa */
						    " SUM(M1.vl_comissao) AS vl_comissao_empresa, " +
						    " SUM(M1.vl_pago) AS vl_pago_empresa, " +
						    " MAX(O1.dt_nota_fiscal) AS dt_pagamento_empresa, " +
						    /* comissão do agente */
						    " SUM(M2.vl_comissao) AS vl_comissao_agente, SUM(M2.vl_pago) AS vl_pago_agente, " +
						    " MAX(O2.dt_pagamento) AS dt_pagamento_agente ";

			if(groupBy.size()>0){
				String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups,
																			" COUNT(*) AS QT_CONTRATO, SUM(vl_financiado) AS vl_financiado, "+
														                    " SUM(vl_liberado) AS vl_liberado, " +
														                    " SUM(M1.vl_comissao) AS vl_comissao_empresa, " +
														                    " SUM(M1.vl_pago) AS vl_pago_empresa, " +
							    											" SUM(M2.vl_comissao) AS vl_comissao_agente, " +
							    											" SUM(M2.vl_pago) AS vl_pago_agente," +
							    											" MAX(O2.dt_pagamento) AS dt_pagamento_agente," +
							    											" MAX(O1.dt_nota_fiscal) AS dt_pagamento_empresa");
				fields = retorno[0];
				groups = retorno[1];
			}
			String sql = "SELECT "+fields+" "+
			             "FROM sce_contrato A "+
						 "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
						 "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
						 "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
						 "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
						 "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
						 "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
						 "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
						 "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
						 "LEFT OUTER JOIN grl_cidade J ON (E.cd_cidade_endereco = J.cd_cidade) "+
						 "LEFT OUTER JOIN grl_pessoa L ON (A.cd_atendente = L.cd_pessoa) " +
						 "LEFT OUTER JOIN sce_contrato_comissao M1 ON (A.cd_contrato_emprestimo = M1.cd_contrato_emprestimo" +
						 "                                         AND M1.cd_pessoa IS NULL) " +
						 "LEFT OUTER JOIN adm_nota_fiscal       O1 ON (M1.cd_nota_fiscal = O1.cd_nota_fiscal) " +
						 "LEFT OUTER JOIN sce_contrato_comissao M2 ON (A.cd_contrato_emprestimo = M2.cd_contrato_emprestimo" +
						 "                                         AND M2.cd_pessoa IS NOT NULL) " +
						 "LEFT OUTER JOIN adm_conta_pagar       O2 ON (M2.cd_conta_pagar = O2.cd_conta_pagar) " +
						 "LEFT OUTER JOIN sce_orgao             P  ON (B.cd_orgao = P.cd_orgao) ";
			System.out.println(sql);
			return Search.find(sql, groups, criterios, Conexao.conectar(), true);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int verificaContratoDuplicado(Contrato contrato, Connection connect)	{
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
				"SELECT lg_expontaneo FROM sce_contrato "+
				"WHERE cd_produto = ? "+
				"  AND cd_plano = ? "+
				"  AND cd_contratante = ? "+
				(contrato.getNrInscricao()==null||contrato.getNrInscricao().trim().equals("")?" AND nr_inscricao IS NULL ":" AND nr_inscricao = ?"));
			pstmt.setInt(1, contrato.getCdProduto());
			pstmt.setInt(2, contrato.getCdPlano());
			pstmt.setInt(3, contrato.getCdContratante());
			if(contrato.getNrInscricao()!=null&&!contrato.getNrInscricao().trim().equals(""))
				pstmt.setString(4, contrato.getNrInscricao());
			ResultSet rs = pstmt.executeQuery();
			int ret = 1;
			if(rs.next())
				ret = rs.getInt("lg_expontaneo")==1 ? DUPLICADO_COM_ESPONTANEO : DUPLICADO;
			return ret;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.verificaContratoDuplicado: " +  e);
			return -1;
		}
	}
	public static int revisarComissao(int cdContratoEmprestimo, boolean revisarComissaoPaga)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM sce_contrato_comissao "+
											 "WHERE cd_contrato_emprestimo = "+cdContratoEmprestimo+
											 " AND cd_nota_fiscal IS NULL " +
											 " AND cd_conta_pagar IS NULL " +
											 " AND cd_conta_receber IS NULL "+
											 " AND cd_comissao_origem IS NULL "+
											 " AND cd_comissao NOT IN (SELECT cd_comissao_origem "+
											 "                         FROM sce_contrato_comissao " +
											 "                         WHERE cd_contrato_emprestimo = " +cdContratoEmprestimo+
											 "                           AND cd_comissao_origem IS NOT NULL)");
			pstmt.executeUpdate();
			return gerarComissao(ContratoDAO.get(cdContratoEmprestimo, connect), connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.revisarComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	public static int updateComissao(int cdComissao, int tpCalculo, float prAplicacao)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement(
					"SELECT A.vl_financiado, A.vl_liberado, A.vl_tac, A.vl_parcelas, A.qt_parcelas, " +
					"       B.pr_aplicacao, B.vl_comissao, B.tp_calculo, B.tp_comissao, B.cd_conta_pagar " +
					"FROM sce_contrato A, sce_contrato_comissao B "+
					"WHERE A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
					"  AND B.cd_comissao = ?");
			pstmt.setInt(1, cdComissao);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				float vlPagoComissao = rs.getFloat("vl_comissao");
				if(rs.getInt("tp_comissao")!=4)
					vlPagoComissao = calcComissao(tpCalculo, prAplicacao,
													rs.getFloat("vl_liberado"), rs.getFloat("vl_parcelas"), rs.getInt("qt_parcelas"),
													rs.getFloat("vl_tac"), prAplicacao);

				int cdContaPagar = rs.getInt("cd_conta_pagar");
				pstmt = connect.prepareStatement(
						"UPDATE sce_contrato_comissao "+
						"SET tp_calculo = ?, pr_aplicacao = ?, vl_comissao=?, vl_pago=? "+
						"WHERE cd_comissao = ?");
				pstmt.setInt(1, tpCalculo);
				pstmt.setFloat(2, prAplicacao);
				pstmt.setFloat(3, rs.getFloat("vl_comissao"));
				pstmt.setFloat(4, vlPagoComissao);
				pstmt.setInt(5, cdComissao);
				pstmt.executeUpdate();
				if(cdContaPagar>0)
					ContaPagarServices.atualizaValoresConta(cdContaPagar, connect);
				connect.commit();
			}
			else
				return -10;
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.updateComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	public static int deletePagamentoOfComissao(int cdComissao)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int cdSituacao = 0;
			if (ParametroServices.getValorOfParametro("CD_SITUACAO_CONFIRMADA")!=null)
				cdSituacao = new Integer(ParametroServices.getValorOfParametro("CD_SITUACAO_CONFIRMADA")).intValue();

			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement("SELECT A.cd_conta_pagar, A.vl_pago, B.st_conta "+
					                         "FROM sce_contrato_comissao A, adm_conta_pagar B "+
					 						 "WHERE A.cd_conta_pagar = B.cd_conta_pagar "+
					 						 "  AND cd_comissao = ?");
			pstmt.setInt(1, cdComissao);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				int cdContaPagar = rs.getInt("cd_conta_pagar");
				if(rs.getInt("st_conta")==1)
					return -10;
				// Retira as informações de pagamento da comissão
				pstmt = connect.prepareStatement("UPDATE sce_contrato_comissao "+
						                         "SET cd_conta_pagar = null, dt_pagamento = null "+(cdSituacao>0?", cd_situacao = ? ":" ")+
												 "WHERE cd_comissao = ?");
				if(cdSituacao>0)
					pstmt.setInt(1, cdSituacao);
				pstmt.setInt(cdSituacao>0 ? 2 : 1, cdComissao);
				pstmt.executeUpdate();
				ContaPagarServices.atualizaValoresConta(cdContaPagar, connect);
				connect.commit();
			}
			else
				return -20;
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	public static int removerComissaoOfNotaFiscal(int cdComissao)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int cdSituacao = 0;
			if (ParametroServices.getValorOfParametro("CD_SITUACAO_COMISSAO")!=null)
				cdSituacao = new Integer(ParametroServices.getValorOfParametro("CD_SITUACAO_COMISSAO")).intValue();
			connect.setAutoCommit(false);
			// Buscando informações necessárias para a remoção da comissão da nota e os procedimentos decorrentes disso
			pstmt = connect.prepareStatement("SELECT A.cd_nota_fiscal, A.cd_contrato_emprestimo, B.cd_fechamento "+
					                         "FROM sce_contrato_comissao A, adm_nota_fiscal B "+
            								 "WHERE A.cd_comissao = ? "+
            								 "  AND A.cd_nota_fiscal = B.cd_nota_fiscal "+
            								 "  AND B.cd_fechamento IS NULL");
			pstmt.setInt(1, cdComissao);
			ResultSet rs = pstmt.executeQuery();
			if(!rs.next())
				return -10;
			int cdNotaFiscal = rs.getInt("cd_nota_fiscal");
			int cdContratoEmprestimo = rs.getInt("cd_contrato_emprestimo");
			// Remove comissão da nota fiscal
			pstmt = connect.prepareStatement("UPDATE sce_contrato_comissao "+
					                         "SET cd_nota_fiscal = null, dt_pagamento = null "+(cdSituacao>0?", cd_situacao = ? ":" ")+
											 "WHERE cd_comissao = ?");
			if(cdSituacao>0)
				pstmt.setInt(1, cdSituacao);
			pstmt.setInt(cdSituacao>0?2:1, cdComissao);
			pstmt.executeUpdate();
			// Altera situação da Nota Fiscal
			pstmt = connect.prepareStatement("UPDATE adm_nota_fiscal SET st_nota_fiscal = ? WHERE cd_nota_fiscal = ? ");
			pstmt.setInt(1, NotaFiscalServices.ST_LANCAMENTO);
			pstmt.setInt(2, cdNotaFiscal);
			pstmt.executeUpdate();
			// Altera situação do contrato
			cdSituacao = 0;
			if (ParametroServices.getValorOfParametro("CD_SITUACAO_CONTRATO")!=null)
				cdSituacao = new Integer(ParametroServices.getValorOfParametro("CD_SITUACAO_CONTRATO")).intValue();
			pstmt = connect.prepareStatement("SELECT * FROM sce_contrato_comissao "+
					                         "WHERE cd_contrato_emprestimo = ? "+
					                         "  AND cd_nota_fiscal IS NOT NULL");
			pstmt.setInt(1, cdContratoEmprestimo);
			if(!pstmt.executeQuery().next()){
				pstmt = connect.prepareStatement(
						"UPDATE sce_contrato "+
                        "SET dt_pagamento = null, vl_liberado = 0 "+(cdSituacao>0?", cd_situacao = ? ":" ")+
						"WHERE cd_contrato_emprestimo = ?");
				if(cdSituacao>0)
					pstmt.setInt(1, cdSituacao);
				pstmt.setInt(cdSituacao>0?2:1, cdContratoEmprestimo);
				pstmt.executeUpdate();
			}
			connect.commit();
			return 1;
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	public static int gerarComissao(Contrato contrato, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			if (isConnectionNull)	{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int cdTabelaComissao = contrato.getCdTabelaComissao();
			Produto produto = null;
			if(cdTabelaComissao<=0)	{
				produto = ProdutoDAO.get(contrato.getCdProduto());
				cdTabelaComissao = TabelaComissaoServices.getTabelaComissao(contrato.getCdEmpresa(), contrato.getCdAgente(),
						                                                    produto!=null?produto.getCdInstituicaoFinanceira():0,
						                                                    contrato.getCdProduto(), contrato.getCdPlano(), 0);
			}
			pstmt = connect.prepareStatement("SELECT * FROM sce_produto_comissao "+
											 "WHERE cd_produto = "+contrato.getCdProduto()+
											 "  AND cd_plano   = "+contrato.getCdPlano()+
											 "  AND (cd_tabela_comissao = "+cdTabelaComissao+
											 "	 OR  cd_tabela_comissao IS NULL)"+
											 "ORDER BY tp_comissao");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			boolean hasComissaoAgente = (contrato.getCdAgente() <= 0);
			// Procura configurações para o agente se for o caso, caso não encontre vai procurando nas tabelas mais gerais (níveis acima)
			for(int i=0; contrato.getCdTabelaComissao()<=0 && !hasComissaoAgente && i<5; i++)	{
				if(i>0)	{
					int cdTabelaAnterior = cdTabelaComissao;
					cdTabelaComissao = TabelaComissaoServices.getTabelaComissao(contrato.getCdEmpresa(), contrato.getCdAgente(),
													                            produto!=null?produto.getCdInstituicaoFinanceira():0,
													                            contrato.getCdProduto(), contrato.getCdPlano(), cdTabelaAnterior);
					rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM sce_produto_comissao "+
																	"WHERE cd_produto = "+contrato.getCdProduto()+
																	"  AND cd_plano   = "+contrato.getCdPlano()+
																	"  AND (cd_tabela_comissao = "+cdTabelaComissao+
																	"	 OR  cd_tabela_comissao IS NULL)"+
																	"ORDER BY tp_comissao").executeQuery());
				};
				//
				while(!hasComissaoAgente && rsm.next())	{
					hasComissaoAgente = rsm.getInt("tp_comissao")==ProdutoServices.COMISSAO_AGENTE;
				}
			}
			// Verifica existência de configuração de comissão
			if(rsm.size()==0 || cdTabelaComissao<=0)	{
				System.out.println("gerarComissao ... PRODUTO_SEM_CONFIGURACAO");
				return PRODUTO_SEM_CONFIGURACAO;
			}
			rsm.beforeFirst();
			int[] comissao = new int[] {0, 0, 0, 0, 0, 0};
			while(rsm.next())	{
				// Verifica se a comissão é para a operação em questão
				if(rsm.getInt("cd_operacao")>0 && rsm.getInt("cd_operacao")!=contrato.getCdOperacao())
					continue;
				// Inicia variáveis
				float vlComissao 	 = 0;
				float vlPagoComissao = 0;
				int cdSituacao 		 = new Integer(ParametroServices.getValorOfParametro("CD_SITUACAO_COMISSAO")).intValue();
				int tpComissao 		 = rsm.getInt("tp_comissao");
				float prAplicacao 	 = rsm.getFloat("vl_comissao");
				GregorianCalendar dtVencimento = (GregorianCalendar)contrato.getDtContrato().clone();
				if(dtVencimento==null)
					dtVencimento= new GregorianCalendar();
				int idComissao 		 = comissao[tpComissao];
				comissao[tpComissao]++;
				// Cálculo da comissão
				vlComissao = calcComissao(rsm.getInt("tp_calculo"), rsm.getFloat("vl_comissao"), contrato.getVlFinanciado(),
										  contrato.getVlParcelas(), contrato.getQtParcelas(), contrato.getVlTac(), prAplicacao);
				vlPagoComissao = calcComissao(rsm.getInt("tp_calculo"), rsm.getFloat("vl_comissao"), contrato.getVlLiberado(),
												contrato.getVlParcelas(), contrato.getQtParcelas(), contrato.getVlTac(), prAplicacao);
				int cdPessoa = 0;
				switch(tpComissao)	{
					case ProdutoServices.COMISSAO_EMPRESA: // Comissão da Empresa
						cdPessoa = 0;
						break;
					case ProdutoServices.COMISSAO_ATENDENTE: // Comissão do Atendente
						//cdPessoa = contrato.getCdAtendente();
						break;
					case ProdutoServices.COMISSAO_AGENTE: // Comissão do Agente
						cdPessoa = contrato.getCdAgente();
						break;
					case ProdutoServices.COMISSAO_SUBAGENTE:
						cdPessoa = contrato.getCdSubagente();
						break;
					case ProdutoServices.COMISSAO_GERENTE:
						//cdPessoa = PessoaServices.getCdResponsavel(contrato.getCdAgente(), contrato.getCdEmpresa());
						break;
				}
				if(rsm.getInt("nr_dias_vencimento")==0)
					dtVencimento.add(GregorianCalendar.DATE, 1);
				else
					dtVencimento.add(GregorianCalendar.DATE, rsm.getInt("nr_dias_vencimento"));
				if(tpComissao>0 && cdPessoa==0)	{
					continue;
				}
				pstmt = connect.prepareStatement("SELECT * FROM sce_contrato_comissao "+
							                     "WHERE cd_contrato_emprestimo = "+contrato.getCdContratoEmprestimo()+
							                     "  AND id_comissao = "+idComissao+
							                     "  AND tp_comissao = "+tpComissao+
							                     (cdPessoa>0 ? " AND cd_pessoa = "+cdPessoa : ""));

				if(pstmt.executeQuery().next())	{
					hasComissaoAgente = hasComissaoAgente || (cdPessoa>0);
					continue;
				}

				int code = Conexao.getSequenceCode("SCE_CONTRATO_COMISSAO");
				pstmt = connect.prepareStatement("INSERT INTO SCE_CONTRATO_COMISSAO (CD_COMISSAO,"+
				                                  "CD_CONTRATO_EMPRESTIMO,"+
				                                  "CD_SITUACAO,"+
				                                  "CD_PESSOA,"+
				                                  "CD_EMPRESA,"+
				                                  "VL_COMISSAO, " +
				                                  "VL_PAGO,"+
				                                  "TP_CALCULO, "+
				                                  "PR_APLICACAO, "+
				                                  "DT_PREVISAO, "+
				                                  "TP_COMISSAO,"+
				                                  "ID_COMISSAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setInt(1, code);
				if(contrato.getCdContratoEmprestimo()==0)
					pstmt.setNull(2, Types.INTEGER);
				else
					pstmt.setInt(2,contrato.getCdContratoEmprestimo());
				if(cdSituacao==0)
					pstmt.setNull(3, Types.INTEGER);
				else
					pstmt.setInt(3,cdSituacao);
				if(cdPessoa==0 || rsm.getInt("cd_operacao")==0)
					pstmt.setNull(4, Types.INTEGER);
				else
					pstmt.setInt(4,cdPessoa);
				if(contrato.getCdEmpresa()==0)
					pstmt.setNull(5, Types.INTEGER);
				else
					pstmt.setInt(5,contrato.getCdEmpresa());
				pstmt.setFloat(6, vlComissao);
				pstmt.setFloat(7, vlPagoComissao);
				pstmt.setInt(8, rsm.getInt("tp_calculo"));
				pstmt.setFloat(9, prAplicacao);
				pstmt.setTimestamp(10,new Timestamp(dtVencimento.getTimeInMillis()));
				pstmt.setInt(11, rsm.getInt("tp_comissao"));
				pstmt.setInt(12, idComissao);
				pstmt.executeUpdate();
				hasComissaoAgente = hasComissaoAgente || (cdPessoa>0);
			}
			if (isConnectionNull)	{
				if(contrato.getCdAgente()<0 || hasComissaoAgente)
					connect.commit();
				else	{
					System.out.println("gerarComissao ... isConnectionNull=true, sem comissão do agente");
					Conexao.rollback(connect);
					return -1;
				}
			}
			else	{
				if(contrato.getCdAgente()>0 && !hasComissaoAgente)	{
					System.out.println("gerarComissao ... isConnectionNull=false, sem comissão do agente");
					return PRODUTO_SEM_CONFIGURACAO;
				}
			}
			return 1;
		}
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.gerarComissao: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteComissao(int cdComissao){
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			pstmt = connect.prepareStatement("DELETE FROM SCE_CONTRATO_COMISSAO WHERE CD_COMISSAO=? ");
			pstmt.setInt(1, cdComissao);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.deleteComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findComissao(ArrayList<ItemComparator> criterios) {
		String sql = "SELECT A.*, B.nm_empresa, C.nm_pessoa AS nm_agente, "+
					 "       D.nm_situacao, E.nr_nota_fiscal, E.dt_nota_fiscal, "+
					 "       F.dt_vencimento, F.dt_pagamento AS dt_pagamento_conta, F.vl_conta, F.vl_abatimento, "+
					 "       F.st_conta "+
	                 "FROM sce_contrato_comissao A "+
					 "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
					 "LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) "+
					 "LEFT OUTER JOIN sce_situacao D ON (A.cd_situacao = D.cd_situacao)"+
					 "LEFT OUTER JOIN adm_nota_fiscal E ON (A.cd_nota_fiscal = E.cd_nota_fiscal) "+
					 "LEFT OUTER JOIN adm_conta_pagar F ON (A.cd_conta_pagar = F.cd_conta_pagar)";
		return Search.find(sql, "ORDER BY A.tp_comissao, A.tp_calculo", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap findContratoComissao(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_produto, C.qt_parcelas, C.vl_tac, C.pr_juros, "+
						   "       D.nm_situacao, E.nm_pessoa AS nm_cliente, F.nm_pessoa AS nm_parceiro, "+
						   "       G.nm_operacao, H.nm_pessoa AS nm_agente, I.nm_empresa "+
		                   "FROM sce_contrato A "+
						   "JOIN sce_produto       B ON (A.cd_produto = B.cd_produto) "+
						   "JOIN sce_produto_plano C ON (A.cd_produto = B.cd_produto AND A.cd_plano = C.cd_plano) "+
						   "JOIN sce_situacao      D ON (A.cd_situacao = D.cd_situacao) "+
						   "JOIN grl_pessoa        E ON (A.cd_contratante = E.cd_pessoa) "+
						   "JOIN grl_pessoa        F ON (B.cd_instituicao_financeira = F.cd_pessoa) "+
						   "LEFT OUTER JOIN sce_tipo_operacao G ON (A.cd_operacao = G.cd_operacao)"+
						   "LEFT OUTER JOIN grl_pessoa        H ON (A.cd_agente = H.cd_pessoa) "+
						   "JOIN grl_empresa       I ON (A.cd_empresa = I.cd_empresa)"+
						   "JOIN sce_contrato_comissao J ON (A.cd_contrato_emprestimo = J.cd_contrato_emprestimo)", criterios, Conexao.conectar());
	}

	public static int confirmarContrato(int cdNotaFiscal, int cdContratoEmprestimo, int cdComissao, int cdSituacaoContrato,
			int cdSituacaoComissaoEmpresa, int cdSituacaoComissaoAgente, float vlLiberado, float prComissao,
			float vlPagoComissao, GregorianCalendar dtPagamento, String nrContrato)
	{
		return 	confirmarContrato(cdNotaFiscal, cdContratoEmprestimo, cdComissao, cdSituacaoContrato,
									cdSituacaoComissaoEmpresa, cdSituacaoComissaoAgente, vlLiberado, prComissao,
									vlPagoComissao, dtPagamento, nrContrato, null);
	}

	public static int confirmarContrato(int cdNotaFiscal, int cdContratoEmprestimo, int cdComissao, int cdSituacaoContrato,
		int cdSituacaoComissaoEmpresa, int cdSituacaoComissaoAgente, float vlLiberado, float prComissao,
		float vlPagoComissao, GregorianCalendar dtPagamento, String nrContrato, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			if(cdNotaFiscal<=0)	{
				System.out.println("ERRO confirmar contrato, código inválido: cdNotaFiscal = "+cdNotaFiscal+"!");
				return -1;
			}
			if(cdComissao<=0)	{
				System.out.println("ERRO confirmar contrato, código inválido: cdComissao = "+cdComissao+"!");
				return -2;
			}
			if(cdContratoEmprestimo<=0)	{
				System.out.println("ERRO confirmar contrato, código inválido: cdContratoEmprestimo = "+cdContratoEmprestimo+"!");
				return -3;
			}
			if(isConnectionNull)	{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(dtPagamento.get(Calendar.YEAR)<2000)
				dtPagamento.set(Calendar.YEAR, dtPagamento.get(Calendar.YEAR)+2000);
			// Alterando dados do contrato
			pstmt = connect.prepareStatement(
									"UPDATE sce_contrato "+
								    "SET cd_situacao = ?, dt_pagamento = ?, vl_liberado = ?, nr_contrato = ?," +
								    "    dt_pagamento_empresa = (SELECT MAX(dt_nota_fiscal) FROM adm_nota_fiscal A, sce_contrato_comissao B "+
					                "                            WHERE A.cd_nota_fiscal = B.cd_nota_fiscal "+
					                "                              AND B.cd_contrato_emprestimo = sce_contrato.cd_contrato_emprestimo) "+
								    "WHERE cd_contrato_emprestimo = ? ");
			pstmt.setInt(1, cdSituacaoContrato);
			pstmt.setTimestamp(2, new Timestamp(dtPagamento.getTimeInMillis()));
			pstmt.setFloat(3, vlLiberado);
			pstmt.setString(4, nrContrato);
			pstmt.setInt(5, cdContratoEmprestimo);
			pstmt.executeUpdate();
			Contrato contrato = ContratoDAO.get(cdContratoEmprestimo, connect);
			if(contrato==null)	{
				System.out.println("ERRO confirmar contrato, código inválido: cdContratoEmprestimo = "+cdContratoEmprestimo+"!");
				return -5;
			}
			// Alterando / Calculando status das comissões da Empresa
			ResultSet rs = connect.prepareStatement("SELECT * FROM sce_contrato_comissao " +
					                         		"WHERE cd_comissao = "+cdComissao+
					                         		"  AND cd_contrato_emprestimo = "+cdContratoEmprestimo).executeQuery();

			if(!rs.next())	{
				System.out.println("ERRO confirmar contrato, nenhuma comissão encontrada!");
				return -10;
			}
			vlPagoComissao = calcComissao(rs.getInt("tp_calculo"), prComissao,
											vlLiberado, contrato.getVlParcelas(),
											contrato.getQtParcelas(), contrato.getVlTac(),
											prComissao);
			pstmt = connect.prepareStatement("UPDATE sce_contrato_comissao "+
			                                 "SET cd_nota_fiscal = "+cdNotaFiscal+
			                                 "   ,cd_situacao = "+cdSituacaoComissaoEmpresa+
			                                 "   ,vl_pago = "+vlPagoComissao+
			                                 "   ,pr_aplicacao = "+prComissao+
			                                 "   ,dt_pagamento = ? "+
			                                 "WHERE cd_contrato_emprestimo = "+cdContratoEmprestimo+
			                                 "  AND cd_pessoa IS NULL " +
			                                 "  AND cd_comissao = "+cdComissao);
			pstmt.setTimestamp(1, new Timestamp(dtPagamento.getTimeInMillis()));
			pstmt.executeUpdate();
			// Selecionando e confirmando comissões de agentes/gerentes e atendentes
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM sce_contrato_comissao "+
														                 "WHERE cd_contrato_emprestimo = "+cdContratoEmprestimo+
																		 "  AND cd_pessoa IS NOT NULL ").executeQuery());
			if(rsm.size()==0 && contrato.getCdAgente()>0)	{
				System.out.println("ERRO confirmar contrato, nenhuma comissão encontrada para o agente!");
				if(isConnectionNull)
					connect.rollback();
				return -11;
			}
			while(rsm.next())	{
				float vlPago = calcComissao(rsm.getInt("tp_calculo"), rsm.getFloat("pr_aplicacao"),
											contrato.getVlLiberado(), contrato.getVlParcelas(),
											contrato.getQtParcelas(), contrato.getVlTac(),
											rsm.getFloat("pr_aplicacao"));
				// Alterando status das comissões dos Agentes
				pstmt = connect.prepareStatement("UPDATE sce_contrato_comissao " +
						                         "SET cd_situacao = "+cdSituacaoComissaoAgente+
						                         "   ,vl_pago = "+vlPago+
	                    						 " WHERE cd_comissao = " +rsm.getInt("cd_comissao")+
	                    						 "   AND cd_contrato_emprestimo = "+rsm.getInt("cd_contrato_emprestimo"));
				if(pstmt.executeUpdate()==0)	{
					System.out.println("ERRO confirmar contrato, nenhuma comissão confirmada! ERRO -12");
					if(isConnectionNull)
						connect.rollback();
					return -12;
				}
			}
			// Verifica situação da Nota Fiscal
			//NotaFiscalServices.verificaSituacao(cdNotaFiscal, connect);
			if(isConnectionNull)	{
				//System.out.println();
				connect.commit();
			}
			return 1;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.confirmarContrato: " +  e);
			return -1;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insertContrato(Contrato contrato, boolean hasContrato){
		Connection connect = Conexao.conectar();
		try {
			if(contrato.getCdOperacao()>0 && contrato.getCdAgente()<=0)
				return AGENTE_OBRIGATORIO;
			if(contrato.getCdAgente()>0 && contrato.getCdOperacao()<=0)
				return OPERACAO_OBRIGATORIA;
			contrato.setDtCadastro(new GregorianCalendar());
			connect.setAutoCommit(false);
			int ret = verificaContratoDuplicado(contrato, connect);
			if(ret==1 || (ret==DUPLICADO && contrato.getCdMotivo()>0))	{
				contrato.setCdContratoEmprestimo(ContratoDAO.insert(contrato, connect));
			}
			else
				return ret;
			// Gerando Comissao
			if(contrato.getCdContratoEmprestimo()>0)	{
				if(gerarComissao(contrato, connect)<=0)	{
					Conexao.rollback(connect);
					System.out.println("insertContrato ... PRODUTO_SEM_CONFIGURACAO");
					return PRODUTO_SEM_CONFIGURACAO;
				}
				// Gera protocolo de documento
				Documento doc = new Documento(0, new GregorianCalendar(),
						null /*txtDocumento*/, null /*nmLocalOrigem*/, null /*nmLocalDestino*/,
						0 /*lgConfidencial*/, null /*idDocumento*/,
					   (hasContrato ? contrato.getCdAtendente() : contrato.getCdAgente()) /*cdPessoaAtual*/,
						contrato.getCdAtendente() /*Emissor*/,
						contrato.getCdEmpresa(),
						contrato.getCdEmpresa(), 0 /*Situação*/);
				int cdDocumento = DocumentoDAO.insert(doc, connect);
				PreparedStatement pstmt = connect.prepareStatement("UPDATE sce_contrato SET cd_documento = " +cdDocumento+
						                                           "WHERE cd_contrato_emprestimo = "+contrato.getCdContratoEmprestimo());
				pstmt.executeUpdate();
			}
			connect.commit();
			return contrato.getCdContratoEmprestimo();
		}
		catch(Exception e)	{
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.insertContrato: " +  e);
			return -100;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int updateContrato(Contrato contrato){
		Connection connect = Conexao.conectar();
		try {
			if(contrato.getCdOperacao()>0 && contrato.getCdAgente()<=0)
				return AGENTE_OBRIGATORIO;
			if(contrato.getCdAgente()>0 && contrato.getCdOperacao()<=0)
				return OPERACAO_OBRIGATORIA;
			if(contrato.getDtCadastro()==null)
				contrato.setDtCadastro(new GregorianCalendar());
			// Verificando duplicidade e incluindo
			int retorno = ContratoDAO.update(contrato, connect);
			// Gerando Comissao
			if(retorno>0)	{
				retorno = contrato.getCdContratoEmprestimo();
				revisarComissao(contrato.getCdContratoEmprestimo(), false);
			}
			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.updateContrato: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int deleteContrato(int cdContratoEmprestimo){
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement("DELETE FROM SCE_CONTRATO_COMISSAO WHERE CD_CONTRATO_EMPRESTIMO=?");
			pstmt.setInt(1, cdContratoEmprestimo);
			pstmt.executeUpdate();
			if(ContratoDAO.delete(cdContratoEmprestimo, connect)>0)	{
				connect.commit();
				return 1;
			}
			connect.rollback();
			return -1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.deleteComissao: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.deleteComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int setProtocolo(String listaContratos, boolean liberar)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE sce_contrato SET dt_protocolo = ? "+
			                                 "WHERE cd_contrato_emprestimo IN ("+listaContratos+")");
			if(liberar)
				pstmt.setNull(1, Types.TIMESTAMP);
			else	{
				GregorianCalendar dtProtocolo = new GregorianCalendar();
				dtProtocolo = new GregorianCalendar(dtProtocolo.get(Calendar.YEAR), dtProtocolo.get(Calendar.MONTH), dtProtocolo.get(Calendar.DATE));
				pstmt.setTimestamp(1, new Timestamp(dtProtocolo.getTimeInMillis()));
			}
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.setProtocolo: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int setSituacao(int cdContratoEmprestimo, int cdSituacao, int cdMotivo)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE sce_contrato SET cd_situacao = ?, cd_motivo = ? "+
			                                 "WHERE cd_contrato_emprestimo = ?");
			pstmt.setInt(1, cdSituacao);
			if(cdMotivo>0)
				pstmt.setInt(2, cdMotivo);
			else
				pstmt.setNull(2, Types.INTEGER);
			pstmt.setInt(3, cdContratoEmprestimo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.setSituacao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int insertContaAPagarAvulso(int cdContaPagar, int cdComissao, int cdEmpresa, int cdPessoa,
			float vlConta, float vlAbatimento, GregorianCalendar dtVencimento)
	{
		PreparedStatement pstmt;
		Connection connect = Conexao.conectar();
		try {
			int cdContaPagarOld = cdContaPagar;
			int cdComissaoFila = 0;
			if (ParametroServices.getValorOfParametro("CD_COMISSAO_FILA")!=null)
				cdComissaoFila = new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_FILA")).intValue();
			else if (ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")!=null)
				cdComissaoFila = new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_PAGA")).intValue();
			connect.setAutoCommit(false);
			int cdContaBancaria = 0;
			if(cdContaPagar<=0){
				cdContaBancaria = com.tivic.manager.crt.PessoaServices.getCdContaBancaria(cdPessoa);
				GregorianCalendar dtEmissao = new GregorianCalendar();
				dtEmissao.set(Calendar.HOUR_OF_DAY, 0);
				dtEmissao.set(Calendar.MINUTE, 0);
				dtEmissao.set(Calendar.SECOND, 0);
				int cdContaPadrao   = 0;
				int cdTipoDocumento = 0;
				ContaPagar conta = new ContaPagar(0 /*cdContaPagar*/, 0 /*cdContrato*/, cdPessoa, cdEmpresa, 0 /*cdContaOrigem*/,
						  0 /*cdDocumentoEntrada*/, cdContaPadrao, cdContaBancaria, dtVencimento, dtEmissao,
						  null /*dtPagamento*/, null /*dtAutorizacao*/, null /*nrDocumento*/, null /*nrReferencia*/,
						  0 /*nrParcela*/, cdTipoDocumento, vlConta, 0 /*vlAbatimento*/, 0 /*vlAcrescimo*/,
						  0 /*vlPago*/, "" /*Histórico*/, com.tivic.manager.adm.ContaPagarServices.ST_EM_ABERTO,
						  0 /*lgAutorizado*/, 0 /*tpFrequencia*/, 0 /*qtParcelas*/, 0 /*vlBaseAutorizacao*/,
						  0 /*cdViagem*/, 0 /*cdManutencao*/, null /*txtObservacao*/, new GregorianCalendar(), dtVencimento, 0/*cdTurno*/);

				cdContaPagar = ContaPagarDAO.insert(conta, connect);
			}
			if(cdContaPagar>0)	{
				pstmt = connect.prepareStatement(
						 "SELECT B.vl_liberado, B.vl_parcelas, B.qt_parcelas, B.vl_tac, "+
						 "		 A.pr_aplicacao, A.tp_calculo, A.tp_comissao, A.vl_comissao "+
						 "FROM sce_contrato_comissao A, sce_contrato B "+
						 "WHERE A.cd_contrato_emprestimo = B.cd_contrato_emprestimo "+
						 "  AND A.cd_comissao = ? ");
				pstmt.setInt(1, cdComissao);
				ResultSet rs = pstmt.executeQuery();
				float vlPagoComissao = 0;
				if(rs.next())	{
					vlPagoComissao = rs.getFloat("vl_comissao");
					if(rs.getInt("tp_comissao")!=4)
						vlPagoComissao = calcComissao(rs.getInt("tp_calculo"), rs.getFloat("pr_aplicacao"),
								rs.getFloat("vl_liberado"), rs.getFloat("vl_parcelas"), rs.getInt("qt_parcelas"),
								rs.getFloat("vl_tac"), rs.getFloat("pr_aplicacao"));
				}
				//
				pstmt = connect.prepareStatement(
						 "UPDATE sce_contrato_comissao "+
						 "SET cd_conta_pagar = ?, dt_previsao = ?, cd_situacao = ?, vl_pago = ? "+
						 "WHERE cd_comissao = ? ");
				pstmt.setInt(1, cdContaPagar);
				pstmt.setTimestamp(2, new Timestamp(dtVencimento.getTimeInMillis()));
				pstmt.setInt(3, cdComissaoFila);
				pstmt.setFloat(4, vlPagoComissao);
				pstmt.setInt(5, cdComissao);
				pstmt.executeUpdate();
				if(vlAbatimento>0&&cdContaPagar>0)
					AdiantamentoServices.setPagamento(cdEmpresa, cdPessoa, vlAbatimento, cdContaPagar, connect);
				if(cdContaPagarOld>0)
					ContaPagarServices.atualizaValoresConta(cdContaPagar, connect);
				connect.commit();
			}
			else
				connect.rollback();
			if(cdContaBancaria<=0 && cdContaPagarOld<=0)
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

	public static String main(int cdProdutoOut)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_contrato_emprestimo, vl_liberado, vl_parcelas, qt_parcelas, vl_tac "+
											 "FROM sce_contrato A "+
											 "WHERE A.vl_liberado > 0  " +
											 "  AND A.cd_produto <> "+cdProdutoOut+
											 "  AND EXISTS (SELECT * FROM sce_contrato_comissao C " +
											 "              WHERE A.cd_contrato_emprestimo = C.cd_contrato_emprestimo " +
											 "                AND A.cd_agente = C.cd_pessoa " +
											 "                AND C.pr_aplicacao > 0 " +
											 "                AND C.vl_pago IS NULL)");
			ResultSet rs = pstmt.executeQuery();
			String retorno = "";
			int count=0;
			while(rs.next())	{
				count++;
				int cdContratoEmprestimo = rs.getInt("cd_contrato_emprestimo");
				ResultSet rsCom = connect.prepareStatement("SELECT cd_comissao, pr_aplicacao, tp_calculo " +
						                                   "FROM sce_contrato_comissao "+
														   "WHERE cd_contrato_emprestimo = "+cdContratoEmprestimo+
						                                   "  AND cd_pessoa IS NOT NULL ").executeQuery();
				retorno += "cdContratoEmprestimo="+cdContratoEmprestimo+"\n";
				while(rsCom.next())	{
					int cdComissao = rsCom.getInt("cd_comissao");
					float prAplicacao = rsCom.getFloat("pr_aplicacao");
					int tpCalculo = rsCom.getInt("tp_calculo");
					float vlPago = calcComissao(tpCalculo, prAplicacao,
												rs.getFloat("vl_liberado"), rs.getFloat("vl_parcelas"),
												rs.getInt("qt_parcelas"), rs.getFloat("vl_tac"),
												prAplicacao);
					retorno += "vlPago="+vlPago+", cdComissao="+cdComissao+"\n";
					// Alterando status das comissões dos Agentes
					pstmt = connect.prepareStatement("UPDATE sce_contrato_comissao " +
					                                 "SET vl_pago = "+vlPago+
													 " WHERE cd_comissao = " +cdComissao+
													 "   AND cd_contrato_emprestimo = "+cdContratoEmprestimo);
					pstmt.executeUpdate();
				}
			}
			System.out.println(count);
			return retorno;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return e.getMessage();
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int insertEstornoComissao(int cdComissaoOrigem, int cdSituacao, float vlComissao)
	{
		PreparedStatement pstmt;
		Connection connect = Conexao.conectar();
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sce_contrato_comissao WHERE cd_comissao_origem = ?");
			pstmt.setInt(1, cdComissaoOrigem);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next())
				return -10;
			pstmt = connect.prepareStatement("SELECT * FROM sce_contrato_comissao WHERE cd_comissao = ?");
			pstmt.setInt(1, cdComissaoOrigem);
			rsm = new ResultSetMap(pstmt.executeQuery());
			if(!rsm.next())
				return -20;
			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement("INSERT INTO SCE_CONTRATO_COMISSAO (CD_COMISSAO,"+
			                                  "CD_CONTRATO_EMPRESTIMO,"+
			                                  "CD_SITUACAO,"+
			                                  "CD_PESSOA,"+
			                                  "CD_EMPRESA,"+
			                                  "VL_COMISSAO, "+
			                                  "TP_CALCULO, "+
			                                  "PR_APLICACAO, "+
			                                  "DT_PREVISAO, "+
			                                  "TP_COMISSAO, "+
			                                  "CD_COMISSAO_ORIGEM, "+
			                                  "VL_PAGO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int code = Conexao.getSequenceCode("SCE_CONTRATO_COMISSAO");
			pstmt.setInt(1, code);
			pstmt.setInt(2, rsm.getInt("cd_contrato_emprestimo"));
			if(cdSituacao==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,cdSituacao);
			if(rsm.getInt("cd_pessoa")==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,rsm.getInt("cd_pessoa"));
			if(rsm.getInt("cd_empresa")==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,rsm.getInt("cd_empresa"));
			pstmt.setFloat(6, vlComissao * -1);
			pstmt.setFloat(7, Produto.FIXO_POR_OPERACAO);
			pstmt.setFloat(8, 0);
			if(rsm.getGregorianCalendar("dt_previsao")==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(rsm.getGregorianCalendar("dt_previsao").getTimeInMillis()));
			pstmt.setInt(10, 4); // Estorno
			pstmt.setInt(11, cdComissaoOrigem);
			pstmt.setFloat(12, vlComissao * -1);
			pstmt.executeUpdate();
			// Atualizando informação da comissão
			cdSituacao = 0;
			if (ParametroServices.getValorOfParametro("CD_COMISSAO_ESTORNADA")!=null)	{
				cdSituacao = new Integer(ParametroServices.getValorOfParametro("CD_COMISSAO_ESTORNADA")).intValue();
				pstmt = connect.prepareStatement("UPDATE sce_contrato_comissao SET cd_situacao = ? WHERE cd_comissao = ?");
				pstmt.setInt(1, cdSituacao);
				pstmt.setInt(2, cdComissaoOrigem);
				pstmt.executeUpdate();
			}
			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.gerarComissao: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.gerarComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int insertComissao(int cdContratoEmprestimo, int cdSituacao, int cdPessoa, int tpComissao,
			int tpCalculo, float vlComissao, float prAplicacao)
	{
		PreparedStatement pstmt;
		Connection connect = Conexao.conectar();
		try {
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM sce_contrato WHERE cd_contrato_emprestimo = "+cdContratoEmprestimo).executeQuery());
			if(!rsm.next())
				return -10;
			float vlPagoComissao = calcComissao(tpCalculo, prAplicacao,
					rsm.getFloat("vl_liberado"), rsm.getFloat("vl_parcelas"), rsm.getInt("qt_parcelas"),
					rsm.getFloat("vl_tac"), prAplicacao);

			connect.setAutoCommit(false);
			pstmt = connect.prepareStatement("INSERT INTO SCE_CONTRATO_COMISSAO (CD_COMISSAO,"+
			                                  "CD_CONTRATO_EMPRESTIMO,"+
			                                  "CD_SITUACAO,"+
			                                  "CD_PESSOA,"+
			                                  "CD_EMPRESA,"+
			                                  "VL_COMISSAO, "+
			                                  "TP_CALCULO, "+
			                                  "PR_APLICACAO, "+
			                                  "DT_PREVISAO, "+
			                                  "TP_COMISSAO, "+
			                                  "VL_PAGO, ID_COMISSAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)");
			int code = Conexao.getSequenceCode("SCE_CONTRATO_COMISSAO");
			pstmt.setInt(1, code);
			pstmt.setInt(2, cdContratoEmprestimo);
			if(cdSituacao==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,cdSituacao);
			if(cdPessoa==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4, cdPessoa);
			if(rsm.getInt("cd_empresa")==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,rsm.getInt("cd_empresa"));
			pstmt.setFloat(6, vlComissao);
			pstmt.setFloat(7, tpCalculo);
			pstmt.setFloat(8, prAplicacao);
			pstmt.setNull(9, Types.TIMESTAMP);
			pstmt.setInt(10, tpComissao); // Estorno
			pstmt.setFloat(11, vlPagoComissao);
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.insertComissao: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.insertComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static float calcComissao(int tpCalculo, float prAplicacao, float vlContrato, float vlParcela,
			int qtParcelas, float vlTac, float vlDeducao)	{
		switch(tpCalculo)	{
			case Produto.PERCENTUAL_EMPRESTADO:
				return (prAplicacao * vlContrato / 100);
			case Produto.PERCENTUAL_SPREAD:
				return (prAplicacao * ((qtParcelas * vlParcela) - vlContrato) / 100);
			case Produto.FIXO_POR_OPERACAO:
				return prAplicacao;
			case Produto.PERCENTUAL_TAC:
				return (prAplicacao * vlTac / 100);
			case Produto.PERCENTUAL_OUTRA_COMISSAO:
				return (prAplicacao * vlContrato / 100);
			case Produto.TAC_MENOS_FIXO:
				return vlTac - vlDeducao;
		}
		return 0;
	}
}