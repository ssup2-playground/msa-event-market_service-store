package com.market.store.server.error;

public class ErrorCode {
  // Resource
  private static final String RESOURCE_STORE = "_STORE";
  private static final String RESOURCE_PRODUCT = "_PRODUCT";

  // Common
  public static final String BAD_REQUEST = "BAD_REQUEST";
  public static final String UNAUTHORIZED = "UNAUTHORIZED";
  public static final String INTERNAl_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

  // Resource not found
  public static final String NOT_FOUND = "NOT_FOUND";
  public static final String NOT_FOUND_STORE = NOT_FOUND + RESOURCE_STORE;
  public static final String NOT_FOUND_PRODUCT = NOT_FOUND + RESOURCE_PRODUCT;

  // Resource conflict
  public static final String CONFLICT = "CONFLICT";
  public static final String CONFLICT_STORE = CONFLICT + RESOURCE_STORE;
  public static final String CONFLICT_PRODUCT = CONFLICT + RESOURCE_PRODUCT;

  // Resource forbidden
  public static final String FORBIDDEN = "FORBIDDEN";
  public static final String FORBIDDEN_STORE = FORBIDDEN + RESOURCE_STORE;
  public static final String FORBIDDEN_PRODUCT = FORBIDDEN + RESOURCE_PRODUCT;
}
