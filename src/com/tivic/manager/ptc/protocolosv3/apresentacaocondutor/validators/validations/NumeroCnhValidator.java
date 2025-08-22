package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class NumeroCnhValidator implements IApresentacaoCondutorValidator {
	
	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection customConnection) throws Exception, ValidacaoException {
		if(apresentacaoCondutor.getApresentacaoCondutor().getTpModeloCnh() != TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey() && 
				apresentacaoCondutor.getApresentacaoCondutor().getTpModeloCnh() != TipoCnhEnum.TP_CNH_NAO_HABILITADO.getKey() &&
				(apresentacaoCondutor.getApresentacaoCondutor().getNrCnh() == null || apresentacaoCondutor.getApresentacaoCondutor().getNrCnh().trim().equals(""))) {
			throw new ValidacaoException("O número da CNH é obrigatório.");
		}
	}
	
}
