package com.tivic.manager.grl.pessoavinculohistorico;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class PessoaVinculoHistorico {
	private int cdVinculoHistorico;
	private int cdPessoa;
	private int cdVinculo;
	private int cdUsuario;
	private int stVinculo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVinculoHistorico;
	
	public int getCdVinculoHistorico() {
		return cdVinculoHistorico;
	}
	public void setCdVinculoHistorico(int cdVinculoHistorico) {
		this.cdVinculoHistorico = cdVinculoHistorico;
	}
	public int getCdPessoa() {
		return cdPessoa;
	}
	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
	}
	public int getCdVinculo() {
		return cdVinculo;
	}
	public void setCdVinculo(int cdVinculo) {
		this.cdVinculo = cdVinculo;
	}
	public int getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public int getStVinculo() {
		return stVinculo;
	}
	public void setStVinculo(int stVinculo) {
		this.stVinculo = stVinculo;
	}
	public GregorianCalendar getDtVinculoHistorico() {
		return dtVinculoHistorico;
	}
	public void setDtVinculoHistorico(GregorianCalendar dtVinculoHistorico) {
		this.dtVinculoHistorico = dtVinculoHistorico;
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
