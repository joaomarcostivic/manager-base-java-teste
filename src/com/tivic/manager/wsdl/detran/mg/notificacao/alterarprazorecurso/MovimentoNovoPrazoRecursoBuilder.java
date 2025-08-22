package com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;

public class MovimentoNovoPrazoRecursoBuilder {

	private AitMovimento aitMovimento;
	
	public MovimentoNovoPrazoRecursoBuilder(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, int tpStatus) {
		aitMovimento = new AitMovimento();
		aitMovimento.setCdAit(alteraPrazoRecursoDTO.getCdAit());
		aitMovimento.setCdUsuario(alteraPrazoRecursoDTO.getCdUsuario());
		aitMovimento.setDtMovimento(new GregorianCalendar());
		aitMovimento.setTpStatus(tpStatus);
		aitMovimento.setDsObservacao(alteraPrazoRecursoDTO.getDsObservacao());
		
	}
	
	public AitMovimento build() {
		return this.aitMovimento;
	}
	
}
