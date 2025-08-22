package com.tivic.test.manager.tasks.limpeza.lotes.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteImpressaoAitRepositoryTest implements ILoteImpressaoAitRepository {

	private List<LoteImpressaoAit> listLoteImpressaoAit;
	
	public LoteImpressaoAitRepositoryTest() {
		listLoteImpressaoAit = new ArrayList<>();
		listLoteImpressaoAit.add(criarLoteImpressaoAit1());
		listLoteImpressaoAit.add(criarLoteImpressaoAit2());
		listLoteImpressaoAit.add(criarLoteImpressaoAit3());
	}
	
	private LoteImpressaoAit criarLoteImpressaoAit1() {
		LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAit();
		loteImpressaoAit.setCdLoteImpressao(1);
		loteImpressaoAit.setCdArquivo(4);
		return loteImpressaoAit;
	}

	private LoteImpressaoAit criarLoteImpressaoAit2() {
		LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAit();
		loteImpressaoAit.setCdLoteImpressao(1);
		loteImpressaoAit.setCdArquivo(5);
		return loteImpressaoAit;
	}

	private LoteImpressaoAit criarLoteImpressaoAit3() {
		LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAit();
		loteImpressaoAit.setCdLoteImpressao(2);
		loteImpressaoAit.setCdArquivo(6);
		return loteImpressaoAit;
	}
	
	@Override
	public LoteImpressaoAit insert(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception {
		this.listLoteImpressaoAit.add(loteImpressaoAit);
		return loteImpressaoAit;
	}

	@Override
	public LoteImpressaoAit update(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressaoAit.size(); i++) {
			LoteImpressaoAit loteImpressaoAitLista = listLoteImpressaoAit.get(i);
			if(loteImpressaoAitLista.getCdLoteImpressao() == loteImpressaoAit.getCdLoteImpressao() && loteImpressaoAitLista.getCdAit() == loteImpressaoAit.getCdAit()) { 
				listLoteImpressaoAit.set(i, loteImpressaoAit);
			}
		}
		return loteImpressaoAit;
	}

	@Override
	public LoteImpressaoAit delete(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressaoAit.size(); i++) {
			LoteImpressaoAit loteImpressaoAitLista = listLoteImpressaoAit.get(i);
			if(loteImpressaoAitLista.getCdLoteImpressao() == loteImpressaoAit.getCdLoteImpressao() && loteImpressaoAitLista.getCdAit() == loteImpressaoAit.getCdAit()) { 
				listLoteImpressaoAit.remove(i);
			}
		}
		return loteImpressaoAit;
	}


	@Override
	public LoteImpressaoAit get(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressaoAit.size(); i++) {
			LoteImpressaoAit loteImpressaoAitLista = listLoteImpressaoAit.get(i);
			if(loteImpressaoAitLista.getCdLoteImpressao() == cdLoteImpressao && loteImpressaoAitLista.getCdAit() == cdAit) { 
				return loteImpressaoAitLista;
			}
		}
		return null;
	}

	@Override
	public List<LoteImpressaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		List<LoteImpressaoAit> loteImpressaoAitFilter = new ArrayList<LoteImpressaoAit>(this.listLoteImpressaoAit);
		for(ItemComparator itemComparator : searchCriterios.getCriterios()) {
			if(itemComparator.getColumn().equals("cd_lote_impressao")) {
				if(itemComparator.getTypeComparation() == ItemComparator.EQUAL) {
					loteImpressaoAitFilter = loteImpressaoAitFilter.stream().filter(loteImpressaoAit -> loteImpressaoAit.getCdLoteImpressao() == Integer.parseInt(itemComparator.getValue())).collect(Collectors.toList());
				} 
			} 
		}
		return loteImpressaoAitFilter;
	}

	@Override
	public LoteImpressaoAitDTO getDTO(int cdAit, int tpDocumento, CustomConnection customConnection) throws Exception {
		return null;
	}


}
