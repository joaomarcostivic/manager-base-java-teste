package com.tivic.manager.mob;

public class TipoEvento {

	private int cdTipoEvento;
	private String nmTipoEvento;
	private String idTipoEvento;
	private String txtTipoEvento;
	private int cdInfracao;

	public TipoEvento() { }

	public TipoEvento(int cdTipoEvento,
			String nmTipoEvento,
			String idTipoEvento,
			String txtTipoEvento,
			int cdInfracao) {
		setCdTipoEvento(cdTipoEvento);
		setNmTipoEvento(nmTipoEvento);
		setIdTipoEvento(idTipoEvento);
		setTxtTipoEvento(txtTipoEvento);
		setCdInfracao(cdInfracao);
	}
	public void setCdTipoEvento(int cdTipoEvento){
		this.cdTipoEvento=cdTipoEvento;
	}
	public int getCdTipoEvento(){
		return this.cdTipoEvento;
	}
	public void setNmTipoEvento(String nmTipoEvento){
		this.nmTipoEvento=nmTipoEvento;
	}
	public String getNmTipoEvento(){
		return this.nmTipoEvento;
	}
	public void setIdTipoEvento(String idTipoEvento){
		this.idTipoEvento=idTipoEvento;
	}
	public String getIdTipoEvento(){
		return this.idTipoEvento;
	}
	public void setTxtTipoEvento(String txtTipoEvento){
		this.txtTipoEvento=txtTipoEvento;
	}
	public String getTxtTipoEvento(){
		return this.txtTipoEvento;
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoEvento: " +  getCdTipoEvento();
		valueToString += ", nmTipoEvento: " +  getNmTipoEvento();
		valueToString += ", idTipoEvento: " +  getIdTipoEvento();
		valueToString += ", txtTipoEvento: " +  getTxtTipoEvento();
		valueToString += ", cdInfracao: " +  getCdInfracao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoEvento(getCdTipoEvento(),
			getNmTipoEvento(),
			getIdTipoEvento(),
			getTxtTipoEvento(),
			getCdInfracao());
	}

}