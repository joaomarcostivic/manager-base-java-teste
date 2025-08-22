package com.tivic.manager.mob.lote.impressao.viaunica.nip;

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
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraSegundaViaNIPEnvio extends TipoImpressaoNotificacaoNPHandler {

	private ILoteNotificacaoService loteNotificacaoService;
	private IAitMovimentoService aitMovimentoServices;
	private ServicoDetranServices servicoDetranServices;
	private AitRepository aitRepository;
	
	public GeraSegundaViaNIPEnvio() throws Exception {
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
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
			LoteImpressao loteNotificacaoNipViaUnica = this.loteNotificacaoService.gerarLoteNotificacaoAitViaUnica(aitList, tipoImpressaoNotificacao.getCdUsuario(), TipoLoteNotificacaoEnum.LOTE_NIP.getKey());
			this.loteNotificacaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNipViaUnica.getCdLoteImpressao(), tipoImpressaoNotificacao.getCdUsuario(), customConnection);
			byte[] arquivo = this.loteNotificacaoService.imprimirLoteNotificacao(loteNotificacaoNipViaUnica.getCdLoteImpressao(), customConnection); 
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
