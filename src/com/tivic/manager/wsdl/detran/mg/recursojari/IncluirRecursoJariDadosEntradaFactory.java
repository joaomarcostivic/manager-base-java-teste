package com.tivic.manager.wsdl.detran.mg.recursojari;


import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class IncluirRecursoJariDadosEntradaFactory {
	private static int COD_MOVIMENTACAO_RECURSO_CETRAN = 14;
	private static int COD_MOVIMENTACAO_CETRAN_DEFERIDO = 23;
	private static int COD_MOVIMENTACAO_CETRAN_INDEFERIDO = 24;
	private static IAitMovimentoService aitMovimentoService;

	private static int[] jari = {
		AitMovimentoServices.RECURSO_JARI, 
		AitMovimentoServices.JARI_COM_PROVIMENTO, 
		AitMovimentoServices.JARI_SEM_PROVIMENTO,
		AitMovimentoServices.PUBLICACAO_RESULTADO_JARI,
		AitMovimentoServices.PENALIDADE_SUSPENSA,
		AitMovimentoServices.PENALIDADE_REATIVADA
	};
	
	private static int[] cetran = {
		COD_MOVIMENTACAO_RECURSO_CETRAN,
		COD_MOVIMENTACAO_CETRAN_DEFERIDO,
		COD_MOVIMENTACAO_CETRAN_INDEFERIDO
	};
	
	public static RecursoJariDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) throws Exception {
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		RecursoJariDadosEntrada recursoJariDadosEntrada = new RecursoJariDadosEntrada();
		int recursoGeradorPenalidadeSuspensa = 0;
		if(aitMovimento.getTpStatus() == TipoStatusEnum.PENALIDADE_SUSPENSA.getKey()) {
			recursoGeradorPenalidadeSuspensa = getRecursoGeradorPenalidadeSuspensa(aitMovimento);
		}
		recursoJariDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		recursoJariDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		recursoJariDadosEntrada.setPlaca(ait.getNrPlaca());
		recursoJariDadosEntrada.setAit(ait.getIdAit());
		recursoJariDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		recursoJariDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		recursoJariDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		recursoJariDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		recursoJariDadosEntrada.setNumeroProcessoRecurso(aitMovimento.getNrProcesso());
		incluirCodigoMovimentacao(aitMovimento, recursoJariDadosEntrada);
		adicionarNumeroDocumento(aitMovimento, recursoGeradorPenalidadeSuspensa, recursoJariDadosEntrada);
		adicionarDataEntrada(aitMovimento, recursoGeradorPenalidadeSuspensa, recursoJariDadosEntrada);
		adicionarDataEncerramento(aitMovimento, recursoJariDadosEntrada);
		//recursoJariDadosEntrada.setCodigoReclassificacaoInfracao(0);
		recursoJariDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		setDataPublicacaoResultado(recursoJariDadosEntrada, aitMovimento);
		recursoJariDadosEntrada.setNmServico("RECURSO JARI");
		return recursoJariDadosEntrada;
	}
	
	private static void incluirCodigoMovimentacao(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.RECURSO_JARI:
				recursoJariDadosEntrada.setCodigoMovimentacao(10);
				break;
			case AitMovimentoServices.JARI_COM_PROVIMENTO:
				recursoJariDadosEntrada.setCodigoMovimentacao(11);
				break;
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
				recursoJariDadosEntrada.setCodigoMovimentacao(12);
				break;
			case AitMovimentoServices.RECURSO_CETRAN:
				recursoJariDadosEntrada.setCodigoMovimentacao(14);
				break;
			case AitMovimentoServices.PENALIDADE_SUSPENSA:
				recursoJariDadosEntrada.setCodigoMovimentacao(18);
				break;
			case AitMovimentoServices.PENALIDADE_REATIVADA:
				recursoJariDadosEntrada.setCodigoMovimentacao(19);
				break;
			case AitMovimentoServices.CETRAN_DEFERIDO:
				recursoJariDadosEntrada.setCodigoMovimentacao(23);
				break;
			case AitMovimentoServices.CETRAN_INDEFERIDO:
				recursoJariDadosEntrada.setCodigoMovimentacao(24);
				break;
			case AitMovimentoServices.PUBLICACAO_RESULTADO_JARI:
				recursoJariDadosEntrada.setCodigoMovimentacao(95);
				break;
		}
	}
	
	private static void adicionarNumeroDocumento(AitMovimento aitMovimento, int recursoGeradorPenalidadeSuspensa, RecursoJariDadosEntrada recursoJariDadosEntrada) throws Exception {
		if(recursoGeradorPenalidadeSuspensa == TipoStatusEnum.RECURSO_CETRAN.getKey() || contains(cetran, recursoJariDadosEntrada.getCodigoMovimentacao())) {
			adicionarNumeroDocumentoCetran(aitMovimento, recursoJariDadosEntrada);
		} else {
			adicionarNumeroDocumentoJari(aitMovimento, recursoJariDadosEntrada);
		}
	}
	
	private static void adicionarNumeroDocumentoCetran(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) throws Exception {
		AitMovimento recursoCetran = AitMovimentoServices.getRecursoCetran(aitMovimento);
		if(recursoCetran == null) {
			throw new ValidacaoException("Recurso Cetran não encontrado");
		}
		recursoJariDadosEntrada.setNumeroDocumento(recursoCetran.getCdAit() + "" + recursoCetran.getCdMovimento());
	}
	
	private static void adicionarNumeroDocumentoJari(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) throws Exception {
		if(contains(jari, recursoJariDadosEntrada.getCodigoMovimentacao())) {
			AitMovimento recursoJari = AitMovimentoServices.getRecursoJari(aitMovimento);
			if(recursoJari == null)
				throw new ValidacaoException("Recurso Jari não encontrado");
			recursoJariDadosEntrada.setNumeroDocumento(recursoJari.getCdAit() + "" + recursoJari.getCdMovimento());
		} else {
			AitMovimento recursoJari = AitMovimentoServices.getJariIndeferida(aitMovimento);
			if(recursoJari == null)
				throw new ValidacaoException("Recurso Jari não encontrado");
			recursoJariDadosEntrada.setNumeroDocumento(recursoJari.getCdAit() + "" + recursoJari.getCdMovimento());
		}
	}

	private static void adicionarDataEntrada(AitMovimento aitMovimento, int recursoGeradorPenalidadeSuspensa, RecursoJariDadosEntrada recursoJariDadosEntrada) throws Exception {
	    if (recursoGeradorPenalidadeSuspensa == TipoStatusEnum.RECURSO_CETRAN.getKey() || contains(cetran, recursoJariDadosEntrada.getCodigoMovimentacao())) {
	        adicionarDataEntradaRecurso(aitMovimento, recursoJariDadosEntrada, TipoStatusEnum.RECURSO_CETRAN);
	    } else if (recursoGeradorPenalidadeSuspensa == TipoStatusEnum.RECURSO_JARI.getKey() || contains(jari, recursoJariDadosEntrada.getCodigoMovimentacao())) {
	        adicionarDataEntradaRecurso(aitMovimento, recursoJariDadosEntrada, TipoStatusEnum.RECURSO_JARI);
	    }
	}

	private static void adicionarDataEntradaRecurso(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada, TipoStatusEnum tipoStatus) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("cd_ait", aitMovimento.getCdAit());
	    searchCriterios.addCriteriosEqualInteger("tp_status", tipoStatus.getKey());
	    searchCriterios.addCriterios("lg_cancela_movimento", String.valueOf(1), ItemComparator.NOTIN, Types.INTEGER);
	    List<AitMovimento> recursoMovimentos = buscarRecurso(aitMovimento, searchCriterios);
	    if (!recursoMovimentos.isEmpty()) {
	        recursoJariDadosEntrada.setDataEntradaRecurso(recursoMovimentos.get(0).getDtMovimento());
	    } else {
	        throw new ValidacaoException("Recurso não encontrado para " + tipoStatus.name());
	    }
	}
	
	private static int getRecursoGeradorPenalidadeSuspensa(AitMovimento aitMovimento) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("cd_ait", aitMovimento.getCdAit());
	    searchCriterios.addCriteriosEqualString("nr_processo", aitMovimento.getNrProcesso());
	    searchCriterios.addCriterios("tp_status", String.valueOf(aitMovimento.getTpStatus()), ItemComparator.NOTIN, Types.INTEGER);
	    searchCriterios.addCriterios("lg_cancela_movimento", String.valueOf(1), ItemComparator.NOTIN, Types.INTEGER);
		searchCriterios.addCriterios("tp_status", String.valueOf(TipoStatusEnum.RECURSO_JARI.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.RECURSO_CETRAN.getKey()), ItemComparator.IN, Types.INTEGER);
	    List<AitMovimento> recursoMovimentos = buscarRecurso(aitMovimento, searchCriterios);
	    if (!recursoMovimentos.isEmpty()) {
	        return recursoMovimentos.get(0).getTpStatus();
	    } else {
	        throw new ValidacaoException("Recurso não encontrado.");
	    }
	}

	private static List<AitMovimento> buscarRecurso(AitMovimento aitMovimento, SearchCriterios searchCriterios) throws Exception {
	    return aitMovimentoService.find(searchCriterios);
	}

	
	private static void adicionarDataEncerramento(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) {
		switch(aitMovimento.getTpStatus()) {
			case AitMovimentoServices.JARI_COM_PROVIMENTO:
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
			case AitMovimentoServices.CETRAN_DEFERIDO:
			case AitMovimentoServices.CETRAN_INDEFERIDO:
				recursoJariDadosEntrada.setDataEncerramentoRecurso(aitMovimento.getDtMovimento());
				break;
			case AitMovimentoServices.PUBLICACAO_RESULTADO_JARI:
				recursoJariDadosEntrada.setDataEncerramentoRecurso(getDataEnceramentoRecurso(aitMovimento));
				break;
		}
	}
	
	private static GregorianCalendar getDataEnceramentoRecurso(AitMovimento aitMovimento) {
		AitMovimento recursoDeferido = AitMovimentoServices.getMovimentoTpStatus(aitMovimento.getCdAit(), AitMovimentoServices.JARI_COM_PROVIMENTO);
		AitMovimento recursoIndeferido = AitMovimentoServices.getMovimentoTpStatus(aitMovimento.getCdAit(), AitMovimentoServices.JARI_SEM_PROVIMENTO);
		if (recursoDeferido.getCdMovimento() > 0) 
			return recursoDeferido.getDtMovimento();
		else 
			return recursoIndeferido.getDtMovimento();
	}
	
	private static boolean contains(int[] array, int num) {
		for(int n : array)
			if(n == num)
				return true;		
		
		return false;
	}
	
	private static void setDataPublicacaoResultado(RecursoJariDadosEntrada recursoJariDadosEntrada, AitMovimento aitMovimento) {
		if (aitMovimento.getTpStatus() == AitMovimentoServices.PUBLICACAO_RESULTADO_JARI) {
			recursoJariDadosEntrada.setDataPublicacaoResultadoJari(aitMovimento.getDtPublicacaoDo());
		}
	}
}
