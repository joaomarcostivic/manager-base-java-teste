package com.tivic.manager.mob.orgao.builders;

import com.tivic.manager.mob.Orgao;

public class OrgaoBuilder {
	
	private Orgao orgao;
	
	public OrgaoBuilder() {
		this.orgao = new Orgao();
	}
	
	public OrgaoBuilder cdOrgao(int cdOrgao) {
		this.orgao.setCdOrgao(cdOrgao);
		return this;
	}

	public OrgaoBuilder nmOrgao(String nmOrgao) {
		this.orgao.setNmOrgao(nmOrgao);
		return this;
	}

	public OrgaoBuilder idOrgao(String idOrgao) {
		this.orgao.setIdOrgao(idOrgao);
		return this;
	}

	public OrgaoBuilder cdResponsavel(int cdResponsavel) {
		this.orgao.setCdResponsavel(cdResponsavel);
		return this;
	}

	public OrgaoBuilder cdFuncaoResponsavel(int cdFuncaoResponsavel) {
		this.orgao.setCdFuncaoResponsavel(cdFuncaoResponsavel);
		return this;
	}	

	public OrgaoBuilder cdPessoaOrgao(int cdPessoaOrgao) {
		this.orgao.setCdPessoaOrgao(cdPessoaOrgao);
		return this;
	}

	public OrgaoBuilder cdCidade(int cdCidade) {
		this.orgao.setCdCidade(cdCidade);
		return this;
	}

	public OrgaoBuilder cdAgenteResponsavel(int cdAgenteResponsavel) {
		this.orgao.setCdAgenteResponsavel(cdAgenteResponsavel);
		return this;
	}

	public OrgaoBuilder lgEmitirAit(int lgEmitirAit) {
		this.orgao.setLgEmitirAit(lgEmitirAit);
		return this;
	}
	
	public Orgao build() {
		return orgao;
	}
}
