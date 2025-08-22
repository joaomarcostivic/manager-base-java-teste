package com.tivic.manager.mob.aitmovimento.validators;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CancelamentoNip implements Validator<AitMovimento>{
	private IAitMovimentoService aitMovimentoService;
	
	public CancelamentoNip() throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public void validate(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		AitMovimento movimento = this.aitMovimentoService.getUltimoMovimento(aitMovimento.getCdAit());
		if(movimento.getTpStatus() == TipoStatusEnum.CANCELAMENTO_NIP.getKey()) {
			throw new BadRequestException("Esta NIP já foi cancelada");
		}
	}

}
