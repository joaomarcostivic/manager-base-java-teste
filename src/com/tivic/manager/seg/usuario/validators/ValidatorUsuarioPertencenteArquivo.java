package com.tivic.manager.seg.usuario.validators;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidatorUsuarioPertencenteArquivo implements Validator<Integer> {
	private IArquivoRepository arquivoRepository;
	
	public ValidatorUsuarioPertencenteArquivo() throws Exception {
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
	}

	@Override
	public void validate(Integer cdUsuario, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
		List<Arquivo> arquivos = arquivoRepository.find(searchCriterios);
		if(!arquivos.isEmpty()) {
			throw new BadRequestException("O usuário não pode ser excluído, pois está vinculado a arquivos.");
		}
	}

}
