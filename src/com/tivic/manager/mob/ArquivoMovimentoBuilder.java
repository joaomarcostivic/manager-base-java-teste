package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class ArquivoMovimentoBuilder {
    private ArquivoMovimento arquivoMovimento;

    public ArquivoMovimentoBuilder() {
        arquivoMovimento = new ArquivoMovimento();
    }

    public ArquivoMovimentoBuilder setCdArquivoMovimento(int cdArquivoMovimento) {
        arquivoMovimento.setCdArquivoMovimento(cdArquivoMovimento);
        return this;
    }

    public ArquivoMovimentoBuilder setCdMovimento(int cdMovimento) {
        arquivoMovimento.setCdMovimento(cdMovimento);
        return this;
    }

    public ArquivoMovimentoBuilder setCdAit(int cdAit) {
        arquivoMovimento.setCdAit(cdAit);
        return this;
    }

    public ArquivoMovimentoBuilder setTpArquivo(int tpArquivo) {
        arquivoMovimento.setTpArquivo(tpArquivo);
        return this;
    }

    public ArquivoMovimentoBuilder setNrRemessa(int nrRemessa) {
        arquivoMovimento.setNrRemessa(nrRemessa);
        return this;
    }

    public ArquivoMovimentoBuilder setNrSequencial(int nrSequencial) {
        arquivoMovimento.setNrSequencial(nrSequencial);
        return this;
    }

    public ArquivoMovimentoBuilder setTpStatus(int tpStatus) {
        arquivoMovimento.setTpStatus(tpStatus);
        return this;
    }

    public ArquivoMovimentoBuilder setNrErro(String nrErro) {
        arquivoMovimento.setNrErro(nrErro);
        return this;
    }

    public ArquivoMovimentoBuilder setDsEntrada(String dsEntrada) {
        arquivoMovimento.setDsEntrada(dsEntrada);
        return this;
    }

    public ArquivoMovimentoBuilder setDsSaida(String dsSaida) {
        arquivoMovimento.setDsSaida(dsSaida);
        return this;
    }

    public ArquivoMovimentoBuilder setTpOrigem(int tpOrigem) {
        arquivoMovimento.setTpOrigem(tpOrigem);
        return this;
    }

    public ArquivoMovimentoBuilder setDtArquivo(GregorianCalendar dtArquivo) {
        arquivoMovimento.setDtArquivo(dtArquivo);
        return this;
    }

    public ArquivoMovimentoBuilder setDsSugestaoCorrecao(String dsSugestaoCorrecao) {
        arquivoMovimento.setDsSugestaoCorrecao(dsSugestaoCorrecao);
        return this;
    }

    public ArquivoMovimento build() {
        return arquivoMovimento;
    }
}

