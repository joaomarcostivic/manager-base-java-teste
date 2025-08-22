package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.List;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.CodigoCancelamentoMovimentoMap;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.factory.impressao.GeraMovimentoNotificacaoFactory;
import com.tivic.manager.mob.lotes.factory.impressao.ListAitsCandidatosNipFactory;
import com.tivic.manager.mob.lotes.impressao.strategy.TipoImpressaoNotificacaoNPHandler;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.DocumentGeneratorNip;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.movimento.IVerificaInfracaoAdvertencia;
import com.tivic.manager.mob.lotes.movimento.viaunica.GeraMovimentoAdvertenciaViaUnica;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.mob.lotes.utils.ParametroWrapper;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraNpAdvertencia extends TipoImpressaoNotificacaoNPHandler {
	
	private IAitMovimentoService aitMovimentoService;
	private ILoteImpressaoService loteImpressaoService;
	private IVerificaInfracaoAdvertencia verificaInfracaoAdvertencia;
	
	public GeraNpAdvertencia() throws Exception {
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		verificaInfracaoAdvertencia = (IVerificaInfracaoAdvertencia) BeansFactory.get(IVerificaInfracaoAdvertencia.class);
	}

	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		NipImpressaoDTO nipImpressaoDTO = new NipImpressaoDTO();		
		nipImpressaoDTO.setCdAit(tipoImpressaoNotificacao.getCdAit());
	    if (verificaInfracaoAdvertencia.isPassivelAdvertencia(tipoImpressaoNotificacao.getCdAit()) 
	    		&& (!(tipoImpressaoNotificacao.getContemMovimento() || tipoImpressaoNotificacao.getMovimentoEnviado() 
	    				|| tipoImpressaoNotificacao.getRegistradoEmLote() || tipoImpressaoNotificacao.getMovimentoCancelado()))) {
			List<Ait> aitList = new ListAitsCandidatosNipFactory()
					.getEstrategiaListAits(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey(), 
							searchNotificacaoViaUnica(tipoImpressaoNotificacao.getCdAit()), customConnection).build();
			finalizarPrazoDefesa(aitList, tipoImpressaoNotificacao.getCdUsuario());
	    	new GeraMovimentoAdvertenciaViaUnica().gerarMovimentos(aitList, tipoImpressaoNotificacao.getCdUsuario());
	    	LoteImpressao loteNotificacaoAdvertenciaViaUnica = loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList,
	    			tipoImpressaoNotificacao.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NIP.getKey());
	        new DocumentGeneratorNip(TipoRemessaCorreiosEnum.fromCode(ParametroWrapper.asInt("MOB_TIPO_ENVIO_CORREIOS_NIP", customConnection)));
			nipImpressaoDTO.setArquivo(this.loteImpressaoService.imprimirLoteNotificacao(loteNotificacaoAdvertenciaViaUnica.getCdLoteImpressao(), customConnection));
			return nipImpressaoDTO;
		}
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}
	
	private void finalizarPrazoDefesa(List<Ait> aitList, int cdUsuario) throws Exception {
		AitMovimento aitMovimento = buscarFimPrazoDefesa(aitList.get(0).getCdAit(), TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
		if (aitMovimento.getCdMovimento() <= 0 || aitMovimento.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			new GeraMovimentoNotificacaoFactory()
				.strategy(TipoLoteImpressaoEnum.LOTE_FIM_PRAZO_DEFESA_VIA_UNICA.getKey())
				.gerarMovimentos(aitList, cdUsuario);
		}
	}
	
	private AitMovimento buscarFimPrazoDefesa (int cdAit, int tpStatus) throws Exception {
		AitMovimento aitMovimentoCancelamento= aitMovimentoService.getMovimentoTpStatus(cdAit, new CodigoCancelamentoMovimentoMap().get(tpStatus));
		AitMovimento aitMovimentoFimPrazoDefesa = aitMovimentoService.getMovimentoTpStatus(cdAit, TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
		if(aitMovimentoCancelamento.getCdMovimento() > 0) {
			return aitMovimentoCancelamento.getDtMovimento().before(aitMovimentoFimPrazoDefesa.getDtMovimento()) ? aitMovimentoFimPrazoDefesa : new AitMovimento();
		}
		return aitMovimentoFimPrazoDefesa;
	}

	private SearchCriterios searchNotificacaoViaUnica(int cdAit) {
		SearchCriterios search = new SearchCriterios();
		search.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return search;
	}

}
