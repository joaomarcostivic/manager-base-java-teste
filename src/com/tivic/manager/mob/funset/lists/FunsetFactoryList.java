package com.tivic.manager.mob.funset.lists;

import com.tivic.manager.mob.AitMovimentoServices;

public class FunsetFactoryList {
	public static FunsetList generate(Integer tpStatus) {
		if(tpStatus.intValue() == AitMovimentoServices.DEVOLUCAO_PAGAMENTO)
			return new FunsetListRestituicao();
		else
			return new FunsetListMultasPagas();
	}
}
