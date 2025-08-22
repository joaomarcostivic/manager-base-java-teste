package com.tivic.manager.ptc.protocolosv3.cetran;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloService;
import com.tivic.manager.ptc.protocolosv3.cetran.validators.CetranValidatorBuilder;
import com.tivic.sol.connection.CustomConnection;

public class CetranService extends ProtocoloService {

	public CetranService() throws Exception {
		super();
	}

	@Override
	public ProtocoloDTO update(ProtocoloDTO protocolo) throws Exception {
		return super.update(protocolo);
	}
	
	@Override
	public ProtocoloDTO insert(ProtocoloInsertDTO dadosProtocolo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		ProtocoloDTO protocoloDTO = new ProtocoloDTO();
	    protocoloDTO.getAit().setCdAit(dadosProtocolo.getCdAit());
	    protocoloDTO.getDocumento().setDtProtocolo(dadosProtocolo.getDtProtocolo());
		try {
			customConnection.initConnection(true);
			new CetranValidatorBuilder().validate(protocoloDTO, customConnection);
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
