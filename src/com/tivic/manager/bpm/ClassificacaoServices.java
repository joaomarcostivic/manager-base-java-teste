package com.tivic.manager.bpm;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class ClassificacaoServices {

	public static ResultSetMap getAllHierarquia() {
		return getAllHierarquia(null);
	}

	public static ResultSetMap getAllHierarquia(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			ResultSetMap rsm = ClassificacaoDAO.find(criterios, connect);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("cd_classificacao_superior") != 0) {
					int pointer = rsm.getPointer();
					int cdClassificacao = rsm.getInt("cd_classificacao_superior");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("cd_classificacao", new Integer(rsm.getInt("cd_classificacao_superior")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("cd_classificacao")==cdClassificacao;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("cd_classificacao")==cdClassificacao;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}