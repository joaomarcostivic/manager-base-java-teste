package  com.tivic.manager.acd;

public class AbastecimentoAgua {

	private int cdAbastecimentoAgua;
	private String nmAbastecimentoAgua;
	private String idAbastecimentoAgua;

	public AbastecimentoAgua(){ }

	public AbastecimentoAgua(int cdAbastecimentoAgua,
			String nmAbastecimentoAgua,
			String idAbastecimentoAgua){
		setCdAbastecimentoAgua(cdAbastecimentoAgua);
		setNmAbastecimentoAgua(nmAbastecimentoAgua);
		setIdAbastecimentoAgua(idAbastecimentoAgua);
	}
	public void setCdAbastecimentoAgua(int cdAbastecimentoAgua){
		this.cdAbastecimentoAgua=cdAbastecimentoAgua;
	}
	public int getCdAbastecimentoAgua(){
		return this.cdAbastecimentoAgua;
	}
	public void setNmAbastecimentoAgua(String nmAbastecimentoAgua){
		this.nmAbastecimentoAgua=nmAbastecimentoAgua;
	}
	public String getNmAbastecimentoAgua(){
		return this.nmAbastecimentoAgua;
	}
	public void setIdAbastecimentoAgua(String idAbastecimentoAgua){
		this.idAbastecimentoAgua=idAbastecimentoAgua;
	}
	public String getIdAbastecimentoAgua(){
		return this.idAbastecimentoAgua;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAbastecimentoAgua: " +  getCdAbastecimentoAgua();
		valueToString += ", nmAbastecimentoAgua: " +  getNmAbastecimentoAgua();
		valueToString += ", idAbastecimentoAgua: " +  getIdAbastecimentoAgua();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AbastecimentoAgua(getCdAbastecimentoAgua(),
			getNmAbastecimentoAgua(),
			getIdAbastecimentoAgua());
	}

}