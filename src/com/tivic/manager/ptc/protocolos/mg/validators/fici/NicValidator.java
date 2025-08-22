package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;

public class NicValidator implements IValidator<DadosProtocoloDTO>{
	private final int NAO_IDENTIFICACAO_CONDUTOR = 50002;
	private final int NAO_IDENTIFICACAO_CONDUTOR_JURIDICO = 50020;
	
	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		Infracao infracao = InfracaoDAO.get(obj.getAit().getCdInfracao(), connection.getConnection());
		if(infracao.getNrCodDetran() == NAO_IDENTIFICACAO_CONDUTOR || infracao.getNrCodDetran() == NAO_IDENTIFICACAO_CONDUTOR_JURIDICO) {
			throw new ValidationException("Infração não pode ser NIC");
		}
		
	}
	
}
