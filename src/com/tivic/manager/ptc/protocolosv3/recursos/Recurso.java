package com.tivic.manager.ptc.protocolosv3.recursos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Recurso {

	private int cdDocumento;
	private int cdAta;

	public Recurso() { }

	public Recurso(int cdDocumento, int cdAta) {
		setCdDocumento(cdDocumento);
		setCdAta(cdAta);
	}
	
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	
	public void setCdAta(int cdAta){
		this.cdAta=cdAta;
	}
	
	public int getCdAta(){
		return this.cdAta;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
	
	public Object clone() {
		return new Recurso(getCdDocumento(),
			getCdAta());
	}
}