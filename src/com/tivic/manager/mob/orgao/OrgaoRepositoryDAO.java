package com.tivic.manager.mob.orgao;

import java.util.List;

import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class OrgaoRepositoryDAO implements OrgaoRepository {

	@Override
    public Orgao insert(Orgao orgao, CustomConnection customConnection) throws Exception {
        int cdOrgao = OrgaoDAO.insert(orgao, customConnection.getConnection());
        if(cdOrgao <= 0)
            throw new Exception("Erro ao inserir orgão");
        orgao.setCdOrgao(cdOrgao);
        return orgao;
    }

    @Override
    public Orgao update(Orgao orgao, CustomConnection customConnection) throws Exception {
    	int resultAtualizacao = OrgaoDAO.update(orgao, customConnection.getConnection());
    	if(resultAtualizacao <= 0) 
    		throw new Exception("Erro ao atualizar orgão");
        return orgao;
    }
    
    @Override
	public Orgao get(int cdOrgao) throws Exception {
		return OrgaoDAO.get(cdOrgao);
	}

    @Override
    public Orgao get(int cdOrgao, CustomConnection customConnection) throws Exception {
        return OrgaoDAO.get(cdOrgao, customConnection.getConnection());
    }

    @Override
	public List<Orgao> find(SearchCriterios searchCriterios) throws Exception {
		Search<Orgao> search = new SearchBuilder<Orgao>("mob_orgao")
				.searchCriterios(searchCriterios)
				.build();
		return search.getList(Orgao.class);
	}

	@Override
	public List<Orgao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Orgao> search = new SearchBuilder<Orgao>("mob_orgao")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(Orgao.class);
	}
}
