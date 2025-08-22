package com.tivic.manager.mob.lote.impressao;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;

public class LoteImpressaoBuilder {
	private LoteImpressao loteImpressao;
	
	public LoteImpressaoBuilder() {
		loteImpressao = new LoteImpressao();
	}
	
	public LoteImpressaoBuilder addIdLoteImpressao(String idLoteImpressao) {
		loteImpressao.setIdLoteImpressao(idLoteImpressao);
		return this;
	}

	public LoteImpressaoBuilder addCdLoteImpressao(int cdLoteImpressao) {
		loteImpressao.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public LoteImpressaoBuilder addDtCriacao(GregorianCalendar dtCriacao) {
		loteImpressao.setDtCriacao(dtCriacao);
		return this;
	}
	
	public LoteImpressaoBuilder addStLoteImpressao(int stLoteImpressao) {
		loteImpressao.setStLoteImpressao(stLoteImpressao);
		return this;
	}
	
	public LoteImpressaoBuilder addCdUsuario(int cdUsuario) {
		loteImpressao.setCdUsuario(cdUsuario);
		return this;
	}
	
	public LoteImpressaoBuilder addTpDocumento(int tpDocumento) {
		loteImpressao.setTpDocumento(tpDocumento);
		return this;
	}
	
	public LoteImpressaoBuilder addQtdDocumentos(int qtdDocumentos) {
		loteImpressao.setQtdDocumentos(qtdDocumentos);
		return this;
	}
	
	public LoteImpressaoBuilder addTxtObservacao(String txtObservacao) {
		loteImpressao.setTxtObservacao(txtObservacao);
		return this;
	}
	
	public LoteImpressaoBuilder addTpDestino(int tpDestino) {
		loteImpressao.setTpDestino(tpDestino);
		return this;
	}
	
	public LoteImpressaoBuilder addTpLoteImpressao(int tpLoteImpressao) {
		loteImpressao.setTpLoteImpressao(tpLoteImpressao);
		return this;
	}
	
	public LoteImpressaoBuilder addAits(List<LoteImpressaoAit> aits) {
		loteImpressao.setAits(aits);
		return this;
	}
	
	public LoteImpressao build() {
		return loteImpressao;
	}
}
