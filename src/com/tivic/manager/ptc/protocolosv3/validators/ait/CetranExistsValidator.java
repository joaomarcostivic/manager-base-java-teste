package com.tivic.manager.ptc.protocolosv3.validators.ait;

import java.util.List;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CetranExistsValidator implements IProtocoloValidator {
	private IAitService aitService;
	private final int ENVIADO_AO_DETRAN = 1;
	
	public CetranExistsValidator() throws Exception {
		aitService = (IAitService) BeansFactory.get(IAitService.class);
	}
	
	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		List<Ait> aitList = aitService.find(montarSearchCriterios(protocolo), connection);
		if(!aitList.isEmpty()) {
			throw new ValidationException("Não pode haver um recurso CETRAN atrelado ao AIT.");
		}
	}

	private SearchCriterios montarSearchCriterios( ProtocoloDTO protocolo ) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("TP_STATUS", TipoStatusEnum.RECURSO_CETRAN.getKey());
		searchCriterios.addCriteriosEqualInteger("CD_AIT", protocolo.getAit().getCdAit());
		searchCriterios.addCriteriosEqualInteger("LG_ENVIADO_DETRAN", ENVIADO_AO_DETRAN);
		return searchCriterios;
	}
}
