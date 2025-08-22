package com.tivic.manager.str.ait;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AitVerificadorDuplicidadeSuite {
	
	public static Test suite() {
		TestSuite suite = new TestSuite(AitVerificadorDuplicidadeSuite.class.getName());
		
		//$JUnit-BEGIN$		
		suite.addTestSuite(AitVerificadorDuplicidadeBaseAntigaTest.class);
		//$JUnit-END$
				
		return suite;
	}

}
