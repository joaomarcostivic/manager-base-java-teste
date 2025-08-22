package com.tivic.manager.mob.lotes.service.impressao.consulta;

import java.sql.SQLException;
import java.util.List;
import com.tivic.manager.mob.Ait;

public interface IListAitsCandidatasNotificacao {
	List<Ait> build() throws IllegalArgumentException, SQLException, Exception;
}
