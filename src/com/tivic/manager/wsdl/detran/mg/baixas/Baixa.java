package com.tivic.manager.wsdl.detran.mg.baixas;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class Baixa extends ServicoDetran {
	private DetranRegistro detranRegistro;
	
	public Baixa() throws Exception {
		this.detranRegistro = new DetranRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG(aitDetranObject);
			BaixaDadosEntrada dadosEntrada = BaixaDadosEntradaFactory.compor(aitDetranObject);
			servicoDetranObjeto.setDadosEntrada(dadosEntrada);
			BaixaDadosRetorno dadosRetorno = (BaixaDadosRetorno) SenderDetran.send(ServicosDetranEnum.BAIXA_SUSPENSAO.getValue(), dadosEntrada, this.arquivoConfiguracao, BaixaDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(dadosRetorno);
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
