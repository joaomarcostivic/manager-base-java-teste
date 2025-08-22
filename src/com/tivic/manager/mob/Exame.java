package com.tivic.manager.mob;

public class Exame {

	private int cdExame;
	private int cdTipoExame;
	private int cdRrd;
	private String txtLaudo;
	private int cdArquivo;
	private int cdTrrav;

	public Exame() { }

	public Exame(int cdExame,
			int cdTipoExame,
			int cdRrd,
			String txtLaudo,
			int cdArquivo,
			int cdTrrav) {
		setCdExame(cdExame);
		setCdTipoExame(cdTipoExame);
		setCdRrd(cdRrd);
		setTxtLaudo(txtLaudo);
		setCdArquivo(cdArquivo);
		setCdTrrav(cdTrrav);
	}
	public void setCdExame(int cdExame){
		this.cdExame=cdExame;
	}
	public int getCdExame(){
		return this.cdExame;
	}
	public void setCdTipoExame(int cdTipoExame){
		this.cdTipoExame=cdTipoExame;
	}
	public int getCdTipoExame(){
		return this.cdTipoExame;
	}
	public void setCdRrd(int cdRrd){
		this.cdRrd=cdRrd;
	}
	public int getCdRrd(){
		return this.cdRrd;
	}
	public void setTxtLaudo(String txtLaudo){
		this.txtLaudo=txtLaudo;
	}
	public String getTxtLaudo(){
		return this.txtLaudo;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdTrrav(int cdTrrav){
		this.cdTrrav=cdTrrav;
	}
	public int getCdTrrav(){
		return this.cdTrrav;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdExame: " +  getCdExame();
		valueToString += ", cdTipoExame: " +  getCdTipoExame();
		valueToString += ", cdRrd: " +  getCdRrd();
		valueToString += ", txtLaudo: " +  getTxtLaudo();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdTrrav: " +  getCdTrrav();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Exame(getCdExame(),
			getCdTipoExame(),
			getCdRrd(),
			getTxtLaudo(),
			getCdArquivo(),
			getCdTrrav());
	}

}