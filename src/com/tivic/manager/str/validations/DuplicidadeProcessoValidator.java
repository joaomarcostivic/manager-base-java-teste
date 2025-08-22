package com.tivic.manager.str.validations;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.str.Processo;
import com.tivic.manager.str.ProcessoDTO;
import com.tivic.manager.str.ProcessoServices;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class DuplicidadeProcessoValidator implements Validator<ProcessoDTO> {

	@Override
	public void validate(ProcessoDTO processo, CustomConnection customConnection) throws Exception {		
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("A.tp_processo", processo.getTpProcesso());
		criterios.addCriteriosEqualInteger("A.cd_ait", processo.getCdAit());
		criterios.addCriteriosEqualInteger("A.st_processo", ProcessoServices.ST_PENDENTE);
		
		com.tivic.sol.search.Search<Processo> search = new SearchBuilder<Processo>("str_processo A ")
				.addJoinTable("JOIN str_processo_requerente B ON (A.cd_processo = B.cd_processo) ")
				.searchCriterios(criterios)
				.log()
				.build();

		String tipoProcesso = TipoProcessoEnum.valueOf(processo.getTpProcesso());
		
		if(search.getList(Processo.class).size() > 0)
			throw new BadRequestException("Já existe uma solicitação de "+tipoProcesso+" em andamento para este Auto de Infração.");
		
	}

}
