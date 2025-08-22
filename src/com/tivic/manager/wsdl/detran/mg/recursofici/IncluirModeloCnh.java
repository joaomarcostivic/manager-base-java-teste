package com.tivic.manager.wsdl.detran.mg.recursofici;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.recursofici.inclurcnhfactory.IncluirDadosCnhFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class IncluirModeloCnh {
	private ApresentacaoCondutor apresentacaoCondutor;
	private RecursoFiciDadosEntrada incluirFiciDadosEntrada;
	private TabelasAuxiliaresMG tabelasAuxiliares;
	
	public IncluirModeloCnh(ApresentacaoCondutor apresentacaoCondutor, RecursoFiciDadosEntrada incluirFiciDadosEntrada) throws Exception {
		this.apresentacaoCondutor = apresentacaoCondutor;
		this.incluirFiciDadosEntrada = incluirFiciDadosEntrada;
		this.tabelasAuxiliares = new TabelasAuxiliaresMG();
	}
	
	public RecursoFiciDadosEntrada incluir() throws ValidacaoException {
		incluirFiciDadosEntrada.setModeloCnh(tabelasAuxiliares.getTipoCnh(apresentacaoCondutor.getTpModeloCnh()));
		incluirFiciDadosEntrada = new IncluirDadosCnhFactory()
				.strategy(apresentacaoCondutor.getTpModeloCnh())
				.build(incluirFiciDadosEntrada, apresentacaoCondutor);
		return incluirFiciDadosEntrada;
	}
}
