package com.tivic.manager.mob.lotes.builders.impressao;

import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;

public class LoteImpressaoStatusBuilder {
	
	private LoteImpressaoStatus loteImpressaoStatus;
	
	public LoteImpressaoStatusBuilder(LoteImpressao loteImpressao, int totalDocumentos, int qtDocumentosGerados) {
		loteImpressaoStatus = new LoteImpressaoStatus();
		loteImpressaoStatus.setCdLoteImpressao(loteImpressao.getCdLoteImpressao());
		loteImpressaoStatus.setTotalDocumentos(totalDocumentos);
		loteImpressaoStatus.setQtDocumentosGerados(qtDocumentosGerados);
	}
	
	public LoteImpressaoStatus build() {
		return loteImpressaoStatus;
	}
	
}
