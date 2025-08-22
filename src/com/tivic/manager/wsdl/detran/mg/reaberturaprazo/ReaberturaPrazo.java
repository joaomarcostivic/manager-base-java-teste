package com.tivic.manager.wsdl.detran.mg.reaberturaprazo;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ReaberturaPrazo extends ServicoDetran {
	private DetranRegistro detranRegistro;
	
	public ReaberturaPrazo() throws Exception {
		this.detranRegistro = new DetranRegistro();
	}	
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try {
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			ReaberturaPrazoDadosEntrada reaberturaPrazoDadosEntrada = ReaberturaPrazoDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(reaberturaPrazoDadosEntrada);
			ReaberturaPrazoDadosRetorno reaberturaPrazoDadosRetorno = (ReaberturaPrazoDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_NOTIFICACAO_PUBLICACAO_PRAZO.getValue(), reaberturaPrazoDadosEntrada, this.arquivoConfiguracao, ReaberturaPrazoDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(reaberturaPrazoDadosRetorno);
			detranRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			return servicoDetranObjeto;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new ServicoDetranObjetoMG();
		}
	}
}
