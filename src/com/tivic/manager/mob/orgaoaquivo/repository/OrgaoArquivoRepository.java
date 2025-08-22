package com.tivic.manager.mob.orgaoaquivo.repository;

import java.util.List;

import com.tivic.manager.mob.orgaoaquivo.OrgaoArquivo;
import com.tivic.manager.mob.orgaoaquivo.OrgaoArquivoDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface OrgaoArquivoRepository {
	void insert(OrgaoArquivo orgaoArquivo, CustomConnection customConnection) throws Exception;
	void update(OrgaoArquivo orgaoArquivo, CustomConnection customConnection) throws Exception;
	OrgaoArquivo get(int cdOrgaoArquivo) throws Exception;
	OrgaoArquivo get(int cdOrgaoArquivo, CustomConnection customConnection) throws Exception;
	List<OrgaoArquivo> find(SearchCriterios searchCriterios) throws Exception;
	List<OrgaoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	Search<OrgaoArquivoDTO> getCaminho(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
