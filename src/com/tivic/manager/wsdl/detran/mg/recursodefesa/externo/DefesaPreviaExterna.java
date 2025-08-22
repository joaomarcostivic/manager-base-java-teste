package com.tivic.manager.wsdl.detran.mg.recursodefesa.externo;

import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.RecursoDefesaDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.RecursoDefesaDadosRetorno;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranRecurso;

public class DefesaPreviaExterna extends ServicoDetranRecurso {
	
	@Override
	public ServicoDetranObjeto executarExterno(ProtocoloExternoDetranObject protocoloExternoDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(protocoloExternoDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(protocoloExternoDetranObject.getAitMovimento());
			RecursoDefesaDadosEntrada defesaPreviaDadosEntrada = DefesaPreviaExternaDadosEntradaFactory.fazerDadoEntrada(protocoloExternoDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(defesaPreviaDadosEntrada);
			RecursoDefesaDadosRetorno defesaPreviaDadosRetorno = (RecursoDefesaDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_DEFESA_AUTUACAO.getValue(), defesaPreviaDadosEntrada, this.arquivoConfiguracao, RecursoDefesaDadosRetorno.class); 
			servicoDetranObjeto.setDadosRetorno(defesaPreviaDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoMG();
		}
	}
}
