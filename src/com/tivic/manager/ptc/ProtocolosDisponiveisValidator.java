package com.tivic.manager.ptc;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;

public class ProtocolosDisponiveisValidator {
	
	private List<AitMovimento> movimentos;
	private List<Integer> statusMovimentos;
	
	public ProtocolosDisponiveisValidator() {}

	public ProtocolosDisponiveisValidator(List<AitMovimento> movimentos) {
		super();
		statusMovimentos = new ArrayList<Integer>();
		this.movimentos = movimentos;
		getStatusMovimentos();
	}
	
	public boolean naiLancada() {
		return movimentoLancado(AitMovimentoServices.NAI_ENVIADO);
	}
	
	public boolean ficiLancada() {
		return movimentoLancado(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
	
	public boolean entradaDefesaLancada() {
		return movimentoLancado(AitMovimentoServices.DEFESA_PREVIA);
	}
	
	public boolean entradaAdvertenciaDefesaLancada() {
		return movimentoLancado(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
	}
	
	public boolean resultadoDefesaLancada() {
		return movimentoLancado(AitMovimentoServices.DEFESA_DEFERIDA) || movimentoLancado(AitMovimentoServices.DEFESA_INDEFERIDA);
	}
	
	public boolean nipLancada() {
		return movimentoLancado(AitMovimentoServices.NIP_ENVIADA);
	}
	
	public boolean entradaCetranLancada() {
		return movimentoLancado(AitMovimentoServices.RECURSO_CETRAN);
	}
	
	public boolean resultadoCetranLancado() {
		return movimentoLancado(AitMovimentoServices.CETRAN_DEFERIDO) || movimentoLancado(AitMovimentoServices.CETRAN_INDEFERIDO);
	}
	
	public boolean entradaJariLancada() {
		return movimentoLancado(AitMovimentoServices.RECURSO_JARI);
	}
	
	public boolean resultadoJariLancado() {
		return movimentoLancado(AitMovimentoServices.JARI_COM_PROVIMENTO) || movimentoLancado(AitMovimentoServices.JARI_SEM_PROVIMENTO);
	}
	
	private void getStatusMovimentos() {
		movimentos.forEach(movimento -> {
			statusMovimentos.add(movimento.getTpStatus());
		});
	}
	
	private boolean movimentoLancado(int tpStatus) {
		if(statusMovimentos.contains(tpStatus))
			return true;
		
		return false;
	}
	

}
