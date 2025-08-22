package com.tivic.manager.seg.usuario;

import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.PessoaEmpresaServices;
import com.tivic.sol.util.date.DateUtil;

public class UsuarioPessoaEmpresaDirector {

	public PessoaEmpresa criar(int cdPessoa, int cdVinculo) {
		return new UsuarioPessoaEmpresaBuilder()
			.dataVinculo(DateUtil.getDataAtual())
			.situacaoVinculo(PessoaEmpresaServices.ST_ATIVO)
			.pessoa(cdPessoa)
			.vinculo(cdVinculo)
			.empresa()
		.build();
			
	}
	
}
