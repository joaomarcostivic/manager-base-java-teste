package com.tivic.manager.mob.talonario;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class TalonarioLoteDTO {
	private int nrInicial;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEntrega;
	private String sgTalao;
	private int tpTalao;
	private int qtdFolhas;
	private int qtdTalonarios;
	
	public int getNrInicial() {
		return nrInicial;
	}
	public void setNrInicial(int nrInicial) {
		this.nrInicial = nrInicial;
	}
	public GregorianCalendar getDtEntrega() {
		return dtEntrega;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega) {
		this.dtEntrega = dtEntrega;
	}
	public String getSgTalao() {
		return sgTalao;
	}
	public void setSgTalao(String sgTalao) {
		this.sgTalao = sgTalao;
	}
	public int getTpTalao() {
		return tpTalao;
	}
	public void setTpTalao(int tpTalao) {
		this.tpTalao = tpTalao;
	}
	public int getQtdFolhas() {
		return qtdFolhas;
	}
	public void setQtdFolhas(int qtdFolhas) {
		this.qtdFolhas = qtdFolhas;
	}
	public int getQtdTalonarios() {
		return qtdTalonarios;
	}
	public void setQtdTalonarios(int qtdTalonarios) {
		this.qtdTalonarios = qtdTalonarios;
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
