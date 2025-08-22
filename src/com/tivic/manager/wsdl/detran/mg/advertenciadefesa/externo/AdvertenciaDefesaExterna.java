package com.tivic.manager.wsdl.detran.mg.advertenciadefesa.externo;

import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.AdvertenciaDefesaDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.AdvertenciaDefesaDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranRecurso;

public class AdvertenciaDefesaExterna extends ServicoDetranRecurso {

	private ArquivoConfiguracaoMg arquivoConfiguracao;
	
	@Override
	public ServicoDetranObjeto executarExterno(ProtocoloExternoDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			AdvertenciaDefesaDadosEntrada advertenciaDefesaDadosEntrada = AdvertenciaDefesaExternaDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(advertenciaDefesaDadosEntrada);
			AdvertenciaDefesaDadosRetorno advertenciaDefesaDadosRetorno = (AdvertenciaDefesaDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_DEFESA_AUTUACAO.getValue(), advertenciaDefesaDadosEntrada, this.arquivoConfiguracao, AdvertenciaDefesaDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(advertenciaDefesaDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoMG();
		}
	}
	
}
