/**
 * 
 */
package org.openo.orchestrator.nfv.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of resource
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String id;
    private String name;

}
