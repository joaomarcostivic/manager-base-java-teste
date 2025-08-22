package com.tivic.manager.ptc.protocolosv3;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IProtocoloDTOGet {
	public ProtocoloDTO get(int cdDocumento) throws Exception, ValidacaoException;
}
