package com.tivic.manager.triagem.entities;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventoEstacionamentoDigital {

	private int cdEvento;
	private String nrNotificacao;
	private String dsNotificacao;
	private int stNotificacao;
	private String dsMotivoCancelamento;
	private GregorianCalendar dtTempoVerificacao;
	
	public EventoEstacionamentoDigital() {}
	
	public EventoEstacionamentoDigital(
			int cdEvento, 
			String nrNotificacao, 
			String dsNotificacao, 
			int stNotificacao,
			String dsMotivoCancelamento,
			GregorianCalendar dtTempoVerificacao
		) {
		super();
		this.cdEvento = cdEvento;
		this.nrNotificacao = nrNotificacao;
		this.dsNotificacao = dsNotificacao;
		this.stNotificacao = stNotificacao;
		this.dsMotivoCancelamento = dsMotivoCancelamento;
		this.dtTempoVerificacao = dtTempoVerificacao;
	}

	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	
	public int getCdEvento(){
		return this.cdEvento;
	}
	
	public void setNrNotificacao(String nrNotificacao){
		this.nrNotificacao=nrNotificacao;
	}
	
	public String getNrNotificacao(){
		return this.nrNotificacao;
	}
	
	public void setDsNotificacao(String dsNotificacao){
		this.dsNotificacao=dsNotificacao;
	}
	
	public String getDsNotificacao(){
		return this.dsNotificacao;
	}
	
	public void setStNotificacao(int stNotificacao){
		this.stNotificacao=stNotificacao;
	}
	
	public int getStNotificacao(){
		return this.stNotificacao;
	}
	
	public void setDsMotivoCancelamento(String dsMotivoCancelamento){
		this.dsMotivoCancelamento=dsMotivoCancelamento;
	}
	
	public String getDsMotivoCancelamento(){
		return this.dsMotivoCancelamento;
	}
	
	public GregorianCalendar getDtTempoVerificacao() {
		return dtTempoVerificacao;
	}

	public void setDtTempoVerificacao(GregorianCalendar dtTempoVerificacao) {
		this.dtTempoVerificacao = dtTempoVerificacao;
	}

	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}

}