package com.tivic.manager.mob.publicacao.edital;

import java.util.List;

import com.tivic.manager.arquivos.repository.IFileSystemRepository;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.mob.lote.impressao.builders.LoteImpressaoArquivoBuilder;
import com.tivic.manager.mob.publicacao.dto.ArquivoEditalDTO;
import com.tivic.manager.mob.publicacao.dto.ArquivoEditalPortalDTO;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDiretorioEnum;
import com.tivic.manager.ptc.protocolosv3.protocoloarquivos.ArquivoDownload;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class EditalPublicacaoService implements IEditalPublicacaoService {
	
	private IArquivoRepository arquivoRepository;
	private IFileSystemRepository fileSystemRepository;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private IParametroRepository parametroRepository;
	
	public EditalPublicacaoService () throws Exception {
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.fileSystemRepository = (IFileSystemRepository) BeansFactory.get(IFileSystemRepository.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
	public ArquivoEditalDTO insert(ArquivoEditalDTO arquivoEditalDTO) throws Exception, ValidacaoException {
		return insert(arquivoEditalDTO, new CustomConnection());
	}
	
	@Override
	public ArquivoEditalDTO insert(ArquivoEditalDTO arquivoEditalDTO, CustomConnection customConnection) throws Exception, ValidacaoException {
		try {
			customConnection.initConnection(true);
			if(arquivoEditalDTO == null)
				throw new ValidacaoException("Arquivo inválido ou vázio.");
			Arquivo arquivoBuilder = new ArquivoBuilder()
					.setNmArquivo(arquivoEditalDTO.getArquivo().getNmArquivo())
					.setNmDocumento(arquivoEditalDTO.getArquivo().getNmDocumento())
					.setCdUsuario(arquivoEditalDTO.getArquivo().getCdUsuario())
					.setDtCriacao(DateUtil.getDataAtual())
					.setDtArquivamento(DateUtil.getDataAtual())
					.setCdTipoArquivo(getCdTipoArquivoEditalPublicacao(customConnection))
					.setBlbArquivo(arquivoEditalDTO.getArquivo().getBlbArquivo())
					.build();
			this.fileSystemRepository.insert(arquivoBuilder, TipoDiretorioEnum.PROTOCOLOS.getValue(), arquivoEditalDTO.getCdLoteImpressao(), customConnection);
			Arquivo arquivo = this.arquivoRepository.insert(arquivoBuilder, customConnection);
            criarLoteImpressaoArquivo(arquivo.getCdArquivo(), arquivoEditalDTO.getCdLoteImpressao(), customConnection);
			customConnection.finishConnection();
			return arquivoEditalDTO;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private int getCdTipoArquivoEditalPublicacao(CustomConnection customConnection) throws Exception, ValidationException {
		int cdTipoArquivoEditalPublicacao = this.parametroRepository.getValorOfParametroAsInt("CD_TIPO_ARQUIVO_EDITAL_PUBLICACAO", customConnection);
		if(cdTipoArquivoEditalPublicacao <= 0) 
			throw new ValidationException("O parâmetro CD_TIPO_ARQUIVO_EDITAL_PUBLICACAO não foi configurado.");
		return cdTipoArquivoEditalPublicacao;
	}
	
	private void criarLoteImpressaoArquivo(int cdArquivo, int cdLoteImpressao, CustomConnection customConnection) throws Exception {
	    LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivoBuilder()
	            .setCdArquivo(cdArquivo)
	            .setCdLoteImpressao(cdLoteImpressao)
	            .build();
	    this.loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, customConnection);
	}
	
	@Override
	public ArquivoDownload buscarArquivoEdital(int cdLoteImpressao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			SearchCriterios searchCriterios = new SearchCriterios();
	        searchCriterios.addCriteriosEqualInteger("B.cd_lote_impressao", cdLoteImpressao, cdLoteImpressao > 0);
	        searchCriterios.addCriteriosEqualInteger("A.cd_tipo_arquivo", getCdTipoArquivoEditalPublicacao(customConnection));
	        Search<Arquivo> publicacoesSearch = buscarArquivoEdital(searchCriterios, customConnection);
	        List<Arquivo> arquivosEdital = publicacoesSearch.getList(Arquivo.class);
	        if (arquivosEdital.isEmpty()) {
	            return null;
	        }
			ArquivoDownload arquivoRetorno = fileSystemRepository.get(arquivosEdital.get(0));
	        return arquivoRetorno;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Search<Arquivo> buscarArquivoEdital(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Arquivo> search = new SearchBuilder<Arquivo>("grl_arquivo A")
				.fields(" A.* ")
				.addJoinTable("JOIN mob_lote_impressao_arquivo B ON (B.cd_arquivo = A.cd_arquivo)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		return search;
	}
	
	@Override
	public ArquivoDownload download(int cdArquivo) throws Exception, ValidacaoException {
		Arquivo arquivo = this.arquivoRepository.get(cdArquivo);
		ArquivoDownload arquivoRetorno = fileSystemRepository.get(arquivo);
		if(arquivo == null)
			throw new ValidacaoException("Nenhum arquivo encontrado.");
		return arquivoRetorno;
	}
	
	@Override
	public PagedResponse<ArquivoEditalPortalDTO> find(SearchCriterios searchCriterios) throws Exception, ValidacaoException {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			searchCriterios.addCriteriosEqualInteger("A.cd_tipo_arquivo", getCdTipoArquivoEditalPublicacao(customConnection));
	        Search<ArquivoEditalPortalDTO> search = buscarArquivosEdital(searchCriterios, customConnection);
	        if (search == null) {
				throw new ValidacaoException("Nenhum edital encontrado.");
	        }
			return new PagedResponse<ArquivoEditalPortalDTO>(search.getList(ArquivoEditalPortalDTO.class), search.getRsm().getTotal());

		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Search<ArquivoEditalPortalDTO> buscarArquivosEdital(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<ArquivoEditalPortalDTO> search = new SearchBuilder<ArquivoEditalPortalDTO>("grl_arquivo A")
				.fields(" A.*, C.dt_publicacao AS dt_envio ")
				.addJoinTable("JOIN mob_lote_impressao_arquivo B ON (B.cd_arquivo = A.cd_arquivo)")
				.addJoinTable("JOIN mob_lote_publicacao C ON (C.cd_lote_publicacao = B.cd_lote_impressao)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("C.dt_publicacao DESC")
			.build();
		return search;
	}
	    
}
