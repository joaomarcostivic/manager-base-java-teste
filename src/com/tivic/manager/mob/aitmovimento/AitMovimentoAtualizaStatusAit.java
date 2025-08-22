package com.tivic.manager.mob.aitmovimento;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class AitMovimentoAtualizaStatusAit {
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoService;
	private MovimentoNaoAtualizaStatusAit movimentoNaoAtualizaStatusAit;
	
	public AitMovimentoAtualizaStatusAit() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.movimentoNaoAtualizaStatusAit = new MovimentoNaoAtualizaStatusAit();
	}
	
	
	
	public Ait atualizaCancelado(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		Ait ait = this.aitRepository.get(aitMovimento.getCdAit(), customConnection);
		AitMovimento movimentoValido = pegarUltimoMovimentoValido(aitMovimento, customConnection);
		ait.setCdMovimentoAtual(movimentoValido.getCdMovimento());
		ait.setTpStatus(movimentoValido.getTpStatus());
		return this.aitRepository.update(ait, customConnection);
	}
	
	private AitMovimento pegarUltimoMovimentoValido(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		AitMovimento movimentoValido = new AitMovimento();
		SearchCriterios criterios = criteriosMovimento(aitMovimento.getCdAit(), checarTipoCancelamento(aitMovimento.getTpStatus()));
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.cd_ait, A.cd_movimento, A.tp_status, A.dt_movimento")
				.searchCriterios(criterios)
				.orderBy(" A.dt_movimento DESC ")
				.customConnection(customConnection)
				.build();
		aitMovimentoList = search.getList(AitMovimento.class);
		if (aitMovimentoList.isEmpty()) {
			throw new NoContentException("Nenhum movimento encontrado para este AIT.");
		}
		movimentoValido = verificarCancelamentoMovimento(aitMovimentoList, customConnection);
		return movimentoValido;
	}
	
	private String checarTipoCancelamento(int tpStatus) {
	    if (tpStatus == TipoStatusEnum.CANCELAMENTO_DIVIDA_ATIVA.getKey()) {
	        return String.valueOf(TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey());
	    } else if (tpStatus == TipoStatusEnum.CANCELAMENTO_NIP.getKey()) {
	        return String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey());
	    } else {
	        return String.valueOf(TipoStatusEnum.MULTA_PAGA.getKey());
	    }
	}

	
	private AitMovimento verificarCancelamentoMovimento(List<AitMovimento> aitMovimentoList, CustomConnection customConnection) throws Exception {
		AitMovimento movimentoValido = new AitMovimento();
		for (AitMovimento aitMovimento : aitMovimentoList) {
			int codigoCancelamentoMovimento = new CodigoCancelamentoMovimentoMap().get(aitMovimento.getTpStatus());
			AitMovimento movimentoCancelado = this.aitMovimentoService.getMovimentoTpStatus(aitMovimento.getCdAit(), codigoCancelamentoMovimento);
			if (movimentoCancelado.getCdMovimento() <= 0) {
				movimentoValido = aitMovimento;
				break;
			}
		}
		if (movimentoValido.getCdMovimento() <= 0) {
			throw new NoContentException("Nenhum movimento valido encontrado para este AIT.");
		}
		return movimentoValido;
	}
	
	private SearchCriterios criteriosMovimento(int cdAit, String tpStatus) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		searchCriterios.addCriterios("A.tp_status", tpStatus, ItemComparator.NOTIN, Types.INTEGER);
		searchCriterios.addCriterios("A.lg_enviado_detran", String.valueOf(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey()), ItemComparator.NOTIN, Types.INTEGER);
		searchCriterios.addCriterios("A.lg_enviado_detran", String.valueOf(TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey()), ItemComparator.NOTIN, Types.INTEGER);
		searchCriterios.addCriterios("A.tp_status",  this.movimentoNaoAtualizaStatusAit.getMovimento().toString().replace("[", "").replace("]", ""), ItemComparator.NOTIN, Types.INTEGER);
		return searchCriterios;
	}

}
