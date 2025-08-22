package com.tivic.manager.adapter.base.antiga.tipoveiculo;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.fta.TipoVeiculo;

public class AdapterTipoVeiculo {
    
    public TipoVeiculoOld toBaseAntiga(TipoVeiculo tipoVeiculo) {
        return new TipoVeiculoOldBuilder()
                .setCodTipo(tipoVeiculo.getCdTipoVeiculo())
                .setNmTipo(tipoVeiculo.getNmTipoVeiculo()) 
                .build();
    }
    
    public TipoVeiculo toBaseNova(TipoVeiculoOld tipoVeiculoOld) 
            throws IOException, BadLocationException {
        return new TipoVeiculoBuilder()
                .setCdTipoVeiculo(tipoVeiculoOld.getCodTipo()) 
                .setNmTipoVeiculo(tipoVeiculoOld.getNmTipo()) 
                .build();
    }
}
