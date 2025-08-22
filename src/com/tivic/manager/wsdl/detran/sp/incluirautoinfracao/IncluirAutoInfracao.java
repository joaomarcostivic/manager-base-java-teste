package com.tivic.manager.wsdl.detran.sp.incluirautoinfracao;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.sp.incluirautoinfracao.IncluirAutoInfracaoDadosRetorno;
import com.tivic.manager.wsdl.detran.sp.ServicoDetranObjetoSP;
import com.tivic.manager.wsdl.detran.sp.ftp.EnvelopeSP;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;
import com.tivic.manager.wsdl.interfaces.Envelope;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ValidatorService;

public class IncluirAutoInfracao extends ServicoDetran {

	private ArquivoConfiguracao arquivoConfiguracao;
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoSP();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada = IncluirAutoInfracaoDadosEntradaFactory.fazerDadoEntrada(aitDetranObject);
			validate(incluirAutoInfracaoDadosEntrada);
			servicoDetranObjeto.setDadosEntrada(incluirAutoInfracaoDadosEntrada);
			String linhaAuto = sendDetran(incluirAutoInfracaoDadosEntrada, this.arquivoConfiguracao);
			IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno = new IncluirAutoInfracaoDadosRetorno(linhaAuto);
			servicoDetranObjeto.setDadosRetorno(incluirAutoInfracaoDadosRetorno);
			IncluirAutoInfracaoRegistro incluirAutoInfracaoRegistro = new IncluirAutoInfracaoRegistro();
			incluirAutoInfracaoRegistro.registrar(servicoDetranObjeto, false);
			return servicoDetranObjeto;
		}
		catch(ValidacaoException ve){
			ve.printStackTrace();
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoSP();
			IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada = new IncluirAutoInfracaoDadosEntrada();
			incluirAutoInfracaoDadosEntrada.setNumeroAuto(aitDetranObject.getAit().getIdAit());
			servicoDetranObjeto.setDadosEntrada(incluirAutoInfracaoDadosEntrada);
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno = new IncluirAutoInfracaoDadosRetorno("");
			incluirAutoInfracaoDadosRetorno.setCodigoRetorno(-1);
			incluirAutoInfracaoDadosRetorno.setMensagemRetorno(ve.getMessage());
			servicoDetranObjeto.setDadosRetorno(incluirAutoInfracaoDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoSP();
		}
		
	}
	
	private String sendDetran(IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada, ArquivoConfiguracao arquivoConfiguracao) throws ValidacaoException{
		try {
			Envelope envelope = new EnvelopeSP(incluirAutoInfracaoDadosEntrada, arquivoConfiguracao);
			return String.valueOf(envelope.enviar());
		} 
		catch(ValidacaoException ve) {
			throw ve;
		}
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	@Override
	public void setArquivoConfiguracao(ArquivoConfiguracao arquivoConfiguracao){
		this.arquivoConfiguracao = arquivoConfiguracao;
	}
	
	private void validate(IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) throws ValidacaoException {
		for(String campo : incluirAutoInfracaoDadosEntrada.getItens().keySet()) {
			DadosItem dadosItem = incluirAutoInfracaoDadosEntrada.getItens().get(campo);
			if(dadosItem.getValidator() != null) {
				try {
					dadosItem.getValidator().validate(dadosItem.getValor(), dadosItem.getTamanho());
				}
				catch(ValidacaoException ve) {
					if(dadosItem.isObrigatorio()) {
						throw ve;
					}
					else {
						dadosItem.setValor(Util.fill("", dadosItem.getTamanho(), ' ', 'D'));
					}
				}
			}
		}
		
		for(ValidatorService validatorService : incluirAutoInfracaoDadosEntrada.getValidators()) {
			validatorService.validate();
		}
	}
	
}
