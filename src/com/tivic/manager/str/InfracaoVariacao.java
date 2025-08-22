package com.tivic.manager.str;

public class InfracaoVariacao {

	private int cdVariacao;
	private int cdInfracao;
	private int cdInfracaoTransporte;
	private int tpVariacao;
	private double vlVariacao;
	private int tpMultiplicador;
	private int nrOrdem;
	private String txtProcedimento;

	public InfracaoVariacao(){ }

	public InfracaoVariacao(int cdVariacao,
			int cdInfracao,
			int cdInfracaoTransporte,
			int tpVariacao,
			double vlVariacao,
			int tpMultiplicador,
			int nrOrdem,
			String txtProcedimento){
		setCdVariacao(cdVariacao);
		setCdInfracao(cdInfracao);
		setCdInfracaoTransporte(cdInfracaoTransporte);
		setTpVariacao(tpVariacao);
		setVlVariacao(vlVariacao);
		setTpMultiplicador(tpMultiplicador);
		setNrOrdem(nrOrdem);
		setTxtProcedimento(txtProcedimento);
	}
	public void setCdVariacao(int cdVariacao){
		this.cdVariacao=cdVariacao;
	}
	public int getCdVariacao(){
		return this.cdVariacao;
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setCdInfracaoTransporte(int cdInfracaoTransporte){
		this.cdInfracaoTransporte=cdInfracaoTransporte;
	}
	public int getCdInfracaoTransporte(){
		return this.cdInfracaoTransporte;
	}
	public void setTpVariacao(int tpVariacao){
		this.tpVariacao=tpVariacao;
	}
	public int getTpVariacao(){
		return this.tpVariacao;
	}
	public void setVlVariacao(double vlVariacao){
		this.vlVariacao=vlVariacao;
	}
	public double getVlVariacao(){
		return this.vlVariacao;
	}
	public void setTpMultiplicador(int tpMultiplicador){
		this.tpMultiplicador=tpMultiplicador;
	}
	public int getTpMultiplicador(){
		return this.tpMultiplicador;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setTxtProcedimento(String txtProcedimento){
		this.txtProcedimento=txtProcedimento;
	}
	public String getTxtProcedimento(){
		return this.txtProcedimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVariacao: " +  getCdVariacao();
		valueToString += ", cdInfracao: " +  getCdInfracao();
		valueToString += ", cdInfracaoTransporte: " +  getCdInfracaoTransporte();
		valueToString += ", tpVariacao: " +  getTpVariacao();
		valueToString += ", vlVariacao: " +  getVlVariacao();
		valueToString += ", tpMultiplicador: " +  getTpMultiplicador();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", txtProcedimento: " +  getTxtProcedimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InfracaoVariacao(getCdVariacao(),
			getCdInfracao(),
			getCdInfracaoTransporte(),
			getTpVariacao(),
			getVlVariacao(),
			getTpMultiplicador(),
			getNrOrdem(),
			getTxtProcedimento());
	}

}
