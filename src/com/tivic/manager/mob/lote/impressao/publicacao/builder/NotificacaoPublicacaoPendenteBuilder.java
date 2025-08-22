package com.tivic.manager.mob.lote.impressao.publicacao.builder;

import java.sql.Types;
import com.tivic.manager.mob.lote.impressao.publicacao.map.TipoPublicacaoNotificacaoMap;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.SearchCriterios;
import sol.dao.ItemComparator;

public class NotificacaoPublicacaoPendenteBuilder {
	
	private SearchCriterios searchCriterios;
	
	public NotificacaoPublicacaoPendenteBuilder() {
		this.searchCriterios = new SearchCriterios();
		this.searchCriterios.addCriterios("C.dt_publicacao_do", "", ItemComparator.ISNULL, Types.TIMESTAMP);
	}
	
	public NotificacaoPublicacaoPendenteBuilder setTpDocumento(int tpDocumento) throws ValidacaoException {
		if (tpDocumento <= 0) {
			throw new ValidacaoException("É necessário selecionar o tipo de documento.");
		}
		int tpMovimento = new TipoPublicacaoNotificacaoMap().getStatusMovimentoNotificacao(tpDocumento);
		this.searchCriterios.addCriteriosEqualInteger("C.tp_status", tpMovimento, tpMovimento > 0);
		return this;
	}
	
	public NotificacaoPublicacaoPendenteBuilder setDtMovimentoMaiorQue(String dtMovimentoInicial) {
		this.searchCriterios.addCriteriosGreaterDate("C.dt_movimento", dtMovimentoInicial, dtMovimentoInicial != null);
		return this;
	}
	
	public NotificacaoPublicacaoPendenteBuilder setDtMovimentoMenorQue(String dtMovimentoFinal) {
		this.searchCriterios.addCriteriosMinorDate("C.dt_movimento", dtMovimentoFinal, dtMovimentoFinal != null);
		return this;
	}
	
	public NotificacaoPublicacaoPendenteBuilder setParametrosPaginator(int nrPagina, int nrLimite) {
		this.searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
		this.searchCriterios.setQtLimite(nrLimite);
		return this;
	}
	
	public SearchCriterios build() {
		return this.searchCriterios;
	}

}
