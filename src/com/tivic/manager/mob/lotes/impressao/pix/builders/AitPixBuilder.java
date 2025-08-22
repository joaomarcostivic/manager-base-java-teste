package com.tivic.manager.mob.lotes.impressao.pix.builders;

import com.tivic.manager.mob.lotes.impressao.pix.model.AitPix;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import java.util.GregorianCalendar;

public class AitPixBuilder {

    private AitPix aitPix;

    public AitPixBuilder() {
        this.aitPix = new AitPix();
    }

    public AitPixBuilder setCdPix(int cdPix) {
        aitPix.setCdPix(cdPix);
        return this;
    }

    public AitPixBuilder setCdAit(int cdAit) throws ValidacaoException {
    	if (cdAit <= 0) {
            throw new ValidacaoException("O cdAit não foi fornecido.");
        }
        aitPix.setCdAit(cdAit);
        return this;
    }

    public AitPixBuilder setCdCobranca(int cdCobranca) {
        aitPix.setCdCobranca(cdCobranca);
        return this;
    }

    public AitPixBuilder setTxtTxid(String txtTxid) {
        aitPix.setTxtTxid(txtTxid);
        return this;
    }

    public AitPixBuilder setStCobranca(int stCobranca) {
        aitPix.setStCobranca(stCobranca);
        return this;
    }

    public AitPixBuilder setDtCriacao(GregorianCalendar dtCriacao) {
        aitPix.setDtCriacao(dtCriacao);
        return this;
    }

    public AitPixBuilder setDtVencimento(GregorianCalendar dtVencimento) {
        aitPix.setDtVencimento(dtVencimento);
        return this;
    }

    public AitPixBuilder setVlCobranca(Double vlCobranca) {
        aitPix.setVlCobranca(vlCobranca);
        return this;
    }

    public AitPixBuilder setTxtObservacao(String txtObservacao) {
        aitPix.setTxtObservacao(txtObservacao);
        return this;
    }

    public AitPixBuilder setTpCobranca(int tpCobranca) {
        aitPix.setTpCobranca(tpCobranca);
        return this;
    }

    public AitPixBuilder setNrCodigoGuiaRecebimento(String nrCodigoGuiaRecebimento) {
        aitPix.setNrCodigoGuiaRecebimento(nrCodigoGuiaRecebimento);
        return this;
    }

    public AitPixBuilder setNmDevedor(String nmDevedor) {
        aitPix.setNmDevedor(nmDevedor);
        return this;
    }

    public AitPixBuilder setNrCpfCnpj(String nrCpfCnpj) {
        aitPix.setNrCpfCnpj(nrCpfCnpj);
        return this;
    }

    public AitPixBuilder setDsQrCode(String dsQrCode) throws ValidacaoException {
    	if (dsQrCode == null || dsQrCode.trim().isEmpty()) {
            throw new ValidacaoException("O dsQrCode não pode ser nulo ou vazio.");
        }
        aitPix.setDsQrCode(dsQrCode);
        return this;
    }

    public AitPix build() {
        return aitPix;
    }
}
