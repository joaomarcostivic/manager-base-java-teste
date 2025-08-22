package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class Ocorrencia extends com.tivic.manager.grl.Ocorrencia {

	private int cdTituloCredito;
	private int cdContrato;

	public Ocorrencia(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdTituloCredito,
			int cdContrato){
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdTituloCredito(cdTituloCredito);
		setCdContrato(cdContrato);
	}
	public void setCdTituloCredito(int cdTituloCredito){
		this.cdTituloCredito=cdTituloCredito;
	}
	public int getCdTituloCredito(){
		return this.cdTituloCredito;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdTituloCredito: " +  getCdTituloCredito();
		valueToString += ", cdContrato: " +  getCdContrato();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ocorrencia(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdTituloCredito(),
			getCdContrato());
	}

}
