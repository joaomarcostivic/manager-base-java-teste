package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.util.Util;

public class GeoTrackerServices {

	public static ResultSetMap track(String idOrgao, String nrMatriculaAgente, GregorianCalendar dtInicial, GregorianCalendar dtFinal, boolean lastPosition) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (idOrgao != null && !idOrgao.equals(""))
			criterios.add(new ItemComparator("A.id_orgao", idOrgao, ItemComparator.EQUAL, Types.VARCHAR));
		if (nrMatriculaAgente != null && !nrMatriculaAgente.equals(""))
			criterios.add(new ItemComparator("A.nr_matricula_agente", nrMatriculaAgente, ItemComparator.EQUAL, Types.VARCHAR));
		if (dtInicial != null)
			criterios.add(new ItemComparator("A.dt_historico", Util.formatDateTime(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		if (dtFinal != null)
			criterios.add(new ItemComparator("A.dt_historico", Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		
		criterios.add(new ItemComparator("A.id_equipamento", "", ItemComparator.DIFFERENT, Types.VARCHAR));
		
		return find(criterios, lastPosition, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, boolean lastPosition) {
		return find(criterios, lastPosition, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, false, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, boolean lastPosition, Connection connect) {
		
		return Search.find("SELECT A.*, B.nm_orgao, C.nm_agente, C.tp_agente, D.nm_cidade FROM str_geo_tracker A " +
						   " LEFT OUTER JOIN str_orgao B ON (A.id_orgao = B.id_orgao) " +
						   " LEFT OUTER JOIN grl_cidade D ON (B.cd_cidade = D.cd_cidade) " +
						   " LEFT OUTER JOIN str_agente C ON (A.nr_matricula_agente = C.nr_matricula) " +
						   (lastPosition ? "WHERE A.cd_tracker IN (SELECT DISTINCT ON(id_orgao, nr_matricula_agente) cd_tracker " +
																	"FROM str_geo_tracker " +
																	"WHERE nr_matricula_agente <> '' " +
																	"ORDER BY id_orgao, nr_matricula_agente, dt_historico DESC) " : ""), 
						   "ORDER BY A.id_orgao, A.nr_matricula_agente, A.dt_historico ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap trackEquipamentos(String idOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, boolean lastPosition) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (idOrgao != null && !idOrgao.equals(""))
			criterios.add(new ItemComparator("B.id_orgao", idOrgao, ItemComparator.EQUAL, Types.VARCHAR));		
		if (dtInicial != null)
			criterios.add(new ItemComparator("A.dt_historico", Util.formatDateTime(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		if (dtFinal != null)
			criterios.add(new ItemComparator("A.dt_historico", Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		
		criterios.add(new ItemComparator("C.id_equipamento", "", ItemComparator.DIFFERENT, Types.VARCHAR));
		
		return findVeiculos(criterios, lastPosition, null);
	}

	public static ResultSetMap findVeiculos(ArrayList<ItemComparator> criterios, boolean lastPosition) {
		return findVeiculos(criterios, lastPosition, null);
	}
	
	public static ResultSetMap findVeiculos(ArrayList<ItemComparator> criterios, boolean lastPosition, Connection connect) {
		
//		return Search.find("SELECT A.*, B.nr_prefixo, C.nr_placa, C.cd_tipo_veiculo, D.id_equipamento, E.*, F.*, G.nm_cidade "+
//						   "FROM mob_veiculo_equipamento A " +
//						   "LEFT OUTER JOIN mob_concessao_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " +
//						   "LEFT OUTER JOIN fta_veiculo           C ON (A.cd_veiculo = C.cd_veiculo) " + 
//						   "LEFT OUTER JOIN grl_equipamento       D ON (A.cd_equipamento = D.cd_equipamento) " +
//						   "LEFT OUTER JOIN str_orgao             E ON (D.cd_orgao = E.cd_orgao) "+
//						   "LEFT OUTER JOIN str_geo_tracker       F ON (E.id_orgao = F.id_orgao  AND D.id_equipamento = F.id_equipamento) "+
//						   "LEFT OUTER JOIN grl_cidade            G ON (E.cd_cidade = G.cd_cidade) "+
//						   (lastPosition ? "WHERE G.cd_tracker IN (SELECT DISTINCT ON(id_orgao, nr_matricula_agente) cd_tracker " +
//														"FROM str_geo_tracker " +
//														"WHERE nr_matricula_agente = '' " +
//														"ORDER BY id_orgao, nr_matricula_agente, dt_historico DESC) " : ""), 
//						   "ORDER BY E.id_orgao, B.nr_prefixo, F.dt_historico ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		try {
			
			String orderBy = "ORDER BY B.id_orgao, C.tp_equipamento, AA.nm_agente, VB.nr_prefixo, A.dt_historico";
			
			String sql = "SELECT A.*, B.cd_orgao, C.cd_orgao, B.nm_orgao, EE.cd_cidade , C.cd_equipamento, " +
					     "C.nm_equipamento, C.tp_equipamento, C.st_equipamento, " + 
						 /*Agente*/
						 "AA.nm_agente, AA.nr_matricula, AA.tp_agente, "+
						 /*Veículo*/
						 "VA.*, VB.nr_prefixo, VB.st_concessao_veiculo, "+
						 "VC.nr_placa, VC.st_veiculo, VC.nr_licenciamento, VD.nr_concessao, VD.tp_concessao, VE.nm_pessoa "+
						 /*Tracker*/
						 "FROM mob_geo_tracker A "+
						 /*Órgão*/
						 "LEFT OUTER JOIN mob_orgao         B ON (A.cd_orgao = B.cd_orgao)  "+
						 /*Equipamento*/
						 "LEFT OUTER JOIN grl_equipamento   C ON (A.id_equipamento = C.id_equipamento) "+
						 "LEFT OUTER JOIN mob_equipamento   D ON (C.cd_equipamento = D.cd_equipamento) "+
						 /*Cidade*/
						 "LEFT OUTER JOIN grl_pessoa           E  ON (B.cd_pessoa_orgao = E.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_pessoa_endereco  EE ON (E.cd_pessoa = EE.cd_pessoa "+
						 "										  AND EE.lg_principal = "+PessoaEnderecoServices.ENDERECO_PRINCIPAL+") " +  
						 /*Agente*/
						 "LEFT OUTER JOIN mob_agente              AA ON (A.nr_matricula_agente = AA.nr_matricula) "+
						 /*Veiculo*/
						 "LEFT OUTER JOIN mob_veiculo_equipamento VA ON (D.cd_equipamento = VA.cd_equipamento) "+ 
					 	 "LEFT OUTER JOIN mob_concessao_veiculo   VB ON (VA.cd_veiculo = VB.cd_veiculo) "+
						 "LEFT OUTER JOIN fta_veiculo             VC ON (VA.cd_veiculo = VC.cd_veiculo) "+ 
						 "LEFT OUTER JOIN mob_concessao           VD ON (VB.cd_concessao = VD.cd_concessao) "+
						 "LEFT OUTER JOIN grl_pessoa              VE ON (VD.cd_concessionario = VE.cd_pessoa) ";
						
						
			ResultSetMap rsm = Search.find(sql, orderBy, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			return rsm;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerServices.findVeiculos: " + e);
			return null;
		}
		
		
	}
		
}
