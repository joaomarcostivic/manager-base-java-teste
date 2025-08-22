package com.tivic.manager.mob.lote.impressao.viaunica.nai;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacaoHandler;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNAIEnvio extends TipoImpressaoNotificacaoHandler {
	
	private ILoteNotificacaoService loteNotificacaoService;
	private IAitMovimentoService aitMovimentoServices;
	private ServicoDetranServices servicoDetranServices;
	private AitRepository aitRepository;
	
	public GeraSegundaViaNAIEnvio() throws Exception {
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (tipoImpressaoNotificacao.getContemMovimento() && !(tipoImpressaoNotificacao.getMovimentoEnviado()) && !(tipoImpressaoNotificacao.getRegistradoEmLote())) {
			AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(tipoImpressaoNotificacao.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
			List<AitMovimento> aitMovimentoList = Arrays.asList(aitMovimento);
			this.servicoDetranServices.remessa(aitMovimentoList);
			verificarEnvioNai(tipoImpressaoNotificacao.getCdAit());
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteNotificacaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteNotificacaoEnum.LOTE_NAI.getKey());
			this.loteNotificacaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteNotificacaoService.imprimirLoteNotificacao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);
		} 
		else {
			throw new ValidacaoException("Erro ao gerar impressão, verifique os dados do movimento.");
		}
	}
	
	private void verificarEnvioNai(int cdAit) throws Exception {
		AitMovimento naiEnviada = this.aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey());
		if (naiEnviada.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			throw new ValidacaoException("Não foi possível gerar a NAI, verifique os envios do movimento.");
		}
	}

}
