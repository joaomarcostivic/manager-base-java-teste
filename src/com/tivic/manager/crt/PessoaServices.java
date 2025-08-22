package com.tivic.manager.crt;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import java.util.GregorianCalendar;
import sol.dao.Search;
import java.util.ArrayList;
import java.sql.*;

import com.tivic.manager.conexao.*;
import com.tivic.manager.crt.Documento;
import com.tivic.manager.crt.DocumentoDAO;
import com.tivic.manager.grl.*;
import com.tivic.sol.connection.Conexao;

public class PessoaServices {
	public static final int PARCEIRO   = 1;
	public static final int CORRETOR   = 2;
	public static final int PROMOTOR   = 3;

	public static final int FISICA   = 0;
	public static final int JURIDICA = 1;

	public static final int SOLTEIRO   	= 0;
	public static final int CASADO 	   	= 1;
	public static final int SEPARADO 	= 2;
	public static final int DIVORCIADO 	= 3;
	public static final int VIUVO 		= 4;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN GRL_CIDADE B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_CIDADE C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)", criterios, Conexao.conectar());
	}

	public static ResultSetMap findWithVinculo(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade, "+
						   "       D.*, E.nm_empresa, F.nm_vinculo "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN grl_cidade B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN grl_cidade C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
		                   "     INNER JOIN grl_pessoa_empresa D ON (A.CD_PESSOA = D.CD_PESSOA) "+
		                   "JOIN grl_empresa E ON (D.cd_empresa = E.cd_empresa) "+
		                   "JOIN grl_vinculo F ON (D.cd_vinculo = F.cd_vinculo)",
		                   "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap findWithVinculoAndConta(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade, "+
						   "       D.*, E.nm_empresa, F.nm_vinculo, G.nr_conta, G.nr_dv, G.nr_agencia," +
						   "       G.tp_operacao, G.nr_cpf_cnpj AS nr_cpf_cnpj_titular, G.nm_titular," +
						   "       H.nr_banco, H.nm_banco "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN grl_cidade B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN grl_cidade C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
		                   "     INNER JOIN grl_pessoa_empresa D ON (A.CD_PESSOA = D.CD_PESSOA) "+
		                   "JOIN grl_empresa E ON (D.cd_empresa = E.cd_empresa) "+
		                   "JOIN grl_vinculo F ON (D.cd_vinculo = F.cd_vinculo) "+
		                   "LEFT OUTER JOIN grl_pessoa_conta_bancaria G ON (A.cd_pessoa = G.cd_pessoa) " +
		                   "LEFT OUTER JOIN grl_banco                 H ON (G.cd_banco  = H.cd_banco) ",
		                   "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}

	public static ResultSetMap getColaboradorBloqueado() {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
						   "SELECT A.cd_pessoa, A.nm_pessoa, D.*, E.nm_empresa, F.nm_vinculo "+
		                   "FROM grl_pessoa_empresa D, grl_pessoa A, grl_empresa E, grl_vinculo F "+
		                   "WHERE A.cd_pessoa = D.cd_pessoa "+
						   "  AND D.st_vinculo = 2 "+
						   "  AND D.cd_vinculo IN (2,3) "+
						   "  AND D.cd_empresa = E.cd_empresa "+
						   "  AND D.cd_vinculo = F.cd_vinculo ");
		   return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getColaboradorBloqueado: " + sqlExpt);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findColaborador(ArrayList<ItemComparator> criterios) {
		int cdEmpresa = 0;
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().toUpperCase().equals("CD_EMPRESA"))	{
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
		}
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade, "+
						   "       D.nm_pessoa AS nm_superior  "+
		                   "FROM GRL_PESSOA A "+
		                   "LEFT OUTER JOIN GRL_CIDADE B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_CIDADE C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
		                   "LEFT OUTER JOIN GRL_PESSOA D ON (A.CD_SUPERIOR = D.CD_PESSOA)"+
		                   "WHERE EXISTS (SELECT * FROM grl_pessoa_empresa D "+
		                   "              WHERE A.cd_pessoa  = D.cd_pessoa "+
		                   (cdEmpresa>0?" AND D.cd_empresa = "+cdEmpresa:"")+
		                   "                AND D.cd_vinculo <> "+VinculoServices.PARCEIRO+")",
		                   "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}
	public static ResultSetMap findCliente(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		try	{
			int top  = 0;
			int skip = 0;
			int lgOrgao   = 0;
			int lgProduto = 0;
			int qtContratoInicial = 0;
			int qtContratoFinal   = 0;
			for(int i=0; i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().equalsIgnoreCase("top"))
					top = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("skip"))
					skip = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lgProduto"))
					lgProduto = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("lgOrgao"))
					lgOrgao = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("qt_contrato_inicial"))
					qtContratoInicial = Integer.parseInt(criterios.get(i).getValue());
				else if(criterios.get(i).getColumn().equalsIgnoreCase("qt_contrato_final"))
					qtContratoFinal = Integer.parseInt(criterios.get(i).getValue());
			}
			String sql = "SELECT "+(top>0?"TOP "+top:"")+" A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						 "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS sg_estado_naturalidade "+
				         "FROM GRL_PESSOA A "+
				         "LEFT OUTER JOIN GRL_CIDADE B ON (A.CD_CIDADE_ENDERECO = B.CD_CIDADE)"+
				         "LEFT OUTER JOIN GRL_CIDADE C ON (A.CD_CIDADE_NATURALIDADE = C.CD_CIDADE)"+
				         "WHERE NOT EXISTS (SELECT * FROM grl_pessoa_empresa D "+
				         "                  WHERE A.CD_PESSOA = D.CD_PESSOA) ";
			String cla = "";
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for(int i=0; i<criterios.size(); i++)	{
				String field = criterios.get(i).getColumn();
				if(field.equalsIgnoreCase("cd_orgao"))	{
					cla += " AND EXISTS (SELECT * FROM grl_pessoa_orgao X " +
						   "             WHERE A.cd_pessoa = X.cd_pessoa " +
						   "               AND X.cd_orgao = "+criterios.get(i).getValue()+")";
				}
				else if(field.equalsIgnoreCase("top") || field.equalsIgnoreCase("skip") ||
						field.equalsIgnoreCase("lgOrgao") || field.equalsIgnoreCase("lgProduto") ||
						field.equalsIgnoreCase("qt_contrato_inicial") || field.equalsIgnoreCase("qt_contrato_final"))	{

				}
				else if(field.equalsIgnoreCase("dt_contrato") || field.equalsIgnoreCase("cd_instituicao_financeira") || field.equalsIgnoreCase("cd_empresa") ||
						field.equalsIgnoreCase("cd_parceiro") || field.equalsIgnoreCase("cd_produto") || field.equalsIgnoreCase("cd_plano"))	{
					cla += 	" AND EXISTS (SELECT * FROM sce_contrato X, sce_produto Y " +
					   		"             WHERE A.cd_pessoa  = X.cd_contratante " +
					   		"               AND X.cd_produto = Y.cd_produto ";
					if(field.equalsIgnoreCase("cd_empresa"))
						cla += " AND X.cd_empresa = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("cd_instituicao_financeira"))
						cla += " AND Y.cd_instituicao_financeira = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("cd_produto"))
						cla += " AND X.cd_produto = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("cd_plano"))
						cla += " AND X.cd_plano = "+criterios.get(i).getValue();
					if(field.equalsIgnoreCase("dt_contrato"))	{
						GregorianCalendar data = com.tivic.manager.util.Util.stringToCalendar(criterios.get(i).getValue());
						cla += " AND X.dt_contrato "+criterios.get(i).getOperatorComparation()+" \'"+com.tivic.manager.util.Util.formatDateTime(data, "MM/dd/yyyy")+"\'";
					}
					cla += ")";
				}
				else
					crt.add(criterios.get(i));
			}
			if (qtContratoInicial > 0)
				cla += " AND (SELECT count(*) FROM sce_contrato X WHERE A.cd_pessoa = X.cd_contratante) >= "+qtContratoInicial;
			if (qtContratoFinal > 0)
				cla += " AND (SELECT count(*) FROM sce_contrato X WHERE A.cd_pessoa = X.cd_contratante) <= "+qtContratoFinal;
			sql += cla;
			if(skip>0)	{
				cla = cla.replaceAll("A\\.", "A2.");
				sql += " AND A.cd_pessoa NOT IN (SELECT TOP "+skip+" cd_pessoa FROM grl_pessoa A2 " +
					   "                         WHERE NOT EXISTS (SELECT * FROM grl_pessoa_empresa D2 "+
				       "                                           WHERE A2.CD_PESSOA = D2.CD_PESSOA) " +
				       cla+
				       "                         ORDER BY A2.nm_pessoa) ";
			}
			ResultSetMap rsm = Search.find(sql, "ORDER BY nm_pessoa", crt, Conexao.conectar(), true);
			if(lgProduto==1 || lgOrgao==1)	{
				PreparedStatement pstmtOrgao = connect.prepareStatement("SELECT * FROM grl_pessoa_orgao A, sce_orgao B " +
						                                                "WHERE A.cd_orgao = B.cd_orgao " +
						                                                "  AND A.cd_pessoa = ?");
				PreparedStatement pstmtProduto = connect.prepareStatement(
						"SELECT DISTINCT nm_produto, nm_pessoa AS nm_parceiro FROM sce_contrato A, sce_produto B, grl_pessoa C " +
                        "WHERE A.cd_produto = B.cd_produto " +
                        "  AND B.cd_instituicao_financeira = C.cd_pessoa " +
                        "  AND A.cd_contratante = ?");
				while(rsm.next())	{
					// Orgão
					pstmtOrgao.setInt(1, rsm.getInt("cd_pessoa"));
					ResultSet rs = pstmtOrgao.executeQuery();
					int i = 0;
					while(rs.next())	{
						rsm.setValueToField("NM_ORGAO"+i, rs.getString("NM_ORGAO"));
						rsm.setValueToField("NR_INSCRICAO"+i, rs.getString("NR_INSCRICAO"));
					}
					// Produto
					pstmtProduto.setInt(1, rsm.getInt("cd_pessoa"));
					rs = pstmtProduto.executeQuery();
					i = 0;
					while(rs.next())	{
						rsm.setValueToField("NM_PARCEIRO"+i, rs.getString("NM_PARCEIRO"));
						rsm.setValueToField("NM_PRODUTO"+i, rs.getString("NM_PRODUTO"));
					}
				}
			}
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ResultSetMap findParceiro(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_cidade AS nm_cidade_endereco, B.sg_estado AS sg_estado_endereco, "+
						   "       C.nm_cidade AS nm_cidade_naturalidade, C.sg_estado AS nm_estado_naturalidade "+
		                   "FROM grl_pessoa A "+
		                   "LEFT OUTER JOIN grl_cidade    B ON (A.cd_cidade_endereco = B.cd_cidade)"+
		                   "LEFT OUTER JOIN grl_cidade    C ON (A.cd_cidade_naturalidade = B.cd_cidade)"+
		                   "INNER JOIN grl_pessoa_empresa D ON (A.cd_pessoa = D.cd_pessoa) "+
		                   "WHERE D.cd_vinculo = "+VinculoServices.PARCEIRO, "ORDER BY A.nm_pessoa", criterios, Conexao.conectar(), true);
	}
	public static ResultSetMap findPessoaOrgao(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_orgao "+
		                   "FROM grl_pessoa_orgao A "+
		                   "JOIN sce_orgao B ON (A.cd_orgao = B.cd_orgao)", criterios, Conexao.conectar());
	}
	public static int insertPessoaOrgao(int cdPessoa, int cdOrgao, String nrInscricao,
				int cdOrgaoOld, String nrInscricaoOld, Connection connect)
	{
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_orgao "+
		                                     "WHERE CD_PESSOA    = ? "+
		                                     "  AND CD_ORGAO     = ? "+
		                                     "  AND NR_INSCRICAO = ? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdOrgao);
			pstmt.setString(3, nrInscricao);
			if(!pstmt.executeQuery().next() && cdOrgaoOld<=0)	{
				pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_orgao "+
			                                     "(CD_PESSOA, CD_ORGAO, NR_INSCRICAO) "+
			                                     "VALUES (?,?,?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdOrgao);
				pstmt.setString(3, nrInscricao);
				pstmt.executeUpdate();
			}
			else if(cdOrgao>0 && cdOrgaoOld>0) {
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_orgao SET CD_ORGAO=?, NR_INSCRICAO=? "+
			                                     "WHERE CD_PESSOA =? AND CD_ORGAO = ? AND NR_INSCRICAO=?");
				pstmt.setInt(1, cdOrgao);
				pstmt.setString(2, nrInscricao);
				pstmt.setInt(3, cdPessoa);
				pstmt.setInt(4, cdOrgaoOld);
				pstmt.setString(5, nrInscricaoOld);
				pstmt.executeUpdate();
			}
			else if(cdOrgaoOld>0)	{
				pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_orgao "+
			                                     "WHERE CD_PESSOA =? AND CD_ORGAO = ? AND NR_INSCRICAO=?");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdOrgaoOld);
				pstmt.setString(3, nrInscricaoOld);
				pstmt.executeUpdate();
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	public static int insertContaBancaria(int cdPessoa, int cdContaBancaria, int cdBanco, String nrAgencia,
			String nrConta, String nrDv, int tpOperacao, int stConta, String nrCpfCnpj, String nmTitular) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Verificando duplicidade
			pstmt = connect.prepareStatement("SELECT * FROM GRL_PESSOA_CONTA_BANCARIA "+
			                                 "WHERE cd_banco   = ? "+
			                                 "  AND nr_agencia = ? "+
			                                 "  AND nr_conta   = ? "+
			                                 "  AND cd_conta_bancaria <> ?");
			pstmt.setInt(1, cdBanco);
			pstmt.setString(2, nrAgencia.trim());
			pstmt.setString(3, nrConta.trim());
			pstmt.setInt(4, cdContaBancaria);
			if(pstmt.executeQuery().next())
				return -10;
			if(cdContaBancaria==0)	{
				cdContaBancaria = Conexao.getSequenceCode("GRL_PESSOA_CONTA_BANCARIA");
				pstmt = connect.prepareStatement("INSERT INTO GRL_PESSOA_CONTA_BANCARIA "+
			                                     "(CD_PESSOA, CD_CONTA_BANCARIA, CD_BANCO, NR_AGENCIA, "+
			                                     " NR_CONTA, NR_DV, TP_OPERACAO, ST_CONTA, NR_CPF_CNPJ, NM_TITULAR)"+
			                                     "VALUES (?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdContaBancaria);
				pstmt.setInt(3, cdBanco);
				pstmt.setString(4, nrAgencia.trim());
				pstmt.setString(5, nrConta.trim());
				pstmt.setString(6, nrDv.trim());
				pstmt.setInt(7, tpOperacao);
				pstmt.setInt(8, stConta);
				pstmt.setString(9, (nrCpfCnpj==null)?"":nrCpfCnpj.replaceAll("[\\.\\-/]", "").trim());
				pstmt.setString(10, nmTitular);
			}
			else	{
				pstmt = connect.prepareStatement("UPDATE GRL_PESSOA_CONTA_BANCARIA SET "+
			                                     "CD_BANCO=?, "+
			                                     "NR_AGENCIA=?, "+
			                                     "NR_CONTA=?, "+
			                                     "NR_DV=?, "+
			                                     "TP_OPERACAO=?, "+
			                                     "ST_CONTA=?, "+
			                                     "NR_CPF_CNPJ=?, "+
			                                     "NM_TITULAR=? WHERE CD_PESSOA=? AND CD_CONTA_BANCARIA=? ");
				pstmt.setInt(1, cdBanco);
				pstmt.setString(2, nrAgencia);
				pstmt.setString(3, nrConta);
				pstmt.setString(4, nrDv);
				pstmt.setInt(5, tpOperacao);
				pstmt.setInt(6, stConta);
				pstmt.setString(7, (nrCpfCnpj==null)?"":nrCpfCnpj.replaceAll("[\\.\\-/]", "").trim());
				pstmt.setString(8, nmTitular);
				pstmt.setInt(9, cdPessoa);
				pstmt.setInt(10, cdContaBancaria);
			}
			pstmt.executeUpdate();
			if(stConta==1 && cdContaBancaria>0)	{
				pstmt = connect.prepareStatement("UPDATE grl_pessoa_conta_bancaria SET st_conta = 0 "+
				                                 "WHERE cd_pessoa = ? AND cd_conta_bancaria <> ? ");
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, cdContaBancaria);
				pstmt.executeUpdate();
				// Atualizando contas
				pstmt = connect.prepareStatement("UPDATE adm_conta_pagar SET cd_conta_bancaria = ? "+
												 "WHERE cd_pessoa = ? AND cd_conta_bancaria IS NULL ");
				pstmt.setInt(1, cdContaBancaria);
				pstmt.setInt(2, cdPessoa);
				pstmt.executeUpdate();
			}
			return cdContaBancaria;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.insertContaBancaria: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteContaOfPessoa(int cdPessoa, int cdContaBancaria) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM GRL_PESSOA_CONTA_BANCARIA "+
		                                     "WHERE CD_PESSOA  = ? "+
		                                     "  AND CD_CONTA_BANCARIA = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdContaBancaria);
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.deleteContaOfPessoa: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findContaBancaria(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.nm_banco, B.nr_banco "+
		                   "FROM GRL_PESSOA_CONTA_BANCARIA A, GRL_BANCO B "+
		                   "WHERE A.CD_BANCO = B.CD_BANCO", criterios,  Conexao.conectar());
	}

	public static ResultSetMap getAgente(int cdEmpresa, int cdOperacao) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_pessoa, A.nm_pessoa, A.nr_cpf_cnpj "+
											 "FROM grl_pessoa_empresa B, grl_pessoa A, sce_vinculo_operacao C "+
											 "WHERE A.cd_pessoa   = B.cd_pessoa" +
											 "  AND B.st_contrato = 1 " +
											 "  AND B.cd_vinculo  = C.cd_vinculo "+
											 (cdOperacao>-10?" AND C.cd_operacao = "+cdOperacao:" AND C.cd_operacao > 0")+
											 (cdEmpresa>0?" AND B.cd_empresa = "+cdEmpresa:"")+
											 " ORDER BY nm_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAgente: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getAgente: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int getCdContaBancaria(int cdPessoa) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_conta_bancaria "+
											 "FROM grl_pessoa_conta_bancaria "+
											 "WHERE cd_pessoa = ? "+
											 "  AND st_conta = 1");
			pstmt.setInt(1, cdPessoa);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.getCdContaBancaria: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deletePessoa(int cdPessoa) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			connect.setAutoCommit(false);
			// Deletando Vínculos
			pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_empresa "+
											 "WHERE cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			// Deletando Conta Bancária
			pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_conta_bancaria "+
											 "WHERE cd_pessoa = ? ");
			pstmt.setInt(1, cdPessoa);
			pstmt.executeUpdate();
			int ret = PessoaDAO.delete(cdPessoa, connect);
			connect.commit();
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.deletePessoa: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	public static int insert(PessoaFisica objeto) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if(objeto.getNrCpf()!=null && !objeto.getNrCpf().trim().equals(""))	{
				pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE nr_cpf = ? AND cd_pessoa <> ?");
				pstmt.setString(1, objeto.getNrCpf().trim());
				pstmt.setInt(2, objeto.getCdPessoa());
				if(pstmt.executeQuery().next())
					return -10;
			}
			return PessoaDAO.insert(objeto, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.insert: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int insertColaborador(PessoaFisica objeto, int cdEmpresa, int cdVinculo, int cdFuncao,
			int cdTabelaComissao, int stVinculo, int stContrato)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if(objeto.getDtCadastro()==null)
				objeto.setDtCadastro(new GregorianCalendar());
			if(objeto.getNrCpf()!=null && !objeto.getNrCpf().trim().equals(""))	{
				pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa A " +
						                         "LEFT OUTER JOIN grl_pessoa_fisica   B ON (A.cd_pessoa = B.cd_pessoa) " +
						                         "WHERE nr_cpf     = ? " +
						                         "  AND cd_pessoa <> ?");
				pstmt.setString(1, objeto.getNrCpf().trim());
				pstmt.setInt(2, objeto.getCdPessoa());
				if(pstmt.executeQuery().next())
					return -10;
			}
			connect.setAutoCommit(false);
			int cdPessoa = PessoaDAO.insert(objeto, connect);
			int ret = 0;

			if (cdPessoa > 0)
				ret = EmpresaServices.insertVinculoWithEmpresa(cdEmpresa, cdPessoa, cdVinculo, cdFuncao, cdTabelaComissao, stVinculo, stContrato, connect);

			if(ret > 0 && cdPessoa > 0)	{
				connect.commit();
				return cdPessoa;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.insertColaborador: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
		return -1;
	}

	public static int updateColaborador(Pessoa objeto, int cdEmpresa, int cdVinculo, int cdFuncao,
			int cdTabelaComissao, int stVinculo, int stContrato)
	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			int ret = PessoaDAO.update(objeto, connect);

			if (ret > 0)
				ret = EmpresaServices.insertVinculoWithEmpresa(cdEmpresa, objeto.getCdPessoa(), cdVinculo, cdFuncao, cdTabelaComissao, stVinculo, stContrato, connect);

			if(ret > 0)	{
				connect.commit();
				return ret;
			}
			connect.rollback();
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.updateColaborador: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static String getEndereco(int cdPessoa) {
		Connection connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdPessoa), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			ResultSetMap rsm = find(criterios);
			if(!rsm.next())
				return null;
			else
				return com.tivic.manager.util.Util.formatEndereco("", rsm.getString("nm_rua"), rsm.getString("nr_endereco"),
										   rsm.getString("nm_complemento_endereco"),
										   rsm.getString("nm_bairro"), rsm.getString("nr_cep"),
										   rsm.getString("nm_cidade_endereco"), rsm.getString("sg_estado_endereco"),
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
	public static int insertCliente(PessoaFisica objeto,
			int cdOrgao1, String nrInscricao1, int cdOrgaoOld1, String nrInscricaoOld1,
			int cdOrgao2, String nrInscricao2, int cdOrgaoOld2, String nrInscricaoOld2)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if(objeto.getNmPessoa()==null)	{
				return -5;
			}
			if(objeto.getNrCpf()!=null && !objeto.getNrCpf().trim().equals(""))	{
				pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_fisica WHERE nr_cpf = ? AND cd_pessoa <> ?");
				pstmt.setString(1, objeto.getNrCpf().trim());
				pstmt.setInt(2, objeto.getCdPessoa());
				if(pstmt.executeQuery().next())
					return -10;
			}
			connect.setAutoCommit(false);
			int cdPessoa = PessoaDAO.insert(objeto, connect);
			int ret = cdPessoa;
			// Inserindo informações de um órgão empregador
			if (cdPessoa > 0 && (cdOrgao1>0 || cdOrgaoOld1>0))
				ret = insertPessoaOrgao(cdPessoa, cdOrgao1, nrInscricao1, cdOrgaoOld1, nrInscricaoOld1, connect);
			// Inserindo informações de outro órgão empregador
			if (ret > 0 && cdPessoa > 0 && (cdOrgao2>0 || cdOrgaoOld2>0))
				ret = insertPessoaOrgao(cdPessoa, cdOrgao2, nrInscricao2, cdOrgaoOld2, nrInscricaoOld2, connect);
			// Verifica se todas as operações foram bem sucessidas
			if(ret > 0 && cdPessoa > 0)	{
				connect.commit();
				return cdPessoa;
			}
			connect.rollback();
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.insertCliente: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	public static int updateCliente(Pessoa objeto,
			int cdOrgao1, String nrInscricao1, int cdOrgaoOld1, String nrInscricaoOld1,
			int cdOrgao2, String nrInscricao2, int cdOrgaoOld2, String nrInscricaoOld2)
	{
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			int ret = PessoaDAO.update(objeto, connect);
			// Inserindo informações de um órgão empregador
			if (ret > 0 && (cdOrgao1>0 || cdOrgaoOld1>0))
				ret = insertPessoaOrgao(objeto.getCdPessoa(), cdOrgao1, nrInscricao1, cdOrgaoOld1, nrInscricaoOld1, connect);
			// Inserindo informações de outro órgão empregador
			if (ret > 0 && (cdOrgao2>0 || cdOrgaoOld2>0))
				ret = insertPessoaOrgao(objeto.getCdPessoa(), cdOrgao2, nrInscricao2, cdOrgaoOld2, nrInscricaoOld2, connect);
			// Verifica se todas as operações foram bem sucessidas
			if(ret > 0)	{
				connect.commit();
				return ret;
			}
			connect.rollback();
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaServices.updateCliente: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int getCdResponsavel(int cdPessoa, int cdEmpresa)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_responsavel "+
											 "FROM grl_pessoa_empresa "+
					                         "WHERE cd_pessoa  = ? "+
											 "  AND cd_empresa = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
			else
				return -1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.getAll: " + sqlExpt);
			return sqlExpt.getErrorCode() * -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.getAll: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int setProtocoloContrato(int cdPessoa, int cdEmpresa, int cdVinculo, int cdUsuario, GregorianCalendar dtPrevisaoContrato)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			Documento doc = new Documento(0, new GregorianCalendar(),
					null /*txtDocumento*/, null /*nmLocalOrigem*/, null /*nmLocalDestino*/,
					0 /*lgConfidencial*/, null /*idDocumento*/,
				    cdUsuario /*cdPessoaAtual*/,
				    cdUsuario /*Emissor*/,
					cdEmpresa,
					cdEmpresa, 0 /*Situação*/);
			connect.setAutoCommit(false);
			int cdDocumento = DocumentoDAO.insert(doc, connect);
			pstmt = connect.prepareStatement("UPDATE grl_pessoa_empresa " +
					                         "SET cd_documento = " +cdDocumento+", st_contrato = 1, dt_previsao_contrato=? "+
					                         "WHERE cd_empresa = "+cdEmpresa+
					                         "  AND cd_pessoa  = "+cdPessoa+
					                         "  AND cd_vinculo = "+cdVinculo);
			pstmt.setTimestamp(1, new Timestamp(dtPrevisaoContrato.getTimeInMillis()));
			pstmt.executeUpdate();
			connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.setProtocoloContrato: " + sqlExpt);
			return sqlExpt.getErrorCode() * -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.setProtocoloContrato: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
}
