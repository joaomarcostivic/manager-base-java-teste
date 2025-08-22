package com.tivic.manager.grl.parametro.repository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroDAO;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.mob.orgao.enums.OrgaoAutuadorParametroEnum;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ParametroRepositoryDAO implements IParametroRepository {

	@Override
	public void insert(Parametro parametro, CustomConnection customConnection) throws ValidacaoException {
		int codRetorno = ParametroDAO.insert(parametro, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao criar parâmetro.");
	}

	@Override
	public void update(Parametro parametro, CustomConnection customConnection) throws ValidacaoException {
		int codRetorno = ParametroDAO.update(parametro, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao atualizar o parâmetro.");
	}

	@Override
	public String getValorOfParametroAsString(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			String vlParametro = getValorOfParametroAsString(nmParametro, customConnection);
			customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public String getValorOfParametroAsString(String nmParametro, CustomConnection customConnection)
			throws SQLException, ValidacaoException {
		return ParametroDAO.getValorOfParametroAsString(nmParametro, customConnection);
	}

	@Override
	public int getValorOfParametroAsInt(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			int vlParametro = getValorOfParametroAsInt(nmParametro, customConnection);
			customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public int getValorOfParametroAsInt(String nmParametro, CustomConnection customConnection) throws Exception {
		return ParametroDAO.getValorOfParametroAsInt(nmParametro, customConnection);
	}

	@Override
	public byte[] getValorOfParametroAsByte(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] vlParametro = getValorOfParametroAsByte(nmParametro, customConnection);
			customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public BufferedImage byteasImgBuffer(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			BufferedImage brfImage = byteasImgBuffer(nmParametro, customConnection);
			customConnection.finishConnection();
			return brfImage;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public BufferedImage byteasImgBuffer(String nmParametro, CustomConnection customConnection) throws Exception {
		byte[] img = getValorOfParametroAsByte(nmParametro, customConnection);
		String strImg = convertByteArray(img);
		img = strImg.getBytes();
		InputStream is = new ByteArrayInputStream(img);
		BufferedImage bi = ImageIO.read(is);
		return bi;
	}

	@Override
	public String recImageToString(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			String imgStr = recImageToString(nmParametro, customConnection);
			customConnection.finishConnection();
			return imgStr;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] getValorOfParametroImageAsBytes(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] imgByte = Base64.getDecoder().decode(recImageToString(nmParametro, customConnection));
			customConnection.finishConnection();
			return imgByte;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public String recImageToString(String nmParametro, CustomConnection customConnection) throws Exception {
		List<String> imgs = new ArrayList<String>();
		byte[] imgByte = getValorOfParametroAsByte(nmParametro, customConnection);
		imgs.add(convertByteArray(imgByte));
		String imgC = "";
		for (String img : imgs) {
			if (img != "" && img != null) {
				imgC = img;
			}
		}
		return imgC.replace("data:image/png;base64,", "");
	}

	@Override
	public byte[] getValorOfParametroAsByte(String nmParametro, CustomConnection customConnection) throws Exception {
		return ParametroDAO.getValorOfParametroAsByte(nmParametro, customConnection);
	}

	private String convertByteArray(byte[] img) {
		if (img != null) {
			String data = new String(img);
			if (data.contains("data:image/png;base64,"))
				return data;
			String conv = "data:image/png;base64," + new String(img);
			return conv;
		}
		return null;
	}

	@Override
	public Parametro getParametroByName(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Parametro parametro = getParametroByName(nmParametro, customConnection);
			customConnection.finishConnection();
			return parametro;
		} finally {
			customConnection.closeConnection();
		}
	}


	@Override
	public Parametro getParametroByName(String nmParametro, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_parametro", nmParametro);
		Search<Parametro> search = new SearchBuilder<Parametro>("grl_parametro")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();

		if (search.getList(Parametro.class).isEmpty()) {
			throw new Exception("Nenhum parâmetro encontrado com esse nome.");
		}

		return search.getList(Parametro.class).get(0);
	}
	
	@Override
	public boolean getValorOfParametroAsBoolean(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			boolean result = getValorOfParametroAsBoolean(nmParametro, customConnection);
			customConnection.finishConnection();
			return result;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public boolean getValorOfParametroAsBoolean(String nmParametro, CustomConnection customConnection)
			throws Exception {
		return ParametroDAO.getValorOfParametroAsBoolean(nmParametro, customConnection);
	}

	@Override
	public int getValorAsIntWithCustomDb(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			int vlParametro = getValorOfParametroAsInt(nmParametro, customConnection);
			customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}

	public Parametro getByName(String name) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Parametro parametro = getByName(name, customConnection);
			customConnection.finishConnection();
			return parametro;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Parametro getByName(String name, CustomConnection customConnection) {
		Parametro parametro = ParametroDAO.getByName(name, customConnection.getConnection());
		return parametro;
	}

	@Override
	public String getValorAsStringWithCustomDb(String nmParametro) throws Exception {
		String vlParametro = null;
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("A.nm_parametro", nmParametro);
			Search<ParametroValor> search = new SearchBuilder<ParametroValor>("grl_parametro A")
					.fields("A.cd_parametro, A.nm_parametro, B.vl_inicial")
					.addJoinTable("LEFT OUTER JOIN grl_parametro_valor B ON (B.cd_parametro = A.cd_parametro)")
					.searchCriterios(searchCriterios)
					.customConnection(customConnection)
					.build();
			
			if(!search.getList(ParametroValor.class).isEmpty()) {
				vlParametro = search.getList(ParametroValor.class).get(0).getVlInicial();
			}
			
			customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public int getOrgaoAutuanteParamValue() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			int vlParametro = getValorOfParametroAsInt(OrgaoAutuadorParametroEnum.NEW.getValue(), customConnection);
			customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public int getOrgaoAutuanteParamValue(CustomConnection customConnection) throws Exception {
		return ParametroDAO.getValorOfParametroAsInt(OrgaoAutuadorParametroEnum.NEW.getValue(), customConnection);
	}
}
