package com.tivic.manager.wsdl.detran.mg.recursofici.inclurcnhfactory;

import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class IncluirDadosCnhFactory {
	public IIncluirDadosCnh strategy(int tpModeloCnh) throws ValidacaoException {
		if (tpModeloCnh == TabelasAuxiliaresMG.TP_CNH_HABILITACAO_ESTRANGEIRA) {
			return new IncluirDadosCnhEstrangeira();
		}
		else {
			return new IncluirDadosCnhNacional();
		}
	}
}
