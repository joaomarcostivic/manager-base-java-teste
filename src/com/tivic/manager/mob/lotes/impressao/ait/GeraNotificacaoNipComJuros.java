package com.tivic.manager.mob.lotes.impressao.ait;

import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public class GeraNotificacaoNipComJuros extends DadosNotificacaoComJurosBase {
	
	public GeraNotificacaoNipComJuros() throws Exception {
		super();
	}
	
	@Override
	protected Search<Notificacao> searchPenalidade(SearchCriterios searchCriterios) throws Exception {
	    return searchBuilder(searchCriterios)
	    		.addField(" B.vl_infracao, B.vl_infracao AS vl_multa ")
	            .build();
	}
	
	@Override
	protected String getReportPath() {
		return "mob/np_carta_simples";
	}
}
