package com.tivic.manager.mob.lotes.impressao.pix.builders;

import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoEnvio;

public class DadosAutenticacaoEnvioBuilder {
	private DadosAutenticacaoEnvio dadosEnvio;

    public DadosAutenticacaoEnvioBuilder() {
        dadosEnvio = new DadosAutenticacaoEnvio();
    }
    
    public DadosAutenticacaoEnvioBuilder addEmail(String email) {
        dadosEnvio.setEmail(email);
        return this;
    }

    public DadosAutenticacaoEnvioBuilder addSenha(String senha) {
    	dadosEnvio.setSenha(senha);
        return this;
    }

    public DadosAutenticacaoEnvio build() {
        return dadosEnvio;
    }
}
