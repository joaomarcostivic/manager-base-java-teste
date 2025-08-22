package com.tivic.manager.wsdl.detran.mg.validators.condutor;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class UFCnhValidator implements ICondutorRegistroInfracaoValidator {

	@Override
	public void validate(Ait ait, CustomConnection connection) throws Exception, ValidacaoException {
		if(ait.getUfCnhCondutor() == null && (existsModeloCnh(ait) || existsNrCnh(ait))) {
			throw new ValidacaoException("O UF da CNH deve ser informado."); 
		}
	}
	
	private boolean existsModeloCnh(Ait ait) {
		return ait.getTpCnhCondutor() > 0 && ait.getTpCnhCondutor() < TipoCnhEnum.TP_CNH_NAO_HABILITADO.getKey();
	}
	
	private boolean existsNrCnh(Ait ait) {
		return ait.getNrCnhCondutor() != null && !ait.getNrCnhCondutor().trim().equals("");
	}
}
