package com.tivic.manager.ptc.portal.vagaespecial;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.ptc.portal.TipoSistemaEnum;
import com.tivic.manager.ptc.portal.builders.CartaoEstacionamentoRequestBuilder;
import com.tivic.manager.ptc.portal.credencialestacionamento.ICredencialEstacionamentoService;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class VagaEspecialPortalService {
	
	private IParametroRepository parametroRepository;
	private ICredencialEstacionamentoService credencialEstacionamentoService;
	
	public VagaEspecialPortalService() throws Exception {
		super();
		credencialEstacionamentoService = (ICredencialEstacionamentoService) BeansFactory.get(ICredencialEstacionamentoService.class);
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	public DocumentoPortalResponse solicitarCartaoIdoso(DocumentoPortalRequest documentoSolicitacao, CustomConnection customConnection) throws Exception {
		int tpDocumento = parametroRepository.getValorOfParametroAsInt("MOB_CD_TIPO_DOCUMENTO_CARTAO_IDOSO");
		CartaoEstacionamentoRequest cartaoEstacionamentoRequest = buildCartaoEstacionamentoRequest(documentoSolicitacao, tpDocumento);
		DocumentoPortalResponse documentoPortal = credencialEstacionamentoService.solicitar(cartaoEstacionamentoRequest, customConnection); 
		return documentoPortal;
	}
	
	public DocumentoPortalResponse solicitarCartaoPcd(DocumentoPortalRequest documentoSolicitacao, CustomConnection customConnection) throws Exception {
		int tpDocumento = parametroRepository.getValorOfParametroAsInt("MOB_CD_TIPO_DOCUMENTO_CARTAO_PCD");
		CartaoEstacionamentoRequest cartaoEstacionamentoRequest = buildCartaoEstacionamentoRequest(documentoSolicitacao, tpDocumento);
		DocumentoPortalResponse documentoPortal = credencialEstacionamentoService.solicitar(cartaoEstacionamentoRequest, customConnection); 
		return documentoPortal;
	}
	
	private CartaoEstacionamentoRequest buildCartaoEstacionamentoRequest(DocumentoPortalRequest documentoSolicitacao, int tpDocumento) throws Exception {
		CartaoEstacionamentoRequest cartaoEstacionamento = new CartaoEstacionamentoRequestBuilder()
				.setNmRequerente(documentoSolicitacao.getNmRequerente())
				.setNrCpfRequerente(documentoSolicitacao.getNrCpfCnpjRequerente())
				.setNrTelefoneRequerente(documentoSolicitacao.getNrTelefoneRequerente())
				.setNrCelularRequerente(documentoSolicitacao.getNrCelularRequerente())
				.setNmEmail(documentoSolicitacao.getEmailSolicitante())
				.setNmLogradouroRequerente(documentoSolicitacao.getNmLogradouroRequerente())
				.setNmBairroRequerente(documentoSolicitacao.getNmBairroRequerente())
				.setNrEnderecoRequerente(documentoSolicitacao.getNrEnderecoRequerente())
				.setNmComplementoRequerente(documentoSolicitacao.getNmComplementoRequerente())
				.setNrCepRequerente(documentoSolicitacao.getNrCepRequerente())
				.setOrigemDocumento(TipoSistemaEnum.PORTAL.getKey())
				.setTpDocumento(tpDocumento)
				.setArquivo(documentoSolicitacao.getArquivo())
			.build();
		return cartaoEstacionamento;
	}
}
