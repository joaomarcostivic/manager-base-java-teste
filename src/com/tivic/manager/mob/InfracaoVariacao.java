package com.tivic.manager.mob;

public class InfracaoVariacao {

	private int cdVariacao;
	private int cdInfracao;
	private int cdInfracaoTransporte;
	private int tpVariacao;
	private float vlVariacao;
	private String txtProcedimentos;
	private int nrOrdem;
	private int tpMultiplicador;

	public InfracaoVariacao(){ }

	public InfracaoVariacao(int cdVariacao,
			int cdInfracao,
			int cdInfracaoTransporte,
			int tpVariacao,
			float vlVariacao,
			String txtProcedimentos,
			int nrOrdem,
			int tpMultiplicador){
		setCdVariacao(cdVariacao);
		setCdInfracao(cdInfracao);
		setCdInfracaoTransporte(cdInfracaoTransporte);
		setTpVariacao(tpVariacao);
		setVlVariacao(vlVariacao);
		setTxtProcedimentos(txtProcedimentos);
		setNrOrdem(nrOrdem);
		setTpMultiplicador(tpMultiplicador);
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
	public void setVlVariacao(float vlVariacao){
		this.vlVariacao=vlVariacao;
	}
	public float getVlVariacao(){
		return this.vlVariacao;
	}
	public void setTxtProcedimentos(String txtProcedimentos){
		this.txtProcedimentos=txtProcedimentos;
	}
	public String getTxtProcedimentos(){
		return this.txtProcedimentos;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setTpMultiplicador(int tpMultiplicador){
		this.tpMultiplicador=tpMultiplicador;
	}
	public int getTpMultiplicador(){
		return this.tpMultiplicador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVariacao: " +  getCdVariacao();
		valueToString += ", cdInfracao: " +  getCdInfracao();
		valueToString += ", cdInfracaoTransporte: " +  getCdInfracaoTransporte();
		valueToString += ", tpVariacao: " +  getTpVariacao();
		valueToString += ", vlVariacao: " +  getVlVariacao();
		valueToString += ", txtProcedimentos: " +  getTxtProcedimentos();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", tpMultiplicador: " +  getTpMultiplicador();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InfracaoVariacao(getCdVariacao(),
			getCdInfracao(),
			getCdInfracaoTransporte(),
			getTpVariacao(),
			getVlVariacao(),
			getTxtProcedimentos(),
			getNrOrdem(),
			getTpMultiplicador());
	}

}