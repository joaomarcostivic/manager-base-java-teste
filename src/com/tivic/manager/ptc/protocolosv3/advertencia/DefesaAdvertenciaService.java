package com.tivic.manager.ptc.protocolosv3.advertencia;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloService;
import com.tivic.manager.ptc.protocolosv3.advertencia.validator.AdvertenciaValidatorBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class DefesaAdvertenciaService extends ProtocoloService {
	
	public DefesaAdvertenciaService() throws Exception {
		super();
	}
	
	@Override
	public ProtocoloDTO insert(ProtocoloInsertDTO dadosProtocolo, CustomConnection customConnection) throws Exception, ValidacaoException {
		ProtocoloDTO protocoloDTO = new ProtocoloDTO();
	    protocoloDTO.getAit().setCdAit(dadosProtocolo.getCdAit());
		try {
			customConnection.initConnection(true);
			new AdvertenciaValidatorBuilder().validate(protocoloDTO, customConnection);
			protocoloDTO = super.insert(dadosProtocolo, customConnection);
			customConnection.finishConnection();
			if(super.isEnvioAutomatico) 
				super.enviarDetran(protocoloDTO.getAitMovimento());
			return protocoloDTO;
		} finally {
			customConnection.closeConnection();
		}		
	}
}
