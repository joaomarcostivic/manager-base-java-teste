package com.tivic.manager.mob.aitmovimento.validators;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ValidadorMovimentoCancelamentoNip implements Validator<AitMovimento> {
	private IAitMovimentoService aitMovimentoService;
	
	public ValidadorMovimentoCancelamentoNip() throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);		
	}

	@Override
	public void validate(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
	    AitMovimento movimentoMultaPaga = this.aitMovimentoService.getStatusMovimento(aitMovimento.getCdAit(), TipoStatusEnum.MULTA_PAGA.getKey());
	    if (movimentoMultaPaga != null && movimentoMultaPaga.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRADO.getKey()) {
	        AitMovimento movimentoAdvertencia = this.aitMovimentoService.getStatusMovimento(aitMovimento.getCdAit(), TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
	        if (movimentoAdvertencia != null && movimentoAdvertencia.getDtMovimento() != null && movimentoMultaPaga.getDtMovimento().after(movimentoAdvertencia.getDtMovimento())) {
	            throw new BadRequestException("Não é permitido cancelar penalidade com multa paga já registrada no Detran.");
	        }
	    }
	}

}
