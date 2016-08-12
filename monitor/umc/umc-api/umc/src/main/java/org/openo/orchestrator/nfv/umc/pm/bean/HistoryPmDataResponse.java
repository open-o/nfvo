/**
 * 
 */
package org.openo.orchestrator.nfv.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPmDataResponse {
    private int totalCout;
    private HistoryPmDataBean[] data;

}
