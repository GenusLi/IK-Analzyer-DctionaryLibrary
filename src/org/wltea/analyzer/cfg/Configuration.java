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
 */
package org.wltea.analyzer.cfg;

import java.util.List;

/**
 * 
 * ���ù�����ӿ�
 * 
 */
public interface Configuration {
	
	/**
	 * ����������ʹ�õĴʵ�����ƣ����ִʵ������Ψһ��
	 * @return
	 */
	public String getDictionaryName();
	
	/**
	 * ����useSmart��־λ
	 * useSmart =true ���ִ���ʹ�������зֲ��ԣ� =false��ʹ��ϸ�����з�
	 * @return useSmart
	 */
	public boolean useSmart();
	
	/**
	 * ����useSmart��־λ
	 * useSmart =true ���ִ���ʹ�������зֲ��ԣ� =false��ʹ��ϸ�����з�
	 * @param useSmart
	 */
	public void setUseSmart(boolean useSmart);
	
	
	/**
	 * ��ȡ���ʵ�·��
	 * 
	 * @return String ���ʵ�·��
	 */
	public String getMainDictionary();

	/**
	 * ��ȡ���ʴʵ�·��
	 * @return String ���ʴʵ�·��
	 */
	public String getQuantifierDicionary();

	/**
	 * ��ȡ��չ�ֵ�����·��
	 * @return List<String> ������������·��
	 */
	public List<String> getExtDictionarys();


	/**
	 * ��ȡ��չֹͣ�ʵ�����·��
	 * @return List<String> ������������·��
	 */
	public List<String> getExtStopWordDictionarys();
}
