package com.tivic.manager.str.sync.logs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.str.sync.logs.instance.FlexInstanceSync;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;


public class FlexSyncLog {
	
	private final FlexSyncConfig config = new FlexSyncConfig();
	private final List<FlexInstanceSync<?>> instances = new ArrayList<FlexInstanceSync<?>>();
	private ManagerLog logger;
	
	public FlexSyncLog() throws Exception { 		 
		this.logger = (ManagerLog) BeansFactory.get(ManagerLog.class);
		logger.info("[FlexSyncLog]", "FlexSyncLog inicializado com sucesso!");
	}
	
	public void addInstance(FlexInstanceSync<?> instance) throws Exception {
		instance.setConfig(config);
		
		if(instance.getData() == null) throw new Exception("Os dados de uma instância de sincronização não pode ser vazia.");
		
		instances.add(instance);
	}
	
	public void write() throws Exception {
		logger.info("[FlexSyncLog]", "Inciando a escrita de logs e backups.");
		logger.info("[FlexSyncLog]", instances.stream().map(instance -> instance.getClass().getName()).collect(Collectors.joining(", ")));
		
		for(FlexInstanceSync<?> instance : instances) {
			instance.write();
		}
	}
	
	
	
}
