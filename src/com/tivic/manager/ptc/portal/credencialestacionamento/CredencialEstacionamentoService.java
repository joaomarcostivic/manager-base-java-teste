package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.portal.comprovante.IComprovanteService;
import com.tivic.manager.ptc.portal.request.CartaoEstacionamentoRequest;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CredencialEstacionamentoService implements ICredencialEstacionamentoService{
	
	private IComprovanteService comprovanteService;
	
	public CredencialEstacionamentoService() throws Exception {
		this.comprovanteService = (IComprovanteService) BeansFactory.get(IComprovanteService.class);
	}
	
	@Override
	public DocumentoPortalResponse solicitar(CartaoEstacionamentoRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		PessoaFisica pessoaFisica = new InsereInfoPessoa().inserir(documentoRecurso, customConnection);
		Documento documento = new InsereDocumento().inserir(documentoRecurso, customConnection);
		new InsereDocumentoPessoa().inserir(documento, pessoaFisica.getCdPessoa(), customConnection);
		for(Arquivo arquivo: documentoRecurso.getArquivo()) {
			new InsereArquivo().inserir(arquivo, customConnection);
			new InsereDocumentoArquivo().inserir(documento.getCdDocumento(), arquivo.getCdArquivo(), customConnection);
		}
		DocumentoPortalResponse documentoPortal = setDocumentoPortal(documento, customConnection);
		return documentoPortal;
	}

	private DocumentoPortalResponse setDocumentoPortal(Documento documento, CustomConnection customConnection) throws Exception {
		DocumentoPortalResponse documentoPortal = new DocumentoPortalResponse();
		documentoPortal.setCdDocumento(documento.getCdDocumento());
		documentoPortal.setNrDocumento(documento.getNrDocumento());
		documentoPortal.setProtocoloRecebimento(comprovanteService.imprimirComprovanteCartaoIdoso(documento.getNrDocumento(), customConnection));
		return documentoPortal;
	}
	
	@Override
	public byte[] getCartaoIdoso(int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] cartaoIdoso = getCartaoIdoso(cdDocumento, customConnection);
			customConnection.finishConnection();
			return cartaoIdoso;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] getCartaoIdoso(int cdDocumento, CustomConnection customConnection) throws Exception {
		String caminho = "ptc/cartao_idoso";
		return new ImprimeCartaoEstacionamento().imprimir(cdDocumento, caminho, customConnection);
	}
	

	@Override
	public void deferir(JulgamentoProtocoloEstacionamento julgamentoEstacionamento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, true);	
			new DefereCredencialEstacionamento().deferir(julgamentoEstacionamento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}		
	}
	
	@Override
	public void indeferir(JulgamentoProtocoloEstacionamento julgamentoEstacionamento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, true);	
			new IndefereCredencialEstacionamento().indeferir(julgamentoEstacionamento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}	
	}

	@Override
	public byte[] getCartaoPcd(int cdDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] cartaoIdoso = getCartaoPcd(cdDocumento, customConnection);
			customConnection.finishConnection();
			return cartaoIdoso;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] getCartaoPcd(int cdDocumento, CustomConnection customConnection) throws Exception {
		String caminho = "ptc/cartao_pcd";
		return new ImprimeCartaoEstacionamento().imprimir(cdDocumento, caminho, customConnection);
	}
}
