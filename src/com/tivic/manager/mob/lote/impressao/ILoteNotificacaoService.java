package com.tivic.manager.mob.lote.impressao;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearch;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.NipImpressaoDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ILoteNotificacaoService {
	public List<Ait> getAllAitsEmitirNAI() throws ValidacaoException, IllegalArgumentException, SQLException, Exception;
	public List<Ait> getAllAitsEmitirNAI(CustomConnection customConnection) throws ValidacaoException, IllegalArgumentException, SQLException, Exception;
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoNaiLote(List<Ait> listAitsNais, int cdUsuario) throws Exception;
	public List<Ait> getAllAitsEmitirNIP() throws ValidacaoException, Exception;
	public List<Ait> getAllAitsEmitirNIP(CustomConnection customConnection) throws ValidacaoException, Exception;
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoNipLote(List<Ait> listAitsNips, int cdUsuario) throws ValidacaoException, Exception;
	public LoteImpressao gerarLoteNotificacao(LoteImpressao loteImpressao, boolean isTask) throws ValidacaoException, Exception;
	public LoteImpressao gerarLoteNotificacao(LoteImpressao loteImpressao) throws ValidacaoException, Exception;
	public LoteImpressao save(LoteImpressao loteImpressao) throws Exception;
	public LoteImpressao save(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	public void gerarDocumentosLote(int cdLoteImpressao, int cdUsuario) throws Exception;
	public Ait atualizarDadosAit(Ait ait, CustomConnection customConnection) throws Exception;
	public LoteImpressaoAitDTO buscarLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception, NoContentException;
	public String getEstadoOrgaoAutuador();
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoFimPrazoDefesa(List<Ait> listAitsNips, int cdUsuario) throws Exception;
	public List<Ait> getAllAitsEmitirFimPrazoDefesa() throws Exception;
	public List<Ait> getAllAitsEmitirFimPrazoDefesa(CustomConnection customConnection) throws Exception;
	public List<AitDTO> buscarQuantidadeAitsParaLoteImpressao(int quantidadeAit, int tipoLote) throws ValidacaoException, Exception;
	public LoteImpressaoAit buscarNotificacao(int cdAit, int tipoNotificacao, CustomConnection customConnection) throws Exception;
	public void gerarDocumentosLote(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception;
	public byte[] imprimirLoteNotificacao(int cdLoteImpressao) throws Exception;
	public byte[] imprimirLoteNotificacao(int cdLoteImpressao, CustomConnection customConnection) throws Exception;
	public byte[] gerarLoteNotificacaoNaiViaUnica(int cdAit, int cdUsuario) throws Exception;
	public byte[] gerarLoteNotificacaoNaiViaUnica(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception;
	public NipImpressaoDTO gerarLoteNotificacaoNipViaUnica(int cdAit, int cdUsuario) throws Exception;
	public NipImpressaoDTO gerarLoteNotificacaoNipViaUnica(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception;
	public Arquivo pegarArquivoLote(int cdLoteNotificacao, int tpArquivo) throws Exception;
	public LoteImpressao gerarLoteNotificacaoAitViaUnica(List<Ait> aitList, int cdUsuario, int tipoLote) throws ValidacaoException, Exception ;
	public void gerarDocsLoteAitViaUnica(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception;
	public void iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario) throws Exception;
	public void iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception;
	public List<LoteImpressaoAit> reiniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario) throws Exception;
	public List<LoteImpressaoAit> reiniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception;
	public PagedResponse<AitDTO> buscarAitsParaLoteImpressao(int tipoLote, SearchCriterios searchCriterios) throws ValidacaoException, Exception;
	public PagedResponse<LoteNotificacaoDTO> buscarLotes(LoteImpressaoSearch loteImpressaoSearch) throws ValidacaoException, Exception;
	public PagedResponse<LoteImpressaoAitDTO> buscarLotesAits(LoteImpressaoSearch loteImpressaoSearch) throws ValidacaoException, Exception;
	public byte[] gerarNotificacaoNipComJuros(int cdAit, Boolean printPortal) throws Exception;
	public void getStatusGeracaoDocumentos(SseEventSink sseEventSink, Sse sse, int cdLoteImpressao) throws Exception;
	public LoteImpressaoAit deleteLoteImpressaoAit(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception;
	public LoteImpressao deleteLoteImpressao(int cdLoteImpressao, CustomConnection customConnection) throws Exception;
	public LoteImpressao deleteLoteImpressao(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	public void atualizarLotePrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, List<Ait> aits, List<LoteImpressaoAitDTO> aitsNovoLote, CustomConnection customConnection) throws ValidacaoException, Exception;
	public List<Ait> buscarAitLoteImpressao(int cdLoteImpressao, int tipoRecurso, CustomConnection customConnection) throws Exception;
	public Boolean verificarAitsComMesmoLote(int cdAit, int tpDocumento) throws Exception;
	public byte[] gerarNotificacaoNicNipComJuros(int cdAit, Boolean printPortal) throws Exception;
}
