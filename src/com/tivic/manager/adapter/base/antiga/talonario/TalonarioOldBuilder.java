package com.tivic.manager.adapter.base.antiga.talonario;

import java.util.GregorianCalendar;

public class TalonarioOldBuilder {
	private TalonarioOld talonarioOld;
	
    public TalonarioOldBuilder() {
    	this.talonarioOld = new TalonarioOld();
    }

    public TalonarioOldBuilder setCodTalao(int codTalao) {
    	talonarioOld.setCodTalao(codTalao);
        return this;
    }

    public TalonarioOldBuilder setNrTalao(int nrTalao) {
    	talonarioOld.setNrTalao(nrTalao);
        return this;
    }

    public TalonarioOldBuilder setNrInicial(int nrInicial) {
    	talonarioOld.setNrInicial(nrInicial);
        return this;
    }
    
    public TalonarioOldBuilder setNrFinal(int nrFinal) {
    	talonarioOld.setNrFinal(nrFinal);
        return this;
    }
    
    public TalonarioOldBuilder setCodAgente(int codAgente) {
    	talonarioOld.setCodAgente(codAgente);
		return this;
	}
    
    public TalonarioOldBuilder setDtEntrega(GregorianCalendar dtEntrega) {
    	talonarioOld.setDtEntrega(dtEntrega);
        return this;
    }

    public TalonarioOldBuilder setDtDevolucao(GregorianCalendar dtDevolucao) {
    	talonarioOld.setDtDevolucao(dtDevolucao);
        return this;
    }

    public TalonarioOldBuilder setStTalao(int stTalao) {
    	talonarioOld.setStTalao(stTalao);
        return this;
    }

    public TalonarioOldBuilder setTpTalao(int tpTalao) {
    	talonarioOld.setTpTalao(tpTalao);
        return this;
    }
    
    public TalonarioOldBuilder setSgTalao(String sgTalao) {
    	talonarioOld.setSgTalao(sgTalao);
        return this;
    }
    
    public TalonarioOldBuilder setNrUltimoAit(int nrUltimoAit) {
    	talonarioOld.setNrUltimoAit(nrUltimoAit);
    	return this;
    }
	
	public TalonarioOld build() {
		return this.talonarioOld;
	}
}
