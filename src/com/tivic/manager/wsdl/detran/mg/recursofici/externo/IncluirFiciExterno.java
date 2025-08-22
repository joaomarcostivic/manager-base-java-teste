package com.tivic.manager.wsdl.detran.mg.recursofici.externo;

import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.incluirautoinfracao.IncluirAutoInfracaoDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranRecurso;

public class IncluirFiciExterno extends ServicoDetranRecurso {

	@Override
	public ServicoDetranObjeto executarExterno(ProtocoloExternoDetranObject protocoloExternoDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(protocoloExternoDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(protocoloExternoDetranObject.getAitMovimento());
			RecursoFiciDadosEntrada incluirFiciDadosEntrada = IncluirFiciExternoDadosEntradaFactory.fazerDadoEntrada(protocoloExternoDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(incluirFiciDadosEntrada);
			RecursoFiciDadosRetorno incluirFiciDadosRetorno = (RecursoFiciDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_IDENTIFICACAO_CONDUTOR_INFRATOR.getValue(), incluirFiciDadosEntrada, this.arquivoConfiguracao, RecursoFiciDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(incluirFiciDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(ValidacaoException ve) {
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(protocoloExternoDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(protocoloExternoDetranObject.getAitMovimento());
			IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno = new IncluirAutoInfracaoDadosRetorno();
			incluirAutoInfracaoDadosRetorno.setCodigoRetorno(-1);
			incluirAutoInfracaoDadosRetorno.setMensagemRetorno(ve.getMessage());
			servicoDetranObjeto.setDadosRetorno(incluirAutoInfracaoDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoMG();
		}
	}	
}