package com.tivic.manager.wsdl.detran.mg.publicacao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;


public class PublicacaoDadosEntradaFactory {
	
	private static final int PUBLICACAO_AUTUACAO 	= 3;
	private static final int PUBLICACAO_PENALIDADE 	= 6;
	private static final int PUBLICACAO_ADVERTENCIA	= 12;
	
	public static PublicacaoDadosEntrada fazerDadosEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) {
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		PublicacaoDadosEntrada publicacaoDadosEntrada = new PublicacaoDadosEntrada();
		publicacaoDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		publicacaoDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		publicacaoDadosEntrada.setPlaca(ait.getNrPlaca());
		publicacaoDadosEntrada.setAit(ait.getIdAit());
		publicacaoDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		publicacaoDadosEntrada.setCodigoRenainf(ait.getNrRenainf());
		publicacaoDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		publicacaoDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirCodigoMovimentacao(aitMovimento, publicacaoDadosEntrada);
		publicacaoDadosEntrada.setDataPublicacao(aitMovimento.getDtPublicacaoDo());
		publicacaoDadosEntrada.setIndicadorNotificacaoEdital(1);//Não
		publicacaoDadosEntrada.setNmServico("PUBLICACAO");
		return publicacaoDadosEntrada;
	}
	
	private static void incluirCodigoMovimentacao(AitMovimento aitMovimento, PublicacaoDadosEntrada publicacaoDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.PUBLICACAO_NAI:
				publicacaoDadosEntrada.setCodigoMovimentacao(PUBLICACAO_AUTUACAO);//NAI
				break;
			case AitMovimentoServices.PUBLICACAO_NIP:
				publicacaoDadosEntrada.setCodigoMovimentacao(PUBLICACAO_PENALIDADE);//NIP
				break;
		}
		
	}

}
