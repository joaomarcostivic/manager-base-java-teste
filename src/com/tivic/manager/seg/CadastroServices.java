package com.tivic.manager.seg;

import sol.dao.ResultSetMap;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

public class CadastroServices{

    public static ResultSetMap getCadastrosOfSistema(int cdSistema)    {
        Connection con = Conexao.conectar();
        try	{
        	PreparedStatement pstmt = con.prepareStatement((new StringBuilder("SELECT * FROM seg_cadastro WHERE cd_sistema  = ")).append(cdSistema).toString());
        	return new ResultSetMap(pstmt.executeQuery());
        }
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(con);
		}
    }

    public static ResultSetMap getPermissoesOfCadastro(int cdSistema, int cdCadastro)	{
        Connection con = Conexao.conectar();
        try	{
        	PreparedStatement pstmt = con.prepareStatement((new StringBuilder("SELECT * FROM seg_atividade WHERE cd_sistema  = ")).append(cdSistema).append("  AND cd_cadastro = ").append(cdCadastro).toString());
        	return new ResultSetMap(pstmt.executeQuery());
        }
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(con);
		}
    }
}
