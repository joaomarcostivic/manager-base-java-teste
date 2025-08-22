package com.tivic.manager.mob.lotes.model.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LotePublicacao {

	private int cdLotePublicacao;
	private int cdLote;
	private int stLote;
	private GregorianCalendar dtPublicacao;
	private int tpPublicacao;
	private List<LotePublicacaoAit> aits;

	public LotePublicacao() { }

	public LotePublicacao(int cdLotePublicacao,
			int cdLote,
			int stLote,
			GregorianCalendar dtPublicacao,
			int tpPublicacao) {
		setCdLotePublicacao(cdLotePublicacao);
		setCdLote(cdLote);
		setStLote(stLote);
		setDtPublicacao(dtPublicacao);
		setTpPublicacao(tpPublicacao);
	}
	
	public void setCdLotePublicacao(int cdLotePublicacao){
		this.cdLotePublicacao=cdLotePublicacao;
	}
	
	public int getCdLotePublicacao(){
		return this.cdLotePublicacao;
	}
	
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	
	public int getCdLote(){
		return this.cdLote;
	}
	
	public void setStLote(int stLote){
		this.stLote=stLote;
	}
	
	public int getStLote(){
		return this.stLote;
	}
	
	public void setDtPublicacao(GregorianCalendar dtPublicacao){
		this.dtPublicacao=dtPublicacao;
	}
	
	public GregorianCalendar getDtPublicacao(){
		return this.dtPublicacao;
	}

	public int getTpPublicacao() {
		return tpPublicacao;
	}

	public void setTpPublicacao(int tpPublicacao) {
		this.tpPublicacao = tpPublicacao;
	}

	public List<LotePublicacaoAit> getAits() {
		return aits;
	}

	public void setAits(List<LotePublicacaoAit> aits) {
		this.aits = aits;
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