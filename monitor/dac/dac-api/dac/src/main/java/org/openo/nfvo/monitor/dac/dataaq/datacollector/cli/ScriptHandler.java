/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.nfvo.monitor.dac.dataaq.datacollector.cli;

public class ScriptHandler {

    private int matchPos; // current position in the match
    private byte[] match; // the current bytes to look for
    private boolean done = true; // nothing to look for!

    /**
     * Setup the parser using the passed string.
     *
     * @param match the string to look for
     */
    public void setup(String match) {
        if (match == null) return;
        this.match = match.getBytes();
        matchPos = 0;
        done = false;
    }

    /**
     * Try to match the byte array s against the match string.
     *
     * @param s the array of bytes to match against
     * @param length the amount of bytes in the array
     * @return true if the string was found, else false
     */
    public boolean match(byte[] s, int length) {
        if (done) return true;
        for (int i = 0; !done && i < length; i++) {
            if (s[i] == match[matchPos]) {
                // the whole thing matched so, return the match answer
                // and reset to use the next match
                if (++matchPos >= match.length) {
                    done = true;
                    return true;
                }
            } else
                matchPos = 0; // get back to the beginning
        }
        return false;
    }

    public boolean match(String s, String proEcho) {
        String strBuff = s.trim();
        if (done) return true;
        if (strBuff.endsWith(proEcho)) {
            done = true;
            return true;
        } else {
            return false;
        }
    }
}
