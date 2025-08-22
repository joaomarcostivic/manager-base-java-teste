package com.tivic.manager.msg.bot;

public class WebhookBotFactory {
	
	/** Canal #desenvolvimento do etivic.slack.com */
	public static final int TINO_MARCOS = 1;
	
	/** Canal #general do etivic.slack.com */
	public static final int HAL9000 = 2;
	
	/** Canal #radar do etivic.slack.com */
	public static final int BENDER = 3;
	
	public static ChatBot getBot(int bot) {
		
		switch (bot) {
		case TINO_MARCOS: 
			return new TinoMarcos();
		case HAL9000: 
			return new HAL9000();
		case BENDER: 
			return new Bender();
		default: 
			return null;
		}
	}
	
	/**
	 * Retorna uma instancia da classe indicada
	 * 
	 * @param type {@link Bender}, {@link TinoMarcos} ou {@link HAL9000}
	 * @return {@link ChatBot}
	 * 
	 * @author @mauriciocordeiro
	 */
	public static ChatBot create(Class<?> type) {
		
		if(type == TinoMarcos.class) {
			return getBot(TINO_MARCOS);
		} else if(type == Bender.class)  {
			return getBot(BENDER);
		} else if(type == HAL9000.class)  {
			return getBot(HAL9000);
		} else {
			return null;
		}
	}

}
