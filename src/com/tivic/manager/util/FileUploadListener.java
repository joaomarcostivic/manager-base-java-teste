package com.tivic.manager.util;

import com.missiondata.fileupload.OutputStreamListener;

public class FileUploadListener implements OutputStreamListener {
	
    private FileUploadStats fileUploadStats = new FileUploadStats();

    public FileUploadListener(long totalSize) {
        fileUploadStats.setTotalSize(totalSize);
    }

    public void start() {
        fileUploadStats.setCurrentStatus(FileUploadStats.START);
    }

    public void bytesRead(int byteCount) {
        fileUploadStats.incrementBytesRead(byteCount);
        fileUploadStats.setCurrentStatus(FileUploadStats.READING);
    }

    public void error(String s) {
        fileUploadStats.setCurrentStatus(FileUploadStats.ERROR);
    }

    public void done() {
        fileUploadStats.setBytesRead(fileUploadStats.getTotalSize());
        fileUploadStats.setCurrentStatus(FileUploadStats.DONE);
    }

    public FileUploadStats getFileUploadStats() {
        return fileUploadStats;
    }

    public static class FileUploadStats {
        
    	public static final int START = 0;
    	public static final int NONE = 1;
    	public static final int READING = 2;
    	public static final int ERROR = 3;
    	public static final int DONE = 4;
    	
        private long totalSize = 0;
        private long bytesRead = 0;
        private double percentComplete = 0.0;
        private long startTime = System.currentTimeMillis();
        private int currentStatus = NONE;

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public long getBytesRead() {
            return bytesRead;
        }

        public long getElapsedTimeInMilliseconds() {
            return (System.currentTimeMillis() - startTime);
        }

        public int getCurrentStatus() {
            return currentStatus;
        }
        
        public double getPercentComplete() {
            if ( totalSize != 0 ) {
                percentComplete = (double)bytesRead/(double)totalSize;
            }
            return percentComplete;
        }
        public void setCurrentStatus(int currentStatus) {
            this.currentStatus = currentStatus;
        }

        public void setBytesRead(long bytesRead) {
            this.bytesRead = bytesRead;
        }

        public void incrementBytesRead(int byteCount) {
            this.bytesRead += byteCount;
        }
    }
}
