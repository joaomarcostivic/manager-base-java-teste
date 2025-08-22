package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.ptc.protocolosv3.IProtocoloService;
import com.tivic.manager.ptc.protocolosv3.documento.ocorrencia.DocumentoOcorrenciaBuilder;
import com.tivic.manager.ptc.protocolosv3.publicacao.cancelamento.CancelaPublicacaoJari;
import com.tivic.manager.ptc.protocolosv3.publicacao.relatorios.GerarRelatorioProcessos;
import com.tivic.manager.ptc.protocolosv3.publicacao.validations.PublicacaoValidations;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class PublicacaoProtocoloService implements IPublicacaoProtocoloService {
	
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private IArquivoService arquivoService;
	private IAitMovimentoService aitMovimentoService;
	private IProtocoloService protocoloService;
	private AitMovimentoRepository aitMovimentoRepository;
	private LoteRepository loteRepository;

	public PublicacaoProtocoloService() throws Exception {
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.protocoloService = (IProtocoloService) BeansFactory.get(IProtocoloService.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
	}
	
	@Override
	public PagedResponse<LotePublicacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LotePublicacaoDto> publicacoesSearch = buscarArquivosPublicados(searchCriterios, customConnection);
			if (publicacoesSearch.getList(LotePublicacaoDto.class).isEmpty()) {
				throw new ValidacaoException("Nenhum Registro encontrado.");
			}
			List<LotePublicacaoDto> lotePublicacaoDtoList = publicacoesSearch.getList(LotePublicacaoDto.class);
			lotePublicacaoDtoList = setandoSituacaoDocumento(lotePublicacaoDtoList);
			return new PagedResponse<LotePublicacaoDto>(lotePublicacaoDtoList, publicacoesSearch.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private List<LotePublicacaoDto> setandoSituacaoDocumento(List<LotePublicacaoDto> lotePublicacaoDtoList) {
	    int deferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
	    List<LotePublicacaoDto> list = new ArrayList<LotePublicacaoDto>();
	    for (LotePublicacaoDto lotePublicacaoDto : lotePublicacaoDtoList) {
	      String julgamento = (lotePublicacaoDto.getCdSituacaoDocumento() == deferido) ? "Deferido" : "Indeferido";
	      lotePublicacaoDto.setStJulgamento(julgamento);
	      list.add(lotePublicacaoDto);
	    } 
	    return list;
	}
	
	@Override
	public Search<LotePublicacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios,	CustomConnection customConnection) throws Exception {
		String codsPublicacaoProtocolos = String.valueOf(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_JARI.getKey())
				+ ", " + String.valueOf(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA.getKey()) + ", "
				+ String.valueOf(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_DEFESA_ADVERTENCIA.getKey());
		searchCriterios.addCriterios("A.tp_publicacao", codsPublicacaoProtocolos, ItemComparator.IN, Types.INTEGER);
		Search<LotePublicacaoDto> publicacoes = new SearchBuilder<LotePublicacaoDto>("mob_lote_publicacao A")
				.fields("DISTINCT ON(A.cd_lote_publicacao) A.cd_lote_publicacao, A.tp_publicacao, G.dt_criacao, A.dt_publicacao, B.cd_pessoa, C.nm_pessoa AS nm_usuario, F.cd_situacao_documento")
				.addJoinTable("JOIN grl_lote G ON (G.cd_lote = A.cd_lote)")
				.addJoinTable("JOIN seg_usuario B ON(B.cd_usuario = G.cd_criador)")
			    .addJoinTable("JOIN grl_pessoa C ON(C.cd_pessoa = B.cd_pessoa)")
			    .addJoinTable("JOIN mob_lote_publicacao_ait D ON (A.cd_lote_publicacao = D.cd_lote_publicacao)")
			    .addJoinTable("JOIN mob_ait_movimento_documento E ON (E.cd_ait = D.cd_ait)")
			    .addJoinTable("JOIN ptc_documento F ON (F.cd_documento = E.cd_documento)")
				.searchCriterios(searchCriterios)
				.orderBy(" A.cd_lote_publicacao DESC, G.dt_criacao DESC ")
			    .count()
			.build();
		return publicacoes;
	}
	
	@Override
	public PagedResponse<ProtocoloPublicacaoPendenteDto> buscarPendentesPublicacao(SearchCriterios searchCriterios) throws Exception {
		int ocorrenciaPublicacaoGerada = ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_GERADO", 0);
		int ocorrenciaPublicacaoEnviada = ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_ENVIADO", 0);
		String codsPublicacaoOcorrencia = String.valueOf(ocorrenciaPublicacaoGerada) + ", " + String.valueOf(ocorrenciaPublicacaoEnviada);
		int codigoJulgado = ParametroServices.getValorOfParametroAsInteger("CD_FASE_JULGADO", 0);
		searchCriterios.addCriteriosEqualInteger("A.cd_fase", codigoJulgado, true);
		Search<ProtocoloPublicacaoPendenteDto> search = new SearchBuilder<ProtocoloPublicacaoPendenteDto>("ptc_documento A")
				.fields(" DISTINCT ON (A.cd_documento, A.dt_protocolo) A.cd_documento, A.nr_documento as nr_protocolo, "
					   + " A.dt_protocolo, A.cd_situacao_documento, B.cd_ocorrencia, B.dt_ocorrencia, B.cd_tipo_ocorrencia, C.cd_ait, D.id_ait, "
					   + " D.nr_placa, D.dt_infracao, E.nm_tipo_documento, E.id_tipo_documento, F.nm_situacao_documento as nm_julgamento, H.dt_ata AS dt_julgamento, H.id_ata")
				.addJoinTable(" JOIN ptc_documento_ocorrencia B ON (B.cd_documento = A.cd_documento) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.cd_documento = A.cd_documento) ")
				.addJoinTable(" JOIN mob_ait D ON (D.cd_ait = C.cd_ait) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = A.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_situacao_documento F ON (F.cd_situacao_documento = A.cd_situacao_documento) ")
				.addJoinTable(" LEFT OUTER JOIN ptc_recurso G ON (G.cd_documento = A.cd_documento) ")
				.addJoinTable(" LEFT OUTER JOIN ptc_ata H ON (G.cd_ata = H.cd_ata) ")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" ( " 
						+ "	 A.cd_documento NOT IN(SELECT Z.cd_documento"  
						+ "  FROM ptc_documento_ocorrencia Z"  
						+ "	 WHERE Z.cd_tipo_ocorrencia IN ( "+ codsPublicacaoOcorrencia + " )"
						+ "	) "
						+ " AND C.cd_ait IN(SELECT I3.cd_ait "  
						+ "		FROM mob_ait_movimento I3 " 
						+ "			WHERE(I3.tp_status IN (  "
						+ "					" + TipoStatusEnum.DEFESA_DEFERIDA.getKey() + ", "
						+ "					" + TipoStatusEnum.DEFESA_INDEFERIDA.getKey() + ", "
						+ "					" + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + ", "
						+ "					" + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() + ", "
						+ "					" + TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", "
						+ "					" + TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()
						+ "					)"
						+ "				) AND I3.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
						+ " )" 
						+ " OR A.CD_DOCUMENTO IN (" 
						+ " SELECT Z2.CD_DOCUMENTO"  
						+ " FROM PTC_DOCUMENTO_OCORRENCIA Z2"  
						+ " JOIN (SELECT CD_DOCUMENTO, MAX(DT_OCORRENCIA) AS MAX_DT_OCORRENCIA"  
						+ "		FROM PTC_DOCUMENTO_OCORRENCIA"  
						+ "			WHERE CD_TIPO_OCORRENCIA IN ("+ codsPublicacaoOcorrencia + ")" 
						+ "			GROUP BY CD_DOCUMENTO) Z3 ON Z2.CD_DOCUMENTO = Z3.CD_DOCUMENTO "  
						+ "				AND Z2.DT_OCORRENCIA = Z3.MAX_DT_OCORRENCIA "  
						+ "										WHERE Z2.CD_TIPO_OCORRENCIA IN ("+ codsPublicacaoOcorrencia + ") "  
						+ "						AND (NOT EXISTS (SELECT 1 FROM MOB_AIT_MOVIMENTO I "  
						+ "					 		WHERE I.TP_STATUS IN (" + TipoStatusEnum.DEFESA_DEFERIDA.getKey() + ", " 
						+ 											   TipoStatusEnum.DEFESA_INDEFERIDA.getKey() + ", " 
						+ 						 					   TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + ", "  
						+ 				 							   TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() + ", " 
						+ 					 						   TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", "  
						+ 					 						   TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()  
						+ 											")" 
						+ "					 AND I.DT_MOVIMENTO > COALESCE((SELECT MAX(I2.DT_MOVIMENTO)"  
						+ "												  	FROM MOB_AIT_MOVIMENTO I2  "  
						+ "													WHERE I2.TP_STATUS IN (" + TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", " 
						+ 											   									TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey() + ", " 
						+ 						 					   									TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey() + ", "  
						+ 				 							   									TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey() + ", " 
						+ 					 						   									TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", "  
						+ 					 						   									TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey()  
						+ "													)), '1900-01-01')"
						+ "										)"
						+ "							)"
						+ " 					) "
						+ ") ")
				.orderBy(" A.dt_protocolo DESC ")
				.count()
				.build();
		List<ProtocoloPublicacaoPendenteDto> protocoloPublicacaoPendenteDtoList = search.getList(ProtocoloPublicacaoPendenteDto.class);
		if (protocoloPublicacaoPendenteDtoList.isEmpty()) {
			throw new NoContentException("Nenhum Registro encontrado.");
		}
		PagedResponse<ProtocoloPublicacaoPendenteDto> pagedProtocoloPublicacaoPendenteDto = 
				new PagedResponse<ProtocoloPublicacaoPendenteDto>(protocoloPublicacaoPendenteDtoList, search.getRsm().getTotal());
		return pagedProtocoloPublicacaoPendenteDto;
	}
	
	@Override
	public Arquivo publicarProtocolo(int cdUsuario, String idTipoDocumento, int stJulgamento, List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LotePublicacao lotePublicacao = new GeraLotePublicacaoProtocolo(cdUsuario, idTipoDocumento, publicaProtocoloDtoList)
					.gerarLoteImpressao(customConnection);
			for (ProtocoloPublicacaoPendenteDto publicaProtocoloDto : publicaProtocoloDtoList) {
				lancarOcorrenciaPublicacaoGerada(publicaProtocoloDto.getCdDocumento(), cdUsuario, customConnection);
			}
			byte[] printProtocolosPublicados = new GerarRelatorioProcessos().gerar(idTipoDocumento, stJulgamento,publicaProtocoloDtoList);
			Arquivo arquivoPublicacao = salvarDocumentoLote(lotePublicacao.getCdLotePublicacao(), printProtocolosPublicados, cdUsuario, customConnection);
			vincularArquivoPublicacao(lotePublicacao.getCdLote(), arquivoPublicacao.getCdArquivo(), customConnection);
			customConnection.finishConnection();
			return arquivoPublicacao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void lancarOcorrenciaPublicacaoGerada(int cdDocumento, int cdUsuario, CustomConnection customConnection) throws ValidacaoException, Exception {
		DocumentoOcorrencia documentoOcorrencia = new DocumentoOcorrenciaBuilder()
				.addCdDocumento(cdDocumento)
				.addCdTipoOcorrencia(ParametroServices.getValorOfParametroAsInteger("MOB_TIPO_OCORRENCIA_ARQUIVO_PUBLICACAO_GERADO", 0))
				.addCdUsuario(cdUsuario)
				.addDtOcorrencia(new GregorianCalendar())
				.build();
		this.documentoOcorrenciaRepository.insert(documentoOcorrencia, customConnection);
	}
	
	private Arquivo salvarDocumentoLote(int cdLotePublicacao, byte[] bytePdfPublicacao, int cdUsuario, CustomConnection customConnection) throws Exception {
		Arquivo arquivoPublicacao = new ArquivoBuilder()
				.setBlbArquivo(bytePdfPublicacao)
				.setCdUsuario(cdUsuario)
				.setNmArquivo("LOTE_PUBLICACAO_PROTOCOLOS_" + cdLotePublicacao + ".doc" )
				.setNmDocumento("Lote de Publicação Protocolos")
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_PUBLICACAO_PROTOCOLOS")))
				.build();
		this.arquivoService.save(arquivoPublicacao, customConnection);
		return arquivoPublicacao;
	}
	
	private void vincularArquivoPublicacao(int cdLote, int cdArquivo, CustomConnection customConnection) throws Exception {
		Lote lote = this.loteRepository.get(cdLote, customConnection);
		lote.setCdArquivo(cdArquivo);
		this.loteRepository.update(lote, customConnection);
	}
	
	@Override
	public LotePublicacaoDto confirmarPublicacao(int cdLotePublicacao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LotePublicacao lotePublicacao = new ConfirmaPublicacao(customConnection)
					.confirmar(cdLotePublicacao, dtConfirmacao, cdUsuario);
			customConnection.finishConnection();
			Lote lote = this.loteRepository.get(lotePublicacao.getCdLote(), customConnection);
			LotePublicacaoDto lotePublicacaoDto = new LotePublicacaoDto();
			lotePublicacaoDto.setCdLotePublicacao(lotePublicacao.getCdLotePublicacao());
			lotePublicacaoDto.setDtPublicacao(lotePublicacao.getDtPublicacao());
			lotePublicacaoDto.setTpPublicacao(lotePublicacao.getTpPublicacao());
			lotePublicacaoDto.setDtCriacao(lote.getDtCriacao());
			return lotePublicacaoDto;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void cancelarPublicacao(int cdAit, int cdMovimento, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			AitMovimento aitMovimento = getMovimentoPublicacao(cdAit, cdMovimento, customConnection);
			if (aitMovimento.getTpStatus() == TipoStatusEnum.PUBLICACAO_NAI.getKey() ||  
				aitMovimento.getTpStatus() == TipoStatusEnum.PUBLICACAO_NIP.getKey()) {
				setLgEnviadoDetran(aitMovimento, customConnection);
				return;
			}
			new CancelaPublicacaoJari().cancelar(aitMovimento, cdUsuario, customConnection);
			customConnection.finishConnection();
			AitMovimento movimentoCancelamento = aitMovimentoService.getMovimentoTpStatus(cdAit, TipoStatusEnum.CANCELAMENTO_PUBLICACAO_RESULTADO_JARI.getKey());
			protocoloService.enviarDetran(movimentoCancelamento);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private AitMovimento getMovimentoPublicacao (int cdAit, int cdMovimento, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = aitMovimentoRepository.get(cdMovimento, cdAit);		
		if(aitMovimento.getCdMovimento() <= 0) {
			throw new Exception("Movimento de publicação não encontrado.");
		}
		new PublicacaoValidations().validate(aitMovimento, customConnection);
		return aitMovimento;
		
	}
	
	private void setLgEnviadoDetran(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		if (aitMovimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRADO.getKey()) {
			throw new Exception("O movimento já foi enviado ao Detran e não pode ser cancelado.");
		}
		aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
		this.aitMovimentoRepository.update(aitMovimento, customConnection);
		customConnection.finishConnection();
	}
	
	@Override
	public Arquivo getArquivoLote(int cdLotePublicacao, int tpArquivo) throws Exception { 
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("C.cd_lote_publicacao", cdLotePublicacao);
		searchCriterios.addCriteriosEqualInteger("A.cd_tipo_arquivo", tpArquivo);
		List<Arquivo> arquivoList = searchArquivoLote(searchCriterios).getList(Arquivo.class);
		if(arquivoList.isEmpty()) {
			throw new NoContentException("Nenhum arquivo encontrado.");
		}
		Arquivo arquivo = arquivoList.get(0);
		return arquivo;
	}

	private Search<Arquivo> searchArquivoLote(SearchCriterios criterios) throws Exception {
		Search<Arquivo> search = new SearchBuilder<Arquivo>("grl_arquivo A")
				.addField("A.cd_arquivo, A.dt_arquivamento, A.cd_tipo_arquivo, A.blb_arquivo")
				.addJoinTable(" JOIN grl_lote B ON (B.cd_arquivo = A.cd_arquivo) ")
				.addJoinTable(" JOIN mob_lote_publicacao C ON (C.cd_lote = B.cd_lote) ")
				.searchCriterios(criterios)
				.build();
		return search;
	}
	
}
