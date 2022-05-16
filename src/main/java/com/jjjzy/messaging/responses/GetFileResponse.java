package com.jjjzy.messaging.responses;

import com.jjjzy.messaging.enums.Status;

import java.io.InputStream;

public class GetFileResponse extends BaseResponse{
    InputStream file;

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public GetFileResponse(Status status, InputStream file) {
        super(status);
        this.file = file;
    }
}
