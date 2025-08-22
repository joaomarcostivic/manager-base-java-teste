package com.tivic.manager.ptc.documentoocorrencia;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.DocumentoOcorrencia;

public class ImpressaoOcorrenciaBuilder extends OcorrenciaBuilder{

	public ImpressaoOcorrenciaBuilder(int cdDocumento, int cdUsuario) {
		super(cdDocumento, cdUsuario);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocumentoOcorrencia build() {
		int cdTipoOcorrenciaIndeferimento = ParametroServices.getValorOfParametroAsInteger("PNE_DOCUMENTO_IMPRESSAO", 0, 0, null);
		this.documentoOcorrencia.setDtOcorrencia(new GregorianCalendar());
		this.documentoOcorrencia.setTpVisibilidade(1);
		this.documentoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrenciaIndeferimento);
		this.documentoOcorrencia.setTxtOcorrencia("Documento impresso.");
		
		return documentoOcorrencia;
	}
	
}
