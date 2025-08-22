package com.tivic.manager.alm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.TipoOperacao;
import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class BalancoServices {

	public static final int ST_ANDAMENTO = 0;
	public static final int ST_CONCLUIDO = 1;

	public static final int TP_COMPLETO  = 0;
	public static final int TP_POR_GRUPO = 1;
	public static final int TP_PARCIAL   = 2;

	public static final String[] situacao = {"Em andamento", "Concluído"};
	public static final String[] tipo     = {"Completo", "Por Grupo", "Parcial"};

	public static Result save(Balanco objeto) {
		return save(objeto, null);
	}

	public static Result save(Balanco objeto, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			/*
			 * INSERT
			 */
			if(objeto.getCdBalanco() <= 0) {
				if(objeto.getDtBalanco() == null)
					objeto.setDtBalanco(new GregorianCalendar());
				objeto.setStBalanco(ST_ANDAMENTO);
				/*
				 * Verifica balanços em andamento
				 */
				if(objeto.getTpBalanco()==TP_COMPLETO) {
					PreparedStatement pstmt = connect.prepareStatement("SELECT cd_balanco FROM alm_balanco " +
							                                           "WHERE cd_empresa    = " +objeto.getCdEmpresa()+
							                                           "  AND st_balanco    = " +ST_ANDAMENTO+
							                                           "  AND cd_local_armazenamento = "+objeto.getCdLocalArmazenamento());
					ResultSet rs = pstmt.executeQuery();
					if (rs.next())
						return new Result(-1, "Não é possível registrar o balanço. Existe balanço anterior que ainda se encontra em andamento.");
				}	
				//

				NumeracaoDocumentoServices.getProximoNumero("BALANCO", new GregorianCalendar().get(Calendar.YEAR), objeto.getCdEmpresa(), connect);
				int cdBalanco = BalancoDAO.insert(objeto, connect);
				if (cdBalanco <= 0)
					return new Result(-1, "Erros reportados ao incluir registro de balanço.");
				//
				objeto.setCdBalanco(cdBalanco);
				return new Result(cdBalanco, "Balanço registrado com sucesso.", "balanco", objeto);
			}
			else
				return new Result(BalancoDAO.update(objeto, connect), "Balanço registrado com sucesso.", "balanco", objeto);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao incluir registro de balanço. Anote a mensagem de erro " +
					              "e entre em contato com o administrador ou o suporte técnico.", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getItem(int cdBalanco, int cdEmpresa, int cdProdutoServico)	{
		return getAllItens(cdBalanco, cdEmpresa, cdProdutoServico, null);
	}
	
	public static ResultSetMap getAllItens(int cdBalanco, int cdEmpresa) {
		return getAllItens(cdBalanco, cdEmpresa, 0, null);
	}

	public static ResultSetMap getAllItens(int cdBalanco, int cdEmpresa, int cdProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			boolean lgContabAut = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connection)==1;
			
			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa, connection);
			int cdLocalArmazenamento  = balanco.getCdLocalArmazenamento();
			// Descobrindo tabela de preço do varejo
			int cdTipoOperacaoVarejo = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
			int cdTabelaPrecoVarejo  = 0;
			if(cdTipoOperacaoVarejo>0){
				TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacaoVarejo, connection);
				cdTabelaPrecoVarejo = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
			}
			// Descobrindo tabela de preço do atacado
			int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
			int cdTabelaPrecoAtacado  = 0;
			if(cdTipoOperacaoAtacado>0)	{
				TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacaoAtacado, connection);
				cdTabelaPrecoAtacado = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
			}

			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_produto_servico, B.id_produto_servico, D.qt_ideal, D.qt_minima, D.qt_maxima, D.id_reduzido, txt_especificacao, txt_dado_tecnico, " +
					"       D.cd_unidade_medida, E.sg_unidade_medida, E.nm_unidade_medida, D.vl_custo_medio, D.qt_precisao_unidade, D.vl_ultimo_custo, F.id_classificacao_fiscal, " +
					// Entradas de ajustes
					"       (SELECT SUM(qt_entrada) FROM alm_documento_entrada_item A1 " +
		            "        JOIN alm_balanco_doc_entrada B1 ON (A1.cd_documento_entrada = B1.cd_documento_entrada) " +
		            "        WHERE A1.cd_produto_servico = A.cd_produto_servico "+
		            "          AND B1.cd_balanco         = "+cdBalanco+
		            "          AND B1.cd_empresa         = "+cdEmpresa+") AS qt_entrada_ajuste, "+
					// Saídas de ajustes
                    "       (SELECT SUM(qt_saida) FROM alm_documento_saida_item A1 " +
                    "		 JOIN alm_balanco_doc_saida B1 ON (A1.cd_documento_saida = B1.cd_documento_saida) " +
					"		 WHERE A1.cd_produto_servico = A.cd_produto_servico "+
					"  		   AND B1.cd_balanco         = "+cdBalanco+
					"          AND B1.cd_empresa         = "+cdEmpresa+") AS qt_saida_ajuste "+
//					/* somando estoque em caso de contabilidzação automatica*/
//					(lgContabAut ? "" : 
//							" (SELECT SUM(D.qt_estoque + D.qt_estoque_consignado) " +
//							"  FROM alm_produto_estoque D " +
//							"  WHERE D.cd_empresa = " +cdEmpresa+
//							"	 AND D.cd_produto_servico = A.cd_produto_servico) AS qt_estoque ") +
//					(!lgContabAut ? "" : 
//							/* Somando saídas */
//							" (SELECT SUM(D.qt_saida + D.qt_saida_consignada) " +
//							"  FROM alm_saida_local_item D, alm_documento_saida E " +
//							"  WHERE D.cd_documento_saida = E.cd_documento_saida " +
//							"    AND E.st_documento_saida = 1 " +
//							"    AND D.cd_empresa         = "+cdEmpresa+
//							"    AND D.cd_produto_servico = A.cd_produto_servico " +
//							(cdLocalArmazenamento>0 ? " AND D.cd_local_armazenamento = "+cdLocalArmazenamento : "")+
//							"    AND E.dt_documento_saida <= ?) AS qt_saida, " +
//							" (SELECT SUM(D.qt_entrada + D.qt_entrada_consignada) " +
//							"  FROM alm_entrada_local_item D, alm_documento_entrada E " +
//							"  WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
//							"    AND E.st_documento_entrada = 1 " +
//							"    AND D.cd_empresa           = "+cdEmpresa+
//							"    AND D.cd_produto_servico   = A.cd_produto_servico " +
//							(cdLocalArmazenamento>0 ? " AND D.cd_local_armazenamento = "+cdLocalArmazenamento : "")+
//							"    AND E.dt_documento_entrada <= ?) AS qt_entrada ") +
				   (cdTipoOperacaoVarejo>0 ? ",(SELECT vl_preco FROM adm_produto_servico_preco VRJ " +
	   		                     " WHERE VRJ.cd_produto_servico = A.cd_produto_servico " +
	   		                     "   AND dt_termino_validade IS NULL " +
	   		                     "   AND VRJ.cd_tabela_preco    = "+cdTabelaPrecoVarejo+") AS vl_preco_varejo " : "")+
	   		       (cdTipoOperacaoAtacado>0 ? ",(SELECT vl_preco FROM adm_produto_servico_preco ATD " +
               				     " WHERE ATD.cd_produto_servico = A.cd_produto_servico " +
		   		                 "   AND dt_termino_validade IS NULL " +
		   		                 "   AND ATD.cd_tabela_preco    = "+cdTabelaPrecoAtacado+") AS vl_preco_atacado ": "")+
					"FROM alm_balanco_produto_servico A " +
					"JOIN grl_produto_servico B ON (B.cd_produto_servico = A.cd_produto_servico)  " +
					"JOIN grl_produto         C ON (C.cd_produto_servico = A.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_produto_servico_empresa D ON (D.cd_produto_servico = A.cd_produto_servico AND D.cd_empresa = "+cdEmpresa+") " +
					"LEFT OUTER JOIN grl_unidade_medida          E ON (E.cd_unidade_medida = D.cd_unidade_medida) " +
					"LEFT OUTER JOIN adm_classificacao_fiscal    F ON (F.cd_classificacao_fiscal = B.cd_classificacao_fiscal) " +
					"WHERE A.cd_balanco = " +cdBalanco+
					"  AND A.cd_empresa = " +cdEmpresa+
					(cdProdutoServico>0 ? " AND A.cd_produto_servico = "+cdProdutoServico : "")+
					" ORDER BY nm_produto_servico");
			
