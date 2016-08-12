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
package org.openo.orchestrator.nfv.dac.common.util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author wangjiangping
 * @date 2016/4/1 15:57:17
 *
 */
public class ExtensionAccess {
    static Map containerMap = new HashMap();

    public static List<String> getExtensionClassName( String extensionID ) {
        ExtensionContainter container = (ExtensionContainter)containerMap.get( extensionID );
        if( container == null ){
            return null;
        }
        return container.getAllClassName();
    }

    public static Class getExtensionClass( String extensionID, String type ){
        ExtensionContainter container = (ExtensionContainter)containerMap.get( extensionID );
        if( container == null ){
            return null;
        }
        return container.getOneClass( type.toLowerCase() );
    }

    public static Object getExtension( String extensionID, String type ){
        ExtensionContainter container = (ExtensionContainter)containerMap.get( extensionID );
        if( container == null ){
            return null;
        }
        return container.getOne( type.toLowerCase() );
    }


    public static Object [] getExtensions( String extensionID, String type ){
        ExtensionContainter container = (ExtensionContainter)containerMap.get( extensionID );
        if( container == null ){
            return new Object[ 0 ];
        }
        return container.getMany( type.toLowerCase() );
    }

    public static Object [] getExtensions( String extensionID ){
        ExtensionContainter container = (ExtensionContainter)containerMap.get( extensionID );
        if( container == null ){
            return new Object[ 0 ];
        }
        return container.getAll();
    }


    public static void tryToInjectExtensionBindings( File [] descFiles, File [] extensionImplFiles ){
        SAXBuilder builder = new SAXBuilder();
        ClassLoader implClassLoader = ExtensionAccess.class.getClassLoader();
        for( int i = 0; i < descFiles.length; i ++ ){
            try{
                Element rootElement = builder.build( descFiles[ i ] ).getRootElement();
                for( Iterator it = rootElement.getChildren().iterator(); it.hasNext(); ) {
                    Element extensionDescElement = (Element)it.next();
                    ExtensionAccess.ExtensionDesc desc = new ExtensionAccess.ExtensionDesc( extensionDescElement );
                    ExtensionAccess.ExtensionContainter container = new ExtensionAccess.ExtensionContainter( desc, implClassLoader );
                    ExtensionAccess.containerMap.put( desc.getExtensionID(), container );
                }
            }
            catch( Exception ignore ){
            }
        }
        for( int i = 0; i < extensionImplFiles.length; i ++ ){
            try{
                Element rootElement = builder.build( extensionImplFiles[ i ] ).getRootElement();
                for( Iterator it = rootElement.getChildren().iterator(); it.hasNext(); ) {
                    Element extensionElement = (Element)it.next();
                    String theExtensionID = extensionElement.getAttribute( "id" ).getValue().trim();
                    ExtensionAccess.ExtensionContainter theContainer = (ExtensionAccess.ExtensionContainter)ExtensionAccess.containerMap.get( theExtensionID );
                    if( theContainer != null ){
                        if( extensionElement.getName().equals( "extension-point" ) ){
                            for( Iterator itor = extensionElement.getChildren().iterator(); itor.hasNext(); ){
                                Element implElement = (Element)itor.next();
                                ExtensionAccess.ExtensionImpl extension = new ExtensionAccess.ExtensionImpl( implElement );
                                theContainer.addImpl( extension );
                            }
                        }
                        else{ //its name must be 'extension-chain'
                            String filterType = "";
                            Attribute typeAttribute = extensionElement.getAttribute( "type" );
                            if( typeAttribute != null ){
                                filterType = typeAttribute.getValue().trim();
                            }
                            Element [] chainElements = (Element [])extensionElement.getChildren().toArray( new Element[ extensionElement.getChildren().size() ] );
                            ExtensionAccess.ExtensionImpl extension = new ExtensionAccess.ExtensionImpl( filterType, chainElements );
                            theContainer.addImpl( extension );
                        }
                    }
                }
            }
            catch( Exception ignore ){
            }
        }
    }

    public static class ExtensionDesc {
        String extensionID;
        String extentionInterfaceClassName = null;
        Class extentionInterfaceClass = null;

