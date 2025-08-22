package com.tivic.manager.mob.inconsistencias;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TiposInconsistenciasDTO {

	private int cdInconsistencia;
	private String nmInconsistencia;

	public TiposInconsistenciasDTO() { }

	public int getCdInconsistencia() {
		return cdInconsistencia;
	}

	public void setCdInconsistencia(int cdInconsistencia) {
		this.cdInconsistencia = cdInconsistencia;
	}

	public String getNmInconsistencia() {
		return nmInconsistencia;
	}

	public void setNmInconsistencia(String nmInconsistencia) {
		this.nmInconsistencia = nmInconsistencia;
	}

	@Override
	public String toString() {
		try {
			JSONObject jsonDadosInconsistenciaDTO = new JSONObject();
			jsonDadosInconsistenciaDTO.put("cdInconsistencia", getCdInconsistencia());
			jsonDadosInconsistenciaDTO.put("nmInconsistencia", getNmInconsistencia());
			return jsonDadosInconsistenciaDTO.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "cdInconsistencia: " + getCdInconsistencia();
		}
	}

}
