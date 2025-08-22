package com.tivic.manager.ptc.protocolos.mg.defesa;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.IProtocoloRecursoServices;
import com.tivic.sol.cdi.BeansFactory;
import javax.ws.rs.BadRequestException;

public class DefesaServices implements IProtocoloRecursoServices{

	IAitMovimentoService aitMovimentoServices;
	
	public DefesaServices() throws Exception {
		aitMovimentoServices = (IAitMovimentoService)BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public DadosProtocoloDTO insertProtocolo(DadosProtocoloDTO protocolo) throws BadRequestException, Exception {
		return insertProtocolo(protocolo, new CustomConnection());
	}

	@Override
	public DadosProtocoloDTO insertProtocolo(DadosProtocoloDTO protocolo, CustomConnection connection) throws BadRequestException, Exception{
		aitMovimentoServices.save(protocolo.getMovimento(), connection);
		return protocolo;
	}

}
