package com.tivic.manager.mob.lote.impressao.ListAitsCandidatosLoteNaiBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.inconsistencias.TipoInconsistenciaEnum;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.manager.mob.lote.impressao.IListAitsCandidatosNai;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ListAitsCandidatosLoteNaiBuilder implements IListAitsCandidatosNai{
	public List<Ait> build(ArrayList<ItemComparator> criterios, Connection connect) throws IllegalArgumentException, SQLException, Exception {
		ResultSetMap rsmAits = findAitsNai(criterios, connect);
		return new ResultSetMapper(rsmAits, Ait.class).toList();
	}
	
	private ResultSetMap findAitsNai (ArrayList<ItemComparator> criterios, Connection connect) {	
		String sql = "SELECT "
				+ " K.dt_movimento, K.tp_status, K.nr_processo, A.*,"
				+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, "
				+ " C.nm_cidade, C.nm_cidade AS nm_municipio,"
				+ " 	C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
				+ " E.nm_categoria,"
				+ " F.nm_cor,"
				+ " G.ds_especie,"
				+ " H.nm_marca, H.nm_modelo,"
				+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
				+ " J.nm_agente, J.nr_matricula, "
				+ " M.dt_afericao, M.id_identificacao_inmetro, "
				+ " N.ds_logradouro, "
				+ " P.nm_bairro "
				+ " "
				+ " FROM 			mob_ait 			   A"
				+ " LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)"
				+ " LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)"
				+ " LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)"
				+ " LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)"
				+ " LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)"
				+ " LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)"
				+ " LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)"
				+ " "
				+ " LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)"
 			    + " LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)"
 			    + " LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)"
 			    + " "
 			    + " LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait) "
 			    + " LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) "
 			    + " LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait) "
 			    + " LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro) "
				+ " WHERE K.tp_status = " + AitMovimentoServices.REGISTRO_INFRACAO 
				+ " AND K.lg_enviado_detran = " + AitMovimentoServices.ENVIADO_AO_DETRAN
				+ " AND NOT EXISTS "
				+ " ( "
				+ " 	SELECT tp_status FROM mob_ait_movimento K "
				+ "		WHERE "
				+ "			( "
				+ "			tp_status = " + AitMovimentoServices.NIP_ENVIADA
				+ "			OR tp_status = " + AitMovimentoServices.CADASTRO_CANCELADO
				+ "			OR tp_status = " + AitMovimentoServices.CANCELA_REGISTRO_MULTA
				+ "			OR tp_status = " + AitMovimentoServices.CANCELAMENTO_AUTUACAO
				+ " 		OR tp_status = " + AitMovimentoServices.CANCELAMENTO_MULTA
				+ " 		OR tp_status = " + AitMovimentoServices.CANCELAMENTO_PONTUACAO
				+ "			OR tp_status = " + AitMovimentoServices.DEVOLUCAO_PAGAMENTO
				+ "			OR tp_status = " + AitMovimentoServices.NAI_ENVIADO
				+ "			)"
				+ " AND K.cd_ait = A.cd_ait "
				+ " )"
				+ "	AND NOT EXISTS "
				+ " ( "
				+ "		SELECT B.nr_cod_detran, B.tp_competencia FROM mob_infracao B "
				+ " 	WHERE "
				+ "			( "
				+ " 		B.nr_cod_detran = 50002 "
				+ "			OR B.nr_cod_detran = 50020 "
				+ "         AND tp_competencia = " + TipoCompetenciaEnum.ESTADUAL.getKey()
				+ " 		)"
				+ " AND A.cd_infracao = B.cd_infracao "
				+ " )"
				+ "	AND NOT EXISTS "
				+ " ( "
				+ "		SELECT C.* FROM MOB_AIT_INCONSISTENCIA C "
				+ "		JOIN mob_inconsistencia Z on (Z.cd_inconsistencia = C.cd_inconsistencia)"
				+ " 	WHERE "
				+ "			( "
				+ " 			Z.tp_inconsistencia = " + TipoInconsistenciaEnum.TP_INDEFERIMENTO.getKey()
				+ " 		)"
				+ " AND A.cd_ait = C.cd_ait "
				+ " )"
				+ "	AND NOT EXISTS "
				+ " ( "
				+ "		SELECT D.cd_lote_impressao, D.tp_lote_impressao, E.cd_ait FROM mob_lote_impressao D "
				+ "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao) "	
				+ " 	WHERE "
				+ "			( "
				+ " 			D.tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NAI.getKey()
				+ " 		)"
				+ " AND E.cd_ait = A.cd_ait "
				+ " )";
		return Search.find (sql, " ORDER BY K.CD_AIT DESC, K.dt_movimento DESC" , criterios, connect!=null ? connect : Conexao.conectar(), connect==null);	
	}
}
