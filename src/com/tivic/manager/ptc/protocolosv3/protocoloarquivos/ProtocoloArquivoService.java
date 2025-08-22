package com.tivic.manager.ptc.protocolosv3.protocoloarquivos;

import java.util.List;

import com.tivic.manager.arquivos.repository.IFileSystemRepository;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.manager.ptc.protocolos.documentoarquivo.DocumentoArquivoRepository;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDiretorioEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ProtocoloArquivoService implements IProtocoloArquivoService {

	private DocumentoArquivoRepository documentoArquivoRepository;
	private IArquivoRepository arquivoRepository;
	private IFileSystemRepository fileSystemRepository;
	
	public ProtocoloArquivoService () throws Exception {
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		documentoArquivoRepository = (DocumentoArquivoRepository) BeansFactory.get(DocumentoArquivoRepository.class);
		fileSystemRepository = (IFileSystemRepository) BeansFactory.get(IFileSystemRepository.class);
	}
	
	@Override
	public ArquivoDTO insert(ArquivoDTO insertArquivo) throws Exception, ValidacaoException {
		return insert(insertArquivo, new CustomConnection());
	}
	
	public ArquivoDTO insert(ArquivoDTO insertArquivo, CustomConnection customConnection) throws Exception, ValidacaoException {
		try {
			customConnection.initConnection(true);
			if(insertArquivo == null)
				throw new ValidacaoException("Arquivo Inválido ou Nulo.");
			Arquivo arquivo = insertArquivo.getArquivo();
			fileSystemRepository.insert(arquivo, TipoDiretorioEnum.PROTOCOLOS.getValue(), insertArquivo.getCdDocumento(), customConnection);
			arquivoRepository.insert(insertArquivo.getArquivo(), customConnection);
			DocumentoArquivo documentoArquivo = new DocumentoArquivo(
					insertArquivo.getArquivo().getCdArquivo(), 
					insertArquivo.getCdDocumento());
			documentoArquivoRepository.insert(documentoArquivo, customConnection);
			customConnection.finishConnection();
			return insertArquivo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<ArquivoSimples> getArquivos(SearchCriterios searchCriterios) throws Exception, ValidacaoException {
		List<ArquivoSimples> arquivos = new SearchBuilder<ArquivoSimples>("grl_arquivo A")
				.fields(" A.cd_arquivo, A.nm_arquivo,A.nm_documento, A.cd_tipo_arquivo, A.ds_arquivo")
				.addJoinTable(" JOIN ptc_documento_arquivo B ON (A.cd_arquivo = B.cd_arquivo) ")
				.addJoinTable(" JOIN ptc_documento C ON (B.cd_documento = C.cd_documento) ")
				.searchCriterios(searchCriterios)
				.orderBy(" A.nm_arquivo ")
				.build()
				.getList(ArquivoSimples.class);
		
		return arquivos;
	}

	@Override
	public ArquivoDownload download(SearchCriterios searchCriterios) throws Exception, ValidacaoException {
		List<ArquivoDownload> arquivos = new SearchBuilder<ArquivoDownload>("grl_arquivo A")
				.fields(" A.cd_arquivo, A.nm_arquivo, A.txt_caminho_arquivo")
				.searchCriterios(searchCriterios)
				.build()
				.getList(ArquivoDownload.class);
		
		Arquivo arquivo = arquivoRepository.get(arquivos.get(0).getCdArquivo());
		ArquivoDownload arquivoRetorno = fileSystemRepository.get(arquivo);
		if(arquivos.isEmpty())
			throw new ValidacaoException("Nenhum arquivo encontrado.");
		
		return arquivoRetorno;
	}

	@Override
	public void delete(Integer cdArquivo, Integer cdDocumento) throws Exception, ValidacaoException {
		delete(cdArquivo, cdDocumento, new CustomConnection());
	}
	
	public void delete(Integer cdArquivo, Integer cdDocumento, CustomConnection customConnection) throws Exception, ValidacaoException {	
		try {
			customConnection.initConnection(true);
			if(cdArquivo < 0 || cdDocumento < 0)
				throw new ValidationException("Código inválido.");
			
			Arquivo arquivo = arquivoRepository.get(cdArquivo);
			documentoArquivoRepository.delete(cdArquivo, cdDocumento, customConnection);
			arquivoRepository.delete(cdArquivo, customConnection);
			fileSystemRepository.deleteArquivo(arquivo);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ArquivoDownload downloadArquivoCredencial(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			ArquivoDownload arquivo = downloadArquivoCredencial(searchCriterios, customConnection);
			customConnection.finishConnection();
			return arquivo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ArquivoDownload downloadArquivoCredencial(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<ArquivoDownload> search = new SearchBuilder<ArquivoDownload>("grl_arquivo")
				.fields("cd_arquivo, blb_arquivo, nm_arquivo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		
		if(search.getList(ArquivoDownload.class).isEmpty())
			throw new ValidacaoException("Nenhum arquivo encontrado.");
		
		return search.getList(ArquivoDownload.class).get(0);
	}
}
