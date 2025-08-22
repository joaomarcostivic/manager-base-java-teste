package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;

public class ApresentacaoCondutorDTO extends ProtocoloDTO {

	private ApresentacaoCondutor apresentacaoCondutor;

	public ApresentacaoCondutor getApresentacaoCondutor() {
		return apresentacaoCondutor;
	}

	public void setApresentacaoCondutor(ApresentacaoCondutor apresentacaoCondutor) {
		this.apresentacaoCondutor = apresentacaoCondutor;
	}
}
