package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DataValidator implements IApresentacaoCondutorValidator {

	private IAitMovimentoService aitMovimentoServices;
	
	public DataValidator() throws Exception {
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public void validate(ApresentacaoCondutorDTO fici, CustomConnection connection) throws ValidationException, Exception {
		AitMovimento nai = this.aitMovimentoServices.getMovimentoTpStatus(fici.getAit().getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
		if (fici.getDocumento().getDtProtocolo().before(nai.getDtMovimento())) {
			throw new ValidationException("Data da FICI é anterior a data da NAI");
		}
	}

}
