package com.tivic.manager.ptc.protocolosv3.validators.builders;

import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;

public class AitMovimentoDocumentoBuilder {
	private ProtocoloDTO protocolo;
	private AitMovimentoDocumento aitMovimentoDocumento;
	
	public AitMovimentoDocumentoBuilder(ProtocoloDTO protocolo, AitMovimentoDocumento aitMovimentoDocumento) {
		this.protocolo = protocolo;
		this.aitMovimentoDocumento = aitMovimentoDocumento;
	}
	
	public AitMovimentoDocumentoBuilder(ProtocoloDTO protocolo) {
		this.protocolo = protocolo;
		this.aitMovimentoDocumento = new AitMovimentoDocumento();
	}
	
	public AitMovimentoDocumentoBuilder ait() {
		aitMovimentoDocumento.setCdAit(protocolo.getAit().getCdAit());
		
		return this;
	}
	
	public AitMovimentoDocumentoBuilder movimento() {
		aitMovimentoDocumento.setCdMovimento(protocolo.getAitMovimento().getCdMovimento());
		
		return this;
	}
	
	public AitMovimentoDocumentoBuilder documento() {
		aitMovimentoDocumento.setCdDocumento(protocolo.getDocumento().getCdDocumento());
		
		return this;
	}
	
	public AitMovimentoDocumento build() {
		return aitMovimentoDocumento;
	}
}
