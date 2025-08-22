package com.tivic.manager.wsdl.detran.mg.validators.condutor;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class CodigoPaisCnhValidator implements ICondutorRegistroInfracaoValidator {
	private String CNHEXTRANGEIRA = "EX";

	@Override
	public void validate(Ait ait, CustomConnection connection) throws Exception, ValidacaoException {
		if(verificaModeloCNH(ait) && verificaUFCNH(ait)) {
			throw new ValidacaoException("O País da CNH deve ser informado.");
		}
	}
	
	private boolean verificaModeloCNH(Ait ait) {		
		return ait.getTpCnhCondutor() == TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey();
	}
	
	private boolean verificaUFCNH(Ait ait) {		
		return ait.getUfCnhAutuacao() == CNHEXTRANGEIRA;
	}
}
