package com.tivic.manager.mob.lote.impressao.lotedocumentoexterno;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LoteDocumentoExternoDTO extends LoteDocumentoExterno{
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	private String nrDocumento;
	private String sgOrgaoExterno;
	
	public LoteDocumentoExternoDTO() { }
	
	public GregorianCalendar getDtCriacao() {
		return dtCriacao;
	}
	
	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	
	public String getNrDocumento() {
		return nrDocumento;
	}
	
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}
	
	public String getSgOrgaoExterno() {
		return sgOrgaoExterno;
	}
	
	public void setSgOrgaoExterno(String sgOrgaoExterno) {
		this.sgOrgaoExterno = sgOrgaoExterno;
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
