package com.tivic.manager.mob.ecarta.services;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class FinalizaThreadsGeracaoArquivoServicoEDI  implements Thread.UncaughtExceptionHandler{
	private ManagerLog managerLog;
	private IArquivoServiceEDI arquivoServiceEDI;

	public FinalizaThreadsGeracaoArquivoServicoEDI() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.arquivoServiceEDI = (IArquivoServiceEDI) BeansFactory.get(IArquivoServiceEDI.class);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
        	arquivoServiceEDI.removeThread(thread);
        	thread.interrupt();
            System.out.println("Uncaught exception: " + ex);
        } catch (Exception e) {
			managerLog.showLog(e);
        }
    }
}
