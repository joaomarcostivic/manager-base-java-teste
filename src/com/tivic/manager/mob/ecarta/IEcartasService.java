package com.tivic.manager.mob.ecarta;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.ecarta.dtos.ArquivoServicoDTO;
import com.tivic.manager.mob.ecarta.dtos.ListaArquivoRetornoCorreioDTO;
import com.tivic.manager.mob.ecarta.dtos.StatusLeituraArquivoRetornoDTO;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.sol.connection.CustomConnection;

import sol.util.Result;

public interface IEcartasService {
	Result iniciarGeracaoArquivoServicoEDI(int cdLoteImpressao, int cdUsuario) throws Exception;

	LoteImpressaoStatus getStatus(int cdLoteImpressao, CustomConnection customConnection) throws Exception;

	void getStatusGeracaoDocumentos(SseEventSink sseEventSink, Sse sse, int cdLoteImpressao) throws Exception;

	ArquivoServicoDTO pegarArquivoLote(int cdLoteNotificacao) throws Exception;

	StatusLeituraArquivoRetornoDTO processarArquivo(ListaArquivoRetornoCorreioDTO listaArquivoRetornoCorreio,
			int cdLoteImpressao, int cdUsuario)
			throws Exception;

	ArquivoServicoDTO rejeitarProducao(int cdLoteNotificacao) throws Exception;

	ArquivoServicoDTO confirmarProducao(int cdLoteImpressao, int cdUsuario) throws Exception;
}
