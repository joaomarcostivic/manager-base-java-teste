package com.tivic.manager.adapter.base.antiga.parametro;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.orgao.enums.OrgaoAutuadorParametroEnum;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class ParametroOldRepositoryDAO implements IParametroRepository {

	private IAdapterService<ParametroOld<?>, Parametro> adapterService;

	public ParametroOldRepositoryDAO() throws Exception {
		this.adapterService = new AdapterParametroService();
	}

	@Override
	public void insert(Parametro parametro, CustomConnection customConnection) throws Exception {
		ParametroOld<?> parametroOld = this.adapterService.toBaseAntiga(parametro);
		int codRetorno = new ParametroDAO().insert(parametroOld, customConnection);
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao criar parâmetro.");
	}

	@Override
	public void update(Parametro parametro, CustomConnection customConnection) throws Exception {
		ParametroOld<?> parametroOld = this.adapterService.toBaseAntiga(parametro);
		int codRetorno = new ParametroDAO().update(parametroOld, customConnection);
		if (codRetorno <= 0)
			throw new ValidacaoException("Erro ao criar parâmetro.");
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
	public String getValorOfParametroAsString(String nmParametro, CustomConnection customConnection) throws Exception {
		return new ParametroDAO().getValorOfParametroAsString(nmParametro, customConnection);
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
		return new ParametroDAO().getValorOfParametroAsInt(nmParametro, customConnection);
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
		InputStream is = new ByteArrayInputStream(img);
		BufferedImage bi = ImageIO.read(is);
		return bi;
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
		return new ParametroDAO().getValorOfParametroAsByte(nmParametro, customConnection);
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
		ParametroOld<String> parametroOld = new ParametroDAO().getByName(nmParametro, customConnection);
		Parametro parametro = this.adapterService.toBaseNova(parametroOld);
		return parametro;
	}

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
		return new ParametroDAO().getValorOfParametroAsBoolean(nmParametro, customConnection);
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

	@Override
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
	public Parametro getByName(String name, CustomConnection customConnection) throws Exception {
		ParametroOld<String> parametroOld = new ParametroDAO().getByName(name, customConnection);
		Parametro parametro = this.adapterService.toBaseNova(parametroOld);
		return parametro;
	}
	
	@Override
	public String getValorAsStringWithCustomDb(String nmParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);
			String vlParametro = getValorOfParametroAsString(nmParametro, customConnection);
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
			int vlParametro = getValorOfParametroAsInt(OrgaoAutuadorParametroEnum.OLD.getValue(), customConnection);
			customConnection.finishConnection();
			return vlParametro;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public int getOrgaoAutuanteParamValue(CustomConnection customConnection) throws Exception {
		return new ParametroDAO().getValorOfParametroAsInt(OrgaoAutuadorParametroEnum.OLD.getValue(), customConnection);
	}
}
