package com.tivic.manager.mob.infracao;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Infracao;

public class InfracaoBuilder {

    private Infracao infracao;

    public InfracaoBuilder() {
    	infracao = new Infracao();
    }
    
    public InfracaoBuilder(Infracao infracao) {
    	this.infracao = infracao;
    }

    public InfracaoBuilder setCdInfracao(int cdInfracao) {
    	infracao.setCdInfracao(cdInfracao);
        return this;
    }

    public InfracaoBuilder setDsInfracao(String dsInfracao) {
    	infracao.setDsInfracao(dsInfracao);
        return this;
    }

    public InfracaoBuilder setNrPontuacao(int nrPontuacao) {
    	infracao.setNrPontuacao(nrPontuacao);
        return this;
    }
    
    public InfracaoBuilder setNrCodDetran(int nrCodDetran) {
    	infracao.setNrCodDetran(nrCodDetran);
        return this;
    }
    
    public InfracaoBuilder setVlUfir(Double vlUfir) {
    	infracao.setVlUfir(vlUfir);
		return this;
	}
    
    public InfracaoBuilder setNmNatureza(String nmNatureza) {
    	infracao.setNmNatureza(nmNatureza);
        return this;
    }

    public InfracaoBuilder setNrArtigo(String nrArtigo) {
    	infracao.setNrArtigo(nrArtigo);
        return this;
    }

    public InfracaoBuilder setNrParagrafo(String nrParagrafo) {
    	infracao.setNrParagrafo(nrParagrafo);
        return this;
    }

    public InfracaoBuilder setNrInciso(String nrInciso) {
    	infracao.setNrInciso(nrInciso);
        return this;
    }
    
    public InfracaoBuilder setNrAlinea(String nrAlinea) {
    	infracao.setNrAlinea(nrAlinea);
        return this;
    }
    
    public InfracaoBuilder setTpCompetencia(int tpCompetencia) {
    	infracao.setTpCompetencia(tpCompetencia);
        return this;
    }
    
    public InfracaoBuilder setLgPrioritaria(int lgPrioritaria) {
    	infracao.setLgPrioritaria(lgPrioritaria);
        return this;
    }
    
    public InfracaoBuilder setDtFimVigencia(GregorianCalendar dtFimVigencia) {
    	infracao.setDtFimVigencia(dtFimVigencia);
        return this;
    }
    
    public InfracaoBuilder setVlInfracao(Double vlInfracao) {
    	infracao.setVlInfracao(vlInfracao);
        return this;
    }
    
    public InfracaoBuilder setLgSuspensaoCnh(int lgSuspensaoCnh) {
    	infracao.setLgSuspensaoCnh(lgSuspensaoCnh);
        return this;
    }
    
    public InfracaoBuilder setTpResponsabilidade(int tpResponsabilidade) {
    	infracao.setTpResponsabilidade(tpResponsabilidade);
        return this;
    }


    public Infracao build() {
		return infracao;
	}
}