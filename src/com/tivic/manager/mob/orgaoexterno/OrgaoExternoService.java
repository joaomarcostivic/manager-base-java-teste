package com.tivic.manager.mob.orgaoexterno;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class OrgaoExternoService implements IOrgaoExternoService{

	OrgaoExternoRepository orgaoExternoRepository;
	
	public OrgaoExternoService() throws Exception {
		orgaoExternoRepository = (OrgaoExternoRepository) BeansFactory.get(OrgaoExternoRepository.class);
	}

	@Override
	public OrgaoExterno insert(OrgaoExterno orgaoExterno) throws Exception {
		return insert(orgaoExterno, new CustomConnection());
	}

	@Override
	public OrgaoExterno insert(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			this.orgaoExternoRepository.insert(orgaoExterno, customConnection);
			customConnection.finishConnection();
			return orgaoExterno;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public OrgaoExterno update(OrgaoExterno orgaoExterno) throws Exception {
		return update(orgaoExterno, new CustomConnection());
	}

	@Override
	public OrgaoExterno update(OrgaoExterno orgaoExterno, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			orgaoExterno = this.orgaoExternoRepository.update(orgaoExterno, customConnection);
			customConnection.finishConnection();
			return orgaoExterno;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public OrgaoExterno get(int cdOrgaoExterno) throws Exception {
		return get(cdOrgaoExterno, new CustomConnection());
	}

	@Override
	public OrgaoExterno get(int cdOrgaoExterno, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			OrgaoExterno orgaoExterno = this.orgaoExternoRepository.get(cdOrgaoExterno, customConnection);
			if(orgaoExterno == null)
				throw new NoContentException("Nenhum equipamento encontrado");
			customConnection.finishConnection();
			return orgaoExterno;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PagedResponse<OrgaoExterno> findPaged(SearchCriterios searchCriterios) throws Exception {
		return findPaged(searchCriterios, new CustomConnection());
	}

	@Override
	public PagedResponse<OrgaoExterno> findPaged(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<OrgaoExterno> search = new SearchBuilder<OrgaoExterno>("mob_orgao_externo")
					.searchCriterios(searchCriterios)
					.count()
					.build();
			customConnection.finishConnection();
			return new PagedResponse<OrgaoExterno>(search.getList(OrgaoExterno.class), search.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<OrgaoExterno> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<OrgaoExterno> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<OrgaoExterno> search = new SearchBuilder<OrgaoExterno>("mob_orgao_externo A")
					.fields("A.nm_orgao_externo, A.sg_orgao_externo, A.cd_orgao_externo")
					.searchCriterios(searchCriterios)
					.build();
			customConnection.finishConnection();
			return search.getList(OrgaoExterno.class);
		} finally {
			customConnection.closeConnection();
		}	
	}
}