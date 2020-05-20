/**
 * IK ���ķִ�  �汾 5.0
 * IK Analyzer release 5.0
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Դ������������(linliangyi2005@gmail.com)�ṩ
 * ��Ȩ���� 2012�������蹤����
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 * 
 */
package org.wltea.analyzer.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * Configuration Ĭ��ʵ��
 * 2012-5-8
 *
 */
public class DefaultConfig implements Configuration{

	/*
	 * �ִ���Ĭ���ֵ�·�� 
	 */
	private static final String PATH_DIC_MAIN = "org/wltea/analyzer/dic/main2012.dic";
	private static final String PATH_DIC_QUANTIFIER = "org/wltea/analyzer/dic/quantifier.dic";

	/*
	 * �ִ��������ļ�·��
	 */	
	private static final String FILE_NAME = "IKAnalyzer.cfg.xml";
	//�������ԡ�����չ�ֵ�
	private static final String EXT_DICT = "ext_dict";
	//�������ԡ�����չֹͣ�ʵ�
	private static final String EXT_STOP = "ext_stopwords";
	//�������ԡ���ʡ����չ�ʵ�
	private static final String EXT_PROVINCE = "ext_province";
	//�������ԡ����м���չ�ʵ�
	private static final String EXT_CITY = "ext_city";
	//�������ԡ����ؼ���չ�ʵ�
	private static final String EXT_DIST = "ext_dist";
	private Properties props;
	/*
	 * �Ƿ�ʹ��smart��ʽ�ִ�
	 */
	private boolean useSmart;
	
	/**
	 * ���ص���
	 * @return Configuration����
	 */
	public static Configuration getInstance(){
		return new DefaultConfig();
	}
	
	/*
	 * ��ʼ�������ļ�
	 */
	private DefaultConfig(){		
		props = new Properties();
		
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
		if(input != null){
			try {
				props.loadFromXML(input);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * ����useSmart��־λ
	 * useSmart =true ���ִ���ʹ�������зֲ��ԣ� =false��ʹ��ϸ�����з�
	 * @return useSmart
	 */
	public boolean useSmart() {
		return useSmart;
	}

	/**
	 * ����useSmart��־λ
	 * useSmart =true ���ִ���ʹ�������зֲ��ԣ� =false��ʹ��ϸ�����з�
	 * @param useSmart
	 */
	public void setUseSmart(boolean useSmart) {
		this.useSmart = useSmart;
	}	
	
	/**
	 * ��ȡ���ʵ�·��
	 * 
	 * @return String ���ʵ�·��
	 */
	public String getMainDictionary(){
		return PATH_DIC_MAIN;
	}

	/**
	 * ��ȡ���ʴʵ�·��
	 * @return String ���ʴʵ�·��
	 */
	public String getQuantifierDicionary(){
		return PATH_DIC_QUANTIFIER;
	}

	/**
	 * ��ȡ��չ�ֵ�����·��
	 * @return List<String> ������������·��
	 */
	public List<String> getExtDictionarys(){
		List<String> extDictFiles = new ArrayList<String>(2);
		String extDictCfg = props.getProperty(EXT_DICT);
		if(extDictCfg != null){
			//ʹ��;�ָ�����չ�ֵ�����
			String[] filePaths = extDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
						extDictFiles.add(filePath.trim());
					}
				}
			}
		}		
		return extDictFiles;		
	}


	
	
	/**
	 * ��ȡ��չֹͣ�ʵ�����·��
	 * @return List<String> ������������·��
	 */
	public List<String> getExtStopWordDictionarys(){
		List<String> extStopWordDictFiles = new ArrayList<String>(2);
		String extStopWordDictCfg = props.getProperty(EXT_STOP);
		if(extStopWordDictCfg != null){
			//ʹ��;�ָ�����չ�ֵ�����
			String[] filePaths = extStopWordDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
						extStopWordDictFiles.add(filePath.trim());
					}
				}
			}
		}		
		return extStopWordDictFiles;		
	}

	@Override
	public String getDictionaryName() {
		return "main";
	}
			

}
