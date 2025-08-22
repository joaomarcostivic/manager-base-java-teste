package com.tivic.manager.mob.aitmovimento;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.arquivos.repository.IFileSystemRepository;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepositoryFactory;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.ait.SituacaoAitEnum;
import com.tivic.manager.mob.ait.ValidaMovimentoCancelamento;
import com.tivic.manager.mob.ait.aitArquivo.AitArquivo;
import com.tivic.manager.mob.ait.aitArquivo.AitArquivoBuilder;
import com.tivic.manager.mob.ait.aitArquivo.IAitArquivoRepository;
import com.tivic.manager.mob.ait.aitArquivo.TipoArquivoDTO;
import com.tivic.manager.mob.ait.cancelamento.validators.CriacaoArquivoValidations;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.PublicacaoResultadoJARIBuilder;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.TipoSituacaoPublicacaoDO;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.ArquivoAitDTO;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.CancelaPagamentoAit;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.CancelaPenalidade;
import com.tivic.manager.mob.aitmovimento.validators.ValidationCancelamentoNip;
import com.tivic.manager.mob.aitmovimentodocumento.DocumentoProcesso;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDiretorioEnum;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class AitMovimentoService implements IAitMovimentoService {
	AitMovimentoRepository aitMovimentoRepository;
	ServicoDetranServices servicoDetranServices;
	private IArquivoRepository arquivoRepository;
	private IAitService aitService;
	private IAitArquivoRepository aitArquivoRepository;
	private IFileSystemRepository fileSystemRepository; 
	private AitRepositoryFactory aitRepositoryFactory;
	private AitMovimentoRepositoryFactory aitMovimentoRepositoryFactory;
	
	public final int[] DEFESAS = {
			TipoStatusEnum.DEFESA_PREVIA.getKey(),
			TipoStatusEnum.DEFESA_DEFERIDA.getKey(), 
			TipoStatusEnum.DEFESA_INDEFERIDA.getKey(),
			TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey(),
			TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(),
			TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey()
	};
			
	public AitMovimentoService() throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.aitArquivoRepository = (IAitArquivoRepository) BeansFactory.get(IAitArquivoRepository.class);
		this.fileSystemRepository = (IFileSystemRepository) BeansFactory.get(IFileSystemRepository.class);
		this.aitRepositoryFactory = new AitRepositoryFactory();
		this.aitMovimentoRepositoryFactory = new AitMovimentoRepositoryFactory();
	}
	
	public AitMovimento getMovimentoTpStatus(int cdAit, int tpStatus) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit >= com.tivic.manager.mob.AitMovimentoServices.CADASTRO_CANCELADO);
			searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus, tpStatus >= com.tivic.manager.mob.AitMovimentoServices.CADASTRO_CANCELADO);
			
			Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento")
					.fields("*")
					.searchCriterios(searchCriterios)
					.orderBy("dt_movimento DESC LIMIT 1 ")
					.build();
			if(search.getList(AitMovimento.class).size() > 0) {
				return search.getList(AitMovimento.class).get(0);
			}
			return new AitMovimento();
			
		} catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			throw new ValidacaoException("Erro SQL! AitMovimentoServices.getMovimentoTpStatus: " + sqlExpt);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			throw new ValidacaoException("Erro! AitMovimentoServices.getMovimentoTpStatus: " + e);
		} 
	}
	
	public PagedResponse<AitMovimentoDTO> findRemessa(SearchCriterios searchCriterios, boolean lgNaoEnviado) throws Exception {
		try {			
			String retornoMensagem = "SELECT E.ds_saida::jsonb FROM mob_arquivo_movimento E WHERE A.cd_ait = E.cd_ait AND A.cd_movimento = E.cd_movimento ORDER BY cd_arquivo_movimento DESC LIMIT 1";
			Search<AitMovimentoDTO> search= new SearchBuilder<AitMovimentoDTO>("mob_ait_movimento A ")
					.fields("DISTINCT A.*, A.cd_ait as cdAit, B.id_ait, COUNT(C.cd_arquivo_movimento) AS qt_arquivo_movimento, "
							+ " (" + retornoMensagem + ") AS retorno_mensagem")
					.addJoinTable("JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
					.addJoinTable("LEFT JOIN mob_arquivo_movimento C ON (A.cd_ait = C.cd_ait AND A.cd_movimento = C.cd_movimento) ")
					.searchCriterios(searchCriterios)
					.additionalCriterias("A.lg_enviado_detran = " + TipoStatusEnum.CADASTRADO.getKey()+" AND B.st_ait = "+ SituacaoAitEnum.ST_CONFIRMADO.getKey() + incluirNaoEnviado(lgNaoEnviado))
					.orderBy("A.dt_movimento DESC")
					.groupBy(" A.cd_movimento, A.cd_ait, B.id_ait ")
					.count()
					.build();

			List<AitMovimentoDTO> listAitMovimento = search.getList(AitMovimentoDTO.class);
			return new AitMovimentoDTOListBuilder(listAitMovimento, search.getRsm().getTotal()).build();
		} catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			throw new ValidacaoException("Erro SQL! AitMovimentoServices.findRemessa: " + sqlExpt);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			throw new ValidacaoException("Erro! AitMovimentoServices.findRemessa: " + e);
		} 
	}
	
	private String incluirNaoEnviado(boolean lgNaoEnviado) throws Exception {
		String naoEnviado = "";
		if (lgNaoEnviado) {
			naoEnviado = " AND C.cd_arquivo_movimento is null ";
		}
		return naoEnviado;
	}
	
	public AitMovimento getMovimentoToSuspensao(String nrProcesso, int cdAit) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("nr_processo", nrProcesso, nrProcesso != null);
			searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > -1);
			Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento")
					.fields("tp_status, cd_ait, nr_processo")
					.searchCriterios(searchCriterios)
					.additionalCriterias("(tp_status = " + com.tivic.manager.mob.AitMovimentoServices.RECURSO_CETRAN + " OR tp_status = " + com.tivic.manager.mob.AitMovimentoServices.RECURSO_JARI +" )")
					.build();
			return search.getList(AitMovimento.class).get(0);
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			throw new ValidacaoException("Erro SQL! AitMovimentoServices.getMovimentoToSuspensao: " + sqlExpt);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			throw new ValidacaoException("Erro! AitMovimentoServices.getMovimentoToSuspensao: " + e);
		}
	}
	
	public AitMovimento save(AitMovimento aitMovimento) throws Exception {
		return save(aitMovimento, new CustomConnection());
	}

	public AitMovimento save(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		try {
			if(aitMovimento==null) 
				throw new ValidacaoException("Erro ao salvar. AitMovimento é nulo");
			customConnection.initConnection(true);
			if(aitMovimento.getCdMovimento()==0 ){
				aitMovimento.setCdMovimento(aitMovimentoRepository
						.insert(aitMovimento, customConnection));
				customConnection.finishConnection();
			}
			else {
				aitMovimentoRepository.update(aitMovimento, customConnection);
				customConnection.finishConnection();
				return aitMovimento;
			}			
			return aitMovimento;
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	public List<AitMovimento> getAllDefesas(int cdAit) throws Exception {
		return searchGetAllDefesas(cdAit).getList(AitMovimento.class);
	}
	
	private Search<AitMovimento> searchGetAllDefesas(int cdAit) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.searchCriterios(searchCriteriosGetAllDefesas(cdAit))
				.build();
		return search;
	}
	
	private SearchCriterios searchCriteriosGetAllDefesas(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		searchCriterios.addCriterios("A.tp_status", Arrays.toString(DEFESAS).replace("[", "").replace("]", ""), ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}
	
	public void setDtPublicacaoDO(List<DocumentoProcesso> listDocumentoProcessos, GregorianCalendar dtPublicacao) throws Exception {
		setDtPublicacaoDO(listDocumentoProcessos, dtPublicacao, new CustomConnection());
	}
	
	public void setDtPublicacaoDO(List<DocumentoProcesso> listDocumentoProcessos, GregorianCalendar dtPublicacao, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			GregorianCalendar _dtPublicacao = dtPublicacao != null ? dtPublicacao : new GregorianCalendar();
			for(AitMovimento movimento: listDocumentoProcessos) {
				movimento.setDtPublicacaoDo(_dtPublicacao);
				movimento.setStPublicacaoDo(TipoSituacaoPublicacaoDO.PUBLICADO_DO.getKey());
				aitMovimentoRepository.update(movimento, customConnection);
				if (movimento.getTpStatus() == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() || movimento.getTpStatus() == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey())
					gerarPublicacaoResultadoJARI(movimento.getCdAit(), movimento.getNrProcesso(), dtPublicacao);
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void gerarPublicacaoResultadoJARI(int cdAit, String nrProcesso, GregorianCalendar dtPublicacao) throws Exception {
		servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
			AitMovimento aitMovimento = new PublicacaoResultadoJARIBuilder()
					.setCdAit(cdAit)
					.setNrProcesso(nrProcesso)
					.setDtPublicacaoDo(dtPublicacao)
					.build();
			aitMovimentoRepository.insert(aitMovimento, customConnection);
			aitMovimentoList.add(aitMovimento);
			customConnection.finishConnection();
			servicoDetranServices.remessa(aitMovimentoList);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void insert(AitMovimento aitMovimento) throws Exception{
		insert(aitMovimento, new CustomConnection());
	}
	
	@Override
	public void insert(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			this.aitMovimentoRepository.insert(aitMovimento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void update(AitMovimento aitMovimento) throws Exception{
		update(aitMovimento, new CustomConnection());
	}
	
	@Override
	public void update(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			this.aitMovimentoRepository.update(aitMovimento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitMovimento get(int cdMovimento, int cdAit) throws Exception{
		return get(cdMovimento, cdAit, new CustomConnection());
	}
	
	@Override
	public AitMovimento get(int cdMovimento, int cdAit, CustomConnection customConnection) throws Exception{
		return this.aitMovimentoRepository.get(cdMovimento, cdAit, customConnection);
	}
	@Override
	public List<AitMovimento> getByAit(int cdAit) throws Exception{
		return getByAit(cdAit, new CustomConnection());
	}
	
	@Override
	public List<AitMovimento> getByAit(int cdAit, CustomConnection customConnection) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		return this.aitMovimentoRepository.find(searchCriterios, customConnection);
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento")
					.fields("*")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
				.build();
				
			List<AitMovimento> aitMovimentoList = search.getList(AitMovimento.class);
			customConnection.finishConnection();
			return aitMovimentoList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AitMovimentoDTO> getMovimentos(SearchCriterios searchCriterios) throws Exception {
		return getMovimentos(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitMovimentoDTO> getMovimentos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<AitMovimentoDTO> search = new SearchBuilder<AitMovimentoDTO>("mob_ait_movimento A")
					.fields("*")
					.addJoinTable("JOIN mob_ait B ON (A.cd_ait = B.cd_ait)")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
				.build();
			List<AitMovimentoDTO> aitMovimentoList = search.getList(AitMovimentoDTO.class);
			customConnection.finishConnection();
			return aitMovimentoList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void cancelarAutuacaoAit(AitMovimento aitMovimento) throws Exception {
		cancelarAutuacaoAit(aitMovimento, new CustomConnection());
	}

	@Override
	public void cancelarAutuacaoAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			new CancelaAutuacao(aitMovimento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void cancelarRegistroAit(AitMovimento aitMovimento) throws Exception {
		cancelarRegistroAit(aitMovimento, new CustomConnection());
	}

	@Override
	public void cancelarRegistroAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			new ValidaMovimentoCancelamento(aitMovimento.getCdAit(), customConnection).cancelamento();
			new CancelaRegistro(aitMovimento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void cancelarPagamento(AitMovimento aitMovimento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			cancelarPagamento(aitMovimento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void cancelarPagamento(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		new CancelaPagamentoAit(aitMovimento, customConnection);
	}
	
	@Override
	public void cancelarNip(AitMovimento aitMovimento, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new ValidationCancelamentoNip(aitMovimento).validate(customConnection);
			cancelarNip(aitMovimento, cdUsuario, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void cancelarNip(AitMovimento aitMovimento, int cdUsuario, CustomConnection customConnection) throws Exception {
		new CancelaPenalidade(aitMovimento, cdUsuario, customConnection);
	}
	
	@Override
	public AitMovimento getUltimoMovimento(int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			AitMovimento aitMovimento = getUltimoMovimento(cdAit, customConnection);
			customConnection.finishConnection();
			return aitMovimento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitMovimento getUltimoMovimento(int cdAit, CustomConnection customConnection) throws Exception {
		AitMovimento ultimoMovimento = new AitMovimento();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		Search<AitMovimento> movimentos = new SearchBuilder<AitMovimento>("mob_ait_movimento")
			.orderBy("cd_movimento DESC")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
		.build();
		if(!movimentos.getList(AitMovimento.class).isEmpty()) {
			ultimoMovimento = movimentos.getList(AitMovimento.class).get(0);
		}
		return ultimoMovimento;
	}

	@Override
	public AitMovimento getStatusMovimento(int cdAit, int tpStatus) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			AitMovimento aitMovimento = getStatusMovimento(cdAit, tpStatus, customConnection);
			customConnection.finishConnection();
			return aitMovimento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitMovimento getStatusMovimento(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		AitMovimento statusMovimento = new AitMovimento();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus);
		Search<AitMovimento> movimentos = new SearchBuilder<AitMovimento>("mob_ait_movimento")
			.orderBy("cd_movimento DESC")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
		.build();
		if(!movimentos.getList(AitMovimento.class).isEmpty()) {
			statusMovimento = movimentos.getList(AitMovimento.class).get(0);
		}
		return statusMovimento;
	}
	
	@Override
	public AitMovimento getMovimentoComJulgamento(int cdAit, int tpStatus) throws Exception {
		int codigoSituacaoDeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		int codigoSituacaoIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus);
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.*")
				.addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_movimento = B.cd_movimento and A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("C.cd_situacao_documento IN (" + codigoSituacaoDeferida + ", " + codigoSituacaoIndeferida + ")")
				.build();
		if(search.getList(AitMovimento.class).size() > 0) {
			return search.getList(AitMovimento.class).get(0);
		}
			
		return new AitMovimento();
	}
	
	@Override
	public ArquivoAitDTO insertArquivo(ArquivoAitDTO arquivoAitDTO) throws Exception, ValidacaoException {
		return insertArquivo(arquivoAitDTO, new CustomConnection());
	}
	
	private ArquivoAitDTO insertArquivo(ArquivoAitDTO arquivoAitDTO, CustomConnection customConnection) throws Exception, ValidacaoException {
	    try {
	        customConnection.initConnection(true);
	        new CriacaoArquivoValidations().validate(arquivoAitDTO.getArquivo(), customConnection);
	        this.fileSystemRepository.insert(arquivoAitDTO.getArquivo(), TipoDiretorioEnum.AIT.getValue(), arquivoAitDTO.getCdAit(),customConnection);
	        Arquivo arquivo = arquivoRepository.insert(arquivoAitDTO.getArquivo(), customConnection);
	        vincularAitArquivo(arquivoAitDTO.getCdAit(), arquivo.getCdArquivo(), customConnection); 
	        customConnection.finishConnection();
	        return arquivoAitDTO;
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	private void vincularAitArquivo (int cdAit, int cdArquivo, CustomConnection customConnection) throws BadRequestException, Exception {
		AitArquivo aitArquivo = new AitArquivoBuilder()
				.addCdAit(cdAit).addCdArquivo(cdArquivo)
				.build();
		this.aitArquivoRepository.insert(aitArquivo, customConnection);
		
	}
	@Override
	public List<TipoArquivoDTO> buscarArquivos(int cdAit) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > 0);
	    Search<TipoArquivoDTO> aitCancelamentos = this.aitArquivoRepository.findArquivosAit(searchCriterios);
	    return aitCancelamentos.getList(TipoArquivoDTO.class);
	}
	
	@Override
	public ArquivoDownload download(int cdArquivo) throws Exception, ValidacaoException {
		Arquivo arquivo = this.arquivoRepository.get(cdArquivo);
		ArquivoDownload arquivoRetorno = fileSystemRepository.get(arquivo);
		if(arquivo == null)
			throw new ValidacaoException("Nenhum arquivo encontrado.");
		return arquivoRetorno;
	}
	
	@Override
	public void delete(int cdArquivo) throws Exception, ValidacaoException {
		delete(cdArquivo, new CustomConnection());
	}
	
	public void delete(int cdArquivo,  CustomConnection customConnection) throws Exception, ValidacaoException {	
		try {
			customConnection.initConnection(true);	
			aitArquivoRepository.delete(cdArquivo, customConnection);
			arquivoRepository.delete(cdArquivo, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Boolean isRecalculoJurosBaseOnJari(int cdAit) throws Exception {
	    boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	    Ait ait = this.aitRepositoryFactory.getStrategy().get(cdAit);
	    if(ait.getDtVencimento().before(DateUtil.getDataAtual())) {
    		SearchCriterios searchCriterios = new SearchCriterios();
	    	if(lgBaseAntiga) {
	    	    searchCriterios.addCriteriosEqualInteger("codigo_ait", cdAit, cdAit > 0);
	    	    searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
	    	} else {
	    		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > 0);
	    	    searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
	    	}
	    	List<AitMovimento> movimentos = this.aitMovimentoRepositoryFactory.getStrategy().find(searchCriterios);	
    		return processarMovimento(movimentos);
	    }
	    return false;
	}
	
	private Boolean processarMovimento(List<AitMovimento> movimentos) throws Exception {
	    int JulgamentoJariSemProvimento = 0;
	    int JulgamentoJariComProvimento = 0;
	    AitMovimento jari = null;
	    for (AitMovimento movimento : movimentos) {
	        if (movimento.getTpStatus() == TipoStatusEnum.RECURSO_JARI.getKey()) {
	            jari = movimento;
	        }
	        if (movimento.getTpStatus() == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()) {
	        	JulgamentoJariSemProvimento = movimento.getTpStatus();
	        }
	        if (movimento.getTpStatus() == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()) {
	        	JulgamentoJariComProvimento = movimento.getTpStatus();
	        }
	    }
	    return verificarCondicoesJari(JulgamentoJariSemProvimento, JulgamentoJariComProvimento, jari);
	}
	
	private Boolean verificarCondicoesJari(int JulgamentoJariSemProvimento, int JulgamentoJariComProvimento, AitMovimento jari) throws Exception {
		if ((jari != null) && (jari.getTpStatus() == TipoStatusEnum.RECURSO_JARI.getKey() && !jariIsTempestiva(jari) && 
	    		JulgamentoJariSemProvimento == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey())) {
	        return true;
	    }
	    if ((jari != null) && (jari.getTpStatus() == TipoStatusEnum.RECURSO_JARI.getKey() && jariIsTempestiva(jari)
	    		&& JulgamentoJariComProvimento == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey())) {
	        return false;
	    }
	    if((jari != null) && (jari.getTpStatus() == TipoStatusEnum.RECURSO_JARI.getKey())) {
    		return true;
    	}
	    return false;
	}
	
	@Override
	public Boolean isRecalculoJuros(int cdAit) throws Exception {
	    Ait ait = this.aitService.get(cdAit);
	    if (ait.getDtVencimento().after(new GregorianCalendar())) {
	        return false;
	    }
	    return true;
	}
	
	@Override
	public boolean verificaJulgamentoJari(List<AitMovimentoDTO> movimentos) throws Exception {
		boolean contemJulgamento = false;
		for(AitMovimentoDTO movimento : movimentos) {
			if((movimento.getTpStatus() == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() || movimento.getTpStatus() == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey())
				&& movimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRADO.getKey()) {
					contemJulgamento = true;
			}
		}
        return contemJulgamento;
	}
	
	@Override
	public Boolean jariIsTempestiva(AitMovimento aitMovimento) throws Exception{
	    Ait ait = this.aitRepositoryFactory.getStrategy().get(aitMovimento.getCdAit());
		return aitMovimento.getDtMovimento().before(ait.getDtVencimento());
	}
}
