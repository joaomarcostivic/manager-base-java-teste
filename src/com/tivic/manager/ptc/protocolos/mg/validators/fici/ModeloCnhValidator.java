package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;

public class ModeloCnhValidator implements IValidator<DadosProtocoloDTO>{
	private final String INDEFERIDO = "indeferido";
	
	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		ApresentacaoCondutor fici = obj.getApresentacaoCondutor();
		int cdFaseIndeferida = FaseServices.getCdFaseByNome(INDEFERIDO, connection.getConnection());
		
		if(obj.getCdFase() == cdFaseIndeferida) {
			return;
		}
		
		if(fici.getTpModeloCnh() < 0) {
			throw new ValidationException("É necessário informar o modelo da CNH.");
		}
		
		if(fici.getTpModeloCnh() == TabelasAuxiliaresMG.TP_CNH_HABILITACAO_ESTRANGEIRA) {
			validateCnhEstrangeira(fici);
		}
	}
	
	private void validateCnhEstrangeira(ApresentacaoCondutor fici) throws ValidationException, Exception{
		if(fici.getIdPaisCnh() == null) {
			throw new ValidationException("É necessário informar o país da CNH em casos de CNH Estranheira.");
		}
	}

}
