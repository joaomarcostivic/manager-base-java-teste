package com.tivic.manager.mob.ait.sync;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.ait.aitimagempessoa.IAitImagemPessoaService;
import com.tivic.manager.mob.ait.aitpessoa.AitPessoa;
import com.tivic.manager.mob.ait.aitpessoa.IAitPessoaService;
import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;

@SuppressWarnings("unchecked")
public class AitPessoaSyncService implements IAitPessoaSyncService {

	private IAitPessoaService aitPessoaService;
	private IAitImagemPessoaService aitImagemPessoaService;
	private IAitSyncTools<AitPessoa> aitSyncTools;
	private IAtualizaTalonarioAitPessoa atualizaTalonarioAitPessoa;
	private ManagerLog managerLog;
	
	public AitPessoaSyncService() throws Exception {
		aitPessoaService = (IAitPessoaService)  BeansFactory.get(IAitPessoaService.class);
		aitImagemPessoaService = (IAitImagemPessoaService)  BeansFactory.get(IAitImagemPessoaService.class);
		aitSyncTools = (IAitSyncTools<AitPessoa>) BeansFactory.get(IAitSyncTools.class);
		atualizaTalonarioAitPessoa = (IAtualizaTalonarioAitPessoa) BeansFactory.get(IAtualizaTalonarioAitPessoa.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@Override
    public List<AitSyncResponse> syncReceive(List<AitPessoa> aitList) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			 List<AitSyncResponse> aitSyncResponseList = syncReceive(aitList, customConnection);
			customConnection.finishConnection();
			return aitSyncResponseList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<AitSyncResponse> syncReceive(List<AitPessoa> aitList, CustomConnection customConnection) throws Exception {
		List<AitPessoa> list = aitSyncTools.verificarAitsDuplicados(aitList);
		atualizaTalonarioAitPessoa.atualizar(aitList, customConnection);
		List<AitSyncResponse> aitSyncResponseList = new ArrayList<AitSyncResponse>();
		for(AitPessoa aitPessoa: list) {			
			boolean aitexistente = this.aitPessoaService.hasAitPessoa(aitPessoa.getIdAit(), customConnection);
			if(!aitexistente) {
				aitPessoaService.insert(aitPessoa, customConnection);
				managerLog.showLog(new InfoLogBuilder("AIT Pessoa" + aitPessoa.getIdAit(), "Sincronzado com sucesso...").build());
				aitImagemPessoaService.insertImagePessoaSync(aitPessoa, customConnection);
				aitSyncTools.setSincronizados(aitPessoa.getIdAit(), aitSyncResponseList, true, false);
			} else {
				managerLog.showLog(new InfoLogBuilder("AIT Pessoa" + aitPessoa.getIdAit(), "Já sincronizado anteriormente...").build());
				aitSyncTools.setSincronizados(aitPessoa.getIdAit(), aitSyncResponseList, false, true);
			}
		}
		return aitSyncResponseList;
	}
}
