package com.tivic.manager.fix.mob.ait.cidade;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class SearchCriteriosFixCidadeBuilder {
	SearchCriterios searchCriterios = new SearchCriterios();
	
	public SearchCriteriosFixCidadeBuilder setDtInfracao(String nmCampoTabela, String dtInfracao) throws ValidacaoException {
		this.searchCriterios.addCriteriosGreaterDate(nmCampoTabela, dtInfracao, validarString(dtInfracao));
		return this;
	}
	
	public SearchCriteriosFixCidadeBuilder setcdsLoteImpressao(String nmCampoTabela, List<Integer> cdsLoteImpressao) {
		if( cdsLoteImpressao != null && !cdsLoteImpressao.isEmpty())
		searchCriterios.addCriterios(nmCampoTabela, cdsLoteImpressao.toString().substring(1, cdsLoteImpressao.toString().length()-1), ItemComparator.IN, Types.INTEGER);
		return this;
	}
	
	public SearchCriteriosFixCidadeBuilder setcdsAit(String nmCampoTabela, List<Integer> cdsAit) {
		if(cdsAit != null && !cdsAit.isEmpty())
			searchCriterios.addCriterios(nmCampoTabela, cdsAit.toString().substring(1, cdsAit.toString().length()-1), ItemComparator.IN, Types.INTEGER);
		return this;
	}
	
	private boolean validarString(String validar) throws ValidacaoException {
		return validar != null && !validar.trim().equals(""); 
	}
	
	public SearchCriterios build() {
		return this.searchCriterios;
	}
}
