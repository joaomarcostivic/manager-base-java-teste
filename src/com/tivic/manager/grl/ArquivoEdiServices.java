package com.tivic.manager.grl;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

public class ArquivoEdiServices {

	public static int insert(ArquivoEdi objeto) {
		return insert(objeto, null);
	}
	public static int insert(ArquivoEdi objeto, Connection connect) {
		boolean isConnNull = connect==null;
		if(isConnNull)
			connect = Conexao.conectar();
		try {
			// Verificando se o arquivo já não foi gravado
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM grl_arquivo_edi A, grl_arquivo B " +
					"WHERE A.cd_arquivo = B.cd_arquivo " +
					"  AND A.nr_remessa = "+objeto.getNrRemessa()+
					"  AND B.cd_tipo_arquivo = "+objeto.getCdTipoArquivo()+
					(objeto.getCdConta()>0 ? "  AND A.cd_conta = "+objeto.getCdConta() : ""));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				objeto.setCdArquivo(rs.getInt("cd_arquivo"));
				ArquivoEdiDAO.update(objeto, connect);
				return objeto.getCdArquivo();
			}
			else
				return ArquivoEdiDAO.insert(objeto, connect);
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiServices.insert: " + sqlExpt);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoEdiServices.insert: " + e);
			return -1;
		}
		finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}

}
