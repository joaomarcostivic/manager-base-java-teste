package com.tivic.manager.wsdl.detran.mg.consultainfracoes;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ConsultaInfracaoOcorrenciaDTO {
	private String orgao;
	private String placa;
	private String ait;
	private int codigoInfracao;
	private int desdobramento;
	private int numeroProcessamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	GregorianCalendar dataInfracao;
	private String gravidade;
	private String convertidaAdvertencia;
	private String pontuacaoRenainf;
	

	public String getOrgao() {
		return orgao;
	}

	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getAit() {
		return ait;
	}

	public void setAit(String ait) {
		this.ait = ait;
	}

	public int getCodigoInfracao() {
		return codigoInfracao;
	}

	public void setCodigoInfracao(int codigoInfracao) {
		this.codigoInfracao = codigoInfracao;
	}

	public int getDesdobramento() {
		return desdobramento;
	}

	public void setDesdobramento(int desdobramento) {
		this.desdobramento = desdobramento;
	}

	public int getNumeroProcessamento() {
		return numeroProcessamento;
	}

	public void setNumeroProcessamento(int numeroProcessamento) {
		this.numeroProcessamento = numeroProcessamento;
	}

	public GregorianCalendar getDataInfracao() {
		return dataInfracao;
	}

	public void setDataInfracao(GregorianCalendar dataInfracao) {
		this.dataInfracao = dataInfracao;
	}

	public String getGravidade() {
		return gravidade;
	}

	public void setGravidade(String gravidade) {
		this.gravidade = gravidade;
	}

	public String getConvertidaAdvertencia() {
		return convertidaAdvertencia;
	}

	public void setConvertidaAdvertencia(String convertidaAdvertencia) {
		this.convertidaAdvertencia = convertidaAdvertencia;
	}

	public String getPontuacaoRenainf() {
		return pontuacaoRenainf;
	}

	public void setPontuacaoRenainf(String pontuacaoRenainf) {
		this.pontuacaoRenainf = pontuacaoRenainf;
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

