package org.openo.orchestrator.nfv.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of resource type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceType {
    private String id;
    private String name;
}