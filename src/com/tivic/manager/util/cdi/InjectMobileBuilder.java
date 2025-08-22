package com.tivic.manager.util.cdi;

import com.tivic.sol.auth.AuthBaseMobile;
import com.tivic.sol.auth.IAuthBase;
import com.tivic.sol.auth.IAuthService;
import com.tivic.sol.auth.mobile.AuthMobileService;
import com.tivic.sol.cdi.Scope;

public class InjectMobileBuilder extends InjectApplicationBuilder {
	
	@Override
	public void build(Scope scope) throws Exception {
		super.build(scope);
		scope.inject(IAuthBase.class, new AuthBaseMobile());
		scope.inject(IAuthService.class, new AuthMobileService());
	}
}
