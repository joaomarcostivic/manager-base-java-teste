package com.tivic.manager.mob.colaborador;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ColaboradorTableDTO {
	
	private int cdPessoa;
	private String nmPessoa;
	private String nrCpf;
	private String nrCelular;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtNascimento;
	private int cdVinculo;
	private String nmVinculo;
	private int stVinculo;
	private GregorianCalendar dtVinculo;
	
	public GregorianCalendar getDtVinculo() {
		return dtVinculo;
	}
	public void setDtVinculo(GregorianCalendar dtVinculo) {
		this.dtVinculo = dtVinculo;
	}
	public int getCdPessoa() {
		return cdPessoa;
	}
	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public String getNrCpf() {
		return nrCpf;
	}
	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}
	public String getNrCelular() {
		return nrCelular;
	}
	public void setNrCelular(String nrCelular) {
		this.nrCelular = nrCelular;
	}
	public GregorianCalendar getDtNascimento() {
		return dtNascimento;
	}
	public void setDtNascimento(GregorianCalendar dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
	public int getCdVinculo() {
		return cdVinculo;
	}
	public void setCdVinculo(int cdVinculo) {
		this.cdVinculo = cdVinculo;
	}
	public String getNmVinculo() {
		return nmVinculo;
	}
	public void setNmVinculo(String nmVinculo) {
		this.nmVinculo = nmVinculo;
	}
	public int getStVinculo() {
		return stVinculo;
	}
	public void setStVinculo(int stVinculo) {
		this.stVinculo = stVinculo;
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
