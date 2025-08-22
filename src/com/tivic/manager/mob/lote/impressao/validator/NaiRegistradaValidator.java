package com.tivic.manager.mob.lote.impressao.validator;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.util.validator.Validator;

public class NaiRegistradaValidator implements Validator<Ait> {

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		List<AitMovimento> listMovimento = searchNaiRegistrada(object).getList(AitMovimento.class);
		if (listMovimento.size() > 0) {
			return;
		}
		throw new AitReportErrorException("E preciso ter NAI emitida e enviada ao detran.");
	}
	
	private Search<AitMovimento> searchNaiRegistrada(Ait ait) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.*")
				.searchCriterios(criteriosNaiRegistrada(ait))
				.build();
		return search;
	}
	
	private SearchCriterios criteriosNaiRegistrada(Ait ait) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", ait.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.NAI_ENVIADO.getKey() , true);
		searchCriterios.addCriteriosEqualInteger("A.lg_enviado_detran", TipoStatusEnum.ENVIADO_AO_DETRAN.getKey() , true);
		return searchCriterios;
	}

}
