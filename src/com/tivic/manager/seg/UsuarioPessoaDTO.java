package com.tivic.manager.seg;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.Vinculo;

public class UsuarioPessoaDTO {
	
	private Usuario usuario;
	private Pessoa pessoa;
	private PessoaFisica pessoaFisica;
	private Vinculo vinculo;
	private int cdUsuario;
	private int cdPessoa;
	private String nmLogin;
	private String nmPessoa;
	
	
	public UsuarioPessoaDTO() {
		super();
	}
	
	public UsuarioPessoaDTO (Usuario usuario, Pessoa pessoa) {
		super();
		this.usuario = usuario;
		this.pessoa = pessoa;
	}
	
	public UsuarioPessoaDTO (Usuario usuario, Pessoa pessoa, PessoaFisica pessoaFisica) {
		super();
		this.usuario = usuario;
		this.pessoa = pessoa;
		this.pessoaFisica = pessoaFisica;
	}
	
	public UsuarioPessoaDTO (Usuario usuario, Pessoa pessoa, PessoaFisica pessoaFisica, Vinculo vinculo) {
		super();
		this.usuario = usuario;
		this.pessoa = pessoa;
		this.pessoaFisica = pessoaFisica;
		this.vinculo = vinculo;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}
	
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}
	
	public Vinculo getVinculo() {
		return vinculo;
	}
	
	public void setVinculo(Vinculo vinculo) {
		this.vinculo = vinculo;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public int getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
	}

	public String getNmLogin() {
		return nmLogin;
	}

	public void setNmLogin(String nmLogin) {
		this.nmLogin = nmLogin;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

}
