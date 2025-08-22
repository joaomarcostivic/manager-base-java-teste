package com.tivic.manager.mob;

import java.sql.Connection;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistenciaDAO;
import com.tivic.manager.mob.AitInconsistenciaBuilder.AitInconsistenciaBuilder;

public class AitReportInconsistencia {	
	public static void salvarInconsistencia(int tpInconsistencia, int tpStatusPretendido, Ait ait){
		salvarInconsistencia(tpInconsistencia, tpStatusPretendido, ait, null);
	}
	
	public static void salvarInconsistencia(int tpInconsistencia, int tpStatusPretendido, Ait ait, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, tpInconsistencia, tpStatusPretendido);
			AitInconsistenciaDAO.insert(aitInconsistencia, connect);
			if(isConnectionNull){
				connect.commit();
			}
		}catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			System.out.println("Erro em AitReportInconsistencia > salvarInconsistencia()");
			e.printStackTrace();
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}