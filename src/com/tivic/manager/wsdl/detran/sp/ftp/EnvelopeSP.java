package com.tivic.manager.wsdl.detran.sp.ftp;

import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.sp.DadosEntradaSP;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;
import com.tivic.manager.wsdl.interfaces.Envelope;

public class EnvelopeSP implements Envelope {

	private DadosEntradaSP dadosEntrada;
	private ArquivoConfiguracaoSp arquivoConfiguracao;
	
	public EnvelopeSP(DadosEntradaSP dadosEntrada, ArquivoConfiguracao arquivoConfiguracao) {
		super();
		this.dadosEntrada = dadosEntrada;
		this.arquivoConfiguracao = (ArquivoConfiguracaoSp)arquivoConfiguracao;
	}

	@Override
	public Object enviar() throws InstantiationException, IllegalAccessException, ValidacaoException {
		return convertToString(dadosEntrada);
	}

	private String convertToString(DadosEntradaSP dadosEntrada) {
		String linhaAuto = "";
		for(String key : dadosEntrada.getItens().keySet()) {
			DadosItem dadosItem = dadosEntrada.getItens().get(key);
			linhaAuto += dadosItem.getValor();
		}
	
		return linhaAuto;
	}

	@Override
	public void validate() throws ValidacaoException {
		for(String campo : this.dadosEntrada.getItens().keySet()){
			DadosItem dadosItem = this.dadosEntrada.getItens().get(campo);
			dadosItem.validate();
		}
	}
	
}
