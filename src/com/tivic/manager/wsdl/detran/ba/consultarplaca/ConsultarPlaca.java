package com.tivic.manager.wsdl.detran.ba.consultarplaca;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.ba.ServicoDetranObjetoBA;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosEntrada;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosEntradaFactory;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.detran.ba.soap.EnvelopeBa;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;
import com.tivic.manager.wsdl.interfaces.Envelope;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ConsultarPlaca extends ServicoDetran {

	private ArquivoConfiguracao arquivoConfiguracao;
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoBA();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			ConsultarPlacaDadosEntrada incluirAutoInfracaoDadosEntrada = ConsultarPlacaDadosEntradaFactory.fazerDadoEntrada(aitDetranObject.getNrPlaca());
			servicoDetranObjeto.setDadosEntrada(incluirAutoInfracaoDadosEntrada);
			ConsultarPlacaDadosRetorno incluirAutoInfracaoDadosRetorno = sendDetran(incluirAutoInfracaoDadosEntrada, this.arquivoConfiguracao);
			servicoDetranObjeto.setDadosRetorno(incluirAutoInfracaoDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoBA();
		}
	}
	
	private ConsultarPlacaDadosRetorno sendDetran(ConsultarPlacaDadosEntrada consultarPlacaDadosEntrada, ArquivoConfiguracao arquivoConfiguracao) throws ValidacaoException {
		try {
			Envelope envelope = new EnvelopeBa("consultarVeiculoPorPlaca", consultarPlacaDadosEntrada, arquivoConfiguracao, ConsultarPlacaDadosRetorno.class);
			envelope.validate();
			ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = (ConsultarPlacaDadosRetorno) envelope.enviar();
			return consultarPlacaDadosRetorno;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			ConsultarPlacaDadosRetorno incluirAutoInfracaoDadosRetorno = new ConsultarPlacaDadosRetorno();
			incluirAutoInfracaoDadosRetorno.setMensagemRetorno("Erro interno do sistema");
			incluirAutoInfracaoDadosRetorno.setCodigoRetorno(-1);//Erro validaï¿½ï¿½o
			return incluirAutoInfracaoDadosRetorno;
		} 
	}
	
	@Override
	public void setArquivoConfiguracao(ArquivoConfiguracao arquivoConfiguracao){
		this.arquivoConfiguracao = arquivoConfiguracao;
	}
	
}
