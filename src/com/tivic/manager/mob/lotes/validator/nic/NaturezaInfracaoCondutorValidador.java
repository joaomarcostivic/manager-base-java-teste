package com.tivic.manager.mob.lotes.validator.nic;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.infracao.TipoResponsabilidadeInfracaoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class NaturezaInfracaoCondutorValidador  implements INICValidador {
	private InfracaoRepository infracaoRepository;
	
	public NaturezaInfracaoCondutorValidador() throws Exception {
		this.infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
	}
	
	@Override
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return getResponsabilidade(ait.getCdInfracao()) == TipoResponsabilidadeInfracaoEnum.MULTA_RESPONSABILIDADE_CONDUTOR.getKey();
	}
	
	public int getResponsabilidade(int cdInfracao) throws Exception {
		Infracao infracao = this.infracaoRepository.get(cdInfracao);
		return infracao.getTpResponsabilidade();
	}
}