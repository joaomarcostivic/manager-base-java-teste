package com.tivic.manager.wsdl.detran.mg.recursonic;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMgHomologacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class IncluirNIC extends ServicoDetran{
	private IncluirNICRegistro incluirNICRegistro;
	
	public IncluirNIC() throws Exception {
		this.incluirNICRegistro = new IncluirNICRegistro();
		this.arquivoConfiguracao = new ArquivoConfiguracaoMgHomologacao();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG(aitDetranObject);
			RecursoNICDadosEntrada incluirNicDadosEntrada = IncluirNICDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(incluirNicDadosEntrada);
			RecursoNICDadosRetorno incluirNICDadosRetorno = (RecursoNICDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_MULTA_NIC.getValue(), incluirNicDadosEntrada, this.arquivoConfiguracao, RecursoNICDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(incluirNICDadosRetorno);
			incluirNICRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			
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
