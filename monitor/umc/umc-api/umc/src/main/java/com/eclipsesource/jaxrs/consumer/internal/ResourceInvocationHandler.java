/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eclipsesource.jaxrs.consumer.internal;

import static javax.ws.rs.core.Response.Status.Family.CLIENT_ERROR;
import static javax.ws.rs.core.Response.Status.Family.SERVER_ERROR;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.message.internal.MediaTypes;


public class ResourceInvocationHandler implements InvocationHandler {

  private final Client client;
  private final String baseUrl;

  public ResourceInvocationHandler( String baseUrl, Configuration configuration ) {
    ClientBuilder clientBuilder = ClientBuilder.newBuilder();
    this.client = clientBuilder.withConfig( configuration )
                               .sslContext( ClientHelper.createSSLContext() )
                               .hostnameVerifier( ClientHelper.createHostNameVerifier() )
                               .build();
    this.baseUrl = baseUrl;
  }

  @Override
  public Object invoke( Object proxy, Method method, Object[] parameter ) throws Throwable {
    RequestConfigurer configurer = new RequestConfigurer( client, baseUrl, method, parameter );
    return sendRequest( method, parameter, configurer );
  }

  private Object sendRequest( Method method, Object[] parameter, RequestConfigurer configurer ) {
    Object result = null;
    Builder request = configurer.configure();
    if( method.isAnnotationPresent( GET.class ) ) {
      result = sendGetRequest( configurer, method, request );
    } if( method.isAnnotationPresent( POST.class ) ) {
      result = sendPostRequest( configurer, method, parameter, request );
    } if( method.isAnnotationPresent( PUT.class ) ) {
      result = sendPutRequest( configurer, method, parameter, request );
    } if( method.isAnnotationPresent( DELETE.class ) ) {
      result = sendDeleteRequest( configurer, method, request );
    } if( method.isAnnotationPresent( HEAD.class ) ) {
      result = sendHeadRequest( configurer, method, request );
    } if( method.isAnnotationPresent( OPTIONS.class ) ) {
      result = sendOptionsRequest( configurer, method, request );
    }
    return result;
  }

  private Object sendGetRequest( RequestConfigurer configurer, Method method, Builder request ) {
    checkHasNoFormParameter( method );
    Response response = request.get();
    validateResponse( configurer, response, "GET" );
    // modify by wangyg 2015.9:change readEntity impl
    //return response.readEntity(  method.getReturnType()  );
    Type genType = method.getGenericReturnType();
	if(genType instanceof ParameterizedType){
		return response.readEntity(new GenericType<Object>(genType));
	}else{
		return response.readEntity(  method.getReturnType()  );
	}
	// end modify by wangyg:change readEntity impl
  }

  private Object sendPostRequest( RequestConfigurer configurer, Method method, Object[] parameter, Builder request ) {
    Response response = request.post( getPostEntity( method, parameter ) );
    validateResponse( configurer, response, "POST" );
    // modify by wangyg 2015.9:change readEntity impl
    //return response.readEntity(  method.getReturnType()  );
    Type genType = method.getGenericReturnType();
	if(genType instanceof ParameterizedType){
		return response.readEntity(new GenericType<Object>(genType));
	}else{
		return response.readEntity(  method.getReturnType()  );
	}
	// end modify by wangyg:change readEntity impl
  }

  private Object sendPutRequest( RequestConfigurer configurer, Method method, Object[] parameter, Builder request ) {
    Response response = request.put( getEntity( method, parameter ) );
    validateResponse( configurer, response, "PUT" );
    // modify by wangyg 2015.9:change readEntity impl
    //return response.readEntity(  method.getReturnType()  );
    Type genType = method.getGenericReturnType();
	if(genType instanceof ParameterizedType){
		return response.readEntity(new GenericType<Object>(genType));
	}else{
		return response.readEntity(  method.getReturnType()  );
	}
	// end modify by wangyg:change readEntity impl
  }

  private Object sendDeleteRequest( RequestConfigurer configurer, Method method, Builder request ) {
    Response response = request.delete();
    validateResponse( configurer, response, "DELETE" );
    // modify by wangyg 2015.9:change readEntity impl
    //return response.readEntity(  method.getReturnType()  );
    Type genType = method.getGenericReturnType();
	if(genType instanceof ParameterizedType){
		return response.readEntity(new GenericType<Object>(genType));
	}else{
		return response.readEntity(  method.getReturnType()  );
	}
	// end modify by wangyg:change readEntity impl
  }

  private Object sendHeadRequest( RequestConfigurer configurer, Method method, Builder request ) {
    Response response = request.head();
    validateResponse( configurer, response, "HEAD" );
    // modify by wangyg 2015.9:change readEntity impl
    //return response.readEntity(  method.getReturnType()  );
    Type genType = method.getGenericReturnType();
	if(genType instanceof ParameterizedType){
		return response.readEntity(new GenericType<Object>(genType));
	}else{
		return response.readEntity(  method.getReturnType()  );
	}
	// end modify by wangyg:change readEntity impl
  }

