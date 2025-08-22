package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.grl.IPessoaEnderecoRepository;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.pessoaendereco.PessoaEnderecoBuilder;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InserePessoaEndereco {
	
	private IPessoaEnderecoRepository pessoaEnderecoRepositoryDAO;
	
	public InserePessoaEndereco() throws Exception {
		pessoaEnderecoRepositoryDAO = (IPessoaEnderecoRepository) BeansFactory.get(IPessoaEnderecoRepository.class);
	}
	
	public void inserir(int cdPessoa, CartaoEstacionamentoRequest documentoCartaoIdoso, CustomConnection customConnection) throws Exception {
			PessoaEndereco pessoaEndereco = new PessoaEnderecoBuilder()
					.setCdPessoa(cdPessoa)
					.setNmLogradouro(documentoCartaoIdoso.getNmLogradouroRequerente())
					.setNmBairro(documentoCartaoIdoso.getNmBairroRequerente())
					.setNrEndereco(documentoCartaoIdoso.getNrEnderecoRequerente())
					.setNmComplemento(documentoCartaoIdoso.getNmComplementoRequerente())
					.setNrCep(documentoCartaoIdoso.getNrCepRequerente())
					.build();
			pessoaEnderecoRepositoryDAO.insertCodeSync(pessoaEndereco, customConnection);
	}
}
