package com.tivic.manager.mob.correios;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.CorreiosLoteDTO;
import com.tivic.manager.mob.correios.builder.GerarEtiquetasBuilder;
import com.tivic.manager.util.DateUtil;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CorreiosLoteService implements ICorreiosLoteService {
	private ICorreiosEtiquetaService correiosEtiquetaService;
	private CorreiosLoteRepository correiosLoteRepository;
	
	public CorreiosLoteService() throws Exception {
		correiosLoteRepository = (CorreiosLoteRepository) BeansFactory.get(CorreiosLoteRepository.class);
		correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
	}
	
	@Override
	public List<CorreiosLoteDTO> findDTO(SearchCriterios searchCriterios) throws Exception, NoContentException {
		return findDTO(searchCriterios, null);
	}
	
	@Override
	public List<CorreiosLoteDTO> findDTO(SearchCriterios searchCriterios, String nrEtiqueta) throws Exception, NoContentException {
		Search<CorreiosLoteDTO> search = new  SearchBuilder<CorreiosLoteDTO>("mob_correios_etiqueta A")
				.fields(" DISTINCT ON(B.cd_lote) B.*, A.sg_servico, A.nr_etiqueta, A.nr_digito_verificador")
				.addJoinTable("LEFT OUTER JOIN mob_correios_lote B ON (B.cd_lote = A.cd_lote) ")
				.searchCriterios(searchCriterios)
				.additionalCriterias(validarBuscaNrEtiqueta(nrEtiqueta))
				.orderBy(" B.cd_lote, ST_LOTE ASC")
				.build();
		List<CorreiosLoteDTO> correiosLoteDtoList = search.getList(CorreiosLoteDTO.class);
		if(correiosLoteDtoList.isEmpty())
			throw new NoContentException("Nenhum lote de etiqueta disponível");
		correiosEtiquetaService.getQtdEtiquetaLivres(correiosLoteDtoList);			
		return correiosLoteDtoList;
	}
	
	private String validarBuscaNrEtiqueta(String nrEtiqueta) {
		return nrEtiqueta != null? " CONCAT(A.sg_servico, A.nr_etiqueta, A.nr_digito_verificador, 'BR') ILIKE  '%" + nrEtiqueta + "%'": null; 
	}
	
	@Override
	public CorreiosLote create(CorreiosLote correiosLote) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			correiosLote.setDtLote(DateUtil.today());
			correiosLoteRepository.insert(correiosLote, customConnection);
			new GerarEtiquetasBuilder().gerarEtiquetas(correiosLote, customConnection);
			customConnection.finishConnection();
			return correiosLote;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public CorreiosLote update(CorreiosLote correiosLote) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new UpdateLoteEtiquetaFactory()
				.getStrategy(correiosLote.getStLote())
				.updateLoteEtiqueta(correiosLote, customConnection);
			customConnection.finishConnection();
			return correiosLote;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public CorreiosLote get(int id) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			CorreiosLote correiosLote = correiosLoteRepository.get(id, customConnection);
			customConnection.finishConnection();
			return correiosLote;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public List<CorreiosLote> find(SearchCriterios searchCriterios) throws Exception {
		Search<CorreiosLote> search = new  SearchBuilder<CorreiosLote>("mob_correios_lote")
				.searchCriterios(searchCriterios)
				.build();
		if(search.getList(CorreiosLote.class).isEmpty()) 
			throw new NoContentException("Nenhum lote de etiqueta disponível");
		return search.getList(CorreiosLote.class);
	}

	@Override
	public PagedResponse<CorreiosLoteDTO> findTable(SearchCriterios searchCriterios) throws Exception {
		return findTable(searchCriterios, new CustomConnection());
	}

	@Override
	public PagedResponse<CorreiosLoteDTO> findTable(SearchCriterios searchCriterios, CustomConnection customConnection)throws Exception {
		try {
			customConnection.initConnection(false);
			Search<CorreiosLoteDTO> search = new  SearchBuilder<CorreiosLoteDTO>("mob_correios_lote A")
					.fields("A.*")
					.addJoinTable("LEFT OUTER JOIN mob_correios_etiqueta B ON (B.cd_lote = A.cd_lote) ")
					.searchCriterios(searchCriterios)
					.count()
					.orderBy("A.DT_LOTE DESC")
					.groupBy(" A.CD_LOTE ")
					.build();
			List<CorreiosLoteDTO> correiosLoteDtoList = search.getList(CorreiosLoteDTO.class);
			correiosEtiquetaService.getQtdEtiquetaLivres(correiosLoteDtoList);
			customConnection.finishConnection();
			return new PagedResponse<CorreiosLoteDTO>(correiosLoteDtoList, search.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
}
