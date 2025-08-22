package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class CPFFiciValidator implements IApresentacaoCondutorValidator {

	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection)
			throws Exception, ValidacaoException {
		ApresentacaoCondutor fici = apresentacaoCondutor.getApresentacaoCondutor();
		if(fici.getTpModeloCnh() == TipoCnhEnum.TP_CNH_NAO_HABILITADO.getKey() && fici.getNrCpfCnpj() == null)
			throw new ValidationException("É necessário informar CPF do condutor.");
	}

}
