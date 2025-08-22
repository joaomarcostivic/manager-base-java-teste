package com.tivic.manager.mob.correios;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.NoContentException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosEtiquetaDTO;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.CorreiosLoteDTO;
import com.tivic.manager.mob.StCorreiosLote;
import com.tivic.manager.mob.correios.validator.UploadArquivoCorreiosValidations;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoCorreiosDTO;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoPostagem;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import sol.dao.ItemComparator;

public class CorreiosEtiquetaService implements ICorreiosEtiquetaService {
	private ICorreiosEtiquetaRepository correiosEtiquetaRepository;
	private CorreiosLoteRepository correiosLoteRepository;
	private CustomConnection customConnection;
	private IFTPSService ftpsService;
	private ManagerLog managerLog;
	
	public CorreiosEtiquetaService() throws Exception {
		correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
		correiosLoteRepository = (CorreiosLoteRepository) BeansFactory.get(CorreiosLoteRepository.class);
		customConnection = new CustomConnection();
		ftpsService = (IFTPSService) BeansFactory.get(IFTPSService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@Override
	public List<CorreiosEtiqueta> find(SearchCriterios searchCriterios) throws Exception {
		Search<CorreiosEtiqueta> search = new  SearchBuilder<CorreiosEtiqueta>("mob_correios_etiqueta")
				.searchCriterios(searchCriterios)
				.orderBy("nr_etiqueta ASC")
				.build();
		
		return search.getList(CorreiosEtiqueta.class);
	}
	
	@Override
	public List<CorreiosEtiquetaDTO> findEtiquetaDTO(SearchCriterios searchCriterios) throws Exception {
		return  findEtiquetaDTO(searchCriterios, null);
	}
	
	@Override
	public List<CorreiosEtiquetaDTO> findEtiquetaDTO(SearchCriterios searchCriterios, String nrEtiqueta) throws Exception {
		Search<CorreiosEtiquetaDTO> search = new  SearchBuilder<CorreiosEtiquetaDTO>("mob_correios_etiqueta A")
				.fields("A.*, B.id_ait")
				.addJoinTable("LEFT OUTER JOIN mob_ait B ON(A.cd_ait = B.cd_ait)")
				.searchCriterios(searchCriterios)
				.additionalCriterias(validarBuscaNrEtiqueta(nrEtiqueta))
				.orderBy("nr_etiqueta ASC")
				.build();
		
		return search.getList(CorreiosEtiquetaDTO.class);
	}
	
	private String validarBuscaNrEtiqueta(String nrEtiqueta) {
		return nrEtiqueta != null? " CONCAT(A.sg_servico, A.nr_etiqueta, A.nr_digito_verificador, 'BR') ILIKE  '%" + nrEtiqueta + "%'": null; 
	}
	
	public List<CorreiosEtiqueta> buscarListaEtiqueta(int tpRemessa, int quantidadeEtiquetasSolicitada) throws Exception {
	    try {
	        this.customConnection.initConnection(false);
	        List<CorreiosLote> loteDisponivelList = correiosLoteRepository.find(criteriosLoteDisponiveis(tpRemessa), this.customConnection);
	        if (loteDisponivelList.isEmpty()) {
	            throw new Exception("Nenhum lote de etiquetas encontrado.");
	        }
	        List<CorreiosEtiqueta> etiquetas = adicionarEtiquetasLivres(loteDisponivelList, quantidadeEtiquetasSolicitada);
	        this.customConnection.finishConnection(); 
	        return etiquetas;

	    } finally {
	        this.customConnection.closeConnection();
	    }
	}

	
	private SearchCriterios criteriosLoteDisponiveis(int tpRemessa) {
		SearchCriterios searchCriterios = new SearchCriterios();
		int stLoteDisponivel = StCorreiosLote.DISPONIVEL;
		searchCriterios.addCriteriosGreaterDate("dt_vencimento", Util.formatDate(Util.getDataAtual(), "yyyy-MM-dd"), true);
		searchCriterios.addCriteriosEqualInteger("st_lote", stLoteDisponivel, true);
		searchCriterios.addCriteriosEqualInteger("tp_lote", tpRemessa, true);
		searchCriterios.setOrderBy("cd_lote ASC");
		return searchCriterios;
	}
	
	private List<CorreiosEtiqueta> adicionarEtiquetasLivres(List<CorreiosLote> correiosLoteList, int quantidadeEtiquetasSolicitada) throws Exception {
		try {
			this.customConnection.initConnection(true);
			List<CorreiosEtiqueta> correiosEtiquetaList= new ArrayList<CorreiosEtiqueta>();
			int qtdEtiquetasRestantes = quantidadeEtiquetasSolicitada;
			for (CorreiosLote correiosLote : correiosLoteList) {
				SearchCriterios searchCriterios = criteriosEtiquetasDisponiveis(correiosLote.getCdLote(), qtdEtiquetasRestantes);
				correiosEtiquetaList.addAll(find(searchCriterios));
				if(correiosEtiquetaList.size() == quantidadeEtiquetasSolicitada) {
					this.customConnection.finishConnection();
					return correiosEtiquetaList;
				}
				qtdEtiquetasRestantes-=correiosEtiquetaList.size();
				correiosLote.setStLote(StCorreiosLote.CONCLUIDO);	
				correiosLoteRepository.update(correiosLote, this.customConnection);
			}
			throw new Exception("Quantidade de etiquetas insuficientes");
		} finally {
			this.customConnection.closeConnection();
		}
	}
	
	private SearchCriterios criteriosEtiquetasDisponiveis(int cdLoteEtiqueta, int quantidadeEtiquetasSolicitada) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("cd_ait", "" , ItemComparator.ISNULL, Types.INTEGER);
		searchCriterios.setQtLimite(quantidadeEtiquetasSolicitada);
		searchCriterios.addCriteriosEqualInteger("cd_lote", cdLoteEtiqueta, true);
		searchCriterios.addCriterios("cd_lote_impressao", "" , ItemComparator.ISNULL, Types.INTEGER);
		return searchCriterios;
	}
	
	@Override
	public CorreiosEtiqueta update(CorreiosEtiqueta correiosEtiqueta) throws Exception {
		try {
			customConnection.initConnection(true);
			correiosEtiquetaRepository.update(correiosEtiqueta, customConnection);
			customConnection.finishConnection();
			return correiosEtiqueta;
		} finally {
			customConnection.closeConnection();
		}
	}
	public CorreiosEtiqueta insert(CorreiosEtiqueta correiosEtiqueta) throws Exception {
		try {
			this.customConnection.initConnection(true);
			correiosEtiquetaRepository.insert(correiosEtiqueta, this.customConnection);
			this.customConnection.finishConnection();
			return correiosEtiqueta;
		} finally {
			this.customConnection.closeConnection();
		}
	}
	
	public List<CorreiosLoteDTO> getQtdEtiquetaLivres(List<CorreiosLoteDTO> correiosLoteDTOList) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("cd_ait", "" , ItemComparator.ISNULL, Types.INTEGER);
		searchCriterios.addCriterios("cd_lote_impressao", "" , ItemComparator.ISNULL, Types.INTEGER);
		for (CorreiosLoteDTO correiosLoteDTO : correiosLoteDTOList) {
			searchCriterios.getAndRemoveCriterio("cd_lote");
			searchCriterios.addCriteriosEqualInteger("cd_lote", correiosLoteDTO.getCdLote(), true);
			correiosLoteDTO.setQtdEtiquetasLivres(find(searchCriterios).size());
		}		
		return correiosLoteDTOList;
	}

