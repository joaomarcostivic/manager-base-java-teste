package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class AitMovimentoBuilder {

    private AitMovimento aitMovimento;

    public AitMovimentoBuilder() {
        aitMovimento = new AitMovimento();
    }

    public AitMovimentoBuilder setCdMovimento(int cdMovimento) {
        aitMovimento.setCdMovimento(cdMovimento);
        return this;
    }

    public AitMovimentoBuilder setCdAit(int cdAit) {
        aitMovimento.setCdAit(cdAit);
        return this;
    }

    public AitMovimentoBuilder setNrMovimento(int nrMovimento) {
        aitMovimento.setNrMovimento(nrMovimento);
        return this;
    }

    public AitMovimentoBuilder setDtMovimento(GregorianCalendar dtMovimento) {
        aitMovimento.setDtMovimento(dtMovimento);
        return this;
    }

    public AitMovimentoBuilder setNrRemessa(int nrRemessa) {
        aitMovimento.setNrRemessa(nrRemessa);
        return this;
    }

    public AitMovimentoBuilder setTpStatus(int tpStatus) {
        aitMovimento.setTpStatus(tpStatus);
        return this;
    }

    public AitMovimentoBuilder setTpArquivo(int tpArquivo) {
        aitMovimento.setTpArquivo(tpArquivo);
        return this;
    }

    public AitMovimentoBuilder setDsObservacao(String dsObservacao) {
        aitMovimento.setDsObservacao(dsObservacao);
        return this;
    }

    public AitMovimentoBuilder setCdOcorrencia(int cdOcorrencia) {
        aitMovimento.setCdOcorrencia(cdOcorrencia);
        return this;
    }

    public AitMovimentoBuilder setLgEnviadoDetran(int lgEnviadoDetran) {
        aitMovimento.setLgEnviadoDetran(lgEnviadoDetran);
        return this;
    }

    public AitMovimentoBuilder setStEntrega(int stEntrega) {
        aitMovimento.setStEntrega(stEntrega);
        return this;
    }

    public AitMovimentoBuilder setNrProcesso(String nrProcesso) {
        aitMovimento.setNrProcesso(nrProcesso);
        return this;
    }

    public AitMovimentoBuilder setDtRegistroDetran(GregorianCalendar dtRegistroDetran) {
        aitMovimento.setDtRegistroDetran(dtRegistroDetran);
        return this;
    }

    public AitMovimentoBuilder setStRecurso(int stRecurso) {
        aitMovimento.setStRecurso(stRecurso);
        return this;
    }

    public AitMovimentoBuilder setNrSequencial(int nrSequencial) {
        aitMovimento.setNrSequencial(nrSequencial);
        return this;
    }

    public AitMovimentoBuilder setNrErro(String nrErro) {
        aitMovimento.setNrErro(nrErro);
        return this;
    }

    public AitMovimentoBuilder setDtDigitacao(GregorianCalendar dtDigitacao) {
        aitMovimento.setDtDigitacao(dtDigitacao);
        return this;
    }

    public AitMovimentoBuilder setLgCancelaMovimento(int lgCancelaMovimento) {
        aitMovimento.setLgCancelaMovimento(lgCancelaMovimento);
        return this;
    }

    public AitMovimentoBuilder setDtCancelamento(GregorianCalendar dtCancelamento) {
    	aitMovimento.setDtCancelamento(dtCancelamento);
    	return this;
    }
    
    public AitMovimentoBuilder setNrRemessaRegistro(int nrRemessaRegistro) {
    	aitMovimento.setNrRemessaRegistro(nrRemessaRegistro);
    	return this;
    }
    
    public AitMovimentoBuilder setDtPrimeiroRegistro(GregorianCalendar dtPrimeiroRegistro) {
    	aitMovimento.setDtPrimeiroRegistro(dtPrimeiroRegistro);
    	return this;
    }
    
    public AitMovimentoBuilder setStRegistroDetran(int stRegistroDetran) {
    	aitMovimento.setStRegistroDetran(stRegistroDetran);
    	return this;
    }

    public AitMovimentoBuilder setCdProcesso(int cdProcesso) {
    	aitMovimento.setCdProcesso(cdProcesso);
    	return this;
    }
    
    public AitMovimentoBuilder setCdUsuario(int cdUsuario) {
    	aitMovimento.setCdUsuario(cdUsuario);
    	return this;
    }
    
    public AitMovimentoBuilder setCdContaReceber(int cdContaReceber) {
    	aitMovimento.setCdContaReceber(cdContaReceber);
    	return this;
    }
    
    public AitMovimentoBuilder setDtPublicacaoDo(GregorianCalendar dtPublicacaoDo) {
    	aitMovimento.setDtPublicacaoDo(dtPublicacaoDo);
    	return this;
    }

    public AitMovimento build() {
		return aitMovimento;
	}
}