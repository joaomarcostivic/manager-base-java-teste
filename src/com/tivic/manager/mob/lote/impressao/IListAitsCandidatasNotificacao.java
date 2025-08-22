package com.tivic.manager.mob.lote.impressao;

import java.sql.SQLException;
import java.util.List;
import com.tivic.manager.mob.Ait;

public interface IListAitsCandidatasNotificacao {
	List<Ait> build() throws IllegalArgumentException, SQLException, Exception;
}
