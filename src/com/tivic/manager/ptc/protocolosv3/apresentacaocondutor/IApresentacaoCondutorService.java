package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IApresentacaoCondutorService {
	public ApresentacaoCondutorDTO insert(ApresentacaoCondutorInsertDTO protocolo) throws Exception, ValidacaoException;
	public ApresentacaoCondutorDTO insert(ProtocoloInsertDTO protocolo, CustomConnection customConnection) throws Exception, ValidacaoException;
	public ApresentacaoCondutorDTO update(ProtocoloDTO protocolo) throws Exception, ValidacaoException;
	public ApresentacaoCondutorDTO update(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception;
	public ApresentacaoCondutorDTO publicar(ProtocoloDTO protocolo) throws Exception, ValidacaoException;
	public ApresentacaoCondutorDTO publicar(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception;
	public ApresentacaoCondutorDTO cancelar(ProtocoloDTO protocolo) throws Exception, ValidacaoException;
	public ApresentacaoCondutorDTO cancelar(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception, ValidacaoException;

}
