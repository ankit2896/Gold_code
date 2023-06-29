package com.freecharge.financial.constants;

public class DBConstants {

     private DBConstants(){
          throw new IllegalStateException("DBConstants");
     }
     public static final String JDBC_URL = "spring.datasource.url";
     public static final String PASS_WORD = "spring.datasource.password";
     public static final String USERNAME = "spring.datasource.username";
     public static final String DRIVER_CLASSNAME = "spring.datasource.driver-class-name";
}
