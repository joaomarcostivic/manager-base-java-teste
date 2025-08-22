package com.tivic.manager.mob.lotes.model.dividaativa;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LoteDividaAtivaAit {

	private int cdLoteDividaAtiva;
	private int cdAit;
	private int lgErro;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEnvio;

	public LoteDividaAtivaAit() {
	}

	public LoteDividaAtivaAit(int cdLoteDividaAtiva, int cdAit, int lgErro, GregorianCalendar dtEnvio) {
		setCdLoteDividaAtiva(cdLoteDividaAtiva);
		setCdAit(cdAit);
		setLgErro(lgErro);
		setDtEnvio(dtEnvio);
	}

	public void setCdLoteDividaAtiva(int cdLoteDividaAtiva) {
		this.cdLoteDividaAtiva = cdLoteDividaAtiva;
	}

	public int getCdLoteDividaAtiva() {
		return this.cdLoteDividaAtiva;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdAit() {
		return this.cdAit;
	}

	public void setLgErro(int lgErro) {
		this.lgErro = lgErro;
	}

	public int getLgErro() {
		return this.lgErro;
	}

	public GregorianCalendar getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(GregorianCalendar dtEnvio) {
		this.dtEnvio = dtEnvio;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possivel serializar o objeto";
		}
	}

}