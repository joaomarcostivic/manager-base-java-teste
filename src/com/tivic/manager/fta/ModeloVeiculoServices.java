package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.manager.bpm.Bem;
import com.tivic.manager.bpm.BemDAO;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ModeloVeiculoServices {

	public static int save(ModeloVeiculo modelo){
		return save(modelo, null);
	}
	public static int save(ModeloVeiculo modelo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno;
			if(modelo.getCdModelo()==0){
				Bem bem = new Bem(0, 0, modelo.getNmModelo(), null, null, null, null, 0, null, null, 0, 0, 0, null, 0, null, 0, 0);
				retorno = BemDAO.insert(bem, connect);
				if(retorno>=0){
					modelo.setCdModelo(retorno);
					retorno = ModeloVeiculoDAO.insert(modelo, connect);
				}
			}
			else{
				retorno = ModeloVeiculoDAO.update(modelo, connect);
				retorno = retorno>0?modelo.getCdModelo():retorno;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllModeloMarca() {
		return getAllModeloMarca(null);
	}

	public static ResultSetMap getAllModeloMarca(Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_modelo_veiculo A, bpm_bem B, grl_produto_servico C, bpm_marca D " +
											"  WHERE A.cd_modelo = B.cd_bem " +
											"  AND   B.cd_bem    = C.cd_produto_servico " +
											"  AND   A.cd_marca  = D.cd_marca " +
											"  ORDER BY nm_marca, A.nm_modelo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoServices.getAllModeloMarca: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoServices.getAllModeloMarca: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findModeloMarca(ArrayList<ItemComparator> criterios) {
		return findModeloMarca(criterios, null);
	}

	public static ResultSetMap findModeloMarca(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM fta_modelo_veiculo a, bpm_marca b where a.cd_marca=b.cd_marca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}