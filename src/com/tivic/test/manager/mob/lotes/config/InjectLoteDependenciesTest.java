package com.tivic.test.manager.mob.lotes.config;

import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.manager.mob.lotes.service.impressao.LoteImpressaoService;
import com.tivic.sol.auth.ManagerUsers;
import com.tivic.sol.auth.jwt.JWT;
import com.tivic.sol.cdi.InjectBuilder;
import com.tivic.sol.cdi.Scope;
import com.tivic.sol.conf.Configuration;
import com.tivic.sol.conf.ConfigurationBuilder;
import com.tivic.sol.connection.creater.CreaterConnection;
import com.tivic.sol.connection.creater.CreaterConnectionConf;
import com.tivic.sol.filemanager.Filemanager;
import com.tivic.sol.filemanager.IFilemanager;
import com.tivic.sol.httpclient.ISolHttpClient;
import com.tivic.sol.httpclient.SolHttpClient;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.ManagerLogBuilder;
import com.tivic.test.manager.mob.lotes.repository.LoteImpressaoAitRepositoryTest;
import com.tivic.test.manager.mob.lotes.repository.LoteImpressaoRepositoryTest;
import com.tivic.test.manager.mob.lotes.repository.LoteRepositoryTest;

public class InjectLoteDependenciesTest implements InjectBuilder {
	
public void build(Scope scope) throws Exception{
	buildTools(scope);
	scope.inject(LoteRepository.class, new LoteRepositoryTest());
	scope.inject(LoteImpressaoRepository.class, new LoteImpressaoRepositoryTest());
	scope.inject(ILoteImpressaoAitRepository.class, new LoteImpressaoAitRepositoryTest());
	scope.inject(ILoteImpressaoService.class, new LoteImpressaoService());
	
}

private void buildTools(Scope scope) throws Exception {
	scope.inject(CreaterConnection.class, new CreaterConnectionConf());
	scope.inject(ManagerLog.class, new ManagerLogBuilder().build());
	scope.inject(Configuration.class, new ConfigurationBuilder().build());
	scope.inject(ISolHttpClient.class, new SolHttpClient());
	scope.inject(IFilemanager.class, new Filemanager());
	scope.inject(ManagerUsers.class, new ManagerUsers());
	scope.inject(JWT.class, new JWT());
}
}