package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AtaRelatorService implements IAtaRelatorService {
	private IAtaRelatorRepository ataRelatorRepository;
	
	public AtaRelatorService() throws Exception {
		ataRelatorRepository = (IAtaRelatorRepository) BeansFactory.get(IAtaRelatorRepository.class);
	}
	
	@Override
	public void insertRelatores(int cdAta, List<Integer> listCdPessoa, CustomConnection customConnection) throws Exception {
		for (int i = 0; listCdPessoa.size() > i; i++) {
			insert(new AtaRelatorBuilder().addCdAta(cdAta).addCdPessoa(Integer.parseInt(listCdPessoa.get(i).toString())).build(), customConnection);
		}
	}

	@Override
	public int insert(AtaRelator ataRelator, CustomConnection customConnection) throws Exception {
		int retorno = ataRelatorRepository.insert(ataRelator, customConnection);
		return retorno;
	}
	
	@Override
	public List<AtaRelator> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}
	
	@Override
	public List<AtaRelator> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<AtaRelator> relatores = ataRelatorRepository.find(searchCriterios, customConnection);
		return relatores;
	}

	@Override
	public List<AtaRelatorDTO> getAtaRelator(int cdAta) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<AtaRelatorDTO> ataRelatorDTO = getAtaRelator(cdAta, customConnection);
			if(ataRelatorDTO.isEmpty()) {
				throw new Exception("Não foi encontrado nenhuma ata com esse código.");
			}
			customConnection.finishConnection();
			return ataRelatorDTO;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AtaRelatorDTO> getAtaRelator(int cdAta, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ata", cdAta);
		Search<AtaRelatorDTO> search = new SearchBuilder<AtaRelatorDTO>("ptc_ata_relator A ")
				.fields("D.nm_vinculo, B.nm_pessoa")
				.addJoinTable("JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)")
				.addJoinTable("JOIN grl_pessoa_empresa C ON (A.cd_pessoa = C.cd_pessoa)")
				.addJoinTable("JOIN grl_vinculo D ON (C.cd_vinculo = D.cd_vinculo)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(AtaRelatorDTO.class);
	}
}
