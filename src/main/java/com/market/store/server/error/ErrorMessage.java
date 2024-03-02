package com.market.store.server.error;

public class ErrorMessage {
  // Resource
  private static final String RESOURCE_STORE = "Store ";
  private static final String RESOURCE_PRODUCT = "Product ";

  // Common
  public static final String BAD_REQUEST = "Bad Request";
  public static final String UNAUTHORIZED = "Unauthorized";
  public static final String INTERNAl_SERVER_ERROR = "Internal server error";

  // Resource not found
  public static final String NOT_FOUND = "Not found";
  public static final String NOT_FOUND_STORE = RESOURCE_STORE + NOT_FOUND;
  public static final String NOT_FOUND_PRODUCT = RESOURCE_PRODUCT + NOT_FOUND;

  // Resource conflict
  public static final String CONFLICT = "Conflict";
  public static final String CONFLICT_STORE = RESOURCE_STORE + CONFLICT;
  public static final String CONFLICT_PRODUCT = RESOURCE_PRODUCT + CONFLICT;

  // Resource forbidden
  public static final String FORBIDDEN = "Forbidden";
  public static final String FORBIDDEN_STORE = RESOURCE_STORE + FORBIDDEN;
  public static final String FORBIDDEN_PRODUCT = RESOURCE_PRODUCT + FORBIDDEN;
}
