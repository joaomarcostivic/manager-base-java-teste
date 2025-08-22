package com.tivic.manager.mob.lotes.impressao.strategy.viaunica.validations;

import java.util.List;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.cdi.BeansFactory;

public class NicNipViaUnicaValidator implements INicNipViaUnicaValidator {

	private final IAitMovimentoService aitMovimentoService;

	public NicNipViaUnicaValidator() throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public boolean validate(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception, ValidacaoException {
		boolean advertencia = tipoImpressaoNotificacao.getMovimentoAdvertencia();
		boolean contemMovimento = tipoImpressaoNotificacao.getContemMovimento();
		boolean enviado = tipoImpressaoNotificacao.getMovimentoEnviado();
		boolean registradoLote = tipoImpressaoNotificacao.getRegistradoEmLote();
		boolean cancelado = tipoImpressaoNotificacao.getMovimentoCancelado();
		boolean nicValido = verificarAitNic(tipoImpressaoNotificacao.getCdAit(), customConnection);

		return (!advertencia && nicValido && !(contemMovimento || enviado || registradoLote)) || cancelado;
	}

	private boolean verificarAitNic(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NIC_ENVIADA.getKey(), true);
		List<AitMovimento> movimentos = this.aitMovimentoService.find(searchCriterios, customConnection);
		return !movimentos.isEmpty();
	}
}