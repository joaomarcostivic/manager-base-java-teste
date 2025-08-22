package com.tivic.manager.grl;

public class Cidade {

	private int cdCidade;
	private String nmCidade;
	private String nrCep;
	private float vlAltitude;
	private float vlLatitude;
	private float vlLongitude;
	private int cdEstado;
	private String idCidade;
	private int cdRegiao;
	private String idIbge;
	private String sgCidade;
	private int qtDistanciaCapital;
	private int qtDistanciaBase;
	
	public Cidade(){ }

	public Cidade(int cdCidade,
			String nmCidade,
			String nrCep,
			float vlAltitude,
			float vlLatitude,
			float vlLongitude,
			int cdEstado,
			String idCidade,
			int cdRegiao,
			String idIbge,
			String sgCidade,
			int qtDistanciaCapital,
			int qtDistanciaBase){
		setCdCidade(cdCidade);
		setNmCidade(nmCidade);
		setNrCep(nrCep);
		setVlAltitude(vlAltitude);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdEstado(cdEstado);
		setIdCidade(idCidade);
		setCdRegiao(cdRegiao);
		setIdIbge(idIbge);
		setSgCidade(sgCidade);
		setQtDistanciaCapital(qtDistanciaCapital);
		setQtDistanciaBase(qtDistanciaBase);
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setNmCidade(String nmCidade){
		this.nmCidade=nmCidade;
	}
	public String getNmCidade(){
		return this.nmCidade;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setVlAltitude(float vlAltitude){
		this.vlAltitude=vlAltitude;
	}
	public float getVlAltitude(){
		return this.vlAltitude;
	}
	public void setVlLatitude(float vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public float getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(float vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public float getVlLongitude(){
		return this.vlLongitude;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setIdCidade(String idCidade){
		this.idCidade=idCidade;
	}
	public String getIdCidade(){
		return this.idCidade;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setIdIbge(String idIbge){
		this.idIbge=idIbge;
	}
	public String getIdIbge(){
		return this.idIbge;
	}
	public void setSgCidade(String sgCidade){
		this.sgCidade=sgCidade;
	}
	public String getSgCidade(){
		return this.sgCidade;
	}
	public void setQtDistanciaCapital(int qtDistanciaCapital){
		this.qtDistanciaCapital=qtDistanciaCapital;
	}
	public int getQtDistanciaCapital(){
		return this.qtDistanciaCapital;
	}
	public void setQtDistanciaBase(int qtDistanciaBase){
		this.qtDistanciaBase=qtDistanciaBase;
	}
	public int getQtDistanciaBase(){
		return this.qtDistanciaBase;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdCidade\": " +  getCdCidade();
		valueToString += ", \"nmCidade\": \"" +  getNmCidade() + "\"";
		valueToString += ", \"nrCep\": \"" +  getNrCep() + "\"";
		valueToString += ", \"vlAltitude\": " +  getVlAltitude();
		valueToString += ", \"vlLatitude\": " +  getVlLatitude();
		valueToString += ", \"vlLongitude\": " +  getVlLongitude();
		valueToString += ", \"cdEstado\": " +  getCdEstado();
		valueToString += ", \"idCidade\": \"" +  getIdCidade() + "\"";
		valueToString += ", \"cdRegiao\": " +  getCdRegiao();
		valueToString += ", \"idIbge\": \"" +  getIdIbge() + "\"";
		valueToString += ", \"sgCidade\": \"" +  getSgCidade() + "\"";
		valueToString += ", \"qtDistanciaCapital\": " +  getQtDistanciaCapital();
		valueToString += ", \"qtDistanciaBase\": " +  getQtDistanciaBase();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cidade(getCdCidade(),
			getNmCidade(),
			getNrCep(),
			getVlAltitude(),
			getVlLatitude(),
			getVlLongitude(),
			getCdEstado(),
			getIdCidade(),
			getCdRegiao(),
			getIdIbge(),
			getSgCidade(),
			getQtDistanciaCapital(),
			getQtDistanciaBase());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdCidade;
		result = prime * result + ((idCidade == null) ? 0 : idCidade.hashCode());
		result = prime * result + ((nmCidade == null) ? 0 : nmCidade.hashCode());
		result = prime * result + ((sgCidade == null) ? 0 : sgCidade.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof Cidade))
			return false;
		Cidade other = (Cidade) obj;
		if (cdCidade != other.cdCidade)
			return false;
		if (idCidade == null) {
			if (other.idCidade != null)
				return false;
		} else if (!idCidade.equals(other.idCidade))
			return false;
		if (nmCidade == null) {
			if (other.nmCidade != null)
				return false;
		} else if (!nmCidade.equals(other.nmCidade))
			return false;
		if (sgCidade == null) {
			if (other.sgCidade != null)
				return false;
		} else if (!sgCidade.equals(other.sgCidade))
			return false;
		return true;
	}
	

}