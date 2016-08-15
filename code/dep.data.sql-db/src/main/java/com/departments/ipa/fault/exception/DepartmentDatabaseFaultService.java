package com.departments.ipa.fault.exception;

/**
 * Created by david on 13/08/16.
 */
public class DepartmentDatabaseFaultService extends  Exception {

    private static final long serialVersionUID = 7752064527877553683L;

/*    public DepartmentDatabaseFaultService(){
        super();
    }

    public DepartmentDatabaseFaultService(String message){
        super(message);
    }*/

    public enum Code {
        /**
         * External service login failure (AuthException)
         */
        UNAUTHORISED(100,"Unauthorised (external service)"),
        /**
         * External service credentials not not specifined in Drupal (null/empty, AuthLookupException)
         */
        INVALID_CREDENTIALS(101,"No credentials for external service?"),
        /**
         * User credentials valid but not function access not allowed. Possible reasons include not being a member of a
         * customer group or user role prohibits use of given function.
         */
        ACCESS_DENIED(102,"Function access denied"),
        /**
         * Invalid arguments supplied to service (IllegalArgumentException)
         */
        INVALID_ARGUMENTS(200,"Invalid arguments passed to service"),
        /**
         * Request was valid but requested resource does not exist.
         */
        NOT_FOUND(204,"Resource not found"),
        /**
         * Operation not supported in context. This mainly applies to actions, i.e. retrieval functions are more likely to
         * return an empty result (or <code>null</code>) in the case where information is not available.
         */
        UNSUPPORTED(300,"Unsupported operation"),
        /**
         * Operation not possible at this time
         */
        UNAVAILABLE(301, "Operation currently unavailable"),
        /**
         * Invalid state of object subject to change
         */
        INVALID_STATE(302, "Operation not possible in current state"),
        /**
         * SIM not found in external source (either doesn't exist or credentials do not permit viewing thereof)
         */
        NOT_FOUND_EXTERNAL(303, "Object not found in external source"),
        /**
         * Internal errors (exception message will not be revealed to client)
         */
        INTERNAL(999, "Internal error");

        private final int code;
        private final String description;

        Code(int code, String description)
        {
            this.code = code;
            this.description = description;
        }

        public int getCode()
        {
            return code;
        }

        public String getDescription()
        {
            return description;
        }
    }

    private final int code;
    private final String detailMessage;

    private DepartmentDatabaseFaultService(final int code, final String message, final String detailMessage)
    {
        super(message);
        this.code = code;
        this.detailMessage = detailMessage;
    }

    private DepartmentDatabaseFaultService(int code, String message, final String detailMessage, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.detailMessage = detailMessage;
    }

    public int getCode()
    {
        return code;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    public String getOverviewMessage()
    {
        return super.getMessage();
    }

    @Override
    public String getMessage()
    {
        return "["+code+"] "+super.getMessage()+": "+detailMessage;
    }

    public static DepartmentDatabaseFaultService create(Code code, String message)
    {
        return create(code, message, null);
    }

    public static DepartmentDatabaseFaultService create(Code code, String message, Throwable cause)
    {
        if (cause != null)
            return new DepartmentDatabaseFaultService(code.getCode(), code.getDescription(), message, cause);
        return new DepartmentDatabaseFaultService(code.getCode(), code.getDescription(), message);
    }

}