        ExtensionDesc(){
        }
        void writeFields( ObjectOutput out )throws IOException{
            out.writeUTF( extensionID );
            out.writeUTF( extentionInterfaceClassName );
        }
        void readFields( ObjectInput in )throws IOException{
            extensionID = in.readUTF();
            extentionInterfaceClassName = in.readUTF();
        }

        public String getExtensionID(){
            return extensionID;
        }


        public ExtensionDesc( Element extensionDescElement ){
            extensionID = extensionDescElement.getAttribute( "id" ).getValue().trim();
            String interfaceClassName = extensionDescElement.getChild( "interface-class" ).getText().trim();
            extentionInterfaceClassName = interfaceClassName;
        }
        ExtensionDesc( String extensionID, Class interfaceClass ){
            this.extensionID = extensionID;
            this.extentionInterfaceClass = interfaceClass;
            this.extentionInterfaceClassName = interfaceClass.getName();
        }
    }

    public static class ExtensionImpl implements Comparable{
        String filterType;
        String implClassName;
        Object implObject;
        String [] setMethodNames = null;
        String[] attributeValues = null;

        String [] chainImplClassNames = null;

        boolean preciseMatch = true;
        int serialNumber = 0;
        boolean singleton = false;

        ExtensionImpl(){
        }
        void writeFields( ObjectOutput out )throws IOException{
            out.writeUTF( filterType );
            out.writeBoolean( preciseMatch );
            out.writeInt( serialNumber );
            out.writeBoolean( singleton );
            if( chainImplClassNames == null ){
                out.writeBoolean( false );
                out.writeUTF( implClassName );
                int methodCount = setMethodNames == null ? 0 : setMethodNames.length;
                out.writeByte( (byte)methodCount ); //8λ�㹻����
                for( int i = 0; i < methodCount; i ++ ){
                    out.writeUTF( setMethodNames[ i ] );
                    out.writeUTF( attributeValues[ i ] );
                }
            }
            else{
                out.writeBoolean( true );
                out.writeByte( chainImplClassNames.length );
                for( int i = 0; i < chainImplClassNames.length; i ++ ){
                    out.writeUTF( chainImplClassNames[ i ] );
                }
            }
        }
        void readFields( ObjectInput in )throws IOException{
            filterType = in.readUTF();
            preciseMatch = in.readBoolean();
            serialNumber = in.readInt();
            singleton = in.readBoolean();
            boolean isChain = in.readBoolean();
            if( !isChain ){
                implClassName = in.readUTF();
                int methodCount = in.readByte();
                if( methodCount > 0 ){
                    setMethodNames = new String[ methodCount ];
                    attributeValues = new String[ methodCount ];
                    for( int i = 0; i < methodCount; i ++ ){
                        setMethodNames[ i ] = in.readUTF();
                        attributeValues[ i ] = in.readUTF();
                    }
                }
            }
            else{
                int chainCount = in.readByte();
                chainImplClassNames = new String[ chainCount ];
                for( int i = 0; i < chainCount; i ++ ){
                    chainImplClassNames[ i ] = in.readUTF();
                }
                implObject = new DynamicSubject( chainImplClassNames );
            }
        }

        public ExtensionImpl( Element extensionElement ){
            if( extensionElement.getAttribute( "type" ) != null ){
                filterType = extensionElement.getAttribute( "type" ).getValue().trim().toLowerCase();
            }
            else{
                filterType = "";
            }
            if( extensionElement.getAttribute( "precise-match" ) != null ){
                String preciseMatchText = extensionElement.getAttributeValue( "precise-match" ).trim();
                if( preciseMatchText.equals( "false" ) ){
                    preciseMatch = false;
                }
            }
            if( extensionElement.getAttribute( "singleton" ) != null ){
                String singletonText = extensionElement.getAttributeValue( "singleton" );
                if( singletonText.equals( "true" ) ){
                    singleton = true;
                }
            }
            if( extensionElement.getAttribute( "serial-number" ) != null ){
                String serialNumberText = extensionElement.getAttributeValue( "serial-number" ).trim();
                serialNumber = Integer.parseInt( serialNumberText );
            }
            implClassName = extensionElement.getAttribute( "class" ).getValue().trim();
            Element attributesElement = extensionElement.getChild("attributes");
            if (attributesElement != null) {
                List attributeElements = attributesElement.getChildren();
                setMethodNames = new String[attributeElements.size()];
                attributeValues = new String[setMethodNames.length];
                int index = 0;
                for (Iterator it = attributeElements.iterator(); it.hasNext(); index++) {
                    Element attributeElement = (Element) it.next();
                    attributeValues[index] = attributeElement.getAttribute("value").getValue().trim();
                    String attributeName = attributeElement.getAttribute("name").getValue().trim();
                    char[] nameChars = attributeName.toCharArray();
                    StringBuffer methodNameBuffer = new StringBuffer("set");
                    methodNameBuffer.append(Character.toUpperCase(nameChars[0]));
                    if (nameChars.length > 1) {
                        methodNameBuffer.append(nameChars, 1, nameChars.length - 1);
                    }
                    setMethodNames[ index ] = methodNameBuffer.toString();
                }
            }
        }

