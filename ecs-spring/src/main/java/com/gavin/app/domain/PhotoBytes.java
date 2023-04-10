package com.gavin.app.domain;

import java.io.Serializable;

/**
 * @author QiangZhi
 * @date 2023/3/5 13:11
 */
public class PhotoBytes implements Serializable {
    private static final long serialVersionUID = -8775612234303127935L;
    /**二进制图片*/
    private byte[] photoBytes;
    public byte[] getPhotoBytes() {
        return photoBytes;
    }
    public void setPhotoBytes(byte[] photoBytes) {
        this.photoBytes = photoBytes;
    }
}