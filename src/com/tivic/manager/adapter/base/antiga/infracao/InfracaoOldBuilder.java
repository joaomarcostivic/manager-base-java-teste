package com.tivic.manager.adapter.base.antiga.infracao;

import java.util.GregorianCalendar;

public class InfracaoOldBuilder {
	private InfracaoOld infracaoOld;
	
    public InfracaoOldBuilder() {
    	this.infracaoOld = new InfracaoOld();
    }

    public InfracaoOldBuilder setCodInfracao(int codInfracao) {
    	infracaoOld.setCodInfracao(codInfracao);
        return this;
    }

    public InfracaoOldBuilder setDsInfracao(String dsInfracao) {
    	infracaoOld.setDsInfracao(dsInfracao);
        return this;
    }
    
    public InfracaoOldBuilder setDsInfracao2(String dsInfracao2) {
    	infracaoOld.setDsInfracao2(dsInfracao2);
        return this;
    }

    public InfracaoOldBuilder setNrPontuacao(int nrPontuacao) {
    	infracaoOld.setNrPontuacao(nrPontuacao);
        return this;
    }
    
    public InfracaoOldBuilder setNrCodDetran(int nrCodDetran) {
    	infracaoOld.setNrCodDetran(nrCodDetran);
        return this;
    }
    
    public InfracaoOldBuilder setNrValorUfir(Double nrValorUfir) {
    	infracaoOld.setNrValorUfir(nrValorUfir);
		return this;
	}
    
    public InfracaoOldBuilder setNmNatureza(String nmNatureza) {
    	infracaoOld.setNmNatureza(nmNatureza);
        return this;
    }

    public InfracaoOldBuilder setNrArtigo(String nrArtigo) {
    	infracaoOld.setNrArtigo(nrArtigo);
        return this;
    }

    public InfracaoOldBuilder setNrParagrafo(String nrParagrafo) {
    	infracaoOld.setNrParagrafo(nrParagrafo);
        return this;
    }

    public InfracaoOldBuilder setNrInciso(String nrInciso) {
    	infracaoOld.setNrInciso(nrInciso);
        return this;
    }
    
    public InfracaoOldBuilder setNrAlinea(String nrAlinea) {
    	infracaoOld.setNrAlinea(nrAlinea);
        return this;
    }
    
    public InfracaoOldBuilder setTpCompetencia(int tpCompetencia) {
    	infracaoOld.setTpCompetencia(tpCompetencia);
        return this;
    }
    
    public InfracaoOldBuilder setLgPrioritaria(int lgPrioritaria) {
    	infracaoOld.setLgPrioritaria(lgPrioritaria);
        return this;
    }
    
    public InfracaoOldBuilder setDtFimVigencia(GregorianCalendar dtFimVigencia) {
    	infracaoOld.setDtFimVigencia(dtFimVigencia);
        return this;
    }
    
    public InfracaoOldBuilder setVlInfracao(Double vlInfracao) {
    	infracaoOld.setVlInfracao(vlInfracao);
        return this;
    }
    
    public InfracaoOldBuilder setLgSuspensaoCnh(int lgSuspensaoCnh) {
    	infracaoOld.setLgSuspensaoCnh(lgSuspensaoCnh);
        return this;
    }
    
    public InfracaoOldBuilder setTpResponsabilidade(int tpResponsabilidade) {
    	infracaoOld.setTpResponsabilidade(tpResponsabilidade);
        return this;
    }
	
	public InfracaoOld build() {
		return this.infracaoOld;
	}
}
