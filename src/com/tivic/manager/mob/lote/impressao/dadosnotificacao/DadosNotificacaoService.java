package com.tivic.manager.mob.lote.impressao.dadosnotificacao;

import java.util.List;

import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class DadosNotificacaoService implements IDadosNotificacaoService {

	@Override
	public List<DadosNotificacao> buscarDadosNAI(int cdAit, CustomConnection customConnection) throws Exception {
		List<DadosNotificacao> dadosNotificacao = new DadosNotificacaoNAI()
				.search(montarCriterios(cdAit), customConnection)
				.getList(DadosNotificacao.class);
		return dadosNotificacao;
	}

	@Override
	public List<DadosNotificacao> buscarDadosNIP(int cdAit, CustomConnection customConnection) throws Exception {
		List<DadosNotificacao> dadosNotificacao = new DadosNotificacaoNIP()
				.search(montarCriterios(cdAit), customConnection)
				.getList(DadosNotificacao.class);
		return dadosNotificacao;
	}
	
	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}

}
