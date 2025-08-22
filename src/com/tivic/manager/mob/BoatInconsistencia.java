package com.tivic.manager.mob;

public class BoatInconsistencia {

	private int cdBoat;
	private int cdInconsistencia;

	public BoatInconsistencia() { }

	public BoatInconsistencia(int cdBoat,
			int cdInconsistencia) {
		setCdBoat(cdBoat);
		setCdInconsistencia(cdInconsistencia);
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setCdInconsistencia(int cdInconsistencia){
		this.cdInconsistencia=cdInconsistencia;
	}
	public int getCdInconsistencia(){
		return this.cdInconsistencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBoat: " +  getCdBoat();
		valueToString += ", cdInconsistencia: " +  getCdInconsistencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatInconsistencia(getCdBoat(),
			getCdInconsistencia());
	}

}