package com.abhishek.retail;

import org.slf4j.MDC;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class RestTemplateUtil {

    public static JdbcTemplate getJdbcTemplate(final JdbcTemplate retailBillingJdbcTemplate,
                                               final JdbcTemplate registeredBillingJdbcTemplate){
        String billingType = MDC.get("billingType");
        return "retailbillingtype".equalsIgnoreCase(billingType) ? retailBillingJdbcTemplate : registeredBillingJdbcTemplate;

    }

    public static NamedParameterJdbcTemplate getNamedJdbcTemplate(final NamedParameterJdbcTemplate retailBillingJdbcTemplate,
                                                             final NamedParameterJdbcTemplate registeredBillingJdbcTemplate){
        String billingType = MDC.get("billingType");
        return "retailbillingtype".equalsIgnoreCase(billingType) || null == billingType ? retailBillingJdbcTemplate : registeredBillingJdbcTemplate;

    }
}
