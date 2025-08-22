package com.tivic.manager.mob.lote.impressao;

import java.util.GregorianCalendar;
import com.tivic.sol.connection.CustomConnection;

public interface IPrazoRecurso {
	GregorianCalendar gerarPrazo(int cdAit, CustomConnection customConnection) throws Exception;
}
