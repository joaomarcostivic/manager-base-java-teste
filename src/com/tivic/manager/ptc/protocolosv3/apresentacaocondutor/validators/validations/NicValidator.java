package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.infracao.TipoInfracaoEnum;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class NicValidator implements IApresentacaoCondutorValidator {

	private InfracaoRepository infracaoRepository;
	
	public NicValidator() throws Exception {
		this.infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
	}
	
	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection) throws Exception, ValidacaoException {
		Infracao infracao = this.infracaoRepository.get(apresentacaoCondutor.getAit().getCdInfracao(), connection);
		if(infracao.getNrCodDetran() == TipoInfracaoEnum.MULTA_NAO_INDENTIFICACAO_CONDUTOR_FISICO.getKey() || infracao.getNrCodDetran() == TipoInfracaoEnum.MULTA_NAO_INDENTIFICACAO_CONDUTOR_JURIDICO.getKey()) {
			throw new ValidationException("Infração não pode ser NIC");
		}
	}

}
