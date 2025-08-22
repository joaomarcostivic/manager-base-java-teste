package com.tivic.manager.mob.lotes.model.impressao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoteImpressaoAit {

	private int cdLoteImpressao;
	private int cdAit;
	private int stImpressao;
	private String txtErro;
	
	public LoteImpressaoAit() {};

	public LoteImpressaoAit(int cdLoteImpressao,
			int cdAit,
			int stImpressao,
			String txtErro) {
		setCdLoteImpressao(cdLoteImpressao);
		setCdAit(cdAit);
		setStImpressao(stImpressao);
		setTxtErro(txtErro);
	}

	public LoteImpressaoAit(
			int cdLoteImpressao,
			int cdAit
			) {
		setCdLoteImpressao(cdLoteImpressao);
		setCdAit(cdAit);
	}
	
	public void setCdLoteImpressao(int cdLoteImpressao){
		this.cdLoteImpressao=cdLoteImpressao;
	}
	
	public int getCdLoteImpressao(){
		return this.cdLoteImpressao;
	}
	
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	
	public int getCdAit(){
		return this.cdAit;
	}

	public int getStImpressao() {
		return stImpressao;
	}

	public void setStImpressao(int stImpressao) {
		this.stImpressao = stImpressao;
	}

	public void setTxtErro(String txtErro){
		this.txtErro=txtErro;
	}
	
	public String getTxtErro(){
		return this.txtErro;
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