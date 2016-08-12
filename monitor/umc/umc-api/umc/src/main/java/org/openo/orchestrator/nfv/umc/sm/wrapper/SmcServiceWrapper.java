/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.sm.wrapper;

import java.util.HashMap;

import org.openo.orchestrator.nfv.umc.sm.bean.LoginInfo;
import org.openo.orchestrator.nfv.umc.sm.bean.LoginResult;

/**
* @ClassName: SmcService
* @Description: TODO(user authentication Service Class )
* @author tanghua10186366
* @date 2015.12.20
*
*/
public class SmcServiceWrapper {

    private static HashMap<String, String> sessionMap = new HashMap<String, String>();
    private static SmcServiceWrapper instance = new SmcServiceWrapper();
    private LoginInfo loginInfo = new LoginInfo();

    public synchronized static SmcServiceWrapper getInstance() {
        return instance;
    }

    public boolean[] checkRight(String[] operations) {
        if (operations != null) {
            boolean[] value = new boolean[operations.length];
            for (int i = 0; i < value.length; i++) {
                value[i] = true;
            }
            return value;
        }
        return new boolean[0];
    }

    /**
    * @Title doLJogin
    * @Description TODO(check user Login Info )
    * @param userlogininfo
    * @param requestIpAddr
    * @return LoginResult
    */
    public LoginResult doLogin(LoginInfo userlogininfo, String requestIpAddr) {
        if (userlogininfo.getUsername().equals(this.loginInfo.getUsername())
                && userlogininfo.getPassword().equals(this.loginInfo.getPassword())) {
            addUser(requestIpAddr, userlogininfo.getUsername());
            return new LoginResult(LoginResult.SUCCESS, "0", "user login success");
        } else {
            return new LoginResult(LoginResult.FAILED, "4", "user Name or password is wrong");
        }
    }

    /**
    * @Title doLogout
    * @Description TODO(user Logout and remove User's requestIpAddr)
    * @param requestIpAddr
    * @return void
    */
    public void doLogout(String requestIpAddr) {
        removeUser(requestIpAddr);
    }

    /**
    * @Title getLoginUserName
    * @Description TODO(get user login info by requestIpAddr)
    * @param requestIpAddr
    * @return String
    */
    public String getLoginUserName(String requestIpAddr) {
        return getUser(requestIpAddr);
    }

    /**
    * @Title checkLoginUserName
    * @Description TODO(determine whether a user is logged in)
    * @param requestIpAddr
    * @param username
    * @return
    * @return boolean
    */
    public boolean checkLoginUserName(String requestIpAddr, String username) {
        if (username == null || username.length() == 0) {
            return false;
        }
        String user = getUser(requestIpAddr);
        if (user == null || !user.equals(username)) {
            return false;
        }
        return true;
    }

    /**
    * @Title addUser
    * @Description TODO(add a user's loginInfo)
    * @param requestIpAddr
    * @param username
    * @return void
    */
    private synchronized void addUser(String requestIpAddr, String username) {
        sessionMap.put(requestIpAddr, username);
    }

    /**
    * @Title removeUser
    * @Description TODO(remove a user's loginInfo)
    * @param requestIpAddr
    * @return void
    */
    private synchronized void removeUser(String requestIpAddr) {
        sessionMap.remove(requestIpAddr);
    }

    private synchronized String getUser(String requestIpAddr) {
        return sessionMap.get(requestIpAddr);
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

}
