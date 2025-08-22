package com.tivic.test.manager.tasks.limpeza.lotes.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTO;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoDTO;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearch;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.SqlFormat;

import sol.dao.ItemComparator;

public class LoteImpressaoRepositoryTest implements ILoteImpressaoRepository {

	private List<LoteImpressao> listLoteImpressao;
	
	public LoteImpressaoRepositoryTest() {
		listLoteImpressao = new ArrayList<>();
		listLoteImpressao.add(criarLoteImpressao1());
		listLoteImpressao.add(criarLoteImpressao2());
		listLoteImpressao.add(criarLoteImpressao3());
	}
	
	private LoteImpressao criarLoteImpressao1() {
		LoteImpressao loteImpressao = new LoteImpressao();
		loteImpressao.setCdLoteImpressao(1);
		loteImpressao.setDtCriacao(new GregorianCalendar(2024, 0, 1, 0, 0, 0));
		loteImpressao.setCdArquivo(7);
		return loteImpressao;
	}

	private LoteImpressao criarLoteImpressao2() {
		LoteImpressao loteImpressao = new LoteImpressao();
		loteImpressao.setCdLoteImpressao(2);
		loteImpressao.setDtCriacao(new GregorianCalendar(2024, 0, 1, 0, 0, 0));
		loteImpressao.setCdArquivo(8);
		return loteImpressao;
	}

	private LoteImpressao criarLoteImpressao3() {
		LoteImpressao loteImpressao = new LoteImpressao();
		loteImpressao.setCdLoteImpressao(3);
		loteImpressao.setDtCriacao(new GregorianCalendar(2024, 0, 2, 0, 0, 0));
		loteImpressao.setCdArquivo(9);
		return loteImpressao;
	}
	
	@Override
	public LoteImpressao insert(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		this.listLoteImpressao.add(loteImpressao);
		return loteImpressao;
	}

	@Override
	public LoteImpressao update(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressao.size(); i++) {
			LoteImpressao loteImpressaoLista = listLoteImpressao.get(i);
			if(loteImpressaoLista.getCdLoteImpressao() == loteImpressao.getCdLoteImpressao()) { 
				listLoteImpressao.set(i, loteImpressao);
			}
		}
		return loteImpressao;
	}

	@Override
	public LoteImpressao delete(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressao.size(); i++) {
			LoteImpressao loteImpressaoLista = listLoteImpressao.get(i);
			if(loteImpressaoLista.getCdLoteImpressao() == loteImpressao.getCdLoteImpressao()) { 
				listLoteImpressao.remove(i);
			}
		}
		return loteImpressao;
	}

	@Override
	public LoteImpressao get(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressao.size(); i++) {
			LoteImpressao loteImpressaoLista = listLoteImpressao.get(i);
			if(loteImpressaoLista.getCdLoteImpressao() == cdLoteImpressao) { 
				return loteImpressaoLista;
			}
		}
		return null;
	}

	@Override
	public List<LoteImpressao> getAll(CustomConnection customConnection) throws Exception {
		return this.listLoteImpressao;
	}

	@Override
	public List<LoteImpressao> find(ArrayList<ItemComparator> criterios, CustomConnection customConnection)
			throws Exception {
		List<LoteImpressao> loteImpressaoFilter = new ArrayList<LoteImpressao>(this.listLoteImpressao);
		for(ItemComparator itemComparator : criterios) {
			if(itemComparator.getColumn().equals("cd_lote_impressao")) {
				if(itemComparator.getTypeComparation() == ItemComparator.EQUAL) {
					loteImpressaoFilter = loteImpressaoFilter.stream().filter(loteImpressao -> loteImpressao.getCdLoteImpressao() == Integer.parseInt(itemComparator.getValue())).collect(Collectors.toList());
				} 
			} else if(itemComparator.getColumn().equals("dt_criacao")) {
				if(itemComparator.getTypeComparation() == ItemComparator.GREATER_EQUAL) {
					loteImpressaoFilter = loteImpressaoFilter.stream().filter(loteImpressao -> loteImpressao.getDtCriacao().after(DateUtil.convStringToCalendar(itemComparator.getValue(), new SqlFormat())) || loteImpressao.getDtCriacao().equals(DateUtil.convStringToCalendar(itemComparator.getValue(), new SqlFormat()))).collect(Collectors.toList());
				} else if(itemComparator.getTypeComparation() == ItemComparator.MINOR_EQUAL) {
					loteImpressaoFilter = loteImpressaoFilter.stream().filter(loteImpressao -> loteImpressao.getDtCriacao().before(DateUtil.convStringToCalendar(itemComparator.getValue(), new SqlFormat())) || loteImpressao.getDtCriacao().equals(DateUtil.convStringToCalendar(itemComparator.getValue(), new SqlFormat()))).collect(Collectors.toList());
				}
			}
		}
		return loteImpressaoFilter;
	}

	@Override
	public Search<LoteNotificacaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<LoteNotificacaoDTO> findLotes(LoteImpressaoSearch loteImpressaoSearch,
			CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<LoteImpressaoAitDTO> getLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<LoteImpressaoAitDTO> getLote(LoteImpressaoSearch loteImpressaoSearchs,
			CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<LoteImpressaoAitDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<LoteImpressaoAitDTO> findLoteAits(LoteImpressaoSearch loteImpressaoSearch,
			CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios) throws Exception {
		return Collections.emptyList();
	}

	@Override
	public List<LoteImpressao> findLoteImpressao(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return Collections.emptyList();
	}


}
