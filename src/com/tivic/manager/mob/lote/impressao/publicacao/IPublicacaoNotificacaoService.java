package com.tivic.manager.mob.lote.impressao.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.LotePublicacaoNotificacaoDto;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.NotificacaoPublicacaoPendenteDto;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IPublicacaoNotificacaoService {
	
	Search<LotePublicacaoNotificacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	PagedResponse<LotePublicacaoNotificacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios) throws Exception;
	PagedResponse<NotificacaoPublicacaoPendenteDto> buscarPendentesPublicacao(SearchCriterios searchCriterios, boolean lgNaoEntregues, int tpDocumetno) throws Exception; 
	Arquivo publicar(int cdUsuario, int tpDocumento, List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos) throws Exception;
	LotePublicacaoNotificacaoDto confirmar(int cdLoteImpressao, GregorianCalendar dtConfirmacaoPublicado, int cdUsuario) throws Exception;
	List<LoteImpressao> buscarLotesAguardandoEnvio(int tpDocumento) throws Exception;
	List<ServicoDetranDTO> enviarPublicacoesLote(LoteImpressao loteImpressao) throws Exception;
	List<ServicoDetranDTO> enviarPublicacoesLote(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
}
