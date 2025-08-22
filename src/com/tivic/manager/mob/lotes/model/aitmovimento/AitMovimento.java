package com.tivic.manager.mob.lotes.model.aitmovimento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitMovimento {
	private int cdMovimento;
	private int cdAit;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	
	public int getCdMovimento() {
		return cdMovimento;
	}
	
	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
	}
	
	public int getCdAit() {
		return cdAit;
	}
	
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}
}
