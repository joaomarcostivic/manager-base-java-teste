package com.tivic.manager.mob.lotes.impressao.pix.builders;

import com.tivic.manager.mob.lotes.impressao.pix.DadosArrecadacaoEnvio;

public class DadosArrecadacaoEnvioBuilder {

    private DadosArrecadacaoEnvio dadosEnvio;

    public DadosArrecadacaoEnvioBuilder() {
        dadosEnvio = new DadosArrecadacaoEnvio();
    }
    
    public DadosArrecadacaoEnvioBuilder addUrl(String url) {
        dadosEnvio.setUrl(url);
        return this;
    }

    public DadosArrecadacaoEnvioBuilder addNrChavePix(String nrChavePix) {
        dadosEnvio.setNrChavePix(nrChavePix.trim());
        return this;
    }

    public DadosArrecadacaoEnvioBuilder addDiasVencimento(String diasVencimento) {
        dadosEnvio.setDiasVencimento(diasVencimento);
        return this;
    }

    public DadosArrecadacaoEnvioBuilder addCodigoBarras(String codigoBarras) {
        dadosEnvio.setCodigoBarras(codigoBarras);
        return this;
    }

    public DadosArrecadacaoEnvioBuilder addVlCobranca(Double vlCobranca) {
        dadosEnvio.setVlCobranca(vlCobranca);
        return this;
    }

    public DadosArrecadacaoEnvioBuilder addTxtDescricao(String txtDescricao) {
        if (txtDescricao == null || txtDescricao.trim().equals("0")) {
            dadosEnvio.setTxtDescricao("Arrecadação de multa de trânsito");
        } else {
            dadosEnvio.setTxtDescricao(txtDescricao);
        }
        return this;
    }

    public DadosArrecadacaoEnvioBuilder addNmDevedor(String nmDevedor) {
        dadosEnvio.setNmDevedor(nmDevedor != null ? nmDevedor : null);
        return this;
    }

    public DadosArrecadacaoEnvioBuilder addCpfDevedor(String cpfDevedor) {
    	if (cpfDevedor != null && cpfDevedor.length() == 11) {
		      dadosEnvio.setCpfDevedor(cpfDevedor);
		}
    	return this;
    }
    
    public DadosArrecadacaoEnvioBuilder addCnpjDevedor(String cnpjDevedor) {
    	if (cnpjDevedor != null && cnpjDevedor.length() == 14) {
		      dadosEnvio.setCnpjDevedor(cnpjDevedor);
		}
        return this;
    }

    public DadosArrecadacaoEnvio build() {
        return dadosEnvio;
    }
}