package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalImagem;

public class EventoEstacionamentoDigitalImagemBuilder {
	
	private EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem;
	
	public EventoEstacionamentoDigitalImagemBuilder() {
		eventoEstacionamentoDigitalImagem = new EventoEstacionamentoDigitalImagem();
	}
	
	public EventoEstacionamentoDigitalImagemBuilder cdEvento(int cdEvento) {
		eventoEstacionamentoDigitalImagem.setCdEvento(cdEvento);
		return this;
	}
	
	public EventoEstacionamentoDigitalImagemBuilder urlImagem(String urlImagem) {
		eventoEstacionamentoDigitalImagem.setUrlImagem(urlImagem);
		return this;
	}
	
	public EventoEstacionamentoDigitalImagemBuilder vlLatitude(Double vlLatitude) {
		eventoEstacionamentoDigitalImagem.setVlLatitude(vlLatitude);
		return this;
	}
	
	public EventoEstacionamentoDigitalImagemBuilder vlLongitude(Double vlLongitude) {
		eventoEstacionamentoDigitalImagem.setVlLongitude(vlLongitude);
		return this;
	}
	
	public EventoEstacionamentoDigitalImagem build() {
		return eventoEstacionamentoDigitalImagem;
	}

}
