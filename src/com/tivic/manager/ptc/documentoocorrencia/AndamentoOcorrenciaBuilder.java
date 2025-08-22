package com.tivic.manager.ptc.documentoocorrencia;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.DocumentoOcorrencia;

public class AndamentoOcorrenciaBuilder extends OcorrenciaBuilder {

	public AndamentoOcorrenciaBuilder(int cdDocumento, int cdUsuario) {
		super(cdDocumento, cdUsuario);
		
	}

	@Override
	public DocumentoOcorrencia build() {
		int cdTipoOcorrenciaAndamento = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_EM_ANDAMENTO", 0, 0, null);
		this.documentoOcorrencia.setDtOcorrencia(new GregorianCalendar());
		this.documentoOcorrencia.setTpVisibilidade(1);
		this.documentoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrenciaAndamento);
		this.documentoOcorrencia.setTxtOcorrencia("Solicitação em andamento.");
		
		return documentoOcorrencia;
	}
}
