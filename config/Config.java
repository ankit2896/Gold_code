package com.freecharge.financial.config;

import com.freecharge.intg.service.SafegoldService;
import com.freecharge.intg.service.SafegoldServiceImpl;
import com.freecharge.kyc.client.IKycManagementClient;
import com.freecharge.kyc.client.KycManagementClientImpl;
import com.freecharge.redirector.client.IDigitalGoldServiceClient;
import com.freecharge.redirector.client.impl.DigitalGoldServiceClientImpl;
import com.snapdeal.fcpt.txnhistoryview.client.ITxnHistoryViewClient;
import com.snapdeal.fcpt.txnhistoryview.client.impl.TxnHistoryViewClientImpl;
import com.snapdeal.payments.sdmoney.client.BankDetailsStoreClient;
import com.snapdeal.payments.sdmoney.client.SDMoneyClient;
import com.snapdeal.payments.sdmoney.client.utils.ClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class Config {

    @Value("${mail.sender.username}")
    private String mailSenderUserName;

    @Value("${mail.sender.password}")
    private String mailSenderPassword;

    @Value("${safegold.client.socketTimeout}")
    private Integer socketTimeout;

    @Value("${safegold.client.connectionTimeout}")
    private Integer connectionTimeout;

    @Value("${safegold.client.maxTotalConnections}")
    private Integer maxTotalConnections;

    @Value("${safegold.client.defaultMaxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;

    @Value("${safegold.client.staleCheckingEnabled}")
    private Boolean staleCheckingEnabled;

    @Value("${fc.thv.clientkey}")
    private String fc_thv_client_key;

    @Value("${fc.thv.clientid}")
    private String fc_thv_client_id;

    @Value("${fc.thv.url}")
    private String fc_thv_url;

    @Value("${fc.thv.port}")
    private String fc_thv_port;

    @Value("${fc.thv.env}")
    private String fc_thv_env;

    @Value("${fc.thv.timeout}")
    private int fc_thv_timeout;


    @Value("${fc.sdmoney.clientname}")
    private String fc_sdmoney_client_name;

    @Value("${fc.sdmoney.clientkey}")
    private String fc_sdmoney_client_key;

    @Value("${fc.sdmoney.url}")
    private String fc_sdmoney_url;

    @Value("${fc.sdmoney.timeout}")
    private int fc_sdmoney_timeout;

    @Value("${mf.digitalgold.clientname}")
    private String mfClientName;

    @Value("${mf.digitalgold.clientkey}")
    private String mfClientKey;

    @Value("${mf.digitalgold.clientid}")
    private String mfClientId;

    @Value("${mf.digitalgold.url}")
    private String mfClientUrl;

    @Value("${mf.digitalgold.timeout}")
    private int mfClienTimeout;

    @Value("${mf.digitalgold.port}")
    private String mfClienPort;

    @Value("${fc.kyc.clientKey}")
    private String kycClientKey;

    @Value("${fc.kyc.url}")
    private String kycUrl;

    @Value("${fc.kyc.port}")
    private String kycPort;

    @Value("${fc.kyc.clientId}")
    private String kycClientId;

    @Value("${fc.kyc.api.timeout}")
    private String kycApiTimeout;

    @Bean
    public SafegoldService safegoldService() {
        return new SafegoldServiceImpl(socketTimeout, connectionTimeout, maxTotalConnections, defaultMaxConnectionsPerHost, staleCheckingEnabled);
    }

    @Bean
    public ITxnHistoryViewClient getTHVService() throws Exception {
        com.snapdeal.fcpt.txnhistoryview.util.ClientDetails.init(fc_thv_url, fc_thv_port, fc_thv_client_key, fc_thv_client_id, fc_thv_timeout, com.snapdeal.fcpt.txnhistoryview.commons.enums.EnvironmentEnum.valueOf(fc_thv_env));
        return new TxnHistoryViewClientImpl();
    }


    private ClientDetails getWalletClientDetails() {
        ClientDetails clientDetails = new ClientDetails();
        clientDetails.setConnectTimeout(fc_sdmoney_timeout);
        clientDetails.setConnectionRequestTimeout(fc_sdmoney_timeout);
        clientDetails.setSocketTimeout(-1);
        clientDetails.setUrl(fc_sdmoney_url);
        clientDetails.setClientName(fc_sdmoney_client_name);
        clientDetails.setClientKey(fc_sdmoney_client_key);
        return clientDetails;
    }

    @Bean
    public BankDetailsStoreClient getBankDetailsStoreClient() throws Exception {
        return new BankDetailsStoreClient(getWalletClientDetails());
    }

    @Bean
    public SDMoneyClient getSDMoneyClient() throws Exception {
        return new SDMoneyClient(getWalletClientDetails());
    }

    @Bean
    public IDigitalGoldServiceClient getMFDigitalGoldServiceClient() throws Exception {
        com.freecharge.redirector.utils.ClientDetails.init(mfClientName, mfClientUrl, mfClienPort, mfClienTimeout, mfClientId, mfClientKey);
        return new DigitalGoldServiceClientImpl();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(mailSenderUserName);
        mailSender.setPassword(mailSenderPassword);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return mailSender;
    }
    @Bean(name = "kycManagementClient")
    public IKycManagementClient initialize() {
        IKycManagementClient kycManagementClient = null;
        try {
            com.freecharge.kyc.utils.ClientDetails.init(kycUrl, kycPort, kycClientKey, kycClientId,
                    Integer.parseInt(kycApiTimeout));
            log.info("kyc client initialised with details: {}, {}, {}, {}, {}", kycUrl, kycPort,
                    kycClientKey, kycClientId, kycApiTimeout);
            kycManagementClient = new KycManagementClientImpl();
        } catch (Exception e) {
            log.error("Error in creating Connection with Kyc Client :{}",e.getMessage());
            e.printStackTrace();
        }
        return kycManagementClient;
    }
}
