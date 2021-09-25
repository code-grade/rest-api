package com.codegrade.restapi.utils;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class RBuilder {

    private Object data;
    private String message;
    private String token;
    private HttpStatus status;

    public static RBuilder success() {
        return new RBuilder("Success", HttpStatus.OK);
    }

    public static RBuilder success(String msg) {
        return new RBuilder(msg, HttpStatus.OK);
    }

    public static RBuilder badRequest(String msg) {
        return new RBuilder("Bad request", HttpStatus.BAD_REQUEST).setMsg(msg);
    }

    public static RBuilder badRequest() {
        return new RBuilder("Bad request", HttpStatus.BAD_REQUEST);
    }

    public static RBuilder unauthorized(String msg) {
        return new RBuilder("Unauthorized", HttpStatus.UNAUTHORIZED).setMsg(msg);
    }

    public static RBuilder unauthorized() {
        return new RBuilder("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    public static RBuilder notFound(String msg) {
        return new RBuilder("Not Found", HttpStatus.NOT_FOUND).setMsg(msg);
    }

    public static RBuilder notFound() {
        return new RBuilder("Not Found", HttpStatus.NOT_FOUND);
    }

    public static RBuilder error() {
        return new RBuilder("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private RBuilder(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public RBuilder setData(String key, Object value) {
        this.data = ImmutableMap.of(key, value);
        return this;
    }

    public RBuilder setData(Object obj) {
        this.data = obj;
        return this;
    }

    public RBuilder setMsg(String message) {
        this.message = message;
        return this;
    }

    public RBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public RBuilder setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public Map<String, Object> compact() {
        Map<String, Object> jsonRes = new HashMap<>();
        jsonRes.put(
                "timestamp", ZonedDateTime.now(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss"))
        );
        jsonRes.put("message", this.message);
        if (this.data != null) jsonRes.put("data", this.data);
        if (this.token != null) jsonRes.put("token", this.token);
        return jsonRes;
    }

    public ResponseEntity<Map<String, Object>> compactResponse() {
        return new ResponseEntity<>(this.compact(), this.status);
    }
}
