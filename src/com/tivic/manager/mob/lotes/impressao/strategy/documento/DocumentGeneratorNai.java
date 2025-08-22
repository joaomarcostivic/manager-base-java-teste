package com.tivic.manager.mob.lotes.impressao.strategy.documento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitimagem.IAitImagemService;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.infracao.TipoResponsabilidadeInfracaoEnum;
import com.tivic.manager.mob.lotes.dto.DadosDocumento;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.factory.impressao.TipoRemessaFactory;
import com.tivic.manager.mob.lotes.impressao.prazorecurso.PrazoRecursoStrategy;
import com.tivic.manager.mob.lotes.impressao.strategy.NomeTipoImpressaoNaiStrategy;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.mob.lotes.service.impressao.facades.DocumentGeneratorFacade;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.util.date.DateUtil;

public class DocumentGeneratorNai implements IDocumentGeneratorStrategy {
	private AitMovimentoRepository aitMovimentoRepository;
	private TipoRemessaCorreiosEnum tpRemessa;
	private IAitImagemService aitImagemService;
	private LoteRepository loteRepository;
	private IArquivoService arquivoService;
	private CustomConnection customConnection;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private IParametroRepository parametroRepository;
	private ILoteImpressaoService loteImpressaoService;
	private IColaboradorService colaboradorService;
	
