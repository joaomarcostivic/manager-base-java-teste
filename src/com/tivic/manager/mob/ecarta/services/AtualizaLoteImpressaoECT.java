package com.tivic.manager.mob.ecarta.services;

import java.util.List;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.enums.LoteImpressaoSituacaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AtualizaLoteImpressaoECT implements IAtualizaLoteImpressaoECT{
	private CustomConnection customConnection;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;

	public AtualizaLoteImpressaoECT() throws Exception {
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory
				.get(ILoteImpressaoAitRepository.class);
	}

	@Override
	public void confirmarProducao(int cdLoteImpressao) throws Exception {
		this.customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			atulizarLoteImpressaoCofirmado(cdLoteImpressao);
			atualizarLoteImpressaoAitConfirmado(cdLoteImpressao);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void rejeitarProducao(int cdLoteImpressao) throws Exception {
		this.customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			atulizarLoteImpressaoRejeitado(cdLoteImpressao);
			atualizarLoteImpressaoAitRejeitado(cdLoteImpressao);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	private void atulizarLoteImpressaoCofirmado(int cdLoteImpressao) throws Exception {
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, this.customConnection);
		loteImpressao.setStLoteImpressao(LoteImpressaoSituacaoEnum.ECARTAS_LOTE_AUTORIZADO.getKey());
		loteImpressaoRepository.update(loteImpressao, this.customConnection);
	}

	private void atualizarLoteImpressaoAitConfirmado(int cdLoteImpressao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressao, true);
		List<LoteImpressaoAit> loteImpressaoAitList = this.loteImpressaoAitRepository.find(searchCriterios,
				this.customConnection);
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
			loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.ECARTAS_ARQUIVO_AUTORIZADO);
			loteImpressaoAitRepository.update(loteImpressaoAit, this.customConnection);
		}
	}

	private void atulizarLoteImpressaoRejeitado(int cdLoteImpressao) throws Exception {
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, this.customConnection);
		loteImpressao.setStLoteImpressao(LoteImpressaoSituacaoEnum.ECARTAS_LOTE_REJEITADO.getKey());
		loteImpressaoRepository.update(loteImpressao, this.customConnection);
	}

	private void atualizarLoteImpressaoAitRejeitado(int cdLoteImpressao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressao, true);
		List<LoteImpressaoAit> loteImpressaoAitList = this.loteImpressaoAitRepository.find(searchCriterios,
				this.customConnection);
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
			loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.ECARTAS_ARQUIVO_NEGADO);
			loteImpressaoAitRepository.update(loteImpressaoAit, this.customConnection);
		}
	}
}
