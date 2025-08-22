package com.tivic.manager.mob.guiapagamento.geraguiapagamento;

import java.util.List;

import com.tivic.manager.mob.Ait;

public interface IGeraGuiaPagamento {

	public byte[] gerarGuiaPagamento(List<Ait> ait) throws Exception;
	
}
