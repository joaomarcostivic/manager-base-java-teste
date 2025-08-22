package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class Configuration {

	public static final int MOD_GRATUITO = 1;
	public static final int MOD_PROFISSIONAL = 2;

	public static ResultSetMap findParametros() {
		return findParametros(null);
	}

	public static ResultSetMap findParametros(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					"FROM seg_modulo A, seg_sistema B " +
					"WHERE A.cd_sistema = B.cd_sistema " +
					"  AND A.id_modulo = ? " +
					"  AND B.id_sistema = ? ");
			pstmt.setString(1, "agd");
			pstmt.setString(2, "dotMng");
			ResultSet rs = pstmt.executeQuery();
			boolean next = rs.next();
			int cdModulo = !next ? 0 : rs.getInt("cd_modulo");
			int cdSistema = !next ? 0 : rs.getInt("cd_sistema");

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("tp_nivel_acesso", Integer.toString(ParametroServices.NIVEL_ACESSO_SISTEMA), ItemComparator.EQUAL, java.sql.Types.SMALLINT));
			criterios.add(new ItemComparator("cd_sistema", Integer.toString(cdSistema), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			criterios.add(new ItemComparator("cd_modulo", Integer.toString(cdModulo), ItemComparator.EQUAL, java.sql.Types.INTEGER));

			return ParametroServices.find(criterios, connection);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
