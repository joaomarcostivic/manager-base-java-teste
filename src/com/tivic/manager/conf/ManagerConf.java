package com.tivic.manager.conf;

import java.io.InputStream;
import java.util.Properties;

import com.tivic.manager.util.Util;

/** 
 * Singleton para acesso a configurações definidas no <i>manager.conf</i>
 * 
 * @author Maurício <mauricio@tivic.com.br>
 */
public class ManagerConf extends sol.util.ConfManager {
	
	public static final String FILE_PATH = "/com/tivic/manager/conf/manager.conf";
	
	private static ManagerConf instance;
		
	private ManagerConf(InputStream in) {
		super(in);
	}
	
	public static ManagerConf getInstance() {
		if(instance == null) {
            synchronized (ManagerConf.class) {
                if (instance == null) {
                	instance = new ManagerConf(Util.class.getResourceAsStream(FILE_PATH));
                }
            }
		}
		return instance;
	}
	
	/**
	 * Propriedades do <i>manager.conf</i>
	 */
	public Properties getProperties() {
		return getInstance().getProps();
	}
	
	/**
	 * Valor de uma propriedade do <i>manager.conf</i>
	 */
	public String get(String key) {
		if(getInstance().getProperties().getProperty(key) != null)
			return getInstance().getProperties().getProperty(key).trim();
		else
			return getInstance().getProperties().getProperty(key);
	}
	
	/**
	 * Retorna valor de uma propriedade do <i>manager.conf</i>.
	 * <br>
	 * Se o valor não existir, retorna o <b>defaulValue</b>.
	 */
	public String get(String key, String defaultValue) {
		try {
			return getInstance().getProperties().getProperty(key, defaultValue).trim();
		} catch (NullPointerException e) {
			return null;
		}
	}
		
	/**
	 * Retorna valor de uma propriedade do <i>manager.conf</i> como um {@link Boolean}.
	 * <br>
	 * Se o valor estiver definido como inteiro: 0 = false, >0 = true
	 * 
	 */
	public Boolean getAsBoolean(String key) {
		try {
			return getAsInteger(key) != 0;
		} catch (NumberFormatException nfe) {
			return Boolean.valueOf(get(key, "false"));
		}
	}
	
	/**
	 * Retorna valor de uma propriedade do <i>manager.conf</i> como um {@link Boolean}.
	 * <br>
	 * Se o valor estiver definido como inteiro: 0 = false, >0 = true
	 * <br>
	 * Se o valor não existir, retorna o <b>defaulValue</b>.
	 */
	public Boolean getAsBoolean(String key, Boolean defaultValue) {
		try {
			return getAsInteger(key) != 0;
		} catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}
	
	/**
	 * Retorna valor de uma propriedade do <i>manager.conf</i> como um {@link Integer}.
	 * <br>
	 * Se o valor não existir, retorna 0.
	 * 
	 */
	public Integer getAsInteger(String key) {
		return Integer.parseInt(get(key, "0"));
	}
	
	/**
	 * Retorna valor de uma propriedade do <i>manager.conf</i> como um {@link Integer}.
	 * <br>
	 * Se o valor não existir, retorna o <b>defaulValue</b>.
	 * 
	 */
	public Integer getAsInteger(String key, Integer defaultValue) {
		return Integer.parseInt(get(key, Integer.toString(defaultValue)));
	}
	
}
