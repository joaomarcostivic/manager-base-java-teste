package com.tivic.manager.fta;

public class TipoVeiculo {

	private int cdTipoVeiculo;
	private String nmTipoVeiculo;
	private String txtCnhRequerida;

	public TipoVeiculo(){ }
	
	public TipoVeiculo(int cdTipoVeiculo,
			String nmTipoVeiculo){
		setCdTipoVeiculo(cdTipoVeiculo);
		setNmTipoVeiculo(nmTipoVeiculo);
	}

	public TipoVeiculo(int cdTipoVeiculo,
			String nmTipoVeiculo,
			String txtCnhRequerida){
		setCdTipoVeiculo(cdTipoVeiculo);
		setNmTipoVeiculo(nmTipoVeiculo);
		setTxtCnhRequerida(txtCnhRequerida);
	}
	public void setCdTipoVeiculo(int cdTipoVeiculo){
		this.cdTipoVeiculo=cdTipoVeiculo;
	}
	public int getCdTipoVeiculo(){
		return this.cdTipoVeiculo;
	}
	public void setNmTipoVeiculo(String nmTipoVeiculo){
		this.nmTipoVeiculo=nmTipoVeiculo;
	}
	public String getNmTipoVeiculo(){
		return this.nmTipoVeiculo;
	}
	public String getTxtCnhRequerida() {
		return txtCnhRequerida;
	}

	public void setTxtCnhRequerida(String txtCnhRequerida) {
		this.txtCnhRequerida = txtCnhRequerida;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoVeiculo: " +  getCdTipoVeiculo();
		valueToString += ", nmTipoVeiculo: " +  getNmTipoVeiculo();
		valueToString += ", txtCnhRequerida: " +  getTxtCnhRequerida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoVeiculo(getCdTipoVeiculo(),
			getNmTipoVeiculo(),
			getTxtCnhRequerida());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdTipoVeiculo;
		result = prime * result + ((nmTipoVeiculo == null) ? 0 : nmTipoVeiculo.hashCode());
		result = prime * result + ((txtCnhRequerida == null) ? 0 : txtCnhRequerida.hashCode());
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
		TipoVeiculo other = (TipoVeiculo) obj;
		if (cdTipoVeiculo != other.cdTipoVeiculo)
			return false;
		if (nmTipoVeiculo == null) {
			if (other.nmTipoVeiculo != null)
				return false;
		} else if (!nmTipoVeiculo.equals(other.nmTipoVeiculo))
			return false;
		if (txtCnhRequerida == null) {
			if (other.txtCnhRequerida != null)
				return false;
		} else if (!txtCnhRequerida.equals(other.txtCnhRequerida))
			return false;
		return true;
	}
	
	

}