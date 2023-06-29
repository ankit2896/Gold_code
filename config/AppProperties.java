package com.freecharge.financial.config;

import com.freecharge.financial.constants.DBConstants;
import com.freecharge.financial.constants.VaultConstants;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.freecharge.financial.constants.VaultConstants.DB_CREDS_URL_PROD;

@Component
@Slf4j
@DependsOn("vaultConfig")
public class AppProperties {

    @Autowired
    VaultOperations vaultOperations;

    @Autowired
    ConfigurableEnvironment configurableEnvironment;

    @Autowired
    VaultConfig vaultConfig;

    @Value("${spring.profiles.active:qa}")
    private String env;

    @Value("${propeties.loging.enable}")
    private boolean isProptiesFileAttributeDisplay;

    Map<String, Object> data = new HashMap<>();


    @PostConstruct
    public void init() {
        getPropertiesFromVault(VaultConstants.APP_PROPERTIES_PATH);
        getPropertiesFromVault(VaultConstants.DB_PROPERTIES_PATH);
        getPropertiesFromVault(VaultConstants.REDIS_SCRET_KEY_PATH);
        generateDbCredentials();
        configurableEnvironment.getPropertySources().addFirst(new MapPropertySource(VaultConstants.PROPERTY_NAME, data));

        if (isProptiesFileAttributeDisplay)
            log.info("vault properties : " + data);

        data.forEach((k, v) -> System.setProperty(k.trim(), v.toString().trim()));
    }

    @SuppressWarnings(value = "unchecked")
    private void getPropertiesFromVault(String path) {
        try {
            log.info("Environment name : "+env);
            String propertiesPath = String.format(path, env);
            log.info("Properties path : "+propertiesPath);
            VaultResponseSupport<Object> vaultResponseSupport = vaultOperations.read(propertiesPath, Object.class);
            Map<?, ?> vaultResponseSupportData = (Map<?, ?>) vaultResponseSupport.getData();
            data.putAll((Map<String, Object>) vaultResponseSupportData.get("data"));
        } catch (Exception e) {
            log.error("Error occur while reading properties from vault -{}", e);
            e.printStackTrace();
        }
    }

    public String getProperty(String propName) {
        if (data.containsKey(propName))
            return data.get(propName).toString().trim();
        else
            return "";
    }

    public void generateDbCredentials() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set(VaultConstants.TOKEN_HEADER_KEY, vaultConfig.getToken());
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            RestTemplate restTemplate = new RestTemplate();
            log.info("Environment name : "+env.toString());
            String dbCredUrl = String.format("prod".equals(env) ? DB_CREDS_URL_PROD : VaultConstants.DB_CREDS_URL, env, env);
            ResponseEntity<String> result = restTemplate.exchange(dbCredUrl, HttpMethod.GET, entity, String.class);
            if (result.getStatusCode() == HttpStatus.OK) {
                String response = result.getBody();
                JSONObject json = new JSONObject(response);
                json = json.getJSONObject("data");
                data.put(DBConstants.USERNAME, json.getString(VaultConstants.DB_USERNAME));
                log.info("DB Username: " + json.getString(VaultConstants.DB_USERNAME));
                System.setProperty(DBConstants.USERNAME, json.getString(VaultConstants.DB_USERNAME));
                data.put(DBConstants.PASS_WORD, json.getString(VaultConstants.DB_PASS_WORD));
                System.setProperty(DBConstants.PASS_WORD, json.getString(VaultConstants.DB_PASS_WORD));
                //log.info("DB Passw0rd: " + json.getString(VaultConstants.DB_PASS_WORD));
            } else {
                log.error("Error occurred while generating DB credentials from vault");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error("Error in generateDbCredentials: " + e);
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("Error in generateDbCredentials " + e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in generateDbCredentials:- " + e);
        }
    }

}
