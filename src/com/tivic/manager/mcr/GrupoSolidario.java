package com.tivic.manager.mcr;

import java.util.GregorianCalendar;

public class GrupoSolidario {

	private int cdGrupoSolidario;
	private String nmGrupoSolidario;
	private GregorianCalendar dtConstituicao;
	private String idGrupoSolidario;
	private int cdEmpresa;

	public GrupoSolidario(int cdGrupoSolidario,
			String nmGrupoSolidario,
			GregorianCalendar dtConstituicao,
			String idGrupoSolidario,
			int cdEmpresa){
		setCdGrupoSolidario(cdGrupoSolidario);
		setNmGrupoSolidario(nmGrupoSolidario);
		setDtConstituicao(dtConstituicao);
		setIdGrupoSolidario(idGrupoSolidario);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdGrupoSolidario(int cdGrupoSolidario){
		this.cdGrupoSolidario=cdGrupoSolidario;
	}
	public int getCdGrupoSolidario(){
		return this.cdGrupoSolidario;
	}
	public void setNmGrupoSolidario(String nmGrupoSolidario){
		this.nmGrupoSolidario=nmGrupoSolidario;
	}
	public String getNmGrupoSolidario(){
		return this.nmGrupoSolidario;
	}
	public void setDtConstituicao(GregorianCalendar dtConstituicao){
		this.dtConstituicao=dtConstituicao;
	}
	public GregorianCalendar getDtConstituicao(){
		return this.dtConstituicao;
	}
	public void setIdGrupoSolidario(String idGrupoSolidario){
		this.idGrupoSolidario=idGrupoSolidario;
	}
	public String getIdGrupoSolidario(){
		return this.idGrupoSolidario;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoSolidario: " +  getCdGrupoSolidario();
		valueToString += ", nmGrupoSolidario: " +  getNmGrupoSolidario();
		valueToString += ", dtConstituicao: " +  sol.util.Util.formatDateTime(getDtConstituicao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idGrupoSolidario: " +  getIdGrupoSolidario();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoSolidario(cdGrupoSolidario,
			nmGrupoSolidario,
			dtConstituicao==null ? null : (GregorianCalendar)dtConstituicao.clone(),
			idGrupoSolidario,
			cdEmpresa);
	}

}