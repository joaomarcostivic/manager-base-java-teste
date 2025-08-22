package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.manager.alm.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class EntradaItemAliquotaServices {

	/**
     * Busca de tributos incidentes sobre o item da entrada
     * @param criterios Criterios de busca
     * @return Conjunto de dados (ResultSetMap) com os tributos incidentes sobre o item de entrada
     */
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	/**
     * Busca de tributos incidentes sobre o item da entrada
     * @param criterios Criterios de busca
     * @param connect Conexão com o banco de dados
     * @return Conjunto de dados (ResultSetMap) com os tributos incidentes sobre o item de entrada
     */
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_tributo " +
				           "FROM adm_entrada_item_aliquota A " +
				           "JOIN adm_tributo B ON (A.cd_tributo = B.cd_tributo) ", criterios, true,
				           connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/**
     * Busca os tributos a ser calculado numa determinada operação de compra levando em consideração a
     * a origem e o produto/serviço ou classificação fiscal do qual faça parte
     * @param item Item (produto/serviço) da Saída
     * @return Impostos e alíquotas que incidem sobre a operação de venda
     */
	public static ResultSetMap calcTributos(DocumentoEntradaItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			// Instanciando Documento de Entrada
			DocumentoEntrada doc   = DocumentoEntradaDAO.get(item.getCdDocumentoEntrada(), connect);
			int cdNaturezaOperacao = doc.getCdNaturezaOperacao();
			if(item.getCdNaturezaOperacao() > 0)
				cdNaturezaOperacao = item.getCdNaturezaOperacao();
			/*
			if(doc.getStDocumentoEntrada()!=DocumentoEntradaServices.ST_EM_ABERTO)
				new Exception("Documento não aberto, não é permitido calcular impostos!");
			*/
			// Descobrindo dados do endereço
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+doc.getCdFornecedor()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1)" +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade)" +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+doc.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			// Excluindo aliquotas pre-existentes
			connect.prepareStatement("DELETE FROM adm_entrada_item_aliquota " +
					                 "WHERE cd_documento_entrada = "+item.getCdDocumentoEntrada()+
					                 "  AND cd_produto_servico   = "+item.getCdProdutoServico()+
					                 "  AND cd_empresa           = "+item.getCdEmpresa()).executeUpdate();
			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			rs = connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      "WHERE cd_produto_servico = " +item.getCdProdutoServico()).executeQuery();
			int cdClassificacaoFiscal = 0;
			if(rs.next())
				cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cálculo
			 */
			float vlBaseCalculo = (item.getQtEntrada() * item.getVlUnitario()) - item.getVlDesconto() - item.getVlDescontoGeral() + item.getVlAcrescimo();
			ResultSetMap rsm    = TributoServices.calcTributos(TributoAliquotaServices.OP_COMPRA, cdNaturezaOperacao,
					                                           cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                           vlBaseCalculo, connect);
			rsm.beforeFirst();
			while(rsm.next())	{
				EntradaItemAliquota itemAliquota = new EntradaItemAliquota(item.getCdProdutoServico(), item.getCdDocumentoEntrada(),
																			item.getCdEmpresa(), rsm.getInt("cd_tributo_aliquota"),
																			rsm.getInt("cd_tributo"), item.getCdItem(), rsm.getFloat("pr_aliquota"),
																			rsm.getFloat("vl_base_calculo"));
				int ret = EntradaItemAliquotaDAO.insert(itemAliquota, connect);
				if (ret <= 0) {
					System.out.println("FALAR AO TENTAR INCLUIR ALÍQUOTA!");
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			
			if (isConnectionNull) {
				connect.commit();
			}

			ArrayList<sol.dao.ItemComparator> crt = new ArrayList<sol.dao.ItemComparator> ();
			crt.add(new ItemComparator("A.cd_documento_entrada", String.valueOf(item.getCdDocumentoEntrada()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			crt.add(new ItemComparator("A.cd_produto_servico", String.valueOf(item.getCdProdutoServico()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			crt.add(new ItemComparator("A.cd_empresa", String.valueOf(item.getCdEmpresa()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			return EntradaItemAliquotaServices.find(crt, connect);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Metodo usado para quando se buscar um produto como item de compra, ele trazer os tributos para esse item, 
	 * baseado no produto e nas regras, para entao o usuario confirmar
     * Busca os tributos a ser calculado numa determinada operação de compra levando em consideração a
     * a origem e o produto/serviço ou classificação fiscal do qual faça parte
     * @param item Item (produto/serviço) da Entrada
     * @return Impostos e alíquotas que incidem sobre a operação de venda
     */
	public static ResultSetMap getTributos(DocumentoEntradaItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			// Instanciando Documento de Entrada
			DocumentoEntrada doc   = DocumentoEntradaDAO.get(item.getCdDocumentoEntrada(), connect);
			int cdNaturezaOperacao = doc.getCdNaturezaOperacao();
			if(item.getCdNaturezaOperacao() > 0)
				cdNaturezaOperacao = item.getCdNaturezaOperacao();
			/*
			if(doc.getStDocumentoEntrada()!=DocumentoEntradaServices.ST_EM_ABERTO)
				new Exception("Documento não aberto, não é permitido calcular impostos!");
			*/
			// Descobrindo dados do endereço
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+doc.getCdFornecedor()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1)" +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade)" +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+doc.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			rs = connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      "WHERE cd_produto_servico = " +item.getCdProdutoServico()).executeQuery();
			int cdClassificacaoFiscal = 0;
			if(rs.next())
				cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cálculo
			 */
			float vlBaseCalculo = (item.getQtEntrada() * item.getVlUnitario()) - item.getVlDesconto() - item.getVlDescontoGeral() + item.getVlAcrescimo();
			ResultSetMap rsm    = TributoServices.calcTributos(TributoAliquotaServices.OP_COMPRA, cdNaturezaOperacao,
					                                           cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                           vlBaseCalculo, connect);
			rsm.beforeFirst();
			
			int cdTributoICMS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
			int cdTributoIPI 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0);
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			int cdTributoII     = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0);
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			while(rsm.next())	{
				
				if(rsm.getInt("cd_tributo") == cdTributoICMS){
					rsmFinal.setValueToField("cd_tributo_icms", cdTributoICMS);
					rsmFinal.setValueToField("cd_tributo_aliquota_icms", rsm.getInt("cd_tributo_aliquota"));
					rsmFinal.setValueToField("pr_aliquota_icms", rsm.getFloat("pr_aliquota"));
					rsmFinal.setValueToField("vl_base_calculo_icms", rsm.getFloat("vl_base_calculo"));
				}
				
				if(rsm.getInt("cd_tributo") == cdTributoIPI){
					rsmFinal.setValueToField("cd_tributo_ipi", cdTributoIPI);
					rsmFinal.setValueToField("cd_tributo_aliquota_ipi", rsm.getInt("cd_tributo_aliquota"));
					rsmFinal.setValueToField("pr_aliquota_ipi", rsm.getFloat("pr_aliquota"));
					rsmFinal.setValueToField("vl_base_calculo_ipi", rsm.getFloat("vl_base_calculo"));
				}
				
				if(rsm.getInt("cd_tributo") == cdTributoPIS){
					rsmFinal.setValueToField("cd_tributo_pis", cdTributoPIS);
					rsmFinal.setValueToField("cd_tributo_aliquota_pis", rsm.getInt("cd_tributo_aliquota"));
					rsmFinal.setValueToField("pr_aliquota_pis", rsm.getFloat("pr_aliquota"));
					rsmFinal.setValueToField("vl_base_calculo_pis", rsm.getFloat("vl_base_calculo"));
				}
				
				if(rsm.getInt("cd_tributo") == cdTributoCOFINS){
					rsmFinal.setValueToField("cd_tributo_cofins", cdTributoCOFINS);
					rsmFinal.setValueToField("cd_tributo_aliquota_cofins", rsm.getInt("cd_tributo_aliquota"));
					rsmFinal.setValueToField("pr_aliquota_cofins", rsm.getFloat("pr_aliquota"));
					rsmFinal.setValueToField("vl_base_calculo_cofins", rsm.getFloat("vl_base_calculo"));
				}
				
				if(rsm.getInt("cd_tributo") == cdTributoII){
					rsmFinal.setValueToField("cd_tributo_ii", cdTributoII);
					rsmFinal.setValueToField("cd_tributo_aliquota_ii", rsm.getInt("cd_tributo_aliquota"));
					rsmFinal.setValueToField("pr_aliquota_ii", rsm.getFloat("pr_aliquota"));
					rsmFinal.setValueToField("vl_base_calculo_ii", rsm.getFloat("vl_base_calculo"));
				}
			}
			
			return rsmFinal;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insert(EntradaItemAliquota objeto) {
		return insert(objeto, null);
	}

	public static Result insert(EntradaItemAliquota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_item_aliquota" +
														"			WHERE cd_produto_servico = ? " +
														"				AND cd_documento_entrada = ? " +
														"				AND cd_empresa = ? " +
														"				AND cd_tributo = ?" +
														"				AND cd_item = ?");
			pstmt.setInt(1, objeto.getCdProdutoServico());
			pstmt.setInt(2, objeto.getCdDocumentoEntrada());
			pstmt.setInt(3, objeto.getCdEmpresa());
			pstmt.setInt(4, objeto.getCdTributo());
			pstmt.setInt(5, objeto.getCdItem());
			if(pstmt.executeQuery().next()){
				Tributo tributo = TributoDAO.get(objeto.getCdTributo(), connect);
				Conexao.rollback(connect);
				return new Result(-1, "Ja existe tributação de "+tributo.getNmTributo()+" para esse item!");
			}
			
			int code = EntradaItemAliquotaDAO.insert(objeto, connect);
			
			if(code <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir aliquota no item!");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Aliquota salva com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro EntradaItemAliquotaServices.insert: " + e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result update(EntradaItemAliquota objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static Result update(EntradaItemAliquota objeto, int cdProdutoServicoOld, int cdDocumentoEntradaOld, int cdEmpresaOld, int cdTributoAliquotaOld, int cdTributoOld, int cdItemOld) {
		return update(objeto, cdProdutoServicoOld, cdDocumentoEntradaOld, cdEmpresaOld, cdTributoAliquotaOld, cdTributoOld, cdItemOld, null);
	}

	public static Result update(EntradaItemAliquota objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static Result update(EntradaItemAliquota objeto, int cdProdutoServicoOld, int cdDocumentoEntradaOld, int cdEmpresaOld, int cdTributoAliquotaOld, int cdTributoOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_item_aliquota" +
														"			WHERE cd_produto_servico = ? " +
														"				AND cd_documento_entrada = ? " +
														"				AND cd_empresa = ? " +
														"				AND cd_tributo = ?" +
														"				AND cd_item = ?");
			pstmt.setInt(1, objeto.getCdProdutoServico());
			pstmt.setInt(2, objeto.getCdDocumentoEntrada());
			pstmt.setInt(3, objeto.getCdEmpresa());
			pstmt.setInt(4, objeto.getCdTributo());
			pstmt.setInt(5, objeto.getCdItem());
			if(pstmt.executeQuery().next() && objeto.getCdTributo() != cdTributoOld){
				Tributo tributo = TributoDAO.get(objeto.getCdTributo(), connect);
				Conexao.rollback(connect);
				return new Result(-1, "Ja existe tributação de "+tributo.getNmTributo()+" para esse item!");
			}
			int code = EntradaItemAliquotaDAO.update(objeto, cdProdutoServicoOld, cdDocumentoEntradaOld, cdEmpresaOld, cdTributoAliquotaOld, cdTributoOld, cdItemOld, connect);
			if(code <= 0){
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar aliquota no item!");
			}
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Aliquota atualizada com sucesso!");
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro EntradaItemAliquotaServices.update: " + e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByItem(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdItem) {
		return getAllByItem(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdItem, null);
	}

	public static ResultSetMap getAllByItem(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, C.nm_tributo, D.* " +
					"FROM adm_entrada_item_aliquota A, adm_tributo_aliquota B, adm_tributo C, fsc_situacao_tributaria D " +
					"WHERE A.cd_situacao_tributaria = D.cd_situacao_tributaria " +
					"  AND A.cd_tributo = D.cd_tributo " +
					"  AND A.cd_tributo_aliquota = B.cd_tributo_aliquota " +
					"  AND A.cd_tributo = B.cd_tributo " +
					"  AND B.cd_tributo = C.cd_tributo " +
					"  AND A.cd_produto_servico = ? " +
					"  AND A.cd_documento_entrada = ? " +
					"  AND A.cd_empresa = ? " +
					"  AND A.cd_item = ? ");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoEntrada);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaServices.getAllByItem: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
