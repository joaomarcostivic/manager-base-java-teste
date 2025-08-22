package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.tivic.sol.connection.Conexao;

import sol.util.Result;

public class ParametroOpcaoServices {
	
	public Result saveOpcoesLimpoArray(int cdParametro, String[] options) {
		try {
			this.limparOpcoesParametro(cdParametro);
			
			for (int i=0; i<options.length; i++) {
				ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options[i] /*vlApresentacao*/, Integer.toString(i) /*vlReal*/,
						0 /*cdPessoa*/, 0 /*cdEmpresa*/));
			}
			
			return new Result(200);
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1);
		}
	}
	
	public Result saveOpcoesLimpoArrayList(int cdParametro, List<String> options) {
		try {
			this.limparOpcoesParametro(cdParametro);
			
			for (int i=0; i<options.size(); i++) {
				ParametroOpcaoDAO.insert(new ParametroOpcao(cdParametro, 0 /*cdOpcao*/, options.get(i) /*vlApresentacao*/, Integer.toString(i) /*vlReal*/,
						0 /*cdPessoa*/, 0 /*cdEmpresa*/));
			}
			
			return new Result(200);
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1);
		}
	}
	
	private boolean limparOpcoesParametro(int cdParametro) {
		Connection conn = Conexao.conectar();
		try {			 
			 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM grl_parametro_opcao WHERE CD_PARAMETRO = ?");
			 pstmt.setInt(1, cdParametro);
			 
			 pstmt.executeUpdate();
			 
			 return true;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return false;
		} finally {
			Conexao.desconectar(conn);
		}
	}

}
