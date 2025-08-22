package com.tivic.manager.mob.ait.validacao;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.ait.SituacaoAitEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ValidacaoAitPendente implements IValidacaoAitPendente {
	
	private IAitService aitService;
	private IAitMovimentoService aitMovimentoService;

	
	public ValidacaoAitPendente() throws Exception {
		aitService = (IAitService) BeansFactory.get(IAitService.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public PagedResponse<AitPendenteDTO> buscarAitsPendentes(ValidacaoAitPendenteSearch validacaoAitPendenteSearch) throws ValidacaoException, Exception {
		Search<AitPendenteDTO> searchAitPendenteDTO = searchListagemAits(setCriteriosSearch(validacaoAitPendenteSearch));
		List<AitPendenteDTO> aitsPendentesList = searchAitPendenteDTO.getList(AitPendenteDTO.class);
		if (aitsPendentesList == null || aitsPendentesList.isEmpty()) {
			throw new ValidacaoException("Nenhum AIT encontrado.");
		}
		return new PagedResponse<AitPendenteDTO>(aitsPendentesList, searchAitPendenteDTO.getRsm().getTotal());
	}
	
	private Search<AitPendenteDTO> searchListagemAits(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		Search<AitPendenteDTO> search = new SearchBuilder<AitPendenteDTO>("mob_ait A")
				.fields("DISTINCT(A.cd_ait), A.dt_infracao, A.id_ait, A.nr_placa, A.tp_status, F.ds_ocorrencia, A.cd_ocorrencia, "
					+ "D.cd_equipamento, D.tp_equipamento, C.tp_competencia, B.nr_matricula, B.nm_agente, C.nr_cod_detran, C.ds_infracao, E.ds_observacao"
				)	
			.addJoinTable("LEFT OUTER JOIN mob_agente B ON (A.cd_agente = B.cd_agente)")
			.addJoinTable("LEFT OUTER JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao)")
			.addJoinTable("LEFT OUTER JOIN grl_equipamento D ON (A.cd_equipamento = D.cd_equipamento)")
			.addJoinTable("LEFT OUTER JOIN mob_ait_movimento E ON (A.cd_ait = E.cd_ait AND A.cd_movimento_atual = E.cd_movimento)")
			.addJoinTable("LEFT OUTER JOIN mob_ocorrencia F ON (A.cd_ocorrencia = F.cd_ocorrencia)")
			.searchCriterios(searchCriterios)
			.orderBy("A.dt_infracao DESC")
			.count()
		.build();
		return search;	
	}
	
	private SearchCriterios setCriteriosSearch(ValidacaoAitPendenteSearch validacaoAitPendenteSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyString("A.id_ait", validacaoAitPendenteSearch.getIdAit(),  validacaoAitPendenteSearch.getIdAit() != null);
		searchCriterios.addCriteriosEqualOnlyDate("A.dt_infracao", validacaoAitPendenteSearch.getDtInfracao(), validacaoAitPendenteSearch.getDtInfracao() != null);
		searchCriterios.addCriteriosEqualInteger("A.cd_agente", validacaoAitPendenteSearch.getCdAgente(), validacaoAitPendenteSearch.getCdAgente() > -1);
		searchCriterios.addCriteriosEqualInteger("A.cd_ocorrencia", validacaoAitPendenteSearch.getCdOcorrencia(), validacaoAitPendenteSearch.getCdOcorrencia() > 0);
		searchCriterios.addCriterios("A.cd_ocorrencia", "1", ItemComparator.GREATER_EQUAL, Types.INTEGER);
		searchCriterios.addCriteriosEqualInteger("A.st_ait", SituacaoAitEnum.ST_PENDENTE_CONFIRMACAO.getKey(), true);
		searchCriterios.setQtDeslocamento(((validacaoAitPendenteSearch.getLimit() * validacaoAitPendenteSearch.getPage()) - validacaoAitPendenteSearch.getLimit()));
		searchCriterios.setQtLimite(validacaoAitPendenteSearch.getLimit());
		return searchCriterios;
	}

	@Override
	public void validarAit(AitPendenteDTO aitPendenteDTO) throws Exception {
		Ait ait = getAit(aitPendenteDTO);
		checarSituacaoAit(ait);
		ait.setStAit(SituacaoAitEnum.ST_CONFIRMADO.getKey());
		removerOcorrenciaCancelamento(ait);
		aitService.update(ait);
	}
	
	private void removerOcorrenciaCancelamento(Ait ait) {
		ait.setCdOcorrencia(0);
		ait.setTxtCancelamento(null);
	}
	
	@Override
	public void invalidarAit(AitPendenteDTO aitPendenteDTO) throws Exception {
		Ait ait = getAit(aitPendenteDTO);
		checarSituacaoAit(ait);
		ait.setStAit(SituacaoAitEnum.ST_CANCELADO.getKey());
		aitService.update(ait);
		atualizarAitMovimento(ait);
	}
	
	private void atualizarAitMovimento(Ait ait) throws Exception {
		AitMovimento movimento = aitMovimentoService.getStatusMovimento(ait.getCdAit(), TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		if(movimento.getLgEnviadoDetran() == TipoSituacaoAitEnum.NAO_ENVIADO.getKey()) {
			movimento.setLgEnviadoDetran(TipoSituacaoAitEnum.NAO_ENVIAR.getKey());
			aitMovimentoService.update(movimento);
		}
	}
	
	private void checarSituacaoAit(Ait ait) throws Exception {
		if (ait.getStAit() != SituacaoAitEnum.ST_PENDENTE_CONFIRMACAO.getKey()) {
			throw new Exception("Este AIT não está pendente de validação.");
		}
	}
	
	private Ait getAit(AitPendenteDTO aitPendenteDTO) throws Exception {
		Ait ait = aitService.get(aitPendenteDTO.getCdAit());
		if(ait == null)
			throw new Exception("Ait não encontrado.");
		return ait;
	} 
}
