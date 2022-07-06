package com.rainman.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "rainman.wf")
@Data
public class RainmanWfProperties {
    /**
     * 查询角色
     */
    List<String> queryRoles;

    Long cleanTime;
}
