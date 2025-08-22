package com.tivic.manager.wsdl.detran.mg.correios;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class Correios extends ServicoDetran{

	private CorreiosRegistro correiosRegistro;
	
	public Correios() throws Exception {
		this.correiosRegistro = new CorreiosRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try {
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			CorreiosDadosEntrada correiosDadosEntrada = CorreiosDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(correiosDadosEntrada);
			CorreiosDadosRetorno correiosDadosRetorno = (CorreiosDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_NOTIFICACAO_PUBLICACAO_PRAZO.getValue(), correiosDadosEntrada, this.arquivoConfiguracao, CorreiosDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(correiosDadosRetorno);
			correiosRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
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
