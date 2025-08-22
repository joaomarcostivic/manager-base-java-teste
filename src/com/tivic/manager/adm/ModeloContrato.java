package com.tivic.manager.adm;


public class ModeloContrato extends com.tivic.manager.grl.ModeloDocumento {

	private int cdModeloContrato;
	private int cdIndicador;
	private int nrParcelas;
	private float vlAdesao;
	private float prJurosMora;
	private float prMultaMora;
	private float prDesconto;

	public ModeloContrato(int cdModelo,
			String nmModelo,
			String txtModelo,
			int tpModelo,
			byte[] blbConteudo,
			String txtConteudo,
			int stModelo,
			int cdTipoDocumento,
			int cdIndicador,
			int nrParcelas,
			float vlAdesao,
			float prJurosMora,
			float prMultaMora,
			float prDesconto){
		super(cdModelo,
			nmModelo,
			txtModelo,
			tpModelo,
			blbConteudo,
			txtConteudo,
			stModelo,
			null,
			cdTipoDocumento,
			null, 
			null, 
			null, 
			0);

		setCdModeloContrato(cdModelo);
		setCdIndicador(cdIndicador);
		setNrParcelas(nrParcelas);
		setVlAdesao(vlAdesao);
		setPrJurosMora(prJurosMora);
		setPrMultaMora(prMultaMora);
		setPrDesconto(prDesconto);
	}
	public void setCdModeloContrato(int cdModeloContrato){
		this.cdModeloContrato=cdModeloContrato;
	}
	public int getCdModeloContrato(){
		return this.cdModeloContrato;
	}
	public void setCdIndicador(int cdIndicador){
		this.cdIndicador=cdIndicador;
	}
	public int getCdIndicador(){
		return this.cdIndicador;
	}
	public void setNrParcelas(int nrParcelas){
		this.nrParcelas=nrParcelas;
	}
	public int getNrParcelas(){
		return this.nrParcelas;
	}
	public void setVlAdesao(float vlAdesao){
		this.vlAdesao=vlAdesao;
	}
	public float getVlAdesao(){
		return this.vlAdesao;
	}
	public void setPrJurosMora(float prJurosMora){
		this.prJurosMora=prJurosMora;
	}
	public float getPrJurosMora(){
		return this.prJurosMora;
	}
	public void setPrMultaMora(float prMultaMora){
		this.prMultaMora=prMultaMora;
	}
	public float getPrMultaMora(){
		return this.prMultaMora;
	}
	public void setPrDesconto(float prDesconto){
		this.prDesconto=prDesconto;
	}
	public float getPrDesconto(){
		return this.prDesconto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModeloContrato: " +  getCdModeloContrato();
		valueToString += ", cdIndicador: " +  getCdIndicador();
		valueToString += ", nrParcelas: " +  getNrParcelas();
		valueToString += ", vlAdesao: " +  getVlAdesao();
		valueToString += ", prJurosMora: " +  getPrJurosMora();
		valueToString += ", prMultaMora: " +  getPrMultaMora();
		valueToString += ", prDesconto: " +  getPrDesconto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ModeloContrato(getCdModelo(),
			getNmModelo(),
			getTxtModelo(),
			getTpModelo(),
			getBlbConteudo(),
			getTxtConteudo(),
			getStModelo(),
			getCdTipoDocumento(),
			getCdIndicador(),
			getNrParcelas(),
			getVlAdesao(),
			getPrJurosMora(),
			getPrMultaMora(),
			getPrDesconto());
	}

}
