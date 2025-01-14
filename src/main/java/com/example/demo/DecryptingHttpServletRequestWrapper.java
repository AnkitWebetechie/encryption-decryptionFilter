package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class DecryptingHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String decryptedBody;

    public DecryptingHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String getEncryptedBody() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = super.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    public void setDecryptedBody(String body) {
        this.decryptedBody = body;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new StringReader(decryptedBody));
    }
}
