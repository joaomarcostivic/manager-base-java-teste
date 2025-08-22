package com.tivic.manager.wsdl.detran.mg.recursofici.inclurcnhfactory;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosEntrada;

public class IncluirDadosCnhNacional implements IIncluirDadosCnh {
	
	@Override
	public RecursoFiciDadosEntrada build(RecursoFiciDadosEntrada dadosEntrada,
			ApresentacaoCondutor apresentacaoCondutor) {
		dadosEntrada.setUfCnh(apresentacaoCondutor.getUfCnh());
		dadosEntrada.setNumeroCnh(apresentacaoCondutor.getNrCnh());
		return dadosEntrada;
	}
	
}
