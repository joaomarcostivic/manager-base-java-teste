package com.tivic.manager.wsdl.detran.mg.recursofici.inclurcnhfactory;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosEntrada;

public class IncluirDadosCnhEstrangeira implements IIncluirDadosCnh {
	private final String UF_ESTRANGEIRA = "EX";
	
	@Override
	public RecursoFiciDadosEntrada build(RecursoFiciDadosEntrada dadosEntrada,
			ApresentacaoCondutor apresentacaoCondutor) {
		dadosEntrada.setCodigoPaisCnh(Integer.parseInt(apresentacaoCondutor.getIdPaisCnh()));
		dadosEntrada.setUfCnh(UF_ESTRANGEIRA);
		dadosEntrada.setNumeroCnh(apresentacaoCondutor.getNrCnh());
        return dadosEntrada;
	}
}
