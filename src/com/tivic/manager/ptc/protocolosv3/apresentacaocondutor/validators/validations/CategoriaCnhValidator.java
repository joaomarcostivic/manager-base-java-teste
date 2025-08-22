package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.TipoModeloCnhEnum;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.sol.connection.CustomConnection;

public class CategoriaCnhValidator implements IApresentacaoCondutorValidator {
	
	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection) throws ValidationException, Exception {	
		ApresentacaoCondutor fici = apresentacaoCondutor.getApresentacaoCondutor();
		
		if (checkValidacaoNecessaria(apresentacaoCondutor)) {
			return;
		}
		
		if((fici.getNrCnh() != null && !fici.getNrCnh().trim().isEmpty()) && fici.getTpCategoriaCnh() < 0) {
			throw new ValidationException("É necessário informar a categoria da CNH.");
		}
	}
	private boolean checkValidacaoNecessaria(ApresentacaoCondutorDTO obj) throws Exception {
		int cdSituacaoIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO",0);
		
		if(cdSituacaoIndeferida <= 0) 
			throw new Exception("Parâmetro CD_SITUACAO_DOCUMENTO_INDEFERIDO não configurado");
		
		return obj.getDocumento().getCdSituacaoDocumento() == cdSituacaoIndeferida || obj.getApresentacaoCondutor().getTpModeloCnh() == TipoModeloCnhEnum.NAO_INFORMADO.getKey();
	}

}
