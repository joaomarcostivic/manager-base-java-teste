package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.manager.grl.*;
import com.tivic.sol.connection.Conexao;


public class VinculoServices {
	public static final int PARCEIRO   = 1;
	public static final int CORRETOR   = 2;
	public static final int PROMOTOR   = 3;

	public static int verificaVinculoEstatico(int cdVinculo, String nmVinculo, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			Vinculo vinculo = VinculoDAO.get(cdVinculo, connect);
			if(vinculo==null)	{
				Conexao.getSequenceCode("GRL_VINCULO");
				pstmt = connect.prepareStatement("INSERT INTO GRL_VINCULO (CD_VINCULO,"+
				                                  "NM_VINCULO,"+
				                                  "LG_ESTATICO,"+
			    	                              "LG_FUNCAO) VALUES (?, ?, ?, ?)");
				pstmt.setInt(1, cdVinculo);
				pstmt.setString(2,nmVinculo);
				pstmt.setInt(3, 1); // Estatico
				pstmt.setInt(4, 0);
				pstmt.executeUpdate();
			}
			else if(!vinculo.getNmVinculo().equals(nmVinculo))	{
				vinculo.setNmVinculo(nmVinculo);
				vinculo.setLgEstatico(1);
				VinculoDAO.update(vinculo, connect);
			}
			return cdVinculo;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoServices.verificaVinculoEstatico: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoServices.verificaVinculoEstatico: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllWithoutEstatico() {
		return Search.find("SELECT * FROM GRL_VINCULO WHERE lg_estatico IS NULL OR lg_estatico <> 1", new ArrayList<ItemComparator>(), Conexao.conectar());
	}

}