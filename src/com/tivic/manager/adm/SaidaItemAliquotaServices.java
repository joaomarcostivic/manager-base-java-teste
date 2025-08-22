package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class SaidaItemAliquotaServices {

	/**
     * Busca de tributos incidentes sobre o item da saída
     * @param criterios Criterios de busca
     * @return Conjunto de dados (ResultSetMap) com os tributos incidentes sobre o item de saída
     */
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	/**
     * Busca de tributos incidentes sobre o item da saída
     * @param criterios Criterios de busca
     * @param connect Conexão com o banco de dados
     * @return Conjunto de dados (ResultSetMap) com os tributos incidentes sobre o item de saída
     */
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_tributo " +
				           "FROM adm_saida_item_aliquota A " +
				           "JOIN adm_tributo B ON (A.cd_tributo = B.cd_tributo) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/**
     * Busca os tributos a ser calculado numa determinada operação de venda levando em consideração a
     * o destino e o produto/serviço ou grupo do qual faça parte
     * @param item Item (produto/serviço) da Saída
     * @return Impostos e alíquotas que incidem sobre a operação de venda
     */
	public static ResultSetMap calcTributos(DocumentoSaidaItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Instanciando Documento de Saída
			 */
			DocumentoSaida doc = DocumentoSaidaDAO.get(item.getCdDocumentoSaida(), connect);
			if(doc.getStDocumentoSaida()!=DocumentoSaidaServices.ST_EM_CONFERENCIA)
				new Exception("Documento não aberto, não é permitido calcular impostos!");
			/*
			 *  Descobrindo dados do endereço
			 */
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+doc.getCdCliente()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
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
			 *  Excluindo aliquotas pre-existentes
			 */
			connect.prepareStatement("DELETE FROM adm_saida_item_aliquota " +
					                 "WHERE cd_documento_saida = "+item.getCdDocumentoSaida()+
					                 "  AND cd_produto_servico = "+item.getCdProdutoServico()).executeUpdate();
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
			float vlBaseCalculo = (item.getQtSaida() * item.getVlUnitario()) - item.getVlDesconto() + item.getVlAcrescimo();
			ResultSetMap rsm = TributoServices.calcTributos(TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				if (SaidaItemAliquotaDAO.delete(item.getCdProdutoServico(), item.getCdDocumentoSaida(), item.getCdEmpresa(),
						rsm.getInt("cd_tributo_aliquota"), rsm.getInt("cd_tributo"), item.getCdItem(), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
				SaidaItemAliquota itemAliquota = new SaidaItemAliquota(item.getCdProdutoServico(),
						item.getCdDocumentoSaida(),
						item.getCdEmpresa(),
						rsm.getInt("cd_tributo_aliquota"),
						rsm.getInt("cd_tributo"),
						rsm.getFloat("pr_aliquota"),
						rsm.getFloat("vl_base_calculo"),
						item.getCdItem());
				if (SaidaItemAliquotaDAO.insert(itemAliquota, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return null;
				}
			}
			if (isConnectionNull)
				connect.commit();
			/* Pesquisa impostos gerados*/
			ArrayList<sol.dao.ItemComparator> crt = new ArrayList<sol.dao.ItemComparator> ();
			crt.add(new ItemComparator("A.cd_documento_saida", String.valueOf(item.getCdDocumentoSaida()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			crt.add(new ItemComparator("A.cd_produto_servico", String.valueOf(item.getCdProdutoServico()), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			rsm = SaidaItemAliquotaServices.find(crt, connect);
			return rsm;
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
     * Pesquisa e retorno a alíquota de um determinado tributo sobre a operação de venda
     * @category PDV
     * @param cdDocumentoSaida Código do documento de saída
     * @param cdProdutoServico Código do produto ou serviço
     * @param cdTributo Código do tributo específico do qual se deseja saber a alíquota incidente sobre a operação
     * @return Alíquota formatada com 4 dígitos sem vírgula + situação tributária, no padrão do ECF, com 2 dígitos
     *               ex: 0017TT = Alíquota 17,00% ST = Trib. Integralmente
     */
	public static String getAliquotaOfTributo(int cdDocumentoSaida, int cdProdutoServico, int cdTributo, float vlBaseCalculo) {
		return getAliquotaOfTributo(cdDocumentoSaida, cdProdutoServico, cdTributo, vlBaseCalculo, null);
	}
	public static String getAliquotaOfTributo(int cdDocumentoSaida, int cdProdutoServico, int cdTributo, float vlBaseCalculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			// Instanciando Documento de Saída
			DocumentoSaida doc = DocumentoSaidaDAO.get(cdDocumentoSaida, connect);
			// Descobrindo dados do endereço
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa = "+doc.getCdCliente()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade) " +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+doc.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			/*
			 *  Calcula e soma alíquotas
			 */
			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			rs = connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      "WHERE cd_produto_servico = " +cdProdutoServico).executeQuery();
			int cdClassificacaoFiscal = 0;
			if(rs.next())
				cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cálculo
			 */
			ResultSetMap rsm = TributoServices.calcTributos(TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, cdProdutoServico, cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			float prAliquota = 0;
			int stTributaria = TributoAliquotaServices.ST_IGNORADA;
			if(rsm.locate("cd_tributo", cdTributo))	{
				stTributaria = rsm.getInt("st_tributaria");
				prAliquota   = rsm.getFloat("pr_aliquota");
			}
			return Util.fillNum(Math.round(prAliquota * 100), 4)+TributoAliquotaServices.situacaoTributariaEcf[stTributaria];
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return "";
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * Retorna o string com os dados referentes aos impostos calculados segundo a tabela IBPTax 0.0.2
	 * @param cdDocumentoSaida   documento de saida / venda
	 * @param cdEmpresa			 codigo da empresa que realizou a venda
	 * @return
	 * @author Joao Marlon Souto Ferraz
	 */
	public static String getAliquotaOfDocSaida(int cdDocumentoSaida, int cdEmpresa, float vlTotalDocumento) {
		return getAliquotaOfDocSaida(cdDocumentoSaida, cdEmpresa, vlTotalDocumento, false, null);
	}
	public static String getAliquotaOfDocSaida(int cdDocumentoSaida, int cdEmpresa, double vlTotalDocumento) {
		return getAliquotaOfDocSaida(cdDocumentoSaida, cdEmpresa, vlTotalDocumento, false, null);
	}
	public static String getAliquotaOfDocSaida(int cdDocumentoSaida, int cdEmpresa, double vlTotalDocumento, boolean isNFe) {
		return getAliquotaOfDocSaida(cdDocumentoSaida, cdEmpresa, vlTotalDocumento, isNFe, null);
	}
	public static String getAliquotaOfDocSaida(int cdDocumentoSaida, int cdEmpresa, double vlTotalDocumento, boolean isNFe, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			double vlTotalAliquota  = 0;
			double prTotalAliquota  = 0;
			ResultSet rs;
			// Buscando dados referentes aos produtos lancados no documento de saida
			if (!isNFe){
				rs = connect.prepareStatement("SELECT A.cd_documento_saida, C.cd_produto_servico, E.nr_ncm, E.vl_aliquota_nacional,  " +
													"       E.vl_aliquota_importado, C.tp_produto_servico,  " +
													"		SUM((B.vl_unitario + B.vl_acrescimo - B.vl_desconto) * B.qt_saida) AS vl_total_item " +
													"FROM alm_documento_saida A " +
													"LEFT OUTER JOIN alm_documento_saida_item B    ON (A.cd_documento_saida = B.cd_documento_saida) " +
													"LEFT OUTER JOIN grl_produto_servico C         ON (B.cd_produto_servico = C.cd_produto_servico) " +
													"LEFT OUTER JOIN grl_produto_servico_empresa D ON (C.cd_produto_servico = D.cd_produto_servico  " +
													"					      					   AND B.cd_empresa         = D.cd_empresa) "+
													"LEFT OUTER JOIN grl_ncm E                     ON (C.cd_ncm = E.cd_ncm) "+
													" WHERE    B.cd_empresa         = " + cdEmpresa +
													"   AND    A.cd_documento_saida = " + cdDocumentoSaida+
													" GROUP BY A.cd_documento_saida, C.cd_produto_servico, E.nr_ncm, E.vl_aliquota_nacional, " +
													"          E.vl_aliquota_importado, C.tp_produto_servico ").executeQuery();
			}else{
			// Buscando dados referentes aos produtos lancados na nota fiscal
				rs = connect.prepareStatement("SELECT B.cd_documento_saida, C.cd_produto_servico, E.nr_ncm, E.vl_aliquota_nacional,  " +
					"       E.vl_aliquota_importado, C.tp_produto_servico,  " +
					"		SUM(B.vl_unitario + B.vl_acrescimo - B.vl_desconto) AS vl_total_item " +
					"FROM fsc_nota_fiscal A " +
					"LEFT OUTER JOIN fsc_nota_fiscal_item B        ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
					"LEFT OUTER JOIN grl_produto_servico C         ON (B.cd_produto_servico = C.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_produto_servico_empresa D ON (C.cd_produto_servico = D.cd_produto_servico  " +
					"					      					   AND B.cd_empresa         = D.cd_empresa) "+
					"LEFT OUTER JOIN grl_ncm E                     ON (C.cd_ncm = E.cd_ncm) "+
					" WHERE    B.cd_empresa         = " + cdEmpresa +
					"   AND    B.cd_documento_saida = " + cdDocumentoSaida+
					" GROUP BY B.cd_documento_saida, C.cd_produto_servico, E.nr_ncm, E.vl_aliquota_nacional, " +
					"          E.vl_aliquota_importado, C.tp_produto_servico ").executeQuery();
			}
			while(rs.next())	{
				// somando valor em R$ da aliquota
				if (rs.getInt("tp_produto_servico") == ProdutoServicoEmpresaServices.NACIONAL)
					vlTotalAliquota += (rs.getDouble("vl_total_item") * (rs.getDouble("vl_aliquota_nacional") > 0 ? rs.getDouble("vl_aliquota_nacional") : 31.5));
				else
					vlTotalAliquota += (rs.getDouble("vl_total_item") * (rs.getDouble("vl_aliquota_importado") > 0? rs.getDouble("vl_aliquota_importado") : 35.5));
				//vlTotalDocumento += rs.getDouble("vl_total_item");
			}
			// gravando total da nota na variavel
			//PreparedStatement pstmt = connect.prepareStatement("SELECT vl_total_documento FROM alm_documento_saida WHERE cd_documento_saida = " + cdDocumentoSaida);
			//ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			//while (rsm.next())
//				vlTotalDocumento = rsm.getDouble("vl_total_documento");
			//
			//vlTotalDocumento = rs.getFloat("vl_total_documento");
			vlTotalAliquota = vlTotalAliquota/100;
			// Calculando Percentual dos impostos pagos em relacao a compra.
			prTotalAliquota = (vlTotalAliquota/vlTotalDocumento) * 100;			
			return "Val Aprox Tributos R$"+Util.formatNumber(Util.arredondar(vlTotalAliquota,2)) + "("+Util.formatNumber(Util.arredondar(prTotalAliquota,2)) + "%) Fonte: IBPT";
		}catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return "";
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
