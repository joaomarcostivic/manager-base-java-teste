package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class Orgao extends com.tivic.manager.grl.Pessoa {

	private int cdOrgao;
	private String nmOrgao;
	private String idOrgao;
	private int cdResponsavel;
	private int cdFuncaoResponsavel;	
	private int cdPessoaOrgao;
	private int cdCidade;
	private int cdAgenteResponsavel;
	private int lgEmitirAit;

	public Orgao() { }
	
	public Orgao(int cdOrgao, String nmOrgao, String idOrgao, int cdResponsavel, int cdFuncaoResponsavel,
			int cdPessoaOrgao, int cdCidade, int cdAgenteResponsavel, int lgEmitirAit) {
		super();
		this.cdOrgao = cdOrgao;
		this.nmOrgao = nmOrgao;
		this.idOrgao = idOrgao;
		this.cdResponsavel = cdResponsavel;
		this.cdFuncaoResponsavel = cdFuncaoResponsavel;
		this.cdPessoaOrgao = cdPessoaOrgao;
		this.cdCidade = cdCidade;
		this.cdAgenteResponsavel = cdAgenteResponsavel;
		this.lgEmitirAit = lgEmitirAit;
	}

	public Orgao(int cdPessoa,
			int cdPessoaSuperior,
			int cdPais,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			byte[] imgFoto,
			int stCadastro,
			String nmUrl,
			String nmApelido,
			String txtObservacao,
			int lgNotificacao,
			String idPessoa,
			int cdClassificacao,
			int cdFormaDivulgacao,
			GregorianCalendar dtChegadaPais,
			int cdUsuarioCadastro,
			String nrOab,
			String nmParceiro,
			String nmOrgao,
			String idOrgao,
			int cdResponsavel,
			int cdFuncaoResponsavel,
			int cdPessoaOrgao,
			int cdCidade,
			int cdAgenteResponsavel,
			int lgEmitirAit) {
		super(cdPessoa,
			cdPessoaSuperior,
			cdPais,
			nmPessoa,
			nrTelefone1,
			nrTelefone2,
			nrCelular,
			nrFax,
			nmEmail,
			dtCadastro,
			gnPessoa,
			imgFoto,
			stCadastro,
			nmUrl,
			nmApelido,
			txtObservacao,
			lgNotificacao,
			idPessoa,
			cdClassificacao,
			cdFormaDivulgacao,
			dtChegadaPais,
			cdUsuarioCadastro,
			nrOab,
			nmParceiro);
		setCdOrgao(cdPessoa);
		setNmOrgao(nmOrgao);
		setIdOrgao(idOrgao);
		setCdResponsavel(cdResponsavel);
		setCdFuncaoResponsavel(cdFuncaoResponsavel);
		setCdPessoaOrgao(cdPessoaOrgao);
		setCdCidade(cdCidade);
		setCdAgenteResponsavel(cdAgenteResponsavel);
		setLgEmitirAit(lgEmitirAit);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setNmOrgao(String nmOrgao){
		this.nmOrgao=nmOrgao;
	}
	public String getNmOrgao(){
		return this.nmOrgao;
	}
	public void setIdOrgao(String idOrgao){
		this.idOrgao=idOrgao;
	}
	public String getIdOrgao(){
		return this.idOrgao;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdFuncaoResponsavel(int cdFuncaoResponsavel){
		this.cdFuncaoResponsavel=cdFuncaoResponsavel;
	}
	public int getCdFuncaoResponsavel(){
		return this.cdFuncaoResponsavel;
	}
	public void setCdPessoaOrgao(int cdPessoaOrgao){
		this.cdPessoaOrgao=cdPessoaOrgao;
	}
	public int getCdPessoaOrgao(){
		return this.cdPessoaOrgao;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdAgenteResponsavel(int cdAgenteResponsavel){
		this.cdAgenteResponsavel=cdAgenteResponsavel;
	}
	public int getCdAgenteResponsavel(){
		return this.cdAgenteResponsavel;
	}
	public void setLgEmitirAit(int lgEmitirAit){
		this.lgEmitirAit=lgEmitirAit;
	}
	public int getLgEmitirAit(){
		return this.lgEmitirAit;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "\"cdOrgao\": " +  getCdOrgao();
		valueToString += ", \"nmOrgao\": \"" +  getNmOrgao() + "\"";
		valueToString += ", \"idOrgao\": \"" +  getIdOrgao() + "\"";
		valueToString += ", \"cdResponsavel\": " +  getCdResponsavel();
		valueToString += ", \"cdFuncaoResponsavel\": " +  getCdFuncaoResponsavel();
		valueToString += ", \"cdPessoaOrgao\": " +  getCdPessoaOrgao();
		valueToString += ", \"cdCidade\": " +  getCdCidade();
		valueToString += ", \"cdAgenteResponsavel\": " +  getCdAgenteResponsavel();
		valueToString += ", \"lgEmitirAit\": " +  getLgEmitirAit();
		return "{" + valueToString + "}";
	}
	
	public Object clone() {
		return new Orgao(getCdPessoa(),
			getCdPessoaSuperior(),
			getCdPais(),
			getNmPessoa(),
			getNrTelefone1(),
			getNrTelefone2(),
			getNrCelular(),
			getNrFax(),
			getNmEmail(),
			getDtCadastro(),
			getGnPessoa(),
			getImgFoto(),
			getStCadastro(),
			getNmUrl(),
			getNmApelido(),
			getTxtObservacao(),
			getLgNotificacao(),
			getIdPessoa(),
			getCdClassificacao(),
			getCdFormaDivulgacao(),
			getDtChegadaPais(),
			getCdUsuarioCadastro(),
			getNrOab(),
			getNmParceiro(),
			getNmOrgao(),
			getIdOrgao(),
			getCdResponsavel(),
			getCdFuncaoResponsavel(),
			getCdPessoaOrgao(),
			getCdCidade(),
			getCdAgenteResponsavel(),
			getLgEmitirAit());
	}

}