package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;

public class UfCnhValidator implements IValidator<DadosProtocoloDTO>{
	private final String INDEFERIDA = 	"indeferido";

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		ApresentacaoCondutor fici = obj.getApresentacaoCondutor();
		int cdFaseIndeferida = FaseServices.getCdFaseByNome(INDEFERIDA, connection.getConnection());
		
		if (obj.getCdFase() == cdFaseIndeferida || 
				obj.getApresentacaoCondutor().getTpModeloCnh() == TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey()) {
			return;
		}
		
		if(fici.getUfCnh() == null) {
			throw new ValidationException("É necessário informar a UF da CNH.");
		}
	}
	
}
