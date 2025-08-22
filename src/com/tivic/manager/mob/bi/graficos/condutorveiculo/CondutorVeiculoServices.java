package com.tivic.manager.mob.bi.graficos.condutorveiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.fta.VeiculoServices;
import com.tivic.manager.mob.bi.graficos.condutorveiculo.CondutorVeiculoDTO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatVeiculo;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CondutorVeiculoServices {


	public static final int TP_SEXO_MASCULINO = 0;
	public static final int TP_SEXO_FEMININO  = 1;
	
	public static String[] tipoSexo = {"Masculino",
		"Feminino"};
	
	public static ResultSetMap findBoatByGenero(ArrayList<ItemComparator> criterios) {
		return findBoatByGenero(criterios, null);
	}
	public static ResultSetMap findBoatByGenero(ArrayList<ItemComparator> criterios, Connection connect) {

		String sql = "SELECT count(*) as qt_sexo, B.tp_sexo"+
				" FROM mob_boat A"+
				" JOIN mob_boat_veiculo B on (A.cd_boat = B.cd_boat)";
		
		ResultSetMap rsm = Search.findAndLog(sql, "GROUP BY B.tp_sexo", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);
	
		return rsm;
	}
	
	
	public static ResultSetMap findBoatByIdade(ArrayList<ItemComparator> criterios) {
		return findBoatByIdade(criterios, null);
	}
	public static ResultSetMap findBoatByIdade(ArrayList<ItemComparator> criterios, Connection connect) {
		int pri = 0,seg = 0, ter = 0,qua = 0,qui = 0,sex = 0, set = 0, oit = 0, non = 0, nulo = 0;
		String sql = "SELECT count(*) as qt_idade, B.nr_idade"+
				" FROM mob_boat A"+
				" JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)";
		ResultSetMap rsm = Search.findAndLog(sql, "GROUP BY B.nr_idade ORDER BY B.nr_idade", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);
		while(rsm.next()) {
			int idade = rsm.getInt("NR_IDADE");
			if(idade < 18 && idade > 0) {
				pri += rsm.getInt("QT_IDADE");
			}
			else if(idade >= 18 && idade < 23) {
				seg += rsm.getInt("QT_IDADE");
			}
			else if(idade >= 23 && idade < 28) {
				ter += rsm.getInt("QT_IDADE");
			}
			else if(idade >= 28 && idade < 33) {
				qua += rsm.getInt("QT_IDADE");
			}
			else if(idade >= 33 && idade < 38) {
				qui += rsm.getInt("QT_IDADE");
			}	
			else if(idade >= 38 && idade < 44) {
				sex += rsm.getInt("QT_IDADE");
			}
			else if(idade >= 44 && idade < 49) {
				set += rsm.getInt("QT_IDADE");
			}
			else if(idade >= 49 && idade < 54) {
				oit += rsm.getInt("QT_IDADE");
			}
			else if(idade >= 54 && idade < 59) {
				non += rsm.getInt("QT_IDADE");
			}
			else {
				nulo += rsm.getInt("QT_IDADE");
			}

			rsm.deleteRow();
			if(rsm.first())
				rsm.beforeFirst();
			else
				rsm.previous();
			continue;
		}
		HashMap<String, Object> regTotais = new HashMap<String, Object>();
		regTotais.put("1-17", pri);
		regTotais.put("18-22", seg);
		regTotais.put("23-27", ter);
		regTotais.put("28-32", qua);
		regTotais.put("33-37", qui);
		regTotais.put("38-43", sex);
		regTotais.put("44-48", set);
		regTotais.put("49-53", oit);
		regTotais.put("54-59", non);
		regTotais.put("nulo", nulo);
		regTotais.put("total", pri+seg+ter+qua+qui+sex+set+oit+non+nulo);
		rsm.addRegister(regTotais);
		return rsm;
	}
	public static ResultSetMap findBoatByEspecie(ArrayList<ItemComparator> criterios) {
		return findBoatByEspecie(criterios, null);
	}
	public static ResultSetMap findBoatByEspecie(ArrayList<ItemComparator> criterios, Connection connect) {

		String sql = "SELECT count(*) as qt_especie, B.cd_especie"+
				" FROM mob_boat A"+
				" JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)";
		ResultSetMap rsm = Search.findAndLog(sql, "GROUP BY B.cd_especie ORDER BY qt_especie DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);
	
		return rsm;
	}
	
	
	public static ResultSetMap findBoatByCidade(ArrayList<ItemComparator> criterios) {
		return findBoatByCidade(criterios, null);
	}
	public static ResultSetMap findBoatByCidade(ArrayList<ItemComparator> criterios, Connection connect) {
		String sqlCount = "SELECT count(*) as qt_cidade, C.nm_cidade, D.sg_estado"+
				" FROM mob_boat A"+
				" JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)"+
				" JOIN grl_cidade C ON (B.cd_cidade = C.cd_cidade)"+
				" JOIN grl_estado D ON (C.cd_estado = D.cd_estado)";
		ResultSetMap rsmCount = Search.findAndLog(sqlCount, "GROUP BY C.cd_cidade,  D.sg_estado ORDER BY qt_cidade DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);

		return rsmCount;
	}
	
	
	public static ResultSetMap findBoatByCategoria(ArrayList<ItemComparator> criterios) {
		return findBoatByCategoria(criterios, null);
	}
	public static ResultSetMap findBoatByCategoria(ArrayList<ItemComparator> criterios, Connection connect) {
		String sqlCount = "SELECT count(*) as qt_categoria, C.nm_categoria"+
				" FROM mob_boat A"+
				" JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)"+
				" LEFT OUTER JOIN fta_categoria_veiculo C ON (B.cd_categoria = C.cd_categoria)";
		ResultSetMap rsmCount = Search.findAndLog(sqlCount, "GROUP BY C.nm_categoria  ORDER BY qt_categoria DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);

		return rsmCount;
	}

}
