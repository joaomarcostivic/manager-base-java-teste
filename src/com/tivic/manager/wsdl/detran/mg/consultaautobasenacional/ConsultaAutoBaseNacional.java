package com.tivic.manager.wsdl.detran.mg.consultaautobasenacional;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ConsultaAutoBaseNacional extends ServicoDetran {
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			ConsultaAutoBaseNacionalDadosEntrada consultaAutoBaseNacionalDadosEntrada = ConsultaAutoBaseNacionalDadosEntradaFactory.fazerDadoEntrada(aitDetranObject);
			servicoDetranObjeto.setDadosEntrada(consultaAutoBaseNacionalDadosEntrada);
			ConsultaAutoBaseNacionalDadosRetorno consultaAutoBaseNacionalDadosRetorno = (ConsultaAutoBaseNacionalDadosRetorno) SenderDetran.send(ServicosDetranEnum.CONSULTAR_INFRACAO_BASE_NACIONAL.getValue(), consultaAutoBaseNacionalDadosEntrada, this.arquivoConfiguracao, ConsultaAutoBaseNacionalDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(consultaAutoBaseNacionalDadosRetorno);
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
