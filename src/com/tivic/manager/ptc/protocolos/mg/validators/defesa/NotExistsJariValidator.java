package com.tivic.manager.ptc.protocolos.mg.validators.defesa;

import java.util.List;

import org.apache.commons.validator.ValidatorException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class NotExistsJariValidator implements IValidator<DadosProtocoloDTO> {
	@Override
	public void validate(DadosProtocoloDTO object, CustomConnection connection) throws Exception {
		List<AitMovimento> listMovimento = searchNaiRegistrada(object.getAit()).getList(AitMovimento.class);
		if (listMovimento.isEmpty()) {
			return;
		}
		throw new ValidatorException("Não é possível registrar uma Advertência com Jari lançada.");
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
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.RECURSO_JARI.getKey() , true);
		return searchCriterios;
	}
}
