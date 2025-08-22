package com.tivic.manager.mob.lote.impressao.codigobarras;

import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.codigobarras.MG.GeraCodigoBarras;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;

public class GerarCodigoBarrasFactory {
	ILoteNotificacaoService loteNotificacaoService;
	
	public GerarCodigoBarrasFactory() throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
	}
	public IGerarCodigoBarras getStrategy() throws ValidacaoException {
		String sgEstado = loteNotificacaoService.getEstadoOrgaoAutuador();
		switch (sgEstado) {
		case "MG":
		case "BA":
			return new GeraCodigoBarras();
		default: 
			throw new ValidacaoException("Código de barras não implementado para este estado.");
	}
	}
}