//			if(lgContabAut)	{
//				pstmt.setTimestamp(1, new java.sql.Timestamp(balanco.getDtBalanco().getTimeInMillis())); 
//				pstmt.setTimestamp(2, new java.sql.Timestamp(balanco.getDtBalanco().getTimeInMillis()));
//			}
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while(rsm.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
				Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios);
				
				Double qtCompras = 0.0;
				Double qtVendas = 0.0;
				Double qtEstoque = 0.0;
				
				if(resultado.getCode() > 0){
					qtCompras = ((double)resultado.getObjects().get("QT_ENTRADA") + (double)resultado.getObjects().get("QT_ENTRADA_CONSIGNADA"));
					qtVendas  = ((double)resultado.getObjects().get("QT_SAIDA") + (resultado.getObjects().get("QT_SAIDA_CONSIGNADA") != null ? (double)resultado.getObjects().get("QT_SAIDA_CONSIGNADA") : 0));
					qtEstoque = ((double)resultado.getObjects().get("QT_ESTOQUE") + (double)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO"));
				}
				
				rsm.setValueToField("QT_ENTRADA", qtCompras);
				rsm.setValueToField("QT_SAIDA", qtVendas);
				rsm.setValueToField("QT_ESTOQUE", qtEstoque);
				
			}
			rsm.beforeFirst();
			
