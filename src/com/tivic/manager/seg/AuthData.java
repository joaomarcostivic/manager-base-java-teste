package com.tivic.manager.seg;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.Setor;

public class AuthData {
	
	//Metadados Autenticação
	private Usuario usuario;
	private Empresa empresa;
	private Setor setor;
	private GregorianCalendar authTime;
	
	//Metadados Sistema
	private String idSistema;
	private String idModulo;
	
	//Metadados Operacao
	private String idForm;
	private String idAcaoInsert;
	private String idAcaoUpdate;
	private String idAcaoDelete;
	private String idAcaoAny;
	
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	public GregorianCalendar getAuthTime() {
		return authTime;
	}
	public void setAuthTime(GregorianCalendar authTime) {
		this.authTime = authTime;
	}
	public String getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(String idModulo) {
		this.idModulo = idModulo;
	}
	public String getIdSistema() {
		return idSistema;
	}
	public void setIdSistema(String idSistema) {
		this.idSistema = idSistema;
	}
	public String getIdForm() {
		return idForm;
	}
	public void setIdForm(String idForm) {
		this.idForm = idForm;
	}
	public String getIdAcaoInsert() {
		return idAcaoInsert;
	}
	public void setIdAcaoInsert(String idAcaoInsert) {
		this.idAcaoInsert = idAcaoInsert;
	}
	public String getIdAcaoUpdate() {
		return idAcaoUpdate;
	}
	public void setIdAcaoUpdate(String idAcaoUpdate) {
		this.idAcaoUpdate = idAcaoUpdate;
	}
	public String getIdAcaoDelete() {
		return idAcaoDelete;
	}
	public void setIdAcaoDelete(String idAcaoDelete) {
		this.idAcaoDelete = idAcaoDelete;
	}
	public String getIdAcaoAny() {
		return idAcaoAny;
	}
	public void setIdAcaoAny(String idAcaoAny) {
		this.idAcaoAny = idAcaoAny;
	}
}
