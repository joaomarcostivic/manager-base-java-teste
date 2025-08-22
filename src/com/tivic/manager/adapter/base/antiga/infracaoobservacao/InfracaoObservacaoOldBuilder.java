package com.tivic.manager.adapter.base.antiga.infracaoobservacao;

public class InfracaoObservacaoOldBuilder {
    private InfracaoObservacaoOld infracaoObservacaoOld;

    public InfracaoObservacaoOldBuilder() {
        this.infracaoObservacaoOld = new InfracaoObservacaoOld();
    }

    public InfracaoObservacaoOldBuilder setCodInfracao(int codInfracao) {
        infracaoObservacaoOld.setCodInfracao(codInfracao);
        return this;
    }

    public InfracaoObservacaoOldBuilder setCdObservacao(int cdObservacao) {
        infracaoObservacaoOld.setCdObservacao(cdObservacao);
        return this;
    }

    public InfracaoObservacaoOldBuilder setNrObservacao(String nrObservacao) {
        infracaoObservacaoOld.setNrObservacao(nrObservacao);
        return this;
    }

    public InfracaoObservacaoOldBuilder setNmObservacao(String nmObservacao) {
        infracaoObservacaoOld.setNmObservacao(nmObservacao);
        return this;
    }

    public InfracaoObservacaoOldBuilder setStObservacao(int stObservacao) {
        infracaoObservacaoOld.setStObservacao(stObservacao);
        return this;
    }

    public InfracaoObservacaoOldBuilder setTxtObservacao(String txtObservacao) {
        infracaoObservacaoOld.setTxtObservacao(txtObservacao);
        return this;
    }

    public InfracaoObservacaoOldBuilder setTpInfracaoObservacao(int tpInfracaoObservacao) {
        infracaoObservacaoOld.setTpInfracaoObservacao(tpInfracaoObservacao);
        return this;
    }

    public InfracaoObservacaoOld build() {
        return this.infracaoObservacaoOld;
    }
}
