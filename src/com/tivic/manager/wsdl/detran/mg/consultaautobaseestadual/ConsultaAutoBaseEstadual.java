package com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosEntradaFactory;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ConsultaAutoBaseEstadual extends ServicoDetran {
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			ConsultaAutoBaseEstadualDadosEntrada consultaAutoBaseEstadualDadosEntrada = ConsultaAutoBaseEstadualDadosEntradaFactory.fazerDadoEntrada(aitDetranObject);
			servicoDetranObjeto.setDadosEntrada(consultaAutoBaseEstadualDadosEntrada);
			ConsultaAutoBaseEstadualDadosRetorno consultaAutoBaseEstadualDadosRetorno = (ConsultaAutoBaseEstadualDadosRetorno) SenderDetran.send(ServicosDetranEnum.CONSULTAR_INFRACAO_BASE_ESTADUAL.getValue(), consultaAutoBaseEstadualDadosEntrada, this.arquivoConfiguracao, ConsultaAutoBaseEstadualDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(consultaAutoBaseEstadualDadosRetorno);
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
