package com.tivic.manager.crt;

public class Empreendimento {

	private int cdEmpreendimento;
	private String nmEmpreendimento;
	private String txtEmpreendimento;
	private byte[] blbLogo;
	private String txtMemorial;
	private byte[] blbGeodados;
	private byte[] blbCapa;
	private int cdLocalArmazenamento;
	private int cdConstrutora;

	public Empreendimento(){ }

	public Empreendimento(int cdEmpreendimento,
			String nmEmpreendimento,
			String txtEmpreendimento,
			byte[] blbLogo,
			String txtMemorial,
			byte[] blbGeodados,
			byte[] blbCapa,
			int cdLocalArmazenamento,
			int cdConstrutora){
		setCdEmpreendimento(cdEmpreendimento);
		setNmEmpreendimento(nmEmpreendimento);
		setTxtEmpreendimento(txtEmpreendimento);
		setBlbLogo(blbLogo);
		setTxtMemorial(txtMemorial);
		setBlbGeodados(blbGeodados);
		setBlbCapa(blbCapa);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdConstrutora(cdConstrutora);
	}
	public void setCdEmpreendimento(int cdEmpreendimento){
		this.cdEmpreendimento=cdEmpreendimento;
	}
	public int getCdEmpreendimento(){
		return this.cdEmpreendimento;
	}
	public void setNmEmpreendimento(String nmEmpreendimento){
		this.nmEmpreendimento=nmEmpreendimento;
	}
	public String getNmEmpreendimento(){
		return this.nmEmpreendimento;
	}
	public void setTxtEmpreendimento(String txtEmpreendimento){
		this.txtEmpreendimento=txtEmpreendimento;
	}
	public String getTxtEmpreendimento(){
		return this.txtEmpreendimento;
	}
	public void setBlbLogo(byte[] blbLogo){
		this.blbLogo=blbLogo;
	}
	public byte[] getBlbLogo(){
		return this.blbLogo;
	}
	public void setTxtMemorial(String txtMemorial){
		this.txtMemorial=txtMemorial;
	}
	public String getTxtMemorial(){
		return this.txtMemorial;
	}
	public void setBlbGeodados(byte[] blbGeodados){
		this.blbGeodados=blbGeodados;
	}
	public byte[] getBlbGeodados(){
		return this.blbGeodados;
	}
	public void setBlbCapa(byte[] blbCapa){
		this.blbCapa=blbCapa;
	}
	public byte[] getBlbCapa(){
		return this.blbCapa;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setCdConstrutora(int cdConstrutora){
		this.cdConstrutora=cdConstrutora;
	}
	public int getCdConstrutora(){
		return this.cdConstrutora;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpreendimento: " +  getCdEmpreendimento();
		valueToString += ", nmEmpreendimento: " +  getNmEmpreendimento();
		valueToString += ", txtEmpreendimento: " +  getTxtEmpreendimento();
		valueToString += ", blbLogo: " +  getBlbLogo();
		valueToString += ", txtMemorial: " +  getTxtMemorial();
		valueToString += ", blbGeodados: " +  getBlbGeodados();
		valueToString += ", blbCapa: " +  getBlbCapa();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", cdConstrutora: " +  getCdConstrutora();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Empreendimento(getCdEmpreendimento(),
			getNmEmpreendimento(),
			getTxtEmpreendimento(),
			getBlbLogo(),
			getTxtMemorial(),
			getBlbGeodados(),
			getBlbCapa(),
			getCdLocalArmazenamento(),
			getCdConstrutora());
	}

}
