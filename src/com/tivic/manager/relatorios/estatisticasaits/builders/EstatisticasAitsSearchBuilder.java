package com.tivic.manager.relatorios.estatisticasaits.builders;

import com.tivic.sol.search.SearchCriterios;

public class EstatisticasAitsSearchBuilder {
	private SearchCriterios searchCriterios;

	public EstatisticasAitsSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}

	public EstatisticasAitsSearchBuilder setDtInfracaoInicial(String dtInfracaoInicial) {
		searchCriterios.addCriteriosGreaterDate("A.dt_infracao", dtInfracaoInicial, dtInfracaoInicial != null);
		return this;
	}
	
	public EstatisticasAitsSearchBuilder setDtInfracaoFinal(String dtInfracaoFinal) {
		searchCriterios.addCriteriosMinorDate("A.dt_infracao", dtInfracaoFinal, dtInfracaoFinal != null);
		return this;
	}
	
	public EstatisticasAitsSearchBuilder setDtInicialImpressao(String dtInfracaoInicial) {
		searchCriterios.addCriteriosEqualString("dt_inicial", dtInfracaoInicial, dtInfracaoInicial != null);
		return this;
	}
	
	public EstatisticasAitsSearchBuilder setDtFinalImpressao(String dtInfracaoFinal) {
		searchCriterios.addCriteriosEqualString("dt_final", dtInfracaoFinal, dtInfracaoFinal != null);
		return this;
	}
	
	public EstatisticasAitsSearchBuilder setTpRelatorio(int tpRelatorio) {
		searchCriterios.addCriteriosEqualInteger("tp_relatorio", tpRelatorio, tpRelatorio > 0);
		return this;
	}

	public EstatisticasAitsSearchBuilder setTpGrafico(int tpGrafico) {
		searchCriterios.addCriteriosEqualInteger("tp_grafico", tpGrafico);
		return this;
	}

	public SearchCriterios build() {
        return searchCriterios;
    }
}
