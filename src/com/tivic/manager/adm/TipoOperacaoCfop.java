package com.tivic.manager.adm;

public class TipoOperacaoCfop {

	private int cdTipoOperacao;
	private int cdNaturezaOperacao;
	private int cdEmpresa;
	private int cdEstado;
	private int cdNcm;
	private int cdClassificacaoFiscal;

	public TipoOperacaoCfop(int cdTipoOperacao,
			int cdNaturezaOperacao,
			int cdEmpresa,
			int cdEstado,
			int cdNcm,
			int cdClassificacaoFiscal){
		setCdTipoOperacao(cdTipoOperacao);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setCdEmpresa(cdEmpresa);
		setCdEstado(cdEstado);
		setCdNcm(cdNcm);
		setCdClassificacaoFiscal(cdClassificacaoFiscal);
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdNcm(int cdNcm){
		this.cdNcm=cdNcm;
	}
	public int getCdNcm(){
		return this.cdNcm;
	}
	public void setCdClassificacaoFiscal(int cdClassificacaoFiscal){
		this.cdClassificacaoFiscal=cdClassificacaoFiscal;
	}
	public int getCdClassificacaoFiscal(){
		return this.cdClassificacaoFiscal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdEstado: " +  getCdEstado();
		valueToString += ", cdNcm: " +  getCdNcm();
		valueToString += ", cdClassificacaoFiscal: " +  getCdClassificacaoFiscal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOperacaoCfop(getCdTipoOperacao(),
			getCdNaturezaOperacao(),
			getCdEmpresa(),
			getCdEstado(),
			getCdNcm(),
			getCdClassificacaoFiscal());
	}

}