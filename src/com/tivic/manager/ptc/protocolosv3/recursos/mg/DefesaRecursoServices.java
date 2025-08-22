package com.tivic.manager.ptc.protocolosv3.recursos.mg;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.recursos.IProtocoloRecursoServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DefesaRecursoServices implements IProtocoloRecursoServices{
	
	IAitMovimentoService aitMovimentoServices;
	
	public DefesaRecursoServices() throws Exception {
		aitMovimentoServices = (IAitMovimentoService)BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public ProtocoloDTO insertProtocolo(ProtocoloDTO protocolo) throws BadRequestException, Exception {
		return insertProtocolo(protocolo, new CustomConnection());
	}

	@Override
	public ProtocoloDTO insertProtocolo(ProtocoloDTO protocolo, CustomConnection connection) throws BadRequestException, Exception{
		aitMovimentoServices.save(protocolo.getAitMovimento(), connection);
		return protocolo;
	}

}
