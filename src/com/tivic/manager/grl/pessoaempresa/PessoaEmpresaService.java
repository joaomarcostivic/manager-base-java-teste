package com.tivic.manager.grl.pessoaempresa;

import java.util.List;

import com.tivic.manager.grl.IPessoaEmpresaRepository;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class PessoaEmpresaService implements IPessoaEmpresaService {

	private IPessoaEmpresaRepository pessoaEmpresaRepository;
	
	public PessoaEmpresaService() throws Exception{
		this.pessoaEmpresaRepository = (IPessoaEmpresaRepository) BeansFactory.get(IPessoaEmpresaRepository.class);
	}
	
	public PessoaEmpresa insert(PessoaEmpresa pessoaEmpresa) throws Exception{
		return insert(pessoaEmpresa, new CustomConnection());
	}
	
	public PessoaEmpresa insert(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			this.pessoaEmpresaRepository.insert(pessoaEmpresa, customConnection);
			customConnection.finishConnection();
			return pessoaEmpresa;
		} finally {
			customConnection.closeConnection();
		}
	}

	public PessoaEmpresa update(PessoaEmpresa pessoaEmpresa) throws Exception{
		return update(pessoaEmpresa, new CustomConnection());
	}
	
	public PessoaEmpresa update(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			this.pessoaEmpresaRepository.update(pessoaEmpresa, customConnection);
			customConnection.finishConnection();
			return pessoaEmpresa;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PessoaEmpresa get(int cdEmpresa, int cdPessoa, int cdVinculo) throws Exception {
		return get(cdEmpresa, cdPessoa, cdVinculo, new CustomConnection());
	}

	@Override
	public PessoaEmpresa get(int cdEmpresa, int cdPessoa, int cdVinculo, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			PessoaEmpresa pessoaEmpresa = this.pessoaEmpresaRepository.get(cdEmpresa, cdPessoa, cdVinculo, customConnection);
			customConnection.finishConnection();
			return pessoaEmpresa;
		} finally {
			customConnection.closeConnection();
		}
	}

	public List<PessoaEmpresa> find(SearchCriterios searchCriterios) throws Exception{
		return find(searchCriterios, new CustomConnection());
	}
	
	public List<PessoaEmpresa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(false);
			List<PessoaEmpresa> listPessoaEmpresa = this.pessoaEmpresaRepository.find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return listPessoaEmpresa;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PessoaEmpresa delete(int cdEmpresa, int cdPessoa, int cdVinculo) throws Exception {
		return delete(cdEmpresa, cdPessoa, cdVinculo, new CustomConnection());
	}

	@Override
	public PessoaEmpresa delete(int cdEmpresa, int cdPessoa, int cdVinculo, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			PessoaEmpresa pessoaEmpresa = get(cdEmpresa, cdPessoa, cdVinculo, customConnection);
			this.pessoaEmpresaRepository.delete(pessoaEmpresa, customConnection);
			customConnection.finishConnection();
			return pessoaEmpresa;
		} finally {
			customConnection.closeConnection();
		}
	}

}
