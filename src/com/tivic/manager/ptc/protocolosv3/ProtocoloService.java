package com.tivic.manager.ptc.protocolosv3;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.arquivos.repository.IFileSystemRepository;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.manager.ptc.portal.vagaespecial.ArquivoDTO;
import com.tivic.manager.ptc.protocolos.documentoarquivo.DocumentoArquivoRepository;
import com.tivic.manager.ptc.protocolosv3.builders.BaseProtocoloDTOBuilder;
import com.tivic.manager.ptc.protocolosv3.builders.BaseUpdateProtocoloDTOBuilder;
import com.tivic.manager.ptc.protocolosv3.builders.ProtocoloSearchPaginatorBuilder;
import com.tivic.manager.ptc.protocolosv3.builders.RecursoJariListBuilder;
import com.tivic.manager.ptc.protocolosv3.cancelamento.CancelaEntradaProtocolo;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDiretorioEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.protocolosv3.factories.ProtocoloParametroFactory;
import com.tivic.manager.ptc.protocolosv3.publicacao.ProtocoloPublicacaoPendenteDto;
import com.tivic.manager.ptc.protocolosv3.recursos.IProtocoloRecursoServices;
import com.tivic.manager.ptc.protocolosv3.recursos.ProtocoloRecursoFactory;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.manager.ptc.protocolosv3.validators.ProtocoloValidator;
import com.tivic.manager.ptc.protocolosv3.validators.builders.AitMovimentoDocumentoBuilder;
import com.tivic.manager.ptc.protocolosv3.validators.builders.ProtocoloInsertValidatorBuilder;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ProtocoloService implements IProtocoloService {

	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	protected AitMovimentoRepository aitMovimentoRepository;
	protected DocumentoRepository documentoRepository;
	private DocumentoArquivoRepository documentoArquivoRepository;
	private IArquivoRepository arquivoRepository;
	protected DocumentoAtualizaStatusAIT documentoAtualizaStatusAIT;
	private ServicoDetranServices servicoDetranServices;
	protected boolean isEnvioAutomatico;
	private IFileSystemRepository fileSystemRepository;
    private ProtocoloParametroFactory protocoloParametroFactory;
	
	final static int defesaPrevia = TipoStatusEnum.DEFESA_PREVIA.getKey();
	
	public ProtocoloService () throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		documentoArquivoRepository = (DocumentoArquivoRepository) BeansFactory.get(DocumentoArquivoRepository.class);
		this.documentoAtualizaStatusAIT = new DocumentoAtualizaStatusAIT();
		isEnvioAutomatico = ParametroServices.getValorOfParametroAsInteger("LG_LANCAR_MOVIMENTO_DOCUMENTO", 0) == 1 ? true: false;
		fileSystemRepository = (IFileSystemRepository) BeansFactory.get(IFileSystemRepository.class);
		protocoloParametroFactory = new ProtocoloParametroFactory();
	}
	
	@Override
	public PagedResponse<ProtocoloSearchDTO> find(SearchCriterios searchCriterios) throws Exception {
		Search<ProtocoloSearchDTO> searchProtocolos = searchProtocolos(searchCriterios);
		List<ProtocoloSearchDTO> protocolos = searchProtocolos.getList(ProtocoloSearchDTO.class);
		PagedResponse<ProtocoloSearchDTO> protocoloList = new ProtocoloSearchPaginatorBuilder(protocolos, 
				searchProtocolos
				.getRsm()
				.getTotal())
				.build();
		return protocoloList;
	}
	
	private Search<ProtocoloSearchDTO> searchProtocolos(SearchCriterios searchCriterios) throws Exception {
		Search<ProtocoloSearchDTO> search = new SearchBuilder<ProtocoloSearchDTO>("mob_ait_movimento A")
			.fields(" A.dt_movimento, A.tp_status, B.id_ait, B.cd_ait, B.dt_infracao, D.nr_documento, D.cd_documento, D.dt_protocolo, D.tp_documento, "
					+ " E.nm_tipo_documento, E.cd_tipo_documento, F.cd_fase, F.nm_fase, H.cd_situacao_documento, "
					+ " H.nm_situacao_documento, G.cd_apresentacao_condutor, "
					+ "	 D.dt_protocolo + INTERVAL '1 year' AS dt_prazo")
			.addJoinTable(" JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
			.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.cd_ait = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
			.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
			.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
			.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
			.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
			.addJoinTable(" LEFT JOIN ptc_apresentacao_condutor G ON (D.cd_documento = G.cd_documento)")
			.searchCriterios(searchCriterios)
			.additionalCriterias(decidirTempestividade(searchCriterios))
			.additionalCriterias(incluirBuscaPublicacao(searchCriterios))
			.additionalCriterias(incluirPrazoJulgamento(searchCriterios))
			.orderBy(" D.dt_protocolo DESC ")
			.count()
		.build();
			
		return search;
	}
	
	private String decidirTempestividade(SearchCriterios searchCriterios) throws Exception {
		ItemComparator itemComparator = searchCriterios.getAndRemoveCriterio("lgTempestividade");
		Boolean lgTempestividade = Boolean.valueOf(itemComparator.getValue());
		if (lgTempestividade) return incluirTempestividade(searchCriterios);
		return "1=1";
	}
	
	private String incluirTempestividade(SearchCriterios searchCriterios) throws Exception {
		if (searchCriterios.getCriterios().isEmpty()) {
			return "(A.tp_status != " + TipoStatusEnum.RECURSO_JARI.getKey()
					+ " AND CAST(D.dt_protocolo AS DATE) <= CAST(B.dt_prazo_defesa AS DATE)) " + "OR (A.tp_status = "
					+ TipoStatusEnum.RECURSO_JARI.getKey() + " AND CAST(D.dt_protocolo AS DATE) <=  CAST(B.dt_vencimento AS DATE))";
		}
		List<ItemComparator> criterios = searchCriterios.getCriterios();
		if (criterios.stream().anyMatch(criterio -> criterio.getColumn().equals("E.cd_tipo_documento")
				&& criterio.getValue().equals(String.valueOf(TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey())))) {
			return "(CAST(D.dt_protocolo AS DATE) <= CAST(B.dt_vencimento AS DATE))";
		}
		if (criterios.stream()
		        .anyMatch(criterio -> criterio.getColumn().equals("E.cd_tipo_documento")
		                && isInApresentacaoOuDefesa(criterio.getValue()))) {
			return "(CAST(D.dt_protocolo AS DATE) <= CAST(B.dt_prazo_defesa AS DATE))";
		}
		return null;
	}

	public static boolean isInApresentacaoOuDefesa(String value) {
	    return EnumSet.of(TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR, 
	    				  TipoDocumentoProtocoloEnum.DEFESA_PREVIA, 
	    				  TipoDocumentoProtocoloEnum.DEFESA_PREVIA_ADVERTENCIA)
	                  .stream()
	                  .anyMatch(tipo -> String.valueOf(tipo.getKey()).equals(value));
	}

	private String incluirBuscaPublicacao(SearchCriterios searchCriterios) throws Exception {
		ItemComparator movimentoPublicacao = searchCriterios.getAndRemoveCriterio("A5.tp_status");
		if (movimentoPublicacao != null ) {
 			return " A.cd_ait IN ( SELECT DISTINCT A5.cd_ait FROM mob_ait_movimento A5 "
			        + " WHERE " 
			        + " 	A5.tp_status = " + movimentoPublicacao.getValue()
			        + " 	AND A5.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
			        + "     AND NOT EXISTS ( "
					+ "				SELECT 1 FROM mob_ait_movimento A6 "
					+ "					WHERE A6.tp_status = " + (Integer.parseInt(movimentoPublicacao.getValue()) == TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey() ? 
											TipoStatusEnum.CANCELAMENTO_PUBLICACAO_RESULTADO_JARI.getKey() : TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey() )
					+ "         		AND A6.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
					+ "					AND A6.dt_movimento > A5.dt_movimento AND A6.cd_ait = A5.cd_ait "
					+ "			) "			
			        + " ) ";
 		}
		return null;
	}
	
	private String incluirPrazoJulgamento(SearchCriterios searchCriterios) throws Exception {
		ItemComparator itemComparatorPrazoInicial = searchCriterios.getAndRemoveCriterio("dtPrazoInicial");
		String dtPrazoInicial = itemComparatorPrazoInicial != null ? itemComparatorPrazoInicial.getValue() : null;
		ItemComparator itemComparatorPrazoFinal = searchCriterios.getAndRemoveCriterio("dtPrazoFinal");
		String dtPrazoFinal = itemComparatorPrazoFinal != null ?  itemComparatorPrazoFinal.getValue() : null;
	    if (dtPrazoInicial != null || dtPrazoFinal != null) {
	    	int cdTipoOcorrenciaDeferidaDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEFERIDA_DOCUMENTO", 0);
	    	int cdTipoOcorrenciaIndeferidaDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INDEFERIDA_DOCUMENTO", 0);
	        return " D.cd_documento IN ( SELECT PDO.cd_documento FROM ptc_documento_ocorrencia PDO "
	                + " WHERE PDO.cd_tipo_ocorrencia IN ("+cdTipoOcorrenciaDeferidaDocumento+", "+cdTipoOcorrenciaIndeferidaDocumento+") AND " 
	                + (dtPrazoInicial != null ? " date(PDO.dt_ocorrencia) >= '" + dtPrazoInicial + "'" : "")
	                + (dtPrazoInicial != null && dtPrazoFinal != null ? " AND " : "") 
	                + (dtPrazoFinal != null ? " date(PDO.dt_ocorrencia) <= '" + dtPrazoFinal + "'" : "") + ")";
	    } 
	    return null;
	}
	
	@Override
	public ProtocoloDTO insert(ProtocoloInsertDTO dadosProtocolo) throws Exception {
		return insert(dadosProtocolo, new CustomConnection());
	}

	@Override
	public ProtocoloDTO insert(ProtocoloInsertDTO dadosProtocolo, CustomConnection customConnection) throws Exception, ValidacaoException {
		try {
			customConnection.initConnection(true);
			ProtocoloDTO protocolo = new BaseProtocoloDTOBuilder(dadosProtocolo, customConnection)
					.ait()
					.tipoDocumento()
					.fase(dadosProtocolo.getCdSituacaoDocumento())
					.movimento()
					.build();	
			validarDocumento(protocolo, customConnection);
			aitMovimentoRepository.insert(protocolo.getAitMovimento(), customConnection);
			documentoRepository.insert(protocolo.getDocumento(), customConnection); 
			inserirAitMovimentoDocumento(protocolo, customConnection);
			boolean isAtualizaStatusAIT = this.documentoAtualizaStatusAIT.verificar(protocolo.getDocumento().getCdTipoDocumento());
			if (isAtualizaStatusAIT) {
				this.documentoAtualizaStatusAIT.atualizarInserido(protocolo, customConnection);
			}
			inserirProtocoloRecurso(protocolo, customConnection);
			for (Arquivo arquivo : dadosProtocolo.getArquivos()) {
				fileSystemRepository.insert(arquivo, TipoDiretorioEnum.PROTOCOLOS.getValue(), protocolo.getDocumento().getCdDocumento(), customConnection);
			}
			saveArquivos(protocolo, customConnection);
			customConnection.finishConnection();
			if(protocolo.getAitMovimento().getTpStatus() == TipoStatusEnum.DEFESA_PREVIA.getKey() && isEnvioAutomatico) 
				enviarDetran(protocolo.getAitMovimento());
			return protocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public ProtocoloDTO update(ProtocoloDTO protocolo) throws Exception {
		return update(protocolo, new CustomConnection());
	}

	@Override
	public ProtocoloDTO update(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		try {
			if(protocolo==null)
				throw new ValidacaoException("Erro ao salvar. Documento é nulo.");
			
			customConnection.initConnection(true);
			ProtocoloDTO novoProtocolo = new BaseUpdateProtocoloDTOBuilder(protocolo)
					.documento()
					.documentoFase()
					.build();
			documentoRepository.update(novoProtocolo.getDocumento(), customConnection);
			customConnection.finishConnection();
			return protocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public ProtocoloDTO publicar(ProtocoloDTO protocolo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProtocoloDTO publicar(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProtocoloDTO cancelar(ProtocoloDTO protocolo) throws Exception {
		return cancelar(protocolo, new CustomConnection());
	}

	@Override
	public ProtocoloDTO cancelar(ProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws ValidacaoException, Exception {
		customConnection.initConnection(true);
		try {
			if(dadosProtocolo==null) {
				throw new ValidacaoException("Erro ao cancelar. Documento é nulo.");
			}
			new CancelaEntradaProtocolo().cancelar(dadosProtocolo, customConnection);
			customConnection.finishConnection();
			return dadosProtocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	protected void validarDocumento(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception, ValidacaoException{
		ProtocoloValidator documentoValidator = new ProtocoloValidator(new ProtocoloInsertValidatorBuilder());
		documentoValidator.validate(protocolo, customConnection);
	}
	
	protected void inserirAitMovimentoDocumento(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		AitMovimentoDocumento aitMovimentoDocumento = new AitMovimentoDocumentoBuilder(protocolo).ait().movimento().documento().build();
		aitMovimentoDocumentoRepository.insert(aitMovimentoDocumento, customConnection);
	}
	
	protected void saveArquivos(ProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws ValidationException, Exception {				
		for (Arquivo arquivo : dadosProtocolo.getArquivos()) {
			arquivoRepository.insert(arquivo, customConnection);
			DocumentoArquivo documentoArquivo = new DocumentoArquivo(arquivo.getCdArquivo(), dadosProtocolo.getDocumento().getCdDocumento());
			documentoArquivoRepository.insert(documentoArquivo, customConnection);
		}		
	}
	
	protected void inserirProtocoloRecurso(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception{
		IProtocoloRecursoServices protocoloRecursoServices = new ProtocoloRecursoFactory().gerarServico(protocolo.getAitMovimento().getTpStatus());
		protocoloRecursoServices.insertProtocolo(protocolo, customConnection);
	}

	@Override
	public void enviarDetran(AitMovimento aitMovimento) throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		aitMovimentoList.add(aitMovimento);
		List<ServicoDetranDTO> remessa = servicoDetranServices.remessa(aitMovimentoList);
		verificarRetorno(remessa.get(0));
	}
	
	private void verificarRetorno(ServicoDetranDTO servicoDetranObjeto) throws ValidacaoException {
		if (servicoDetranObjeto.getCodigoRetorno() > 0)
			throw new ValidacaoException("Erro ao enviar movimento. Por favor verifique os envios do Detran.");
	}

	@Override
	public Documento getDocumento(int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Documento documento = getDocumento(cdDocumento, customConnection);
			customConnection.finishConnection();			
			return documento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Documento getDocumento(int cdDocumento, CustomConnection customConnection) throws Exception {
		Documento documento = this.documentoRepository.get(cdDocumento, customConnection);
		return documento;
	}

	@Override
	public PagedResponse<ProtocoloSearchDTO> findProtocoloCredencialEstacionamento(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			PagedResponse<ProtocoloSearchDTO> protocolos = findProtocoloCredencialEstacionamento(searchCriterios, customConnection);
			customConnection.finishConnection();
			return protocolos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PagedResponse<ProtocoloSearchDTO> findProtocoloCredencialEstacionamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		int codigoIdoso = this.protocoloParametroFactory.getStrategy().getValorOfParametroAsInt("MOB_CD_TIPO_DOCUMENTO_CARTAO_IDOSO", customConnection);
		int codigoPcd = this.protocoloParametroFactory.getStrategy().getValorOfParametroAsInt("MOB_CD_TIPO_DOCUMENTO_CARTAO_PCD", customConnection);
		Search<ProtocoloSearchDTO> search = new SearchBuilder<ProtocoloSearchDTO>("ptc_documento D")
				.fields(" D.nr_documento, D.cd_documento, D.dt_protocolo, D.tp_documento, "
						+ " E.nm_tipo_documento, E.cd_tipo_documento, F.nm_fase, H.cd_situacao_documento, "
						+ " H.nm_situacao_documento, I.dt_ocorrencia + INTERVAL '5 year' AS dt_prazo")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
				.addJoinTable(" LEFT JOIN ptc_documento_ocorrencia I ON (D.cd_documento = I.cd_documento)" )
				.additionalCriterias("E.cd_tipo_documento IN " + "(" + codigoIdoso + "," + codigoPcd + ")")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy(" D.dt_protocolo DESC ")
				.count()
				.build();
		
		PagedResponse<ProtocoloSearchDTO> protocoloList = new ProtocoloSearchPaginatorBuilder(search.getList(ProtocoloSearchDTO.class), 
				search.getRsm().getTotal())
				.build();
		
		return protocoloList;
	}
	
	@Override
	public PagedResponse<ProtocoloSearchDTO> findJari(SearchCriterios searchCriterios ) throws Exception {
		Search<ProtocoloSearchDTO> searchProtocolos = searchProtocolosJari(searchCriterios);
	    Double mediaTempoJulgamento = calcularMediaTempoJulgamento(searchCriterios);
		List<ProtocoloSearchDTO> protocolos = searchProtocolos.getList(ProtocoloSearchDTO.class);
		PagedResponse<ProtocoloSearchDTO> protocoloList = new RecursoJariListBuilder(protocolos, 
				searchProtocolos
				.getRsm()
				.getTotal(), mediaTempoJulgamento)
				.build();
		if(protocoloList.getItems().isEmpty())
			throw new NoContentException("Nenhum protocolo encontrado");
		return protocoloList;
	}
	
	private Search<ProtocoloSearchDTO> searchProtocolosJari(SearchCriterios searchCriterios) throws Exception {
	    int codigoJulgado = ParametroServices.getValorOfParametroAsInteger("CD_FASE_JULGADO", 0);
	    Search<ProtocoloSearchDTO> search = new SearchBuilder<ProtocoloSearchDTO>("mob_ait_movimento A")
	            .fields(" EXTRACT(MONTH FROM D.dt_protocolo) AS nr_mes, EXTRACT(YEAR FROM D.dt_protocolo) AS nr_ano, "
	            		+ " COUNT(B.cd_ait) AS qtd_ait, SUM(COUNT(B.cd_ait)) OVER () AS total_Aits, "
	            		+ " SUM(CASE WHEN D1.cd_fase = 5 THEN 1 ELSE 0 END) AS qtd_jari_julgada, "
	            		+ "	MAX(H.dt_ata) AS dt_julgamento, "
	            		+ "	EXTRACT(MONTH FROM MAX(H.dt_ata)) AS mes_julgamento	")
	            .addJoinTable(" LEFT OUTER JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
	            .addJoinTable(" LEFT OUTER JOIN mob_ait_movimento_documento C ON (C.cd_ait = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
	            .addJoinTable(" LEFT OUTER JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
	            .addJoinTable(" LEFT OUTER JOIN ptc_documento D1 ON (C.cd_documento = D1.cd_documento AND D1.cd_fase = " + codigoJulgado + " ) ")
	            .addJoinTable(" LEFT JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
	            .addJoinTable(" LEFT OUTER JOIN ptc_situacao_documento F ON (F.cd_situacao_documento = D.cd_situacao_documento) ")
	            .addJoinTable(" LEFT OUTER JOIN ptc_recurso G ON (G.cd_documento = D.cd_documento) ")
	            .addJoinTable(" LEFT OUTER JOIN ptc_ata H ON (G.cd_ata = H.cd_ata) ")
	            .searchCriterios(searchCriterios)
	            .groupBy(" nr_ano, nr_mes ")
	            .orderBy(" nr_ano, nr_mes ")
	            .build();
	    return search;
	}
	
	private Double calcularMediaTempoJulgamento(SearchCriterios searchCriterios) throws Exception {
	    int codigoJulgado = ParametroServices.getValorOfParametroAsInteger("CD_FASE_JULGADO", 0);
	    searchCriterios.addCriteriosEqualInteger("D.cd_fase", codigoJulgado, true);
		searchCriterios.getAndRemoveCriterio("A.tp_status");
		searchCriterios.getAndRemoveCriterio("B.dt_movimento_inicial");
	    Search<ProtocoloPublicacaoPendenteDto> search = new SearchBuilder<ProtocoloPublicacaoPendenteDto>("ptc_documento D")
	            .fields(" DISTINCT ON (D.cd_documento, D.dt_protocolo) D.cd_documento, "
	                    + " D.dt_protocolo, H.dt_ata AS dt_julgamento")
	            .addJoinTable(" JOIN ptc_documento_ocorrencia B ON (B.cd_documento = D.cd_documento) ")
	            .addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.cd_documento = D.cd_documento) ")
	            .addJoinTable(" JOIN mob_ait A ON (A.cd_ait = C.cd_ait) ")
	            .addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
	            .addJoinTable(" JOIN ptc_situacao_documento F ON (F.cd_situacao_documento = D.cd_situacao_documento) ")
	            .addJoinTable(" LEFT OUTER JOIN ptc_recurso G ON (G.cd_documento = D.cd_documento) ")
	            .addJoinTable(" LEFT OUTER JOIN ptc_ata H ON (G.cd_ata = H.cd_ata) ")
	            .searchCriterios(searchCriterios)
	            .orderBy(" D.dt_protocolo DESC ")
	            .count()
	            .build();

	    List<ProtocoloPublicacaoPendenteDto> protocoloPublicacaoPendenteDtoList = search.getList(ProtocoloPublicacaoPendenteDto.class);

	    if (protocoloPublicacaoPendenteDtoList.isEmpty()) {
	        throw new NoContentException("Nenhum Registro encontrado.");
	    }

	    long somaDiferencaDias = 0;
	    int contadorElementos = 0;

	    for (ProtocoloPublicacaoPendenteDto dto : protocoloPublicacaoPendenteDtoList) {
	        if (dto.getDtJulgamento() != null && dto.getDtProtocolo() != null) {
	            Instant instantDtProtocolo = dto.getDtProtocolo().toInstant();
	            Instant instantDtJulgamento = dto.getDtJulgamento().toInstant();

	            LocalDate dataProtocolo = instantDtProtocolo.atZone(ZoneId.systemDefault()).toLocalDate();
	            LocalDate dataJulgamento = instantDtJulgamento.atZone(ZoneId.systemDefault()).toLocalDate();

	            long diferencaDias = ChronoUnit.DAYS.between(dataProtocolo, dataJulgamento);
	            somaDiferencaDias += diferencaDias;
	            contadorElementos++;
	        }
	    }

	    if (contadorElementos > 0) {
	        double mediaDiferencaDias = (double) somaDiferencaDias / contadorElementos;
	        mediaDiferencaDias = Math.round(mediaDiferencaDias * 10.0) / 10.0;
	        return mediaDiferencaDias;
	    } else {
	        throw new NoContentException("Nenhum elemento válido para calcular a média.");
	    }
	}

	@Override
	public ProtocoloSearchDTO getProtocoloCredencialEstacionamento(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			ProtocoloSearchDTO protocolo = getProtocoloCredencialEstacionamento(searchCriterios, customConnection);
			customConnection.finishConnection();
			return protocolo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ProtocoloSearchDTO getProtocoloCredencialEstacionamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<ProtocoloSearchDTO> search = new SearchBuilder<ProtocoloSearchDTO>("ptc_documento D")
				.fields(" D.nr_documento, D.cd_documento, D.dt_protocolo, D.tp_documento, D.cd_tipo_documento, "
						+ " E.nm_tipo_documento, E.cd_tipo_documento, F.nm_fase, H.cd_situacao_documento, "
						+ " H.nm_situacao_documento, J.nm_pessoa, J.nm_email, J.nr_telefone1, L.nr_cpf, M.txt_ocorrencia")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
				.addJoinTable(" JOIN ptc_documento_pessoa I ON (D.cd_documento = I.cd_documento)" )
				.addJoinTable(" LEFT JOIN grl_pessoa J ON (I.cd_pessoa = J.cd_pessoa)" )
				.addJoinTable(" LEFT JOIN grl_pessoa_fisica L ON (J.cd_pessoa = L.cd_pessoa)" )
				.addJoinTable(" LEFT JOIN ptc_documento_ocorrencia M ON (D.cd_documento = M.cd_documento)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy(" D.dt_protocolo DESC ")
				.count()
			.build();
		
		return search.getList(ProtocoloSearchDTO.class).get(0);
	}

	@Override
	public List<ArquivoDTO> getArquivoProtocolo(int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			List<ArquivoDTO> arquivoProtocolo = getArquivoProtocolo(cdDocumento, customConnection);
			customConnection.finishConnection();
			return arquivoProtocolo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<ArquivoDTO> getArquivoProtocolo(int cdDocumento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento);
		Search<ArquivoDTO> search = new SearchBuilder<ArquivoDTO>("ptc_documento A")
				.fields("C.cd_arquivo, C.nm_arquivo, C.dt_arquivamento, C.cd_tipo_arquivo, D.nm_tipo_arquivo")
				.addJoinTable(" JOIN ptc_documento_arquivo B ON (A.cd_documento = B.cd_documento) ")
				.addJoinTable(" JOIN grl_arquivo C ON (B.cd_arquivo = C.cd_arquivo) ")
				.addJoinTable(" JOIN grl_tipo_arquivo D ON (D.cd_tipo_arquivo = C.cd_tipo_arquivo)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		
		return search.getList(ArquivoDTO.class);
	}
}
