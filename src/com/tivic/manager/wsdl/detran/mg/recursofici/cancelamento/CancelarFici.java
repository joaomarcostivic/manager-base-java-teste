package com.tivic.manager.wsdl.detran.mg.recursofici.cancelamento;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class CancelarFici extends ServicoDetran {

	private DetranRegistro detranRegistro;
	
	public CancelarFici	() throws Exception {
		this.detranRegistro = new DetranRegistro();
	}

	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			RecursoFiciDadosEntrada cancelarFiciDadosEntrada = CancelarFiciDadosEntradaFactory.fazerDadoEntrada(aitDetranObject.getAit(), aitDetranObject.getAitMovimento(), this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(cancelarFiciDadosEntrada);
			RecursoFiciDadosRetorno cancelarFiciDadosRetorno = (RecursoFiciDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_IDENTIFICACAO_CONDUTOR_INFRATOR.getValue(), cancelarFiciDadosEntrada, this.arquivoConfiguracao, RecursoFiciDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(cancelarFiciDadosRetorno);
			detranRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			return servicoDetranObjeto;
		}
		catch(ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, ve.getMessage()).build();
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, "Erro de sistema ao enviar").build();
		}
	}
	
}
