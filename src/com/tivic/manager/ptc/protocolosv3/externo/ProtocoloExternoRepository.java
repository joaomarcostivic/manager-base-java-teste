package com.tivic.manager.ptc.protocolosv3.externo;

import com.tivic.sol.connection.CustomConnection;

public interface ProtocoloExternoRepository {
	public ProtocoloExterno insert(ProtocoloExterno protocoloExterno, CustomConnection customConnection) throws Exception; 
	public ProtocoloExterno update(ProtocoloExterno protocoloExterno, CustomConnection customConnection) throws Exception;
	public ProtocoloExterno get(int cdDocumentoExterno) throws Exception;
	public ProtocoloExterno get(int cdDocumentoExterno, CustomConnection customConnection) throws Exception;

}