package com.tivic.manager.fsc;

import java.sql.Connection;
import java.sql.PreparedStatement;


import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;

public class AdicaoServices {

	public static ResultSetMap getAllByCdNfe(int cdNotaFiscal){
		Connection connect = Conexao.conectar();
		try{
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.nr_ncm FROM fsc_adicao A " +
																"	JOIN fsc_declaracao_importacao B ON (A.cd_declaracao_importacao = B.cd_declaracao_importacao) " +
																"	JOIN grl_ncm C ON (A.cd_ncm = C.cd_ncm) " +
																"	WHERE B.cd_nota_fiscal = "+cdNotaFiscal);
			return new ResultSetMap(pstmt.executeQuery());
		}
		
		catch(Exception e){return null;}
		
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAdicaoByNotaFiscalNcm(int cdNotaFiscal, int cdNcm){
		Connection connect = Conexao.conectar();
		try{
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* FROM fsc_adicao A " +
																"	JOIN fsc_declaracao_importacao B ON (A.cd_declaracao_importacao = B.cd_declaracao_importacao) " +
																"	JOIN fsc_nota_fiscal 		   C ON (B.cd_nota_fiscal = C.cd_nota_fiscal) " +
																"	WHERE C.cd_nota_fiscal = " + cdNotaFiscal + " AND A.cd_ncm = " + cdNcm);
			return new ResultSetMap(pstmt.executeQuery());
		}
		
		catch(Exception e){return null;}
		
		finally{
			Conexao.desconectar(connect);
		}
	}
}


