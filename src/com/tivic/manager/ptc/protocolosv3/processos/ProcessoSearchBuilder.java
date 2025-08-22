package com.tivic.manager.ptc.protocolosv3.processos;

import com.tivic.sol.search.SearchCriterios;

public class ProcessoSearchBuilder {

	private SearchCriterios searchCriterios;
	
	public ProcessoSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public ProcessoSearchBuilder setAitDocumento(String aitDocumento) {
		searchCriterios.addCriteriosEqualString("ait_documento", aitDocumento, aitDocumento != null);
		return this;
	}
	
	public ProcessoSearchBuilder setCdTipoDocumento(int cdTipoDocumento) {
		searchCriterios.addCriteriosEqualInteger("E.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
		return this;
	}
	
	public ProcessoSearchBuilder setIdAit(String idAit) {
		searchCriterios.addCriteriosEqualString("B.id_ait", idAit, idAit != null);
		return this;
	}
	
	public ProcessoSearchBuilder setStAtual(Integer stAtual) {
		searchCriterios.addCriteriosEqualInteger("B.tp_status", stAtual, stAtual != null);
		return this;
	}
	
	public ProcessoSearchBuilder setCdFase(int cdFase) {
		searchCriterios.addCriteriosEqualInteger("F.cd_fase", cdFase, cdFase > 0);
		return this;
	}
	
	public ProcessoSearchBuilder setNrDocumento(String nrDocumento) {
		searchCriterios.addCriteriosEqualString("D.nr_documento", nrDocumento, nrDocumento != null);
		return this;
	}
	
	public ProcessoSearchBuilder setNrPlaca(String nrPlaca) {
		searchCriterios.addCriteriosEqualString("B.nr_placa", nrPlaca, nrPlaca != null);
		return this;
	}
	
	public ProcessoSearchBuilder setCdSituacaoDocumento(int cdSituacaoDocumento) {
		searchCriterios.addCriteriosEqualInteger("D.cd_situacao_documento", cdSituacaoDocumento, cdSituacaoDocumento > 0);
		return this;
	}
	
	public ProcessoSearchBuilder setDtProtocoloInicial(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("D.dt_protocolo", dtInicial, dtInicial != null);
		return this;
	}
	
	public ProcessoSearchBuilder setDtProtocoloFinal(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("D.dt_protocolo", dtFinal, dtFinal != null);
		return this;
	}
	
	public ProcessoSearchBuilder setTpStatus(int tpStatus) {
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus, tpStatus > 0);
		return this;
	}
	
	public ProcessoSearchBuilder setStatus(Integer ctMovimento) {
		searchCriterios.addCriteriosEqualInteger("CT.tp_status", ctMovimento, ctMovimento != null);
		return this;
	}
	
	public ProcessoSearchBuilder setDtInicialVencimento(String dtInicialVencimento) {
		searchCriterios.addCriteriosGreaterDate("PTCV1.dt_protocolo", dtInicialVencimento, dtInicialVencimento != null);
		return this;
	}
	
	public ProcessoSearchBuilder setDtFinalVencimento(String dtFinalVencimento) {
		searchCriterios.addCriteriosMinorDate("PTCV2.dt_protocolo", dtFinalVencimento, dtFinalVencimento != null);
		return this;
	}
	
	public ProcessoSearchBuilder setDeslocamento(int nrLimite, int nrPagina) {
		searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
		return this;
	}
	
	public ProcessoSearchBuilder setLimite(int nrLimite) {
		searchCriterios.setQtLimite(nrLimite);
		return this;
	}
	
	public SearchCriterios build() {
		return searchCriterios;
	}
}
