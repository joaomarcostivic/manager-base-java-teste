package com.tivic.manager.grl.cidade;

import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface CidadeRepository  {
    public Cidade insert(Cidade cidade, CustomConnection customConnection) throws Exception;
    public Cidade update(Cidade cidade, CustomConnection customConnection) throws Exception;
    public Cidade get(int cdCidade) throws Exception;
    public Cidade get(int cdCidade, CustomConnection customConnection) throws Exception;
    public List<Cidade> getAll() throws Exception;
    public List<Cidade> getAll(CustomConnection customConnection) throws Exception;
    public List<Cidade> find(SearchCriterios searchCriterios) throws Exception;
    public List<Cidade> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
