package com.tivic.manager.grl.pessoafisica;

import java.util.List;

import com.tivic.manager.grl.IPessoaFisicaRepository;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class PessoaFisicaService implements IPessoaFisicaService {

	private IPessoaFisicaRepository pessoaFisicaRepository;
	
	public PessoaFisicaService() throws Exception{
		this.pessoaFisicaRepository = (IPessoaFisicaRepository) BeansFactory.get(IPessoaFisicaRepository.class);
	}
	
	public PessoaFisica insert(PessoaFisica pessoaFisica) throws Exception{
		return insert(pessoaFisica, new CustomConnection());
	}
	
	public PessoaFisica insert(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception{
		this.pessoaFisicaRepository.insert(pessoaFisica, customConnection);
		return pessoaFisica;
	}

	public PessoaFisica update(PessoaFisica pessoaFisica) throws Exception{
		return update(pessoaFisica, new CustomConnection());
	}
	
	public PessoaFisica update(PessoaFisica pessoaFisica, CustomConnection customConnection) throws Exception{
		this.pessoaFisicaRepository.update(pessoaFisica, customConnection);
		return pessoaFisica;
	}

	public PessoaFisica get(int cdPessoa) throws Exception{
		return get(cdPessoa, new CustomConnection());
	}
	
	public PessoaFisica get(int cdPessoa, CustomConnection customConnection) throws Exception{
		return this.pessoaFisicaRepository.get(cdPessoa, customConnection);
	}

	public List<PessoaFisica> find(SearchCriterios searchCriterios) throws Exception{
		return find(searchCriterios, new CustomConnection());
	}
	
	public List<PessoaFisica> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception{
		return this.pessoaFisicaRepository.find(searchCriterios, customConnection);
	}

	@Override
	public PessoaFisica getPessoaFisica(int cdPessoaFisica) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			PessoaFisica pessoaFisica = get(cdPessoaFisica, customConnection);
			customConnection.finishConnection();
			return pessoaFisica;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PessoaFisica getPessoaFisica(int cdPessoaFisica, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_Pessoa", cdPessoaFisica);
		PessoaFisica pessoaFisica = new PessoaFisica();
		Search<PessoaFisica> search = new SearchBuilder<PessoaFisica>("grl_pessoa_fisica")
				.customConnection(customConnection).searchCriterios(searchCriterios).build();
		if(!search.getList(PessoaFisica.class).isEmpty()) {
			pessoaFisica = search.getList(PessoaFisica.class).get(0);
		}
		return pessoaFisica;
	}
}