        public int compareTo( Object o ){
            ExtensionImpl other = (ExtensionImpl)o;
            return this.serialNumber - other.serialNumber;
        }

        public ExtensionImpl( String filterType, Element [] chainElements ){
            this.filterType = filterType;

            chainImplClassNames = new String[ chainElements.length ];
            for( int i = 0; i < chainElements.length; i ++ ){
                chainImplClassNames[ i ] = chainElements[ i ].getAttribute( "class" ).getValue().trim();
            }
            InvocationHandler ds = new DynamicSubject( chainImplClassNames );
            implObject = ds;
        }

        Object getImplObject( Class extensionInterfaceClass, ClassLoader implClassLoader ){
            if( implObject == null || !singleton ){
                Class implClass = null;
                try {
                    if( implClassLoader == null ){
                        implClass = Class.forName( implClassName );
                    }
                    else{
                        implClass = implClassLoader.loadClass( implClassName );
                    }
                    implObject = implClass.newInstance();
                    if( setMethodNames != null ){
                        for( int i = 0; i < setMethodNames.length; i ++ ){
                            Method setMethod = null;
                            try {
                                setMethod = implClass.getMethod(setMethodNames[ i ], new Class[] {String.class});
                                setMethod.invoke( implObject, new Object[] { attributeValues[ i ] } );
                            }
                            catch (NoSuchMethodException e) {
                                throw new Error("The class '" + implClass + "' has no public method of '" + setMethodNames[ i ] + "'", e);
                            }
                            catch (InvocationTargetException e) {
                                throw new Error("The method '" + setMethod + "' can not be invoked", e);
                            }
                            catch (IllegalAccessException e) {
                                throw new Error("Can not access the method '" + setMethod + "'", e );
                            }
                        }
                    }
                }
                catch( ClassNotFoundException e ){
                    throw new Error( "Can not found the class '" + implClassName + "'", e );
                }
                catch( InstantiationException e ){
                    throw new Error( "Can not construct an instance with the class '" + implClass + "'" );
                }
                catch( IllegalAccessException e ){
                    throw new Error( "Can not access the class '" + implClass + "'" );
                }
            }
            else if( implObject.getClass().equals( DynamicSubject.class ) ){
                implObject = Proxy.newProxyInstance( extensionInterfaceClass.getClassLoader(), new Class[]{ extensionInterfaceClass }, (InvocationHandler)implObject );
            }
            return implObject;
        }

        Class getImplClass(ClassLoader implClassLoader) {
            Class implClass = null;
            try {
                if (implClassLoader == null) {
                    implClass = Class.forName(implClassName);
                } else {
                    implClass = implClassLoader.loadClass(implClassName);
                }
            } catch (ClassNotFoundException e) {
                throw new Error("Can not found the class '" + implClassName
                        + "'", e);
            }

            return implClass;
        }
    }

    public static class ExtensionContainter {
        ExtensionDesc desc;
        List extensions = new LinkedList();
        ClassLoader implClassLoader = null;

        ExtensionContainter(){
        }

