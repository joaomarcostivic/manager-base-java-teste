package com.tivic.manager.grl;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaEnderecoRepository {
	public PessoaEndereco insert(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception;
	public PessoaEndereco update(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception;
	public PessoaEndereco delete(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception;
	public PessoaEndereco get(int cdPessoaEndereco) throws Exception;
	public PessoaEndereco get(int cdPessoaEndereco, CustomConnection customConnection) throws Exception;
	public List<PessoaEndereco> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;
	public void insertCodeSync(PessoaEndereco pessoaEndereco, CustomConnection customConnection) throws Exception;
}