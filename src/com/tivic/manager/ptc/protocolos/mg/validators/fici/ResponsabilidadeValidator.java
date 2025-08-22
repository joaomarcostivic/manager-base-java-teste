package com.tivic.manager.ptc.protocolos.mg.validators.fici;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;

public class ResponsabilidadeValidator implements IValidator<DadosProtocoloDTO>{
	private final int RESPONSABILIDADE_CONDUTOR = 0;
	
	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		Infracao infracao = InfracaoDAO.get(obj.getAit().getCdInfracao(), connection.getConnection());
		
		if(infracao.getTpResponsabilidade() != RESPONSABILIDADE_CONDUTOR) {
			throw new ValidationException("Não é possivel lançar FICI para infrações de responsabilidade do proprietário.");
		}
	}

}
