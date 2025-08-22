package com.tivic.manager.ptc.documentoocorrencia;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.DocumentoOcorrencia;

public class AnulacaoOcorrenciaBuilder extends OcorrenciaBuilder {

	public AnulacaoOcorrenciaBuilder(int cdDocumento, int cdUsuario) {
		super(cdDocumento, cdUsuario);
		
	}

	@Override
	public DocumentoOcorrencia build() {
		int cdTipoOcorrenciaIndeferimento = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_CANCELADO", 0, 0, null);
		this.documentoOcorrencia.setDtOcorrencia(new GregorianCalendar());
		this.documentoOcorrencia.setTpVisibilidade(1);
		this.documentoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrenciaIndeferimento);
		this.documentoOcorrencia.setTxtOcorrencia("Documento Cancelado.");
		
		return documentoOcorrencia;
	}

}
