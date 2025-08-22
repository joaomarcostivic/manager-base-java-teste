package com.tivic.manager.mob.lote.impressao.lotedocumentoexterno;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LoteDocumentoExternoService implements ILoteDocumentoExternoService{
	
	private ILoteImpressaoRepository loteImpressaoRepository;
	
	public LoteDocumentoExternoService() throws Exception {
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
	}
	
	@Override
	public LoteImpressao saveOrUpdateLoteImpressao(LoteImpressao loteImpressao) throws Exception{
		return saveOrUpdateLoteImpressao(loteImpressao, new CustomConnection());
	}

	public LoteImpressao saveOrUpdateLoteImpressao(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception{
		try {
			if(loteImpressao==null)
				throw new ValidacaoException("Erro ao salvar. Lote Impressao é nulo");
			
			customConnection.initConnection(true);
			if(loteImpressao.getCdLoteImpressao() == 0){
				loteImpressao.setCdLoteImpressao(loteImpressaoRepository.insert(loteImpressao, customConnection).getCdLoteImpressao());
			}
			else {
				loteImpressao = loteImpressaoRepository.update(loteImpressao, customConnection);
			}
			customConnection.finishConnection();
			return loteImpressao;
		}
		finally {
			customConnection.closeConnection();
		}
	} 
	
	@Override
	public PagedResponse<LoteDocumentoExternoDTO> find(SearchCriterios searchCriterios) throws Exception {
		PagedResponse<LoteDocumentoExternoDTO> lotesDocumentoExterno = find(searchCriterios, new CustomConnection());
		if(lotesDocumentoExterno == null)
			throw new NoContentException("Nenhum Registro encontrado.");
		
		return lotesDocumentoExterno;
	}
	
	public PagedResponse<LoteDocumentoExternoDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws ValidacaoException, Exception {
		Search<LoteDocumentoExternoDTO> search = new SearchBuilder<LoteDocumentoExternoDTO>("mob_lote_impressao A")
				.fields("DISTINCT ON(A.cd_lote_impressao, A.dt_criacao) A.cd_lote_impressao, A.dt_criacao, E.nr_documento, D.sg_orgao_externo")
				.addJoinTable("JOIN mob_lote_documento_externo B ON (A.cd_lote_impressao = B.cd_lote_impressao)")
				.addJoinTable("JOIN mob_documento_externo C ON (C.cd_documento = B.cd_documento)")
				.addJoinTable("JOIN mob_orgao_externo D ON(C.cd_orgao_externo = D.cd_orgao_externo)")
				.addJoinTable("JOIN ptc_documento E ON (E.cd_documento = B.cd_documento)")
				.orderBy("A.dt_criacao DESC")
				.searchCriterios(searchCriterios)
				.count()
				.build();
		
		List<LoteDocumentoExternoDTO> protocoloExternoDTOList = search.getList(LoteDocumentoExternoDTO.class);
		PagedResponse<LoteDocumentoExternoDTO> protocoloExternoDTO = new PagedResponse<LoteDocumentoExternoDTO>(protocoloExternoDTOList, search.getRsm().getTotal());
		return protocoloExternoDTO;
	}
	
	@Override
	public Arquivo getArquivoLote(int cdLoteImpressao, int cdTpArquivo) throws Exception {
		List<Arquivo> arquivoList = searchArquivoLote(cdLoteImpressao, cdTpArquivo).getList(Arquivo.class);
		if (arquivoList.isEmpty()) {
			throw new NoContentException("Nenhum arquivo encontrado.");
		}
		Arquivo arquivo = arquivoList.get(0);
		return arquivo;
	}
	
	private Search<Arquivo> searchArquivoLote(int cdLoteImpressao, int cdTpArquivo) throws Exception {
		SearchCriterios seasrchCriterios = new SearchCriterios();
		seasrchCriterios.addCriteriosEqualInteger("B.cd_lote_impressao", cdLoteImpressao);
		seasrchCriterios.addCriteriosEqualInteger("A.cd_tipo_arquivo", cdTpArquivo);
		Search<Arquivo> search = new SearchBuilder<Arquivo>("GRL_ARQUIVO A")
				.addField("A.cd_arquivo, A.dt_arquivamento, A.cd_tipo_arquivo, A.blb_arquivo")
				.addJoinTable("JOIN mob_lote_impressao_arquivo B ON (B.cd_arquivo = A.cd_arquivo)")
				.searchCriterios(seasrchCriterios)
				.build();
		return search;
	}
}
