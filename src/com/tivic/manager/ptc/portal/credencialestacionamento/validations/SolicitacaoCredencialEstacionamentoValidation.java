package com.tivic.manager.ptc.portal.credencialestacionamento.validations;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class SolicitacaoCredencialEstacionamentoValidation {
	
	private CartaoEstacionamentoRequest documentoCartaoIdosoRequest;
	private List<Validator<CartaoEstacionamentoRequest>> validators;
	
	public SolicitacaoCredencialEstacionamentoValidation(CartaoEstacionamentoRequest documentoCartaoIdosoRequest) throws Exception {
		this.documentoCartaoIdosoRequest = documentoCartaoIdosoRequest;
		validators =  new ArrayList<Validator<CartaoEstacionamentoRequest>>();
		validators.add(new ValidatorSolicitacaoPendente());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for (Validator<CartaoEstacionamentoRequest> validator : validators) {
			validator.validate(documentoCartaoIdosoRequest, customConnection);
		}
	}
}
