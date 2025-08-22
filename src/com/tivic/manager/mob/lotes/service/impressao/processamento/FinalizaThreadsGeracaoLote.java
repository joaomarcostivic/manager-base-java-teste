package com.tivic.manager.mob.lotes.service.impressao.processamento;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class FinalizaThreadsGeracaoLote implements Thread.UncaughtExceptionHandler {
	private ManagerLog managerLog;
	private IGeracaoDocumentosSSE geracaoDocumentoSse;

    public FinalizaThreadsGeracaoLote() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		geracaoDocumentoSse = (IGeracaoDocumentosSSE) BeansFactory.get(IGeracaoDocumentosSSE.class);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
        	geracaoDocumentoSse.removeThread(thread);
        	thread.interrupt();
            System.out.println("Uncaught exception: " + ex);
        } catch (Exception e) {
			managerLog.showLog(e);
        }
    }
}
