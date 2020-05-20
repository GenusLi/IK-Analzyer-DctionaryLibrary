/**
 * 
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
package org.wltea.analyzer.dic;

/**
 * ��ʾһ�δʵ�ƥ�������
 */
public class Hit {
	//Hit��ƥ��
	private static final int UNMATCH = 0x00000000;
	//Hit��ȫƥ��
	private static final int MATCH = 0x00000001;
	//Hitǰ׺ƥ��
	private static final int PREFIX = 0x00000010;
	
	
	//��HIT��ǰ״̬��Ĭ��δƥ��
	private int hitState = UNMATCH;
	
	//��¼�ʵ�ƥ������У���ǰƥ�䵽�Ĵʵ��֧�ڵ�
	private DictSegment matchedDictSegment; 
	/*
	 * �ʶο�ʼλ��
	 */
	private int begin;
	/*
	 * �ʶεĽ���λ��
	 */
	private int end;
	
	
	/**
	 * �ж��Ƿ���ȫƥ��
	 */
	public boolean isMatch() {
		return (this.hitState & MATCH) > 0;
	}
	/**
	 * 
	 */
	public void setMatch() {
		this.hitState = this.hitState | MATCH;
	}

	/**
	 * �ж��Ƿ��Ǵʵ�ǰ׺
	 */
	public boolean isPrefix() {
		return (this.hitState & PREFIX) > 0;
	}
	/**
	 * 
	 */
	public void setPrefix() {
		this.hitState = this.hitState | PREFIX;
	}
	/**
	 * �ж��Ƿ��ǲ�ƥ��
	 */
	public boolean isUnmatch() {
		return this.hitState == UNMATCH ;
	}
	/**
	 * 
	 */
	public void setUnmatch() {
		this.hitState = UNMATCH;
	}
	
	public DictSegment getMatchedDictSegment() {
		return matchedDictSegment;
	}
	
	public void setMatchedDictSegment(DictSegment matchedDictSegment) {
		this.matchedDictSegment = matchedDictSegment;
	}
	
	public int getBegin() {
		return begin;
	}
	
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}	
	
}
