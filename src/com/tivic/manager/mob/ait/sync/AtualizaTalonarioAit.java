package com.tivic.manager.mob.ait.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.mob.talonario.enuns.SituacaoTalaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class AtualizaTalonarioAit implements IAtualizaTalonarioAit {

	private Map<Integer, List<Ait>> agrupados;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	
	public AtualizaTalonarioAit() throws Exception {
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
	}
	
	@Override
	public void atualizar(List<Ait> aitList, CustomConnection customConnection) throws Exception {	
		agruparAit(aitList);	
		for(List<Ait> list: agrupados.values()) {
			int nrAit = getMaiorNrAit(list);
			Talonario talonario = conversorBaseAntigaNovaFactory.getTalonarioRepository().get(list.get(0).getCdTalao(), customConnection);
			talonario.setNrUltimoAit(nrAit); 
			if (talonario.getNrFinal() == talonario.getNrUltimoAit()) {
				talonario.setStTalao(SituacaoTalaoEnum.ST_TALAO_CONCLUIDO.getKey());
			}
			conversorBaseAntigaNovaFactory.getTalonarioRepository().update(talonario, customConnection);
		}
	}
	
	private void agruparAit(List<Ait> aitList) throws Exception {
		Map<Integer, List<Ait>> agrupados = new HashMap<>();
		
		for(Ait ait: aitList) {
			int cdTalao = ait.getCdTalao();
			if(!agrupados.containsKey(cdTalao)) {
				agrupados.put(cdTalao, new ArrayList<>());
			}
			agrupados.get(cdTalao).add(ait);
		}
		this.agrupados = agrupados;
	}
	
	private int getMaiorNrAit(List<Ait> aitList) {
	    return aitList.stream()
	            .mapToInt(Ait::getNrAit)
	            .max()
	            .getAsInt();
	}
}
