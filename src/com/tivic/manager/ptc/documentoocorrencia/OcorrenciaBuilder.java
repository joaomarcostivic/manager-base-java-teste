package com.tivic.manager.ptc.documentoocorrencia;

import com.tivic.manager.ptc.DocumentoOcorrencia;

public abstract class OcorrenciaBuilder {
	protected DocumentoOcorrencia documentoOcorrencia;
	
	public OcorrenciaBuilder(int cdDocumento, int cdUsuario) {
		this.documentoOcorrencia = new DocumentoOcorrencia();
		documentoOcorrencia.setCdDocumento(cdDocumento);
		documentoOcorrencia.setCdUsuario(cdUsuario);
	}
	
	public abstract DocumentoOcorrencia build();
}
