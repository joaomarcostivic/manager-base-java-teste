package com.tivic.manager.acd;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.Ocorrencia;

public class OcorrenciaMatricula extends Ocorrencia {

	private int cdMatriculaOrigem;
	private int cdMatriculaDestino;
	private int cdTurmaOrigem;
	private int cdTurmaDestino;
	private int stMatriculaOrigem;
	private int cdUsuarioModificador;

	public OcorrenciaMatricula(){ }

	public OcorrenciaMatricula(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdMatriculaOrigem,
			int cdMatriculaDestino,
			int cdTurmaOrigem,
			int cdTurmaDestino,
			int stMatriculaOrigem,
			int cdUsuarioModificador){
		super(cdOcorrencia, 
			  cdPessoa, 
			  txtOcorrencia, 
			  dtOcorrencia, 
			  cdTipoOcorrencia, 
			  stOcorrencia, 
			  cdSistema, 
			  cdUsuario);
		setCdMatriculaOrigem(cdMatriculaOrigem);
		setCdMatriculaDestino(cdMatriculaDestino);
		setCdTurmaOrigem(cdTurmaOrigem);
		setCdTurmaDestino(cdTurmaDestino);
		setStMatriculaOrigem(stMatriculaOrigem);
		setCdUsuarioModificador(cdUsuarioModificador);
	}
	
	public void setCdMatriculaOrigem(int cdMatriculaOrigem){
		this.cdMatriculaOrigem=cdMatriculaOrigem;
	}
	public int getCdMatriculaOrigem(){
		return this.cdMatriculaOrigem;
	}
	public void setCdMatriculaDestino(int cdMatriculaDestino){
		this.cdMatriculaDestino=cdMatriculaDestino;
	}
	public int getCdMatriculaDestino(){
		return this.cdMatriculaDestino;
	}
	public void setCdTurmaOrigem(int cdTurmaOrigem){
		this.cdTurmaOrigem=cdTurmaOrigem;
	}
	public int getCdTurmaOrigem(){
		return this.cdTurmaOrigem;
	}
	public void setCdTurmaDestino(int cdTurmaDestino){
		this.cdTurmaDestino=cdTurmaDestino;
	}
	public int getCdTurmaDestino(){
		return this.cdTurmaDestino;
	}
	public void setStMatriculaOrigem(int stMatriculaOrigem) {
		this.stMatriculaOrigem = stMatriculaOrigem;
	}
	public int getStMatriculaOrigem() {
		return stMatriculaOrigem;
	}
	public void setCdUsuarioModificador(int cdUsuarioModificador) {
		this.cdUsuarioModificador = cdUsuarioModificador;
	}
	public int getCdUsuarioModificador() {
		return cdUsuarioModificador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", stOcorrencia: " +  getStOcorrencia();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdMatriculaOrigem: " +  getCdMatriculaOrigem();
		valueToString += ", cdMatriculaDestino: " +  getCdMatriculaDestino();
		valueToString += ", cdTurmaOrigem: " +  getCdTurmaOrigem();
		valueToString += ", cdTurmaDestino: " +  getCdTurmaDestino();
		valueToString += ", stMatriculaOrigem: " +  getStMatriculaOrigem();
		valueToString += ", cdUsuarioModificador: " +  getCdUsuarioModificador();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaMatricula(getCdOcorrencia(),
				getCdPessoa(),
				getTxtOcorrencia(),
				getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
				getCdTipoOcorrencia(),
				getStOcorrencia(),
				getCdSistema(),
				getCdUsuario(),
				getCdMatriculaOrigem(),
				getCdMatriculaDestino(),
				getCdTurmaOrigem(),
				getCdTurmaDestino(),
				getStMatriculaOrigem(),
				getCdUsuarioModificador());
	}

}