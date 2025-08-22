package com.tivic.manager.mob.colaborador;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.pessoavinculohistorico.enums.StPessoaVinculoEnum;

public class ColaboradorEmpresaBuilder {
	private PessoaEmpresa _pessoaEmpresa = new PessoaEmpresa();

	public ColaboradorEmpresaBuilder(int cdPessoa, int cdVinculo) {
		Empresa _empresa = EmpresaServices.getEmpresaById("JARI");
		_pessoaEmpresa.setCdVinculo(cdVinculo);
		_pessoaEmpresa.setCdPessoa(cdPessoa);
		_pessoaEmpresa.setCdEmpresa(_empresa.getCdEmpresa());
		_pessoaEmpresa.setDtVinculo(new GregorianCalendar());
		_pessoaEmpresa.setStVinculo(StPessoaVinculoEnum.ST_ATIVO.getKey());
	}
	
	public PessoaEmpresa build() {
		return this._pessoaEmpresa;
	}
	
}
