package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNaiEnvio extends TipoImpressaoNotificacaoHandler {
	
	private ILoteImpressaoService loteImpressaoService;
	private IAitMovimentoService aitMovimentoService;
	private ServicoDetranServices servicoDetranServices;
	private AitRepository aitRepository;
	
	public GeraSegundaViaNaiEnvio() throws Exception {
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public byte[] gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (tipoImpressaoNotificacao.getContemMovimento() && !(tipoImpressaoNotificacao.getMovimentoEnviado()) && !(tipoImpressaoNotificacao.getRegistradoEmLote())) {
			AitMovimento aitMovimento = this.aitMovimentoService.getMovimentoTpStatus(tipoImpressaoNotificacao.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
			List<AitMovimento> aitMovimentoList = Arrays.asList(aitMovimento);
			this.servicoDetranServices.remessa(aitMovimentoList);
			verificarEnvioNai(tipoImpressaoNotificacao.getCdAit());
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NAI.getKey());
			loteNotificacaoNaiViaUnica.setLgGeracaoViaUnica(true);
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			return this.loteImpressaoService.imprimirLoteNotificacao(loteNotificacaoNaiViaUnica.getCdLoteImpressao(), customConnection);
		} 
		else {
			throw new ValidacaoException("Erro ao gerar impressão, verifique os dados do movimento.");
		}
	}
	
	private void verificarEnvioNai(int cdAit) throws Exception {
		AitMovimento naiEnviada = this.aitMovimentoService.getMovimentoTpStatus(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey());
		if (naiEnviada.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			throw new ValidacaoException("Não foi possível gerar a NAI, verifique os envios do movimento.");
		}
	}

}
