package com.tivic.manager.prc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PRCTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(PRCTestSuite.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ProcessoAndamentoServiceTest.class);
		suite.addTestSuite(ProcessoServiceTest.class);
		//$JUnit-END$
		return suite;
	}

}
