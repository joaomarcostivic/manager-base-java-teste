package com.tivic.manager.mob.correios.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoCorreiosDTO;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class UploadArquivoCorreiosValidations {

private List<Validator<ArquivoRetornoCorreiosDTO>> validators;
	
	public UploadArquivoCorreiosValidations() throws Exception {
		this.validators = new ArrayList<Validator<ArquivoRetornoCorreiosDTO>>();
		this.validators.add(new UploadArquivoCorreiosValidator());
	}
	
	public void validate(ArquivoRetornoCorreiosDTO arquivoRetornoCorreiosDTO, CustomConnection customConnection) throws Exception {
		for( Validator<ArquivoRetornoCorreiosDTO> validator: validators) {
			validator.validate(arquivoRetornoCorreiosDTO, customConnection);
		}
	}
}
