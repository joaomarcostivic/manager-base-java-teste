package com.tivic.manager.mob.lotes.impressao.strategy.viaunica;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.builders.impressao.viaunica.NipImpressaoDTOBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.impressao.strategy.TipoImpressaoNotificacaoNPHandler;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNipEnvio extends TipoImpressaoNotificacaoNPHandler {
	
	private ILoteImpressaoService loteImpressaoService;
	private IAitMovimentoService aitMovimentoServices;
	private ServicoDetranServices servicoDetranServices;
	private AitRepository aitRepository;
	
	public GeraSegundaViaNipEnvio() throws Exception {
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public NipImpressaoDTO gerar(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception {
		if (!tipoImpressaoNotificacao.getMovimentoAdvertencia() && tipoImpressaoNotificacao.getContemMovimento() && !(tipoImpressaoNotificacao.getMovimentoEnviado()) && !(tipoImpressaoNotificacao.getRegistradoEmLote())) {
			AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(tipoImpressaoNotificacao.getCdAit(), TipoStatusEnum.NIP_ENVIADA.getKey());
			List<AitMovimento> aitMovimentoList = Arrays.asList(aitMovimento);
			this.servicoDetranServices.remessa(aitMovimentoList);
			verificarEnvioNIP(tipoImpressaoNotificacao.getCdAit());
			Ait ait = aitRepository.get(tipoImpressaoNotificacao.getCdAit());
			List<Ait> aitList = Arrays.asList(ait);
			LoteImpressao loteNotificacaoNipViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NIP.getKey());
			loteNotificacaoNipViaUnica.setLgGeracaoViaUnica(true);
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNipViaUnica, tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			byte[] arquivo = this.loteImpressaoService.imprimirLoteNotificacao(loteNotificacaoNipViaUnica.getCdLoteImpressao(), customConnection); 
			NipImpressaoDTO nipImpressaoDTO = new NipImpressaoDTOBuilder()
					.addArquivo(arquivo)
					.addCdAit(tipoImpressaoNotificacao.getCdAit())
					.build();
			return nipImpressaoDTO;
		} 
		else {
			throw new ValidacaoException("Erro ao gerar impressão, verifique os dados do movimento.");
		}
	}
	
	private void verificarEnvioNIP(int cdAit) throws Exception {
		AitMovimento nipEnviada = this.aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.NIP_ENVIADA.getKey());
		if (nipEnviada.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			throw new ValidacaoException("Não foi possível gerar a NIP, verifique os envios do movimento.");
		}
	}

}
