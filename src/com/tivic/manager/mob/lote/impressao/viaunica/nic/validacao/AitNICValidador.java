package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AitNICValidador implements INICValidador {
	
	private AitMovimentoRepository aitMovimentoRepository;
	
	public AitNICValidador() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}

	@Override
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return naoProprioAitNic(ait.getCdAit());
	}
	
	private boolean naoProprioAitNic(int cdAit) throws Exception {
		List<AitMovimento> aitMovimentoList = this.aitMovimentoRepository.find(montaSearchCriterios(cdAit));
		return aitMovimentoList.isEmpty();
	}
	
	private SearchCriterios montaSearchCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NIC_ENVIADA.getKey());
		return searchCriterios;
	}
	
}
