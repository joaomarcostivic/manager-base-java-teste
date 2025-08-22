package com.tivic.manager.adapter.base.antiga.infracaoobservacao;

import com.tivic.manager.mob.InfracaoObservacao;

public class InfracaoObservacaoBuilder {
    private InfracaoObservacao infracaoObservacao;

    public InfracaoObservacaoBuilder() {
        this.infracaoObservacao = new InfracaoObservacao();
    }

    public InfracaoObservacaoBuilder setCdInfracao(int cdInfracao) {
        infracaoObservacao.setCdInfracao(cdInfracao);
        return this;
    }

    public InfracaoObservacaoBuilder setCdObservacao(int cdObservacao) {
        infracaoObservacao.setCdObservacao(cdObservacao);
        return this;
    }

    public InfracaoObservacaoBuilder setNrObservacao(String nrObservacao) {
        infracaoObservacao.setNrObservacao(nrObservacao);
        return this;
    }

    public InfracaoObservacaoBuilder setNmObservacao(String nmObservacao) {
        infracaoObservacao.setNmObservacao(nmObservacao);
        return this;
    }

    public InfracaoObservacaoBuilder setTxtObservacao(String txtObservacao) {
        infracaoObservacao.setTxtObservacao(txtObservacao);
        return this;
    }

    public InfracaoObservacaoBuilder setStObservacao(int stObservacao) {
        infracaoObservacao.setStObservacao(stObservacao);
        return this;
    }

    public InfracaoObservacao build() {
        return this.infracaoObservacao;
    }
}
