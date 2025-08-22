package com.tivic.manager.ptc.portal.credencialestacionamento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.IPessoaFisicaRepository;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class VerificaPessoaFisica {
	private IPessoaFisicaRepository pessoaFisicaRepositoryDAO;

	public VerificaPessoaFisica() throws Exception {
		pessoaFisicaRepositoryDAO = (IPessoaFisicaRepository) BeansFactory.get(IPessoaFisicaRepository.class);
	}
	
	public List<PessoaFisica> verificar(CartaoEstacionamentoRequest documentoCartaoIdoso, CustomConnection customConnection) throws Exception {
		List<PessoaFisica> pessoas = new ArrayList<PessoaFisica>();
		List <PessoaFisica> pessoaFisicaList = getPessoaFisica(documentoCartaoIdoso.getNrCpfRequerente(), customConnection);
		if(pessoaFisicaList.isEmpty()) {
			return pessoas;
		} else {
			return pessoaFisicaList;
		}
	}
	
	private List <PessoaFisica> getPessoaFisica(String nrCpf, CustomConnection customConnection) throws  Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nr_cpf", nrCpf);
		List <PessoaFisica> pessoaFisica = pessoaFisicaRepositoryDAO.find(searchCriterios, customConnection);
		return pessoaFisica;
	}
}
