package com.tivic.manager.conf.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.tivic.manager.conf.ManagerConf;

/** 
 * Singleton para acesso a configurações do JWT definidas no <i>manager.conf</i>
 * 
 * @author Maurício <mauricio@tivic.com.br>
 */
public class JWTConf {
	
	private static JWTConf instance;
	
	private JWTConf() { }
	
	public static JWTConf getInstance() {
		if(instance == null) {
            synchronized (JWTConf.class) {
                if (instance == null) {
                	instance = new JWTConf();
                }
            }
		}
		return instance;
	}
	
	/**
	 * JWT está ativo?
	 */
	public Boolean isEnabled() {
		return ManagerConf.getInstance().getAsBoolean("JWT");
	}
	
	/**
	 * Tempo de duração do token em horas.
	 * <br>
	 * Se o valor não existir, retorna <b>1</b>.
	 */
	public Integer getExp() {
		return ManagerConf.getInstance().getAsInteger("JWT_TOKEN_EXP", 1);
	}
	
	/**
	 * Log de acessos está ativo?
	 */
	public Boolean isLogEnabled() {
		return ManagerConf.getInstance().getAsBoolean("JWT_LOG");
	}
	
	/**
	 * Lista de caminhos não validados com JWT
	 */
	public List<String> getAllowedPaths() {
		List<String> paths = new ArrayList<String>();
		String strPaths = ManagerConf.getInstance().get("JWT_ALLOWED_PATHS", "");
		
		StringTokenizer tokens = new StringTokenizer(strPaths, ",");
		while(tokens.hasMoreTokens()) {
			paths.add(tokens.nextToken().trim());
		}
		
		return paths;
	}

}
