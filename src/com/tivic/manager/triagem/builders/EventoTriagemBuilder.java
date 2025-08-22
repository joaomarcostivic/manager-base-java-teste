package com.tivic.manager.triagem.builders;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.triagem.dtos.EquipamentoTriagemDTO;
import com.tivic.manager.triagem.dtos.EventoTriagemDTO;

public class EventoTriagemBuilder {
	private EventoTriagemDTO eventoTriagem;
	
	public EventoTriagemBuilder() {
		this.eventoTriagem = new EventoTriagemDTO();
	}
	
    public EventoTriagemBuilder cdEvento(int cdEvento) {
        this.eventoTriagem.setCdEvento(cdEvento);
        return this;
    }
    
    public EventoTriagemBuilder tipoEvento(TipoEvento tipoEvento) {
        this.eventoTriagem.setTipoEvento(tipoEvento);
        return this;
    }
    
    public EventoTriagemBuilder dtEvento(GregorianCalendar dtEvento) {
        this.eventoTriagem.setDtEvento(dtEvento);
        return this;
    }
    
    public EventoTriagemBuilder dtConclusao(GregorianCalendar dtConclusao) {
        this.eventoTriagem.setDtConclusao(dtConclusao);
        return this;
    }
    
    public EventoTriagemBuilder dsLocal(String dsLocal) {
        this.eventoTriagem.setDsLocal(dsLocal);
        return this;
    }
    
    public EventoTriagemBuilder nmOrgaoAutuador(String nmOrgaoAutuador) {
        this.eventoTriagem.setNmOrgaoAutuador(nmOrgaoAutuador);
        return this;
    }
    
    public EventoTriagemBuilder nrPlaca(String nrPlaca) {
        this.eventoTriagem.setNrPlaca(nrPlaca);
        return this;
    }
    
    public EventoTriagemBuilder equipamento(EquipamentoTriagemDTO equipamento) {
        this.eventoTriagem.setEquipamento(equipamento);
        return this;
    }
    
    public EventoTriagemBuilder stEvento(int stEvento) {
        this.eventoTriagem.setStEvento(stEvento);
        return this;
    }
    
    public EventoTriagemDTO build() {
    	return eventoTriagem;
    }
}
