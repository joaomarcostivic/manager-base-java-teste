package com.tivic.manager.mob.ait.sync.observer;

import java.util.List;

import com.tivic.manager.mob.aitimagem.task.IClientConversorImagemTask;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.concurrent.AsyncListProcessor;
import com.tivic.sol.log.ManagerLog;

public class ImageConversionObserver implements IAitSyncObserver {
    private final IClientConversorImagemTask clientConversorImagemTask;
    private final ManagerLog managerLog;
    private final AsyncListProcessor<Integer> asyncListProcessor;

    public ImageConversionObserver() throws Exception {
		clientConversorImagemTask = (IClientConversorImagemTask) BeansFactory.get(IClientConversorImagemTask.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
        asyncListProcessor = new AsyncListProcessor<>();
    }

    @Override
    public void onAitSyncPersisted(List<Integer> cdAitList) {
        try {
            asyncListProcessor.process(cdAitList, cdAit -> {
                clientConversorImagemTask.execute(cdAit);
            });
        } catch (Exception e) {
            managerLog.info("Erro ao iniciar processamento de imagens: ", e.getMessage());
        }
    }
}