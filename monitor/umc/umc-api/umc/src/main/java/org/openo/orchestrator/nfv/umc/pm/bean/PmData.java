
package org.openo.orchestrator.nfv.umc.pm.bean;

import org.openo.orchestrator.nfv.umc.i18n.I18n;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of performance data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmData {
    private String counterOrIndexId;
    private String counterOrIndexName;
    private String value;
    
    public void translate(String language){
        I18n i18n = I18n.getInstance(language);
        if(i18n == null){
            return;
        }
        
        this.counterOrIndexName = i18n.translate(this.counterOrIndexName);
    }
}
