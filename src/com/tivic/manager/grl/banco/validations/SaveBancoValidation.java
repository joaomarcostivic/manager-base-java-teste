package com.tivic.manager.grl.banco.validations;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.Banco;
import com.tivic.manager.grl.banco.repository.BancoRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public class SaveBancoValidation implements Validator<Banco> {
	private BancoRepository bancoRepository;
	
	public SaveBancoValidation() throws Exception {
		bancoRepository = (BancoRepository) BeansFactory.get(BancoRepository.class);
	}
	
	@Override
	public void validate(Banco banco, CustomConnection customConnection) throws Exception {
		if(banco.getCdBanco() > 0) {
			Banco oldBanco = bancoRepository.get(banco.getCdBanco(), customConnection);
			if(oldBanco.getIdBanco().equals(banco.getIdBanco())) {
				return;
			}
		}
		
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_banco", banco.getIdBanco(), banco.getIdBanco() != null);
		
		Search<Banco> pagedBancos = bancoRepository.find(searchCriterios, customConnection);
		
		List<Banco> bancos = pagedBancos.getList(Banco.class);
		
		if(!bancos.isEmpty())
			throw new BadRequestException("Já existe um banco cadastrado com esse mesmo ID");	
	}

}
