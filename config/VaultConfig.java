package com.freecharge.financial.config;

import com.freecharge.financial.constants.VaultConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration("vaultConfig")
@Profile({"qa", "staging", "prod"})
public class VaultConfig extends AbstractVaultConfiguration {

    @Value("${spring.profiles.active:qa}")
    private String env;
    private String token;
    private static final String username = System.getenv(VaultConstants.USERNAME);
    private static final String password = System.getenv(VaultConstants.PASS_WORD);

    @Override
    public VaultEndpoint vaultEndpoint() {
        try {
            final String vaultUri = String.format(VaultConstants.URI, env);
            VaultEndpoint endpoint = VaultEndpoint.from(new URI(vaultUri));
            log.info("VaultConfig env: " + env);
            return endpoint;
        } catch (URISyntaxException e) {
            log.error("Exception occurred due to invalid/incorrect VaultUri with following error\n" + e);
        }
        return null;
    }

    @SneakyThrows
    @Override
    public ClientAuthentication clientAuthentication() {
        return new TokenAuthentication(getToken());
    }

    private String getVaultToken() throws Throwable {
        String token = "";
        RestTemplate restTemplate = new RestTemplate();
        RetryTemplate retryTemplate = retryTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("password", password);
        try {
            log.info("Fetching Vault token");
            ResponseEntity<Object> responseEntity = retryTemplate.execute(new RetryCallback<ResponseEntity<Object>, Throwable>() {
                @Override
                public ResponseEntity<Object> doWithRetry(RetryContext retryContext) throws Throwable {
                   // System.out.println("Vault user : "+String.format(VaultConstants.TOKEN_URI, username));
                    //System.out.println("Vault Pasword : "+String.format(VaultConstants.TOKEN_URI, password));
                    return restTemplate.postForEntity(String.format(VaultConstants.TOKEN_URI, username), map, Object.class);
                }
            });
            log.info("Vault Api call status code: " + responseEntity.getStatusCode());
            Map<String, Object> responseEntityBody = (Map<String, Object>) responseEntity.getBody();
            Map<String, Object> authData = (Map<String, Object>) responseEntityBody.get("auth");
            token = (String) authData.get("client_token");
            log.info("Vault token: " + token);
        } catch (RestClientException e) {
            log.error("Exception occurred during Vault API call with error message" + e);
        }
        return token;
    }

    @SneakyThrows
    public String getToken() {
        return token == null ? getVaultToken() : token;
    }

    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(2000l);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        Map<Class<? extends Throwable>, Boolean> r = new HashMap<>();
        r.put(Exception.class, true);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3, r);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    @Bean
    @Primary
    public VaultTemplate vaultTemplate() {
        return new RetryableVaultTemplate(this.vaultEndpointProvider(), this.clientHttpRequestFactoryWrapper().getClientHttpRequestFactory(), this.sessionManager(), retryTemplate());
    }
}