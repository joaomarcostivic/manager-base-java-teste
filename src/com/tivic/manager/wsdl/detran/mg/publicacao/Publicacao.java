package com.tivic.manager.wsdl.detran.mg.publicacao;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class Publicacao extends ServicoDetran {
	private PublicacaoRegistro publicacaoRegistro;
	
	public Publicacao() throws Exception {
		this.publicacaoRegistro = new PublicacaoRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try {
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			PublicacaoDadosEntrada publicacaoDadosEntrada = PublicacaoDadosEntradaFactory.fazerDadosEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(publicacaoDadosEntrada);
			PublicacaoDadosRetorno publicacaoDadosRetorno = (PublicacaoDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_NOTIFICACAO_PUBLICACAO_PRAZO.getValue(), publicacaoDadosEntrada, this.arquivoConfiguracao, PublicacaoDadosRetorno.class);
			if (publicacaoDadosRetorno.getAit() == null || publicacaoDadosRetorno.getAit().trim().isEmpty()) {
				publicacaoDadosRetorno.setAit(aitDetranObject.getAit().getIdAit());
			}
			PublicacaoDadosPreenchimento.preencherDatasViaBaseEstadual(publicacaoDadosRetorno, aitDetranObject.getAit().getIdAit());
			servicoDetranObjeto.setDadosRetorno(publicacaoDadosRetorno);
			publicacaoRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
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
