/**
 * 
 */
package org.openo.orchestrator.nfv.umc.pm.adpt.roc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author yuanhu
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourceEntity {
    private String oid;
    private String moc;
    private String name;

}
