package com.tivic.manager.fix.mob.ait.cidade;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.cidadeproprietario.ICorretorIDCidade;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class FixCidadeService implements IFixCidadeService {
	private AitRepository aitRepository;
	private ICorretorIDCidade corretorIDCidade;
	private ManagerLog managerLog;
	
	public FixCidadeService() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.corretorIDCidade = (ICorretorIDCidade) BeansFactory.get(ICorretorIDCidade.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	public void corrigirCidadeUf(String dtInfracao, List<Integer> cdsLoteImpressao, List<Integer> cdsAit) throws Exception, ValidacaoException {
		CustomConnection customConnection = new CustomConnection();
		try {
			this.managerLog.info("INICIALIZAÇÃO", "Correção para cidade/uf foi iniciada.\n");
			customConnection.initConnection(true);
			SearchCriterios searchCriterios = new SearchCriteriosFixCidadeBuilder()
					.setDtInfracao("A.dt_infracao", dtInfracao)
					.setcdsAit("A.cd_ait", cdsAit)
					.setcdsLoteImpressao("cd_lote_impressao", cdsLoteImpressao)
					.build();
			List<AitFixDTO> aitFixDTOList =buscarAits(searchCriterios, customConnection);
			this.managerLog.info("QUANTIDADE PARA ATUALIZAÇÃO", "Existe(m) " + aitFixDTOList.size() + " AIT(s) para correção.\n");
			atualizarAit(aitFixDTOList, customConnection);
			this.managerLog.info("FINALIZAÇÃO", "Os dados foram corrigidos!\n");
			customConnection.finishConnection();			
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private List<AitFixDTO> buscarAits(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
	    Search<AitFixDTO> search = new SearchBuilder<AitFixDTO>("mob_ait A")
	            .fields(" DISTINCT ON (A.cd_ait) A.cd_ait, replace(ds_saida->>'codigoMunicipioEmplacamento','\"', '') as id_cidade_prodemge,  ds_saida->>'ufJurisdicaoVeiculo' as uf_placa_prodemge ") 
	            .addJoinTable(" JOIN mob_ait_movimento B ON(A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento) ")
	            .addJoinTable("	JOIN mob_lote_impressao_ait C ON(A.cd_ait = C.cd_ait) ")
	            .addJoinTable("	LEFT OUTER JOIN grl_cidade D  ON (A.cd_cidade = D.cd_cidade) ")
	            .addJoinTable(" LEFT OUTER JOIN grl_estado E ON (D.cd_estado = E.cd_estado) ")
	            .addJoinTable(" LEFT OUTER JOIN mob_arquivo_movimento F ON (A.cd_ait = F.cd_ait) ")
	            .searchCriterios(searchCriterios)
	            .additionalCriterias(" (UPPER(sg_uf_veiculo) <> UPPER(replace(ds_saida->>'ufJurisdicaoVeiculo','\"', '')) OR " + 
	            		"		   (CASE WHEN " + 
	            		"		   		replace(ds_saida->>'codigoMunicipioEmplacamento','\"', '') <> 'null' " +
	            		"		   		AND replace(ds_saida->>'codigoMunicipioEmplacamento','\"', '') <> '' " +
	            		"				AND id_cidade is not null" + 
	            		"				then " + 
	            		"					replace(ds_saida->>'codigoMunicipioEmplacamento','\"', '')::int <> (id_cidade)::int " + 
	            		"			ELSE" + 
	            		"					false" + 
	            		"			END)" + 
	            		"		)" +
	            		"       AND F.tp_status = "+ TipoStatusEnum.REGISTRO_INFRACAO.getKey() + 
	            		"		AND ds_saida->>'ufJurisdicaoVeiculo' <> 'null'")
	            .orderBy(" A.cd_ait DESC ") 
	            .customConnection(customConnection)
	    		.build();
	    if(search.getList(AitFixDTO.class).isEmpty())
	    	throw new NoContentException("Nenhum AIT encontrado para correção.");
	    List<AitFixDTO> aitFixDTOList = search.getList(AitFixDTO.class);
		return aitFixDTOList;
	}
	
	private void atualizarAit(List<AitFixDTO> aitFixDTOList, CustomConnection customConnection) throws Exception {
		for (AitFixDTO aitFixDTO : aitFixDTOList) {
			this.managerLog.info("ATUALIZANDO", "O AIT " + aitFixDTO.getCdAit() + " esta sendo atualizado.\n");
			Ait ait = this.aitRepository.get(aitFixDTO.getCdAit(), customConnection);
			montarAit(ait, aitFixDTO);
			this.aitRepository.update(ait, customConnection);
		}
	}
	
	private Ait montarAit(Ait ait, AitFixDTO aitFixDTO) throws Exception {
		Cidade cidade = this.corretorIDCidade.getCidadeById(aitFixDTO.getIdCidadeProdemge());
		ait.setSgUfVeiculo(aitFixDTO.getUfPlacaProdemge());
		ait.setCdCidade(cidade.getCdCidade());
		this.managerLog.info("AIT CORRIGIDO: ", "O ait corrigo: " + ait.toString() +"\n");
		return ait;
	}
	
}
