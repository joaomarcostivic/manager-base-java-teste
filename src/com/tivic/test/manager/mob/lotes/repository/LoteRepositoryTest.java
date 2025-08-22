package com.tivic.test.manager.mob.lotes.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteRepositoryTest implements LoteRepository {
	private List<Lote> lotes = new ArrayList<>();

	@Override
	public void insert(Lote lote, CustomConnection customConnection) throws Exception {
	    int maxCdLote = lotes.stream()
	                         .map(Lote::getCdLote)
	                         .max(Integer::compareTo)
	                         .orElse(0);
	    lote.setCdLote(maxCdLote + 1);
	    lotes.add(lote);
	}
	public void update(Lote updatedLote, CustomConnection customConnection) throws Exception {
	    lotes.replaceAll(lote -> lote.getCdLote() == updatedLote.getCdLote() ? updatedLote : lote);
	}

	@Override
	public Lote get(int cdLote, CustomConnection customConnection) throws Exception {
	    return lotes.stream()
	            .filter(lote -> lote.getCdLote() == cdLote)
	            .findFirst()
	            .orElse(null);
	}


	@Override
	public List<Lote> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Lote> filteredLotes = new ArrayList<Lote>(this.lotes);
		for(ItemComparator itemComparator : searchCriterios.getCriterios()) {
			if(itemComparator.getColumn().equals("cd_lote")) {
				if(itemComparator.getTypeComparation() == ItemComparator.EQUAL) {
					filteredLotes = filteredLotes.stream().filter(lote -> lote.getCdLote() == Integer.parseInt(itemComparator.getValue())).collect(Collectors.toList());
				} 
			} 
		}
		return filteredLotes;
	}
	
	@Override
	public void delete(int cdLote, CustomConnection customConnection) throws Exception {
	    lotes.removeIf(lote -> lote.getCdLote() == cdLote);
	}

}
