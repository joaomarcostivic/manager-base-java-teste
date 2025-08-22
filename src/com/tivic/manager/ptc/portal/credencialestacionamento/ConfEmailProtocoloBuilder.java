package com.tivic.manager.ptc.portal.credencialestacionamento;

public class ConfEmailProtocoloBuilder {
	
	private SendEmailProtocolo sendEmailProtocolo;
	
	public ConfEmailProtocoloBuilder(SendEmailProtocolo sendEmailProtocolo) {
		this.sendEmailProtocolo = sendEmailProtocolo;
	}
	
	
	public ConfEmailProtocoloBuilder setProvedorHost(String nmProvedorHost) {
		sendEmailProtocolo.setEmailProvedorHost(nmProvedorHost);
		return this;
	}
	
	public ConfEmailProtocoloBuilder setProvedorPort(int nrProvedorPort) {
		sendEmailProtocolo.setEmailProvedorPort(nrProvedorPort);
		return this;
	}
	
	public ConfEmailProtocoloBuilder setEmailRemetente(String nmEmailRemetente) {
		sendEmailProtocolo.setEmailRemetente(nmEmailRemetente);
		return this;
	}
	
	public ConfEmailProtocoloBuilder setSenhaRemetente(String senhaRemetente) {
		sendEmailProtocolo.setSenhaRemetente(senhaRemetente);
		return this;
	}

	public ConfEmailProtocoloBuilder setEmailHeader(String emailHeader) {
		sendEmailProtocolo.setEmailHeader(emailHeader);
		return this;
	}
	
	public ConfEmailProtocoloBuilder setNmTemplate(String nmTemplate) {
		sendEmailProtocolo.setNmTamplate(nmTemplate);
		return this;
	}
	
	public ConfEmailProtocoloBuilder setNmAssunto(String nmAssunto) {
		sendEmailProtocolo.setNmAssunto(nmAssunto);
		return this;
	}
	
	public SendEmailProtocolo build() {
		return sendEmailProtocolo;
	}
}
