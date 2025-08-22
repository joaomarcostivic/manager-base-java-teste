package com.tivic.manager.util.ddl;

import com.tivic.manager.util.Util;

public class DDLManagerFactory {

	public static DDLManager generate() {
		if(Util.isStrBaseAntiga())
			return new DDLManagerBaseAntiga();
		else
			return new DDLManagerBaseNova();
		
	}
	
}
