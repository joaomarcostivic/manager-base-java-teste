package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.dtos.ImagemNotificacaoDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalImagem;

public class EventoImagemNotificacaoBuilder {
	
	private EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem;
	
	public EventoImagemNotificacaoBuilder() {
		eventoEstacionamentoDigitalImagem = new EventoEstacionamentoDigitalImagem();
	}
	
	public EventoImagemNotificacaoBuilder imagemNotificacao(ImagemNotificacaoDTO imagemNotificacaoDTO, int cdEvento) {
		eventoEstacionamentoDigitalImagem = new EventoEstacionamentoDigitalImagemBuilder()
				.cdEvento(cdEvento)
				.urlImagem(imagemNotificacaoDTO.getUrl())
				.vlLatitude(imagemNotificacaoDTO.getVlLatitude())
				.vlLongitude(imagemNotificacaoDTO.getVlLongitude())
				.build();
		return this;
	}
	
	public EventoEstacionamentoDigitalImagem build() {
		return eventoEstacionamentoDigitalImagem;
	}

}
