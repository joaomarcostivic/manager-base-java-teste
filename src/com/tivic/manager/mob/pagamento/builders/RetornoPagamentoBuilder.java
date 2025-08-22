package com.tivic.manager.mob.pagamento.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.RetornoBancoDTO;

public class RetornoPagamentoBuilder {

    private RetornoBancoDTO retornoBancoDTO;

    public RetornoPagamentoBuilder() {
        retornoBancoDTO = new RetornoBancoDTO();
    }

    public RetornoPagamentoBuilder setStImportacao(String stImportacao) {
        retornoBancoDTO.setStImportacao(stImportacao);
        return this;
    }

    public RetornoPagamentoBuilder setNmBanco(String nmBanco) {
        retornoBancoDTO.setNmBanco(nmBanco);
        return this;
    }

    public RetornoPagamentoBuilder setDtPagamento(GregorianCalendar dtPagamento) {
        retornoBancoDTO.setDtPagamento(dtPagamento);
        return this;
    }

    public RetornoPagamentoBuilder setIdAit(String idAit) {
        retornoBancoDTO.setIdAit(idAit);
        return this;
    }

    public RetornoPagamentoBuilder setNrControle(String nrControle) {
        retornoBancoDTO.setNrControle(nrControle);
        return this;
    }

    public RetornoPagamentoBuilder setVlPago(double vlPago) {
        retornoBancoDTO.setVlPago(vlPago);
        return this;
    }

    public RetornoBancoDTO build() {
        return retornoBancoDTO;
    }

}