	@Override
	public List<CorreiosEtiqueta> delete(int cdLote) throws Exception {
		try {
			this.customConnection.initConnection(true);
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_lote", cdLote, cdLote > 0);
			List<CorreiosEtiqueta> correiosEtiquetaList = find(searchCriterios);
			for (CorreiosEtiqueta correiosEtiqueta : correiosEtiquetaList) {
				correiosEtiquetaRepository.delete(correiosEtiqueta.getCdEtiqueta(), this.customConnection);			
			}
			this.customConnection.finishConnection();
			return correiosEtiquetaList;
		} finally {
			this.customConnection.closeConnection();
		}
	}
	
	@Override
	public List<CorreiosEtiquetaDTO> deleteListEtiquetas(List<CorreiosEtiquetaDTO> listCorreiosEtiqueta) throws Exception {
		try {
			this.customConnection.initConnection(true);
			for (CorreiosEtiqueta correiosEtiqueta : listCorreiosEtiqueta) {
				correiosEtiquetaRepository.delete(correiosEtiqueta.getCdEtiqueta(), customConnection);			
			}
			this.customConnection.finishConnection();
			return listCorreiosEtiqueta;
		} finally {
			this.customConnection.closeConnection();
		}
	}
	
	@Override
	public List<DadosRetornoCorreioDto> uploadRetornoCorreiosWeb(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, Boolean isTask) throws Exception, ValidacaoException {
		return uploadRetornoCorreios(arquivoRetornoCorreios, cdUsuario, isTask);
	}
	
