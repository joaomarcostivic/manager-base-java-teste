package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IPublicacaoProtocoloService {
	 public Search<LotePublicacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	 public PagedResponse<LotePublicacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios) throws Exception;
	 public PagedResponse<ProtocoloPublicacaoPendenteDto> buscarPendentesPublicacao(SearchCriterios searchCriterios) throws Exception;
	 public Arquivo publicarProtocolo(int cdUsuario, String idTipoDocumento, int stJulgamento, List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) throws Exception;
	 public LotePublicacaoDto confirmarPublicacao(int cdLoteImpressao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception;
	 public void cancelarPublicacao(int cdAit, int cdMovimento, int cdUsuario) throws Exception;
	 public Arquivo getArquivoLote(int cdLotePublicacao, int tpArquivo) throws Exception;
}
