package com.tivic.manager.ptc.protocolosv3.publicacao.validations;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class VerificaPublicacaoValidator implements Validator<AitMovimento> {
	private AitMovimentoRepository aitMovimentoRepository;
	
	public VerificaPublicacaoValidator() throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	@Override
	public void validate(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		if (aitMovimento.getTpStatus() != TipoStatusEnum.PUBLICACAO_NAI.getKey() && 
				aitMovimento.getTpStatus() != TipoStatusEnum.PUBLICACAO_NIP.getKey() &&
				aitMovimento.getTpStatus() != TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey()) {
			throw new Exception("Movimento não é uma publicação.");
		}
	}
}
