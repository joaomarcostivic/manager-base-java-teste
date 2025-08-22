package com.tivic.manager.agd;

public class TipoAgendamento {

	private int cdTipoAgendamento;
	private String nmTipoAgendamento;
	private int tpAgendamento;
	private int lgNegritoTexto;
	private String idCorTexto;
	private String idCorBackground;

	public TipoAgendamento() { }

	public TipoAgendamento(int cdTipoAgendamento,
			String nmTipoAgendamento,
			int tpAgendamento,
			int lgNegritoTexto,
			String idCorTexto,
			String idCorBackground) {
		setCdTipoAgendamento(cdTipoAgendamento);
		setNmTipoAgendamento(nmTipoAgendamento);
		setTpAgendamento(tpAgendamento);
		setLgNegritoTexto(lgNegritoTexto);
		setIdCorTexto(idCorTexto);
		setIdCorBackground(idCorBackground);
	}
	public void setCdTipoAgendamento(int cdTipoAgendamento){
		this.cdTipoAgendamento=cdTipoAgendamento;
	}
	public int getCdTipoAgendamento(){
		return this.cdTipoAgendamento;
	}
	public void setNmTipoAgendamento(String nmTipoAgendamento){
		this.nmTipoAgendamento=nmTipoAgendamento;
	}
	public String getNmTipoAgendamento(){
		return this.nmTipoAgendamento;
	}
	public void setTpAgendamento(int tpAgendamento){
		this.tpAgendamento=tpAgendamento;
	}
	public int getTpAgendamento(){
		return this.tpAgendamento;
	}
	public void setLgNegritoTexto(int lgNegritoTexto){
		this.lgNegritoTexto=lgNegritoTexto;
	}
	public int getLgNegritoTexto(){
		return this.lgNegritoTexto;
	}
	public void setIdCorTexto(String idCorTexto){
		this.idCorTexto=idCorTexto;
	}
	public String getIdCorTexto(){
		return this.idCorTexto;
	}
	public void setIdCorBackground(String idCorBackground){
		this.idCorBackground=idCorBackground;
	}
	public String getIdCorBackground(){
		return this.idCorBackground;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAgendamento: " +  getCdTipoAgendamento();
		valueToString += ", nmTipoAgendamento: " +  getNmTipoAgendamento();
		valueToString += ", tpAgendamento: " +  getTpAgendamento();
		valueToString += ", lgNegritoTexto: " +  getLgNegritoTexto();
		valueToString += ", idCorTexto: " +  getIdCorTexto();
		valueToString += ", idCorBackground: " +  getIdCorBackground();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAgendamento(getCdTipoAgendamento(),
			getNmTipoAgendamento(),
			getTpAgendamento(),
			getLgNegritoTexto(),
			getIdCorTexto(),
			getIdCorBackground());
	}

}