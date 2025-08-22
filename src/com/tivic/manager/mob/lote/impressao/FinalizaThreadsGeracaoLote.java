package com.tivic.manager.mob.lote.impressao;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class FinalizaThreadsGeracaoLote implements Thread.UncaughtExceptionHandler {
    private List<Thread> threadsGeracaoLote = new ArrayList<>();
	private ManagerLog managerLog;

    public FinalizaThreadsGeracaoLote(List<Thread> threadsGeracaoLote) throws Exception {
        this.threadsGeracaoLote = threadsGeracaoLote;
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
    }

    @Override
    public void uncaughtException(Thread th, Throwable ex) {
        try {
            threadsGeracaoLote.remove(th);
            th.interrupt();
            System.out.println("Uncaught exception: " + ex);
        } catch (Exception e) {
			managerLog.showLog(e);
        }
    }
}
