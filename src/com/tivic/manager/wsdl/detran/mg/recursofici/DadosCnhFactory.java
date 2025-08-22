package com.tivic.manager.wsdl.detran.mg.recursofici;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;

public class DadosCnhFactory {
	private ApresentacaoCondutor apresentacaoCondutor;
	private RecursoFiciDadosEntrada incluirFiciDadosEntrada;
	
	public DadosCnhFactory(ApresentacaoCondutor apresentacaoCondutor, RecursoFiciDadosEntrada incluirFiciDadosEntrada) throws Exception {
		this.apresentacaoCondutor = apresentacaoCondutor;
		this.incluirFiciDadosEntrada = incluirFiciDadosEntrada;
	}
	
	public RecursoFiciDadosEntrada strategy() throws BadRequestException, Exception {
		incluirDadosCnh();
		return incluirFiciDadosEntrada;
	}
	
	private void incluirDadosCnh() throws BadRequestException, Exception {	
		if(checkTipoModeloPreenchido())	{	
			incluirFiciDadosEntrada = new IncluirModeloCnh(apresentacaoCondutor, incluirFiciDadosEntrada).incluir();
		}	
	}
	
	private boolean checkTipoModeloPreenchido() {
		return apresentacaoCondutor.getTpModeloCnh() > 0;
	}
}
