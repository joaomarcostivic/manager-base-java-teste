package com.tivic.manager.mob.colaborador.validators;

import java.sql.Types;
import java.util.List;

import com.tivic.manager.grl.pessoavinculohistorico.enums.StPessoaVinculoEnum;
import com.tivic.manager.mob.colaborador.ColaboradorDTO;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ColaboradorPorVinculoValidator implements Validator<ColaboradorDTO> {
	
	@Override
	public void validate(ColaboradorDTO colaboradorDTO, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("B.nm_vinculo", "RELATOR", ItemComparator.DIFFERENT, Types.VARCHAR);
		searchCriterios.addCriterios("B.nm_vinculo", "AGENTE", ItemComparator.DIFFERENT, Types.VARCHAR);
		searchCriterios.addCriteriosEqualInteger("A.st_vinculo", StPessoaVinculoEnum.ST_ATIVO.getKey());		
		Search<ColaboradorDTO> search = new SearchBuilder<ColaboradorDTO>("grl_pessoa_empresa A")
				.fields("A.cd_pessoa, A.cd_vinculo, B.nm_vinculo, A.st_vinculo")
				.addJoinTable("JOIN grl_vinculo B ON (A.cd_vinculo = B.cd_vinculo)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		List<ColaboradorDTO> listaMembrosAtivos = search.getList(ColaboradorDTO.class);
		if (verificarCadastro(colaboradorDTO, listaMembrosAtivos)) {
			throw new Exception("Já existe um membro ativo com esse vínculo.");
		}
	}
	
	private boolean verificarCadastro(ColaboradorDTO colaboradorDTO, List<ColaboradorDTO> listaMembrosAtivos) {
		return listaMembrosAtivos.stream().anyMatch(r -> r.getCdVinculo() == colaboradorDTO.getCdVinculo());
	}
}
