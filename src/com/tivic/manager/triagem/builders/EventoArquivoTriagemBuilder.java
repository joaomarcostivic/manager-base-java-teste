package com.tivic.manager.triagem.builders;

import java.util.GregorianCalendar;
import com.tivic.manager.mob.EventoArquivo;

public class EventoArquivoTriagemBuilder {

	private EventoArquivo eventoArquivo;
	
	public EventoArquivoTriagemBuilder() {
		eventoArquivo = new EventoArquivo();
	}
	
	public EventoArquivoTriagemBuilder cdEvento(int cdEvento) {
		eventoArquivo.setCdEvento(cdEvento);
		return this;
	}
	
	public EventoArquivoTriagemBuilder cdArquivo(int cdArquivo) {
		eventoArquivo.setCdArquivo(cdArquivo);
		return this;
	}
	
	public EventoArquivoTriagemBuilder dtAquivo(GregorianCalendar dtEvento) {
		eventoArquivo.setDtArquivo(dtEvento);
		return this;
	}
	
	public EventoArquivo build() {
		return eventoArquivo;
	}
	
}
