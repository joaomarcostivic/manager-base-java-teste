package com.tivic.manager.mob.bi.graficos.local;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LocalServices {
	
	/*Local*/
	public static ResultSetMap findBoatByTipo(ArrayList<ItemComparator> criterios) {
		return findBoatByTipo(criterios, null);
	}
	public static ResultSetMap findBoatByTipo(ArrayList<ItemComparator> criterios, Connection connect) {
		String sqlCount = "SELECT count(*) as qt_tipo, C.nm_tipo_veiculo"+
				" FROM mob_boat A"+
				" JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)"+
				" LEFT OUTER JOIN fta_tipo_veiculo C ON (B.cd_tipo = C.cd_tipo_veiculo)";
		ResultSetMap rsmCount = Search.findAndLog(sqlCount, "GROUP BY C.nm_tipo_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);

		return rsmCount;
	}
	
	
	public static ResultSetMap findBoatBoletim(ArrayList<ItemComparator> criterios) {
		return findBoatBoletim(criterios, null);
	}
	public static ResultSetMap findBoatBoletim(ArrayList<ItemComparator> criterios, Connection connect) {
		int seg = 0, ter = 0, qua = 0, qui = 0, sex = 0, sab = 0, dom = 0, nulo = 0;
		String sql = "SELECT A.dt_ocorrencia"+
				" FROM mob_boat A";
		ResultSetMap rsm = Search.findAndLog(sql, "", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);
		
		while(rsm.next()) {
			Timestamp day = Util.convCalendarToTimestamp(Util.convStringToCalendar(rsm.getString("DT_OCORRENCIA")));
			switch(day.getDay()) {
			  case 1:
			    seg++;
			    break;
			  case 2:
			    ter++;
			    break;
			  case 3:
				qua++;
				break;
			  case 4:
				qui++;
				break;
			  case 5:
				sex++;
				break;
			  case 6:
				sab++;
				break;
			  case 7:
				dom++;
				break;  
			  default:
			    nulo++;
			}
			rsm.deleteRow();
			if(rsm.first())
				rsm.beforeFirst();
			else
				rsm.previous();
			continue;
		}
		HashMap<String, Object> regTotais = new HashMap<String, Object>();
		regTotais.put("seg", seg);
		regTotais.put("ter", ter);
		regTotais.put("qua", qua);
		regTotais.put("qui", qui);
		regTotais.put("sex", sex);
		regTotais.put("sab", sab);
		regTotais.put("dom", dom);
		regTotais.put("nulo", nulo);
		regTotais.put("Total", seg + ter + qua + qui + sex + sab + dom);
		rsm.addRegister(regTotais);
		
		return rsm;
	}
	
	
	public static ResultSetMap findBoatLocal(ArrayList<ItemComparator> criterios) {
		return findBoatLocal(criterios, null);
	}
	public static ResultSetMap findBoatLocal(ArrayList<ItemComparator> criterios, Connection connect) {
		HashMap<String, Object> regTotais = new HashMap<String, Object>();
		String sql = "SELECT COUNT(*) as qt_local, A.ds_local_ocorrencia"+
				" FROM mob_boat A";
		ResultSetMap rsm = Search.findAndLog(sql, "GROUP BY A.ds_local_ocorrencia ORDER BY qt_local DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);

		return rsm;
	}
	
	public static ResultSetMap findBoatLocalHora(ArrayList<ItemComparator> criterios) {
		return findBoatLocalHora(criterios, null);
	}
	public static ResultSetMap findBoatLocalHora(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql = "SELECT COUNT(*) as qt_ocorrencias, EXTRACT (HOUR FROM A.dt_ocorrencia) AS hr_ocorrencia"+
				" FROM mob_boat A";
		ResultSetMap rsm = Search.findAndLog(sql, "GROUP BY hr_ocorrencia ORDER BY hr_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);

		return rsm;
	}
	
	public static ResultSetMap findBoatLocaldia(ArrayList<ItemComparator> criterios) {
		return findBoatLocaldia(criterios, null);
	}
	public static ResultSetMap findBoatLocaldia(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql = "SELECT COUNT(*) as qt_ocorrencias, EXTRACT (DAY FROM A.dt_ocorrencia) AS dia_ocorrencia"+
				" FROM mob_boat A";
		ResultSetMap rsm = Search.findAndLog(sql, "GROUP BY dia_ocorrencia ORDER BY dia_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect != null);

		return rsm;
	}
	
	

}

