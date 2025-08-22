package com.tivic.manager.tasks.limpeza.lotes;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LimpezaLotesDTO {
	private List<Integer> codigosLote;
	private GregorianCalendar dtCriacao;
	private int limite;
	
	public LimpezaLotesDTO() {
		this.codigosLote = new ArrayList<>();
	}
	
	public List<Integer> getCodigosLote() {
		return codigosLote;
	}
	public void setCodigosLote(List<Integer> codigosLote) {
		this.codigosLote = codigosLote;
	}
	public GregorianCalendar getDtCriacao() {
		return dtCriacao;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	public int getLimite() {
		return limite;
	}
	public void setLimite(int limite) {
		this.limite = limite;
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
