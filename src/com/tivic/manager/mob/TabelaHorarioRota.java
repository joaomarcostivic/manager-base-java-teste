package com.tivic.manager.mob;

public class TabelaHorarioRota {

	private int cdLinha;
	private int cdTabelaHorario;
	private int cdRota;
	private int cdVariacao;

	public TabelaHorarioRota() { }

	public TabelaHorarioRota(int cdLinha,
			int cdTabelaHorario,
			int cdRota,
			int cdVariacao) {
		setCdLinha(cdLinha);
		setCdTabelaHorario(cdTabelaHorario);
		setCdRota(cdRota);
		setCdVariacao(cdVariacao);
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdTabelaHorario(int cdTabelaHorario){
		this.cdTabelaHorario=cdTabelaHorario;
	}
	public int getCdTabelaHorario(){
		return this.cdTabelaHorario;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setCdVariacao(int cdVariacao){
		this.cdVariacao=cdVariacao;
	}
	public int getCdVariacao(){
		return this.cdVariacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLinha: " +  getCdLinha();
		valueToString += ", cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdVariacao: " +  getCdVariacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaHorarioRota(getCdLinha(),
			getCdTabelaHorario(),
			getCdRota(),
			getCdVariacao());
	}

}