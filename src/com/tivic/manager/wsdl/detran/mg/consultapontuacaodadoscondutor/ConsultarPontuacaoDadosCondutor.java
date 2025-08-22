package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ConsultarPontuacaoDadosCondutor extends ServicoDetran {

	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try {
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setDadosCondutorDocumentoEntrada(aitDetranObject.getDadosCondutorDocumentoEntrada());
			ConsultarPontuacaoDadosCondutorDadosEntrada consultarDadosCondutorDadosEntrada = (ConsultarPontuacaoDadosCondutorDadosEntrada) ConsultarPontuacaoDadosCondutorDadosEntradaFactory
					.fazerDadoEntrada(aitDetranObject);
			servicoDetranObjeto.setDadosEntrada(consultarDadosCondutorDadosEntrada);
			ConsultarPontuacaoDadosCondutorDadosRetorno consultarDadosCondutorDadosRetorno = (ConsultarPontuacaoDadosCondutorDadosRetorno) SenderDetran
					.send(ServicosDetranEnum.CONSULTAR_PONTUACAO_DADOS_CONDUTOR.getValue(), consultarDadosCondutorDadosEntrada, this.arquivoConfiguracao, ConsultarPontuacaoDadosCondutorDadosRetorno.class);
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
