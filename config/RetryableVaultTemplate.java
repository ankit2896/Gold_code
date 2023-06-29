package com.freecharge.financial.config;

import lombok.SneakyThrows;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.vault.authentication.SessionManager;
import org.springframework.vault.client.VaultEndpointProvider;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultResponseSupport;


public class RetryableVaultTemplate extends VaultTemplate {

    private RetryTemplate retryTemplate;

    public RetryableVaultTemplate(VaultEndpointProvider endpointProvider,
                                  ClientHttpRequestFactory clientHttpRequestFactory,
                                  SessionManager sessionManager, RetryTemplate retryTemplate) {
        super(endpointProvider, clientHttpRequestFactory, sessionManager);
        this.retryTemplate = retryTemplate;
    }

    @SneakyThrows
    @Override
    public VaultResponse read(final String path) {
        return retryTemplate
                .execute(new RetryCallback<VaultResponse, Throwable>() {
                    @Override
                    public VaultResponse doWithRetry(RetryContext context) {
                        return RetryableVaultTemplate.super.read(path);
                    }
                });
    }

    @Override
    public <T> VaultResponseSupport<T> read(final String path, final Class<T> responseType) {
        return retryTemplate
                .execute(new RetryCallback<VaultResponseSupport<T>, RuntimeException>() {
                    @Override
                    public VaultResponseSupport<T> doWithRetry(RetryContext context) {
                        return RetryableVaultTemplate.super.read(path, responseType);
                    }
                });
    }
}
