package com.controlcenter.homerestipa.cash.control.response;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Created by david on 14/11/16.
 */
public class RestResponseHandler {

    private static final String HEADER_EXPIRE_DATE = "Fri, 01 Jan 1990 00:00:00 GMT";

    public static Response success(final Object response) {
        return Response.ok(response, MediaType.APPLICATION_JSON)
                .cacheControl(getCacheControl())
                .header("Pragma", "no-cache")
                .header(HttpHeaders.EXPIRES, HEADER_EXPIRE_DATE)
                .build();
    }

    public static Response success() {
        return Response.ok()
                .cacheControl(getCacheControl())
                .header("Pragma", "no-cache")
                .header(HttpHeaders.EXPIRES, HEADER_EXPIRE_DATE)
                .build();
    }

    public static Response error(final Object response) {

        return Response.ok(response, MediaType.APPLICATION_JSON)
                .cacheControl(getCacheControl())
                .header("Pragma", "no-cache")
                .header(HttpHeaders.EXPIRES, HEADER_EXPIRE_DATE).build();
    }

    public  static Response badRequest(String message) {
        return error(Status.BAD_REQUEST, message);
    }

    public static Response internalServerError(String message) {
        return error(Status.INTERNAL_SERVER_ERROR, message);
    }

    public static  Response forbidden(String message) {
        return error(Status.FORBIDDEN, message);
    }


    private static CacheControl getCacheControl() {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoTransform(false);
        return cacheControl;
    }


    public static Response error(Status status, String message) {
        String msg = "";
        if ( message == null ) {
            msg = "{\"message\":null}";
        } else {
            msg = "{\"message\":\""+message+"\"}";
        }
        return Response.status(status).entity(msg).type(MediaType.APPLICATION_JSON).build();
    }
}
