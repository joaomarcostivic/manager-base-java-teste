package com.tivic.manager.seg.usuario.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.protocolosv3.documento.ata.Ata;
import com.tivic.manager.ptc.protocolosv3.documento.ata.IAtaRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorUsuarioPertencenteAta implements Validator<Integer> {
	private IAtaRepository ataRpository;
	
	public ValidatorUsuarioPertencenteAta() throws Exception{
		ataRpository = (IAtaRepository) BeansFactory.get(IAtaRepository.class);
	}

	@Override
	public void validate(Integer cdUsuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
		List<Ata> atas = ataRpository.find(searchCriterios, customConnection);
		if(!atas.isEmpty()) {
			throw new BadRequestException("O usuário não pode ser excluído, pois está vinculado a Atas.");
		}
	}
}
