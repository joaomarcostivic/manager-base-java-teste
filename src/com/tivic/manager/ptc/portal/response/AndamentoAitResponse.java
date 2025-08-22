package com.tivic.manager.ptc.portal.response;

public class AndamentoAitResponse {
    private byte[] impressao;

    public AndamentoAitResponse(byte[] impressao) {
        this.impressao = impressao;
    }

    public byte[] getImpressao() {
        return impressao;
    }

    public void setImpressao(byte[] impressao) {
        this.impressao = impressao;
    }
}
