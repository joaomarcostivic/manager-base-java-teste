package com.tivic.manager.ptc.protocolos.mg.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.CategoriaCnhValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.DataValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.FiciExistsValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.ModeloCnhValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.NaiValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.NicValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.ResponsabilidadeValidator;
import com.tivic.manager.ptc.protocolos.mg.validators.fici.UfCnhValidator;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.ptc.protocolos.validators.RecursoValidator;

public class FiciValidator implements RecursoValidator {
	private List<IValidator<DadosProtocoloDTO>> validators;
	
	public FiciValidator() {
		this.validators = new ArrayList<IValidator<DadosProtocoloDTO>>();
		this.validators.add(new FiciExistsValidator());
		this.validators.add(new NaiValidator());
		this.validators.add(new DataValidator());
		this.validators.add(new ResponsabilidadeValidator());
		this.validators.add(new NicValidator());
		this.validators.add(new ModeloCnhValidator());
		this.validators.add(new CategoriaCnhValidator());
		this.validators.add(new UfCnhValidator());
	}
	
	@Override
	public void validate(DadosProtocoloDTO protocolo, CustomConnection connection) throws ValidationException, Exception{
		for(IValidator<DadosProtocoloDTO> validator: validators) {
			validator.validate(protocolo, connection);
		}
	}
}
