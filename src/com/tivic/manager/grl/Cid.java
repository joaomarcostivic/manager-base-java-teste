package com.tivic.manager.grl;

public class Cid {

	private int cdCid;
	private String nmCid;
	private String idCid;
	private int cdCidSuperior;
	private int tpClassificacao;
	private int tpSexo;
	private int lgNaoCausaObito;

	public Cid() { }

	public Cid(int cdCid,
			String nmCid,
			String idCid,
			int cdCidSuperior,
			int tpClassificacao,
			int tpSexo,
			int lgNaoCausaObito) {
		setCdCid(cdCid);
		setNmCid(nmCid);
		setIdCid(idCid);
		setCdCidSuperior(cdCidSuperior);
		setTpClassificacao(tpClassificacao);
		setTpSexo(tpSexo);
		setLgNaoCausaObito(lgNaoCausaObito);
	}
	public void setCdCid(int cdCid){
		this.cdCid=cdCid;
	}
	public int getCdCid(){
		return this.cdCid;
	}
	public void setNmCid(String nmCid){
		this.nmCid=nmCid;
	}
	public String getNmCid(){
		return this.nmCid;
	}
	public void setIdCid(String idCid){
		this.idCid=idCid;
	}
	public String getIdCid(){
		return this.idCid;
	}
	public void setCdCidSuperior(int cdCidSuperior){
		this.cdCidSuperior=cdCidSuperior;
	}
	public int getCdCidSuperior(){
		return this.cdCidSuperior;
	}
	public void setTpClassificacao(int tpClassificacao){
		this.tpClassificacao=tpClassificacao;
	}
	public int getTpClassificacao(){
		return this.tpClassificacao;
	}
	public void setTpSexo(int tpSexo){
		this.tpSexo=tpSexo;
	}
	public int getTpSexo(){
		return this.tpSexo;
	}
	public void setLgNaoCausaObito(int lgNaoCausaObito){
		this.lgNaoCausaObito=lgNaoCausaObito;
	}
	public int getLgNaoCausaObito(){
		return this.lgNaoCausaObito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCid: " +  getCdCid();
		valueToString += ", nmCid: " +  getNmCid();
		valueToString += ", idCid: " +  getIdCid();
		valueToString += ", cdCidSuperior: " +  getCdCidSuperior();
		valueToString += ", tpClassificacao: " +  getTpClassificacao();
		valueToString += ", tpSexo: " +  getTpSexo();
		valueToString += ", lgNaoCausaObito: " +  getLgNaoCausaObito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cid(getCdCid(),
			getNmCid(),
			getIdCid(),
			getCdCidSuperior(),
			getTpClassificacao(),
			getTpSexo(),
			getLgNaoCausaObito());
	}

}