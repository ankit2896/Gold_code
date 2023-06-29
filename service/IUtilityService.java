package com.freecharge.financial.service;


import java.util.Map;

public interface IUtilityService {

   void sendEmailAlerts(String subject, String content);
   void sendEmailAlerts(String subject, Exception e);
   void sendEmailAlerts(String subject, Map<String, String> metadata, Exception e);

}