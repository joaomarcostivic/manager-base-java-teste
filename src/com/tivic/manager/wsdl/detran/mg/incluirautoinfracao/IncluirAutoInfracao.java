package com.tivic.manager.wsdl.detran.mg.incluirautoinfracao;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class IncluirAutoInfracao extends ServicoDetran {

	private IncluirAutoInfracaoRegistro incluirAutoInfracaoRegistro;
	
	public IncluirAutoInfracao() throws Exception {
		this.incluirAutoInfracaoRegistro = new IncluirAutoInfracaoRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG(aitDetranObject);
			IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada = IncluirAutoInfracaoDadosEntradaFactory.fazerDadoEntrada(aitDetranObject.getAit(), this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(incluirAutoInfracaoDadosEntrada);
			IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno = (IncluirAutoInfracaoDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_AUTO_INFRACAO.getValue(), incluirAutoInfracaoDadosEntrada, this.arquivoConfiguracao, IncluirAutoInfracaoDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(incluirAutoInfracaoDadosRetorno);
			incluirAutoInfracaoRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			return servicoDetranObjeto;
		}
		catch(ValidacaoException ve) {
			ve.printStackTrace();
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, ve.getMessage()).build();
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, "Erro de sistema ao enviar").build();
		}
	}
}
