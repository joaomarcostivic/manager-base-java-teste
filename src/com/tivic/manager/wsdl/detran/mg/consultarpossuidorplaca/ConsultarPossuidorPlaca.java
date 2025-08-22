package com.tivic.manager.wsdl.detran.mg.consultarpossuidorplaca;

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

public class ConsultarPossuidorPlaca extends ServicoDetran {
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			ConsultarPossuidorPlacaDadosEntrada consultarPossuidorPlacaDadosEntrada = ConsultarPossuidorPlacaDadosEntradaFactory.fazerDadoEntrada(aitDetranObject.getNrPlaca());
			servicoDetranObjeto.setDadosEntrada(consultarPossuidorPlacaDadosEntrada);
			ConsultarPossuidorPlacaDadosRetorno consultarPossuidorPlacaDadosRetorno = (ConsultarPossuidorPlacaDadosRetorno) SenderDetran.send(ServicosDetranEnum.CONSULTAR_POSSUIDOR_VEICULO_PLACA.getValue(), consultarPossuidorPlacaDadosEntrada, this.arquivoConfiguracao, ConsultarPossuidorPlacaDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(consultarPossuidorPlacaDadosRetorno);
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
