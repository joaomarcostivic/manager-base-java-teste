package com.tivic.manager.mob.ait;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.EfeitoSuspensivoDTO;
import com.tivic.manager.mob.TipoStatusEnum;

public class AitMovimentoSuspensaoBuilder {
private AitMovimento aitMovimento;
	
	public AitMovimentoSuspensaoBuilder(EfeitoSuspensivoDTO efeitoSuspensivoDTO) {
		aitMovimento = new AitMovimento();
		aitMovimento.setDtMovimento(new GregorianCalendar());
		aitMovimento.setCdAit(efeitoSuspensivoDTO.getCdAit());
		aitMovimento.setCdUsuario(efeitoSuspensivoDTO.getCdUsuario());
		aitMovimento.setNrProcesso(efeitoSuspensivoDTO.getNrProcesso());
		addTpStatus(efeitoSuspensivoDTO.getTpStatus());
	}
	
	public AitMovimentoSuspensaoBuilder addTpStatus(int tpStatus) {
		switch (tpStatus) {
			case AitMovimentoServices.RECURSO_JARI:
			case AitMovimentoServices.RECURSO_CETRAN:
				aitMovimento.setTpStatus(TipoStatusEnum.PENALIDADE_SUSPENSA.getKey());
				break;
			case AitMovimentoServices.PENALIDADE_SUSPENSA:
				aitMovimento.setTpStatus(TipoStatusEnum.PENALIDADE_REATIVADA.getKey());
		}		
		return this;
	}
	
	public AitMovimento build() {
		return aitMovimento;
	}
}
