package com.tivic.manager.wsdl.detran.mg;

import com.tivic.manager.wsdl.detran.mg.soap.EnvelopeMg;
import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;
import com.tivic.manager.wsdl.interfaces.Envelope;

public class SenderDetran {
	public static DadosRetorno send(String serviço, DadosEntradaMG dadosEntrada, ArquivoConfiguracao arquivoConfiguracao, Class dadosRetornoMg) throws Exception {
		try {
			Envelope envelope = new EnvelopeMg(serviço, dadosEntrada, arquivoConfiguracao, dadosRetornoMg);
			envelope.validate();
			DadosRetornoMG dadosRetorno = (DadosRetornoMG) envelope.enviar();
			return dadosRetorno;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return new DadosRetornoErroMG(-1, "Erro interno do sistema");
		} 
	}
}
