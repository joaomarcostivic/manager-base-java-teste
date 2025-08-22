package com.tivic.manager.mob.ait.builders;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;

public class AitListBuilder {

	private List<Ait> aits;
	private Search<Ait> search;
	private IAitMovimentoService aitMovimentoService;
	
	public AitListBuilder(Search<Ait> search) throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		
		this.search = search;
		this.aits = this.search.getList(Ait.class);
	}
	
	public AitListBuilder movimentos() throws Exception{
		for(int i=0; i < this.aits.size(); i++) {
			Ait ait = this.aits.get(i);
			ait.setMovimentos(this.aitMovimentoService.getByAit(ait.getCdAit()));
			this.aits.set(i, ait);
		}
		return this;
	}

	public AitListBuilder movimentoAtual() throws Exception{
		for(int i=0; i < this.aits.size(); i++) {
			Ait ait = this.aits.get(i);
			ait.setMovimentoAtual(this.aitMovimentoService.get(ait.getCdMovimentoAtual(), ait.getCdAit()));
			this.aits.set(i, ait);
		}
		return this;
	}
	
	public List<Ait> build(){
		return this.aits;
	}
	
}
