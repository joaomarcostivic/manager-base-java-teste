package com.tivic.manager.mob.lote.impressao.LoteImpressaoAitBuilder;

import java.util.GregorianCalendar;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;

public class LoteImpressaoAitBuilder {
	private LoteImpressaoAit loteImpressaoAit;
	
	public LoteImpressaoAitBuilder() {
		loteImpressaoAit = new LoteImpressaoAit();
	}
	
	public LoteImpressaoAitBuilder addCdLoteImpressao(int cdLoteImpressao) {
		loteImpressaoAit.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public LoteImpressaoAitBuilder addCdAit(int cdAit) {
		loteImpressaoAit.setCdAit(cdAit);
		return this;
	}
	
	public LoteImpressaoAitBuilder addDtInclusao(GregorianCalendar dtInclusao) {
		loteImpressaoAit.setDtInclusao(dtInclusao);
		return this;
	}
	
	public LoteImpressaoAitBuilder addIdLoteImpressaoAit(String idLoteImpressaoAit) {
		loteImpressaoAit.setIdLoteImpressaoAit(idLoteImpressaoAit);
		return this;
	}
	
	public LoteImpressaoAitBuilder addStImpressao(int stImpressao) {
		loteImpressaoAit.setStImpressao(stImpressao);
		return this;
	}
	
	public LoteImpressaoAit build() {
		return loteImpressaoAit;
	}
}
