package com.tivic.manager.mob;

import com.tivic.manager.grl.Pessoa;

public class BoatTestemunha {

	private int cdBoat;
	private int cdPessoa;
	
	private String nrRg;
	private String sgOrgaoRg;
	private String txtEndereco;

	private Pessoa pessoa;
	
	public BoatTestemunha() { }

	public BoatTestemunha(int cdBoat,
			int cdPessoa,
			String nrRg,
			String sgOrgaoRg,
			String txtEndereco) {
		setCdBoat(cdBoat);
		setCdPessoa(cdPessoa);
		
		setNrRg(nrRg);
		setSgOrgaoRg(sgOrgaoRg);
		setTxtEndereco(txtEndereco);
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	
	public void setPessoa(Pessoa pessoa){
		this.pessoa=pessoa;
	}
	public Pessoa getPessoa(){
		return this.pessoa;
	}
	public void setNrRg(String nrRg){
		this.nrRg=nrRg;
	}
	public String getNrRg(){
		return this.nrRg;
	}
	public void setSgOrgaoRg(String sgOrgaoRg){
		this.sgOrgaoRg=sgOrgaoRg;
	}
	public String getSgOrgaoRg(){
		return this.sgOrgaoRg;
	}
	public void setTxtEndereco(String txtEndereco){
		this.txtEndereco=txtEndereco;
	}
	public String getTxtEndereco(){
		return this.txtEndereco;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBoat: " +  getCdBoat();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		
		valueToString += ", nrRg: " +  getNrRg();
		valueToString += ", sgOrgaoRg: " +  getSgOrgaoRg();
		valueToString += ", txtEndereco: " +  getTxtEndereco();
		
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatTestemunha(getCdBoat(),
			getCdPessoa(),
			getNrRg(),
			getSgOrgaoRg(),
			getTxtEndereco());
	}

}