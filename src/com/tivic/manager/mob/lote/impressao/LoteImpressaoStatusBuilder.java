package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;

public class LoteImpressaoStatusBuilder {
	private LoteImpressaoStatus loteImpressaoStatus;
	
	public LoteImpressaoStatusBuilder(LoteImpressao lote, int totalDocumentos, int qtDocumentosGerados) {
		loteImpressaoStatus = new LoteImpressaoStatus();
		loteImpressaoStatus.setCdLoteImpressao(lote.getCdLoteImpressao());
		loteImpressaoStatus.setIdLoteImpressaoAit(lote.getIdLoteImpressao());
		loteImpressaoStatus.setDtInicio(lote.getDtCriacao());
		loteImpressaoStatus.setTotalDocumentos(totalDocumentos);
		loteImpressaoStatus.setQtDocumentosGerados(qtDocumentosGerados);
		loteImpressaoStatus.setStDocumento(lote.getStLoteImpressao());
	}
	
	public LoteImpressaoStatus build() {
		return loteImpressaoStatus;
	}
	
}
