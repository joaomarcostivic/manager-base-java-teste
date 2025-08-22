package com.tivic.manager.wsdl.detran.mg.correios;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.correios.CorreiosEtiquetaRepositoryDAO;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CorreiosDadosEntradaFactory {
	
	private static final int DADOS_CORREIO_NA = 2;
	private static final int DADOS_CORREIO_NP = 5;
	
	public static CorreiosDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) throws Exception{
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		CorreiosDadosEntrada correiosDadosEntrada = new CorreiosDadosEntrada();
		CorreiosEtiqueta etiqueta = getEtiqueta(ait.getCdAit(), aitMovimento.getTpStatus());
		correiosDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		correiosDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		correiosDadosEntrada.setPlaca(ait.getNrPlaca());
		correiosDadosEntrada.setAit(ait.getIdAit());
		correiosDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		correiosDadosEntrada.setCodigoRenainf(ait.getNrRenainf());
		correiosDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		correiosDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirCodigoMovimentacao(aitMovimento, correiosDadosEntrada);
		correiosDadosEntrada.setDataPublicacao(aitMovimento.getDtPublicacaoDo());
		correiosDadosEntrada.setIndicadorNotificacaoEdital(1);//Não
		correiosDadosEntrada.setDataEntregaCorreio(etiqueta.getDtAvisoRecebimento());
		correiosDadosEntrada.setSituacaoEntregaCorreio(etiqueta.getStAvisoRecebimento());
		correiosDadosEntrada.setNmServico("CORREIOS");
		return correiosDadosEntrada;
	}
	
	private static void incluirCodigoMovimentacao(AitMovimento aitMovimento, CorreiosDadosEntrada correiosDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.DADOS_CORREIO_NA:
				correiosDadosEntrada.setCodigoMovimentacao(DADOS_CORREIO_NA);
				break;
			case AitMovimentoServices.DADOS_CORREIO_NP:
				correiosDadosEntrada.setCodigoMovimentacao(DADOS_CORREIO_NP);
				break;
		}
	}
	
	private static CorreiosEtiqueta getEtiqueta(int cdAit, int tpStatus) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		int tpStatusNaOrNp = tpStatus == AitMovimentoServices.DADOS_CORREIO_NA ? AitMovimentoServices.NAI_ENVIADO : AitMovimentoServices.NIP_ENVIADA;
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("tp_status", tpStatusNaOrNp);
		Search<CorreiosEtiqueta> search = new SearchBuilder<CorreiosEtiqueta>("mob_correios_etiqueta")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		List<CorreiosEtiqueta> etiqueta = search.getList(CorreiosEtiqueta.class);
		return etiqueta.get(0);
	}
}