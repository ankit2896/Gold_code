package com.freecharge.financial.constants;

public interface VaultConstants {

    String TOKEN_URI = "https://vault.freecharge.in/v1/auth/ldap/login/%s";
    String URI = "https://vault.freecharge.in/v1/databags/data/%s/digitalgold";
    String USERNAME = "vault_username";
    String PASS_WORD = "vault_password";
    String APP_PROPERTIES_PATH = "https://vault.freecharge.in/v1/databags/data/%s/digitalgold/app.properties";
    String REDIS_SCRET_KEY_PATH_BY_Jaswender = "https://vault.freecharge.in/ui/vault/secrets/secrets/show/qa/digitalgold/secrets.properties";
    String REDIS_SCRET_KEY_PATH = "https://vault.freecharge.in/v1/secrets/data/%s/digitalgold/secrets.properties";
    String DB_PROPERTIES_PATH = "https://vault.freecharge.in/v1/databags/data/%s/digitalgold/db.properties";
    String TOKEN_HEADER_KEY = "X-Vault-Token";
    String DB_CREDS_URL = "https://vault.freecharge.in/v1/database/creds/%s_digitalgold_%s_write";
    String DB_CREDS_URL_PROD = "https://vault.freecharge.in/v1/database/creds/%s_giftcards_write";
    String DB_USERNAME = "username";
    String DB_PASS_WORD = "password";
    String PROPERTY_NAME = "vault.properties";

}
