package com.tivic.manager.ptc.portal.baseantiga;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.mob.pessoa.dto.AitPortalDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IPortalNotificacoesService {
	List<AitPortalDTO> listarNotificacoes(String nrPlaca, String nrRenavam, String nrAit) throws ValidacaoException, Exception;
	byte[] gerarSegundaViaNotificacao(int cdAit, int tpStatus) throws ValidacaoException, Exception;
	List<CidadeDTO> findCidade() throws SQLException;
	byte[] getCartaJulgamento(int cdAit, int tpStatus) throws Exception;
	byte[] getCartaJulgamento(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception;
}
