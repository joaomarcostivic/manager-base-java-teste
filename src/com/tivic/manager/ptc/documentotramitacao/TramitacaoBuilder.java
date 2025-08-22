package com.tivic.manager.ptc.documentotramitacao;

import com.tivic.manager.ptc.DocumentoTramitacao;

public abstract class TramitacaoBuilder {
	protected DocumentoTramitacao documentoTramitacao;
	
	public TramitacaoBuilder(int cdDocumento) {
		this.documentoTramitacao = new DocumentoTramitacao();
		documentoTramitacao.setCdDocumento(cdDocumento);
	}
	
	public abstract DocumentoTramitacao build();
}
