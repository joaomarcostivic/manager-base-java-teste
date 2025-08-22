package com.tivic.manager.ptc.protocolosv3.ocorrencia;

import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IProtocoloOcorrenciaService {
	public List<ProtocoloOcorrenciaDTO> find(Integer cdDocumento) throws Exception, ValidacaoException;
}
