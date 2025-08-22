package com.tivic.manager.tasks.limpeza.lotes;

import java.sql.Types;

import com.tivic.manager.tasks.limpeza.lotes.exceptions.LimpezaLoteDataMaiorException;
import com.tivic.manager.tasks.limpeza.lotes.exceptions.LimpezaSemCriteriosException;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class SearchCriteriosLoteImpressaoBuilder {

	private SearchCriterios searchCriterios;
	
	public SearchCriteriosLoteImpressaoBuilder(LimpezaLotesDTO limpezaLotesDTO) throws LimpezaSemCriteriosException, LimpezaLoteDataMaiorException {
		this.searchCriterios = new SearchCriterios();
		boolean limpezaSemCriterios = true;
		if(limpezaLotesDTO.getDtCriacao() != null) {
			if(limpezaLotesDTO.getDtCriacao().after(DateUtil.getDataAtual()))
				throw new LimpezaLoteDataMaiorException();
			searchCriterios.addCriteriosEqualOnlyDate("dt_criacao", DateUtil.formatDate(limpezaLotesDTO.getDtCriacao(), "yyyy-MM-dd"));
			limpezaSemCriterios = false;
		}
		if(!limpezaLotesDTO.getCodigosLote().isEmpty()) {
			searchCriterios.addCriterios("cd_lote_impressao", limpezaLotesDTO.getCodigosLote().toString().substring(1, limpezaLotesDTO.getCodigosLote().toString().length()-1), ItemComparator.IN, Types.INTEGER);
			limpezaSemCriterios = false;
		}
		if(limpezaLotesDTO.getLimite() > 0) {
			searchCriterios.setQtLimite(limpezaLotesDTO.getLimite());
			limpezaSemCriterios = false;
		}
		
		if(limpezaSemCriterios)
			throw new LimpezaSemCriteriosException();
	}
	
	public SearchCriterios build() {
		return this.searchCriterios;
	}
	
	
}
