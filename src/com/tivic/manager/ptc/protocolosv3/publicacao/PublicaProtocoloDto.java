package com.tivic.manager.ptc.protocolosv3.publicacao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PublicaProtocoloDto {

	private int cdAit;
	private int cdDocumento;
	
	public int getCdAit() {
		return cdAit;
	}
	
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	
	public int getCdDocumento() {
		return cdDocumento;
	}
	
	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
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
