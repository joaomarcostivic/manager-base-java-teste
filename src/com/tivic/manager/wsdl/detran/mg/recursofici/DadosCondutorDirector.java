package com.tivic.manager.wsdl.detran.mg.recursofici;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class DadosCondutorDirector {
	private DadosCondutorBuilder dadosCondutorBuilder;
	private AitMovimento aitMovimento;
	private RecursoFiciDadosEntrada dadosEntrada;
	
	public DadosCondutorDirector(RecursoFiciDadosEntrada dadosEntrada, AitMovimento aitMovimento) throws Exception {
		this.dadosEntrada = dadosEntrada;
		this.aitMovimento = aitMovimento;
		this.dadosCondutorBuilder = new DadosCondutorBuilder(dadosEntrada);
	}
	
	
	public RecursoFiciDadosEntrada constructFiciFormulario() throws ValidacaoException, Exception{
		return dadosCondutorBuilder.dadosFormulario(aitMovimento).build();
	}
	
	public RecursoFiciDadosEntrada constructFiciApresentacaoCondutor() throws ValidacaoException, Exception{
		return dadosCondutorBuilder.dadosApresentacaoCondutor(aitMovimento).build();
	}
	
}
