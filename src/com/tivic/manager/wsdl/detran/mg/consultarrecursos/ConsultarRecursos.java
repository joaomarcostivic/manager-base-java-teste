package com.tivic.manager.wsdl.detran.mg.consultarrecursos;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.consultarrecursos.ConsultarRecursosDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.consultarrecursos.ConsultarRecursosDadosEntradaFactory;
import com.tivic.manager.wsdl.detran.mg.consultarrecursos.ConsultarRecursosDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ConsultarRecursos extends ServicoDetran {

	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			ConsultarRecursosDadosEntrada consultarRecursosDadosEntrada = ConsultarRecursosDadosEntradaFactory.fazerDadoEntrada(aitDetranObject);
			servicoDetranObjeto.setDadosEntrada(consultarRecursosDadosEntrada);
			ConsultarRecursosDadosRetorno consultarRecursosDadosRetorno = (ConsultarRecursosDadosRetorno) SenderDetran.send(ServicosDetranEnum.CONSULTAR_DEFESA_RECURSO_INFRACAO.getValue(), consultarRecursosDadosEntrada, this.arquivoConfiguracao, ConsultarRecursosDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(consultarRecursosDadosRetorno);
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
