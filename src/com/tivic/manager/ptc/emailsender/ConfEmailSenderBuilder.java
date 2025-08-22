package com.tivic.manager.ptc.emailsender;

public class ConfEmailSenderBuilder<T extends EmailContent> {

	private T emailContent;
	
	public ConfEmailSenderBuilder(T emailContent) {
		this.emailContent = emailContent;
	}
	
	public ConfEmailSenderBuilder<T> setProvedorHost(String nmProvedorHost) {
		emailContent.setEmailProvedorHost(nmProvedorHost);
		return this;
	}
	
	public ConfEmailSenderBuilder<T> setProvedorPort(int nrProvedorPort) {
		emailContent.setEmailProvedorPort(nrProvedorPort);
		return this;
	}
	
	public ConfEmailSenderBuilder<T> setEmailRemetente(String nmEmailRemetente) {
		emailContent.setEmailRemetente(nmEmailRemetente);
		return this;
	}
	
	public ConfEmailSenderBuilder<T> setSenhaRemetente(String senhaRemetente) {
		emailContent.setSenhaRemetente(senhaRemetente);
		return this;
	}

	public ConfEmailSenderBuilder<T> setEmailHeader(String emailHeader) {
		emailContent.setEmailHeader(emailHeader);
		return this;
	}
	
	public ConfEmailSenderBuilder<T> setNmTemplate(String nmTemplate) {
		emailContent.setNmTamplate(nmTemplate);
		return this;
	}
	
	public T build() {
		return emailContent;
	}
	
	public static <T extends EmailContent> ConfEmailSenderBuilder<T> createBuilder(T emailContent) {
        return new ConfEmailSenderBuilder<>(emailContent);
    }
}
