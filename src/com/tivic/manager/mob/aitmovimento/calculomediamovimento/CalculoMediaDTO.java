package com.tivic.manager.mob.aitmovimento.calculomediamovimento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CalculoMediaDTO {
	
	private double mediaDiasDefesa;
	private double mediaDiasJari;
	private double mediaDiasEsperaDefesa;
	private double mediaDiasEsperaJari;
	private double qtTalaoRadarDisponivel;
	private double qtTalaoDisponivel;
	
	public double getMediaDiasJari() {
		return mediaDiasJari;
	}
	public void setMediaDiasJari(double mediaDiasJari) {
		this.mediaDiasJari = mediaDiasJari;
	}
	public double getMediaDiasDefesa() {
		return mediaDiasDefesa;
	}
	public void setMediaDiasDefesa(double mediaDiasJulgamento) {
		this.mediaDiasDefesa = mediaDiasJulgamento;
	}
	public double getMediaDiasEsperaDefesa() {
		return mediaDiasEsperaDefesa;
	}
	public void setMediaDiasEsperaDefesa(double mediaDiasEsperaDefesa) {
		this.mediaDiasEsperaDefesa = mediaDiasEsperaDefesa;
	}
	public double getMediaDiasEsperaJari() {
		return mediaDiasEsperaJari;
	}
	public void setMediaDiasEsperaJari(double mediaDiasEsperaJari) {
		this.mediaDiasEsperaJari = mediaDiasEsperaJari;
	}
	public double getQtTalaoRadarDisponivel() {
		return qtTalaoRadarDisponivel;
	}
	public void setQtTalaoRadarDisponivel(double qtTalaoRadarDisponivel) {
		this.qtTalaoRadarDisponivel = qtTalaoRadarDisponivel;
	}
	public double getQtTalaoDisponivel() {
		return qtTalaoDisponivel;
	}
	public void setQtTalaoDisponivel(double qtTalaoDisponivel) {
		this.qtTalaoDisponivel = qtTalaoDisponivel;
	}
	
	@Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
	
}