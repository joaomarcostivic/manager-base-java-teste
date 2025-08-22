package com.tivic.manager.mob.aitpagamento.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitPagamento;

public class AitPagamentoBuilder {
    
    public AitPagamento aitPagamento;
    
    public AitPagamentoBuilder() {
        aitPagamento = new AitPagamento();
    }
    
    public AitPagamentoBuilder setCdAit(int cdAit) {
        aitPagamento.setCdAit(cdAit);
        return this;
    }
    
    public AitPagamentoBuilder setVlTarifa(Double vlTarifa) {
        aitPagamento.setVlTarifa(vlTarifa);
        return this;
    }
    
    public AitPagamentoBuilder setVlPago(Double vlPago) {
        aitPagamento.setVlPago(vlPago);
        return this;
    }
    
    public AitPagamentoBuilder setNrBanco(String nrBanco) {
        aitPagamento.setNrBanco(nrBanco);
        return this;
    }
    
    public AitPagamentoBuilder setDtPagamento(GregorianCalendar dtPagamento) {
        aitPagamento.setDtPagamento(dtPagamento);
        return this;
    }
    
    public AitPagamentoBuilder setDtCredito(GregorianCalendar dtCredito) {
        aitPagamento.setDtCredito(dtCredito);
        return this;
    }
    public AitPagamentoBuilder setNrAgencia(String nrAgencia) {
    	aitPagamento.setNrAgencia(nrAgencia);
    	return this;
    }
    public AitPagamentoBuilder setTpArrecadacao(int tpArrecadacao) {
    	aitPagamento.setTpArrecadacao(tpArrecadacao);
    	return this;
    }
    public AitPagamentoBuilder setNrContaCredito(String nrContaCredito) {
    	aitPagamento.setNrContaCredito(nrContaCredito);
    	return this;
    }
    public AitPagamentoBuilder setNrAgenciaCredito(String nrAgenciaCredito) {
    	aitPagamento.setNrAgenciaCredito(nrAgenciaCredito);
    	return this;
    }
    public AitPagamentoBuilder setTpCondicionalidade(int tpCondicionalidade) {
    	aitPagamento.setTpCondicionalidade(tpCondicionalidade);
    	return this;
    }
    
    public AitPagamentoBuilder setTpPagamento(int tpPagamento) {
    	aitPagamento.setTpPagamento(tpPagamento);
    	return this;
    }
    public AitPagamentoBuilder setTpModalidade(int tpModalidade) {
    	aitPagamento.settpModalidade(tpModalidade);
    	return this;
    }
    public AitPagamentoBuilder setUfPagamento(String ufPagamento) {
    	aitPagamento.setUfPagamento(ufPagamento);
    	return this;
    }
    public AitPagamentoBuilder setNrDocumento(String nrDocumento) {
    	aitPagamento.setNrDocumento(nrDocumento);
    	return this;
    }
    public AitPagamentoBuilder setVlRepasse(Double vlRepasse) {
    	aitPagamento.setVlRepasse(vlRepasse);
    	return this;
    }
    public AitPagamentoBuilder setCdContaReceber(int cdContaReceber) {
    	aitPagamento.setCdContaReceber(cdContaReceber);
    	return this;
    }
    public AitPagamentoBuilder setVlDetranArrecadador(Double vlDetranArrecadador) {
    	aitPagamento.setVlDetranArrecadador(vlDetranArrecadador);
    	return this;
    }
    public AitPagamentoBuilder setVlFunset(Double vlFunset) {
    	aitPagamento.setVlFunset(vlFunset);
    	return this;
    }
    public AitPagamentoBuilder setVlDenatran(Double vlDenatran) {
    	aitPagamento.setVlDenatran(vlDenatran);
    	return this;
    }
    public AitPagamentoBuilder setVlOrgao(Double vlOrgao) {
    	aitPagamento.setVlOrgao(vlOrgao);
    	return this;
    }
    public AitPagamentoBuilder setNrErro(String nrErro) {
    	aitPagamento.setNrErro(nrErro);
    	return this;
    }
    public AitPagamentoBuilder setCdArquivo(int cdArquivo) {
    	aitPagamento.setCdArquivo(cdArquivo);
    	return this;
    }
    
    public AitPagamentoBuilder setStPagamento(int stPagamento) {
    	aitPagamento.setStPagamento(stPagamento);
    	return this;
    }
    
    public AitPagamento build() {
        return aitPagamento;
    }
}