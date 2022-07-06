package com.rainman.modules.ists.cmcc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rainman.cmcc")
@Data
public class RainmanCmccProperties {
    String url;
    Boolean enabled;
    String appCode;
    String jumpBackUrl;
    String xjsywWeburl;
    String xjsywQueryList;
    String xjsywQueryCount;
    String xjsywDetailView;
    String xaRoleCode;
}
