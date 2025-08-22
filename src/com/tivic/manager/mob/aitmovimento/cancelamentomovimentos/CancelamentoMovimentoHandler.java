package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import com.tivic.sol.connection.CustomConnection;

public abstract class CancelamentoMovimentoHandler {
	protected CancelamentoMovimentoHandler nextGenerator;
	public void setNextGenerator(CancelamentoMovimentoHandler nextGenerator) {
		this.nextGenerator = nextGenerator;
	}
	public abstract void gerar(CustomConnection customConnection) throws Exception;
}
