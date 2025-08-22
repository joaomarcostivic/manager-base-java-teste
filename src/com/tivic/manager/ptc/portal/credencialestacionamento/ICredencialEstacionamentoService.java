package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.sol.connection.CustomConnection;

public interface ICredencialEstacionamentoService {

	public DocumentoPortalResponse solicitar(CartaoEstacionamentoRequest documentoRecurso, CustomConnection customConnection) throws Exception;
	public byte[] getCartaoIdoso(int cdDocumento) throws Exception;
	public byte[] getCartaoIdoso(int cdDocumento, CustomConnection customConnection) throws Exception;
	public byte[] getCartaoPcd(int cdDocumento) throws Exception;
	public byte[] getCartaoPcd(int cdDocumento, CustomConnection customConnection) throws Exception;
	public void deferir(JulgamentoProtocoloEstacionamento julgamentoEstacionamento) throws Exception;
	public void indeferir(JulgamentoProtocoloEstacionamento julgamentoEstacionamento) throws Exception;
}
