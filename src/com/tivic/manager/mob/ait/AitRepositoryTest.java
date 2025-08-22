package com.tivic.manager.mob.ait;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Talonario;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AitRepositoryTest implements AitRepository {

	private List<Ait> aits;
	private int cdAit;
	
	public AitRepositoryTest() {
		this.aits = new ArrayList<Ait>();
		this.cdAit = 1;
	}
	
	@Override
	public Ait insert(Ait ait, CustomConnection customConnection) throws Exception {
		ait.setCdAit(this.cdAit++);
		this.aits.add(ait);
		return ait;
	}

	@Override
	public Ait update(Ait ait, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.aits.size(); i++) {
			Ait aitFromList = this.aits.get(i);
			if(aitFromList.getCdAit() == ait.getCdAit()) {
				this.aits.set(i, ait);
				return ait;
			}
		}
		return null;
	}

	@Override
	public Ait get(int cdAit) throws Exception {
		return get(cdAit, new CustomConnection());
	}

	@Override
	public Ait get(int cdAit, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.aits.size(); i++) {
			Ait aitFromList = this.aits.get(i);
			if(aitFromList.getCdAit() == cdAit) {
				return aitFromList;
			}
		}
		return null;
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.aits;
	}

	@Override
	public List<Ait> findByEvento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAit(String idAit, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getUltimoNrAitByTalao(Talonario talonario, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
