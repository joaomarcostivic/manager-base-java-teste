package com.tivic.manager.msg.bot;

import com.tivic.manager.msg.Mensagem;

public interface ChatBot {
	
	void enviar(String mensagem);
	
	void enviar(Mensagem mensagem);

}
