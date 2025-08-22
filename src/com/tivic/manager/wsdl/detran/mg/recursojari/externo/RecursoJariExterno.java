package com.tivic.manager.wsdl.detran.mg.recursojari.externo;

import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.recursojari.RecursoJariDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.recursojari.RecursoJariDadosRetorno;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranRecurso;

public class RecursoJariExterno extends ServicoDetranRecurso {
	
	@Override
	public ServicoDetranObjeto executarExterno(ProtocoloExternoDetranObject protocoloExternoDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(protocoloExternoDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(protocoloExternoDetranObject.getAitMovimento());
			RecursoJariDadosEntrada recursoJariDadosEntrada = RecursoJariExternoDadosEntradaFactory.fazerDadoEntrada(protocoloExternoDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(recursoJariDadosEntrada);
			RecursoJariDadosRetorno recursoJariDadosRetorno = (RecursoJariDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_RECURSO_INFRACAO.getValue(), recursoJariDadosEntrada, this.arquivoConfiguracao, RecursoJariDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(recursoJariDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoMG();
		}
	}
}
