package com.tivic.manager.mob.lotes.service.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.TipoSituacaoPublicacaoDO;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.manager.mob.lotes.publicacao.map.TipoPublicacaoMap;
import com.tivic.manager.mob.lotes.repository.publicacao.ILotePublicacaoAitRepository;
import com.tivic.manager.mob.lotes.repository.publicacao.LotePublicacaoRepository;
import com.tivic.manager.mob.lotes.validator.publicacao.ConfirmaPublicacaoValidations;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ConfirmaPublicacao implements IConfirmaPublicacao {

	private CustomConnection customConnection;
	private LotePublicacaoRepository lotePublicacaoRepository;
	private ILotePublicacaoAitRepository lotePublicacaoAitRepository;
	private IAitMovimentoService aitMovimentoService;
	private AitMovimentoRepository aitMovimentoRepository;

	public ConfirmaPublicacao(CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		this.lotePublicacaoRepository = (LotePublicacaoRepository) BeansFactory.get(LotePublicacaoRepository.class);
		this.lotePublicacaoAitRepository = (ILotePublicacaoAitRepository) BeansFactory.get(ILotePublicacaoAitRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	@Override
	public LotePublicacao confirmar(int cdLotePublicacao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception {
		LotePublicacao lotePublicacao = this.lotePublicacaoRepository.get(cdLotePublicacao, this.customConnection);
		if (lotePublicacao.getDtPublicacao() != null) {
			throw new ValidacaoException("Já houve confirmação desta publicação.");
		} 
		atualizarLote(lotePublicacao, dtConfirmacao);
		new ConfirmaPublicacaoValidations().validate(lotePublicacao, customConnection);
		lancarPublicacao(lotePublicacao, dtConfirmacao, cdUsuario);
		return lotePublicacao;
	}
	
	private void atualizarLote(LotePublicacao lotePublicacao, GregorianCalendar dtConfirmacao) throws Exception {
		lotePublicacao.setDtPublicacao(dtConfirmacao);
		lotePublicacao.setStLote(LoteImpressaoSituacao.AGUARDANDO_ENVIO);
		this.lotePublicacaoRepository.update(lotePublicacao, this.customConnection);
	}
	
	private void lancarPublicacao(LotePublicacao lotePublicacao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_publicacao", lotePublicacao.getCdLotePublicacao());
		List<LotePublicacaoAit> lotePublicacaoAitList = this.lotePublicacaoAitRepository.find(searchCriterios, this.customConnection);
		int tpMovimento = new TipoPublicacaoMap().getStatusMovimentoNotificacao(lotePublicacao.getTpPublicacao());
		for (LotePublicacaoAit lotePublicacaoAit : lotePublicacaoAitList) {
			AitMovimento aitMovimento = this.aitMovimentoService.getMovimentoTpStatus(lotePublicacaoAit.getCdAit(), tpMovimento);
			atualizarMovimentoNotificacao(aitMovimento, dtConfirmacao);
			lancarMovimentoPublicacao(aitMovimento, dtConfirmacao, cdUsuario);
		}
	}
	
	private void atualizarMovimentoNotificacao(AitMovimento aitMovimento, GregorianCalendar dtConfirmacao) throws Exception {
		aitMovimento.setDtPublicacaoDo(dtConfirmacao);
		aitMovimento.setStPublicacaoDo(TipoSituacaoPublicacaoDO.PUBLICADO_DO.getKey());
		this.aitMovimentoRepository.update(aitMovimento, this.customConnection);
	}
	
	private void lancarMovimentoPublicacao(AitMovimento movimentoNotificacao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception {
		int tpStatusPublicacao = new TipoPublicacaoMap().getStatusMovimentoPublicacao(movimentoNotificacao.getTpStatus()); 
		AitMovimento movimentoPublicacao = this.aitMovimentoService.getMovimentoTpStatus(movimentoNotificacao.getCdAit(), tpStatusPublicacao);
		if (movimentoPublicacao.getCdMovimento() <= 0) {
			movimentoPublicacao = new AitMovimentoBuilder()
					.setCdAit(movimentoNotificacao.getCdAit())
					.setCdUsuario(cdUsuario)
					.setDtMovimento()
					.setTpStatus(tpStatusPublicacao)
					.setDtPublicacaoDo(dtConfirmacao)
					.setStPublicacaoDo(TipoSituacaoPublicacaoDO.PUBLICADO_DO.getKey())
					.build();
			this.aitMovimentoRepository.insert(movimentoPublicacao, this.customConnection);
		} 
	}
}
