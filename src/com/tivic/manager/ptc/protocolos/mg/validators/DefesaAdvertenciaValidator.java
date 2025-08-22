package com.tivic.manager.ptc.protocolos.mg.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.mg.validators.defesa.NotExistsAdvertenciaDefesaPreviaValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.defesa.NotExistsDefesaPreviaValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.defesa.NotExistsJariValidator;
import com.tivic.manager.ptc.protocolos.validators.CancelamentoValidator;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.ptc.protocolos.validators.NaiRegistradaValidator;
import com.tivic.manager.ptc.protocolos.validators.RecursoValidator;
import com.tivic.sol.connection.CustomConnection;

public class DefesaAdvertenciaValidator implements RecursoValidator {

private List<IValidator<DadosProtocoloDTO>> validators;
	
	public DefesaAdvertenciaValidator() {
		this.validators = new ArrayList<IValidator<DadosProtocoloDTO>>();
		this.validators.add(new NaiRegistradaValidator());
		this.validators.add(new NotExistsJariValidator());
		this.validators.add(new NotExistsAdvertenciaDefesaPreviaValidator());
		this.validators.add(new CancelamentoValidator());
	}
	
	@Override
	public void validate(DadosProtocoloDTO protocolo, CustomConnection connection) throws ValidationException, Exception {
		for(IValidator<DadosProtocoloDTO> validator: validators) {
			validator.validate(protocolo, connection);
		}
	}

}
