package com.tivic.manager.mob.lote.impressao.viaunica.nip;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.CodigoCancelamentoMovimentoMap;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.GeraDocImpressaoAdvertenciaViaUnica;
import com.tivic.manager.mob.lote.impressao.GeraMovimentoAdvertenciaViaUnica;
import com.tivic.manager.mob.lote.impressao.GerarMovimentoNotificacaoFactory;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.ListAitsCandidatosNipFactory;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraAdvertencia extends TipoImpressaoNotificacaoNPHandler {
	private IAitMovimentoService aitMovimentoService;
	private ILoteNotificacaoService loteNotificacaoService;
	private IVerificaInfracaoAdvertencia verificaInfracaoAdvertencia;
	
	public GeraAdvertencia() throws Exception {
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
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
					.getEstrategiaListAits(TipoLoteNotificacaoEnum.LOTE_NIP_VIA_UNICA.getKey(), 
							searchNotificacaoViaUnica(tipoImpressaoNotificacao.getCdAit()), customConnection).build();
			finalizarPrazoDefesa(aitList, tipoImpressaoNotificacao.getCdUsuario());
	    	new GeraMovimentoAdvertenciaViaUnica().gerarMovimentos(aitList, tipoImpressaoNotificacao.getCdUsuario());
	    	LoteImpressao loteNotificacaoAdvertenciaViaUnica = loteNotificacaoService.gerarLoteNotificacaoAitViaUnica(aitList,
	    			tipoImpressaoNotificacao.getCdUsuario(), TipoLoteNotificacaoEnum.LOTE_NIP.getKey());
			new GeraDocImpressaoAdvertenciaViaUnica().gerarDocumentos(loteNotificacaoAdvertenciaViaUnica, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			nipImpressaoDTO.setArquivo(this.loteNotificacaoService.imprimirLoteNotificacao(loteNotificacaoAdvertenciaViaUnica.getCdLoteImpressao(), customConnection));
			return nipImpressaoDTO;
		}
		else {
			return nextGenerator.gerar(tipoImpressaoNotificacao, customConnection);
		}
	}
	
	private void finalizarPrazoDefesa(List<Ait> aitList, int cdUsuario) throws Exception {
		AitMovimento aitMovimento = buscarFimPrazoDefesa(aitList.get(0).getCdAit(), TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
		if (aitMovimento.getCdMovimento() <= 0 || aitMovimento.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			new GerarMovimentoNotificacaoFactory()
				.strategy(TipoLoteNotificacaoEnum.LOTE_FIM_PRAZO_DEFESA_VIA_UNICA.getKey())
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
