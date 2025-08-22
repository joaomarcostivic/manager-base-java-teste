package com.tivic.manager.adapter.base.antiga.marcamodelo;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.fta.MarcaModelo;

public class AdapterMarcaModelo {
    
    public MarcaModeloOld toBaseAntiga(MarcaModelo marcaModelo) {
        return new MarcaModeloOldBuilder()
                .setCodMarca(marcaModelo.getCdMarca())
                .setNmMarca(marcaModelo.getNmMarca()) 
                .setNmModelo(marcaModelo.getNmModelo()) 
                .setDtAtualizacao(marcaModelo.getDtAtualizacao()) 
                .build();
    }
    
    public MarcaModelo toBaseNova(MarcaModeloOld marcaModeloOld) 
            throws IOException, BadLocationException {
        return new MarcaModeloBuilder()
                .setCdMarca(marcaModeloOld.getCodMarca()) 
                .setNmMarca(marcaModeloOld.getNmMarca()) 
                .setNmModelo(marcaModeloOld.getNmModelo()) 
                .setDtAtualizacao(marcaModeloOld.getDtAtualizacao()) 
                .build();
    }
}
