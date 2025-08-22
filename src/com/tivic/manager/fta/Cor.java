package com.tivic.manager.fta;

public class Cor {

	private int cdCor;
	private String nmCor;

	public Cor(){ }

	public Cor(int cdCor,
			String nmCor){
		setCdCor(cdCor);
		setNmCor(nmCor);
	}
	public void setCdCor(int cdCor){
		this.cdCor=cdCor;
	}
	public int getCdCor(){
		return this.cdCor;
	}
	public void setNmCor(String nmCor){
		this.nmCor=nmCor;
	}
	public String getNmCor(){
		return this.nmCor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCor: " +  getCdCor();
		valueToString += ", nmCor: " +  getNmCor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cor(getCdCor(),
			getNmCor());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdCor;
		result = prime * result + ((nmCor == null) ? 0 : nmCor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cor other = (Cor) obj;
		if (cdCor != other.cdCor)
			return false;
		if (nmCor == null) {
			if (other.nmCor != null)
				return false;
		} else if (!nmCor.equals(other.nmCor))
			return false;
		return true;
	}
	
	

}