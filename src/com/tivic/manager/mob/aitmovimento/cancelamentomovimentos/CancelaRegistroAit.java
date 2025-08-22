package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimento.CancelaRegistro;
import com.tivic.sol.connection.CustomConnection;

public class CancelaRegistroAit extends CancelamentoMovimentoHandler {
	
	private Ait ait;
	private AitMovimento aitMovimento;
	
	public CancelaRegistroAit(Ait ait, AitMovimento aitMovimento) throws Exception {
		this.ait = ait;
		this.aitMovimento = aitMovimento;
	}

	@Override
	public void gerar(CustomConnection customConnection) throws Exception {
		if(ait.getNrControle() != null) {
			new CancelaRegistro(aitMovimento, customConnection);
			nextGenerator.gerar(customConnection);				
		} else {
			nextGenerator.gerar(customConnection);
		}
	}
}