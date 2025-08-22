package com.tivic.manager.mob.lote.impressao.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTO;

public class LoteImpressaoAitDTOBuilder {
	private LoteImpressaoAitDTO loteImpressaoAitDTO;
	
	public LoteImpressaoAitDTOBuilder() {
		this.loteImpressaoAitDTO = new LoteImpressaoAitDTO();
	}
	
	public LoteImpressaoAitDTOBuilder addCdLoteImpressao(int cdLoteImpressao) {
		this.loteImpressaoAitDTO.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}

	public LoteImpressaoAitDTOBuilder addCdAit(int cdAit) {
		this.loteImpressaoAitDTO.setCdAit(cdAit);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addIdAit(String idAit) {
		this.loteImpressaoAitDTO.setIdAit(idAit);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addNrPlaca(String nrPlaca) {
		this.loteImpressaoAitDTO.setNrPlaca(nrPlaca);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addDtInfracao(GregorianCalendar dtInfracao) {
		this.loteImpressaoAitDTO.setDtInfracao(dtInfracao);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addDtVencimento(GregorianCalendar dtVencimento) {
		this.loteImpressaoAitDTO.setDtVencimento(dtVencimento);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addNrRenavam(String nrRenavam) {
		this.loteImpressaoAitDTO.setNrRenavan(nrRenavam);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addNmProprietario(String nmProprietario) {
		this.loteImpressaoAitDTO.setNmProprietario(nmProprietario);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addIdLoteImpressao(String idLoteImpressao) {
		this.loteImpressaoAitDTO.setIdLoteImpressao(idLoteImpressao);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addDtCriacao(GregorianCalendar dtCriacao) {
		this.loteImpressaoAitDTO.setDtCriacao(dtCriacao);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addNmPessoa(String nmPessoa) {
		this.loteImpressaoAitDTO.setNmPessoa(nmPessoa);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addQtdDocumentos(int qtdDocumentos) {
		this.loteImpressaoAitDTO.setQtdDocumentos(qtdDocumentos);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addNrLote(int nrLote) {
		this.loteImpressaoAitDTO.setNrLote(nrLote);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addNrEtiqueta(int nrEtiqueta) {
		this.loteImpressaoAitDTO.setNrEtiqueta(nrEtiqueta);
		return this;
	}
	
	public LoteImpressaoAitDTOBuilder addStLoteImpressao(int stLoteImpressao) {
		this.loteImpressaoAitDTO.setStLoteImpressao(stLoteImpressao);
		return this;
	}

	public LoteImpressaoAitDTOBuilder addTpRecurso(int tpRecurso) {
		this.loteImpressaoAitDTO.setTpRecurso(tpRecurso);
		return this;
	}

	public LoteImpressaoAitDTOBuilder addCdUsuario(int cdUsuario) {
		this.loteImpressaoAitDTO.setCdUsuario(cdUsuario);
		return this;
	}
	
	public LoteImpressaoAitDTO build() {
		return this.loteImpressaoAitDTO;
	}
}
