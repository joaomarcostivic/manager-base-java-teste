package com.tivic.manager.blb;

public class Publicacao {

	private int cdPublicacao;
	private int cdEditora;
	private String nmPublicacao;
	private String nrIsbn;
	private String nrEdicao;
	private String nrAnoPublicacao;
	private int tpPublicacao;
	private int nrVolume;
	private String txtSinopse;
	private String idPublicacao;
	private String nmSubtitulo;
	private String txtObservacao;

	public Publicacao(){ }

	public Publicacao(int cdPublicacao,
			int cdEditora,
			String nmPublicacao,
			String nrIsbn,
			String nrEdicao,
			String nrAnoPublicacao,
			int tpPublicacao,
			int nrVolume,
			String txtSinopse,
			String idPublicacao,
			String nmSubtitulo,
			String txtObservacao){
		setCdPublicacao(cdPublicacao);
		setCdEditora(cdEditora);
		setNmPublicacao(nmPublicacao);
		setNrIsbn(nrIsbn);
		setNrEdicao(nrEdicao);
		setNrAnoPublicacao(nrAnoPublicacao);
		setTpPublicacao(tpPublicacao);
		setNrVolume(nrVolume);
		setTxtSinopse(txtSinopse);
		setIdPublicacao(idPublicacao);
		setNmSubtitulo(nmSubtitulo);
		setTxtObservacao(txtObservacao);
	}
	public void setCdPublicacao(int cdPublicacao){
		this.cdPublicacao=cdPublicacao;
	}
	public int getCdPublicacao(){
		return this.cdPublicacao;
	}
	public void setCdEditora(int cdEditora){
		this.cdEditora=cdEditora;
	}
	public int getCdEditora(){
		return this.cdEditora;
	}
	public void setNmPublicacao(String nmPublicacao){
		this.nmPublicacao=nmPublicacao;
	}
	public String getNmPublicacao(){
		return this.nmPublicacao;
	}
	public void setNrIsbn(String nrIsbn){
		this.nrIsbn=nrIsbn;
	}
	public String getNrIsbn(){
		return this.nrIsbn;
	}
	public void setNrEdicao(String nrEdicao){
		this.nrEdicao=nrEdicao;
	}
	public String getNrEdicao(){
		return this.nrEdicao;
	}
	public void setNrAnoPublicacao(String nrAnoPublicacao){
		this.nrAnoPublicacao=nrAnoPublicacao;
	}
	public String getNrAnoPublicacao(){
		return this.nrAnoPublicacao;
	}
	public void setTpPublicacao(int tpPublicacao){
		this.tpPublicacao=tpPublicacao;
	}
	public int getTpPublicacao(){
		return this.tpPublicacao;
	}
	public void setNrVolume(int nrVolume){
		this.nrVolume=nrVolume;
	}
	public int getNrVolume(){
		return this.nrVolume;
	}
	public void setTxtSinopse(String txtSinopse){
		this.txtSinopse=txtSinopse;
	}
	public String getTxtSinopse(){
		return this.txtSinopse;
	}
	public void setIdPublicacao(String idPublicacao){
		this.idPublicacao=idPublicacao;
	}
	public String getIdPublicacao(){
		return this.idPublicacao;
	}
	public void setNmSubtitulo(String nmSubtitulo){
		this.nmSubtitulo=nmSubtitulo;
	}
	public String getNmSubtitulo(){
		return this.nmSubtitulo;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPublicacao: " +  getCdPublicacao();
		valueToString += ", cdEditora: " +  getCdEditora();
		valueToString += ", nmPublicacao: " +  getNmPublicacao();
		valueToString += ", nrIsbn: " +  getNrIsbn();
		valueToString += ", nrEdicao: " +  getNrEdicao();
		valueToString += ", nrAnoPublicacao: " +  getNrAnoPublicacao();
		valueToString += ", tpPublicacao: " +  getTpPublicacao();
		valueToString += ", nrVolume: " +  getNrVolume();
		valueToString += ", txtSinopse: " +  getTxtSinopse();
		valueToString += ", idPublicacao: " +  getIdPublicacao();
		valueToString += ", nmSubtitulo: " +  getNmSubtitulo();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Publicacao(getCdPublicacao(),
			getCdEditora(),
			getNmPublicacao(),
			getNrIsbn(),
			getNrEdicao(),
			getNrAnoPublicacao(),
			getTpPublicacao(),
			getNrVolume(),
			getTxtSinopse(),
			getIdPublicacao(),
			getNmSubtitulo(),
			getTxtObservacao());
	}

}
