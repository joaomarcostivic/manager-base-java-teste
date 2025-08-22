package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BuscarAitsParaLoteImpressaoNai implements IBuscarAitsParaLoteImpressao {

	@Override
	public Search<AitDTO> buscarAitsParaLoteImpressao(int quantidadeAit) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(quantidadeAit);
		return searchAits(searchCriterios);
	}
	
	public Search<AitDTO> searchAits(SearchCriterios searchCriterios) throws Exception {
		Search<AitDTO> search = new SearchBuilder<AitDTO>("MOB_AIT A")
				.fields("A.id_ait, A.nr_placa, A.dt_infracao, A.cd_ait, K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran ")
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait)")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" NOT EXISTS "
								   + " ("   
								   + " 	SELECT K.cd_ait FROM mob_ait_movimento K"
								   + " 		WHERE"
								   + "			("
								   + "				K.tp_status IN (" + TipoStatusEnum.NIP_ENVIADA.getKey()
								   + "				," + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
								   + "				, " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
								   + "				, " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
								   + "				, " + TipoStatusEnum.CANCELAMENTO_MULTA.getKey()
								   + "				, " + TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey()
								   + "				, " + TipoStatusEnum.DEVOLUCAO_PAGAMENTO.getKey()
								   + "				)"
								   + "			) AND K.cd_ait = A.cd_ait "
								   + "	)"
								   + "	AND NOT EXISTS("
								   + "		SELECT D.cd_lote_impressao, "
								   + "		D.tp_lote_impressao, "
								   + "		E.cd_ait "
								   + "	FROM mob_lote_impressao D"
								   + "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao)"
								   + "		WHERE"
								   + "			("
								   + "				D.tp_documento IN (" + TipoLoteNotificacaoEnum.LOTE_NAI.getKey() + ", "
								   + "								   " + TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey()
								   + "			) )"
								   + "	AND E.cd_ait = A.cd_ait "
								   + "	)"
								   + " " + incluirCondicionalAdesaoSne()
								   + " ")
				.orderBy("A.dt_infracao DESC")
				.count()
				.build();
		return search;
	}
	
	private SearchCriterios montarCriterios(int quantidadeDocumento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("K.tp_status", AitMovimentoServices.NAI_ENVIADO, true);
		searchCriterios.addCriteriosEqualInteger("K.lg_enviado_detran", AitMovimentoServices.REGISTRADO, true);
		searchCriterios.setQtLimite(quantidadeDocumento);
		return searchCriterios;
	}
	
	private String incluirCondicionalAdesaoSne() throws Exception {
		int sneAdesaoOrgaoAutuador = ParametroServices.getValorOfParametroAsInteger("MOB_SNE_ADESAO_ORGAO_AUTUADOR", 0);
		if(sneAdesaoOrgaoAutuador == TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey()) {
			return " AND NOT EXISTS( SELECT L.cd_ait FROM mob_ait_movimento L"
	                    + " WHERE("
	                    + " L.st_adesao_sne = " + TipoAdesaoSneEnum.COM_OPCAO_SNE.getKey() 
	                    + " AND L.tp_status = " + TipoStatusEnum.NAI_ENVIADO.getKey()
	                    + ") AND  L.cd_ait = A.cd_ait)";
	    }
		return "";
	}
	
	@Override
	public Search<AitDTO> montarSearchLoteImpressao(SearchCriterios searchCriterios) throws Exception {
		searchCriterios.addCriteriosEqualInteger("K.lg_enviado_detran", AitMovimentoServices.REGISTRADO, true);
		montarCriterios(searchCriterios.getQtLimite());
		return searchAits(searchCriterios);
	}

}
