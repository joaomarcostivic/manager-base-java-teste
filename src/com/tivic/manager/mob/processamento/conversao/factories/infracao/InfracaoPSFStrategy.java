package com.tivic.manager.mob.processamento.conversao.factories.infracao;

import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.infracao.IInfracaoService;
import com.tivic.manager.util.parametro.ParametroFacade;
import com.tivic.sol.connection.CustomConnection;

public class InfracaoPSFStrategy implements InfracaoStrategy {
	
	private IInfracaoService infracaoService;
	
	public InfracaoPSFStrategy(IInfracaoService infracaoService) {
		this.infracaoService = infracaoService;
	}
	
	public Infracao getInfracao(CustomConnection customConnection) throws Exception {
		return infracaoService.getByCodDetran(new ParametroFacade().getVlParametroAsInt("NR_CODIGO_PARADA_SOBRE_FAIXA", customConnection), customConnection);
	}
}
