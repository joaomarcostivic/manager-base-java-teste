package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.TipoSituacaoPublicacaoDO;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.publicacao.ILotePublicacaoAitRepository;
import com.tivic.manager.mob.lotes.repository.publicacao.LotePublicacaoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.ptc.protocolosv3.documento.ocorrencia.DocumentoOcorrenciaBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ConfirmaPublicacao {
	
	private CustomConnection customConnection;
	private LotePublicacaoRepository lotePublicacaoRepository;
	private ILotePublicacaoAitRepository lotePublicacaoAitRepository;
	private IAitMovimentoService aitMovimentoServices;
	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	private DocumentoRepository documentoRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private List<Documento> documentosPublicados;
	private List<AitMovimento> aitMovimentoJulgamentoList;
	private LoteRepository loteRepository;

	public ConfirmaPublicacao(CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		this.lotePublicacaoRepository = (LotePublicacaoRepository) BeansFactory.get(LotePublicacaoRepository.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.lotePublicacaoAitRepository = (ILotePublicacaoAitRepository) BeansFactory.get(ILotePublicacaoAitRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		this.documentosPublicados = new ArrayList<Documento>();
		this.aitMovimentoJulgamentoList = new ArrayList<AitMovimento>();
	}
	
	public LotePublicacao confirmar(int cdLotePublicacao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception {
		LotePublicacao lotePublicacao = this.lotePublicacaoRepository.get(cdLotePublicacao, this.customConnection);
		if (lotePublicacao.getDtPublicacao() != null) {
			throw new ValidacaoException("Já houve confirmação desta publicação.");
		} 
		dataValidate(lotePublicacao.getCdLote(), dtConfirmacao);
		lotePublicacao.setDtPublicacao(dtConfirmacao);
		pegarDadosPublicacao(lotePublicacao);
		salvarDtMovimentoPublicado(lotePublicacao, dtConfirmacao);
		lancarOcorrenciaPublicacaoEnviada(cdUsuario);
		this.lotePublicacaoRepository.update(lotePublicacao, customConnection);
		return lotePublicacao;
	}
	
	private void pegarDadosPublicacao(LotePublicacao lotePublicacao) throws Exception {
		SearchCriterios searchCriterios1 = new SearchCriterios();
		searchCriterios1.addCriteriosEqualInteger("cd_lote_publicacao", lotePublicacao.getCdLotePublicacao());
		List<LotePublicacaoAit> lotePublicacaoAitList = this.lotePublicacaoAitRepository.find(searchCriterios1, this.customConnection);
		int tpMovimento = new DocumentoPublicacaoMap().getCodMovimento(lotePublicacao.getTpPublicacao());
		for (LotePublicacaoAit lotePublicacaoAit : lotePublicacaoAitList) {
			AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoComJulgamento(lotePublicacaoAit.getCdAit(), tpMovimento);
			SearchCriterios criteriosMovimento = new SearchCriterios();
			criteriosMovimento.addCriteriosEqualInteger("cd_ait", lotePublicacaoAit.getCdAit());
			criteriosMovimento.addCriteriosEqualInteger("cd_movimento", aitMovimento.getCdMovimento());
			AitMovimentoDocumento aitMovimentoDocumento = this.aitMovimentoDocumentoRepository.find(criteriosMovimento, this.customConnection).get(0);
			Documento documentoPublicado = this.documentoRepository.get(aitMovimentoDocumento.getCdDocumento(), this.customConnection);
			int tpMovimentoJulgamento = new MovimentoJulgamentoMap().get(tpMovimento, documentoPublicado.getCdSituacaoDocumento());
			AitMovimento movimentoJulgamento =  this.aitMovimentoServices.getMovimentoTpStatus(lotePublicacaoAit.getCdAit(), tpMovimentoJulgamento);
			this.documentosPublicados.add(documentoPublicado);
			this.aitMovimentoJulgamentoList.add(movimentoJulgamento);
		}
	}
	
	private void salvarDtMovimentoPublicado(LotePublicacao lotePublicacao, GregorianCalendar dtConfirmacao) throws Exception {
		for (AitMovimento movimento : this.aitMovimentoJulgamentoList) {
			movimento.setDtPublicacaoDo(dtConfirmacao);
			movimento.setStPublicacaoDo(TipoSituacaoPublicacaoDO.PUBLICADO_DO.getKey());
			this.aitMovimentoRepository.update(movimento, customConnection);
			if (movimento.getTpStatus() == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() || movimento.getTpStatus() == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey())
				lotePublicacao.setStLote(LoteImpressaoSituacao.AGUARDANDO_ENVIO);
		}
	}
	
	private void lancarOcorrenciaPublicacaoEnviada(int cdUsuario) throws ValidacaoException, Exception {
		for (Documento documento : this.documentosPublicados) {
			DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrenciaBuilder()
					.addCdDocumento(documento.getCdDocumento())
					.addCdTipoOcorrencia(ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_ENVIADO", 0))
					.addCdUsuario(cdUsuario)
					.addDtOcorrencia(new GregorianCalendar())
					.build();
			this.documentoOcorrenciaRepository.insert(documentoOcorrencia, this.customConnection);
		}
	}
	
	public void dataValidate(int cdLote, GregorianCalendar dateConfirmacao) throws Exception, ValidacaoException {
		Lote lote = this.loteRepository.get(cdLote, this.customConnection);
		GregorianCalendar dateCriacao = lote.getDtCriacao();
		dateCriacao.set(Calendar.HOUR_OF_DAY, 0);
		dateCriacao.set(Calendar.MINUTE, 0);
		dateCriacao.set(Calendar.SECOND, 0);
		dateCriacao.set(Calendar.MILLISECOND, 0);
		if (dateConfirmacao.before(dateCriacao)) {
			throw new ValidacaoException("A data de confirmação não deve ser menor que a data de criação.");
		} 
	}
	
}
