package com.tivic.manager.mob.lote.impressao.viaunica.nip;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.lote.impressao.GerarDocumentoSegundaViaAdvertencia;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaAdvertencia extends TipoImpressaoNotificacaoNPHandler {
	private IAitMovimentoService aitMovimentoService;
	
	public GeraSegundaViaAdvertencia() throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		NipImpressaoDTO nipImpressaoDTO = new NipImpressaoDTO();		
		nipImpressaoDTO.setCdAit(tipoImpressaoNotificacao.getCdAit());
		if (tipoImpressaoNotificacao.getMovimentoAdvertencia() && tipoImpressaoNotificacao.getContemMovimento() && tipoImpressaoNotificacao.getRegistradoEmLote()
				&& !verificarSituacaoAdvertenciaCancelada(tipoImpressaoNotificacao.getCdAit())) {			
			if(!tipoImpressaoNotificacao.getMovimentoEnviado()) {
				throw new ValidacaoException("Para gerar uma segunda via da advertência o movimento precisa ser enviado ao DETRAN.");
			}
			verificarEnvioAdvertencia(tipoImpressaoNotificacao.getCdAit());
			Boolean printPortal = false;
			nipImpressaoDTO.setArquivo(new GerarDocumentoSegundaViaAdvertencia().gerarDocumentos(tipoImpressaoNotificacao.getCdAit(), printPortal, customConnection));
			return nipImpressaoDTO;
		} else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}
	
	private void verificarEnvioAdvertencia(int cdAit) throws Exception {
		AitMovimento advertenciaEnviada = this.aitMovimentoService.getMovimentoTpStatus(cdAit, TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		if (advertenciaEnviada.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			throw new ValidacaoException("Não foi possível gerar a advertencia, verifique os envios do movimento.");
		}
	}
	
	private boolean verificarSituacaoAdvertenciaCancelada(int cdAit) throws Exception {
		AitMovimento penalidadeAdvertencia = this.aitMovimentoService.getMovimentoTpStatus(cdAit, TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		if(penalidadeAdvertencia == null) {
			return false;
		}
		return penalidadeAdvertencia.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey();
	}
}
