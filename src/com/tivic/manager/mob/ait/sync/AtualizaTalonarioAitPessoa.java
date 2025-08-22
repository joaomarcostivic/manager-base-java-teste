package com.tivic.manager.mob.ait.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.aitpessoa.AitPessoa;
import com.tivic.manager.mob.talonario.ITalonarioService;
import com.tivic.manager.mob.talonario.TalonarioRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class AtualizaTalonarioAitPessoa implements IAtualizaTalonarioAitPessoa {
	private ITalonarioService talonarioService;
	private TalonarioRepository talonarioRepository;
	private Map<Integer, List<AitPessoa>> agrupados;
	
	public AtualizaTalonarioAitPessoa() throws Exception {
		talonarioService = (ITalonarioService) BeansFactory.get(ITalonarioService.class);
		talonarioRepository = (TalonarioRepository) BeansFactory.get(TalonarioRepository.class);
	}
	
	@Override
	public void atualizar(List<AitPessoa> aitPessoaList, CustomConnection customConnection) throws Exception {	
		agruparAit(aitPessoaList);
		for(List<AitPessoa> aitlist: agrupados.values()) {
			int nrAit = getMaiorNrAit(aitlist);
			Talonario talonario = talonarioService.get(aitlist.get(0).getCdTalao());
			if(talonario.getNrUltimoAit() < nrAit) {
				talonario.setNrUltimoAit(nrAit); 
				talonarioRepository.update(talonario, customConnection);
			}
		}
	}
	
	private void agruparAit(List<AitPessoa> aitList) throws Exception {
		Map<Integer, List<AitPessoa>> agrupados = new HashMap<>();
		
		for(AitPessoa ait: aitList) {
			int cdTalao = ait.getCdTalao();
			if(!agrupados.containsKey(cdTalao)) {
				agrupados.put(cdTalao, new ArrayList<>());
			}
			agrupados.get(cdTalao).add(ait);
		}
		this.agrupados = agrupados;
	}
	
	private int getMaiorNrAit(List<AitPessoa> aitList) {
	    return aitList.stream()
	            .mapToInt(AitPessoa::getNrAit)
	            .max()
	            .getAsInt();
	}

}
