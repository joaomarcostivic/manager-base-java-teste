package com.tivic.manager.mob.aitmovimento.validators;


import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class UniqueResultadoValidator implements IValidator<AitMovimento>{
	private IAitMovimentoService aitMovimentoServices;

	public UniqueResultadoValidator() throws ValidationException, Exception {
		aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	@Override
	public void validate(AitMovimento obj, CustomConnection connection) throws ValidationException, Exception {
		AitMovimento movimentoDeferido = aitMovimentoServices.getMovimentoTpStatus(obj.getCdAit(), TipoStatusEnum.DEFESA_DEFERIDA.getKey());
		AitMovimento movimentoIndeferido = aitMovimentoServices.getMovimentoTpStatus(obj.getCdAit(), TipoStatusEnum.DEFESA_INDEFERIDA.getKey());
		
		if (movimentoDeferido.getCdMovimento() > 0) {
			throw new ValidationException("Já existe um resultado de Defesa Prévia atrelada ao AIT.");
		}
		
		if (movimentoIndeferido.getCdMovimento() > 0) {
			throw new ValidationException("Já existe um resultado de Defesa Prévia atrelada ao AIT.");
		}
	}
}
