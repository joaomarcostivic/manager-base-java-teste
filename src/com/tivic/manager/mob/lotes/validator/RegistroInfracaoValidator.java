package com.tivic.manager.mob.lotes.validator;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RegistroInfracaoValidator implements IValidator<Ait> {

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		List<AitMovimento> listRegistroInfracao = searchRegistroInfracao(object).getList(AitMovimento.class);
		if (listRegistroInfracao.size() > 0) {
			return;
		}
		throw new AitReportErrorException("O AIT precisa estar registrado e enviado ao detran.");
	}
	
	private Search<AitMovimento> searchRegistroInfracao(Ait ait) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.*")
				.searchCriterios(criteriosRegistroInfracao(ait))
				.build();
		return search;
	}
	
	private SearchCriterios criteriosRegistroInfracao(Ait ait) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", ait.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.REGISTRO_INFRACAO.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("A.lg_enviado_detran", TipoStatusEnum.ENVIADO_AO_DETRAN.getKey(), true);
		return searchCriterios;
	}

}
