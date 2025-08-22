package com.tivic.manager.mob.lotes.repository.aitmovimento;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.model.aitmovimento.AitMovimento;

public interface AitMovimentoRepository {
	public AitMovimento getByTpStatus(int cdAit, TipoStatusEnum tpStatus) throws Exception;
}
