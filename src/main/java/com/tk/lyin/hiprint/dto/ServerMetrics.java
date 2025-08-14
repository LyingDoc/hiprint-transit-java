package com.tk.lyin.hiprint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMetrics {
    private String version;
    private Integer currentClients;
    private Long allClients;
    private Long webClients;
    private Long allWebClients;
    @JsonProperty("totalmem")
    private Long totalMemory;
    @JsonProperty("freemem")
    private Long freeMemory;
}
