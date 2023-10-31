package com.paulorodrigues.authentication.util;

public class ConstantsUtil {

    public static final int MAX_SIZE_NAME = 100;
    public static final int MAX_SIZE_SHORT_TEXT = 200;
    public static final int MAX_SIZE_LONG_TEXT = 600;

    public static final int MAX_SIZE_ADDRESS_NUMBER = 9;
    public static final int MAX_SIZE_ADDRESS_CEP = 8;
    public static final int MAX_SIZE_ADDRESS_ZIPCODE = 12;
    public static final int MAX_SIZE_ADDRESS_COORDINATION = 20;

    public static final int MAX_SIZE_CNPJ = 14;
    public static final int MAX_SIZE_CPF = 11;
    public static final String SAMPLE_V1_BASE_API = "/api/v1/samples";

    public static final String FIND_ALL_PATH = "/all";
    public static final String FIND_PAGEABLE_PATH = "/fetch";
    public static final String FIND_BY_ID_PATH = "/{id}";
    public static final String FIND_BY_NAME_PATH = "/fetch/{name}";
    public static final String UPDATE_PATH = "/{id}";
    public static final String DELETE_PATH = "/{id}";
}
