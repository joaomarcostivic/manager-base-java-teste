package com.tivic.manager.ptc.documentoocorrencia;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.DocumentoOcorrencia;

public class IndeferimentoOcorrenciaBuilder extends OcorrenciaBuilder {
	public IndeferimentoOcorrenciaBuilder(int cdDocumento, int cdUsuario) {
		super(cdDocumento, cdUsuario);
	}

	@Override
	public DocumentoOcorrencia build() {
		int cdTipoOcorrenciaIndeferimento = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INDEFERIDO", 0);
		this.documentoOcorrencia.setDtOcorrencia(new GregorianCalendar());
		this.documentoOcorrencia.setTpVisibilidade(1);
		this.documentoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrenciaIndeferimento);
		this.documentoOcorrencia.setTxtOcorrencia("Documento indeferido.");
		
		return documentoOcorrencia;
	}
}
