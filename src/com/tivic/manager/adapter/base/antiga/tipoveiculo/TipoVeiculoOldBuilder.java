package com.tivic.manager.adapter.base.antiga.tipoveiculo;

public class TipoVeiculoOldBuilder {
    private TipoVeiculoOld tipoVeiculoOld;

    public TipoVeiculoOldBuilder() {
        this.tipoVeiculoOld = new TipoVeiculoOld();
    }

    public TipoVeiculoOldBuilder setCodTipo(int codTipo) {
        tipoVeiculoOld.setCodTipo(codTipo);
        return this;
    }

    public TipoVeiculoOldBuilder setNmTipo(String nmTipo) {
        tipoVeiculoOld.setNmTipo(nmTipo);
        return this;
    }

    public TipoVeiculoOld build() {
        return this.tipoVeiculoOld;
    }
}
