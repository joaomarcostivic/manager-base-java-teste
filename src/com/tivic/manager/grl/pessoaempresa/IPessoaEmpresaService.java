package com.tivic.manager.grl.pessoaempresa;

import java.util.List;

import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaEmpresaService {
	public PessoaEmpresa insert(PessoaEmpresa pessoaEmpresa) throws Exception;
	public PessoaEmpresa insert(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception;
	public PessoaEmpresa update(PessoaEmpresa pessoaEmpresa) throws Exception;
	public PessoaEmpresa update(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception;
	public PessoaEmpresa get(int cdEmpresa, int cdPessoa, int cdVinculo) throws Exception;
	public PessoaEmpresa get(int cdEmpresa, int cdPessoa, int cdVinculo, CustomConnection customConnection) throws Exception;
	public List<PessoaEmpresa> find(SearchCriterios searchCriterios) throws Exception;
	public List<PessoaEmpresa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public PessoaEmpresa delete(int cdEmpresa, int cdPessoa, int cdVinculo) throws Exception;
	public PessoaEmpresa delete(int cdEmpresa, int cdPessoa, int cdVinculo, CustomConnection customConnection) throws Exception;
}
