package com.tivic.test.manager.mob.lotes.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteImpressaoAitRepositoryTest implements ILoteImpressaoAitRepository {

	private List<LoteImpressaoAit> lotesImpressao = new ArrayList<>();
	
	@Override
	public void insert(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception {
	    lotesImpressao.add(loteImpressaoAit);
	}
	
	@Override
	public void update(LoteImpressaoAit updatedLoteAit, CustomConnection customConnection) throws Exception {
	    lotesImpressao.replaceAll(loteImpressao -> 
	    	loteImpressao.getCdLoteImpressao() == updatedLoteAit.getCdLoteImpressao() && loteImpressao.getCdAit() == updatedLoteAit.getCdAit() ? updatedLoteAit : loteImpressao);
	}

	@Override
	public LoteImpressaoAit get(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
	    return lotesImpressao.stream()
	            .filter(loteImpressao -> loteImpressao.getCdLoteImpressao() == cdLoteImpressao && loteImpressao.getCdAit() == cdAit)
	            .findFirst()
	            .orElse(null);
	}

	@Override
	public List<LoteImpressaoAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<LoteImpressaoAit> filteredLotes = new ArrayList<LoteImpressaoAit>(this.lotesImpressao);
		for(ItemComparator itemComparator : searchCriterios.getCriterios()) {
			if(itemComparator.getColumn().equals("cd_lote_impressao")) {
				if(itemComparator.getTypeComparation() == ItemComparator.EQUAL) {
					filteredLotes = filteredLotes.stream().filter(loteImpressao -> 
						loteImpressao.getCdLoteImpressao() == Integer.parseInt(itemComparator.getValue())).collect(Collectors.toList());
				} 
			} 
		}
		return filteredLotes;
	}

	@Override
	public LoteImpressaoAit delete(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LoteImpressaoAit> findByCdLoteImpressao(int cdLoteImpressao) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
