package com.tivic.manager.mcr;

public class CreditoSolicitado {

	private int cdCredito;
	private int cdPessoa;
	private String txtFinalidade;
	private int nrPrazo;
	private float vlSolicitado;
	private float vlMaximoParcela;
	private int nrDiaVenctoSugerido;
	private int lgGarantiaAval;
	private int lgGarantiaBem;
	private int lgGarantiaGrupoSolidario;

	public CreditoSolicitado(int cdCredito,
			int cdPessoa,
			String txtFinalidade,
			int nrPrazo,
			float vlSolicitado,
			float vlMaximoParcela,
			int nrDiaVenctoSugerido,
			int lgGarantiaAval,
			int lgGarantiaBem,
			int lgGarantiaGrupoSolidario){
		setCdCredito(cdCredito);
		setCdPessoa(cdPessoa);
		setTxtFinalidade(txtFinalidade);
		setNrPrazo(nrPrazo);
		setVlSolicitado(vlSolicitado);
		setVlMaximoParcela(vlMaximoParcela);
		setNrDiaVenctoSugerido(nrDiaVenctoSugerido);
		setLgGarantiaAval(lgGarantiaAval);
		setLgGarantiaBem(lgGarantiaBem);
		setLgGarantiaGrupoSolidario(lgGarantiaGrupoSolidario);
	}
	public void setCdCredito(int cdCredito){
		this.cdCredito=cdCredito;
	}
	public int getCdCredito(){
		return this.cdCredito;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTxtFinalidade(String txtFinalidade){
		this.txtFinalidade=txtFinalidade;
	}
	public String getTxtFinalidade(){
		return this.txtFinalidade;
	}
	public void setNrPrazo(int nrPrazo){
		this.nrPrazo=nrPrazo;
	}
	public int getNrPrazo(){
		return this.nrPrazo;
	}
	public void setVlSolicitado(float vlSolicitado){
		this.vlSolicitado=vlSolicitado;
	}
	public float getVlSolicitado(){
		return this.vlSolicitado;
	}
	public void setVlMaximoParcela(float vlMaximoParcela){
		this.vlMaximoParcela=vlMaximoParcela;
	}
	public float getVlMaximoParcela(){
		return this.vlMaximoParcela;
	}
	public void setNrDiaVenctoSugerido(int nrDiaVenctoSugerido){
		this.nrDiaVenctoSugerido=nrDiaVenctoSugerido;
	}
	public int getNrDiaVenctoSugerido(){
		return this.nrDiaVenctoSugerido;
	}
	public void setLgGarantiaAval(int lgGarantiaAval){
		this.lgGarantiaAval=lgGarantiaAval;
	}
	public int getLgGarantiaAval(){
		return this.lgGarantiaAval;
	}
	public void setLgGarantiaBem(int lgGarantiaBem){
		this.lgGarantiaBem=lgGarantiaBem;
	}
	public int getLgGarantiaBem(){
		return this.lgGarantiaBem;
	}
	public void setLgGarantiaGrupoSolidario(int lgGarantiaGrupoSolidario){
		this.lgGarantiaGrupoSolidario=lgGarantiaGrupoSolidario;
	}
	public int getLgGarantiaGrupoSolidario(){
		return this.lgGarantiaGrupoSolidario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCredito: " +  getCdCredito();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", txtFinalidade: " +  getTxtFinalidade();
		valueToString += ", nrPrazo: " +  getNrPrazo();
		valueToString += ", vlSolicitado: " +  getVlSolicitado();
		valueToString += ", vlMaximoParcela: " +  getVlMaximoParcela();
		valueToString += ", nrDiaVenctoSugerido: " +  getNrDiaVenctoSugerido();
		valueToString += ", lgGarantiaAval: " +  getLgGarantiaAval();
		valueToString += ", lgGarantiaBem: " +  getLgGarantiaBem();
		valueToString += ", lgGarantiaGrupoSolidario: " +  getLgGarantiaGrupoSolidario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CreditoSolicitado(getCdCredito(),
			getCdPessoa(),
			getTxtFinalidade(),
			getNrPrazo(),
			getVlSolicitado(),
			getVlMaximoParcela(),
			getNrDiaVenctoSugerido(),
			getLgGarantiaAval(),
			getLgGarantiaBem(),
			getLgGarantiaGrupoSolidario());
	}

}
