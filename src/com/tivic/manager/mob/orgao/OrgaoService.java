package com.tivic.manager.mob.orgao;

import java.util.List;

import com.tivic.manager.mob.Orgao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class OrgaoService implements IOrgaoService{
	
	private OrgaoRepository orgaoRepository;

	public OrgaoService() throws Exception {
		this.orgaoRepository = (OrgaoRepository) BeansFactory.get(OrgaoRepository.class);
	}
	
	@Override
	public Orgao getByNome(String nmOrgao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_orgao", nmOrgao, true);
		List<Orgao> orgaos = this.orgaoRepository.find(searchCriterios);
		if(orgaos.isEmpty())
			throw new Exception("Nenhum orgão encontrado");
		return orgaos.get(0);
	}

	@Override
	public Orgao getById(String idOrgao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_orgao", idOrgao, true);
		List<Orgao> orgaos = this.orgaoRepository.find(searchCriterios);
		if(orgaos.isEmpty())
			throw new Exception("Nenhum orgão encontrado");
		return orgaos.get(0);
	}
	
	@Override
	public Orgao getByCdOrgao(int cdOrgao, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_orgao", cdOrgao, true);
		List<Orgao> orgaos = this.orgaoRepository.find(searchCriterios, customConnection);
		if(orgaos.isEmpty())
			throw new Exception("Nenhum orgão encontrado");
		return orgaos.get(0);
	}

	@Override
	public Orgao getOrgaoUnico() throws Exception {
		Orgao orgao = new Orgao();
		List<Orgao> orgaos = this.orgaoRepository.find(new SearchCriterios());
		if(orgaos.isEmpty())
			throw new Exception("Nenhum orgão encontrado");
		for(Orgao orgaoUnico: orgaos) {
			if(orgaoUnico.getLgEmitirAit() == 0)
				continue;
			orgao = orgaoUnico;
			break;
		}
		return orgao;
	}
	
}
