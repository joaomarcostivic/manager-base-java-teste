package com.tivic.manager.mob.lote.impressao.builders;

import com.tivic.manager.mob.lote.impressao.lotedocumentoexterno.LoteDocumentoExterno;

public class LoteDocumentoExternoBuilder {
	
	private LoteDocumentoExterno documentoExterno;
	
	public LoteDocumentoExternoBuilder() {
		documentoExterno = new LoteDocumentoExterno();
	}
		
	public LoteDocumentoExternoBuilder setCdLoteImpressao(int cdLoteImpressao) {
		documentoExterno.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public LoteDocumentoExternoBuilder setCdDocumento(int cdDocumento) {
		documentoExterno.setCdDocumento(cdDocumento);
		return this;
	}
	
	public LoteDocumentoExternoBuilder setCdDocumentoExterno(int cdDocumentoExterno) {
		documentoExterno.setCdDocumentoExterno(cdDocumentoExterno);
		return this;
	}
	
	public LoteDocumentoExterno build() {
		return documentoExterno;
	}
}