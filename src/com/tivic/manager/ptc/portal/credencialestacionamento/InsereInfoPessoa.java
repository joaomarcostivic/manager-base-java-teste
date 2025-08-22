package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.util.List;

import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.sol.connection.CustomConnection;

public class InsereInfoPessoa {
	
	public PessoaFisica inserir(CartaoEstacionamentoRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		List<PessoaFisica> pessoaslist = new VerificaPessoaFisica().verificar(documentoRecurso, customConnection);
		if(pessoaslist.isEmpty()) {	
			PessoaFisica pessoaFisica = new InserePessoaFisica().inserir(documentoRecurso, customConnection);
			new InserePessoaEndereco().inserir(pessoaFisica.getCdPessoa(), documentoRecurso, customConnection);
			return pessoaFisica;
		} else {			
			return pessoaslist.get(0);
		}
	}
}
