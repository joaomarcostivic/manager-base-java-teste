package com.tivic.manager.task;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;


public interface ITaskExecucao {

	public void executarTask(int cdTask) throws ValidacaoException, Exception;

}
