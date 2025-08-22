package com.tivic.manager.fta;

public class CategoriaVeiculo {

	private int cdCategoria;
	private String nmCategoria;

	public CategoriaVeiculo(){ }

	public CategoriaVeiculo(int cdCategoria,
			String nmCategoria){
		setCdCategoria(cdCategoria);
		setNmCategoria(nmCategoria);
	}
	public void setCdCategoria(int cdCategoria){
		this.cdCategoria=cdCategoria;
	}
	public int getCdCategoria(){
		return this.cdCategoria;
	}
	public void setNmCategoria(String nmCategoria){
		this.nmCategoria=nmCategoria;
	}
	public String getNmCategoria(){
		return this.nmCategoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCategoria: " +  getCdCategoria();
		valueToString += ", nmCategoria: " +  getNmCategoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CategoriaVeiculo(getCdCategoria(),
			getNmCategoria());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdCategoria;
		result = prime * result + ((nmCategoria == null) ? 0 : nmCategoria.hashCode());
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
		CategoriaVeiculo other = (CategoriaVeiculo) obj;
		if (cdCategoria != other.cdCategoria)
			return false;
		if (nmCategoria == null) {
			if (other.nmCategoria != null)
				return false;
		} else if (!nmCategoria.equals(other.nmCategoria))
			return false;
		return true;
	}
	
	

}