package com.tivic.manager.ptc.portal.dtos;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AitMovimentoDTO{
	private int tpStatus;
	private GregorianCalendar dtMovimento;
	
	public AitMovimentoDTO() {
	}
	
	public AitMovimentoDTO(int tpStatus, GregorianCalendar dtMovimento) {
		this.tpStatus = tpStatus;
		this.dtMovimento = dtMovimento;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
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
