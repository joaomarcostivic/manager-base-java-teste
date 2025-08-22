package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.ptc.protocolosv3.builders.SituacaoDocumentoResultado;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class UfCnhValidator implements IApresentacaoCondutorValidator {

	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection)
			throws Exception, ValidacaoException {
		
		SituacaoDocumentoResultado resultadoSituacao = new SituacaoDocumentoResultado();
		ApresentacaoCondutor fici = apresentacaoCondutor.getApresentacaoCondutor();
		
		if (resultadoSituacao.isIndeferido(apresentacaoCondutor.getDocumento().getCdSituacaoDocumento()) || 
				apresentacaoCondutor.getApresentacaoCondutor().getTpModeloCnh() != TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey()) {
			return;
		}
		
		if(fici.getUfCnh() == null) {
			throw new ValidationException("É necessário informar a UF da CNH.");
		}
	}

}
