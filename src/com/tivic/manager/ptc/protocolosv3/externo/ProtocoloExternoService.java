package com.tivic.manager.ptc.protocolosv3.externo;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.arquivos.repository.IFileSystemRepository;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.ptc.protocolos.documentoarquivo.DocumentoArquivoRepository;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDiretorioEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.protocolosv3.externo.builders.ApresentacaoCondutorBuilder;
import com.tivic.manager.ptc.protocolosv3.externo.builders.DocumentoBuilder;
import com.tivic.manager.ptc.protocolosv3.externo.builders.ProtocoloExternoBuilder;
import com.tivic.manager.ptc.protocolosv3.externo.builders.ProtocoloExternoUpdateBuilder;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ProtocoloExternoService implements IProtocoloExternoService {

	private ProtocoloExternoRepository protocoloExternoRepository;
	private ApresentacaoCondutorRepository apresentacaoCondutorRepository;
	private DocumentoRepository documentoRepository;
	private IArquivoRepository arquivoRepository;
	private	DocumentoArquivoRepository documentoArquivoRepository;
	private IFileSystemRepository fileSystemRepository;
	
	public ProtocoloExternoService() throws Exception {
		this.fileSystemRepository = (IFileSystemRepository) BeansFactory.get(IFileSystemRepository.class);
		this.protocoloExternoRepository = (ProtocoloExternoRepository) BeansFactory.get(ProtocoloExternoRepository.class);
		this.apresentacaoCondutorRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.documentoArquivoRepository = (DocumentoArquivoRepository) BeansFactory.get(DocumentoArquivoRepository.class);
	}
	
	@Override
	public ProtocoloExternoDTO insert(ProtocoloExternoDTO protocoloExternoDTO) throws Exception {
		return insert(protocoloExternoDTO, new CustomConnection());
	}

	@Override
	public ProtocoloExternoDTO insert(ProtocoloExternoDTO protocoloExternoDTO, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			Documento documento = new DocumentoBuilder(protocoloExternoDTO).build();
			documento = this.documentoRepository.insert(documento, customConnection);
			protocoloExternoDTO.setCdDocumento(documento.getCdDocumento());
			ProtocoloExterno protocoloExterno = new ProtocoloExternoBuilder(protocoloExternoDTO).build();
			protocoloExterno = this.protocoloExternoRepository.insert(protocoloExterno, customConnection);
			protocoloExternoDTO.setCdDocumentoExterno(protocoloExterno.getCdDocumentoExterno());
			insertApresentacaoCondutor(protocoloExternoDTO, customConnection);
			saveArquivos(protocoloExternoDTO, customConnection);
			customConnection.finishConnection();
			return protocoloExternoDTO;
		} finally {
			customConnection.closeConnection();
		}
	}

	
	private void saveArquivos(ProtocoloExternoDTO protocoloExternoDTO, CustomConnection customConnection) throws Exception {
		for(Arquivo arquivo: protocoloExternoDTO.getArquivos()) {
			this.fileSystemRepository.insert(arquivo, TipoDiretorioEnum.PROTOCOLOS.getValue(), protocoloExternoDTO.getCdDocumento(), customConnection);
			arquivoRepository.insert(arquivo, customConnection);
			DocumentoArquivo documentoArquivo = new DocumentoArquivo(arquivo.getCdArquivo(), protocoloExternoDTO.getCdDocumento());
			documentoArquivoRepository.insert(documentoArquivo, customConnection);
		}
	}
	
	public void insertApresentacaoCondutor(ProtocoloExternoDTO protocoloExternoDTO,  CustomConnection customConnection) throws BadRequestException, Exception {
		if(protocoloExternoDTO.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey()) {
			ApresentacaoCondutor apresentacaoCondutor = new ApresentacaoCondutorBuilder(protocoloExternoDTO).build();
			apresentacaoCondutor = this.apresentacaoCondutorRepository.insert(apresentacaoCondutor, customConnection);
			protocoloExternoDTO.setCdApresentacaoCondutor(apresentacaoCondutor.getCdApresentacaoCondutor());
		}
	}
	
	@Override
	public ProtocoloExternoDTO update(ProtocoloExternoDTO protocoloExternoDTO, int cdDocumento, int cdDocumentoExterno) throws Exception {
		return update(protocoloExternoDTO, cdDocumentoExterno, cdDocumento, new CustomConnection());
	}

	@Override
	public ProtocoloExternoDTO update(ProtocoloExternoDTO protocoloExternoDTO, int cdDocumentoExterno, int cdDocumento, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			ProtocoloExternoDTO protocolo = new ProtocoloExternoUpdateBuilder(protocoloExternoDTO, cdDocumentoExterno, cdDocumento).build();
			Documento documento = new DocumentoBuilder(protocolo).build();
			this.documentoRepository.update(documento, customConnection);
			ProtocoloExterno protocoloExterno = new ProtocoloExternoBuilder(protocolo).build();
			this.protocoloExternoRepository.update(protocoloExterno, customConnection);
			if(protocolo.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey()) {
				ApresentacaoCondutor apresentacaoCondutor = new ApresentacaoCondutorBuilder(protocolo).build();
				this.apresentacaoCondutorRepository.update(apresentacaoCondutor);
			}
			saveArquivos(protocoloExternoDTO, customConnection);
			customConnection.finishConnection();
			return protocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public ProtocoloExternoDTO get(int cdDocumentoExterno, int cdDocumento) throws Exception {
		return get(cdDocumentoExterno, cdDocumento, new CustomConnection());
	}

	@Override
	public ProtocoloExternoDTO get(int cdDocumentoExterno, int cdDocumento, CustomConnection customConnection) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("B.cd_documento_externo", cdDocumentoExterno);
			searchCriterios.addCriteriosEqualInteger("B.cd_documento", cdDocumento);
			customConnection.initConnection(true);
			Search<ProtocoloExternoDTO> search = new SearchBuilder<ProtocoloExternoDTO>("ptc_documento A")
					.fields("A.cd_documento, A.cd_arquivo, A.dt_protocolo, A.tp_documento, A.nr_documento, A.cd_tipo_documento, A.cd_fase, A.cd_situacao_documento, A.nm_requerente, A.txt_observacao,"
							+ "B.nm_condutor, B.cd_documento_externo, B.id_ait, B.nr_placa, B.cd_infracao, B.nr_renainf, B.dt_entrada, B.cd_orgao_externo, C.*,"
							+ "D.cd_orgao_externo, D.nm_orgao_externo, D.sg_orgao_externo, D.nm_orgao_externo, D.cd_tipo_logradouro")
					.addJoinTable("JOIN mob_documento_externo B ON (A.cd_documento = B.cd_documento)")
					.addJoinTable("LEFT OUTER JOIN ptc_apresentacao_condutor C ON (A.cd_documento = C.cd_documento)")
					.addJoinTable("LEFT OUTER JOIN mob_orgao_externo D ON (B.cd_orgao_externo = D.cd_orgao_externo)")
					.searchCriterios(searchCriterios)
					.build();
			customConnection.finishConnection();
			return search.getList(ProtocoloExternoDTO.class).get(0);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public PagedResponse<ProtocoloExternoDTO> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public PagedResponse<ProtocoloExternoDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws ValidacaoException, Exception {
		try {
			customConnection.initConnection(false);
			String geracaoOficioString = searchCriterios.getAndRemoveCriterio("geracaoOficio").getValue();
			Boolean geracaoOficio = Boolean.parseBoolean(geracaoOficioString);
			String incluirNaoPertencenteLote = incluirNaoPertencenteLote(geracaoOficio);
			Search<ProtocoloExternoDTO> search = new SearchBuilder<ProtocoloExternoDTO>("ptc_documento A")
					.fields("A.cd_documento, A.cd_arquivo, A.dt_protocolo, A.tp_documento, A.nr_documento, A.cd_tipo_documento, A.cd_fase, A.cd_situacao_documento, A.nm_requerente, "
							+ "B.nm_condutor, B.cd_documento_externo, B.id_ait, B.nr_placa, B.cd_infracao, B.nr_renainf, B.dt_entrada, B.cd_orgao_externo, C.*,"
							+ " D.cd_orgao_externo, D.nm_orgao_externo, D.sg_orgao_externo, D.cd_tipo_logradouro")
					.addJoinTable("JOIN mob_documento_externo B ON (A.cd_documento = B.cd_documento)")
					.addJoinTable("LEFT OUTER JOIN ptc_apresentacao_condutor C ON (A.cd_documento = C.cd_documento)")
					.addJoinTable("LEFT OUTER JOIN mob_orgao_externo D ON (B.cd_orgao_externo = D.cd_orgao_externo)")
					.searchCriterios(searchCriterios)
					.additionalCriterias(incluirNaoPertencenteLote)
					.orderBy("A.dt_protocolo DESC")
					.count()
					.build();
			
			List<ProtocoloExternoDTO> protocoloExternoDTOList = search.getList(ProtocoloExternoDTO.class);
			
			if(protocoloExternoDTOList.isEmpty())
				throw new NoContentException("Nenhum Registro encontrado.");
			
			PagedResponse<ProtocoloExternoDTO> protocoloExternoDTO = new PagedResponse<ProtocoloExternoDTO>(protocoloExternoDTOList, search.getRsm().getTotal());
			customConnection.finishConnection();
			return protocoloExternoDTO;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private String incluirNaoPertencenteLote(Boolean geracaoOficio) {
		if(geracaoOficio) {
			return "NOT EXISTS (" + 
					"    SELECT 1" + 
					"    FROM mob_lote_impressao A2" + 
					"    JOIN mob_lote_documento_externo B2 ON (A2.cd_lote_impressao = B2.cd_lote_impressao)" + 
					"    JOIN mob_documento_externo C2 ON (C2.cd_documento = B2.cd_documento)" + 
					"    JOIN mob_orgao_externo D2 ON (C2.cd_orgao_externo = D2.cd_orgao_externo)" + 
					"    JOIN ptc_documento E2 ON (E2.cd_documento = B2.cd_documento)" + 
					"    WHERE A2.tp_impressao = " + TipoLoteDocumentoEnum.LOTE_OFICIO_PROTOCOLO_EXTERNO.getKey() + 
					"    AND E2.cd_documento = A.cd_documento" + 
					")";
		}
		return null;
	}

}
