package com.tivic.manager.mob.processamento.conversao.dtos;

import java.util.GregorianCalendar;

public class GrupoEventoDTO {
	private int cdGrupoEvento;
    private GregorianCalendar dtGrupoEvento;
    private int qtdEventos;
    
	public int getCdGrupoEvento() {
		return cdGrupoEvento;
	}
	
	public void setCdGrupoEvento(int cdGrupoEvento) {
		this.cdGrupoEvento = cdGrupoEvento;
	}
	
	public GregorianCalendar getDtGrupoEvento() {
		return dtGrupoEvento;
	}
	
	public void setDtGrupoEvento(GregorianCalendar dtGrupoEvento) {
		this.dtGrupoEvento = dtGrupoEvento;
	}

	public int getQtdEventos() {
		return qtdEventos;
	}

	public void setQtdEventos(int qtdEventos) {
		this.qtdEventos = qtdEventos;
	}
}
