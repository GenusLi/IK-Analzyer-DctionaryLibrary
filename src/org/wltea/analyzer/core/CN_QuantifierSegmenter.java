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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;

/**
 * 
 * �����������ӷִ���
 */
class CN_QuantifierSegmenter implements ISegmenter{
	
	//�ӷִ�����ǩ
	static final String SEGMENTER_NAME = "QUAN_SEGMENTER";
	
	//��������
	private static String Chn_Num = "һ�������������߰˾�ʮ��Ҽ��������½��ƾ�ʰ��ǧ����ʰ��Ǫ�f�|��ئإ";//Cnum
	private static Set<Character> ChnNumberChars = new HashSet<Character>();
	static{
		char[] ca = Chn_Num.toCharArray();
		for(char nChar : ca){
			ChnNumberChars.add(nChar);
		}
	}
	
	/*
	 * ��Ԫ�Ŀ�ʼλ�ã�
	 * ͬʱ��Ϊ�ӷִ���״̬��ʶ
	 * ��start > -1 ʱ����ʶ��ǰ�ķִ������ڴ����ַ�
	 */
	private int nStart;
	/*
	 * ��¼��Ԫ����λ��
	 * end��¼�����ڴ�Ԫ�����һ�����ֵĺ�������ʽ���
	 */
	private int nEnd;

	//�����������hit����
	private List<Hit> countHits;
	
	
	CN_QuantifierSegmenter(){
		nStart = -1;
		nEnd = -1;
		this.countHits  = new LinkedList<Hit>();
	}
	
	/**
	 * �ִ�
	 */
	public void analyze(AnalyzeContext context) {
		//������������
		this.processCNumber(context);
		//������������
		this.processCount(context);
		
		//�ж��Ƿ�����������
		if(this.nStart == -1 && this.nEnd == -1	&& countHits.isEmpty()){
			//�Ի���������
			context.unlockBuffer(SEGMENTER_NAME);
		}else{
			context.lockBuffer(SEGMENTER_NAME);
		}
	}
	

	/**
	 * �����ӷִ���״̬
	 */
	public void reset() {
		nStart = -1;
		nEnd = -1;
		countHits.clear();
	}
	
	/**
	 * ��������
	 */
	private void processCNumber(AnalyzeContext context){
		if(nStart == -1 && nEnd == -1){//��ʼ״̬
			if(CharacterUtil.CHAR_CHINESE == context.getCurrentCharType() 
					&& ChnNumberChars.contains(context.getCurrentChar())){
				//��¼���ʵ���ʼ������λ��
				nStart = context.getCursor();
				nEnd = context.getCursor();
			}
		}else{//���ڴ���״̬
			if(CharacterUtil.CHAR_CHINESE == context.getCurrentCharType() 
					&& ChnNumberChars.contains(context.getCurrentChar())){
				//��¼���ʵĽ���λ��
				nEnd = context.getCursor();
			}else{
				//�������
				this.outputNumLexeme(context);
				//����ͷβָ��
				nStart = -1;
				nEnd = -1;
			}
		}
		
		//�������Ѿ����꣬������δ���������
		if(context.isBufferConsumed()){
			if(nStart != -1 && nEnd != -1){
				//�������
				outputNumLexeme(context);
				//����ͷβָ��
				nStart = -1;
				nEnd = -1;
			}
		}	
	}
	
	/**
	 * ������������
	 * @param context
	 */
	private void processCount(AnalyzeContext context){
		// �ж��Ƿ���Ҫ��������ɨ��
		if(!this.needCountScan(context)){
			return;
		}
		
		if(CharacterUtil.CHAR_CHINESE == context.getCurrentCharType()){
			
			//���ȴ���countHits�е�hit
			if(!this.countHits.isEmpty()){
				//����ʶζ���
				Hit[] tmpArray = this.countHits.toArray(new Hit[this.countHits.size()]);
				for(Hit hit : tmpArray){
					hit = Dictionary.getSingleton(context.getConfig()).matchWithHit(context.getSegmentBuff(), context.getCursor() , hit);
					if(hit.isMatch()){
						//�����ǰ�Ĵ�
						Lexeme newLexeme = new Lexeme(context.getBufferOffset() , hit.getBegin() , context.getCursor() - hit.getBegin() + 1 , Lexeme.TYPE_COUNT);
						context.addLexeme(newLexeme);
						
						if(!hit.isPrefix()){//���Ǵ�ǰ׺��hit����Ҫ����ƥ�䣬�Ƴ�
							this.countHits.remove(hit);
						}
						
					}else if(hit.isUnmatch()){
						//hit���Ǵʣ��Ƴ�
						this.countHits.remove(hit);
					}					
				}
			}				

			//*********************************
			//�Ե�ǰָ��λ�õ��ַ����е���ƥ��
			Hit singleCharHit = Dictionary.getSingleton(context.getConfig()).matchInQuantifierDict(context.getSegmentBuff(), context.getCursor(), 1);
			if(singleCharHit.isMatch()){//���ֳ����ʴ�
				//�����ǰ�Ĵ�
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() , context.getCursor() , 1 , Lexeme.TYPE_COUNT);
				context.addLexeme(newLexeme);

				//ͬʱҲ�Ǵ�ǰ׺
				if(singleCharHit.isPrefix()){
					//ǰ׺ƥ�������hit�б�
					this.countHits.add(singleCharHit);
				}
			}else if(singleCharHit.isPrefix()){//����Ϊ����ǰ׺
				//ǰ׺ƥ�������hit�б�
				this.countHits.add(singleCharHit);
			}
			
			
		}else{
			//����Ĳ��������ַ�
			//���δ���ε�����
			this.countHits.clear();
		}
		
		//�����������Ѿ����꣬������δ���������
		if(context.isBufferConsumed()){
			//���δ���ε�����
			this.countHits.clear();
		}
	}
	
	/**
	 * �ж��Ƿ���Ҫɨ������
	 * @return
	 */
	private boolean needCountScan(AnalyzeContext context){
		if((nStart != -1 && nEnd != -1 ) || !countHits.isEmpty()){
			//���ڴ�����������,�������ڴ�������
			return true;
		}else{
			//�ҵ�һ�����ڵ�����
			if(!context.getOrgLexemes().isEmpty()){
				Lexeme l = context.getOrgLexemes().peekLast();
				if(Lexeme.TYPE_CNUM == l.getLexemeType() ||  Lexeme.TYPE_ARABIC == l.getLexemeType()){
					if(l.getBegin() + l.getLength() == context.getCursor()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * ������ʴ�Ԫ�������
	 * @param context
	 */
	private void outputNumLexeme(AnalyzeContext context){
		if(nStart > -1 && nEnd > -1){
			//�������
			Lexeme newLexeme = new Lexeme(context.getBufferOffset() , nStart , nEnd - nStart + 1 , Lexeme.TYPE_CNUM);
			context.addLexeme(newLexeme);
			
		}
	}

}
