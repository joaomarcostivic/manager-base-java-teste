package com.tivic.manager.agd;

public class Agenda {

	private int cdAgenda;
	private String nmAgenda;
	private int cdEmpresa;
	private String txtAberturaAta;
	private String txtAgenda;

	public Agenda(int cdAgenda,
			String nmAgenda,
			int cdEmpresa,
			String txtAberturaAta,
			String txtAgenda){
		setCdAgenda(cdAgenda);
		setNmAgenda(nmAgenda);
		setCdEmpresa(cdEmpresa);
		setTxtAberturaAta(txtAberturaAta);
		setTxtAgenda(txtAgenda);
	}
	public void setCdAgenda(int cdAgenda){
		this.cdAgenda=cdAgenda;
	}
	public int getCdAgenda(){
		return this.cdAgenda;
	}
	public void setNmAgenda(String nmAgenda){
		this.nmAgenda=nmAgenda;
	}
	public String getNmAgenda(){
		return this.nmAgenda;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setTxtAberturaAta(String txtAberturaAta){
		this.txtAberturaAta=txtAberturaAta;
	}
	public String getTxtAberturaAta(){
		return this.txtAberturaAta;
	}
	public void setTxtAgenda(String txtAgenda){
		this.txtAgenda=txtAgenda;
	}
	public String getTxtAgenda(){
		return this.txtAgenda;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgenda: " +  getCdAgenda();
		valueToString += ", nmAgenda: " +  getNmAgenda();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", txtAberturaAta: " +  getTxtAberturaAta();
		valueToString += ", txtAgenda: " +  getTxtAgenda();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Agenda(getCdAgenda(),
			getNmAgenda(),
			getCdEmpresa(),
			getTxtAberturaAta(),
			getTxtAgenda());
	}

}
