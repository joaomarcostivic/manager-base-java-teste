package com.tivic.manager.mob.aitmovimentodocumento.builders;

import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;

public class AitMovimentoDocumentoBuilder {
	private AitMovimentoDocumentoDTO dadosProtocoloDTO;
	private AitMovimentoDocumento aitMovimentoDocumento;
	
	public AitMovimentoDocumentoBuilder(AitMovimentoDocumentoDTO dadosProtocoloDTO, AitMovimentoDocumento aitMovimentoDocumento) {
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
		aitMovimentoDocumento.setCdDocumento(dadosProtocoloDTO.getDocumento().getCdDocumento());
		
		return this;
	}
	
	public AitMovimentoDocumento build() {
		return aitMovimentoDocumento;
	}
}
