package com.tivic.manager.util.cdi;

import com.tivic.sol.cdi.Scope;

public class InjectTestBuilder {

	private Scope scope;
	
	public InjectTestBuilder(Scope scope) {
		this.scope = scope;
	}

	public void build() throws Exception{
		injectMobScope();
	}
	
	public void injectMobScope() throws Exception {
		
	}
}
