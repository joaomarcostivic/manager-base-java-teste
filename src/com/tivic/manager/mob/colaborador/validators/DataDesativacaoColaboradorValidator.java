package com.tivic.manager.mob.colaborador.validators;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.colaborador.ColaboradorDTO;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class DataDesativacaoColaboradorValidator implements Validator<ColaboradorDTO> {
	@Override
	public void validate(ColaboradorDTO colaboradorDTO, CustomConnection customConnection) throws Exception {
		if(colaboradorDTO.getDtVinculo().after(colaboradorDTO.getDtVinculoHistorico())) {
			throw new ValidationException("A data da desativação não deve ser anterior a data de vínculo.");
		}
	}
}
