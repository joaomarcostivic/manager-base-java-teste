package com.tivic.manager.wsdl.detran.mg;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;

public class AitSyncDTO {
	private String idAit;
	private String nrPlaca;
	private GregorianCalendar dtInfracao;
	private String nrControle;
	private Ait aitSistema;
	private Ait aitDetran;
	
	public AitSyncDTO(Ait aitSistema, Ait aitDetran) {
		this.aitSistema = aitSistema;
		this.aitDetran = aitDetran;
		this.idAit = aitSistema.getIdAit();
		this.nrPlaca = aitSistema.getNrPlaca();
		this.dtInfracao = aitSistema.getDtInfracao();
		this.nrControle = aitSistema.getNrControle();
	}
	
	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public Ait getAitSistema() {
		return aitSistema;
	}

	public void setAitSistema(Ait aitSistema) {
		this.aitSistema = aitSistema;
	}

	public Ait getAitDetran() {
		return aitDetran;
	}

	public void setAitDetran(Ait aitDetran) {
		this.aitDetran = aitDetran;
	}
	
	public String getNrControle() {
		return nrControle;
	}
	
	public void setNrControle(String nrControle) {
		this.nrControle = nrControle;
	}
}
