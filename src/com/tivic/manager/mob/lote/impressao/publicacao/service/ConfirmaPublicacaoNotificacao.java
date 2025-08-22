package com.tivic.manager.mob.lote.impressao.publicacao.service;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.TipoSituacaoPublicacaoDO;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.publicacao.map.TipoPublicacaoNotificacaoMap;
import com.tivic.manager.mob.lote.impressao.publicacao.validator.ConfirmaPublicacaoValidatios;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ConfirmaPublicacaoNotificacao implements IConfirmaPublicacaoNotificacao {

	private CustomConnection customConnection;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private IAitMovimentoService aitMovimentoServices;
	private AitMovimentoRepository aitMovimentoRepository;

	public ConfirmaPublicacaoNotificacao(CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	@Override
	public LoteImpressao confirmar(int cdLoteImpressao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception {
		LoteImpressao lotePublicacao = this.loteImpressaoRepository.get(cdLoteImpressao, this.customConnection);
		if (lotePublicacao.getDtEnvio() != null) {
			throw new ValidacaoException("Já houve confirmação desta publicação.");
		} 
		atualizarLote(lotePublicacao, dtConfirmacao);
		new ConfirmaPublicacaoValidatios().validate(lotePublicacao, customConnection);
		lancarPublicacao(lotePublicacao, dtConfirmacao, cdUsuario);
		return lotePublicacao;
	}
	
	private void atualizarLote(LoteImpressao lotePublicacao, GregorianCalendar dtConfirmacao) throws Exception {
		lotePublicacao.setDtEnvio(dtConfirmacao);
		lotePublicacao.setStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_ENVIO);
		this.loteImpressaoRepository.update(lotePublicacao, this.customConnection);
	}
	
	private void lancarPublicacao(LoteImpressao lotePublicacao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", lotePublicacao.getCdLoteImpressao());
		List<LoteImpressaoAit> loteImpressaoAitList = this.loteImpressaoAitRepository.find(searchCriterios, this.customConnection);
		int tpMovimento = new TipoPublicacaoNotificacaoMap().getStatusMovimentoNotificacao(lotePublicacao.getTpDocumento());
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
			AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(loteImpressaoAit.getCdAit(), tpMovimento);
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
		int tpStatusPublicacao =  new TipoPublicacaoNotificacaoMap().getStatusMovimentoPublicacao(movimentoNotificacao.getTpStatus()); 
		AitMovimento movimentoPublicacao = this.aitMovimentoServices.getMovimentoTpStatus(movimentoNotificacao.getCdAit(), tpStatusPublicacao);
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
