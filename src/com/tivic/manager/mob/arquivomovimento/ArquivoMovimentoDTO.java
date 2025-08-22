package com.tivic.manager.mob.arquivomovimento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ArquivoMovimentoDTO {
	private int cdAit;
	private String idAit;
	private String dsEntrada;
	private String dsSaida;
	private int cdArquivoMovimento;
	private String dsSugestaoCorrecao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtArquivo;
	private String nrErro;
	private int tpStatus;
	
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public String getDsEntrada() {
		return dsEntrada;
	}
	public void setDsEntrada(String dsEntrada) {
		this.dsEntrada = dsEntrada;
	}
	public String getDsSaida() {
		return dsSaida;
	}
	public void setDsSaida(String dsSaida) {
		this.dsSaida = dsSaida;
	}
	public int getCdArquivoMovimento() {
		return cdArquivoMovimento;
	}
	public void setCdArquivoMovimento(int cdArquivoMovimento) {
		this.cdArquivoMovimento = cdArquivoMovimento;
	}
	public String getDsSugestaoCorrecao() {
		return dsSugestaoCorrecao;
	}
	public void setDsSugestaoCorrecao(String dsSugestaoCorrecao) {
		this.dsSugestaoCorrecao = dsSugestaoCorrecao;
	}
	public GregorianCalendar getDtArquivo() {
		return dtArquivo;
	}
	public void setDtArquivo(GregorianCalendar dtArquivo) {
		this.dtArquivo = dtArquivo;
	}
	public String getNrErro() {
		return nrErro;
	}
	public void setNrErro(String nrErro) {
		this.nrErro = nrErro;
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
			return "Não foi possível serializar o objeto informado";
		}
	}

}
