package com.tivic.manager.triagem.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventoEstacionamentoDigitalImagem {

	private int cdEvento;
	private String urlImagem;
	private Double vlLatitude;
	private Double vlLongitude;

	public EventoEstacionamentoDigitalImagem() { }
	
	public EventoEstacionamentoDigitalImagem(
			int cdEvento, 
			String urImagem, 
			Double vlLatitude, 
			Double vlLongitude
	) {
		super();
		this.cdEvento = cdEvento;
		this.urlImagem = urImagem;
		this.vlLatitude = vlLatitude;
		this.vlLongitude = vlLongitude;
	}

	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	
	public int getCdEvento(){
		return this.cdEvento;
	}
	
	public void setUrlImagem(String urlImagem){
		this.urlImagem=urlImagem;
	}
	
	public String getUrlImagem(){
		return this.urlImagem;
	}
	
	public void setVlLatitude(Double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	
	public Double getVlLatitude(){
		return this.vlLatitude;
	}
	
	public void setVlLongitude(Double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	
	public Double getVlLongitude(){
		return this.vlLongitude;
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