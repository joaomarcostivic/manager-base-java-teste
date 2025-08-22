package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class ModeloCnhValidator implements IApresentacaoCondutorValidator {

	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutorDTO, CustomConnection connection) throws Exception, ValidacaoException {
		int situacaoDocumento = apresentacaoCondutorDTO.getDocumento().getCdSituacaoDocumento();
		int situacaoIndeferida =  Integer.valueOf(ParametroServices.getValorOfParametro("CD_SITUACAO_DOCUMENTO_INDEFERIDO", connection.getConnection()));
		if(situacaoDocumento == situacaoIndeferida) {
			return;
		}
		
		if(apresentacaoCondutorDTO.getApresentacaoCondutor().getTpModeloCnh() < 0) {
			throw new ValidationException("É necessário informar o modelo da CNH.");
		}
		
		if(apresentacaoCondutorDTO.getApresentacaoCondutor().getTpModeloCnh() == TabelasAuxiliaresMG.TP_CNH_HABILITACAO_ESTRANGEIRA) {
			validateCnhEstrangeira(apresentacaoCondutorDTO.getApresentacaoCondutor());
		}
	}
	
	private void validateCnhEstrangeira(ApresentacaoCondutor fici) throws ValidationException, Exception{
		if(fici.getIdPaisCnh() == null) {
			throw new ValidationException("É necessário informar o país da CNH em casos de CNH Estrangeira.");
		}
	}

}
