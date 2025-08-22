package com.tivic.manager.mob.lotes.builders.publicacao;

import java.sql.Types;

import com.tivic.manager.mob.lotes.publicacao.map.TipoPublicacaoMap;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class NotificacaoPublicacaoPendenteSearchBuilder {
	
	private SearchCriterios searchCriterios;
	
	public NotificacaoPublicacaoPendenteSearchBuilder() {
		this.searchCriterios = new SearchCriterios();
		this.searchCriterios.addCriterios("C.dt_publicacao_do", "", ItemComparator.ISNULL, Types.TIMESTAMP);
	}
	
	public NotificacaoPublicacaoPendenteSearchBuilder setTpPublicacao(int tpPubliacao) throws ValidacaoException {
		if (tpPubliacao <= 0) {
			throw new ValidacaoException("É necessário selecionar o tipo de publicação.");
		}
		int tpMovimento = new TipoPublicacaoMap().getStatusMovimentoNotificacao(tpPubliacao);
		this.searchCriterios.addCriteriosEqualInteger("C.tp_status", tpMovimento, tpMovimento > 0);
		return this;
	}
	
	public NotificacaoPublicacaoPendenteSearchBuilder setDtMovimentoMaiorQue(String dtMovimentoInicial) {
		this.searchCriterios.addCriteriosGreaterDate("C.dt_movimento", dtMovimentoInicial, dtMovimentoInicial != null);
		return this;
	}
	
	public NotificacaoPublicacaoPendenteSearchBuilder setDtMovimentoMenorQue(String dtMovimentoFinal) {
		this.searchCriterios.addCriteriosMinorDate("C.dt_movimento", dtMovimentoFinal, dtMovimentoFinal != null);
		return this;
	}
	
	public NotificacaoPublicacaoPendenteSearchBuilder setParametrosPaginator(int nrPagina, int nrLimite) {
		this.searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
		this.searchCriterios.setQtLimite(nrLimite);
		return this;
	}
	
	public SearchCriterios build() {
		return this.searchCriterios;
	}

}
