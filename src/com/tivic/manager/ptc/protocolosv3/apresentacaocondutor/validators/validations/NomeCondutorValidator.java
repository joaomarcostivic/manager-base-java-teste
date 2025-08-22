package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class NomeCondutorValidator implements IApresentacaoCondutorValidator {
	
	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection customConnection) throws Exception, ValidacaoException {
		if(apresentacaoCondutor.getApresentacaoCondutor().getTpModeloCnh() == TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey() &&
			(apresentacaoCondutor.getApresentacaoCondutor().getNmCondutor() == null || apresentacaoCondutor.getApresentacaoCondutor().getNmCondutor().trim().equals(""))) {
			throw new ValidacaoException("O nome do condutor é obrigatório.");
		}
	}
}
