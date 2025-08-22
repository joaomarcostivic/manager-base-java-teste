package com.tivic.manager.mob.talonario;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Talonario;

public class TalonarioBuilder {

    private Talonario talonario;

    public TalonarioBuilder() {
    	talonario = new Talonario();
    }
    
    public TalonarioBuilder(Talonario talonario) {
    	this.talonario = talonario;
    }

    public TalonarioBuilder setCdTalao(int cdTalao) {
    	talonario.setCdTalao(cdTalao);
        return this;
    }

    public TalonarioBuilder setNrTalao(int nrTalao) {
    	talonario.setNrTalao(nrTalao);
        return this;
    }

    public TalonarioBuilder setNrInicial(int nrInicial) {
    	talonario.setNrInicial(nrInicial);
        return this;
    }
    
    public TalonarioBuilder setNrFinal(int nrFinal) {
    	talonario.setNrFinal(nrFinal);
        return this;
    }
    
    public TalonarioBuilder setCdAgente(int cdAgente) {
    	talonario.setCdAgente(cdAgente);
		return this;
	}
    
    public TalonarioBuilder setDtEntrega(GregorianCalendar dtEntrega) {
    	talonario.setDtEntrega(dtEntrega);
        return this;
    }

    public TalonarioBuilder setDtDevolucao(GregorianCalendar dtDevolucao) {
    	talonario.setDtDevolucao(dtDevolucao);
        return this;
    }

    public TalonarioBuilder setStTalao(int stTalao) {
    	talonario.setStTalao(stTalao);
        return this;
    }

    public TalonarioBuilder setTpTalao(int tpTalao) {
    	talonario.setTpTalao(tpTalao);
        return this;
    }
    
    public TalonarioBuilder setSgTalao(String sgTalao) {
    	talonario.setSgTalao(sgTalao);
        return this;
    }
    
    public TalonarioBuilder setStLogin(int stLogin) {
    	talonario.setStLogin(stLogin);
        return this;
    }
    
    public TalonarioBuilder setNrUltimoAit(int nrUltimoAit) {
    	talonario.setNrUltimoAit(nrUltimoAit);
        return this;
    }

    public Talonario build() {
		return talonario;
	}
}