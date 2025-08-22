package com.tivic.manager.mcr;

public class ParecerAgente {

	private int cdParecer;
	private int cdPessoa;
	private String txtCarater;
	private String txtCapital;
	private String txtCondicaoNegocio;
	private String txtCapacidadePagamento;
	private String txtCollateral;
	private String txtConclusao;
	private float vlSugerido;
	private int nrParcelasSugerido;

	public ParecerAgente(int cdParecer,
			int cdPessoa,
			String txtCarater,
			String txtCapital,
			String txtCondicaoNegocio,
			String txtCapacidadePagamento,
			String txtCollateral,
			String txtConclusao,
			float vlSugerido,
			int nrParcelasSugerido){
		setCdParecer(cdParecer);
		setCdPessoa(cdPessoa);
		setTxtCarater(txtCarater);
		setTxtCapital(txtCapital);
		setTxtCondicaoNegocio(txtCondicaoNegocio);
		setTxtCapacidadePagamento(txtCapacidadePagamento);
		setTxtCollateral(txtCollateral);
		setTxtConclusao(txtConclusao);
		setVlSugerido(vlSugerido);
		setNrParcelasSugerido(nrParcelasSugerido);
	}
	public void setCdParecer(int cdParecer){
		this.cdParecer=cdParecer;
	}
	public int getCdParecer(){
		return this.cdParecer;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTxtCarater(String txtCarater){
		this.txtCarater=txtCarater;
	}
	public String getTxtCarater(){
		return this.txtCarater;
	}
	public void setTxtCapital(String txtCapital){
		this.txtCapital=txtCapital;
	}
	public String getTxtCapital(){
		return this.txtCapital;
	}
	public void setTxtCondicaoNegocio(String txtCondicaoNegocio){
		this.txtCondicaoNegocio=txtCondicaoNegocio;
	}
	public String getTxtCondicaoNegocio(){
		return this.txtCondicaoNegocio;
	}
	public void setTxtCapacidadePagamento(String txtCapacidadePagamento){
		this.txtCapacidadePagamento=txtCapacidadePagamento;
	}
	public String getTxtCapacidadePagamento(){
		return this.txtCapacidadePagamento;
	}
	public void setTxtCollateral(String txtCollateral){
		this.txtCollateral=txtCollateral;
	}
	public String getTxtCollateral(){
		return this.txtCollateral;
	}
	public void setTxtConclusao(String txtConclusao){
		this.txtConclusao=txtConclusao;
	}
	public String getTxtConclusao(){
		return this.txtConclusao;
	}
	public void setVlSugerido(float vlSugerido){
		this.vlSugerido=vlSugerido;
	}
	public float getVlSugerido(){
		return this.vlSugerido;
	}
	public void setNrParcelasSugerido(int nrParcelasSugerido){
		this.nrParcelasSugerido=nrParcelasSugerido;
	}
	public int getNrParcelasSugerido(){
		return this.nrParcelasSugerido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdParecer: " +  getCdParecer();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", txtCarater: " +  getTxtCarater();
		valueToString += ", txtCapital: " +  getTxtCapital();
		valueToString += ", txtCondicaoNegocio: " +  getTxtCondicaoNegocio();
		valueToString += ", txtCapacidadePagamento: " +  getTxtCapacidadePagamento();
		valueToString += ", txtCollateral: " +  getTxtCollateral();
		valueToString += ", txtConclusao: " +  getTxtConclusao();
		valueToString += ", vlSugerido: " +  getVlSugerido();
		valueToString += ", nrParcelasSugerido: " +  getNrParcelasSugerido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ParecerAgente(getCdParecer(),
			getCdPessoa(),
			getTxtCarater(),
			getTxtCapital(),
			getTxtCondicaoNegocio(),
			getTxtCapacidadePagamento(),
			getTxtCollateral(),
			getTxtConclusao(),
			getVlSugerido(),
			getNrParcelasSugerido());
	}

}
