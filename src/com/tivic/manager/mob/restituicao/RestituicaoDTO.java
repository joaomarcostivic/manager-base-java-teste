package com.tivic.manager.mob.restituicao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class RestituicaoDTO {
	
	private String idAit;
    private String nmProprietario;
    @JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
    private GregorianCalendar dtInfracao;
    @JsonSerialize(converter = CalendarSerializer.class)
   	@JsonDeserialize(converter = CalendarDeserialize.class)
    private GregorianCalendar dtPagamento;
    private double vlMulta; 
    private int tpStatus;
    private String nmSituacaoAtual;
    
	public String getNmSituacaoAtual() {
		return nmSituacaoAtual;
	}
	public void setNmSituacaoAtual(String nmSituacaoAtual) {
		this.nmSituacaoAtual = nmSituacaoAtual;
	}
	public String getNmProprietario() {
		return nmProprietario;
	}
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
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
	public GregorianCalendar getDtPagamento() {
		return dtPagamento;
	}
	public void setDtPagamento(GregorianCalendar dtPagamento) {
		this.dtPagamento = dtPagamento;
	}
	public double getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(double vlMulta) {
		this.vlMulta = vlMulta;
	}
	public int getTpStatus() {
		return tpStatus;
	}
	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
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
