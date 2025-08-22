package com.tivic.manager.adapter.base.antiga.marcamodelo;

import com.tivic.manager.fta.MarcaModelo;
import java.util.GregorianCalendar;

public class MarcaModeloBuilder {
    private MarcaModelo marcaModelo;

    public MarcaModeloBuilder() {
        this.marcaModelo = new MarcaModelo();
    }

    public MarcaModeloBuilder setCdMarca(int cdMarca) {
        marcaModelo.setCdMarca(cdMarca);
        return this;
    }

    public MarcaModeloBuilder setNmMarca(String nmMarca) {
        marcaModelo.setNmMarca(nmMarca);
        return this;
    }

    public MarcaModeloBuilder setNmModelo(String nmModelo) {
        marcaModelo.setNmModelo(nmModelo);
        return this;
    }

    public MarcaModeloBuilder setTpMarca(int tpMarca) {
        marcaModelo.setTpMarca(tpMarca);
        return this;
    }

    public MarcaModeloBuilder setDtAtualizacao(GregorianCalendar dtAtualizacao) {
        marcaModelo.setDtAtualizacao(dtAtualizacao);
        return this;
    }

    public MarcaModeloBuilder setNrMarca(String nrMarca) {
        marcaModelo.setNrMarca(nrMarca);
        return this;
    }

    public MarcaModelo build() {
        return this.marcaModelo;
    }
}
