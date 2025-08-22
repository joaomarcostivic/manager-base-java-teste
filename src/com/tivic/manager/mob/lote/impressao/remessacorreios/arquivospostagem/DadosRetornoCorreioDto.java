package com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DadosRetornoCorreioDto {
	
	private String sgServico;
	private String nrEtiqueta;
	private int digitoVerificado;
	private String sgPaisOrigem;
	private GregorianCalendar dataEntrega;
	private int stEntrega;
	
    public String getSgServico() {
		return sgServico;
	}

	public void setSgServico(String sgServico) {
		this.sgServico = sgServico;
	}

	public String getNrEtiqueta() {
		return nrEtiqueta;
	}

	public void setNrEtiqueta(String nrEtiqueta) {
		this.nrEtiqueta = nrEtiqueta;
	}

	public int getDigitoVerificado() {
		return digitoVerificado;
	}

	public void setDigitoVerificado(int digitoVerificado) {
		this.digitoVerificado = digitoVerificado;
	}

	public String getSgPaisOrigem() {
		return sgPaisOrigem;
	}

	public void setSgPaisOrigem(String sgPaisOrigem) {
		this.sgPaisOrigem = sgPaisOrigem;
	}

	public GregorianCalendar getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(GregorianCalendar dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public int getStEntrega() {
		return stEntrega;
	}

	public void setStEntrega(int stEntrega) {
		this.stEntrega = stEntrega;
	}

	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
	
}
