package com.tivic.manager.mob;

import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class AitMovimentoDocumentoBuilder {
	private DadosProtocoloDTO dadosProtocoloDTO;
	private AitMovimentoDocumento aitMovimentoDocumento;
	
	public AitMovimentoDocumentoBuilder(DadosProtocoloDTO dadosProtocoloDTO, AitMovimentoDocumento aitMovimentoDocumento) {
		this.dadosProtocoloDTO = dadosProtocoloDTO;
		this.aitMovimentoDocumento = aitMovimentoDocumento;
	}
	
	public AitMovimentoDocumentoBuilder ait() {
		aitMovimentoDocumento.setCdAit(dadosProtocoloDTO.getAit().getCdAit());
		
		return this;
	}
	
	public AitMovimentoDocumentoBuilder movimento() {
		aitMovimentoDocumento.setCdMovimento(dadosProtocoloDTO.getMovimento().getCdMovimento());
		
		return this;
	}
	
	public AitMovimentoDocumentoBuilder documento() {
		aitMovimentoDocumento.setCdDocumento(dadosProtocoloDTO.getCdDocumento());
		
		return this;
	}
	
	public AitMovimentoDocumento build() {
		return aitMovimentoDocumento;
	}
}
