package com.tivic.manager.mob.lotes.service.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoSearch;
import com.tivic.manager.mob.lotes.dto.publicacao.LotePublicacaoNotificacaoDTO;
import com.tivic.manager.mob.lotes.dto.publicacao.NotificacaoPublicacaoPendenteDTO;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface ILotePublicacaoService {

	public PagedResponse<LotePublicacaoNotificacaoDTO> buscarArquivosPublicados(LotePublicacaoSearch lotePublicacaoSearch) throws Exception;
	public Search<LotePublicacaoNotificacaoDTO> buscarArquivosPublicados(LotePublicacaoSearch lotePublicacaoSearch, CustomConnection customConnection) throws Exception;
	public PagedResponse<NotificacaoPublicacaoPendenteDTO> buscarPendentesPublicacao(SearchCriterios searchCriterios, boolean lgNaoEntregues, int tpDocumetno) throws Exception;
	public LotePublicacao save(LotePublicacao lotePublicacao, CustomConnection customConnection) throws Exception;
	public Arquivo publicar(int cdUsuario, int tpDocumento, List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos) throws Exception;
	public LotePublicacaoNotificacaoDTO confirmar(int cdLoteImpressao, GregorianCalendar dtConfirmacaoPublicado, int cdUsuario) throws Exception;
	public Arquivo getArquivoLote(int cdLotePublicacao, int tpArquivo) throws Exception;
	public List<LotePublicacao> buscarLotesAguardandoEnvio(int tpDocumento) throws Exception;
	public List<ServicoDetranDTO> enviarPublicacoesLote(LotePublicacao lotePublicacao) throws Exception;
	public List<ServicoDetranDTO> enviarPublicacoesLote(LotePublicacao lotePublicacao, CustomConnection customConnection) throws Exception;
}
