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

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.dic.Dictionary;

/**
 * 
 * �ִ���������״̬
 * 
 */
class AnalyzeContext {
	
	//Ĭ�ϻ�������С
	private static final int BUFF_SIZE = 4096;
	//�������ľ����ٽ�ֵ
	private static final int BUFF_EXHAUST_CRITICAL = 100;	
	
 
	//�ַ��ܶ�ȡ����
    private char[] segmentBuff;
    //�ַ���������
    private int[] charTypes;
    
    
    //��¼Reader���ѷ������ִ��ܳ���
    //�ڷֶ�η�����Ԫʱ���ñ����ۼƵ�ǰ��segmentBuff�����reader��ʼλ�õ�λ��
	private int buffOffset;	
    //��ǰ������λ��ָ��
    private int cursor;
    //���һ�ζ����,�ɴ�����ִ�����
	private int available;

	
	//�ӷִ�����
    //�ü��Ϸǿգ�˵�����ӷִ�����ռ��segmentBuff
    private Set<String> buffLocker;
    
    //ԭʼ�ִʽ�����ϣ�δ�����崦��
    private QuickSortSet orgLexemes;    
    //LexemePathλ��������
    private Map<Integer , LexemePath> pathMap;    
    //���շִʽ����
    private LinkedList<Lexeme> results;
    
	//�ִ���������
	private Configuration cfg;
    
	public Configuration getConfig() {
		return cfg;
	}
	
    public AnalyzeContext(Configuration cfg){
    	this.cfg = cfg;
    	this.segmentBuff = new char[BUFF_SIZE];
    	this.charTypes = new int[BUFF_SIZE];
    	this.buffLocker = new HashSet<String>();
    	this.orgLexemes = new QuickSortSet();
    	this.pathMap = new HashMap<Integer , LexemePath>();    	
    	this.results = new LinkedList<Lexeme>();
    }
    
    int getCursor(){
    	return this.cursor;
    }
//    
//    void setCursor(int cursor){
//    	this.cursor = cursor;
//    }
    
    char[] getSegmentBuff(){
    	return this.segmentBuff;
    }
    
    char getCurrentChar(){
    	return this.segmentBuff[this.cursor];
    }
    
    int getCurrentCharType(){
    	return this.charTypes[this.cursor];
    }
    
    int getBufferOffset(){
    	return this.buffOffset;
    }
	
    /**
     * ����context����������������segmentBuff 
     * @param reader
     * @return ���ش������ģ���Ч�ģ��ִ�����
     * @throws IOException 
     */
    int fillBuffer(Reader reader) throws IOException{
    	int readCount = 0;
    	if(this.buffOffset == 0){
    		//�״ζ�ȡreader
    		readCount = reader.read(segmentBuff);
    	}else{
    		int offset = this.available - this.cursor;
    		if(offset > 0){
    			//���һ�ζ�ȡ��>���һ�δ���ģ���δ������ִ�������segmentBuffͷ��
    			System.arraycopy(this.segmentBuff , this.cursor , this.segmentBuff , 0 , offset);
    			readCount = offset;
    		}
    		//������ȡreader ����onceReadIn - onceAnalyzedΪ��ʼλ�ã��������segmentBuffʣ��Ĳ���
    		readCount += reader.read(this.segmentBuff , offset , BUFF_SIZE - offset);
    	}            	
    	//��¼���һ�δ�Reader�ж���Ŀ����ַ�����
    	this.available = readCount;
    	//���õ�ǰָ��
    	this.cursor = 0;
    	return readCount;
    }

    /**
     * ��ʼ��buffָ�룬�����һ���ַ�
     */
    void initCursor(){
    	this.cursor = 0;
    	this.segmentBuff[this.cursor] = CharacterUtil.regularize(this.segmentBuff[this.cursor]);
    	this.charTypes[this.cursor] = CharacterUtil.identifyCharType(this.segmentBuff[this.cursor]);
    }
    
