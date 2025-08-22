package com.tivic.manager.mob.veiculosmultavencida;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitVeiculoMultaVencidaDTO {
    private String idAit;
    @JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
    private GregorianCalendar dtInfracao; 
    @JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
    private GregorianCalendar dtVencimento;
	private double vlMulta;
	private int tpStatus;
	private int lgEnviadoDetran;
	
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}
	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	public double getVlMulta() {
		return vlMulta;
	}
	public void setMvlMulta(double vlMulta) {
		this.vlMulta = vlMulta;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int stAit) {
		this.tpStatus = stAit;
	}
	public int getLgEnviadoDetran() {
		return lgEnviadoDetran;
	}
	public void setLgEnviadoDetran(int lgEnviadoDetran) {
		this.lgEnviadoDetran = lgEnviadoDetran;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possivel serializar o objeto.";
		}
	}
}
