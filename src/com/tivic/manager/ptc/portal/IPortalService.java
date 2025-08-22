package com.tivic.manager.ptc.portal;

import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.AitProtocoloResponse;
import com.tivic.manager.ptc.portal.response.AitResponse;
import com.tivic.manager.ptc.portal.response.AndamentoAitResponse;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.manager.ptc.portal.response.ParametroContatoResponse;
import com.tivic.manager.ptc.portal.response.ParametroInstrucaoResponse;
import com.tivic.manager.ptc.portal.response.ParametroNmOrgaoResponse;
import com.tivic.manager.ptc.portal.response.ParametroValorResponse;
import com.tivic.manager.ptc.portal.response.SegundaViaNotificacaoResponse;
import com.tivic.manager.ptc.portal.response.TipoArquivoResponse;
import com.tivic.manager.ptc.portal.vagaespecial.ProtocoloSolicitacaoDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IPortalService {
	TipoArquivoResponse findTipoArquivo() throws Exception;
	List<AitResponse> findAit(String nrPlaca, SearchCriterios searchCriterios) throws Exception;
	List<AitResponse> findAit(String nrPlaca, String nrPlacaConvertida, SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	List<CidadeDTO> getCidades(String nmCidade) throws Exception;
	List<CidadeDTO> getCidades(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	SegundaViaNotificacaoResponse gerarSegundaViaNotificacao(int cdAit, int tpStatus) throws Exception;
	DocumentoPortalResponse solicitarJari(DocumentoPortalRequest documentoRecurso) throws Exception;
	DocumentoPortalResponse solicitarCetran(DocumentoPortalRequest documentoRecurso) throws Exception;
	DocumentoPortalResponse solicitarDefesaPrevia(DocumentoPortalRequest documentoRecurso) throws Exception;
	DocumentoPortalResponse solicitarCartaoIdoso(DocumentoPortalRequest documentoSolicitacao) throws Exception;
	DocumentoPortalResponse solicitarCartaoPcd(DocumentoPortalRequest documentoSolicitacao) throws Exception;
	DocumentoPortalResponse solicitarFici(DocumentoPortalRequest documentoRecurso) throws Exception;
	AndamentoAitResponse gerarImpressaoAit(int cdAit) throws Exception;
	byte[] imprimirFormulario(int tpDocumento) throws Exception;
	AitProtocoloResponse findAitProtocolo(String nrPlaca, SearchCriterios searchCriterios) throws Exception;
	AitProtocoloResponse findAitProtocolo(String nrPlaca, String nrPlacaConvertida, SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	byte[] buscarImagem(String nmParametro) throws Exception;
	ParametroInstrucaoResponse buscarInstrucoes(String nmParametro) throws Exception;
	ParametroContatoResponse buscarContato() throws Exception;
	byte[] imprimirFormularioCartaoIdoso() throws Exception;
	byte[] imprimirFormularioCartaoPcd() throws Exception;
	DocumentoPortalResponse solicitarCredencialEstacionamento(CartaoEstacionamentoRequest documentoRecurso) throws Exception;
	byte[] getCartaoIdoso(int cdDocumento) throws Exception;
	byte[] getCartaoPcd(int cdDocumento) throws Exception;
	String getTextIdoso() throws Exception;
	String getTextPcd() throws Exception;
	CepDefault isCepDefault() throws Exception;
	CepDefault isCepDefault(CustomConnection customConnection) throws Exception;
	ParametroNmOrgaoResponse buscarNmOrgao(String nmParametro) throws Exception;
	String buscarSgDepartamento(String nmParametro) throws Exception;
	ParametroValorResponse getParametroValor(String idParametro) throws Exception;
	List<ProtocoloSolicitacaoDTO> findSolicitacoes(SearchCriterios searchCriterios) throws Exception;
	List<Arquivo> findAnexos(SearchCriterios searchCriterios) throws Exception;
}
