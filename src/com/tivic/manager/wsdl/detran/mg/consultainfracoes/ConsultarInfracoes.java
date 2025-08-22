package com.tivic.manager.wsdl.detran.mg.consultainfracoes;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ConsultarInfracoes extends ServicoDetran {

	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try {
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			ConsultarInfracoesDadosEntrada consultarInfracoesDadosEntrada = (ConsultarInfracoesDadosEntrada) ConsultarInfracoesDadosEntradaFactory
					.fazerDadoEntrada(aitDetranObject);
			servicoDetranObjeto.setDadosEntrada(consultarInfracoesDadosEntrada);
			ConsultarInfracoesDadosRetorno consultarDadosCondutorDadosRetorno = (ConsultarInfracoesDadosRetorno) SenderDetran
					.send(ServicosDetranEnum.CONSULTAR_INFRACOES_POR_CNH_DATA_INFRACAO.getValue(), consultarInfracoesDadosEntrada, this.arquivoConfiguracao, ConsultarInfracoesDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(consultarDadosCondutorDadosRetorno);
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

