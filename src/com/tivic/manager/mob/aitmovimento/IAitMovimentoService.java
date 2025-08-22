package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.aitArquivo.TipoArquivoDTO;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.ArquivoAitDTO;
import com.tivic.manager.mob.aitmovimentodocumento.DocumentoProcesso;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IAitMovimentoService {
	public AitMovimento getMovimentoTpStatus(int cdAit, int tpStatus) throws Exception;
	public AitMovimento getMovimentoToSuspensao(String nrProcesso, int cdAit) throws ValidacaoException;
	public AitMovimento save(AitMovimento aitMovimento) throws Exception;
	public AitMovimento save(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public PagedResponse<AitMovimentoDTO> findRemessa(SearchCriterios searchCriterios, boolean lgNaoEnviado) throws Exception;
	public List<AitMovimento> getAllDefesas(int cdAit) throws Exception;
	public void setDtPublicacaoDO(List<DocumentoProcesso> _movimentos, GregorianCalendar dtPublicacao) throws Exception;
	public void setDtPublicacaoDO(List<DocumentoProcesso> _movimentos, GregorianCalendar dtPublicacao, CustomConnection customConnection) throws Exception;
	public void insert(AitMovimento aitMovimento) throws Exception;
	public void insert(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void update(AitMovimento aitMovimento) throws Exception;
	public void update(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public AitMovimento get(int cdAit, int cdMovimento) throws Exception;
	public AitMovimento get(int cdAit, int cdMovimento, CustomConnection customConnection) throws Exception;
	public List<AitMovimento> getByAit(int cdAit) throws Exception;
	public List<AitMovimento> getByAit(int cdAit, CustomConnection customConnection) throws Exception;
	public List<AitMovimento> find (SearchCriterios searchCriterios) throws Exception;
	public List<AitMovimento> find (SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<AitMovimentoDTO> getMovimentos (SearchCriterios searchCriterios) throws Exception;
	public List<AitMovimentoDTO> getMovimentos (SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public void cancelarAutuacaoAit(AitMovimento aitMovimento) throws Exception;
	public void cancelarAutuacaoAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void cancelarRegistroAit(AitMovimento aitMovimento) throws Exception;
	public void cancelarRegistroAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void cancelarPagamento(AitMovimento aitMovimento) throws Exception;
	public void cancelarPagamento(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception;
	public void cancelarNip(AitMovimento aitMovimento, int cdUsuario) throws Exception;
	public void cancelarNip(AitMovimento aitMovimento, int cdUsuario, CustomConnection customConnection) throws Exception;
	public AitMovimento getUltimoMovimento (int cdAit) throws Exception;
	public AitMovimento getUltimoMovimento (int cdAit, CustomConnection customConnection) throws Exception;
	public AitMovimento getStatusMovimento (int cdAit, int tpStatus) throws Exception;
	public AitMovimento getStatusMovimento (int cdAit, int tpStatus, CustomConnection customConnection) throws Exception;
	public AitMovimento getMovimentoComJulgamento(int cdAit, int tpStatus) throws Exception;
	public ArquivoAitDTO insertArquivo(ArquivoAitDTO arquivoAitDTO) throws Exception, ValidacaoException;
	public ArquivoDownload download(int cdArquivo) throws Exception, ValidacaoException;
	public void delete(int cdArquivo) throws Exception, ValidacaoException;
	public List<TipoArquivoDTO> buscarArquivos(int cdAit) throws Exception;
	public Boolean isRecalculoJurosBaseOnJari(int cdAit) throws Exception;
	public Boolean isRecalculoJuros(int cdAit) throws Exception;
	public boolean verificaJulgamentoJari(List<AitMovimentoDTO> movimentos) throws Exception;
	public Boolean jariIsTempestiva(AitMovimento aitMovimento) throws Exception;
}
