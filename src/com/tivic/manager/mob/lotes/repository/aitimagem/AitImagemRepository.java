package com.tivic.manager.mob.lotes.repository.aitimagem;

import com.tivic.manager.mob.lotes.model.aitimagem.AitImagem;

public interface AitImagemRepository {
	public AitImagem getMelhorImagem(int cdAit) throws Exception;
}
