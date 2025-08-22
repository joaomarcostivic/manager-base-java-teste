package com.tivic.manager.wsdl.detran.mg.advertenciadefesa.cancelamento;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.AdvertenciaDefesaDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.advertenciadefesa.AdvertenciaDefesaDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class CancelarAdvertenciaDefesa extends ServicoDetran {
	private DetranRegistro detranRegistro;
	
	public CancelarAdvertenciaDefesa() throws Exception {
		this.detranRegistro = new DetranRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			AdvertenciaDefesaDadosEntrada advertenciaDefesaDadosEntrada = CancelarAdvertenciaDefesaDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(advertenciaDefesaDadosEntrada);
			AdvertenciaDefesaDadosRetorno advertenciaDefesaDadosRetorno =  (AdvertenciaDefesaDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_DEFESA_AUTUACAO.getValue(), advertenciaDefesaDadosEntrada, this.arquivoConfiguracao, AdvertenciaDefesaDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(advertenciaDefesaDadosRetorno);
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
