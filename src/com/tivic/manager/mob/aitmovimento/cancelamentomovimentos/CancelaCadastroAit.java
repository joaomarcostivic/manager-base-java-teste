package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimento.CancelaCadastro;
import com.tivic.sol.connection.CustomConnection;

public class CancelaCadastroAit extends CancelamentoMovimentoHandler {
	
	private Ait ait;
	private AitMovimento aitMovimento;
	
	public CancelaCadastroAit(Ait ait, AitMovimento aitMovimento) throws Exception {
		this.ait = ait;
		this.aitMovimento = aitMovimento;
	}

	@Override
	public void gerar(CustomConnection customConnection) throws Exception {
		if(this.ait.getNrControle() == null || this.ait.getNrControle().trim().equals("")) {	
			new CancelaCadastro(ait, this.aitMovimento.getCdOcorrencia(), this.aitMovimento.getCdUsuario(), customConnection);
		} 
	}
}
