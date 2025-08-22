package com.tivic.test.manager.tasks.limpeza.lotes;

import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.tasks.limpeza.lotes.ILimpezaLotesService;
import com.tivic.manager.tasks.limpeza.lotes.LimpezaLotesService;
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
import com.tivic.test.manager.tasks.limpeza.lotes.repositories.ArquivoRepositoryTest;
import com.tivic.test.manager.tasks.limpeza.lotes.repositories.LoteImpressaoArquivoRepositoryTest;
import com.tivic.test.manager.tasks.limpeza.lotes.repositories.LoteImpressaoAitRepositoryTest;
import com.tivic.test.manager.tasks.limpeza.lotes.repositories.LoteImpressaoRepositoryTest;

public class InjectLimpezaLoteImpressaoTesteInjectBuilder implements InjectBuilder {
		
	public void build(Scope scope) throws Exception{
		buildTools(scope);
		scope.inject(ILoteImpressaoRepository.class, new LoteImpressaoRepositoryTest());
		scope.inject(ILoteImpressaoArquivoRepository.class, new LoteImpressaoArquivoRepositoryTest());
		scope.inject(ILoteImpressaoAitRepository.class, new LoteImpressaoAitRepositoryTest());
		scope.inject(IArquivoRepository.class, new ArquivoRepositoryTest());
		scope.inject(ILimpezaLotesService.class, new LimpezaLotesService());
		
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