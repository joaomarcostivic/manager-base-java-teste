package com.tivic.manager.mob.lotes.model.impressao;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoteImpressao {
	private int cdLoteImpressao;
	private int cdLote;
	private int stLote;
	private int tpImpressao;
	private List<LoteImpressaoAit> aits;
	private String idLote;
	private Boolean lgGeracaoViaUnica;
	
	public LoteImpressao() {}
	
	public LoteImpressao(int cdLoteImpressao,
			int cdLote,
			int stLote,
			int tpImpressao) {
		setCdLoteImpressao(cdLoteImpressao);
		setCdLote(cdLote);
		setStLote(stLote);
		setTpImpressao(tpImpressao);
	}
	
	public void setCdLoteImpressao(int cdLoteImpressao){
		this.cdLoteImpressao=cdLoteImpressao;
	}
	
	public int getCdLoteImpressao(){
		return this.cdLoteImpressao;
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
	
	public void setTpImpressao(int tpImpressao){
		this.tpImpressao=tpImpressao;
	}
	
	public int getTpImpressao(){
		return this.tpImpressao;
	}
	
	public List<LoteImpressaoAit> getAits() {
		return aits;
	}
	public void setAits(List<LoteImpressaoAit> aits) {
		this.aits = aits;
	}

	public String getIdLote() {
		return idLote;
	}

	public void setIdLote(String idLote) {
		this.idLote = idLote;
	}
	
	public Boolean getLgGeracaoViaUnica() {
		return lgGeracaoViaUnica;
	}

	public void setLgGeracaoViaUnica(Boolean tpGeracao) {
		this.lgGeracaoViaUnica = tpGeracao;
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