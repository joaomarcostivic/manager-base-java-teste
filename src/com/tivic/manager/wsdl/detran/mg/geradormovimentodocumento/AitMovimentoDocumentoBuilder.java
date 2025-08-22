package com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.ptc.Documento;

public class AitMovimentoDocumentoBuilder {

	private AitMovimentoDocumento aitMovimentoDocumento;
	
	public AitMovimentoDocumentoBuilder(AitMovimento aitMovimento, Documento documento) {
		this.aitMovimentoDocumento = new AitMovimentoDocumento();
		this.aitMovimentoDocumento.setCdAit(aitMovimento.getCdAit());
		this.aitMovimentoDocumento.setCdMovimento(aitMovimento.getCdMovimento());
		this.aitMovimentoDocumento.setCdDocumento(documento.getCdDocumento());
	}
	
	public AitMovimentoDocumento build() {
		return this.aitMovimentoDocumento;
	}
}
