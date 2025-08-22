package com.tivic.manager.wsdl.detran.mg.enviorenainf;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class EnvioRenainf extends ServicoDetran  {
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			EnvioRenainfDadosEntrada envioRenainfDadosEntrada = EnvioRenainfDadosEntradaFactory.fazerDadoEntrada(aitDetranObject.getAit(), aitDetranObject.getAitMovimento());
			servicoDetranObjeto.setDadosEntrada(envioRenainfDadosEntrada);
			EnvioRenainfDadosRetorno envioRenainfDadosRetorno = (EnvioRenainfDadosRetorno) SenderDetran.send(ServicosDetranEnum.SINCRONIZAR_BASE_NACIONAL_COM_ESTADUAL.getValue(), envioRenainfDadosEntrada, this.arquivoConfiguracao, EnvioRenainfDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(envioRenainfDadosRetorno);
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
