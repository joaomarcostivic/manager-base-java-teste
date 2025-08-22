package com.tivic.manager.ptc.protocolos.julgamento;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BuscarAitsParaLoteJariDeferida implements IBuscarAitsParaLoteJulgamento {
	
	@Override
	public Search<AitDTO> buscarAitsParaLoteJulgamento(int quantidadeAit) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(quantidadeAit);
		return searchAits(searchCriterios);
	}
	
	@Override
	public Search<AitDTO> montarSearchLoteJulgamento(SearchCriterios searchCriterios) throws Exception {
		searchCriterios.addCriteriosEqualInteger("K.tp_status", AitMovimentoServices.JARI_COM_PROVIMENTO, true);
		montarCriterios(searchCriterios.getQtLimite());
		return searchAits(searchCriterios);
	}
	
	public Search<AitDTO> searchAits(SearchCriterios searchCriterios) throws Exception {
		Search<AitDTO> search = new SearchBuilder<AitDTO>("MOB_AIT A")
				.fields("A.cd_ait, A.id_ait, A.dt_infracao, A.nr_placa, A.tp_status, K.dt_movimento, K.tp_status, K.nr_processo")
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento K ON (A.cd_ait = K.cd_ait)")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" NOT EXISTS"
								   + " ("   
								   + " 		SELECT tp_status FROM mob_ait_movimento K"
								   + " 		WHERE"
								   + "			("
								   + "				tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
								   + "				OR tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
								   + "				OR tp_status = " + AitMovimentoServices.CANCELAMENTO_PONTUACAO
								   + "				"
								   + "			)"
								   + "		AND K.cd_ait = A.cd_ait"
								   + "	)"
								   + "	AND NOT EXISTS"
								   + "	("
								   + "		SELECT D.cd_lote_impressao, E.cd_ait FROM mob_lote_impressao D"
								   + "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao)"
								   + "		WHERE"
								   + "			("
								   + "				D.tp_impressao = " + TipoLoteJulgamentoEnum.LOTE_JARI_COM_PROVIMENTO.getKey()
								   + "			)"
								   + "	AND E.cd_ait = A.cd_ait"
								   + "	)")
				.orderBy("A.DT_INFRACAO DESC")
				.count()
				.build();
		return search;
	}
	
	private SearchCriterios montarCriterios(int quantidadeDocumento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("K.tp_status", AitMovimentoServices.JARI_COM_PROVIMENTO, true);
		searchCriterios.setQtLimite(quantidadeDocumento);
		return searchCriterios;
	}
}