	@Override
	public List<DadosRetornoCorreioDto> uploadRetornoCorreiosTask(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, Boolean isTask) throws Exception, ValidacaoException {
		return uploadRetornoCorreios(arquivoRetornoCorreios, cdUsuario, isTask);
	}

	public List<DadosRetornoCorreioDto> uploadRetornoCorreios(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, Boolean isTask) throws Exception, ValidacaoException {
		try {
			CustomConnection customConnection = new CustomConnection();
			customConnection.initConnection(true);
			if(!isTask)
				new UploadArquivoCorreiosValidations().validate(arquivoRetornoCorreios, customConnection);
			List<DadosRetornoCorreioDto> retornoCorreioDTO = new ArquivoRetornoPostagem().ler(arquivoRetornoCorreios, cdUsuario, customConnection);
			customConnection.finishConnection();
			return retornoCorreioDTO; 
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public PagedResponse<Arquivo> findArquivo(CorreiosEtiquetaSearch correiosEtiquetaSearch) throws Exception {
		return findArquivo(setCriteriosSearch(correiosEtiquetaSearch), new CustomConnection());
	}
	
	public PagedResponse<Arquivo> findArquivo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<Arquivo> search = new SearchBuilder<Arquivo>("grl_arquivo")
					.searchCriterios(searchCriterios)
					.orderBy("dt_criacao DESC")
					.count()
					.build();
			
			customConnection.finishConnection();
			return new ArquivoCorreiosSearchBuilder(search.getList(Arquivo.class), search.getRsm().getTotal()).build();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void getFilesCorreios(int cdUsuario) throws NoContentException, Exception { 
        String remoteDir = "/SGD";
        String fileExtension = ".TXT";
        Boolean isTask = true;
        try {
        	ftpsService.connect(remoteDir);
    		List<ArquivoRetornoCorreiosDTO> RetornoArquivoCorreios = ftpsService.getFiles(fileExtension);
    		for(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios : RetornoArquivoCorreios) {
    			try {
    				new UploadArquivoCorreiosValidations().validate(arquivoRetornoCorreios, customConnection);
    				uploadRetornoCorreiosTask(arquivoRetornoCorreios, cdUsuario, isTask);
    			} catch (ValidacaoException ex) {
                    managerLog.info("Erro ao processar o arquivo: " + arquivoRetornoCorreios.getNmArquivo() + ". Por favor, tente novamente manualmente.", ex.getMessage());
                    continue;
                } catch (Exception e) {
                    managerLog.error("Erro inesperado ao processar o arquivo: " + arquivoRetornoCorreios.getNmArquivo(), e.getMessage());
                    continue;
                }
    		}
    		ftpsService.disconnect();
		} catch(Exception e) {
			managerLog.showLog(e);
		}
	}
	
	private SearchCriterios setCriteriosSearch(CorreiosEtiquetaSearch correiosEtiquetaSearch) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyString("nm_arquivo", correiosEtiquetaSearch.getNmArquivo(), correiosEtiquetaSearch.getNmArquivo() != null);
		searchCriterios.addCriteriosEqualInteger("cd_tipo_arquivo", correiosEtiquetaSearch.getCdTipoArquivo(), correiosEtiquetaSearch.getCdTipoArquivo() > 0);
		searchCriterios.addCriteriosGreaterDate("dt_criacao", correiosEtiquetaSearch.getDtCriacaoInicial(), correiosEtiquetaSearch.getDtCriacaoInicial() != null);
		searchCriterios.addCriteriosMinorDate("dt_criacao", correiosEtiquetaSearch.getDtCriacaoFinal(), correiosEtiquetaSearch.getDtCriacaoFinal() != null);
		searchCriterios.addCriteriosEqualInteger("nr_registro", correiosEtiquetaSearch.getNrRegistro(), correiosEtiquetaSearch.getNrRegistro() > 0);
		searchCriterios.setQtDeslocamento(((correiosEtiquetaSearch.getLimit() * correiosEtiquetaSearch.getPage()) - correiosEtiquetaSearch.getLimit()));
		searchCriterios.setQtLimite(correiosEtiquetaSearch.getLimit());
		return searchCriterios;
	}
}
