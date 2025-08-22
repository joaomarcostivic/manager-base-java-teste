package com.tivic.manager.mob.lotes.builders.impressao;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;

public class LoteImpressaoAitBuilder {
	
	private LoteImpressaoAit loteImpressaoAit;
	
	public LoteImpressaoAitBuilder() {
		loteImpressaoAit = new LoteImpressaoAit();
	}
	
	public LoteImpressaoAitBuilder setCdLoteImpressao(int cdLoteImpressao) {
		loteImpressaoAit.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public LoteImpressaoAitBuilder setCdAit(int cdAit) {
		loteImpressaoAit.setCdAit(cdAit);
		return this;
	}
	
	public LoteImpressaoAitBuilder setStImpressao(int stImpressao) {
		loteImpressaoAit.setStImpressao(stImpressao);
		return this;
	}
	
	public LoteImpressaoAitBuilder setTxtErro(String txtErro) {
		loteImpressaoAit.setTxtErro(txtErro);
		return this;
	}
	
	public LoteImpressaoAit build() {
		return loteImpressaoAit;
	}

}
