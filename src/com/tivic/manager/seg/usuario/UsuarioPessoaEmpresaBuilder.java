package com.tivic.manager.seg.usuario;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.PessoaEmpresa;

public class UsuarioPessoaEmpresaBuilder {

	private PessoaEmpresa pessoaEmpresa;
	
	public UsuarioPessoaEmpresaBuilder() {
		this.pessoaEmpresa = new PessoaEmpresa();
	}
	
	public UsuarioPessoaEmpresaBuilder pessoa(int cdPessoa) {
		this.pessoaEmpresa.setCdPessoa(cdPessoa);
		return this;
	}

	public UsuarioPessoaEmpresaBuilder vinculo(int cdVinculo) {
		this.pessoaEmpresa.setCdVinculo(cdVinculo);
		return this;
	}

	public UsuarioPessoaEmpresaBuilder situacaoVinculo(int stVinculo) {
		this.pessoaEmpresa.setStVinculo(stVinculo);
		return this;
	}

	public UsuarioPessoaEmpresaBuilder dataVinculo(GregorianCalendar dtVinculo) {
		this.pessoaEmpresa.setDtVinculo(dtVinculo);
		return this;
	}
	
	public UsuarioPessoaEmpresaBuilder empresa() {
		Empresa _empresa = EmpresaServices.getEmpresaById("JARI");
		this.pessoaEmpresa.setCdEmpresa(_empresa.getCdEmpresa());
		return this;
	}
	
	public PessoaEmpresa build(){
		return this.pessoaEmpresa;
	}
}
