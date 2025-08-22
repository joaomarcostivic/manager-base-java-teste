package com.tivic.manager.ptc.emailsender;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class EmailContent {

	protected String nmPessoa;
	protected String nmEmail;
	protected String urlSistema;
	protected String emailHeader;
	protected String emailFooter;
	protected String nmTamplate;
	protected String emailProvedorHost;
	protected int emailProvedorPort;
	protected String emailRemetente;
	protected String senhaRemetente;
	protected String nmAssunto;
	protected HashMap<String, String> body;

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public String getNmEmail() {
		return nmEmail;
	}

	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}

	public String getUrlSistema() {
		return urlSistema;
	}

	public void setUrlSistema(String urlSistema) {
		this.urlSistema = urlSistema;
	}

	public String getEmailHeader() {
		return emailHeader;
	}

	public void setEmailHeader(String emailHeader) {
		this.emailHeader = emailHeader;
	}

	public String getEmailFooter() {
		return emailFooter;
	}

	public void setEmailFooter(String emailFooter) {
		this.emailFooter = emailFooter;
	}

	public String getNmTamplate() {
		return nmTamplate;
	}

	public void setNmTamplate(String nmTamplate) {
		this.nmTamplate = nmTamplate;
	}

	public String getEmailProvedorHost() {
		return emailProvedorHost;
	}

	public void setEmailProvedorHost(String emailProvedorHost) {
		this.emailProvedorHost = emailProvedorHost;
	}

	public int getEmailProvedorPort() {
		return emailProvedorPort;
	}

	public void setEmailProvedorPort(int emailProvedorPort) {
		this.emailProvedorPort = emailProvedorPort;
	}

	public String getEmailRemetente() {
		return emailRemetente;
	}

	public void setEmailRemetente(String emailRemetente) {
		this.emailRemetente = emailRemetente;
	}

	public String getSenhaRemetente() {
		return senhaRemetente;
	}

	public void setSenhaRemetente(String senhaRemetente) {
		this.senhaRemetente = senhaRemetente;
	}

	public String getNmAssunto() {
		return nmAssunto;
	}

	public void setNmAssunto(String nmAssunto) {
		this.nmAssunto = nmAssunto;
	}

	public HashMap<String, String> getBody() {
		return body;
	}

	public void setBody(HashMap<String, String> body) {
		this.body = body;
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
