package com.tivic.manager.mob.correios;

import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosEtiquetaDTO;
import com.tivic.manager.mob.CorreiosLoteDTO;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoCorreiosDTO;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ICorreiosEtiquetaService {
	List<CorreiosEtiqueta> find(SearchCriterios searchCriterios) throws Exception;
	List<CorreiosEtiquetaDTO> findEtiquetaDTO(SearchCriterios searchCriterios) throws Exception;
	List<CorreiosEtiquetaDTO> findEtiquetaDTO(SearchCriterios searchCriterios, String nrEtiqueta) throws Exception;
	public CorreiosEtiqueta insert(CorreiosEtiqueta correiosEtiqueta) throws Exception;
	List<CorreiosEtiqueta> buscarListaEtiqueta(int tpRemessa, int quantidadeEtiquetasSolicitada) throws Exception;
	CorreiosEtiqueta update(CorreiosEtiqueta correiosEtiqueta) throws Exception;
	public List<CorreiosLoteDTO> getQtdEtiquetaLivres(List<CorreiosLoteDTO> correiosLoteDTOList) throws Exception;
	public List<CorreiosEtiqueta> delete(int cdLote) throws Exception;
	public List<CorreiosEtiquetaDTO> deleteListEtiquetas(List<CorreiosEtiquetaDTO> listCorreiosEtiqueta) throws Exception;
	public List<DadosRetornoCorreioDto> uploadRetornoCorreiosWeb(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, Boolean isTask) throws Exception, ValidacaoException;
	public List<DadosRetornoCorreioDto> uploadRetornoCorreiosTask(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, Boolean isTask) throws Exception, ValidacaoException;
	public List<DadosRetornoCorreioDto> uploadRetornoCorreios(ArquivoRetornoCorreiosDTO arquivoRetornoCorreios, int cdUsuario, Boolean isTask) throws Exception, ValidacaoException;
	public PagedResponse<Arquivo> findArquivo(CorreiosEtiquetaSearch correiosEtiquetaSearch) throws Exception;
	public PagedResponse<Arquivo> findArquivo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public void getFilesCorreios(int cdUsuario) throws Exception;
}
