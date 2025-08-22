package com.tivic.manager.fsc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NotaFiscalTributoServices {

	public static ArrayList<NotaFiscalTributo> getAllTributo(int cdNotaFiscal) {
		return getAllTributo(cdNotaFiscal, null);
	}
	
	public static ArrayList<NotaFiscalTributo> getAllTributo(int cdNotaFiscal, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			connect = (isConnectionNull ? Conexao.conectar() : connect);
			
			ArrayList<NotaFiscalTributo> notasTributo = new ArrayList<NotaFiscalTributo>();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_tributo " +
	                											"WHERE cd_nota_fiscal = "+cdNotaFiscal);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				notasTributo.add(NotaFiscalTributoDAO.get(cdNotaFiscal, rs.getInt("cd_tributo")));
			}
		
			return notasTributo;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			Conexao.desconectar(connect);
		}
	}

}
