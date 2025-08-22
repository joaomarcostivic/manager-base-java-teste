package com.tivic.manager.mob.lotes.repository.banco;

import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.lotes.enums.banco.BancoConvenioEnum;
import com.tivic.manager.mob.lotes.model.banco.Banco;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BancoRepositoryDAO implements BancoRepository {
	
	public String getConveniados() throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.banco_conveniado", BancoConvenioEnum.COM_CONVENIO.getKey());
		List<Banco> bancos = new SearchBuilder<Banco>("grl_banco A")
				.searchCriterios(searchCriterios)
				.build()
				.getList(Banco.class);		
		if (bancos.isEmpty()) {
			return null;
		}
		return bancos.stream()
                .map(Banco::getNmBanco)
                .collect(Collectors.joining(", "));
	}
}
