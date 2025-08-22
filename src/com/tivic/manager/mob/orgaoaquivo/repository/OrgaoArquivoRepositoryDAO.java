package com.tivic.manager.mob.orgaoaquivo.repository;

import java.util.List;

import com.tivic.manager.mob.orgaoaquivo.OrgaoArquivo;
import com.tivic.manager.mob.orgaoaquivo.OrgaoArquivoDAO;
import com.tivic.manager.mob.orgaoaquivo.OrgaoArquivoDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class OrgaoArquivoRepositoryDAO implements OrgaoArquivoRepository {

	@Override
	public void insert(OrgaoArquivo orgaoArquivo, CustomConnection customConnection) throws Exception {
		int cdOrgaoArquivo = OrgaoArquivoDAO.insert(orgaoArquivo, customConnection.getConnection());
		if(cdOrgaoArquivo < 0)
			throw new Exception("Erro ao inserir OrgaoArquivo");
		orgaoArquivo.setCdOrgaoArquivo(cdOrgaoArquivo);
	}

	@Override
	public void update(OrgaoArquivo orgaoArquivo, CustomConnection customConnection) throws Exception {
		int cdOrgaoArquivo = OrgaoArquivoDAO.update(orgaoArquivo, customConnection.getConnection());
		if(cdOrgaoArquivo <= 0)
			throw new Exception("Erro ao inserir OrgaoArquivo");		
	}

	@Override
	public OrgaoArquivo get(int cdOrgaoArquivo) throws Exception {
		return get(cdOrgaoArquivo, new CustomConnection());
	}

	@Override
	public OrgaoArquivo get(int cdOrgaoArquivo, CustomConnection customConnection) throws Exception {
		return OrgaoArquivoDAO.get(cdOrgaoArquivo, customConnection.getConnection());
	}

	@Override
	public List<OrgaoArquivo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<OrgaoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<OrgaoArquivo> search = new SearchBuilder<OrgaoArquivo>("mob_orgao_arquivo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(OrgaoArquivo.class);
	}

	@Override
	public Search<OrgaoArquivoDTO> getCaminho(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<OrgaoArquivoDTO> caminhoArquivo = new SearchBuilder<OrgaoArquivoDTO>("mob_orgao_arquivo A")
				.fields("A.*, B.nm_arquivo, B.txt_caminho_arquivo, B.nm_documento")
				.addJoinTable("JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo)")
				.addJoinTable("JOIN mob_orgao C ON (A.cd_orgao = C.cd_orgao)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		return caminhoArquivo;
	}
}
