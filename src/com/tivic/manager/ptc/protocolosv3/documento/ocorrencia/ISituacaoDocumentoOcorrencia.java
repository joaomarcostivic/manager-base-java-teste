package com.tivic.manager.ptc.protocolosv3.documento.ocorrencia;

import com.tivic.manager.ptc.DocumentoOcorrencia;

public interface ISituacaoDocumentoOcorrencia {
	DocumentoOcorrencia montarDocumentoOcorrencia(DocumentoOcorrencia documentoOcorrencia) throws Exception;
	int findTipoOcorrencia() throws Exception;
}
