package org.openo.orchestrator.nfv.umc.cometdserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CometdServletInfo {
    private String servletClass;
    private String servletPath;
}
