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

import java.util.Arrays;

/**
 * 
 * Ӣ���ַ��������������ӷִ���
 */
class LetterSegmenter implements ISegmenter {
	
	//�ӷִ�����ǩ
	static final String SEGMENTER_NAME = "LETTER_SEGMENTER";
	//���ӷ���
	private static final char[] Letter_Connector = new char[]{'#' , '&' , '+' , '-' , '.' , '@' , '_'};
	
	//���ַ���
	private static final char[] Num_Connector = new char[]{',' , '.'};
	
	/*
	 * ��Ԫ�Ŀ�ʼλ�ã�
	 * ͬʱ��Ϊ�ӷִ���״̬��ʶ
	 * ��start > -1 ʱ����ʶ��ǰ�ķִ������ڴ����ַ�
	 */
	private int start;
	/*
	 * ��¼��Ԫ����λ��
	 * end��¼�����ڴ�Ԫ�����һ�����ֵ�Letter����Sign_Connector���ַ���λ��
	 */
	private int end;
	
	/*
	 * ��ĸ��ʼλ��
	 */
	private int englishStart;

	/*
	 * ��ĸ����λ��
	 */
	private int englishEnd;
	
	/*
	 * ������������ʼλ��
	 */
	private int arabicStart;
	
	/*
	 * ���������ֽ���λ��
	 */
	private int arabicEnd;
	
	LetterSegmenter(){
		Arrays.sort(Letter_Connector);
		Arrays.sort(Num_Connector);
		this.start = -1;
		this.end = -1;
		this.englishStart = -1;
		this.englishEnd = -1;
		this.arabicStart = -1;
		this.arabicEnd = -1;
	}


	/* (non-Javadoc)
	 * @see org.wltea.analyzer.core.ISegmenter#analyze(org.wltea.analyzer.core.AnalyzeContext)
	 */
	public void analyze(AnalyzeContext context) {
		boolean bufferLockFlag = false;
		//����Ӣ����ĸ
		bufferLockFlag = this.processEnglishLetter(context) || bufferLockFlag;
		//����������ĸ
		bufferLockFlag = this.processArabicLetter(context) || bufferLockFlag;
		//��������ĸ(���Ҫ�����������ͨ��QuickSortSet�ų��ظ�)
		bufferLockFlag = this.processMixLetter(context) || bufferLockFlag;
		
		//�ж��Ƿ�����������
		if(bufferLockFlag){
			context.lockBuffer(SEGMENTER_NAME);
		}else{
			//�Ի���������
			context.unlockBuffer(SEGMENTER_NAME);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.core.ISegmenter#reset()
	 */
	public void reset() {
		this.start = -1;
		this.end = -1;
		this.englishStart = -1;
		this.englishEnd = -1;
		this.arabicStart = -1;
		this.arabicEnd = -1;
	}	
	
	/**
	 * ����������ĸ������
	 * �磺windos2000 | linliangyi2005@gmail.com
	 * @param input
	 * @param context
	 * @return
	 */
	private boolean processMixLetter(AnalyzeContext context){
		boolean needLock = false;
		
		if(this.start == -1){//��ǰ�ķִ�����δ��ʼ�����ַ�
			if(CharacterUtil.CHAR_ARABIC == context.getCurrentCharType()
					|| CharacterUtil.CHAR_ENGLISH == context.getCurrentCharType()){
				//��¼��ʼָ���λ��,�����ִ������봦��״̬
				this.start = context.getCursor();
				this.end = start;
			}
			
		}else{//��ǰ�ķִ������ڴ����ַ�			
			if(CharacterUtil.CHAR_ARABIC == context.getCurrentCharType()
					|| CharacterUtil.CHAR_ENGLISH == context.getCurrentCharType()){
				//��¼�¿��ܵĽ���λ��
				this.end = context.getCursor();
				
			}else if(CharacterUtil.CHAR_USELESS == context.getCurrentCharType()
						&& this.isLetterConnector(context.getCurrentChar())){
				//��¼�¿��ܵĽ���λ��
				this.end = context.getCursor();
			}else{
				//������Letter�ַ��������Ԫ
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() , this.start , this.end - this.start + 1 , Lexeme.TYPE_LETTER);
				context.addLexeme(newLexeme);
				this.start = -1;
				this.end = -1;
			}			
		}
		
		//�жϻ������Ƿ��Ѿ�����
		if(context.isBufferConsumed()){
			if(this.start != -1 && this.end != -1){
				//�����Զ��꣬�����Ԫ
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() , this.start , this.end - this.start + 1 , Lexeme.TYPE_LETTER);
				context.addLexeme(newLexeme);
				this.start = -1;
				this.end = -1;
			}
		}
		
		//�ж��Ƿ�����������
		if(this.start == -1 && this.end == -1){
			//�Ի���������
			needLock = false;
		}else{
			needLock = true;
		}
		return needLock;
	}
	
