package com.tivic.manager.mob.lotes.dto.dividaativa;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LoteDividaAtivaDTO extends Lote {

	private int qtdAit;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEnvio;
	private int stLote;

	public int getQtdAit() {
		return qtdAit;
	}

	public void setQtdAit(int qtdAit) {
		this.qtdAit = qtdAit;
	}

	public GregorianCalendar getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(GregorianCalendar dtEnvio) {
		this.dtEnvio = dtEnvio;
	}

	public int getStLote() {
		return stLote;
	}

	public void setStLote(int stLote) {
		this.stLote = stLote;
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