  private Object sendOptionsRequest( RequestConfigurer configurer, Method method, Builder request ) {
    Response response = request.options();
    validateResponse( configurer, response, "OPTIONS" );
    // modify by wangyg 2015.9:change readEntity impl
    //return response.readEntity(  method.getReturnType()  );
    Type genType = method.getGenericReturnType();
	if(genType instanceof ParameterizedType){
		return response.readEntity(new GenericType<Object>(genType));
	}else{
		return response.readEntity(  method.getReturnType()  );
	}
	// end modify by wangyg:change readEntity impl
  }

  private void validateResponse( RequestConfigurer configurer, Response response, String method ) {
    Family family = response.getStatusInfo().getFamily();
    if( family == SERVER_ERROR || family == CLIENT_ERROR ) {
      RequestError requestError = new RequestError( configurer, response, method );
      throw new IllegalStateException( requestError.getMessage() );
    }
  }

  private void checkHasNoFormParameter( Method method ) {
    if( hasFormParameter( method ) || hasMultiPartFormParameter( method ) ) {
      throw new IllegalStateException( "@GET methods can not have @FormParam or @FormDataParam parameters." );
    }
  }

  private Entity<?> getPostEntity( Method method, Object[] parameter ) {
    Entity<?> result;
    try {
      result = getEntity( method, parameter );
    } catch( IllegalStateException noEntityException ) {
      result = null;
    }
    return result;
  }

  private Entity<?> getEntity( Method method, Object[] parameter ) {
    Entity<?> result = null;
    if( hasFormParameter( method ) ) {
      Form form = computeForm( method, parameter );
      result = Entity.form( form );
    } else if( hasMultiPartFormParameter( method ) ) {
      MultiPart mp = computeMultiPart( method, parameter );
      result = Entity.entity( mp, mp.getMediaType() );
    } else {
      result = determineBodyParameter( method, parameter );
    }
    return result;
  }

  private boolean hasFormParameter( Method method ) {
    return ClientHelper.hasFormAnnotation( method, FormParam.class );
  }

  private Form computeForm( Method method, Object[] parameter ) {
    Form result = new Form();
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      FormParam formParam = extractAnnotation( annotations, FormParam.class );
      if( formParam != null ) {
        result.param( formParam.value(), parameter[ i ].toString() );
      }
    }
    return result.asMap().isEmpty() ? null : result;
  }

  private boolean hasMultiPartFormParameter( Method method ) {
    return ClientHelper.hasFormAnnotation( method, FormDataParam.class );
  }

  @SuppressWarnings( "resource" )
  private MultiPart computeMultiPart( Method method, Object[] parameter ) {
    FormDataMultiPart result = new FormDataMultiPart();
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      FormDataParam param = extractAnnotation( annotations, FormDataParam.class );
      if( param != null ) {
        result.field( param.value(), parameter[ i ], determinePartContentType( parameter[ i ] ) );
      }
    }
    return result.getFields().isEmpty() ? null : result;
  }

  private MediaType determinePartContentType( Object parameter ) {
    // TODO: how to get the media type? we could use something like @FormData
    if( parameter instanceof BodyPart ) {
      return ( ( BodyPart )parameter ).getMediaType();
    }
    if( parameter instanceof ContentDisposition ) {
      return MediaType.valueOf( ( ( ContentDisposition )parameter ).getType() );
    }
    return MediaType.TEXT_PLAIN_TYPE;
  }

  private <T extends Annotation> T extractAnnotation( Annotation[] annotations, Class<T> type ) {
    for( Annotation annotation : annotations ) {
      if( annotation.annotationType() == type ) {
        return type.cast( annotation );
      }
    }
    return null;
  }

  private Entity<?> determineBodyParameter( Method method, Object[] parameter ) {
    Entity<?> result = null;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    checkParametersForAnnotation( method, parameterAnnotations );
    int firstNonAnnotatedParameter = getFirstNonAnnotatedParameter( parameterAnnotations );
    if( firstNonAnnotatedParameter != -1 ) {
      result =  Entity.entity( parameter[ firstNonAnnotatedParameter ], determineContentType( method ) );
    } else {
      throw new IllegalStateException( "Can not find entity for method " + method.getName()
                                       + ". It has no non-annotated parameter" );
    }
    return result;
  }

  private int getFirstNonAnnotatedParameter( Annotation[][] parameterAnnotations ) {
    int firstNonAnnotatedParameter = -1;
    for( int i = 0; i < parameterAnnotations.length; i++ ) {
      Annotation[] annotations = parameterAnnotations[ i ];
      if( annotations.length == 0 ) {
        firstNonAnnotatedParameter = i;
        break;
      }
    }
    return firstNonAnnotatedParameter;
  }

  private void checkParametersForAnnotation( Method method, Annotation[][] parameterAnnotations ) {
    if( parameterAnnotations.length == 0 ) {
      throw new IllegalStateException( "Can not find entity for method "
                                       + method.getName() + ". It has no paramters." );
    }
  }

  // TODO: Think about a better determination of the content type
  private MediaType determineContentType( Method method ) {
    MediaType result = MediaType.TEXT_PLAIN_TYPE;
    if( method.isAnnotationPresent( Consumes.class ) ) {
      result = MediaTypes.createFrom( method.getAnnotation( Consumes.class ) ).get( 0 );
    }
    return result;
  }
}
