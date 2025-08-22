package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.IPessoaFisicaRepository;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.pessoafisica.PessoaFisicaBuilder;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InserePessoaFisica {
	
	private IPessoaFisicaRepository pessoaFisicaRepositoryDAO;


	public InserePessoaFisica() throws Exception {
		pessoaFisicaRepositoryDAO = (IPessoaFisicaRepository) BeansFactory.get(IPessoaFisicaRepository.class);
	}
	
	public PessoaFisica inserir(CartaoEstacionamentoRequest documentoCartaoIdoso, CustomConnection customConnection) throws Exception {
		PessoaFisica pessoaFisica = new PessoaFisicaBuilder()
				.setNmPessoa(documentoCartaoIdoso.getNmRequerente())
				.setNrCpf(documentoCartaoIdoso.getNrCpfRequerente())
				.setNrTelefone1(documentoCartaoIdoso.getNrTelefoneRequerente())
				.setNrCelular(documentoCartaoIdoso.getNrCelularRequerente())
				.setNmEmail(documentoCartaoIdoso.getNmEmail())
				.setDtCadastro(new GregorianCalendar())
				.build();
		pessoaFisicaRepositoryDAO.insertCodeSync(pessoaFisica, customConnection);

		return pessoaFisica;
	}
}
