package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.CPFFiciValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.CategoriaCnhValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.DataValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.UfCnhValidator;
import com.tivic.sol.connection.CustomConnection;

public class ApresentacaoCondutorUpdateValidatorBuilder {
	private List<IApresentacaoCondutorValidator> validators;
	
	public ApresentacaoCondutorUpdateValidatorBuilder() throws Exception {
		this.validators = new ArrayList<IApresentacaoCondutorValidator>();
		this.validators.add(new CategoriaCnhValidator());
		this.validators.add(new UfCnhValidator());
		this.validators.add(new DataValidator());
		this.validators.add(new CPFFiciValidator());
	}
	
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection) throws Exception {
		for(IApresentacaoCondutorValidator validator: this.validators) {
			validator.validate(apresentacaoCondutor, connection);
		}
	}

}
