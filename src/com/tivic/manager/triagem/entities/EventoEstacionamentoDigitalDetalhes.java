package com.tivic.manager.triagem.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventoEstacionamentoDigitalDetalhes {

	private int cdEvento;
	private String idDispositivo;
	private String nrVaga;
	private String nmRuaNotificacao;
	private String nrImovelReferencia;
	private String idColaborador;

	public EventoEstacionamentoDigitalDetalhes() { }
	
	public EventoEstacionamentoDigitalDetalhes(
			int cdEvento, 
			String idDispositivo, 
			String nrVaga,
			String nmRuaNotificacao, 
			String nrImovelReferencia, 
			String idColaborador
		) {
		super();
		this.cdEvento = cdEvento;
		this.idDispositivo = idDispositivo;
		this.nrVaga = nrVaga;
		this.nmRuaNotificacao = nmRuaNotificacao;
		this.nrImovelReferencia = nrImovelReferencia;
		this.idColaborador = idColaborador;
	}

	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	
	public int getCdEvento(){
		return this.cdEvento;
	}
	
	public void setIdDispositivo(String idDispositivo){
		this.idDispositivo=idDispositivo;
	}
	
	public String getIdDispositivo(){
		return this.idDispositivo;
	}
	
	public void setNrVaga(String nrVaga){
		this.nrVaga=nrVaga;
	}
	
	public String getNrVaga(){
		return this.nrVaga;
	}
	
	public void setNmRuaNotificacao(String nmRuaNotificacao){
		this.nmRuaNotificacao=nmRuaNotificacao;
	}
	
	public String getNmRuaNotificacao(){
		return this.nmRuaNotificacao;
	}
	
	public void setNrImovelReferencia(String nrImovelReferencia){
		this.nrImovelReferencia=nrImovelReferencia;
	}
	
	public String getNrImovelReferencia(){
		return this.nrImovelReferencia;
	}
	
	public void setIdColaborador(String idColaborador){
		this.idColaborador=idColaborador;
	}
	
	public String getIdColaborador(){
		return this.idColaborador;
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