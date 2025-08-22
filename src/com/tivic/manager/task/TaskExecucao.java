package com.tivic.manager.task;

import java.lang.reflect.Method;
import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.task.Task;

public class TaskExecucao implements ITaskExecucao {

	@Override
	public void executarTask(int cdTask) throws ValidacaoException, Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(true);
	        
	        List<Task> tasks = searchTask(cdTask).getList(Task.class);
	        if (tasks.isEmpty()) {
				throw new ValidacaoException("Task não encontrada.");
			}
	        Task task = tasks.get(0);
	        String nmClass = task.getNmClass();
	        String nmMethod = task.getNmMethod();

	        Class<?> classe = Class.forName(nmClass);
	        Object instanciaClasse = BeansFactory.get(classe); 
	        Method metodo = classe.getMethod(nmMethod);
	        metodo.invoke(instanciaClasse);
	        
	        customConnection.finishConnection();
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	public Search<Task> searchTask(int cdTask) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_task", cdTask);
	    SearchBuilder<Task> searchBuilder = new SearchBuilder<Task>("sis_task")
	            .fields("*")
	            .searchCriterios(searchCriterios);
	    Search<Task> search = searchBuilder.build();
	    return search;
	}

}
