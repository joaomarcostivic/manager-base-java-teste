package com.tivic.manager.mob.ait;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.EfeitoSuspensivoDTO;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.sol.cdi.BeansFactory;

public class AitEfeitoSuspensaoRetomada{
	AitRepository aitRepository;
	IAitMovimentoService aitMovimentoServices;
	IAitService aitService;
	
	public AitEfeitoSuspensaoRetomada() throws Exception {
		aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		aitService =  (IAitService) BeansFactory.get(IAitService.class);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}
	
	public AitMovimento criarMovimentoSuspensaoRetomadaAuto(EfeitoSuspensivoDTO efeitoSuspensivoDTO,
			CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = new AitMovimentoSuspensaoBuilder(efeitoSuspensivoDTO).build();
		AitMovimento aitMovimentoSuspensao = aitMovimentoServices.getMovimentoTpStatus(efeitoSuspensivoDTO.getCdAit(), aitMovimento.getTpStatus()); 
		if(aitMovimentoSuspensao.getCdMovimento() > 0) 
			return aitMovimentoSuspensao;
		aitMovimentoServices.save(aitMovimento, customConnection);
		return aitMovimento;
	}
}