    /**
     * ָ��+1
     * �ɹ����� true�� ָ���Ѿ�����buffβ��������ǰ��������false
     * ������ǰ�ַ�
     */
    boolean moveCursor(){
    	if(this.cursor < this.available - 1){
    		this.cursor++;
        	this.segmentBuff[this.cursor] = CharacterUtil.regularize(this.segmentBuff[this.cursor]);
        	this.charTypes[this.cursor] = CharacterUtil.identifyCharType(this.segmentBuff[this.cursor]);
    		return true;
    	}else{
    		return false;
    	}
    }
	
    /**
     * ���õ�ǰsegmentBuffΪ����״̬
     * ����ռ��segmentBuff���ӷִ������ƣ���ʾռ��segmentBuff
     * @param segmenterName
     */
	void lockBuffer(String segmenterName){
		this.buffLocker.add(segmenterName);
	}
	
	/**
	 * �Ƴ�ָ�����ӷִ��������ͷŶ�segmentBuff��ռ��
	 * @param segmenterName
	 */
	void unlockBuffer(String segmenterName){
		this.buffLocker.remove(segmenterName);
	}
	
	/**
	 * ֻҪbuffLocker�д���segmenterName
	 * ��buffer������
	 * @return boolean ����ȥ�Ƿ�����
	 */
	boolean isBufferLocked(){
		return this.buffLocker.size() > 0;
	}

	/**
	 * �жϵ�ǰsegmentBuff�Ƿ��Ѿ�����
	 * ��ǰִ��cursor����segmentBuffĩ��this.available - 1
	 * @return
	 */
	boolean isBufferConsumed(){
		return this.cursor == this.available - 1;
	}
	
	/**
	 * �ж�segmentBuff�Ƿ���Ҫ��ȡ������
	 * 
	 * ����һ������ʱ��
	 * 1.available == BUFF_SIZE ��ʾbuffer����
	 * 2.buffIndex < available - 1 && buffIndex > available - BUFF_EXHAUST_CRITICAL��ʾ��ǰָ�봦���ٽ�����
	 * 3.!context.isBufferLocked()��ʾû��segmenter��ռ��buffer
	 * Ҫ�жϵ�ǰѭ����bufferҪ������λ�����ٶ�ȡ���ݵĲ�����
	 * @return
	 */
	boolean needRefillBuffer(){
		return this.available == BUFF_SIZE 
			&& this.cursor < this.available - 1   
			&& this.cursor  > this.available - BUFF_EXHAUST_CRITICAL
			&& !this.isBufferLocked();
	}
	
	/**
	 * �ۼƵ�ǰ��segmentBuff�����reader��ʼλ�õ�λ��
	 */
	void markBufferOffset(){
		this.buffOffset += this.cursor;
	}
	
	/**
	 * ��ִʽ������Ӵ�Ԫ
	 * @param lexeme
	 */
	void addLexeme(Lexeme lexeme){
		this.orgLexemes.addLexeme(lexeme);
	}
	
	/**
	 * ��ӷִʽ��·��
	 * ·����ʼλ�� ---> ·�� ӳ���
	 * @param path
	 */
	void addLexemePath(LexemePath path){
		if(path != null){
			this.pathMap.put(path.getPathBegin(), path);
		}
	}
	
	
	/**
	 * ����ԭʼ�ִʽ��
	 * @return
	 */
	QuickSortSet getOrgLexemes(){
		return this.orgLexemes;
	}
	
	/**
	 * ���ͷִʽ�����������
	 * 1.��buffͷ��������this.cursor�Ѵ���λ��
	 * 2.��map�д��ڵķִʽ������results
	 * 3.��map�в����ڵ�CJDK�ַ��Ե��ַ�ʽ����results
	 */
	void outputToResult(){
		int index = 0;
		for( ; index <= this.cursor ;){
			//������CJK�ַ�
			if(CharacterUtil.CHAR_USELESS == this.charTypes[index]){
				index++;
				continue;
			}
			//��pathMap�ҳ���Ӧindexλ�õ�LexemePath
			LexemePath path = this.pathMap.get(index);
			if(path != null){
				//���LexemePath�е�lexeme��results����
				Lexeme l = path.pollFirst();
				while(l != null){
					this.results.add(l);
					//��index����lexeme��
					index = l.getBegin() + l.getLength();					
					l = path.pollFirst();
					if(l != null){
						//���path�ڲ�����Ԫ����©�ĵ���
						for(;index < l.getBegin();index++){
							this.outputSingleCJK(index);
						}
					}
				}
			}else{//pathMap���Ҳ���index��Ӧ��LexemePath
				//�������
				this.outputSingleCJK(index);
				index++;
			}
		}
		//��յ�ǰ��Map
		this.pathMap.clear();
	}
	