        void writeFields( ObjectOutput out )throws IOException{
            desc.writeFields( out );
            out.writeShort( extensions.size() );
            for( Iterator it = extensions.iterator(); it.hasNext(); ){
                ExtensionImpl next = (ExtensionImpl)it.next();
                next.writeFields( out );
            }
        }
        void readFields( ObjectInput in )throws IOException{
            desc = new ExtensionDesc();
            desc.readFields( in );
            int extensionCount = in.readShort();
            for( int i = 0; i < extensionCount; i ++ ){
                ExtensionImpl next = new ExtensionImpl();
                next.readFields( in );
                extensions.add( next );
            }
        }

        ExtensionContainter( ExtensionDesc desc ){
            this.desc = desc;
        }

        public ExtensionContainter( ExtensionDesc desc, ClassLoader implClassLoader ){
            this.desc = desc;
            this.implClassLoader = implClassLoader;
        }

        public void addImpl( ExtensionImpl impl ){
            extensions.add( impl );
        }
        void ensure( ExtensionDesc desc ){
            if( desc.extentionInterfaceClass == null ){
                try{
                    if( implClassLoader == null ){
                        desc.extentionInterfaceClass = Class.forName( desc.extentionInterfaceClassName );
                    }
                    else{
                        desc.extentionInterfaceClass = implClassLoader.loadClass( desc.extentionInterfaceClassName );
                    }
                }
                catch( ClassNotFoundException e ){
                    throw new Error( "The extension interface class '" + desc.extentionInterfaceClassName + "' does not exist" );
                }
            }
        }

        public Object getOne( String type ){
        	ExtensionImpl ei = getExtension( type );
        	if (ei != null)
        	{
        		return ei.getImplObject( desc.extentionInterfaceClass, implClassLoader );
        	}
        	else
        	{
        		return null;
        	}
        }

        public Class getOneClass( String type ){
        	ExtensionImpl ei = getExtension( type );
        	if (ei != null)
        	{
        		return ei.getImplClass(implClassLoader );
        	}
        	else
        	{
        		return null;
        	}
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public List<String> getAllClassName() {
            List<String> implClassNames = new ArrayList<String>();
            Collections.sort( extensions );
            for( Iterator it = extensions.iterator(); it.hasNext(); ){
                ExtensionImpl next = (ExtensionImpl)it.next();
                ensure( desc );
                implClassNames.add(next.implClassName);
            }
            return implClassNames;
        }

        private ExtensionImpl getExtension( String type )
        {
            Collections.sort( extensions );
            ExtensionImpl fuctionExtension = null;
            ExtensionImpl extension = null;
            for( Iterator it = extensions.iterator(); it.hasNext(); ){
                ExtensionImpl next = (ExtensionImpl)it.next();
                if( !next.preciseMatch ){
                    if( type.startsWith( next.filterType ) || isMatched( type, next.filterType ) ){
                        if( extension == null ){
                            extension = next;
                        }
                        else{
                            if( extension.filterType.length() < next.filterType.length() ){
                                extension = next;
                            }
                        }
                    }
                    else if( isFunctionCheck( next, type ) ){
                        fuctionExtension = next;
                    }
                }
                else{
                    if( type.equals( next.filterType ) ){
                        extension = next;
                        break;
                    }
                }
            }
            if( extension == null ){
                extension = fuctionExtension;
            }
            if( extension == null ){
                return null;
            }
            ensure( desc );
            return extension;
        }

        public Object [] getMany( String type ){
            List implInstances = new LinkedList();
            Collections.sort( extensions );
            for( Iterator it = extensions.iterator(); it.hasNext(); ){
                ExtensionImpl next = (ExtensionImpl)it.next();
                if( !next.preciseMatch ){
                    if( isMatched( type, next.filterType ) || isFunctionCheck( next, type ) ){
                        ensure( desc );
                        implInstances.add( next.getImplObject( desc.extentionInterfaceClass, implClassLoader ) );
                    }
                }
                else{
                    if( type.equals( next.filterType ) ){
                        ensure( desc );
                        implInstances.add( next.getImplObject( desc.extentionInterfaceClass, implClassLoader ) );
                    }
                }
            }

            return implInstances.toArray();
        }

        public Object [] getAll(){
            Object [] returnedValues = new Object[ extensions.size() ];
            int index = 0;
            Collections.sort( extensions );
            for( Iterator it = extensions.iterator(); it.hasNext(); index ++ ){
                ExtensionImpl next = (ExtensionImpl)it.next();
                ensure( desc );
                returnedValues[ index ] = next.getImplObject( desc.extentionInterfaceClass, implClassLoader );
            }
            return returnedValues;
        }
        private boolean isFunctionCheck( ExtensionImpl next, String inputType ){
            if( next.filterType.startsWith( "check(bound(")){
                String valueText = next.filterType.substring( 12 );
                int firstCommaIndex = valueText.indexOf( "," );
                int firstQuataIndex = valueText.indexOf( ")" );
                String lowText = valueText.substring( 0, firstCommaIndex ).trim();
                String highText = valueText.substring( firstCommaIndex + 1, firstQuataIndex );
                try{
                    int low = Integer.parseInt( lowText );
                    int high = Integer.parseInt( highText );
                    int inputValue = Integer.parseInt( inputType );
                    if( low <= inputValue && inputValue <= high ){
                        return true;
                    }
                }
                catch( Exception ex ){
                    return false;
                }
            }
            return false;
        }
    }
    static class DynamicSubject implements InvocationHandler{
        private String [] implClassNames = null;
        private Object [] chains = null;

