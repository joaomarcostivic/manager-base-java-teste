package com.tivic.test.manager.mob.lotes.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.Mockito;

import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoSearch;
import com.tivic.manager.mob.lotes.dto.impressao.AitDTO;
import com.tivic.manager.mob.lotes.dto.impressao.LoteImpressaoDTO;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepositoryDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class LoteImpressaoRepositoryTest implements LoteImpressaoRepository {

	private List<LoteImpressao> lotesImpressao = new ArrayList<>();
	
	@Override
	public LoteImpressao insert(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
	    int maxCdLote = lotesImpressao.stream()
	                         .map(LoteImpressao::getCdLoteImpressao)
	                         .max(Integer::compareTo)
	                         .orElse(0);
	    loteImpressao.setCdLoteImpressao(maxCdLote + 1);
	    lotesImpressao.add(loteImpressao);
		return loteImpressao;
	}
	
	@Override
	public LoteImpressao update(LoteImpressao updatedLote, CustomConnection customConnection) throws Exception {
	    lotesImpressao.replaceAll(loteImpressao -> loteImpressao.getCdLoteImpressao() == updatedLote.getCdLoteImpressao() ? updatedLote : loteImpressao);
		return updatedLote;
	}

	@Override
	public LoteImpressao get(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
	    return lotesImpressao.stream()
	            .filter(loteImpressao -> loteImpressao.getCdLoteImpressao() == cdLoteImpressao)
	            .findFirst()
	            .orElse(null);
	}

	@Override
	public List<LoteImpressao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<LoteImpressao> filteredLotes = new ArrayList<LoteImpressao>(this.lotesImpressao);
		for(ItemComparator itemComparator : searchCriterios.getCriterios()) {
			if(itemComparator.getColumn().equals("cd_lote_impressao")) {
				if(itemComparator.getTypeComparation() == ItemComparator.EQUAL) {
					filteredLotes = filteredLotes.stream().filter(loteImpressao -> loteImpressao.getCdLoteImpressao() == Integer.parseInt(itemComparator.getValue())).collect(Collectors.toList());
				} 
			} 
		}
		return filteredLotes;
	}

	@Override
	public void delete(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
	    lotesImpressao.removeIf(loteImpressao -> loteImpressao.getCdLoteImpressao() == cdLoteImpressao);
	}

	@Override
	public Search<AitDTO> buscarAitsParaLoteImpressao(SearchCriterios searchCriterios, int tpNotificacao)
			throws Exception {
		SearchCriterios criterios = new SearchCriterios();
	    tpNotificacao = 1;
	    Search<AitDTO> resultadoEsperado = Mockito.mock(Search.class);
	    AitDTO ait = new AitDTO();
	    ait.setCdAit(1);

	    List<AitDTO> lista = Collections.singletonList(ait);
	    Mockito.when(resultadoEsperado.getRsm()).thenReturn((ResultSetMap) lista);
	    LoteImpressaoRepositoryDAO daoReal = new LoteImpressaoRepositoryDAO();
	    LoteImpressaoRepositoryDAO spyDAO = Mockito.spy(daoReal);
	    Mockito.doReturn(resultadoEsperado).when(spyDAO).searchAits(criterios, tpNotificacao);
	    Search<AitDTO> resultado = spyDAO.buscarAitsParaLoteImpressao(criterios, tpNotificacao);

	    assertNotNull(resultado);
	    assertEquals(1, resultado.getRsm().size());
	    assertEquals(1, resultado.getRsm().getInt("cd_ait"));
		return resultado;
	}

	@Override
	public Search<LoteImpressaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		LoteImpressaoSearch searchCriteria = new LoteImpressaoSearch();
	    Search<LoteImpressaoDTO> result = findLotes(searchCriteria);
	    assertNull(result, "Como método está stub, deve retornar null");
		return result;
	}

	@Override
	public Search<LoteImpressaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		 LoteImpressaoRepositoryDAO dao = new LoteImpressaoRepositoryDAO();
		    CustomConnection mockConnection = Mockito.mock(CustomConnection.class);
		    LoteImpressaoSearch search = new LoteImpressaoSearch();
		    Search<LoteImpressaoDTO> mockSearch = Mockito.mock(Search.class);
		    SearchBuilder<LoteImpressaoDTO> searchBuilderMock = Mockito.mock(SearchBuilder.class);
		    Mockito.when(searchBuilderMock.fields(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.addJoinTable(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.searchCriterios(Mockito.any())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.additionalCriterias(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.groupBy(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.orderBy(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.count()).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.customConnection(mockConnection)).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.build()).thenReturn(mockSearch);
		    Search<LoteImpressaoDTO> result = dao.findLotes(search, mockConnection);
		    assertNotNull(result);
			return result;
	}

	@Override
	public Search<LoteImpressaoDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		LoteImpressaoRepositoryDAO dao = Mockito.spy(new LoteImpressaoRepositoryDAO());
	    LoteImpressaoSearch search = new LoteImpressaoSearch();
	    Search<LoteImpressaoDTO> mockSearch = Mockito.mock(Search.class);
	    Mockito.doReturn(mockSearch).when(dao).findLoteAits(Mockito.eq(search), Mockito.any(CustomConnection.class));
	    Search<LoteImpressaoDTO> result = dao.findLoteAits(search);
	    assertNotNull(result);
	    return Mockito.verify(dao).findLoteAits(Mockito.eq(search), Mockito.any(CustomConnection.class));
	}

	@Override
	public Search<LoteImpressaoDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		 LoteImpressaoRepositoryDAO dao = new LoteImpressaoRepositoryDAO();
		    CustomConnection mockConnection = Mockito.mock(CustomConnection.class);
		    LoteImpressaoSearch search = new LoteImpressaoSearch();
		    Search<LoteImpressaoDTO> mockSearch = Mockito.mock(Search.class);
		    SearchBuilder<LoteImpressaoDTO> searchBuilderMock = Mockito.mock(SearchBuilder.class);
		    Mockito.when(searchBuilderMock.fields(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.addJoinTable(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.searchCriterios(Mockito.any())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.orderBy(Mockito.anyString())).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.count()).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.customConnection(mockConnection)).thenReturn(searchBuilderMock);
		    Mockito.when(searchBuilderMock.build()).thenReturn(mockSearch);
		    Search<LoteImpressaoDTO> result = dao.findLoteAits(search, mockConnection);
		    assertNotNull(result);
			return result;
	}
	
	@Override
	public Search<LoteImpressaoDTO> getLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		LoteImpressaoRepositoryDAO dao = Mockito.spy(new LoteImpressaoRepositoryDAO());
	    LoteImpressaoSearch search = new LoteImpressaoSearch();
	    Search<LoteImpressaoDTO> mockSearch = Mockito.mock(Search.class);
	    Mockito.doReturn(mockSearch).when(dao).getLote(Mockito.eq(search), Mockito.any(CustomConnection.class));
	    Search<LoteImpressaoDTO> result = dao.getLote(search);
	    assertNotNull(result);
	    return Mockito.verify(dao).getLote(Mockito.eq(search), Mockito.any(CustomConnection.class));
	}

	@Override
	public Search<LoteImpressaoDTO> getLote(LoteImpressaoSearch loteImpressaoSearch, CustomConnection customConnection) throws Exception {
		LoteImpressaoRepositoryDAO dao = Mockito.spy(new LoteImpressaoRepositoryDAO());
	    CustomConnection mockConnection = Mockito.mock(CustomConnection.class);
	    LoteImpressaoSearch search = new LoteImpressaoSearch();

	    Search<LoteImpressaoDTO> mockSearch = Mockito.mock(Search.class);
	    SearchBuilder<LoteImpressaoDTO> searchBuilderMock = Mockito.mock(SearchBuilder.class);
	    Mockito.when(searchBuilderMock.fields(Mockito.anyString())).thenReturn(searchBuilderMock);
	    Mockito.when(searchBuilderMock.addJoinTable(Mockito.anyString())).thenReturn(searchBuilderMock);
	    Mockito.when(searchBuilderMock.searchCriterios(Mockito.any())).thenReturn(searchBuilderMock);
	    Mockito.when(searchBuilderMock.additionalCriterias(Mockito.anyString())).thenReturn(searchBuilderMock);
	    Mockito.when(searchBuilderMock.groupBy(Mockito.anyString())).thenReturn(searchBuilderMock);
	    Mockito.when(searchBuilderMock.orderBy(Mockito.anyString())).thenReturn(searchBuilderMock);
	    Mockito.when(searchBuilderMock.customConnection(mockConnection)).thenReturn(searchBuilderMock);
	    Mockito.when(searchBuilderMock.build()).thenReturn(mockSearch);
	    Search<LoteImpressaoDTO> result = dao.getLote(search, mockConnection);
	    assertNotNull(result);
		return result;
	}

}
