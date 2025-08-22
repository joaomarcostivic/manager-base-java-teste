package com.tivic.manager.grl;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

public class ArquivoTipoRegistroServices	{
	
	public static final String prefixCnab240R = "cnab240R_";
	public static final String prefixCnab240T = "cnab240T_";
	public static final String prefixCnab400T = "cnab400T_";
	
	public static String[][] cnab240T = {{"02", "Entrada Confirmada"},
										 {"03", "Entrada Rejeitada"},
										 {"06", "Liquidação"},
										 {"07", "Confirmação do Recebimento da Instrução de Desconto"},
										 {"08", "Confirmação do Recebimento do Cancelamento da Instrução de Desconto"},
										 {"09", "Baixa"},
										 {"10", "Confirmação do Recebimento da Instrução de Repactuação"},
										 {"12", "Confirmação do Recebimento da Instrução de Abatimento"},
										 {"13", "Confirmação do Recebimento do Cancelamento da Instrução de Abatimento"},
										 {"14", "Confirmação do Recebimento da Instrução de Alteração de Vencimento"},
										 {"17", "Liquidação após Baixa ou Liquidação Título não Registrado"},
										 {"26", "Instrução Rejeitada"},
										 {"27", "Confirmação do Pedido de Alteração de Outros Dados"},
										 {"30", "Alteração de Dados Rejeitada"},
										 {"36", "Concentração (Será informado apenas no arquivo retorno dos dados do Comprador)"},
										 {"37", "Títulos debitados a Empresa após o término da carência"},
										 {"38", "Títulos pagos em atraso creditados a Empresa"}};
	public static final String[][] cnab240R = {{"01","CNAB 240 - Entrada de Títulos"},
											   {"02","CNAB 240 - Pedido de Baixa"},
											   {"04","CNAB 240 - Concessão de Abatimento"},
											   {"05","CNAB 240 - Cancelamento de Abatimento"},
											   {"06","CNAB 240 - Alteração de Vencimento"},
											   {"07","CNAB 240 - Concessão de Desconto"},
											   {"08","CNAB 240 - Cancelamento de Desconto"},
											   {"12","CNAB 240 - Confirmação de Repactuação"},
											   {"31","CNAB 240 - Alteração de Outros Dados"},
											   {"41","CNAB 240 - Alteração de Dados do Comprador"},
											   {"42","CNAB 240 - Alteração de Dados do Título"}};
	public static String[][] cnab400T = {{"02", "Entrada Confirmada"},
		 {"03", "Entrada Rejeitada"},
		 {"06", "Liquidação"},
		 {"09", "Baixado Automat. via Arquivo"},
		 {"10", "Baixado conforme instruções da Agência"},
		 {"11", "Em Ser - Arquivo de Títulos pendentes (sem motivo)"},
		 {"12", "Abatimento Concedido (sem motivo)"},
		 {"13", "Abatimento Cancelado (sem motivo)"},
		 {"14", "Vencimento Alterado (sem motivo)"},
		 {"15", "Liquidação em Cartório (sem motivo)"},
		 {"16", "Título Pago em Cheque  Vinculado"},
		 {"17", "Liquidação após baixa ou Título não registrado"},
		 {"18", "Acerto de Depositária"},
		 {"19", "Confirmação Receb. Inst. de Protesto"},
		 {"20", "Confirmação Recebimento Instrução Sustação de Protesto"},
		 {"21", "Acerto do Controle do Participante"}, 
		 {"22", "Título Com Pagamento Cancelado"}, 
		 {"23", "Entrada do Título em Cartório"}, 
		 {"24", "Entrada rejeitada por CEP Irregular"}, 
		 {"27", "Baixa Rejeitada"}, 
		 {"28", "Débito de tarifas/custas"}, 
		 {"30", "Alteração de Outros Dados Rejeitados"}, 
		 {"32", "Instrução Rejeitada"}, 
		 {"33", "Confirmação Pedido Alteração Outros Dados"}, 
		 {"34", "Retirado de Cartório e Manutenção Carteira"}, 
		 {"35", "Desagendamento do débito automático"}, 
		 {"40", "Estorno de pagamento"}, 
		 {"55", "Sustado judicial"}, 
		 {"68", "Acerto dos dados do rateio de Crédito"}, 
		 {"69", "Cancelamento dos dados do rateio"}};

	public static ArquivoTipoRegistro getTipoRegistroById(String prefix, String idRegistro, String[][] idList, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement(
					"SELECT * " +
					"FROM grl_arquivo_tipo_registro A " +
					"WHERE id_tipo_registro = \'"+prefix+idRegistro+"\'").executeQuery();
			if(rs.next()) 
				return new ArquivoTipoRegistro(rs.getInt("cd_tipo_registro"),
												rs.getString("nm_tipo_registro"),
												rs.getString("id_tipo_arquivo"));
			else	{
				String nmTipoRegistro = "Tipo de registro desconhecido"; 			
				for(int i=0; i<idList.length; i++)
					if(idList[i][0].equals(idRegistro))
						nmTipoRegistro = idList[i][1];

				ArquivoTipoRegistro tipo = new ArquivoTipoRegistro(0, prefix+idRegistro, nmTipoRegistro);
				tipo.setCdTipoRegistro(ArquivoTipoRegistroDAO.insert(tipo, connect));
				return tipo;
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoTipoRegistroServices.getTipoRegistroById: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}