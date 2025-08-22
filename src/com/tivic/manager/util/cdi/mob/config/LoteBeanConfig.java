package com.tivic.manager.util.cdi.mob.config;

import com.tivic.manager.grl.arquivo.ArquivoService;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;
import com.tivic.manager.mob.ait.AitService;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.aitimagem.AitImagemService;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.correios.CorreiosEtiquetaRepositoryDAO;
import com.tivic.manager.mob.correios.CorreiosEtiquetaService;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaService;
import com.tivic.manager.mob.lotes.impressao.pix.repository.AitPixRepository;
import com.tivic.manager.mob.lotes.impressao.pix.repository.AitPixRepositoryDAO;
import com.tivic.manager.mob.lotes.movimento.IVerificaInfracaoAdvertencia;
import com.tivic.manager.mob.lotes.movimento.VerificaInfracaoAdvertencia;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.LoteRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.aitimagem.AitImagemRepository;
import com.tivic.manager.mob.lotes.repository.aitimagem.AitImagemRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.lotes.repository.aitmovimento.AitMovimentoRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.arquivo.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lotes.repository.arquivo.LoteImpressaoArquivoRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.banco.BancoRepository;
import com.tivic.manager.mob.lotes.repository.banco.BancoRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaRepository;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.documentoexterno.LoteDocumentoExternoRepository;
import com.tivic.manager.mob.lotes.repository.documentoexterno.LoteDocumentoExternoRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoAitRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.publicacao.ILotePublicacaoAitRepository;
import com.tivic.manager.mob.lotes.repository.publicacao.LotePublicacaoAitRepositoryDAO;
import com.tivic.manager.mob.lotes.repository.publicacao.LotePublicacaoRepository;
import com.tivic.manager.mob.lotes.repository.publicacao.LotePublicacaoRepositoryDAO;
import com.tivic.manager.mob.lotes.service.dividaativa.ILoteDividaAtivaService;
import com.tivic.manager.mob.lotes.service.dividaativa.LoteDividaAtivaService;
import com.tivic.manager.mob.lotes.service.documentoexterno.ILoteDocumentoExternoService;
import com.tivic.manager.mob.lotes.service.documentoexterno.LoteDocumentoExternoService;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.mob.lotes.service.impressao.LoteImpressaoService;
import com.tivic.manager.mob.lotes.service.impressao.processamento.GeracaoDocumentosSSE;
import com.tivic.manager.mob.lotes.service.impressao.processamento.IGeracaoDocumentosSSE;
import com.tivic.manager.mob.lotes.service.publicacao.ILotePublicacaoService;
import com.tivic.manager.mob.lotes.service.publicacao.LotePublicacaoService;
import com.tivic.manager.mob.lotes.task.nic.ClientGeracaoNicTask;
import com.tivic.manager.mob.lotes.task.nic.IClientGeracaoNicTask;
import com.tivic.manager.mob.lotes.task.notificacao.ClientNotificacaoTask;
import com.tivic.manager.mob.lotes.task.notificacao.IClientNotificacaoTask;
import com.tivic.manager.mob.lotes.task.publicacao.ClientPublicacaoJariTask;
import com.tivic.manager.mob.lotes.task.publicacao.ClientPublicacaoNotificacaoTask;
import com.tivic.manager.mob.lotes.task.publicacao.IClientPublicacaoJariTask;
import com.tivic.manager.mob.lotes.task.publicacao.IClientPublicacaoNotificacaoTask;
import com.tivic.manager.ptc.generators.GeraNumeroEdital;
import com.tivic.manager.ptc.generators.IGeraNumeroEdital;
import com.tivic.manager.ptc.protocolos.julgamento.CartaJulgamentoService;
import com.tivic.manager.ptc.protocolos.julgamento.ICartaJulgamentoService;
import com.tivic.manager.ptc.protocolosv3.externo.oficio.GeraOficioProtocoloExterno;
import com.tivic.manager.ptc.protocolosv3.externo.oficio.IGeraOficioProtocoloExterno;
import com.tivic.sol.cdi.Scope;

public class LoteBeanConfig {

	public static void inject(Scope scope) throws Exception {
		injectRepositories(scope);
		injectServices(scope);
	}
	
	private static void injectRepositories(Scope scope) throws Exception {
		scope.inject(LoteRepository.class, new LoteRepositoryDAO());
		scope.inject(LoteImpressaoRepository.class, new LoteImpressaoRepositoryDAO());
		scope.inject(ILoteImpressaoAitRepository.class, new LoteImpressaoAitRepositoryDAO());
		scope.inject(LotePublicacaoRepository.class, new LotePublicacaoRepositoryDAO());
		scope.inject(LoteDocumentoExternoRepository.class, new LoteDocumentoExternoRepositoryDAO());
		scope.inject(ILotePublicacaoAitRepository.class, new LotePublicacaoAitRepositoryDAO());
		scope.inject(LoteDividaAtivaRepository.class, new LoteDividaAtivaRepositoryDAO());
		scope.inject(AitMovimentoRepository.class, new AitMovimentoRepositoryDAO());
		scope.inject(AitImagemRepository.class, new AitImagemRepositoryDAO());
		scope.inject(BancoRepository.class, new BancoRepositoryDAO());
		scope.inject(IParametroRepository.class, new ParametroRepositoryDAO());
		scope.inject(ILoteImpressaoArquivoRepository.class, new LoteImpressaoArquivoRepositoryDAO());
		scope.inject(AitPixRepository.class, new AitPixRepositoryDAO());
		scope.inject(ICorreiosEtiquetaRepository.class, new CorreiosEtiquetaRepositoryDAO());
	}
	
	private static void injectServices(Scope scope) throws Exception {
		scope.inject(IGeracaoDocumentosSSE.class, new GeracaoDocumentosSSE());
		scope.inject(ILoteImpressaoService.class, new LoteImpressaoService());
		scope.inject(ICartaJulgamentoService.class, new CartaJulgamentoService());
		scope.inject(ILotePublicacaoService.class, new LotePublicacaoService());
		scope.inject(ILoteDocumentoExternoService.class, new LoteDocumentoExternoService());
		scope.inject(IGeraOficioProtocoloExterno.class, new GeraOficioProtocoloExterno());
		scope.inject(ILoteDividaAtivaService.class, new LoteDividaAtivaService());
		scope.inject(ICorreiosEtiquetaService.class, new CorreiosEtiquetaService());
		scope.inject(IAitService.class, new AitService());
		scope.inject(IVerificaInfracaoAdvertencia.class, new VerificaInfracaoAdvertencia());
		scope.inject(IClientNotificacaoTask.class, new ClientNotificacaoTask());
		scope.inject(IAitImagemService.class, new AitImagemService());
		scope.inject(IArquivoService.class, new ArquivoService());
		scope.inject(IClientGeracaoNicTask.class, new ClientGeracaoNicTask());
		scope.inject(IGeraNumeroEdital.class, new GeraNumeroEdital());
		scope.inject(IClientPublicacaoNotificacaoTask.class, new ClientPublicacaoNotificacaoTask());
		scope.inject(IClientPublicacaoJariTask.class, new ClientPublicacaoJariTask());
	}
}
