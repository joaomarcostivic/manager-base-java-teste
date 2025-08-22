package com.tivic.manager.triagem.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.seg.Usuario;
import com.tivic.manager.triagem.dtos.GrupoEventoTriagemDTO;
import com.tivic.manager.triagem.dtos.OrgaoTriagemDTO;

public class GrupoEventoTriagemBuilder {
	
	private GrupoEventoTriagemDTO grupoEvento;
	
	public GrupoEventoTriagemBuilder() {
		this.grupoEvento = new GrupoEventoTriagemDTO();
	}
	
	public GrupoEventoTriagemBuilder cdGrupoEvento(Long cdGrupoEvento) {
        this.grupoEvento.setCdGrupoEvento(cdGrupoEvento);
        return this;
    }
	
    public GrupoEventoTriagemBuilder orgao(OrgaoTriagemDTO orgao) {
        this.grupoEvento.setOrgao(orgao);
        return this;
    }
    
    public GrupoEventoTriagemBuilder dtGrupoEvento(GregorianCalendar dtGrupoEvento) {
        this.grupoEvento.setDtGrupoEvento(dtGrupoEvento);
        return this;
    }
    
    public GrupoEventoTriagemBuilder usuario(Usuario usuario) {
        this.grupoEvento.setUsuario(usuario);
        return this;
    }
    
    public GrupoEventoTriagemBuilder stEmProcessamento(Boolean stEmProcessamento) {
        this.grupoEvento.setStEmProcessamento(stEmProcessamento);
        return this;
    }
    
    public GrupoEventoTriagemBuilder qtEventos(Long qtEventos) {
        this.grupoEvento.setQtEventos(qtEventos);
        return this;
    }
    
    public GrupoEventoTriagemDTO build() {
    	return grupoEvento;
    }
}
