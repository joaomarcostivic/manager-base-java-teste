package com.tivic.manager.seg.usuario;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsuarioLoginDTO {
	private int cdUsuario;
	private int cdEquipamento;
	private String nmEquipamento;
	private int stEquipamento;
	private int stLogin;
	private int stUsuario;
	private String nmLogin;
	private String nmPessoa;
	private int cdPessoa;
	
	public int getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public int getCdEquipamento() {
		return cdEquipamento;
	}
	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}
	public String getNmEquipamento() {
		return nmEquipamento;
	}
	public void setNmEquipamento(String nmEquipamento) {
		this.nmEquipamento = nmEquipamento;
	}	
	public int getStEquipamento() {
		return stEquipamento;
	}
	public void setStEquipamento(int stEquipamento) {
		this.stEquipamento = stEquipamento;
	}
	public int getStLogin() {
		return stLogin;
	}
	public void setStLogin(int stLogin) {
		this.stLogin = stLogin;
	}	
	public int getStUsuario() {
		return stUsuario;
	}
	public void setStUsuario(int stUsuario) {
		this.stUsuario = stUsuario;
	}
	public String getNmLogin() {
		return nmLogin;
	}
	public void setNmLogin(String nmLogin) {
		this.nmLogin = nmLogin;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public int getCdPessoa() {
		return cdPessoa;
	}
	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
