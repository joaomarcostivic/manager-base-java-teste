package com.tivic.manager.mob.lote.impressao.viaunica.nic.task;

import java.sql.Types;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.fix.mob.ait.proprietario.exceptions.DadosEmptyListException;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.pessoa.TipoPessoaEnum;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.infracao.TipoResponsabilidadeInfracaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.GeradorNic;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.ValidadorGeracaoNIC;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.exceptions.UsuarioException;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ClientGeracaoNicTask implements IClientGeracaoNicTask {
	private ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	private IUsuarioRepository usuarioRepository;
	private IParametroRepository parametroRepository;
	
	public ClientGeracaoNicTask() throws Exception {
		usuarioRepository = (IUsuarioRepository) BeansFactory.get(IUsuarioRepository.class);
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
    public void taskGerarMultaNic() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		int contadorAitsGerados = 0;
        try {
        	customConnection.initConnection(true);
            managerLog.info("TASK GERAÇÃO DE NIC INICIADA: ", new GregorianCalendar().getTime().toString());
            List<Ait> aitMovimentoList = buscarAits(customConnection);
            managerLog.info("QUANTIDADE DE AITS CANDIDATOS: ", String.valueOf(aitMovimentoList.size()));
            for (Ait aitOriginarioNic : aitMovimentoList) {
	            if (new ValidadorGeracaoNIC().validate(aitOriginarioNic, customConnection)) {
	                new GeradorNic(customConnection).generate(aitOriginarioNic, getUsuario(customConnection), customConnection);
	                contadorAitsGerados++;
	            }
	        }
            managerLog.info("QUANTIDADE DE AITS QUE GERARAM NIC: ", String.valueOf(contadorAitsGerados));
            customConnection.finishConnection();
        } catch (NoContentException nce) {
            managerLog.info("NENHUM AIT GERADOR DE NIC ENCONTRADO: ", new GregorianCalendar().getTime().toString());
        } catch (Exception e) {
            managerLog.showLog(e);
        } finally {
        	customConnection.closeConnection();
            managerLog.info("TASK GERAÇÃO DE NIC FINALIZADA: ", new GregorianCalendar().getTime().toString());
        }
    }
	
	private List<Ait> buscarAits(CustomConnection customConnection) throws DadosEmptyListException, Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("C.tp_responsabilidade", TipoResponsabilidadeInfracaoEnum.MULTA_RESPONSABILIDADE_CONDUTOR.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("A.tp_pessoa_proprietario", TipoPessoaEnum.JURIDICA.getKey(), true);
		searchCriterios.addCriterios("A.nr_cpf_cnpj_proprietario", "", ItemComparator.NOTISNULL, Types.INTEGER);
		searchCriterios.addCriterios("A.cd_ait_origem", "", ItemComparator.ISNULL, Types.INTEGER);
		searchCriterios.addCriterios("A.dt_infracao", dtLimiteInfracao(), ItemComparator.GREATER_EQUAL, Types.VARCHAR);
	    List<Ait> aitList = findAits(searchCriterios, customConnection).getList(Ait.class);
	    if(aitList.isEmpty()) {
	        throw new NoContentException("Não há AITs candidatos para gerar NIC.");
	    } 
	    return aitList;
	}

	public Search<Ait> findAits(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Ait> search = new SearchBuilder<Ait>(" mob_ait A")
	            .fields(" A.* ")
	            .addJoinTable(" JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao) ")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" NOT EXISTS ("
						+ " 		   		SELECT B2.cd_ait FROM mob_ait_movimento B2"
						+ "			  		WHERE B2.tp_status = "+ TipoStatusEnum.NIC_ENVIADA.getKey() + " "
						+ "   				AND B2.cd_ait = A.cd_ait"
						+ " 		)"
				).additionalCriterias(" NOT EXISTS ("
						+ " 		   		SELECT A2.cd_ait FROM mob_ait A2"
						+ "			  		WHERE A2.cd_ait_origem = A.cd_ait "
						+ " 		)"
				).additionalCriterias(" EXISTS ("
						+ " 		   		SELECT B3.cd_ait FROM mob_ait_movimento B3"
						+ "			  		WHERE B3.tp_status = "+ TipoStatusEnum.NIP_ENVIADA.getKey() + " "
						+ "   				AND B3.cd_ait = A.cd_ait"
						+ "					AND B3.lg_enviado_detran = "+ TipoLgEnviadoDetranEnum.REGISTRADO.getKey()+""
						+ " 		)"
				).additionalCriterias(" ("
			            + " 		   		EXISTS ("
			            + " 		   			SELECT B4.cd_ait FROM mob_ait_movimento B4"
			            + "			  			WHERE B4.tp_status = " + TipoStatusEnum.PUBLICACAO_NIP.getKey() + " "
			            + "   					AND B4.cd_ait = A.cd_ait"
			            + "						AND B4.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey() + ""
			            + " 				)"
			            + "				OR"
			            + "				EXISTS ("
			            + " 		   			SELECT B5.cd_ait FROM mob_ait_movimento B5"
			            + "			  			WHERE B5.tp_status = " + TipoStatusEnum.DADOS_CORREIO_NP.getKey() + " "
			            + "   					AND B5.cd_ait = A.cd_ait"
			            + "						AND B5.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey() + ""
			            + "						AND EXISTS ("
			            + "							SELECT 1 FROM mob_correios_etiqueta D"
			            + "							WHERE D.cd_ait = A.cd_ait"
			            +"							AND D.tp_status = "+ TipoStatusEnum.NIP_ENVIADA.getKey() + " "	
			            + "							AND D.st_aviso_recebimento = 1"
			            + "						)"
			            + " 				)"
			            + " 		)"
				).additionalCriterias(" A.nr_cpf_cnpj_proprietario <> '00000000000000' ")
	            .orderBy(" A.dt_infracao DESC ")
	            .build();
	    return search;
	}
	
	private String dtLimiteInfracao() throws Exception {
		int limiteDeAnos = parametroRepository.getValorOfParametroAsInt("MOB_PRAZO_EMISSAO_NIC");
		if (limiteDeAnos <= 0)
			throw new BadRequestException("O parâmetro MOB_PRAZO_EMISSAO_NIC não foi configurado.");
		LocalDate dataLimite = LocalDate.now().minusYears(limiteDeAnos);
	    return dataLimite.toString();
	}
	
	private int getUsuario(CustomConnection customConnection) throws Exception, UsuarioException {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_login", "TIVIC");
		List<Usuario> usuario = this.usuarioRepository.find(searchCriterios, customConnection);
		if (usuario.isEmpty()) {
			throw new UsuarioException("Usuário TIVIC não localizado.");
		}
		return usuario.get(0).getCdUsuario();	
	}
}
