package com.tivic.manager.fsc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class NotaFiscalItemTributoServices {

	public static ArrayList<NotaFiscalItemTributo> getAllItemTributo(int cdNotaFiscal, int cdItem) {
		return getAllItemTributo(cdNotaFiscal, cdItem, null);
	}
	
	public static ArrayList<NotaFiscalItemTributo> getAllItemTributo(int cdNotaFiscal, int cdItem, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ArrayList<NotaFiscalItemTributo> notasItemTributo = new ArrayList<NotaFiscalItemTributo>();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_item_tributo " +
	                											"WHERE cd_nota_fiscal = "+cdNotaFiscal + " AND cd_item = " + cdItem);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				notasItemTributo.add(NotaFiscalItemTributoDAO.get(cdNotaFiscal, cdItem, rs.getInt("cd_tributo"), connect));
			}
		
			return notasItemTributo;
		}
		
		catch(Exception e){return null;}
		
		finally{
			if(isConnectionNull)
				Conexao.rollback(connect);
		}
	}

	
	
}
