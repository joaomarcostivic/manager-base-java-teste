package com.tivic.manager.ptc.protocolosv3.documento.ocorrencia;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.DocumentoOcorrencia;;

public class SituacaoDeferido implements ISituacaoDocumentoOcorrencia {
	
	public DocumentoOcorrencia montarDocumentoOcorrencia(DocumentoOcorrencia documentoOcorrencia) throws Exception {
		documentoOcorrencia.setCdTipoOcorrencia(findTipoOcorrencia());
		return documentoOcorrencia;
	}

	public int findTipoOcorrencia() throws Exception {
		int cdDeferido = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEFERIDA", 0);
		if(cdDeferido > 0)
			return cdDeferido;
		
		throw new Exception("Valor para o Parâmetro CD_TIPO_OCORRENCIA_DEFERIDA não encontrado.");
	}
}