	/**
	 * ����Ӣ����ĸ���
	 * @param context
	 * @return
	 */
	private boolean processEnglishLetter(AnalyzeContext context){
		boolean needLock = false;
		
		if(this.englishStart == -1){//��ǰ�ķִ�����δ��ʼ����Ӣ���ַ�	
			if(CharacterUtil.CHAR_ENGLISH == context.getCurrentCharType()){
				//��¼��ʼָ���λ��,�����ִ������봦��״̬
				this.englishStart = context.getCursor();
				this.englishEnd = this.englishStart;
			}
		}else {//��ǰ�ķִ������ڴ���Ӣ���ַ�	
			if(CharacterUtil.CHAR_ENGLISH == context.getCurrentCharType()){
				//��¼��ǰָ��λ��Ϊ����λ��
				this.englishEnd =  context.getCursor();
			}else{
				//������English�ַ�,�����Ԫ
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() , this.englishStart , this.englishEnd - this.englishStart + 1 , Lexeme.TYPE_ENGLISH);
				context.addLexeme(newLexeme);
				this.englishStart = -1;
				this.englishEnd= -1;
			}
		}
		
		//�жϻ������Ƿ��Ѿ�����
		if(context.isBufferConsumed()){
			if(this.englishStart != -1 && this.englishEnd != -1){
				//�����Զ��꣬�����Ԫ
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() , this.englishStart , this.englishEnd - this.englishStart + 1 , Lexeme.TYPE_ENGLISH);
				context.addLexeme(newLexeme);
				this.englishStart = -1;
				this.englishEnd= -1;
			}
		}	
		
		//�ж��Ƿ�����������
		if(this.englishStart == -1 && this.englishEnd == -1){
			//�Ի���������
			needLock = false;
		}else{
			needLock = true;
		}
		return needLock;			
	}
	
	/**
	 * ���������������
	 * @param context
	 * @return
	 */
	private boolean processArabicLetter(AnalyzeContext context){
		boolean needLock = false;
		
		if(this.arabicStart == -1){//��ǰ�ķִ�����δ��ʼ���������ַ�	
			if(CharacterUtil.CHAR_ARABIC == context.getCurrentCharType()){
				//��¼��ʼָ���λ��,�����ִ������봦��״̬
				this.arabicStart = context.getCursor();
				this.arabicEnd = this.arabicStart;
			}
		}else {//��ǰ�ķִ������ڴ��������ַ�	
			if(CharacterUtil.CHAR_ARABIC == context.getCurrentCharType()){
				//��¼��ǰָ��λ��Ϊ����λ��
				this.arabicEnd = context.getCursor();
			}else if(CharacterUtil.CHAR_USELESS == context.getCurrentCharType()
					&& this.isNumConnector(context.getCurrentChar())){
				//��������֣�������ǽ���
			}else{
				////������Arabic�ַ�,�����Ԫ
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() , this.arabicStart , this.arabicEnd - this.arabicStart + 1 , Lexeme.TYPE_ARABIC);
				context.addLexeme(newLexeme);
				this.arabicStart = -1;
				this.arabicEnd = -1;
			}
		}
		
		//�жϻ������Ƿ��Ѿ�����
		if(context.isBufferConsumed()){
			if(this.arabicStart != -1 && this.arabicEnd != -1){
				//�������зֵĴ�Ԫ
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() ,  this.arabicStart , this.arabicEnd - this.arabicStart + 1 , Lexeme.TYPE_ARABIC);
				context.addLexeme(newLexeme);
				this.arabicStart = -1;
				this.arabicEnd = -1;
			}
		}
		
		//�ж��Ƿ�����������
		if(this.arabicStart == -1 && this.arabicEnd == -1){
			//�Ի���������
			needLock = false;
		}else{
			needLock = true;
		}
		return needLock;		
	}	

	/**
	 * �ж��Ƿ�����ĸ���ӷ���
	 * @param input
	 * @return
	 */
	private boolean isLetterConnector(char input){
		int index = Arrays.binarySearch(Letter_Connector, input);
		return index >= 0;
	}
	
	/**
	 * �ж��Ƿ����������ӷ���
	 * @param input
	 * @return
	 */
	private boolean isNumConnector(char input){
		int index = Arrays.binarySearch(Num_Connector, input);
		return index >= 0;
	}
}
