package com.tivic.manager.wsdl.detran.mg.consultarplaca;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosEntradaFactory;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ConsultarPlaca extends ServicoDetran {
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			ConsultarPlacaDadosEntrada consultarPlacaDadosEntrada = ConsultarPlacaDadosEntradaFactory.fazerDadoEntrada(aitDetranObject.getNrPlaca());
			servicoDetranObjeto.setDadosEntrada(consultarPlacaDadosEntrada);
			ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = (ConsultarPlacaDadosRetorno) SenderDetran.send(ServicosDetranEnum.CONSULTAR_VEICULO_PLACA.getValue(), consultarPlacaDadosEntrada, this.arquivoConfiguracao, ConsultarPlacaDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(consultarPlacaDadosRetorno);
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
