package com.tivic.manager.wsdl.detran.mg.recursojari;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.recursojari.RecursoJariDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.recursojari.IncluirRecursoJariDadosEntradaFactory;
import com.tivic.manager.wsdl.detran.mg.recursojari.RecursoJariDadosRetorno;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class IncluirRecursoJari extends ServicoDetran {
	private DetranRegistro detranRegistro;
	
	public IncluirRecursoJari() throws Exception {
		this.detranRegistro = new DetranRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			RecursoJariDadosEntrada recursoJariDadosEntrada = IncluirRecursoJariDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(recursoJariDadosEntrada);
			RecursoJariDadosRetorno recursoJariDadosRetorno = (RecursoJariDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_RECURSO_INFRACAO.getValue(), recursoJariDadosEntrada, this.arquivoConfiguracao, RecursoJariDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(recursoJariDadosRetorno);
			detranRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoMG();
		}
	}
	
}
