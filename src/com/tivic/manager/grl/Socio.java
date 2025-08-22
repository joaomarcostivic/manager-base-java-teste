package com.tivic.manager.grl;

public class Socio {

	private int cdSocio;
	private int cdPessoa;
	private int cdResponsavel;
	private int cdPaisDipj;
	private String nmEmailRelatorio;
	private int tpRepresentanteLegal;
	private float prCapital;
	private String txtObservacao;
	private int tpRegimeMatrimonial;

	public Socio(int cdSocio,
			int cdPessoa,
			int cdResponsavel,
			int cdPaisDipj,
			String nmEmailRelatorio,
			int tpRepresentanteLegal,
			float prCapital,
			String txtObservacao,
			int tpRegimeMatrimonial){
		setCdSocio(cdSocio);
		setCdPessoa(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setCdPaisDipj(cdPaisDipj);
		setNmEmailRelatorio(nmEmailRelatorio);
		setTpRepresentanteLegal(tpRepresentanteLegal);
		setPrCapital(prCapital);
		setTxtObservacao(txtObservacao);
		setTpRegimeMatrimonial(tpRegimeMatrimonial);
	}
	public void setCdSocio(int cdSocio){
		this.cdSocio=cdSocio;
	}
	public int getCdSocio(){
		return this.cdSocio;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdPaisDipj(int cdPaisDipj){
		this.cdPaisDipj=cdPaisDipj;
	}
	public int getCdPaisDipj(){
		return this.cdPaisDipj;
	}
	public void setNmEmailRelatorio(String nmEmailRelatorio){
		this.nmEmailRelatorio=nmEmailRelatorio;
	}
	public String getNmEmailRelatorio(){
		return this.nmEmailRelatorio;
	}
	public void setTpRepresentanteLegal(int tpRepresentanteLegal){
		this.tpRepresentanteLegal=tpRepresentanteLegal;
	}
	public int getTpRepresentanteLegal(){
		return this.tpRepresentanteLegal;
	}
	public void setPrCapital(float prCapital){
		this.prCapital=prCapital;
	}
	public float getPrCapital(){
		return this.prCapital;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setTpRegimeMatrimonial(int tpRegimeMatrimonial){
		this.tpRegimeMatrimonial=tpRegimeMatrimonial;
	}
	public int getTpRegimeMatrimonial(){
		return this.tpRegimeMatrimonial;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSocio: " +  getCdSocio();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdPaisDipj: " +  getCdPaisDipj();
		valueToString += ", nmEmailRelatorio: " +  getNmEmailRelatorio();
		valueToString += ", tpRepresentanteLegal: " +  getTpRepresentanteLegal();
		valueToString += ", prCapital: " +  getPrCapital();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", tpRegimeMatrimonial: " +  getTpRegimeMatrimonial();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Socio(getCdSocio(),
			getCdPessoa(),
			getCdResponsavel(),
			getCdPaisDipj(),
			getNmEmailRelatorio(),
			getTpRepresentanteLegal(),
			getPrCapital(),
			getTxtObservacao(),
			getTpRegimeMatrimonial());
	}

}
