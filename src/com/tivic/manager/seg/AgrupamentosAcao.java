package com.tivic.manager.seg;

import java.util.ArrayList;
import java.util.List;

public class AgrupamentosAcao {
	
	List<AgrupamentoAcao> agrupamentoAcao;
	
	public AgrupamentosAcao() {
		agrupamentoAcao = new ArrayList<AgrupamentoAcao>();
	}

	public void setAgrupamentoAcao(List<AgrupamentoAcao> agrupamentoAcao) {
		this.agrupamentoAcao = agrupamentoAcao;
	}
	
	public List<AgrupamentoAcao> getAgrupamentoAcao() {
		return agrupamentoAcao;
	}
	
}
