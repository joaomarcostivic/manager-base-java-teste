package com.tivic.manager.mob.processamento.conversao.factories.infracao;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.infracao.IInfracaoService;
import com.tivic.manager.util.parametro.ParametroFacade;
import com.tivic.sol.connection.CustomConnection;

public class InfracaoVALStrategy implements InfracaoStrategy {
	
	private EventoEquipamento evento;
	private IInfracaoService infracaoService;
	
	public InfracaoVALStrategy(EventoEquipamento evento, IInfracaoService infracaoService) {
		this.infracaoService = infracaoService;
		this.evento = evento;
	}

	public Infracao getInfracao(CustomConnection customConnection) throws Exception {
		double prFaixa = getPrFaixa(evento.getVlLimite(), evento.getVlConsiderada());
		int nrCodDetran = getNrCodDetran(prFaixa, customConnection);
		return infracaoService.getByCodDetran(nrCodDetran, customConnection);
	}
	
	private double getPrFaixa(double vlLimite, double vlConsiderada) {
        return Math.abs((vlLimite - vlConsiderada) / vlConsiderada) * 100;
    }

	private int getNrCodDetran(double prFaixa, CustomConnection customConnection) throws Exception {
		if (prFaixa > 0 && prFaixa <= 20)
			return new ParametroFacade().getVlParametroAsInt("MOB_NR_INFRACAO_VELOCIDADE_20", customConnection);
		if (prFaixa > 20 && prFaixa <= 50)
			return new ParametroFacade().getVlParametroAsInt("MOB_NR_INFRACAO_VELOCIDADE_20_50", customConnection);
		if (prFaixa > 50)
			return new ParametroFacade().getVlParametroAsInt("MOB_NR_INFRACAO_VELOCIDADE_50", customConnection);
		
		throw new IllegalArgumentException("A faixa do evento é inválida.");

	}
}