	/**
	 * ��CJK�ַ����е������
	 * @param index
	 */
	private void outputSingleCJK(int index){
		if(CharacterUtil.CHAR_CHINESE == this.charTypes[index]){			
			Lexeme singleCharLexeme = new Lexeme(this.buffOffset , index , 1 , Lexeme.TYPE_CNCHAR);
			this.results.add(singleCharLexeme);
		}else if(CharacterUtil.CHAR_OTHER_CJK == this.charTypes[index]){
			Lexeme singleCharLexeme = new Lexeme(this.buffOffset , index , 1 , Lexeme.TYPE_OTHER_CJK);
			this.results.add(singleCharLexeme);
		}
	}
		
	/**
	 * ����lexeme 
	 * 
	 * ͬʱ����ϲ�
	 * @return
	 */
	Lexeme getNextLexeme(){
		//�ӽ����ȡ�������Ƴ���һ��Lexme
		Lexeme result = this.results.pollFirst();
		while(result != null){
    		//�����ʺϲ�
    		this.compound(result);
    		if(Dictionary.getSingleton(this.cfg).isStopWord(this.segmentBuff ,  result.getBegin() , result.getLength())){
       			//��ֹͣ�ʼ���ȡ�б����һ��
    			result = this.results.pollFirst(); 				
    		}else{
	 			//����ֹͣ��, ����lexeme�Ĵ�Ԫ�ı�,���
	    		result.setLexemeText(String.valueOf(segmentBuff , result.getBegin() , result.getLength()));
	    		break;
    		}
		}
		return result;
	}
	
	/**
	 * ���÷ִ�������״̬
	 */
	void reset(){		
		this.buffLocker.clear();
        this.orgLexemes = new QuickSortSet();
        this.available =0;
        this.buffOffset = 0;
    	this.charTypes = new int[BUFF_SIZE];
    	this.cursor = 0;
    	this.results.clear();
    	this.segmentBuff = new char[BUFF_SIZE];
    	this.pathMap.clear();
	}
	
	/**
	 * ��ϴ�Ԫ
	 */
	private void compound(Lexeme result){
		if(!this.cfg.useSmart()){
			return ;
		}
   		//�����ʺϲ�����
		if(!this.results.isEmpty()){

			if(Lexeme.TYPE_ARABIC == result.getLexemeType()){
				Lexeme nextLexeme = this.results.peekFirst();
				boolean appendOk = false;
				if(Lexeme.TYPE_CNUM == nextLexeme.getLexemeType()){
					//�ϲ�Ӣ������+��������
					appendOk = result.append(nextLexeme, Lexeme.TYPE_CNUM);
				}else if(Lexeme.TYPE_COUNT == nextLexeme.getLexemeType()){
					//�ϲ�Ӣ������+��������
					appendOk = result.append(nextLexeme, Lexeme.TYPE_CQUAN);
				}
				if(appendOk){
					//����
					this.results.pollFirst(); 
				}
			}
			
			//���ܴ��ڵڶ��ֺϲ�
			if(Lexeme.TYPE_CNUM == result.getLexemeType() && !this.results.isEmpty()){
				Lexeme nextLexeme = this.results.peekFirst();
				boolean appendOk = false;
				 if(Lexeme.TYPE_COUNT == nextLexeme.getLexemeType()){
					 //�ϲ���������+��������
 					appendOk = result.append(nextLexeme, Lexeme.TYPE_CQUAN);
 				}  
				if(appendOk){
					//����
					this.results.pollFirst();   				
				}
			}

		}
	}
	
}
