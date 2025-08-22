package com.tivic.manager.mob.publicacao.dto;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ArquivoEditalPortalDTO {
	private int cdArquivo;
	private String nmArquivo;
	private String nmDocumento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEnvio;
	private byte[] blbArquivo;
	private int cdTipoArquivo;
	
	public int getCdArquivo() {
		return cdArquivo;
	}
	
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}
	
	public String getNmArquivo() {
		return nmArquivo;
	}
	
	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}
	
	public String getNmDocumento() {
		return nmDocumento;
	}
	
	public void setNmDocumento(String nmDocumento) {
		this.nmDocumento = nmDocumento;
	}
	
	public GregorianCalendar getDtEnvio() {
		return dtEnvio;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio) {
		this.dtEnvio = dtEnvio;
	}
	
	public byte[] getBlbArquivo() {
		return blbArquivo;
	}
	
	public void setBlbArquivo(byte[] blbArquivo) {
		this.blbArquivo = blbArquivo;
	}
	
	public int getCdTipoArquivo() {
		return cdTipoArquivo;
	}
	
	public void setCdTipoArquivo(int cdTipoArquivo) {
		this.cdTipoArquivo = cdTipoArquivo;
	}
	
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }

}
