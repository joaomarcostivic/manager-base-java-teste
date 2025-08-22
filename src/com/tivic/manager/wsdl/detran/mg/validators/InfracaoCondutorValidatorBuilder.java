package com.tivic.manager.wsdl.detran.mg.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.detran.mg.validators.condutor.ICondutorRegistroInfracaoValidator;
import com.tivic.manager.wsdl.detran.mg.validators.condutor.ModeloCnhValidator;
import com.tivic.manager.wsdl.detran.mg.validators.condutor.TipoDocumentoValidator;
import com.tivic.manager.wsdl.detran.mg.validators.condutor.UFCnhValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class InfracaoCondutorValidatorBuilder {
	private List<ICondutorRegistroInfracaoValidator> validators;
	
	public InfracaoCondutorValidatorBuilder() throws Exception {
		this.validators = new ArrayList<ICondutorRegistroInfracaoValidator>();
		this.validators.add(new ModeloCnhValidator());
		this.validators.add(new UFCnhValidator());
		this.validators.add(new TipoDocumentoValidator());
	}
	
	public void validate(Ait ait, CustomConnection customConnection) throws ValidacaoException, Exception {
		for(ICondutorRegistroInfracaoValidator validator: this.validators) {
			validator.validate(ait, customConnection);
		}
	}
}
