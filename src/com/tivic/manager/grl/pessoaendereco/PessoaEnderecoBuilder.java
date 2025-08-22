package com.tivic.manager.grl.pessoaendereco;

import com.tivic.manager.grl.PessoaEndereco;

public class PessoaEnderecoBuilder {
	private PessoaEndereco pessoaEndereco;

	public PessoaEnderecoBuilder(){
		pessoaEndereco = new PessoaEndereco();
	}
	
	public PessoaEnderecoBuilder setCdEndereco(int cdEndereco) {
        pessoaEndereco.setCdEndereco(cdEndereco);
        return this;
    }

    public PessoaEnderecoBuilder setCdPessoa(int cdPessoa) {
        pessoaEndereco.setCdPessoa(cdPessoa);
        return this;
    }

    public PessoaEnderecoBuilder setDsEndereco(String dsEndereco) {
        pessoaEndereco.setDsEndereco(dsEndereco);
        return this;
    }

    public PessoaEnderecoBuilder setCdTipoLogradouro(int cdTipoLogradouro) {
        pessoaEndereco.setCdTipoLogradouro(cdTipoLogradouro);
        return this;
    }

    public PessoaEnderecoBuilder setCdTipoEndereco(int cdTipoEndereco) {
        pessoaEndereco.setCdTipoEndereco(cdTipoEndereco);
        return this;
    }

    public PessoaEnderecoBuilder setCdLogradouro(int cdLogradouro) {
        pessoaEndereco.setCdLogradouro(cdLogradouro);
        return this;
    }

    public PessoaEnderecoBuilder setCdBairro(int cdBairro) {
        pessoaEndereco.setCdBairro(cdBairro);
        return this;
    }

    public PessoaEnderecoBuilder setCdCidade(int cdCidade) {
        pessoaEndereco.setCdCidade(cdCidade);
        return this;
    }

    public PessoaEnderecoBuilder setNmLogradouro(String nmLogradouro) {
        pessoaEndereco.setNmLogradouro(nmLogradouro);
        return this;
    }

    public PessoaEnderecoBuilder setNmBairro(String nmBairro) {
        pessoaEndereco.setNmBairro(nmBairro);
        return this;
    }

    public PessoaEnderecoBuilder setNrCep(String nrCep) {
        pessoaEndereco.setNrCep(nrCep);
        return this;
    }

    public PessoaEnderecoBuilder setNrEndereco(String nrEndereco) {
        pessoaEndereco.setNrEndereco(nrEndereco);
        return this;
    }

    public PessoaEnderecoBuilder setNmComplemento(String nmComplemento) {
        pessoaEndereco.setNmComplemento(nmComplemento);
        return this;
    }

    public PessoaEnderecoBuilder setNrTelefone(String nrTelefone) {
        pessoaEndereco.setNrTelefone(nrTelefone);
        return this;
    }

    public PessoaEnderecoBuilder setNmPontoReferencia(String nmPontoReferencia) {
        pessoaEndereco.setNmPontoReferencia(nmPontoReferencia);
        return this;
    }

    public PessoaEnderecoBuilder setLgCobranca(int lgCobranca) {
        pessoaEndereco.setLgCobranca(lgCobranca);
        return this;
    }

    public PessoaEnderecoBuilder setLgPrincipal(int lgPrincipal) {
        pessoaEndereco.setLgPrincipal(lgPrincipal);
        return this;
    }

    public PessoaEnderecoBuilder setTpZona(int tpZona) {
        pessoaEndereco.setTpZona(tpZona);
        return this;
    }

    public PessoaEnderecoBuilder setTpLocalizacaoDiferenciada(int tpLocalizacaoDiferenciada) {
        pessoaEndereco.setTpLocalizacaoDiferenciada(tpLocalizacaoDiferenciada);
        return this;
    }
	
	public PessoaEndereco build() {
		return pessoaEndereco;
	}
}
