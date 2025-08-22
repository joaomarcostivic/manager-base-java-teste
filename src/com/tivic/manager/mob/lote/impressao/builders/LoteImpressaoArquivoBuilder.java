package com.tivic.manager.mob.lote.impressao.builders;

import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;

public class LoteImpressaoArquivoBuilder {
	
	private LoteImpressaoArquivo loteImpressaoArquivo;
	
	public LoteImpressaoArquivoBuilder() {
		this.loteImpressaoArquivo = new LoteImpressaoArquivo();
	}
	
	public LoteImpressaoArquivoBuilder setCdLoteImpressao(int cdLoteImpressao) {
		this.loteImpressaoArquivo.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public LoteImpressaoArquivoBuilder setCdArquivo(int cdArquivo) {
		this.loteImpressaoArquivo.setCdArquivo(cdArquivo); 
		return this;
	}
	
	public LoteImpressaoArquivo build() {
		return this.loteImpressaoArquivo;
	}
	
}
