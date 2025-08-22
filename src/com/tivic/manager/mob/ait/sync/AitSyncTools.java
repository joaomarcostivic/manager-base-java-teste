package com.tivic.manager.mob.ait.sync;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tivic.manager.mob.ait.sync.builders.AitSyncResponseBuilder;
import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;
import com.tivic.manager.mob.ait.sync.enuns.SituacaoExistEnum;
import com.tivic.manager.mob.ait.sync.enuns.SituacaoSyncEnum;

public class AitSyncTools implements IAitSyncTools<Object> {
	
	@Override
	public List<Object> verificarAitsDuplicados(List<Object> aitList) throws Exception {
		Set<Object> list = new HashSet<>(aitList);
		List<Object> aitsUnicos = new ArrayList<Object>(list);
		return aitsUnicos;
	}
	
	@Override
	public void setSincronizados(String idAit, List<AitSyncResponse> aitSyncResponseList, boolean sync, boolean stExist) {
		aitSyncResponseList.add(new AitSyncResponseBuilder()
				.setIdAit(idAit)
				.setStSync(sync ? SituacaoSyncEnum.SINCRONIZADO.getKey() 
								: SituacaoSyncEnum.NAO_SINCRONIZADO.getKey())
				.setStExist(stExist ? SituacaoExistEnum.EXISTE.getKey() 
								: SituacaoExistEnum.NAO_EXISTE.getKey())
				.build());
	}
}
