package com.tivic.manager.ptc.protocolos.mg.fici;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.IProtocoloRecursoServices;
import com.tivic.sol.cdi.BeansFactory;
import javax.ws.rs.BadRequestException;

public class FiciServices implements IProtocoloRecursoServices{
	
	private ApresentacaoCondutorRepository apresentacaoCondutorRepository;
	private IAitMovimentoService aitMovimentoServices;
	
	public FiciServices() throws Exception{
		apresentacaoCondutorRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
		aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public DadosProtocoloDTO insertProtocolo(DadosProtocoloDTO protocolo) throws BadRequestException, Exception{
		return insertProtocolo(protocolo, null);
	}

	@Override
	public DadosProtocoloDTO insertProtocolo(DadosProtocoloDTO protocolo, CustomConnection connection) throws BadRequestException, Exception{
		protocolo = new DadosFiciBuilder(protocolo).movimento().apresentacaoCondutor().build();
		
		aitMovimentoServices.save(protocolo.getMovimento(), connection);
		
		apresentacaoCondutorRepository.insert(protocolo.getApresentacaoCondutor(), connection);
		
		return protocolo;
	}

}
