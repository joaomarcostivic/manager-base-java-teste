package com.tivic.manager.adapter.base.antiga.aitmovimento;

import java.util.GregorianCalendar;

public class AitMovimentoOldBuilder {
	private AitMovimentoOld aitMovimentoOld;
	
	public AitMovimentoOldBuilder() {
		this.aitMovimentoOld = new AitMovimentoOld();
	}
	
	public AitMovimentoOldBuilder setCodAit(int codigoAit) {
		this.aitMovimentoOld.setCodigoAit(codigoAit);
		return this;
	}
	
	public AitMovimentoOldBuilder setNrMovimento(int nrMovimento) {
		this.aitMovimentoOld.setNrMovimento(nrMovimento);
		return this;
	}
	
	public AitMovimentoOldBuilder setDtMovimento(GregorianCalendar dtMovimento) {
		this.aitMovimentoOld.setDtMovimento(dtMovimento);
		return this;
	}
	
	public AitMovimentoOldBuilder setNrRemessa(int nrRemessa) {
		this.aitMovimentoOld.setNrRemessa(nrRemessa);
		return this;
	}
	
	public AitMovimentoOldBuilder setTpStatus(int tpStatus) {
		this.aitMovimentoOld.setTpStatus(tpStatus);
		return this;
	}
	
	public AitMovimentoOldBuilder setTpArquivo(int tpArquivo) {
		this.aitMovimentoOld.setTpArquivo(tpArquivo);
		return this;
	}
	
	public AitMovimentoOldBuilder setCodOcorrencia(int codOcorrencia) {
		this.aitMovimentoOld.setCodOcorrencia(codOcorrencia);
		return this;
	}
	
	public AitMovimentoOldBuilder setLgEnviadoDetran(int lgEnviadoDetran) {
		this.aitMovimentoOld.setLgEnviadoDetran(lgEnviadoDetran);
		return this;
	}
	
	public AitMovimentoOldBuilder setStEntrega(int stEntrega) {
		this.aitMovimentoOld.setStEntrega(stEntrega);
		return this;
	}
	
	public AitMovimentoOldBuilder setNrProcesso(String nrProcesso) {
		this.aitMovimentoOld.setNrProcesso(nrProcesso);
		return this;
	}
	
	public AitMovimentoOldBuilder setDtRegistroDetran(GregorianCalendar dtRegistroDetran) {
		this.aitMovimentoOld.setDtRegistroDetran(dtRegistroDetran);
		return this;
	}
	
	public AitMovimentoOldBuilder setStRecurso(int stRecurso) {
		this.aitMovimentoOld.setStRecurso(stRecurso);
		return this;
	}
	
	public AitMovimentoOldBuilder setNrSequencial(int nrSequencial) {
		this.aitMovimentoOld.setNrSequencial(nrSequencial);
		return this;
	}
	
	public AitMovimentoOldBuilder setNrErro(String nrErro) {
		this.aitMovimentoOld.setNrErro(nrErro);
		return this;
	}
	
	public AitMovimentoOldBuilder setDtDigitacao(GregorianCalendar dtDigitacao) {
		this.aitMovimentoOld.setDtDigitacao(dtDigitacao);
		return this;
	}
	
	public AitMovimentoOldBuilder setLgCancelaMovimento(int lgCancelaMovimento) {
		this.aitMovimentoOld.setLgCancelaMovimento(lgCancelaMovimento);
		return this;
	}
	
	public AitMovimentoOldBuilder setDtCancelamento(GregorianCalendar dtCancelamento) {
		this.aitMovimentoOld.setDtCancelamento(dtCancelamento);
		return this;
	}
	
	public AitMovimentoOldBuilder setNrRemessaRegistro(int nrRemessaRegistro) {
		this.aitMovimentoOld.setNrRemessaRegistro(nrRemessaRegistro);
		return this;
	}
	
	public AitMovimentoOldBuilder setDtPrimeiroRegistro(GregorianCalendar dtPrimeiroRegistro) {
		this.aitMovimentoOld.setDtPrimeiroRegistro(dtPrimeiroRegistro);
		return this;
	}
	
	public AitMovimentoOldBuilder setStRegistroDetran(int stRegistroDetran) {
		this.aitMovimentoOld.setStRegistroDetran(stRegistroDetran);
		return this;
	}
	
	public AitMovimentoOldBuilder setStAvisoRecebimento(int stAvisoRecebimento) {
		this.aitMovimentoOld.setStAvisoRecebimento(stAvisoRecebimento);
		return this;
	}
	
	public AitMovimentoOldBuilder setDtAvisoRecebimento(GregorianCalendar dtAvisoRecebimento) {
		this.aitMovimentoOld.setDtAvisoRecebimento(dtAvisoRecebimento);
		return this;
	}
	
	public AitMovimentoOldBuilder setCdProcesso(int cdProcesso) {
		this.aitMovimentoOld.setCdProcesso(cdProcesso);
		return this;
	}
	
	public AitMovimentoOldBuilder setCdUsuario(int cdUsuario) {
		this.aitMovimentoOld.setCdUsuario(cdUsuario);
		return this;
	}
	
	public AitMovimentoOldBuilder setCdContaReceber(int cdContaReceber) {
		this.aitMovimentoOld.setCdContaReceber(cdContaReceber);
		return this;
	}
	
	public AitMovimentoOldBuilder setDtExpedicao(GregorianCalendar dtExpedicao) {
		this.aitMovimentoOld.setDtExpedicao(dtExpedicao);
		return this;
	}
	
	public AitMovimentoOldBuilder setDsObservacao(byte[] dsObservacao) {
		this.aitMovimentoOld.setDsObservacao(dsObservacao);
		return this;
	}
	
	public AitMovimentoOld build() {
		return this.aitMovimentoOld;
	}
}
