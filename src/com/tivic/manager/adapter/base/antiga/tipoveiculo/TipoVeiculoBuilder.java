package com.tivic.manager.adapter.base.antiga.tipoveiculo;

import com.tivic.manager.fta.TipoVeiculo;

public class TipoVeiculoBuilder {
    private TipoVeiculo tipoVeiculo;

    public TipoVeiculoBuilder() {
        this.tipoVeiculo = new TipoVeiculo();
    }

    public TipoVeiculoBuilder setCdTipoVeiculo(int cdTipoVeiculo) {
        tipoVeiculo.setCdTipoVeiculo(cdTipoVeiculo);
        return this;
    }

    public TipoVeiculoBuilder setNmTipoVeiculo(String nmTipoVeiculo) {
        tipoVeiculo.setNmTipoVeiculo(nmTipoVeiculo);
        return this;
    }

    public TipoVeiculo build() {
        return this.tipoVeiculo;
    }
}
