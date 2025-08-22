package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.CancelaMultaAit;
import com.tivic.sol.connection.CustomConnection;

public class GeraCancelamento {
	
	private CancelaMultaAit cancelarMultaAit;
	private CancelaAutuacaoAit cancelarAutuacaoAit;
	private CancelaRegistroAit cancelarRegistroAit;
	private CancelaCadastroAit cancelaCadastroAit; 
	
	public GeraCancelamento(Ait ait, AitMovimento aitMovimento) throws Exception {
		this.cancelarMultaAit = new CancelaMultaAit(aitMovimento);
		this.cancelarAutuacaoAit = new CancelaAutuacaoAit(aitMovimento);
		this.cancelarRegistroAit = new CancelaRegistroAit(ait, aitMovimento);
		this.cancelaCadastroAit = new CancelaCadastroAit(ait, aitMovimento);
	}
	
	public void gerar(CustomConnection customConnection) throws Exception {
		this.cancelarMultaAit.setNextGenerator(this.cancelarAutuacaoAit);
		this.cancelarAutuacaoAit.setNextGenerator(this.cancelarRegistroAit);
		this.cancelarRegistroAit.setNextGenerator(this.cancelaCadastroAit);
		this.cancelarMultaAit.gerar(customConnection);
	}
}
