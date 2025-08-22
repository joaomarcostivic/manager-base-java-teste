package com.tivic.manager.mob.guiapagamento;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.guiapagamento.np.GeraNpFicticia;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class GuiaPagamentoService implements IGuiaPagamentoService{

	@Override
	public PagedResponse<Ait> findGuiaPagamento(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<Ait> guiasPagamento = findGuiaPagamento(searchCriterios, customConnection);
			if(guiasPagamento.getList(Ait.class).isEmpty())
				throw new NoContentException("Nenhum registro encontrado.");
			customConnection.finishConnection();
			return new PagedResponse<Ait>(guiasPagamento.getList(Ait.class), guiasPagamento.getRsm().getTotal()); 
		} finally {
			customConnection.closeConnection();
		}
		
	}
	
	@Override
	public Search<Ait> findGuiaPagamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
			Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
					.fields("DISTINCT ON (A.cd_ait) A.*")
					.addJoinTable("JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
					.searchCriterios(searchCriterios)
					.additionalCriterias("A.cd_ait NOT IN (SELECT cd_ait FROM mob_ait_movimento WHERE tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + ")")
					.groupBy("A.cd_ait ")
					.orderBy(" A.cd_ait DESC")
					.count()
					.build();
			
			return  search;
	}
	
	@Override	
	public byte[] gerarNpFicticia() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] documento = new GeraNpFicticia().gerarDocumento(customConnection);
			customConnection.finishConnection(); 
			return documento;
		}
		finally {
			customConnection.closeConnection(); 
		}
	}
	
}
