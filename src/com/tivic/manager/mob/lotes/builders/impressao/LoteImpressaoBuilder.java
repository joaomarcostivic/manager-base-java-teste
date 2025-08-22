package com.tivic.manager.mob.lotes.builders.impressao;

import java.util.List;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;

public class LoteImpressaoBuilder {
	
	private LoteImpressao loteImpressao;
	
	public LoteImpressaoBuilder() {
		loteImpressao = new LoteImpressao();
	}
	
	public LoteImpressaoBuilder setCdLoteImpressao(int cdLoteImpressao) {
		loteImpressao.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
		
	public LoteImpressaoBuilder setCdLote(int cdLote) {
		loteImpressao.setCdLote(cdLote);
		return this;
	}
	
	public LoteImpressaoBuilder setStLote(int stLote) {
		loteImpressao.setStLote(stLote);
		return this;
	}
	
	public LoteImpressaoBuilder setTpImpressao(int tpImpressao) {
		loteImpressao.setTpImpressao(tpImpressao);
		return this;
	}
	
	public LoteImpressaoBuilder setAits(List<LoteImpressaoAit> aits) {
		this.loteImpressao.setAits(aits);
		return this;
	}
	
	public LoteImpressao build() {
		return loteImpressao;
	}	

}
