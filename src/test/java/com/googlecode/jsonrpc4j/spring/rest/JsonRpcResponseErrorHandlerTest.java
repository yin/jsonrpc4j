package com.googlecode.jsonrpc4j.spring.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class JsonRpcResponseErrorHandlerTest {
    @Mock
    ClientHttpResponse response;

    JsonRpcResponseErrorHandler errorHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        errorHandler = new JsonRpcResponseErrorHandler();
    }

    @Test
    public void hasError_200OK() throws Exception {
        when(response.getStatusCode())
                .thenReturn(HttpStatus.ACCEPTED);
        assertFalse(errorHandler.hasError(response));
    }

    @Test
    public void hasError_404NOT_FOUND() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        when(response.getStatusCode())
                .thenReturn(HttpStatus.NOT_FOUND);
        when(response.getHeaders())
                .thenReturn(httpHeaders);
        assertTrue(errorHandler.hasError(response));
    }

    @Test
    public void hasError_403FORBIDDEN() throws Exception {
        when(response.getStatusCode())
                .thenReturn(HttpStatus.FORBIDDEN);
        assertTrue(errorHandler.hasError(response));
    }

    @Test
    public void getHttpStatusCode() throws Exception {
        when(response.getStatusCode())
                .thenReturn(HttpStatus.FORBIDDEN);
        assertEquals(HttpStatus.FORBIDDEN, errorHandler.getHttpStatusCode(response));
    }

    @Test
    public void getResponseBody() throws Exception {
        when(response.getBody())
                .thenReturn(new ByteArrayInputStream("Hello world!\n".getBytes()));
        assertArrayEquals("Hello world!\n".getBytes(), errorHandler.getResponseBody(response));
    }

    @Test
    public void getCharset() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        when(response.getHeaders())
                .thenReturn(httpHeaders);
        assertEquals(Charset.forName("UTF-8"), errorHandler.getCharset(response));
    }

}