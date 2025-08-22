package com.tivic.manager.ptc.documentoocorrencia;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.DocumentoOcorrencia;

public class DeferimentoOcorrenciaBuilder extends OcorrenciaBuilder {

	public DeferimentoOcorrenciaBuilder(int cdDocumento, int cdUsuario) {
		super(cdDocumento, cdUsuario);
	}

	@Override
	public DocumentoOcorrencia build() {
		int cdTipoOcorrenciaDeferimento = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_DEFERIDO", 0);
		this.documentoOcorrencia.setDtOcorrencia(new GregorianCalendar());
		this.documentoOcorrencia.setTpVisibilidade(1);
		this.documentoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrenciaDeferimento);
		this.documentoOcorrencia.setTxtOcorrencia("Documento deferido.");
		
		return documentoOcorrencia;
	}

}
