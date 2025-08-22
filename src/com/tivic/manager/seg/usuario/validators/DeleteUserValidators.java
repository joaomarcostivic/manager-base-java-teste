package com.tivic.manager.seg.usuario.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class DeleteUserValidators {
	private int cdUsuario;
	private List<Validator<Integer>> validators;
	
	public DeleteUserValidators(int cdUsuario) throws Exception {
		this.cdUsuario = cdUsuario;
		validators = new ArrayList<Validator<Integer>>();
		validators.add(new ValidatorUsuarioPertencenteAIT());
		validators.add(new ValidatorUsuarioPertencenteMovimento());
		validators.add(new ValidatorUsuarioPertencenteLoteImpressao());
		validators.add(new ValidatorUsuarioPertencenteDocumento());
		validators.add(new ValidatorUsuarioPertencenteDocumentoOcorrencia());
		validators.add(new ValidatorUsuarioPertencenteAta());
		validators.add(new ValidatorUsuarioPertencenteAgente());
		validators.add(new ValidatorUsuarioPertencenteArquivo());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for(Validator<Integer> validator : validators) {
			validator.validate(cdUsuario, customConnection);
		}
	}

}
