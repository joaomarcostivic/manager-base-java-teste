package com.tivic.manager.triagem.builders;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class EventoEstacionamentoDigitalSearchBuilder {

	private SearchCriterios searchCriterios;
	private IParametroRepository parametroRepository;
	
	public EventoEstacionamentoDigitalSearchBuilder() throws Exception {
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.searchCriterios = new SearchCriterios();
		int cdTipoEvento = getTipoEvento();
		this.searchCriterios.addCriteriosEqualInteger("cd_tipo_evento", cdTipoEvento);
	}
	
	private int getTipoEvento() throws Exception {
		int cdTipoEvento = parametroRepository.getValorOfParametroAsInt("MOB_EVENTO_ESTACIONAMENTO_DIGITAL");
		
		if(cdTipoEvento == 0) {
			throw new Exception("O parâmetro MOB_EVENTO_ESTACIONAMENTO_DIGITAL não foi configurado.");
		}
		
		return cdTipoEvento;
	}
	
	public EventoEstacionamentoDigitalSearchBuilder stEvento(int stEvento) {
		this.searchCriterios.addCriteriosEqualInteger("st_evento", stEvento);
		return this;
	}
	
	public EventoEstacionamentoDigitalSearchBuilder dtConclusao(String dtEvento) {
		this.searchCriterios.addCriteriosEqualString("CAST(dt_conclusao AS DATE)", dtEvento);
		return this;
	}
	
	public SearchCriterios build() {
		return searchCriterios;
	}
}
