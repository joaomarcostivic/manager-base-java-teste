package com.tivic.manager.mob.lotes.builders.publicacao;

import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;

public class LotePublicacaoAitBuilder {

    private LotePublicacaoAit lotePublicacaoAit;

    public LotePublicacaoAitBuilder() {
        lotePublicacaoAit = new LotePublicacaoAit();
    }

    public LotePublicacaoAitBuilder setCdLotePublicacao(int cdLotePublicacao) {
        lotePublicacaoAit.setCdLotePublicacao(cdLotePublicacao);
        return this;
    }

    public LotePublicacaoAitBuilder setCdAit(int cdAit) {
        lotePublicacaoAit.setCdAit(cdAit);
        return this;
    }

    public LotePublicacaoAitBuilder setTxtErro(String txtErro) {
        lotePublicacaoAit.setTxtErro(txtErro);
        return this;
    }

    public LotePublicacaoAit build() {
        return lotePublicacaoAit;
    }
}
