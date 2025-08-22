package com.tivic.manager.ctb;

public class LancamentoAuto {

	private int cdLancamentoAuto;
	private int cdContaDebito;
	private int cdContaCredito;
	private int cdHistorico;
	private int cdEmpresa;
	private String nmLancamentoAuto;
	private String txtHistorico;
	private int stLancamentoAuto;
	private String idLancamentoAuto;
	private int cdCentroCustoDebito;
	private int cdCentroCustoCredito;

	public LancamentoAuto(int cdLancamentoAuto,
			int cdContaDebito,
			int cdContaCredito,
			int cdHistorico,
			int cdEmpresa,
			String nmLancamentoAuto,
			String txtHistorico,
			int stLancamentoAuto,
			String idLancamentoAuto,
			int cdCentroCustoDebito,
			int cdCentroCustoCredito){
		setCdLancamentoAuto(cdLancamentoAuto);
		setCdContaDebito(cdContaDebito);
		setCdContaCredito(cdContaCredito);
		setCdHistorico(cdHistorico);
		setCdEmpresa(cdEmpresa);
		setNmLancamentoAuto(nmLancamentoAuto);
		setTxtHistorico(txtHistorico);
		setStLancamentoAuto(stLancamentoAuto);
		setIdLancamentoAuto(idLancamentoAuto);
		setCdCentroCustoDebito(cdCentroCustoDebito);
		setCdCentroCustoCredito(cdCentroCustoCredito);
	}
	public void setCdLancamentoAuto(int cdLancamentoAuto){
		this.cdLancamentoAuto=cdLancamentoAuto;
	}
	public int getCdLancamentoAuto(){
		return this.cdLancamentoAuto;
	}
	public void setCdContaDebito(int cdContaDebito){
		this.cdContaDebito=cdContaDebito;
	}
	public int getCdContaDebito(){
		return this.cdContaDebito;
	}
	public void setCdContaCredito(int cdContaCredito){
		this.cdContaCredito=cdContaCredito;
	}
	public int getCdContaCredito(){
		return this.cdContaCredito;
	}
	public void setCdHistorico(int cdHistorico){
		this.cdHistorico=cdHistorico;
	}
	public int getCdHistorico(){
		return this.cdHistorico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmLancamentoAuto(String nmLancamentoAuto){
		this.nmLancamentoAuto=nmLancamentoAuto;
	}
	public String getNmLancamentoAuto(){
		return this.nmLancamentoAuto;
	}
	public void setTxtHistorico(String txtHistorico){
		this.txtHistorico=txtHistorico;
	}
	public String getTxtHistorico(){
		return this.txtHistorico;
	}
	public void setStLancamentoAuto(int stLancamentoAuto){
		this.stLancamentoAuto=stLancamentoAuto;
	}
	public int getStLancamentoAuto(){
		return this.stLancamentoAuto;
	}
	public void setIdLancamentoAuto(String idLancamentoAuto){
		this.idLancamentoAuto=idLancamentoAuto;
	}
	public String getIdLancamentoAuto(){
		return this.idLancamentoAuto;
	}
	public void setCdCentroCustoDebito(int cdCentroCustoDebito){
		this.cdCentroCustoDebito=cdCentroCustoDebito;
	}
	public int getCdCentroCustoDebito(){
		return this.cdCentroCustoDebito;
	}
	public void setCdCentroCustoCredito(int cdCentroCustoCredito){
		this.cdCentroCustoCredito=cdCentroCustoCredito;
	}
	public int getCdCentroCustoCredito(){
		return this.cdCentroCustoCredito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLancamentoAuto: " +  getCdLancamentoAuto();
		valueToString += ", cdContaDebito: " +  getCdContaDebito();
		valueToString += ", cdContaCredito: " +  getCdContaCredito();
		valueToString += ", cdHistorico: " +  getCdHistorico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmLancamentoAuto: " +  getNmLancamentoAuto();
		valueToString += ", txtHistorico: " +  getTxtHistorico();
		valueToString += ", stLancamentoAuto: " +  getStLancamentoAuto();
		valueToString += ", idLancamentoAuto: " +  getIdLancamentoAuto();
		valueToString += ", cdCentroCustoDebito: " +  getCdCentroCustoDebito();
		valueToString += ", cdCentroCustoCredito: " +  getCdCentroCustoCredito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LancamentoAuto(getCdLancamentoAuto(),
			getCdContaDebito(),
			getCdContaCredito(),
			getCdHistorico(),
			getCdEmpresa(),
			getNmLancamentoAuto(),
			getTxtHistorico(),
			getStLancamentoAuto(),
			getIdLancamentoAuto(),
			getCdCentroCustoDebito(),
			getCdCentroCustoCredito());
	}

}
