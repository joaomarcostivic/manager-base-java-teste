package com.tivic.manager.wsdl.detran.mg.validators.condutor;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class TipoDocumentoValidator implements ICondutorRegistroInfracaoValidator {

	@Override
	public void validate(Ait ait, CustomConnection connection) throws Exception, ValidacaoException {
		if(ait.getTpCnhCondutor() == TipoCnhEnum.TP_CNH_NAO_HABILITADO.getKey() && ait.getTpDocumento() == 0) {
			throw new ValidacaoException("O tipo de documento precisa ser informado.");
		}
	}

}
