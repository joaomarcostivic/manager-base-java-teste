package com.tivic.manager.triagem.dtos;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.triagem.serializer.CalendarSerializerWithoutTimezone;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class EventoTriagemDTO {
    private int cdEvento;
    private TipoEvento tipoEvento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
    private GregorianCalendar dtEvento;
	
	@JsonSerialize(using = CalendarSerializerWithoutTimezone.class)
    @JsonDeserialize(converter = CalendarDeserialize.class)
    private GregorianCalendar dtConclusao;
    private String dsLocal;
    private String nmOrgaoAutuador;
    private String nrPlaca;
    private EquipamentoTriagemDTO equipamento;
    private int stEvento;

	public int getCdEvento() {
		return cdEvento;
	}

	public void setCdEvento(int cdEvento) {
		this.cdEvento = cdEvento;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public GregorianCalendar getDtEvento() {
		return dtEvento;
	}

	public void setDtEvento(GregorianCalendar dtEvento) {
		this.dtEvento = dtEvento;
	}
	
	public GregorianCalendar getDtConclusao() {
		return dtConclusao;
	}
	
	public void setDtConclusao(GregorianCalendar dtConclusao) {
		this.dtConclusao = dtConclusao;
	}

	public String getDsLocal() {
		return dsLocal;
	}

	public void setDsLocal(String dsLocal) {
		this.dsLocal = dsLocal;
	}

	public String getNmOrgaoAutuador() {
		return nmOrgaoAutuador;
	}

	public void setNmOrgaoAutuador(String nmOrgaoAutuador) {
		this.nmOrgaoAutuador = nmOrgaoAutuador;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public EquipamentoTriagemDTO getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(EquipamentoTriagemDTO equipamento) {
		this.equipamento = equipamento;
	}

	public int getStEvento() {
		return stEvento;
	}

	public void setStEvento(int stEvento) {
		this.stEvento = stEvento;
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
