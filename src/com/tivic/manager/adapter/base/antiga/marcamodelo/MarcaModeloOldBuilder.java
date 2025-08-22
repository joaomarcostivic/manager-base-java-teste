package com.tivic.manager.adapter.base.antiga.marcamodelo;

import java.util.GregorianCalendar;

public class MarcaModeloOldBuilder {
    private MarcaModeloOld marcaModeloOld;

    public MarcaModeloOldBuilder() {
        this.marcaModeloOld = new MarcaModeloOld();
    }

    public MarcaModeloOldBuilder setCodMarca(int codMarca) {
        marcaModeloOld.setCodMarca(codMarca);
        return this;
    }

    public MarcaModeloOldBuilder setNmMarca(String nmMarca) {
        marcaModeloOld.setNmMarca(nmMarca);
        return this;
    }

    public MarcaModeloOldBuilder setNmModelo(String nmModelo) {
        marcaModeloOld.setNmModelo(nmModelo);
        return this;
    }

    public MarcaModeloOldBuilder setDtAtualizacao(GregorianCalendar dtAtualizacao) {
        marcaModeloOld.setDtAtualizacao(dtAtualizacao);
        return this;
    }

    public MarcaModeloOld build() {
        return this.marcaModeloOld;
    }
}
