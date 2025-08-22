package com.tivic.manager.mob.lotes.service.documentoexterno;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.lotes.dto.documentoexterno.LoteDocumentoExternoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LoteDocumentoExternoService implements ILoteDocumentoExternoService{
	
	private LoteImpressaoRepository loteImpressaoRepository;
	private LoteRepository loteRepository;
	private IArquivoRepository arquivoRepository;
	
	public LoteDocumentoExternoService() throws Exception {
		this.loteImpressaoRepository = (LoteImpressaoRepository) BeansFactory.get(LoteImpressaoRepository.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
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
				.fields("DISTINCT ON(A.cd_lote_impressao, F.dt_criacao) A.cd_lote_impressao, E.nr_documento, D.sg_orgao_externo, F.dt_criacao")
				.addJoinTable("JOIN mob_lote_documento_externo B ON (A.cd_lote_impressao = B.cd_lote_impressao)")
				.addJoinTable("JOIN mob_documento_externo C ON (C.cd_documento = B.cd_documento)")
				.addJoinTable("JOIN mob_orgao_externo D ON(C.cd_orgao_externo = D.cd_orgao_externo)")
				.addJoinTable("JOIN ptc_documento E ON (E.cd_documento = B.cd_documento)")
				.addJoinTable("JOIN grl_lote F ON (F.cd_lote = A.cd_lote)")
				.orderBy("F.dt_criacao DESC")
				.searchCriterios(searchCriterios)
				.count()
				.build();
		
		List<LoteDocumentoExternoDTO> protocoloExternoDTOList = search.getList(LoteDocumentoExternoDTO.class);
		PagedResponse<LoteDocumentoExternoDTO> protocoloExternoDTO = new PagedResponse<LoteDocumentoExternoDTO>(protocoloExternoDTOList, search.getRsm().getTotal());
		return protocoloExternoDTO;
	}
	
	public byte[] imprimirOficioExterno(int cdLoteImpressao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), customConnection);
			Arquivo arquivoLote = this.arquivoRepository.get(lote.getCdArquivo(), customConnection);
			if (lote.getCdArquivo() <= 0 || arquivoLote == null) {
				throw new ValidacaoException("O arquivo para download não foi encontrado.");
			}
			loteImpressao.setStLote(StatusLoteImpressaoEnum.IMPRESSO.getKey());
			customConnection.finishConnection();
			return arquivoLote.getBlbArquivo();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
}