//			if(lgContabAut)	{
//				while (rsm.next())	{
//					rsm.getRegister().put("QT_ESTOQUE", rsm.getFloat("qt_entrada") - rsm.getFloat("qt_saida"));
//					if(rsm.getFloat("VL_CUSTO_MEDIO")<=0)
//						rsm.setValueToField("VL_CUSTO_MEDIO", rsm.getFloat("VL_ULTIMO_CUSTO"));
//					//
//					if(rsm.getFloat("qt_estoque_balanco") > 0 && rsm.getFloat("VL_CUSTO_MEDIO")<=0)	{
//						ResultSet rs = connection.prepareStatement("SELECT AVG(vl_preco) AS vl_preco FROM adm_produto_servico_preco A, adm_tabela_preco B " +
//								                                   "WHERE A.cd_tabela_preco  = B.cd_tabela_preco " +
//								                                   "  AND A.cd_produto_servico = "+rsm.getInt("cd_produto_servico")+
//								                                   "  AND B.cd_empresa         = "+cdEmpresa).executeQuery();
//						if(rs.next())
//							rsm.setValueToField("VL_CUSTO_MEDIO", rs.getFloat("vl_preco")/2);
//					}
//					if(rsm.getFloat("VL_CUSTO_MEDIO")<=0.01)
//						rsm.setValueToField("VL_CUSTO_MEDIO", 0.01);
//				}
//				rsm.beforeFirst();
//			}
			// Se tiver passado o produto, busca pelo menos o saldo atual
			if(rsm.size()==0 && cdProdutoServico>0) {
				HashMap<String,Object> reg = new HashMap<String,Object>();
				reg.put("QT_ESTOQUE_BALANCO", 0);
				reg.put("QT_ENTRADA", 0);
				reg.put("QT_SAIDA", 0);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				if(cdLocalArmazenamento > 0)
					criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdProdutoServico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("lgFiscal", "1", ItemComparator.EQUAL, Types.INTEGER));
				Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connection);
				reg.put("QT_ESTOQUE", ((double)resultado.getObjects().get("QT_ESTOQUE") + (double)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO")));
				rsm.addRegister(reg);
				rsm.beforeFirst();
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllEntradasAjuste(int cdBalanco, int cdEmpresa) {
		return getAllEntradasAjuste(cdBalanco, cdEmpresa, null);
	}

	public static ResultSetMap getAllEntradasAjuste(int cdBalanco, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_produto_servico, B.id_produto_servico, D.id_reduzido, txt_especificacao, txt_dado_tecnico, " +
					"       D.cd_unidade_medida, E.sg_unidade_medida, D.vl_custo_medio, D.qt_precisao_unidade " +
					"FROM alm_documento_entrada_item A " +
					"JOIN grl_produto_servico                    B  ON (B.cd_produto_servico = A.cd_produto_servico)  " +
					"JOIN grl_produto                            C  ON (C.cd_produto_servico = A.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_produto_servico_empresa D  ON (D.cd_produto_servico = A.cd_produto_servico AND D.cd_empresa = "+cdEmpresa+") " +
					"LEFT OUTER JOIN grl_unidade_medida          E  ON (E.cd_unidade_medida = D.cd_unidade_medida) " +
					"JOIN alm_balanco_doc_entrada                A1 ON (A1.cd_documento_entrada = A.cd_documento_entrada) " +
					"WHERE A1.cd_balanco = " +cdBalanco+
					"  AND A1.cd_empresa = " +cdEmpresa+
					" ORDER BY nm_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllSaidasAjuste(int cdBalanco, int cdEmpresa) {
		return getAllSaidasAjuste(cdBalanco, cdEmpresa, null);
	}

	public static ResultSetMap getAllSaidasAjuste(int cdBalanco, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_produto_servico, B.id_produto_servico, D.id_reduzido, txt_especificacao, txt_dado_tecnico, " +
					"       D.cd_unidade_medida, E.sg_unidade_medida, D.vl_custo_medio, D.qt_precisao_unidade " +
					"FROM alm_documento_saida_item A " +
					"JOIN grl_produto_servico                    B  ON (B.cd_produto_servico = A.cd_produto_servico)  " +
					"JOIN grl_produto                            C  ON (C.cd_produto_servico = A.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_produto_servico_empresa D  ON (D.cd_produto_servico = A.cd_produto_servico AND D.cd_empresa = "+cdEmpresa+") " +
					"LEFT OUTER JOIN grl_unidade_medida          E  ON (E.cd_unidade_medida = D.cd_unidade_medida) " +
					"JOIN alm_balanco_doc_saida                  A1 ON (A1.cd_documento_saida = A.cd_documento_saida) " +
					"WHERE A1.cd_balanco = " +cdBalanco+
					"  AND A1.cd_empresa = " +cdEmpresa+
					" ORDER BY nm_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result insertAllProdutos(int cdBalanco, int cdEmpresa) {
		return insertAllProdutos(cdBalanco, cdEmpresa, null);
	}

	public static Result insertAllProdutos(int cdBalanco, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			boolean lgContabAut = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connection)==1;
			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa, connection);
			GregorianCalendar dtBalanco = balanco.getDtBalanco();
			dtBalanco.set(Calendar.HOUR_OF_DAY, 23);
			dtBalanco.set(Calendar.MINUTE, 59);
			dtBalanco.set(Calendar.SECOND, 59);
			dtBalanco.set(Calendar.MILLISECOND, 999);

			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.cd_produto_servico," +
					/* somando estoque em caso de contabilidzação automatica*/
					(lgContabAut ? "" : 
							" (SELECT SUM(D.qt_estoque + D.qt_estoque_consignado) " +
							"  FROM alm_produto_estoque D " +
							"  WHERE D.cd_empresa         = " +cdEmpresa+
							"	 AND D.cd_produto_servico = A.cd_produto_servico) AS qt_estoque ") +
					(!lgContabAut ? "" : 
							" (SELECT SUM(D.qt_saida + D.qt_saida_consignada) " +
							"  FROM alm_saida_local_item D, alm_documento_saida E " +
							"  WHERE D.cd_documento_saida = E.cd_documento_saida " +
							"    AND D.cd_empresa         = " +cdEmpresa+
							"    AND D.cd_produto_servico = A.cd_produto_servico " +
							"    AND E.dt_documento_saida <= ?) AS qt_saida, " +
							" (SELECT SUM(D.qt_entrada + D.qt_entrada_consignada) " +
							"  FROM alm_entrada_local_item D, alm_documento_entrada E " +
							"  WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
							"    AND D.cd_empresa           = " +cdEmpresa+
							"    AND D.cd_produto_servico = A.cd_produto_servico " +
							"    AND E.dt_documento_entrada <= ?) AS qt_entrada ") +
					"FROM grl_produto_servico A " +
					"JOIN grl_produto                   B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"JOIN grl_produto_servico_empresa   C ON (A.cd_produto_servico = C.cd_produto_servico AND C.cd_empresa = "+cdEmpresa+") " +
					"WHERE A.tp_produto_servico = "+ProdutoServicoServices.TP_PRODUTO+
					(balanco.getCdGrupo()>0 ? " AND EXISTS (SELECT * FROM alm_produto_grupo PG " +
					                          "             WHERE PG.cd_produto_servico = C.cd_produto_servico " +
					                          "               AND PG.cd_empresa         = C.cd_empresa " +
					                          "               AND PG.cd_grupo           = "+balanco.getCdGrupo()+")" : ""));
			if (lgContabAut) {
				pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtBalanco));
				pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtBalanco));
			}
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_produto_servico " +
							                    "WHERE cd_balanco = "+cdBalanco+
							                    "  AND cd_empresa = "+cdEmpresa+
							                    "  AND cd_produto_servico = ? ");
			int qtIncluido = 0;
			int qtItens    = 0;
			while (lgContabAut && balanco.getStBalanco()==ST_ANDAMENTO && rsm.next()) {
				Double qtEstoque = rsm.getFloat("qt_entrada") - rsm.getDouble("qt_saida");
				if (rsm.getFloat("qt_entrada")>0 || rsm.getFloat("qt_saida")>0)	{
					qtItens++;
					pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
					ResultSet rs = pstmt.executeQuery();
					if(!rs.next())	{
						qtIncluido++;
						BalancoProdutoServicoServices.insert(new BalancoProdutoServico(cdBalanco, cdEmpresa, rsm.getInt("cd_produto_servico"), 
								                                                       qtEstoque, 0.0, rsm.getInt("cd_unidade_medida")), connection);
					}
				}
			}

			return new Result(1, qtIncluido+"/"+qtItens+" item(ns) incluido(s) com sucesso!");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Falhar ao incluir todos os produtos!");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, C.nm_pessoa AS nm_responsavel, D.nm_local_armazenamento, E.nm_grupo  " +
						   "FROM alm_balanco A " +
						   "LEFT OUTER JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
						   "LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
						   "LEFT OUTER JOIN alm_local_armazenamento D ON (A.cd_local_armazenamento = D.cd_local_armazenamento) "+
						   "LEFT OUTER JOIN alm_grupo               E ON (A.cd_grupo = E.cd_grupo) ",
				           criterios, connect!=null ? connect : Conexao.conectar(), connect != null);
	}

	public static ResultSetMap getBalancosEmAberto(int cdEmpresa) {
		return getBalancosEmAberto(cdEmpresa, null);
	}

	public static ResultSetMap getBalancosEmAberto(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ResultSetMap rsm = new ResultSetMap();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa AS nm_responsavel, D.nm_local_armazenamento, E.nm_grupo, " +
															 "	(\'Nº: \' || A.nr_balanco || \' - Data: \' || A.dt_balanco || \' - Local: \' || D.nm_local_armazenamento) AS ds_balanco " +
														     "FROM alm_balanco A " +
														     "LEFT OUTER JOIN seg_usuario             B ON (A.cd_usuario = B.cd_usuario) " +
														     "LEFT OUTER JOIN grl_pessoa              C ON (B.cd_pessoa = C.cd_pessoa) " +
														     "LEFT OUTER JOIN alm_local_armazenamento D ON (A.cd_local_armazenamento = D.cd_local_armazenamento) "+
														     "LEFT OUTER JOIN alm_grupo               E ON (A.cd_grupo = E.cd_grupo) "+
														     "WHERE A.cd_empresa = "+cdEmpresa+
														     "  AND A.st_balanco = 0");
			rsm = new ResultSetMap(pstmt.executeQuery());
			//
			while(rsm.next()){
				rsm.setValueToField("DS_BALANCO", "Nº: " + (rsm.getString("NR_BALANCO") != null ? rsm.getString("NR_BALANCO") : "") + " - Data: " + Util.formatDate(rsm.getTimestamp("dt_balanco"), "dd/MM/yyyy") + " - Local: " + (rsm.getString("NM_LOCAL_ARMAZENAMENTO") != null ? rsm.getString("NM_LOCAL_ARMAZENAMENTO") : ""));
			}
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Balanco getUltimoBalanco(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			ResultSetMap rsm = new ResultSetMap();
			Balanco balanco = new Balanco();
			PreparedStatement pstmt = connect.prepareStatement(
				"SELECT * "
			  + "FROM alm_balanco "
			  + "ORDER BY dt_balanco DESC "
			  + "LIMIT 1"
			);
			
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				balanco = BalancoDAO.get(rsm.getInt("CD_BALANCO"), cdEmpresa);
			}
			
			return balanco.getCdBalanco() > 0 ? balanco : null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result delete(int cdBalanco, int cdEmpresa) {
		return delete(cdBalanco, cdEmpresa, null);
	}

	public static Result delete(int cdBalanco, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa, connect);
			/*
			 * Verifica se existem balanços posteriores a esse
			 */
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_balanco FROM alm_balanco " +
					                                           "WHERE dt_fechamento > ? " +
					                                           "  AND cd_empresa = "+cdEmpresa);
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(balanco.getDtFechamento()));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possivel excluir o Balanço. Certifique-se de que não existam balanços posteriores ao balanço em questão.");
			}
			// Exclui itens [produtos e serviços]
			connect.prepareStatement("DELETE FROM alm_balanco_produto_servico " +
									 "WHERE cd_empresa = " +cdEmpresa+
									 "  AND cd_balanco = " +cdBalanco).execute();

			// Excluindo entradas de ajuste
			rs = connect.prepareStatement("SELECT * FROM alm_balanco_doc_entrada " +
					                      "WHERE cd_balanco = " +cdBalanco+
										  "  AND cd_empresa = " +cdEmpresa).executeQuery();
			while (rs.next()) {
				if (BalancoDocEntradaDAO.delete(cdBalanco, cdEmpresa, rs.getInt("cd_documento_entrada"), connect) <= 0)
					return new Result(-1, "Falha ao remover balanço. Anote a mensagem de erro e entre em contato " +
							              "com o administrador ou o suporte técnico.\n" +
										  "Não foi possível remover entrada do balanço (código erro: -1).");

				Result result = DocumentoEntradaServices.delete(rs.getInt("cd_documento_entrada"), connect);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					result.setMessage("Falha ao tentar excluir balanço. Anote a mensagem de erro e entre em contato " +
							          "com o administrador ou o suporte técnico.\n" +
							          "Falha ao remover movimento de entrada de balanço: "+result.getMessage());
					return result;
				}
			}
			// Excluindo saídas de ajuste
			rs = connect.prepareStatement("SELECT * FROM alm_balanco_doc_saida " +
					                      "WHERE cd_balanco = " +cdBalanco+
					                      "  AND cd_empresa = " +cdEmpresa).executeQuery();
			while (rs.next()) {
				if (BalancoDocSaidaDAO.delete(cdBalanco, cdEmpresa, rs.getInt("cd_documento_saida"), connect) <= 0)
					return new Result(-1, "Erros reportados ao remover balanço. Anote a mensagem de erro e entre em contato " +
							"com o administrador ou o suporte técnico.\n" +
							"Erro ao remover movimento de entrada de balanço (código erro: -1).");

				Result result = DocumentoSaidaServices.delete(rs.getInt("cd_documento_saida"), connect); 
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erros reportados ao remover balanço. Anote a mensagem de erro e entre em contato " +
							"com o administrador ou o suporte técnico.\n" +
							result.getMessage());
				}
			}
			/*
			 * Finalmente excluindo balanço
			 */
			int code = 0;
			if ((code = BalancoDAO.delete(cdBalanco, cdEmpresa, connect)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erros reportados ao remover registro de balanço. Anote a mensagem de erro e entre em contato " +
						"com o administrador ou o suporte técnico.\n" +
						"Erro ao remover registro de balanço (código erro: " + code +").");
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Balanço excluído com sucesso.");
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erros reportados ao remover balanço. Anote a mensagem de erro e entre em contato " +
					              "com o administrador ou o suporte técnico.", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result lancarAjustes(int cdBalanco, int cdEmpresa){
		return lancarAjustes(cdBalanco, cdEmpresa, null);
	}

	public static Result lancarAjustes(int cdBalanco, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa, connect);
			if (balanco.getStBalanco() != ST_ANDAMENTO)
				throw new Exception("Balanço já se encontra concluído.");

			connect.prepareStatement("DELETE FROM alm_documento_entrada_item " +
					                 "WHERE cd_documento_entrada IN (SELECT cd_documento_entrada FROM alm_balanco_doc_entrada " +
					                 "                               WHERE cd_balanco         = "+cdBalanco+
                                     "                                 AND cd_empresa         = "+cdEmpresa+")").executeUpdate();

			connect.prepareStatement("DELETE FROM alm_documento_saida_item A " +
	                 				 "WHERE cd_documento_saida IN (SELECT cd_documento_saida FROM alm_balanco_doc_saida " +
	                 				 "                             WHERE cd_balanco         = "+cdBalanco+
	                 				 "                               AND cd_empresa         = "+cdEmpresa+")").executeUpdate();
			ArrayList<DocumentoEntradaItem> entradas = new ArrayList<DocumentoEntradaItem>();
			ArrayList<DocumentoSaidaItem>   saidas   = new ArrayList<DocumentoSaidaItem>();

			ResultSetMap rsmProdutos = getAllItens(cdBalanco, cdEmpresa, 0/*cdProdutoServico*/, connect);
			while (rsmProdutos.next()) {
				// Verifica se a diferença é a menor ou a maior
				float qtDiferenca = rsmProdutos.getFloat("qt_estoque") - rsmProdutos.getFloat("qt_estoque_balanco");
				// Saídas
				if (qtDiferenca > 0.01)	{
					saidas.add(new DocumentoSaidaItem(0 /*cdDocumentoSaida*/, rsmProdutos.getInt("cd_produto_servico"), cdEmpresa, 
													  Math.abs(qtDiferenca) /*qtSaida*/, rsmProdutos.getFloat("vl_custo_medio") /*vlUnitario*/, 
													  0 /*vlAcrescimo*/, 0 /*vlDesconto*/, null /*dtEntregaPrevista*/, 
													  rsmProdutos.getInt("cd_unidade_medida"), 0 /*cdTabelaPreco*/, 0 /*cdItem*/, 0/*cdBico*/));
				}
				// Entradas
				else if (Math.abs(qtDiferenca) > 0.01)	{
					entradas.add(new DocumentoEntradaItem(0 /*cdDocumento*/, rsmProdutos.getInt("cd_produto_servico"), cdEmpresa, Math.abs(qtDiferenca) /*qtEntrada*/,
							                              (float)0 /*vlUnitario*/, (float)0 /*vlAcrescimo*/, (float)0 /*vlDesconto*/, rsmProdutos.getInt("cd_unidade_medida"),
							                              null /*dtEntregaPrevista*/, 0 /*cdNaturezaOperacao*/, 0 /*cdAdicao*/, 0 /*cdItem*/, (float)0, (float)0, 0));
				}
			}

			/*
			 * CRIANDO ENTRADA DE AJUSTE
			 */
			// Verifica se já existe entrada de ajuste
			if (entradas.size()>0)	{
				// Verifica se já existe entrada de ajuste
				ResultSet rs = connect.prepareStatement("SELECT cd_documento_entrada FROM alm_balanco_doc_entrada " +
														"WHERE cd_balanco = " +cdBalanco+
														"  AND cd_empresa = " +cdEmpresa).executeQuery();
				int cdDocumentoEntrada = rs.next() ? rs.getInt("cd_documento_entrada") : 0;
				// Se já não existe cria entrada
				if (cdDocumentoEntrada <= 0)	{
					DocumentoEntrada docEntrada = new DocumentoEntrada(0 /*cdDocumentoEntrada*/, cdEmpresa, 0 /*cdTransportadora*/, 0 /*cdForneecedor*/,
																   balanco.getDtBalanco() /*dtEmissao*/, balanco.getDtBalanco(),
																   DocumentoEntradaServices.ST_EM_ABERTO, 0 /*vlDesconto*/, 0 /*vlAcrescimo*/,
																   "BAL" + new DecimalFormat("0000").format(cdBalanco) /*nrDocumentoEntrada*/,
																   DocumentoEntradaServices.TP_DOC_NAO_FISCAL /*tpDocumentoEntrada*/, "" /*nrConhecimento*/,
																   DocumentoEntradaServices.ENT_AJUSTE /*tpEntrada*/, "" /*txtObservacao*/,
																   0 /*cdNaturezaOperacao*/, DocumentoEntradaServices.FRT_CIF /*tpFrete*/,
																   "" /*nrPlacaVeiculo*/, "" /*sgPlacaVeiculo*/, 0 /*qtVolumes*/, null /*dtSaidaTransportadora*/,
																   "" /*dsViaTransporte*/, "" /*txtCorpoNotalFiscal*/, 0 /*vlPesoBruto*/,
																   0 /*vlPesoLiquido*/, "" /*dsEspecieVolumes*/, "" /*dsMarcaVolumes*/, "" /*nrVolumes*/,
																   DocumentoEntradaServices.MOV_ESTOQUE_NAO_CONSIGNADO /*tpMovimentoEstoque*/,
																   0 /*cdMoeda*/, 0 /*cdTabelaPreco*/, 0 /*vlTotalDocumento*/, 0 /*cdDocumentoSaidaOrigem*/,
																   0 /*vlFrete*/, 0 /*vlSeguro*/, 0 /*cdDigitador*/, 0 /*vlTotalItens*/, 1 /*nrSerie*/);
					cdDocumentoEntrada = DocumentoEntradaDAO.insert(docEntrada, connect);
				}
				else
					BalancoDocEntradaDAO.delete(cdBalanco, cdEmpresa, cdDocumentoEntrada, connect);
				if (cdDocumentoEntrada<=0)
					throw new Exception("Erro reportado ao registrar movimento de entrada de balanço (código: " + cdDocumentoEntrada + ").");

				for (int i=0; i<entradas.size(); i++) {
					entradas.get(i).setCdDocumentoEntrada(cdDocumentoEntrada);
					Result objReturn = DocumentoEntradaItemServices.insert(entradas.get(i), 0, false /*registerTributacao*/, connect);
					if (objReturn.getCode() <= 0)	{
						Conexao.rollback(connect);
						objReturn.setMessage("Falha ao incluir item na entrada de ajuste! "+objReturn.getMessage());
						return objReturn; 
					}
				}
				
				if (BalancoDocEntradaDAO.insert(new BalancoDocEntrada(cdBalanco, cdEmpresa, cdDocumentoEntrada), connect) <= 0)
					throw new Exception("Falha ao registrar vínculo de movimento de entrada de balanço.");
			}
			/*
			 * CRIANDO SAÍDA DE AJUSTE
			 */
			if (saidas.size()>0){
				// Verifica se já existe entrada de ajuste
				ResultSet rs = connect.prepareStatement("SELECT cd_documento_saida FROM alm_balanco_doc_saida " +
														"WHERE cd_balanco = " +cdBalanco+
														"  AND cd_empresa = " +cdEmpresa).executeQuery();
				int cdDocumentoSaida = rs.next() ? rs.getInt("cd_documento_saida") : 0;
				// Se não existir cria saída 
				if(cdDocumentoSaida <= 0)	{
					DocumentoSaida docSaida = new DocumentoSaida(0 /*cdDocumentoSaida*/, 0 /*cdTransportadora*/, cdEmpresa, 0 /*cdCliente*/, balanco.getDtBalanco(),
															 DocumentoSaidaServices.ST_EM_CONFERENCIA /*stDocumentoSaida*/,
															 "BAL" + new DecimalFormat("0000").format(cdBalanco) /*nrDocumentoSaida*/,
															 DocumentoSaidaServices.TP_DOC_NAO_FISCAL /*tpDocumentoSaida*/, DocumentoSaidaServices.SAI_AJUSTE /*tpSaida*/,
															 "" /*nrConhecimento*/, 0 /*vlDesconto*/, 0 /*vlAcrescimo*/,
															 new GregorianCalendar() /*dtEmissao*/, DocumentoSaidaServices.FRT_CIF /*tpFrete*/,
															 "" /*txtMensagem*/, "" /*txtObservacao*/, "" /*nrPlacaVeiculo*/, "" /*sgPlacaVeiculo*/,
															 "" /*nrVolumes*/, null /*dtSaidaTransportadora*/, "" /*nrConhecimento*/, 0 /*cdNaturezaOperacao*/,
															 "" /*txtCorpoNotaFiscal*/, 0 /*vlPesoLiquido*/, 0 /*vlPesoBruto*/,
															 "" /*dsEspecieVolumes*/, "" /*dsMarcaVolumes*/, 0 /*qtVolumes*/,
															 DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO /*tpMovimentoEstoque*/,
															 0 /*cdVendedor*/, 0 /*cdMoeda*/, 0 /*cdReferenciaEcf*/, 0 /*cdSolicitacaoMaterial*/,
															 0 /*cdTipoOperacao*/, 0 /*vlTotalDocumento*/, 0 /*cdContrato*/, 0 /*vlFrete*/,
															 0 /*vlSeguro*/, 0 /*cdDigitador*/, 0 /*cdDocumento*/,
															 0 /*cdConta*/,0 /*cdTurno*/, 0 /*vlTotalItens*/, 1 /*nrSerie*/);
					cdDocumentoSaida = DocumentoSaidaDAO.insert(docSaida, connect);
				}
				else
					BalancoDocSaidaDAO.delete(cdBalanco, cdEmpresa, cdDocumentoSaida, connect);
				//
				if (cdDocumentoSaida<=0)
					throw new Exception("Erro reportado ao registrar movimento de saída de balanço (código: " + cdDocumentoSaida + ").");

				for (int i=0; i<saidas.size(); i++) {
					saidas.get(i).setCdDocumentoSaida(cdDocumentoSaida);
					Result objReturn = DocumentoSaidaItemServices.insert(saidas.get(i), 0, 0 /*cdLoacalDestino*/, false /*registerTributacao*/, connect);
					if (objReturn.getCode() <= 0)	{
						Conexao.rollback(connect);
						objReturn.setMessage("Falha ao incluir item na saida de ajuste! "+objReturn.getMessage());
						return objReturn; 
					}
				}

				if (BalancoDocSaidaDAO.insert(new BalancoDocSaida(cdBalanco, cdEmpresa, cdDocumentoSaida), connect) <= 0)
					throw new Exception("Falha ao registrar vínculo de movimento de saída de balanço.");
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Ajustes lançados com sucesso...\n Entradas: "+entradas.size()+"\nSaídas: "+saidas.size());
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao baixar/concluir Balanço. Anote a mensagem de erro e entre em contato " +
								  "com o administrador ou o suporte técnico.", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result verificaPendencias(int cdBalanco, int cdEmpresa) {
		return verificaPendencias(cdBalanco, cdEmpresa, null);
	}

	public static Result verificaPendencias(int cdBalanco, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa, connection);
			// Se for um balanço parcial e não houver indicação de grupo, não tem o que verificar
			if(balanco.getTpBalanco()!=TP_COMPLETO && balanco.getCdGrupo()<=0) 
				return new Result(0);
			//
			GregorianCalendar dtBalanco = balanco.getDtBalanco();
			dtBalanco.set(Calendar.HOUR_OF_DAY, 23);
			dtBalanco.set(Calendar.MINUTE, 59);
			dtBalanco.set(Calendar.SECOND, 59);
			dtBalanco.set(Calendar.MILLISECOND, 999);
			
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.cd_produto_servico, id_produto_servico, id_reduzido, nm_produto_servico, qt_precisao_unidade, " +
					"       txt_dado_tecnico, txt_especificacao, " +
					" (SELECT SUM(D.qt_saida + D.qt_saida_consignada) " +
					"  FROM alm_saida_local_item D, alm_documento_saida E " +
					"  WHERE D.cd_documento_saida = E.cd_documento_saida " +
					"    AND D.cd_empresa         = " +cdEmpresa+
					"    AND D.cd_produto_servico = A.cd_produto_servico " +
					"    AND E.dt_documento_saida <= ?) AS qt_saida, " +
					" (SELECT SUM(D.qt_entrada + D.qt_entrada_consignada) " +
					"  FROM alm_entrada_local_item D, alm_documento_entrada E " +
					"  WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
					"    AND D.cd_empresa           = " +cdEmpresa+
					"    AND D.cd_produto_servico = A.cd_produto_servico " +
					"    AND E.dt_documento_entrada <= ?) AS qt_entrada " +
					"FROM grl_produto_servico A " +
					"JOIN grl_produto                   B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"JOIN grl_produto_servico_empresa   C ON (A.cd_produto_servico = C.cd_produto_servico AND C.cd_empresa = "+cdEmpresa+") " +
					"WHERE A.tp_produto_servico = "+ProdutoServicoServices.TP_PRODUTO+
					(balanco.getTpBalanco()!=TP_COMPLETO ? " AND EXISTS (SELECT * FROM alm_produto_grupo PG " +
							                               "             WHERE PG.cd_produto_servico = C.cd_produto_servico " +
							                               "               AND PG.cd_empresa         = C.cd_empresa " +
							                               "               AND PG.cd_grupo           = "+balanco.getCdGrupo()+")" : ""));
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtBalanco));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtBalanco));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//
			pstmt = connection.prepareStatement("SELECT * FROM alm_balanco_produto_servico " +
							                    "WHERE cd_balanco = "+cdBalanco+
							                    "  AND cd_empresa = "+cdEmpresa+
							                    "  AND cd_produto_servico = ? ");
			ResultSetMap rsmPendencia = new ResultSetMap();
			while (rsm.next()) {
				float qtEstoque = rsm.getFloat("qt_entrada") - rsm.getFloat("qt_saida");
				if (Math.abs(qtEstoque) > 0.01)	{
					pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
					ResultSet rs = pstmt.executeQuery();
					if(!rs.next())	{
						HashMap<String,Object> register = new HashMap<String,Object>();
						register.put("CD_PRODUTO_SERVICO", rsm.getInt("cd_produto_servico"));
						register.put("NM_PRODUTO_SERVICO", rsm.getString("nm_produto_servico"));
						register.put("ID_PRODUTO_SERVICO", rsm.getString("id_produto_servico"));
						register.put("ID_REDUZIDO", rsm.getString("id_reduzido"));
						register.put("TXT_DADO_TECNICO", rsm.getString("txt_dado_tecnico"));
						register.put("TXT_ESPECIFICACAO", rsm.getString("txt_especificacao"));
						register.put("QT_PRECISAO_UNIDADE", rsm.getInt("qt_precisao_unidade"));
						register.put("QT_ESTOQUE", new Float(qtEstoque));
						register.put("DS_MENSAGEM", "Produto com estoque ou movimentação ausente do balanço");
						rsmPendencia.addRegister(register);
					}
				}
			}

			return new Result(rsmPendencia.size(), "Pendências foram detectadas", "rsmPendencia", rsmPendencia);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Falhar ao verificar pendências!");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result finalizarBalanco(int cdBalanco, int cdEmpresa){
		return finalizarBalanco(cdBalanco, cdEmpresa, null);
	}

	public static Result finalizarBalanco(int cdBalanco, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa, connect);
			if (balanco.getStBalanco() != ST_ANDAMENTO)
				throw new Exception("Balanço já se encontra concluído.");
			// Verifica pendências
			Result result = verificaPendencias(cdBalanco, cdEmpresa, connect);
			if(result.getCode() != 0)	{
				result.setCode(-100);
				return result; 
			}
			
			PreparedStatement pstmtEntradaAjuste = connect.prepareStatement("SELECT qt_entrada FROM alm_documento_entrada_item A " +
					                                                        "JOIN alm_balanco_doc_entrada B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
					                                                        "WHERE A.cd_produto_servico = ? "+
					                                                        "  AND B.cd_balanco         = "+cdBalanco+
					                                                        "  AND B.cd_empresa         = "+cdEmpresa);

			PreparedStatement pstmtSaidaAjuste = connect.prepareStatement("SELECT SUM(qt_saida) AS qt_saida FROM alm_documento_saida_item A " +
                    													  "JOIN alm_balanco_doc_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
                    													  "WHERE A.cd_produto_servico = ? "+
                    													  "  AND B.cd_balanco         = "+cdBalanco+
                    													  "  AND B.cd_empresa         = "+cdEmpresa);
			
			ArrayList<DocumentoEntradaItem> entradas = new ArrayList<DocumentoEntradaItem>();
			ArrayList<DocumentoSaidaItem>   saidas   = new ArrayList<DocumentoSaidaItem>();

			ResultSetMap rsmProdutos = getAllItens(cdBalanco, cdEmpresa, 0/*cdProdutoServico*/, connect);
			while (rsmProdutos.next()) {
				// Verifica se a diferença é a menor ou a maior
				float qtDiferenca = rsmProdutos.getFloat("qt_estoque") - rsmProdutos.getFloat("qt_estoque_balanco");
				// Diminuindo a entrada do ajuste
				pstmtEntradaAjuste.setInt(1, rsmProdutos.getInt("cd_produto_servico"));
				ResultSet rs = pstmtEntradaAjuste.executeQuery();
				qtDiferenca += rs.next() ? rs.getFloat("qt_entrada") : 0.0;
				// Somando saída do ajuste
				pstmtSaidaAjuste.setInt(1, rsmProdutos.getInt("cd_produto_servico"));
				rs = pstmtSaidaAjuste.executeQuery();
				qtDiferenca -= rs.next() ? rs.getFloat("qt_saida") : 0.0;
				
				if (qtDiferenca > 0.01)
					saidas.add(new DocumentoSaidaItem(0 /*cdDocumentoSaida*/, rsmProdutos.getInt("cd_produto_servico"),
							                          cdEmpresa, rsmProdutos.getFloat("qt_estoque") - rsmProdutos.getFloat("qt_estoque_balanco") /*qtSaida*/,
							                          0 /*vlUnitario*/, 0 /*vlAcrescimo*/, 0 /*vlDesconto*/, null /*dtEntregaPrevista*/,
							                          rsmProdutos.getInt("cd_unidade_medida"), 0 /*cdTabelaPreco*/, 0 /*cdItem*/,0/*cdBico*/));
				else if (Math.abs(qtDiferenca) > 0.01)
					entradas.add(new DocumentoEntradaItem(0 /*cdDocumentoEntrada*/, rsmProdutos.getInt("cd_produto_servico"),
							                              cdEmpresa, rsmProdutos.getFloat("qt_estoque_balanco") - rsmProdutos.getFloat("qt_estoque") /*qtEntrada*/,
							                              0 /*vlUnitario*/, 0 /*vlAcrescimo*/, 0 /*vlDesconto*/, rsmProdutos.getInt("cd_unidade_medida"),
							                              null /*dtEntregaPrevista*/, 0/*cdNaturezaOperacao*/, 0 /*cdAdicao*/, 0/*cdItem*/, 0/*vlVucv*/, 0 /*vlDescontoGeral*/, 0));
				
			}
			if (entradas.size()>0 || saidas.size()>0)	{
				return new Result(-1, "Não foi possível finalizar o balanço porque existem itens que necessitam de ajustes! " +
						              "\n\t"+entradas.size()+" item(ns) necessitam de uma entrada de ajuste;"+
						              "\n\t"+saidas.size()+" item(ns) necessita(m) de uma saída de ajuste;"); 
			}
			// Local de Armazenamento
			int cdLocalArmazenamento = balanco.getCdLocalArmazenamento();
			if (cdLocalArmazenamento<=0) {
				cdLocalArmazenamento = ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT", 0, cdEmpresa, connect);
				if (cdLocalArmazenamento<=0) {
					ResultSet rs = connect.prepareStatement("SELECT cd_local_armazenamento FROM alm_local_armazenamento " +
														"WHERE cd_empresa = "+cdEmpresa).executeQuery();
					cdLocalArmazenamento = !rs.next() ? 0 : rs.getInt("cd_local_armazenamento");
				}
			}

			if (cdLocalArmazenamento <= 0)
				throw new Exception("Não foi possível identificar um local de armazenamento para registro de movimentos de balanço.");
			/*
			 * LIBERANDO ENTRADA DE AJUSTE
			 */
			ResultSet rs = connect.prepareStatement("SELECT cd_documento_entrada FROM alm_balanco_doc_entrada " +
                                          			"WHERE cd_balanco = " +cdBalanco+
                                          			"  AND cd_empresa = " +cdEmpresa).executeQuery();
			if(rs.next())	{
				int cdDocumentoEntrada = rs.getInt("cd_documento_entrada");
				// Liberando entrada
				Result objReturn = DocumentoEntradaServices.liberarEntrada(cdDocumentoEntrada, cdLocalArmazenamento, connect);
				if (objReturn.getCode() <= 0)	{
					Conexao.rollback(connect);
					objReturn.setMessage("Falha ao tentar liberar entrada de ajuste! "+objReturn.getMessage());
					return objReturn; 
				}
			}
			/*
			 * LIBERANDO SAÍDA DE AJUSTE
			 */
			rs = connect.prepareStatement("SELECT cd_documento_saida FROM alm_balanco_doc_saida " +
          								  "WHERE cd_balanco = " +cdBalanco+
          								  "  AND cd_empresa = " +cdEmpresa).executeQuery();
			if (rs.next())	{
				int cdDocumentoSaida = rs.getInt("cd_documento_saida");
				// Exclui para permitir alterar algo na saída
				BalancoDocSaidaDAO.delete(cdBalanco, cdEmpresa, cdDocumentoSaida, connect);
				// Atualizando totais
				rs = connect.prepareStatement("SELECT SUM(qt_saida * vl_unitario) AS vl_total FROM alm_documento_saida_item " +
						                      "WHERE cd_documento_saida = "+cdDocumentoSaida).executeQuery();
				rs.next();		
				DocumentoSaidaServices.updatTotaisDocumentoSaida(cdDocumentoSaida, rs.getFloat("vl_total"), 0/*vlAcrescimo*/, 0/*vlDesconto*/, connect);
				// Lieberanod saída
				Result objReturn = DocumentoSaidaServices.liberarSaida(cdDocumentoSaida, cdLocalArmazenamento, connect);
				if (objReturn.getCode() <= 0)	{
					Conexao.rollback(connect);
					objReturn.setMessage("Falha ao tentar liberar saida de ajuste! "+objReturn.getMessage());
					return objReturn; 
				}
				// Incluir novamente no balanço
				BalancoDocSaidaDAO.insert(new BalancoDocSaida(cdBalanco, cdEmpresa, cdDocumentoSaida), connect);
			}
			/*
			 * ATUALIZAÇÃO O STATUS DO BALANÇO
			 */
			balanco.setStBalanco(ST_CONCLUIDO);
			if (BalancoDAO.update(balanco, connect) <= 0)
				throw new Exception("Erro reportado ao atualizar status de balanço.");

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Baixa de Balanço efetivado com sucesso...");
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao baixar/concluir Balanço. Anote a mensagem de erro e entre em contato " +
					              "com o administrador ou o suporte técnico.\n", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result updateSaldosBalanco(int cdBalanco, int cdEmpresa, ArrayList<HashMap<String, Object>> saldos){
		return updateSaldosBalanco(cdBalanco, cdEmpresa, saldos, null);
	}

	public static Result updateSaldosBalanco(int cdBalanco, int cdEmpresa, ArrayList<HashMap<String, Object>> saldos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Balanco balanco = BalancoDAO.get(cdBalanco, cdEmpresa, connect);
			if (balanco.getStBalanco()==ST_CONCLUIDO)
				throw new Exception("Balanco ja se encontra finalizado.");

			ResultSetMap rsmProdutos = getAllItens(cdBalanco, cdEmpresa, 0/*cdProdutoServico*/, connect);

			connect.prepareStatement("DELETE FROM alm_balanco_produto_servico " +
									 "WHERE cd_empresa = " +cdEmpresa+
									 "  AND cd_balanco = " +cdBalanco).execute();

			for (int i=0; saldos!=null && i<saldos.size(); i++) {
				HashMap<String, Object> saldo = (HashMap<String, Object>)saldos.get(i);
				int cdProdutoServico = (Integer)saldo.get("cdProdutoServico");
				Double vlSaldo = new Double(String.valueOf(saldo.get("vlSaldo")));

				rsmProdutos.beforeFirst();
				boolean locate = rsmProdutos.locate("cd_produto_servico", cdProdutoServico, false);
				Double vlSaldoAnt = !locate ? 0 : rsmProdutos.getDouble("qt_estoque");
				int cdUnidadeMedida = !locate ? 0 : rsmProdutos.getInt("cd_unidade_medida");

				if (cdUnidadeMedida<=0) {
					PreparedStatement pstmt = connect.prepareStatement("SELECT MAX(cd_unidade_medida) " +
							"FROM alm_documento_entrada_item " +
							"WHERE cd_empresa = ? " +
							"  AND cd_produto_servico = ?");
					pstmt.setInt(1, cdEmpresa);
					pstmt.setInt(2, cdProdutoServico);
					ResultSet rs = pstmt.executeQuery();
					int cdUnidade = !rs.next() ? 0 : rs.getInt(1);

					if (cdUnidade <= 0) {
						pstmt = connect.prepareStatement("SELECT MAX(cd_unidade_medida) " +
								"FROM alm_documento_saida_item " +
								"WHERE cd_empresa = ? " +
								"  AND cd_produto_servico = ?");
						pstmt.setInt(1, cdEmpresa);
						pstmt.setInt(2, cdProdutoServico);
						rs = pstmt.executeQuery();
						cdUnidade = !rs.next() ? 0 : rs.getInt(1);
					}
				}

				if (BalancoProdutoServicoServices.insert(new BalancoProdutoServico(cdBalanco, cdEmpresa, cdProdutoServico, vlSaldoAnt, vlSaldo, cdUnidadeMedida), connect).getCode() <= 0) {
					throw new Exception("Erros ao registrar balanço de produto.");
				}
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Saldo de Produtos de Balanço atualizado com sucesso...");
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao atualizar saldos de produtos de balanço. Anote a mensagem de erro e entre em contato " +
					              "com o administrador ou o suporte técnico.\n "+e.getMessage(), e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getProximoNrDocumento(int cdEmpresa){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect!=null;
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT COUNT(*) FROM alm_balanco WHERE EXTRACT (YEAR FROM dt_balanco) = ? AND cd_empresa = ?");
		
			pstmt.setInt(1, Util.getDataAtual().get(Calendar.YEAR));
			pstmt.setInt(2, cdEmpresa);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				return rs.getInt("COUNT") + 1;
			}
			
			return -1;
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getProximoNrDocumento2(int cdEmpresa){
		return getProximoNrDocumento2(cdEmpresa, null);
	}
	
	public static String getProximoNrDocumento2(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2("BALANCO", nrAno, cdEmpresa, connection)) <= 0)
				return null;

			return new DecimalFormat("00000").format(nrDocumento) + "/" + nrAno;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result saveBalancoCae(Balanco balanco, ResultSetMap rsmItens){
		return saveBalancoCae(balanco, rsmItens, null);
	}
	
	public static Result saveBalancoCae(Balanco balanco, ResultSetMap rsmItens, Connection connection){
		boolean isConnectionNull = connection==null;
		try {			
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			connection.setAutoCommit(false);
			Result saveBalanco = save(balanco, connection);
			
			if(saveBalanco.getCode() < 1)
				return new Result(-1, saveBalanco.getMessage());
			
			balanco = (Balanco) saveBalanco.getObjects().get("balanco");			
			Result updateBalanco = updateSaldosBalanco(balanco.getCdBalanco(), balanco.getCdEmpresa(), rsmItens.getLines(), connection);
			
			if(updateBalanco.getCode() < 1)
				return new Result(updateBalanco.getCode(), updateBalanco.getMessage());
			
			connection.commit();
			
			return new Result(updateBalanco.getCode(), updateBalanco.getMessage(), "BALANCO", balanco);
		} 
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result lancarAjustesCae(int cdBalanco, int cdEmpresa){
		return lancarAjustesCae(cdBalanco, cdEmpresa, null);
	}
	
	public static Result lancarAjustesCae(int cdBalanco, int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {			
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			int code = 0;
			int cdLocalArmazenamento = ParametroServices.getValorOfParametroAsInteger("CD_LOCAL_ARMAZENAMENTO_DEFAULT_CAE", 0, 0, connection);
			Balanco balanco          = BalancoDAO.get(cdBalanco, cdEmpresa, connection);
			
			if(balanco.getStBalanco() != 0)
				return new Result(-1, "Este balanço já obteve um ajuste no estoque.");
			
			Result lancarAjustes = lancarAjustes(cdBalanco, cdEmpresa, connection);
			
			if(lancarAjustes.getCode() < 0)
				return new Result(-2, "Houve um problema ao ajustar o balanco com o estoque.");
			
			ResultSetMap rsmDocEntrada = BalancoServices.getAllEntradasAjuste(cdBalanco, cdEmpresa, connection);
			ResultSetMap rsmDocSaida   = BalancoServices.getAllSaidasAjuste(cdBalanco, cdEmpresa, connection);
			
			while(rsmDocEntrada.next()){
				Result result = DocumentoEntradaServices.liberarEntradaBalanco(rsmDocEntrada.getInt("CD_DOCUMENTO_ENTRADA"), connection);
			}
			
			while(rsmDocSaida.next()){
				DocumentoSaidaServices.liberarSaida(rsmDocSaida.getInt("CD_DOCUMENTO_SAIDA"), cdLocalArmazenamento, connection);
			}

			balanco.setStBalanco(1);
			code = BalancoDAO.update(balanco, connection);

			if (isConnectionNull)
				connection.commit();
			
			return new Result(code, "Ajuste concluído com sucesso!", "BALANCO", balanco);
		} 
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}
