package com.tivic.manager.triagem.dtos;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.mob.Orgao;

public class OrgaoTriagemDTO extends Orgao {
	private Cidade cidade;
	private int cdOrgaoAutuador;

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	
	public int getCdOrgaoAutuador() {
		return cdOrgaoAutuador;
	}

	public void setCdOrgaoAutuador(int cdOrgaoAutuador) {
		this.cdOrgaoAutuador = cdOrgaoAutuador;
	}
}
