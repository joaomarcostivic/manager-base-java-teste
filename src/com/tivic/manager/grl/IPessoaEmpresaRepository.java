package com.tivic.manager.grl;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaEmpresaRepository {
	public PessoaEmpresa insert(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception;
	public PessoaEmpresa update(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception;
	public PessoaEmpresa delete(PessoaEmpresa pessoaEmpresa, CustomConnection customConnection) throws Exception;
	public PessoaEmpresa get(int cdPessoaEmpresa, int cdPessoa, int cdVinculo) throws Exception;
	public PessoaEmpresa get(int cdPessoaEmpresa, int cdPessoa, int cdVinculo, CustomConnection customConnection) throws Exception;
	public List<PessoaEmpresa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
