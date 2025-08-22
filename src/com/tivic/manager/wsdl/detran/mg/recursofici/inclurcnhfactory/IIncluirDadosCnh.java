package com.tivic.manager.wsdl.detran.mg.recursofici.inclurcnhfactory;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosEntrada;

public interface IIncluirDadosCnh {
	public RecursoFiciDadosEntrada build(RecursoFiciDadosEntrada dadosEntrada, ApresentacaoCondutor apresentacaoCondutor);
}
