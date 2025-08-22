package com.tivic.manager.grl;

public class PessoaCid {

	private int cdPessoa;
	private int cdCid;

	public PessoaCid() { }

	public PessoaCid(int cdPessoa,
			int cdCid) {
		setCdPessoa(cdPessoa);
		setCdCid(cdCid);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdCid(int cdCid){
		this.cdCid=cdCid;
	}
	public int getCdCid(){
		return this.cdCid;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdCid: " +  getCdCid();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaCid(getCdPessoa(),
			getCdCid());
	}

}