package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.infracao.TipoResponsabilidadeInfracaoEnum;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ResponsabilidadeValidator implements IApresentacaoCondutorValidator {
	
	private InfracaoRepository infracaoRepository;
	
	public ResponsabilidadeValidator() throws Exception {
		this.infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
	}
	
	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection) throws Exception, ValidacaoException {
		Infracao infracao = this.infracaoRepository.get(apresentacaoCondutor.getAit().getCdInfracao(), connection);
		if(infracao.getTpResponsabilidade() == TipoResponsabilidadeInfracaoEnum.MULTA_RESPONSABILIDADE_PROPRIETARIO.getKey()) {
			throw new ValidationException("Não é possivel lançar FICI para infrações de responsabilidade do proprietário.");
		}
	}

}
