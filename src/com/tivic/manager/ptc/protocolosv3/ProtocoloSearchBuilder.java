package com.tivic.manager.ptc.protocolosv3;

import com.tivic.sol.search.SearchCriterios;

public class ProtocoloSearchBuilder {

	private SearchCriterios searchCriterios;

	public ProtocoloSearchBuilder() {
		this.searchCriterios = new SearchCriterios();
	}

	public ProtocoloSearchBuilder setCdTipoDocumento(int cdTipoDocumento) {
		searchCriterios.addCriteriosEqualInteger("E.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
		return this;
	}

	public ProtocoloSearchBuilder setCdDocumento(int cdDocumento) {
		searchCriterios.addCriteriosEqualInteger("D.cd_documento", cdDocumento, cdDocumento > 0);
		return this;
	}

	public ProtocoloSearchBuilder setIdAit(String idAit) {
		searchCriterios.addCriteriosLikeAnyString("B.id_ait", idAit, idAit != null);
		return this;
	}

	public ProtocoloSearchBuilder setCdFase(int cdFase) {
		searchCriterios.addCriteriosEqualInteger("F.cd_fase", cdFase, cdFase > 0);
		return this;
	}

	public ProtocoloSearchBuilder setNrDocumento(String nrDocumento) {
		searchCriterios.addCriteriosEqualString("D.nr_documento", nrDocumento, nrDocumento != null && !nrDocumento.isEmpty());
		return this;
	}

	public ProtocoloSearchBuilder setNrPlaca(String nrPlaca) {
		searchCriterios.addCriteriosEqualString("B.nr_placa", nrPlaca, nrPlaca != null);
		return this;
	}

	public ProtocoloSearchBuilder setCdSituacaoDocumento(int cdSituacaoDocumento) {
		searchCriterios.addCriteriosEqualInteger("D.cd_situacao_documento", cdSituacaoDocumento,
				cdSituacaoDocumento > 0);
		return this;
	}

	public ProtocoloSearchBuilder setDtProtocoloInicial(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("D.dt_protocolo", dtInicial, dtInicial != null);
		return this;
	}

	public ProtocoloSearchBuilder setDtProtocoloFinal(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("D.dt_protocolo", dtFinal, dtFinal != null);
		return this;
	}

	public ProtocoloSearchBuilder setTpStatus(int tpStatus) {
		searchCriterios.addCriteriosEqualInteger("A5.tp_status", tpStatus, tpStatus > 0);
		return this;
	}

	public ProtocoloSearchBuilder setDtPrazoInicial(String dtPrazoInicial) {
		searchCriterios.addCriteriosEqualString("dtPrazoInicial", dtPrazoInicial, dtPrazoInicial != null);
		return this;
	}

	public ProtocoloSearchBuilder setDtPrazoFinal(String dtPrazoFinal) {
		searchCriterios.addCriteriosEqualString("dtPrazoFinal", dtPrazoFinal, dtPrazoFinal != null);
		return this;
	}

	public ProtocoloSearchBuilder setLgEnviadoDetran(int lgEnviadoDetran) {
		searchCriterios.addCriteriosEqualInteger("A.lg_enviado_detran", lgEnviadoDetran, lgEnviadoDetran >= 0);
		return this;
	}
	
	public ProtocoloSearchBuilder setLgTempestividade(String lgTempestividade) {
		searchCriterios.addCriteriosEqualString("lgTempestividade", lgTempestividade, lgTempestividade != null);
		return this;
	}
	
	public ProtocoloSearchBuilder setNrCpfRequerente(String nrCpfRequerente) {
		searchCriterios.addCriteriosEqualString("B.nr_cpf", nrCpfRequerente);
		return this;
	}
	
	public ProtocoloSearchBuilder setTpDocumento(int tpDocumento) {
		searchCriterios.addCriteriosEqualInteger("D.cd_tipo_documento", tpDocumento, tpDocumento > 0);
		return this;
	}

	public ProtocoloSearchBuilder setQtDeslocamento(int nrLimite, int nrPagina) {
		searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
		return this;
	}

	public ProtocoloSearchBuilder setQtLimite(int nrLimite) {
		searchCriterios.setQtLimite(nrLimite);
		return this;
	}

	public SearchCriterios build() {
		return this.searchCriterios;
	}
}
