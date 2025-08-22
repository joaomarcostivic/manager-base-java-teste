package com.tivic.manager.srh;

public class TabelaHorario {

	private int cdTabelaHorario;
	private String nmTabelaHorario;
	private String idTabelaHorario;
	private int qtHorasMes;
	private int stTabelaHorario;
	private int cdEmpresa;

	public TabelaHorario() { }

	public TabelaHorario(int cdTabelaHorario,
			String nmTabelaHorario,
			String idTabelaHorario,
			int qtHorasMes,
			int stTabelaHorario,
			int cdEmpresa) {
		setCdTabelaHorario(cdTabelaHorario);
		setNmTabelaHorario(nmTabelaHorario);
		setIdTabelaHorario(idTabelaHorario);
		setQtHorasMes(qtHorasMes);
		setStTabelaHorario(stTabelaHorario);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdTabelaHorario(int cdTabelaHorario){
		this.cdTabelaHorario=cdTabelaHorario;
	}
	public int getCdTabelaHorario(){
		return this.cdTabelaHorario;
	}
	public void setNmTabelaHorario(String nmTabelaHorario){
		this.nmTabelaHorario=nmTabelaHorario;
	}
	public String getNmTabelaHorario(){
		return this.nmTabelaHorario;
	}
	public void setIdTabelaHorario(String idTabelaHorario){
		this.idTabelaHorario=idTabelaHorario;
	}
	public String getIdTabelaHorario(){
		return this.idTabelaHorario;
	}
	public void setQtHorasMes(int qtHorasMes){
		this.qtHorasMes=qtHorasMes;
	}
	public int getQtHorasMes(){
		return this.qtHorasMes;
	}
	public void setStTabelaHorario(int stTabelaHorario){
		this.stTabelaHorario=stTabelaHorario;
	}
	public int getStTabelaHorario(){
		return this.stTabelaHorario;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", nmTabelaHorario: " +  getNmTabelaHorario();
		valueToString += ", idTabelaHorario: " +  getIdTabelaHorario();
		valueToString += ", qtHorasMes: " +  getQtHorasMes();
		valueToString += ", stTabelaHorario: " +  getStTabelaHorario();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaHorario(getCdTabelaHorario(),
			getNmTabelaHorario(),
			getIdTabelaHorario(),
			getQtHorasMes(),
			getStTabelaHorario(),
			getCdEmpresa());
	}

}