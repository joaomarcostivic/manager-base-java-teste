package com.tivic.manager.ptc.protocolosv3.jari;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloService;
import com.tivic.manager.ptc.protocolosv3.jari.validators.JariValidatorBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class JariService extends ProtocoloService {
	
	public JariService() throws Exception { 
		super();
	}
	
	@Override
	public ProtocoloDTO update(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		return super.update(protocolo, customConnection);
	}
	
	@Override
	public ProtocoloDTO insert(ProtocoloInsertDTO dadosProtocolo, CustomConnection customConnection) throws Exception, ValidacaoException {
	    ProtocoloDTO protocoloDTO = new ProtocoloDTO();
	    protocoloDTO.getAit().setCdAit(dadosProtocolo.getCdAit());
	    protocoloDTO.getDocumento().setDtProtocolo(dadosProtocolo.getDtProtocolo());
	    try {
	        customConnection.initConnection(true);
	        new JariValidatorBuilder().validate(protocoloDTO, customConnection);
	        protocoloDTO = super.insert(dadosProtocolo, customConnection);
	        customConnection.finishConnection();
	        if (super.isEnvioAutomatico)
	            super.enviarDetran(protocoloDTO.getAitMovimento());
	        return protocoloDTO;
	    } finally {
	        customConnection.closeConnection();
	    }       
	}

}
