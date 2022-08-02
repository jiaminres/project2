package com.jiamin.NioServer.pojo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MyHttpInputMessage implements HttpInputMessage {
    private InputStream inputStream;
    public MyHttpInputMessage(String str){
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        inputStream = new ByteArrayInputStream(bytes);
    }
    public MyHttpInputMessage(byte[] bytes){
        inputStream = new ByteArrayInputStream(bytes);
    }
    @Override
    public InputStream getBody() throws IOException {
        return inputStream;
    }

    @Override
    public HttpHeaders getHeaders() {
        return null;
    }
}
