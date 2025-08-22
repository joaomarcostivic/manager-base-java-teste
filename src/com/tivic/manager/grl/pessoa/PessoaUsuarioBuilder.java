package com.tivic.manager.grl.pessoa;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.seg.UsuarioPessoaDTO;
import com.tivic.manager.util.Util;

public class PessoaUsuarioBuilder {

	private Pessoa pessoa;
	
	public PessoaUsuarioBuilder(UsuarioPessoaDTO usuarioPessoaDto) {
		pessoa = new Pessoa();
		pessoa.setStCadastro(PessoaServices.ST_ATIVO);
		pessoa.setDtCadastro(Util.getDataAtual());
		pessoa.setGnPessoa(PessoaServices.TP_FISICA);
		pessoa.setNmPessoa(usuarioPessoaDto.getPessoa().getNmPessoa());
		pessoa.setNmEmail(usuarioPessoaDto.getPessoa().getNmEmail());	
		pessoa.setNmApelido(usuarioPessoaDto.getPessoa().getNmApelido());
		pessoa.setNrCelular(usuarioPessoaDto.getPessoa().getNrCelular());
	}
	
	public PessoaUsuarioBuilder codigo(int cdPessoa) {
		this.pessoa.setCdPessoa(cdPessoa);
		return this;
	}
	
	public Pessoa build() {
		return this.pessoa;
	}
	
}
