package com.tivic.manager.ptc.protocolosv3.resultado;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IProtocoloResultadoService {
	public ResultadoDTO deferir(ResultadoDTO resultado) throws Exception, ValidacaoException;
	public ResultadoDTO indeferir(ResultadoDTO resultado) throws Exception, ValidacaoException;
	public ProtocoloDTO cancelarJulgamento(ProtocoloDTO dadosProtocolo) throws ValidacaoException, Exception;
}
