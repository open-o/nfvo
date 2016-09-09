/**
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
package com.eclipsesource.jaxrs.consumer.internal;

import javax.ws.rs.core.Response;


public class RequestError {

  private final String requestUrl;
  private final Response response;
  private final String method;

  public RequestError( RequestConfigurer configurer, Response response, String method ) {
    this.response = response;
    this.method = method;
    this.requestUrl = configurer.getRequestUrl();
  }

  public String getMessage() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append( "Failed to send " + method + " request to: " + requestUrl );
    stringBuilder.append( "\n" );
    stringBuilder.append( "Received Status: " + response.getStatus() );
    stringBuilder.append( "\nReceived Body: " );
    if( response.hasEntity() ) {
      String body = response.readEntity( String.class );
      stringBuilder.append( body );
    }
    stringBuilder.append( "\n" );
    return stringBuilder.toString();
  }
}
