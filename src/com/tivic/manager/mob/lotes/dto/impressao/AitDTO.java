package com.tivic.manager.mob.lotes.dto.impressao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.Ait;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitDTO extends Ait {
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtPrazoEmissao;
	
	public AitDTO() {}

	public GregorianCalendar getDtPrazoEmissao() {
		return dtPrazoEmissao;
	}

	public void setDtPrazoEmissao(GregorianCalendar dtPrazoEmissao) {
		this.dtPrazoEmissao = dtPrazoEmissao;
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
