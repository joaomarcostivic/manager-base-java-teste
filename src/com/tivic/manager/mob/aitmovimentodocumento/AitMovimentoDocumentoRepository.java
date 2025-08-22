package com.tivic.manager.mob.aitmovimentodocumento;

import java.util.List;

import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import javax.ws.rs.BadRequestException;

public interface AitMovimentoDocumentoRepository {
	public void insert(AitMovimentoDocumento aitMovimentoDocumento, CustomConnection customConnection) throws Exception;
	public void update(AitMovimentoDocumento aitMovimentoDocumento, CustomConnection customConnection) throws Exception;
	public AitMovimentoDocumento get(int cdAit, int cdMovimento, int cdDocumento) throws Exception;
	public AitMovimentoDocumento get(int cdAit, int cdMovimento, int cdDocumento, CustomConnection customConnection) throws Exception;
	public List<AitMovimentoDocumento> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitMovimentoDocumento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public AitMovimentoDocumento getAit (int cdAit, int cdMovimento) throws BadRequestException, Exception;
}