        DynamicSubject( String [] implClassNames ){
            this.implClassNames = implClassNames;
        }
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if( chains == null ){
                chains = new Object[ implClassNames.length ];
                for( int i = 0; i < implClassNames.length; i ++ ){
                    try{
                        Class theImplClass = Class.forName( implClassNames[ i ] );
                        chains[ i ] = theImplClass.newInstance();
                    }
                    catch( Exception ignore ){
                        throw new Error( "Can not resolve the class '" + implClassNames[ i ] + "'", ignore );
                    }
                }
            }
            for( int i = 0; i < chains.length; i ++ ){
                method.invoke( chains[ i ], args );
            }
            return null;
        }
    }
    private static Pattern createPattern( String inputStr ) {
        String inputText = inputStr;
        String prefix = "";
        String suffix = "";
        if ( inputText.indexOf( '*' ) >= 0 || inputText.indexOf( '?' ) >= 0 ) { //��*��
            if ( inputText.startsWith( "*" ) == false && inputText.startsWith( "?" ) == false ) { //���ԡ�*����ͷ
                prefix = "^";
            }
            if ( inputText.endsWith( "*" ) == false && inputText.endsWith( "?" ) == false ) { //���ԡ�*����β
                suffix = "$";
            }
        }
        else {
        }

        StringBuffer noEscapeStr = new StringBuffer( inputText.length() );
        for ( int i = 0, length = inputText.length(); i < length; i++ ) {

            char c = inputText.charAt( i );

            if ( c == '\\' ) {
                noEscapeStr.append( "\\\\" );
            }
            else if ( c == '.' ) {
                noEscapeStr.append( "\\." );
            }
            else if ( c == '*' ) {
                noEscapeStr.append( ".*" );
            }
            else if ( c == '$' ) {
                noEscapeStr.append( "\\$" );
            }
            else if ( c == '+' ) {
                noEscapeStr.append( "\\+" );
            }
            else if ( c == '|' ) {
                noEscapeStr.append( "\\|" );
            }
            else if ( c == '[' ) {
                noEscapeStr.append( "\\[" );
            }
            else if ( c == ']' ) {
                noEscapeStr.append( "\\]" );
            }
            else if ( c == '{' ) {
                noEscapeStr.append( "\\{" );
            }
            else if ( c == '}' ) {
                noEscapeStr.append( "\\}" );
            }
            else if ( c == '(' ) {
                noEscapeStr.append( "\\(" );
            }
            else if ( c == ')' ) {
                noEscapeStr.append( "\\)" );
            }
            else if ( c == '?' ) {
                noEscapeStr.append( ".?" );
            }
            else {
                noEscapeStr.append( c );
            }
        }

        return Pattern.compile( prefix + noEscapeStr.toString() + suffix );
    }
    private static boolean isMatched( String inputType, String filterType ){
        Pattern pattern = createPattern( filterType );
        Matcher matcher = pattern.matcher( inputType );
        return matcher.find();
    }
}
