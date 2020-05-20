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
package org.wltea.analyzer.core;

/**
 * IK��Ԫ���� 
 */
public class Lexeme implements Comparable<Lexeme>{
	//lexemeType����
	//δ֪
	public static final int TYPE_UNKNOWN = 0;
	//Ӣ��
	public static final int TYPE_ENGLISH = 1;
	//����
	public static final int TYPE_ARABIC = 2;
	//Ӣ�����ֻ��
	public static final int TYPE_LETTER = 3;
	//���Ĵ�Ԫ
	public static final int TYPE_CNWORD = 4;
	//���ĵ���
	public static final int TYPE_CNCHAR = 64;
	//�պ�����
	public static final int TYPE_OTHER_CJK = 8;
	//��������
	public static final int TYPE_CNUM = 16;
	//��������
	public static final int TYPE_COUNT = 32;
	//����������
	public static final int TYPE_CQUAN = 48;
	
	//��Ԫ����ʼλ��
	private int offset;
    //��Ԫ�������ʼλ��
    private int begin;
    //��Ԫ�ĳ���
    private int length;
    //��Ԫ�ı�
    private String lexemeText;
    //��Ԫ����
    private int lexemeType;
    
    
	public Lexeme(int offset , int begin , int length , int lexemeType){
		this.offset = offset;
		this.begin = begin;
		if(length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
		this.lexemeType = lexemeType;
	}
	
    /*
     * �жϴ�Ԫ����㷨
     * ��ʼλ��ƫ�ơ���ʼλ�á���ֹλ����ͬ
     * @see java.lang.Object#equals(Object o)
     */
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if(this == o){
			return true;
		}
		
		if(o instanceof Lexeme){
			Lexeme other = (Lexeme)o;
			if(this.offset == other.getOffset()
					&& this.begin == other.getBegin()
					&& this.length == other.getLength()){
				return true;			
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
	
    /*
     * ��Ԫ��ϣ�����㷨
     * @see java.lang.Object#hashCode()
     */
    public int hashCode(){
    	int absBegin = getBeginPosition();
    	int absEnd = getEndPosition();
    	return  (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }
    
    /*
     * ��Ԫ�����򼯺��еıȽ��㷨
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
	public int compareTo(Lexeme other) {
		//��ʼλ������
        if(this.begin < other.getBegin()){
            return -1;
        }else if(this.begin == other.getBegin()){
        	//��Ԫ��������
        	if(this.length > other.getLength()){
        		return -1;
        	}else if(this.length == other.getLength()){
        		return 0;
        	}else {//this.length < other.getLength()
        		return 1;
        	}
        	
        }else{//this.begin > other.getBegin()
        	return 1;
        }
	}
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getBegin() {
		return begin;
	}
	/**
	 * ��ȡ��Ԫ���ı��е���ʼλ��
	 * @return int
	 */
	public int getBeginPosition(){
		return offset + begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	/**
	 * ��ȡ��Ԫ���ı��еĽ���λ��
	 * @return int
	 */
	public int getEndPosition(){
		return offset + begin + length;
	}
	
	/**
	 * ��ȡ��Ԫ���ַ�����
	 * @return int
	 */
	public int getLength(){
		return this.length;
	}	
	
	public void setLength(int length) {
		if(this.length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
	}
	
	/**
	 * ��ȡ��Ԫ���ı�����
	 * @return String
	 */
	public String getLexemeText() {
		if(lexemeText == null){
			return "";
		}
		return lexemeText;
	}

	public void setLexemeText(String lexemeText) {
		if(lexemeText == null){
			this.lexemeText = "";
			this.length = 0;
		}else{
			this.lexemeText = lexemeText;
			this.length = lexemeText.length();
		}
	}

	/**
	 * ��ȡ��Ԫ����
	 * @return int
	 */
	public int getLexemeType() {
		return lexemeType;
	}
	
	/**
	 * ��ȡ��Ԫ���ͱ�ʾ�ַ���
	 * @return String
	 */
	public String getLexemeTypeString(){
		switch(lexemeType) {

		case TYPE_ENGLISH :
			return "ENGLISH";
			
		case TYPE_ARABIC :
			return "ARABIC";
			
		case TYPE_LETTER :
			return "LETTER";
			
		case TYPE_CNWORD : 
			return "CN_WORD";
			
		case TYPE_CNCHAR : 
			return "CN_CHAR";
			
		case TYPE_OTHER_CJK :
			return "OTHER_CJK";
			
		case TYPE_COUNT :
			return "COUNT";
			
		case TYPE_CNUM :
			return "TYPE_CNUM";
			
		case TYPE_CQUAN:	
			return "TYPE_CQUAN";
			
		default :
			return "UNKONW";
		}
	}
		

	public void setLexemeType(int lexemeType) {
		this.lexemeType = lexemeType;
	}
	
	/**
	 * �ϲ��������ڵĴ�Ԫ
	 * @param l
	 * @param lexemeType
	 * @return boolean ��Ԫ�Ƿ�ɹ��ϲ�
	 */
	public boolean append(Lexeme l , int lexemeType){
		if(l != null && this.getEndPosition() == l.getBeginPosition()){
			this.length += l.getLength();
			this.lexemeType = lexemeType;
			return true;
		}else {
			return false;
		}
	}
	

	/**
	 * 
	 */
	public String toString(){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
		strbuf.append(" : ").append(this.lexemeText).append(" : \t");
		strbuf.append(this.getLexemeTypeString());
		return strbuf.toString();
	}
	

}