	public DocumentGeneratorNai(TipoRemessaCorreiosEnum tpRemessa) throws Exception {
		this.tpRemessa = tpRemessa;
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);

	}
	
	@Override
	public byte[] generate(LoteImpressao loteImpressao, String idLote, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
	    List<String> criterios = new ArrayList<>(Arrays.asList(
			"MOB_CD_ORGAO_AUTUADOR",
	        "MOB_IMPRESSOS_DS_TEXTO_NAI",
	        "DS_ENDERECO",
	        "NM_SUBORGAO",
	        "MOB_APRESENTACAO_CONDUTOR",
	        "MOB_PUBLICAR_AITS_NAO_ENTREGUES",
	        "mob_apresentar_observacao",
	        "MOB_CORREIOS_SG_CLIENTE"
	    ));
	    
	    DadosDocumento dadosDocumento = TipoRemessaFactory.getStrategy(this.tpRemessa)
	    	    .getDadosDocumento(loteImpressao, customConnection)
	    	    .addParameters(criterios, customConnection)
	    	    .setArquivoListaPostagem(customConnection)
	    	    .criar(); 

    	if (dadosDocumento.getNotificacoes().size() >= 2) {
    		dadosDocumento.getCriterios().addParametros("IS_NOT_SEGUNDA_VIA", true);
    	}
    	
    	DocumentGeneratorFacade documentGeneratorFacade = new DocumentGeneratorFacade(loteImpressao);
    	for(Notificacao notificacao : dadosDocumento.getNotificacoes()) {
    		AitMovimento aitMovimento = aitMovimentoRepository.getByStatus(notificacao.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
    		notificacao.setDtMovimento(aitMovimento.getDtMovimento());
    		if (notificacao.getDtPrazoDefesa() == null) {
    			notificacao.setDtPrazoDefesa(new PrazoRecursoStrategy()
        				.getStrategy(TipoStatusEnum.DEFESA_PREVIA.getKey())
        				.gerarPrazo(notificacao.getCdAit(), customConnection));
    		}
	    	String nmDocumentoImpressao = new NomeTipoImpressaoNaiStrategy().buscar(notificacao.getIdAitGeradora(), this.tpRemessa);
	    	Map<String, Object> assinaturaAutoridade = colaboradorService.buscarAssinaturaAutoridade();
    		dadosDocumento.setNmDocumento(nmDocumentoImpressao);
    		dadosDocumento.getCriterios().addParametros("MOB_IMAGEM_VEICULO", capturarImagemVeiculo(notificacao.getCdAit()));
    		dadosDocumento.getCriterios().addParametros("MOB_INFORMACOES_ADICIONAIS_NAI", verificarTipoResponsabilidadeInfracao(notificacao.getTpResponsabilidade()));
    		dadosDocumento.getCriterios().addParametros("LG_GERACAO_VIA_UNICA",  Boolean.TRUE.equals(loteImpressao.getLgGeracaoViaUnica()));
    		dadosDocumento.getCriterios().addParametros("NM_REMESSA_CORREIOS", this.tpRemessa.getValue());
    		dadosDocumento.getCriterios().addParametros("TP_MODELO_NOTIFICACAO", Integer.parseInt(ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nai")));
    		dadosDocumento.getCriterios().addParametros("MOB_TP_DOCUMENTO", TipoStatusEnum.NAI_ENVIADO.getKey());
    		dadosDocumento.getCriterios().addParametros("ASSINATURA_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
    				? (byte[]) assinaturaAutoridade.get("assinaturaAutoridade") : null);
    		dadosDocumento.getCriterios().addParametros("NM_AUTORIDADE", (assinaturaAutoridade != null && !assinaturaAutoridade.isEmpty()) 
    				? (String) assinaturaAutoridade.get("nomeAutoridade") : null);
    		documentGeneratorFacade.generateNotificacao(notificacao, dadosDocumento.getCriterios(), nmDocumentoImpressao, this.tpRemessa);
    		salvarDocumentoLoteAit(notificacao.getCdLoteImpressao(), notificacao.getCdAit(), customConnection);
    	    criterios.remove("MOB_IMAGEM_VEICULO");
		}
		
    	documentGeneratorFacade.updateStatusEtiquetas(TipoStatusEnum.NAI_ENVIADO, dadosDocumento.getEtiquetas(), customConnection);
        byte[] pdfBytes = documentGeneratorFacade.gerarDocumentoLote(idLote);

        salvarDocumentoLote(loteImpressao, pdfBytes);
        return pdfBytes;
	}
	
	private void salvarDocumentoLoteAit(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		LoteImpressaoAit loteImpressaoAit = this.loteImpressaoAitRepository.get(cdLoteImpressao, cdAit, customConnection);
		loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
		this.loteImpressaoAitRepository.update(loteImpressaoAit, customConnection);
	}
	
	private void salvarDocumentoLote(LoteImpressao loteImpressao, byte[] bytePdfNotificacao) throws Exception {
		Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), new CustomConnection());
		Arquivo arquivoNotificacao = new ArquivoBuilder()
				.setBlbArquivo(bytePdfNotificacao)
				.setCdUsuario(lote.getCdCriador())
				.setNmArquivo("LOTE_NOTIFICACAO_NA_" + loteImpressao.getCdLoteImpressao() + ".pdf" )
				.setNmDocumento("Lote de Notificações NA")
				.setDtArquivamento(DateUtil.getDataAtual())
				.setDtCriacao(DateUtil.getDataAtual())
				.build();
		this.arquivoService.save(arquivoNotificacao, this.customConnection);
		lote.setCdArquivo(arquivoNotificacao.getCdArquivo());
		loteImpressao.setStLote(LoteImpressaoSituacao.EM_IMPRESSAO);
		this.loteImpressaoService.save(loteImpressao, customConnection);
		this.loteRepository.update(lote, this.customConnection);
	}
	
	private byte[] capturarImagemVeiculo(int cdAit) throws Exception {
		AitImagem aitImagem = aitImagemService.getFromAit(cdAit);
		if (aitImagem.getBlbImagem() != null) {
			return aitImagem.getBlbImagem();
		}
		return null;
	}
	
	private String verificarTipoResponsabilidadeInfracao(int tpResponsabilidade) throws Exception {
		if (tpResponsabilidade == TipoResponsabilidadeInfracaoEnum.MULTA_RESPONSABILIDADE_PROPRIETARIO.getKey()) {
	        return parametroRepository.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_RESPONSABILIDADE_PROPRIETARIO");
	    }
	    return parametroRepository.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NAI");
	}
	
}
