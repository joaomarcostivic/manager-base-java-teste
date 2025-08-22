package com.tivic.manager.mob.eventoarquivo;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.EventoArquivo;

public class EventoArquivoBuilder {
    private EventoArquivo eventoArquivo;

    public EventoArquivoBuilder() {
        this.eventoArquivo = new EventoArquivo();
    }

    public EventoArquivoBuilder cdEvento(int cdEvento) {
        eventoArquivo.setCdEvento(cdEvento);
        return this;
    }

    public EventoArquivoBuilder cdArquivo(int cdArquivo) {
        eventoArquivo.setCdArquivo(cdArquivo);
        return this;
    }

    public EventoArquivoBuilder tpArquivo(int tpArquivo) {
        eventoArquivo.setTpArquivo(tpArquivo);
        return this;
    }

    public EventoArquivoBuilder dtArquivo(GregorianCalendar dtArquivo) {
        eventoArquivo.setDtArquivo(dtArquivo);
        return this;
    }

    public EventoArquivoBuilder idArquivo(String idArquivo) {
        eventoArquivo.setIdArquivo(idArquivo);
        return this;
    }

    public EventoArquivoBuilder tpEventoFoto(int tpEventoFoto) {
        eventoArquivo.setTpEventoFoto(tpEventoFoto);
        return this;
    }

    public EventoArquivoBuilder tpFoto(int tpFoto) {
        eventoArquivo.setTpFoto(tpFoto);
        return this;
    }

    public EventoArquivoBuilder lgImpressao(int lgImpressao) {
        eventoArquivo.setLgImpressao(lgImpressao);
        return this;
    }

    public EventoArquivo build() {
        return eventoArquivo;
    }
}

