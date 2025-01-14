package com.example.demo.filter;

import com.example.demo.config.AESUtil;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Component
@WebFilter(dispatcherTypes = DispatcherType.REQUEST)
public class CustomRequestResponseFilter extends OncePerRequestFilter {

    private static final String staticKey = "ankitkumarajavat"; // Replace with your key
    private static final String AES_IV = "1234567890abcdef";         // Replace with your IV
    private static final String AES_KEY = "ankitkumarajavat";
    Logger logger = LoggerFactory.getLogger(CustomRequestResponseFilter.class);

    private String getRequestBody(ContentCachingRequestWrapper requestWrapper) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(requestWrapper.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        String request = stringBuilder.toString();
        request = request.replace("{\"request\":\"", "");
        request = request.replace("\"}", "");
        return request;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String dynamicKey = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        logger.info("dynamic key : {}", dynamicKey);

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);


        try {
            String encryptedBody = getRequestBody(wrappedRequest);

            if (!encryptedBody.isEmpty()) {
                // Decrypt the body


                String decryptData = AESUtil.decryptData(encryptedBody, staticKey);
                request.setAttribute("encryptedData", decryptData);

                logger.info("Data to controller" + decryptData);
                String encryptprivatekey=AESUtil.encryptData(dynamicKey,staticKey);
                // Replace the request body with the decrypted content
                DecryptedHttpServletRequest decryptedRequest =
                        new DecryptedHttpServletRequest(wrappedRequest, decryptData);

                responseWrapper.setHeader("X-VERIFY",encryptprivatekey);
                filterChain.doFilter(decryptedRequest, responseWrapper);
            }
            else {
                String encryptprivatekey=AESUtil.encryptData(dynamicKey,staticKey);
                filterChain.doFilter(wrappedRequest, responseWrapper);
            }
         }
        catch (Exception e) {
            throw new ServletException("Error during decryption", e);
            }

             logger.info("Response status: " + responseWrapper.getStatus());


        // Read the original response body
        byte[] responseBodyBytes = responseWrapper.getContentAsByteArray();
        String originalResponseBody = new String(responseBodyBytes, responseWrapper.getCharacterEncoding());

        // Check if the response body is empty
        if (!originalResponseBody.isBlank()) {


            String modifiedResponseBody = null;

            try {
                modifiedResponseBody = AESUtil.encryptData(originalResponseBody,dynamicKey);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Write the modified response back to the output stream
            responseWrapper.resetBuffer(); // Clear the original buffer
            responseWrapper.getWriter().write(modifiedResponseBody);
        }

           // Copy headers and commit the response
            responseWrapper.copyBodyToResponse();
    }

}
