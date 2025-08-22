package com.tivic.manager.mob.ait.imagemvideo.enums;

public enum TipoMidiaEnum {
    
    JPEG(new byte[]{(byte) 0xFF, (byte) 0xD8}, "image/jpeg"),
    MP4(new byte[]{0x00, 0x00, 0x00, 0x20, 0x66, 0x74, 0x79, 0x70}, "video/mp4"),
    MP4_ISO(new byte[]{0x00, 0x00, 0x00, 0x18, 0x66, 0x74, 0x79, 0x70}, "video/mp4-iso"),
    WEBP(new byte[]{'R', 'I', 'F', 'F'}, "image/webp");

    private final byte[] header;
    private final String mimeType;

    TipoMidiaEnum(byte[] header, String mimeType) {
        this.header = header;
        this.mimeType = mimeType;
    }

    public byte[] getHeader() {
        return header;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static String detectFileType(byte[] bytes) {
        for (TipoMidiaEnum type : values()) {
            if (matches(bytes, type.getHeader())) {
                return type.getMimeType();
            }
        }
        return "unknown";
    }

    private static boolean matches(byte[] bytes, byte[] header) {
        if (bytes.length < header.length) {
            return false;
        }
        for (int i = 0; i < header.length; i++) {
            if (bytes[i] != header[i]) {
                return false;
            }
        }
        return true;
    }
}
