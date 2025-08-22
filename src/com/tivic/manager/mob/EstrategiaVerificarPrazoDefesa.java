package com.tivic.manager.mob;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class EstrategiaVerificarPrazoDefesa {
	public IVerificarPrazoDefesa getEstrategia(String estado) throws ValidacaoException {
		switch(estado){
			case "BA":
				return new VerificarPrazoDefesaBA();
			case "MG":
				return new VerificarPrazoDefesaMG();
			default:
				throw new ValidacaoException ("Prazo de defesa ainda não definido para seu estado, verifique com a equipe de desenvolvimento.");
		}
	}
}
