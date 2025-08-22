package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitDtInfracaoDeserialize extends Ait {
	
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	
	public GregorianCalendar getDtInfracao(){
		return this.dtInfracao;
	}
	
	public void setDtInfracao(GregorianCalendar dtInfracao){
		this.dtInfracao = dtInfracao;
	}
	
}
