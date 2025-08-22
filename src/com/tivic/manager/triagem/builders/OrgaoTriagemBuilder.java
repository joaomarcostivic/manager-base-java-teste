package com.tivic.manager.triagem.builders;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.triagem.dtos.OrgaoTriagemDTO;

public class OrgaoTriagemBuilder {
	
	private OrgaoTriagemDTO orgaoTriagem;
	
	public OrgaoTriagemBuilder() {
		this.orgaoTriagem = new OrgaoTriagemDTO();
	}

	public OrgaoTriagemBuilder cdOrgao(int cdOrgao) {
        this.orgaoTriagem.setCdOrgao(cdOrgao);
        return this;
    }
	
	public OrgaoTriagemBuilder nmOrgao(String nmOrgao) {
        this.orgaoTriagem.setNmOrgao(nmOrgao);
        return this;
    }
	
	public OrgaoTriagemBuilder idOrgao(String idOrgao) {
        this.orgaoTriagem.setIdOrgao(idOrgao);
        return this;
    }
	
	public OrgaoTriagemBuilder cidade(Cidade cidade) {
        this.orgaoTriagem.setCidade(cidade);
        return this;
    }
	
	public OrgaoTriagemBuilder cdOrgaoAutuador(int cdOrgaoAutuador) {
        this.orgaoTriagem.setCdOrgaoAutuador(cdOrgaoAutuador);
        return this;
    }
	
	public OrgaoTriagemBuilder cdAgenteResponsavel(int cdAgenteResponsavel) {
        this.orgaoTriagem.setCdAgenteResponsavel(cdAgenteResponsavel);
        return this;
    }
	
	public OrgaoTriagemDTO build() {
		return orgaoTriagem;
	}
}
