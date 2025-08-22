package com.tivic.manager.grl;

public class FormularioAtributo {

	private int cdFormularioAtributo;
	private int cdFormulario;
	private String nmAtributo;
	private String sgAtributo;
	private int lgObrigatorio;
	private int tpDado;
	private int nrCasasDecimais;
	private int nrOrdem;
	private float vlMaximo;
	private float vlMinimo;
	private String txtFormula;
	private String idAtributo;
	private int tpRestricaoPessoa;
	private int cdVinculo;
	private int cdUnidadeMedida;
	
	public FormularioAtributo(){ }
	
	public FormularioAtributo(int cdFormularioAtributo,
			int cdFormulario,
			String nmAtributo,
			String sgAtributo,
			int lgObrigatorio,
			int tpDado,
			int nrCasasDecimais,
			int nrOrdem,
			float vlMaximo,
			float vlMinimo,
			String txtFormula,
			String idAtributo,
			int tpRestricaoPessoa,
			int cdVinculo,
			int cdUnidadeMedida){
		setCdFormularioAtributo(cdFormularioAtributo);
		setCdFormulario(cdFormulario);
		setNmAtributo(nmAtributo);
		setSgAtributo(sgAtributo);
		setLgObrigatorio(lgObrigatorio);
		setTpDado(tpDado);
		setNrCasasDecimais(nrCasasDecimais);
		setNrOrdem(nrOrdem);
		setVlMaximo(vlMaximo);
		setVlMinimo(vlMinimo);
		setTxtFormula(txtFormula);
		setIdAtributo(idAtributo);
		setTpRestricaoPessoa(tpRestricaoPessoa);
		setCdVinculo(cdVinculo);
		setCdUnidadeMedida(cdUnidadeMedida);
	}
	public void setCdFormularioAtributo(int cdFormularioAtributo){
		this.cdFormularioAtributo=cdFormularioAtributo;
	}
	public int getCdFormularioAtributo(){
		return this.cdFormularioAtributo;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setNmAtributo(String nmAtributo){
		this.nmAtributo=nmAtributo;
	}
	public String getNmAtributo(){
		return this.nmAtributo;
	}
	public void setSgAtributo(String sgAtributo){
		this.sgAtributo=sgAtributo;
	}
	public String getSgAtributo(){
		return this.sgAtributo;
	}
	public void setLgObrigatorio(int lgObrigatorio){
		this.lgObrigatorio=lgObrigatorio;
	}
	public int getLgObrigatorio(){
		return this.lgObrigatorio;
	}
	public void setTpDado(int tpDado){
		this.tpDado=tpDado;
	}
	public int getTpDado(){
		return this.tpDado;
	}
	public void setNrCasasDecimais(int nrCasasDecimais){
		this.nrCasasDecimais=nrCasasDecimais;
	}
	public int getNrCasasDecimais(){
		return this.nrCasasDecimais;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setVlMaximo(float vlMaximo){
		this.vlMaximo=vlMaximo;
	}
	public float getVlMaximo(){
		return this.vlMaximo;
	}
	public void setVlMinimo(float vlMinimo){
		this.vlMinimo=vlMinimo;
	}
	public float getVlMinimo(){
		return this.vlMinimo;
	}
	public void setTxtFormula(String txtFormula){
		this.txtFormula=txtFormula;
	}
	public String getTxtFormula(){
		return this.txtFormula;
	}
	public void setIdAtributo(String idAtributo){
		this.idAtributo=idAtributo;
	}
	public String getIdAtributo(){
		return this.idAtributo;
	}
	public void setTpRestricaoPessoa(int tpRestricaoPessoa){
		this.tpRestricaoPessoa=tpRestricaoPessoa;
	}
	public int getTpRestricaoPessoa(){
		return this.tpRestricaoPessoa;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormularioAtributo: " +  getCdFormularioAtributo();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", nmAtributo: " +  getNmAtributo();
		valueToString += ", sgAtributo: " +  getSgAtributo();
		valueToString += ", lgObrigatorio: " +  getLgObrigatorio();
		valueToString += ", tpDado: " +  getTpDado();
		valueToString += ", nrCasasDecimais: " +  getNrCasasDecimais();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", vlMaximo: " +  getVlMaximo();
		valueToString += ", vlMinimo: " +  getVlMinimo();
		valueToString += ", txtFormula: " +  getTxtFormula();
		valueToString += ", idAtributo: " +  getIdAtributo();
		valueToString += ", tpRestricaoPessoa: " +  getTpRestricaoPessoa();
		valueToString += ", cdVinculo: " +  getCdVinculo();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormularioAtributo(getCdFormularioAtributo(),
			getCdFormulario(),
			getNmAtributo(),
			getSgAtributo(),
			getLgObrigatorio(),
			getTpDado(),
			getNrCasasDecimais(),
			getNrOrdem(),
			getVlMaximo(),
			getVlMinimo(),
			getTxtFormula(),
			getIdAtributo(),
			getTpRestricaoPessoa(),
			getCdVinculo(),
			getCdUnidadeMedida());
	}

}
