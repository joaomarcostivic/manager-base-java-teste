package com.tivic.manager.wsdl.detran.mg.cancelarautoinfracao;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class CancelarAutoInfracao extends ServicoDetran {
	private DetranRegistro detranRegistro;

	public CancelarAutoInfracao() throws Exception {
		this.detranRegistro = new DetranRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			CancelarAutoInfracaoDadosEntrada cancelarAutoInfracaoDadosEntrada = CancelarAutoInfracaoDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(cancelarAutoInfracaoDadosEntrada);
			CancelarAutoInfracaoDadosRetorno cancelarAutoInfracaoDadosRetorno = (CancelarAutoInfracaoDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_BAIXA_SUSPENSAO_RETORNO_INFRACAO.getValue(), cancelarAutoInfracaoDadosEntrada, this.arquivoConfiguracao, CancelarAutoInfracaoDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(cancelarAutoInfracaoDadosRetorno);
			detranRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
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
