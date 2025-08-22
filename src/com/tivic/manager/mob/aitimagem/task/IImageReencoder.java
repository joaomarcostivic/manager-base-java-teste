package com.tivic.manager.mob.aitimagem.task;

public interface IImageReencoder {
	byte[] reencodeToJpg(byte[] imageBytes) throws Exception;
}