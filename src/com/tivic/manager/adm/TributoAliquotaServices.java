package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fsc.SituacaoTributariaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.Search;

public class TributoAliquotaServices {
	/* situacao tributaria */
	public static final int ST_IGNORADA 			   = 0;
	public static final int ST_TRIBUTACAO_INTEGRAL 	   = 1;
	public static final int ST_SUBSTITUICAO_TRIBUTARIA = 2;
	public static final int ST_ISENTO 				   = 3;
	public static final int ST_SEM_TRIBUTACAO 		   = 4;

	/* tipo de operação */
	public static final int OP_IGNORADA = 0;
	public static final int OP_COMPRA 	= 1;
	public static final int OP_VENDA 	= 2; 

	/* tipo de fator de aplição */
	public static final int FT_VALOR        = 0;
	public static final int FT_PERCENTUAL 	= 1;

	public static final String[] tipoFatorVariacaoBase = {"Valor", "Percentual"};

	public static final String[] tipoOperacao          = {"Ignorada", "Compra", "Venda"};

	public static final String[] situacaoTributaria    = {"Ignorada", "Tributada Integralmente", "Substituição tributária", "Isento", "Não tributado"};

	public static final String[] situacaoTributariaEcf = {"XX","TT","FF","II","NN"};

	public static final int BC_MVA            = 0;
	public static final int BC_PAUTA          = 1;
	public static final int BC_PRECO_TABELADO = 2;
	public static final int BC_VALOR_OPERACAO = 3;
	public static final int BC_LISTA_NEGATIVA = 4;
	public static final int BC_LISTA_POSITIVA = 5;
	public static final int BC_LISTA_NEUTRA   = 6;

	/* tipo de desoneracao */
	public static final int DES_TAXI          = 1;
	public static final int DES_DEF_FIS       = 2;
	public static final int DES_PROD_AGRO     = 3;
	public static final int DES_FROT_LOC      = 4;
	public static final int DES_DIPLO_CONS    = 5;
	public static final int DES_UTIL_MOT      = 6;
	public static final int DES_SUFRAMA       = 7;
	public static final int DES_VEN_ORG_PUB   = 8;
	public static final int DES_OUTROS        = 9;
	
	public static final String[] motivoDesoneracao = {"Selecione...", "Táxi", "Deficiente Físico", "Produto Agropecurário", "Frotista / Locadora", "Diplomático / Consular",
		                                              "Utilit. ou Motoc. da Amazônia Ocid. e Áreas de Livre Comércio", "SUFRAMA", "Venda a Órgão Públicos", "Outros"};
	
	public static final String[] modalidadeBaseCalculo = {"Margem de Valor Agregado - MVA (%)", "Pauta (Valor)", "Preço Tabelado ou Máx. Sugerido (valor)", 
                                                          "Valor da Operação"};
	
	public static final String[] modalidadeBaseCalculoST = {"Margem de Valor Agregado - MVA (%)", "Pauta (Valor)", "Preço Tabelado ou Máx. Sugerido (valor)", 
		                                                    "Valor da Operação", "Lista Negativa (valor)", "Lista Positiva (valor)", "Lista Neutra (valor)"};

	public static ResultSetMap getAllProdutoServicosOfAliquota(int cdTributo, int cdAtributoAliquota) {
		return getAllProdutoServicosOfAliquota(cdTributo, cdAtributoAliquota, null);
	}

	public static ResultSetMap getAllProdutoServicosOfAliquota(int cdTributo, int cdAtributoAliquota, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull){
				connection = Conexao.conectar();
			}
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_cidade, C.nm_estado, d.nm_pais, E.nr_codigo_fiscal, E.nm_natureza_operacao, " +
					"       F.id_classificacao_fiscal, F.nm_classificacao_fiscal " +
					"FROM adm_produto_servico_tributo A " +
					"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado C ON (A.cd_estado = C.cd_estado) " +
					"LEFT OUTER JOIN grl_pais   D ON (A.cd_pais = D.cd_pais) " +
					"LEFT OUTER JOIN adm_natureza_operacao E ON (A.cd_natureza_operacao = E.cd_natureza_operacao) " +
					"LEFT OUTER JOIN adm_classificacao_fiscal F ON (A.cd_classificacao_fiscal = F.cd_classificacao_fiscal) " +
					"WHERE A.cd_tributo = " +cdTributo+
					"  AND A.cd_tributo_aliquota = " +cdAtributoAliquota+
					"ORDER BY nr_codigo_fiscal, nm_classificacao_fiscal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		int cdTributo = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
		ResultSetMap rsm = Search.find("SELECT A.*, B.* FROM adm_tributo_aliquota A " +
				"LEFT OUTER JOIN fsc_situacao_tributaria B ON (A.cd_situacao_tributaria = B.cd_situacao_tributaria" +
				"                                          AND A.cd_tributo             = B.cd_tributo)" +
				" WHERE 1=1 AND A.cd_tributo = " + cdTributo + " ", criterios, true,
				connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			rsm.setValueToField("DS_ST_TRIBUTARIA", SituacaoTributariaServices.tipoSituacaoTributaria[rsm.getInt("st_tributaria")]);
			rsm.setValueToField("DS_TP_OPERACAO", SituacaoTributariaServices.tipoOperacao[rsm.getInt("tp_operacao")]);
			rsm.setValueToField("CL_RESULTADO", (rsm.getString("DS_TP_OPERACAO") != null ? rsm.getString("DS_TP_OPERACAO") : "SEM TIPO DE OPERAÇÃO") + " - " + (rsm.getString("nr_situacao_tributaria") != null ? rsm.getString("nr_situacao_tributaria") : "SEM CST/CSOSN") + " - " + (rsm.getString("DS_ST_TRIBUTARIA") != null ? rsm.getString("DS_ST_TRIBUTARIA") : "SEM SITUAÇÃO TRIBUTÁRIA") + " - " + Util.formatNumber(rsm.getFloat("pr_aliquota")));
		}
		rsm.beforeFirst();
		return rsm;
	}
}
