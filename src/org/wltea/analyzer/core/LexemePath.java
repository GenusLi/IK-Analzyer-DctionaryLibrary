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
 * Lexeme����·����
 */
class LexemePath extends QuickSortSet implements Comparable<LexemePath>{
	
	//��ʼλ��
	private int pathBegin;
	//����
	private int pathEnd;
	//��Ԫ������Ч�ַ�����
	private int payloadLength;
	
	LexemePath(){
		this.pathBegin = -1;
		this.pathEnd = -1;
		this.payloadLength = 0;
	}

	/**
	 * ��LexemePath׷���ཻ��Lexeme
	 * @param lexeme
	 * @return 
	 */
	boolean addCrossLexeme(Lexeme lexeme){
		if(this.isEmpty()){
			this.addLexeme(lexeme);
			this.pathBegin = lexeme.getBegin();
			this.pathEnd = lexeme.getBegin() + lexeme.getLength();
			this.payloadLength += lexeme.getLength();
			return true;
			
		}else if(this.checkCross(lexeme)){
			this.addLexeme(lexeme);
			if(lexeme.getBegin() + lexeme.getLength() > this.pathEnd){
				this.pathEnd = lexeme.getBegin() + lexeme.getLength();
			}
			this.payloadLength = this.pathEnd - this.pathBegin;
			return true;
			
		}else{
			return  false;
			
		}
	}
	
	/**
	 * ��LexemePath׷�Ӳ��ཻ��Lexeme
	 * @param lexeme
	 * @return 
	 */
	boolean addNotCrossLexeme(Lexeme lexeme){
		if(this.isEmpty()){
			this.addLexeme(lexeme);
			this.pathBegin = lexeme.getBegin();
			this.pathEnd = lexeme.getBegin() + lexeme.getLength();
			this.payloadLength += lexeme.getLength();
			return true;
			
		}else if(this.checkCross(lexeme)){
			return  false;
			
		}else{
			this.addLexeme(lexeme);
			this.payloadLength += lexeme.getLength();
			Lexeme head = this.peekFirst();
			this.pathBegin = head.getBegin();
			Lexeme tail = this.peekLast();
			this.pathEnd = tail.getBegin() + tail.getLength();
			return true;
			
		}
	}
	
	/**
	 * �Ƴ�β����Lexeme
	 * @return
	 */
	Lexeme removeTail(){
		Lexeme tail = this.pollLast();
		if(this.isEmpty()){
			this.pathBegin = -1;
			this.pathEnd = -1;
			this.payloadLength = 0;			
		}else{		
			this.payloadLength -= tail.getLength();
			Lexeme newTail = this.peekLast();
			this.pathEnd = newTail.getBegin() + newTail.getLength();
		}
		return tail;
	}
	
	/**
	 * ����Ԫλ�ý��棨��������з֣�
	 * @param lexeme
	 * @return
	 */
	boolean checkCross(Lexeme lexeme){
		return (lexeme.getBegin() >= this.pathBegin && lexeme.getBegin() < this.pathEnd)
				|| (this.pathBegin >= lexeme.getBegin() && this.pathBegin < lexeme.getBegin()+ lexeme.getLength());
	}
	
	int getPathBegin() {
		return pathBegin;
	}

	int getPathEnd() {
		return pathEnd;
	}

	/**
	 * ��ȡPath����Ч�ʳ�
	 * @return
	 */
	int getPayloadLength(){
		return this.payloadLength;
	}
	
	/**
	 * ��ȡLexemePath��·������
	 * @return
	 */
	int getPathLength(){
		return this.pathEnd - this.pathBegin;
	}
	

	/**
	 * XȨ�أ���Ԫ���Ȼ���
	 * @return
	 */
	int getXWeight(){
		int product = 1;
		Cell c = this.getHead();
		while( c != null && c.getLexeme() != null){
			product *= c.getLexeme().getLength();
			c = c.getNext();
		}
		return product;
	}
	
	/**
	 * ��Ԫλ��Ȩ��
	 * @return
	 */
	int getPWeight(){
		int pWeight = 0;
		int p = 0;
		Cell c = this.getHead();
		while( c != null && c.getLexeme() != null){
			p++;
			pWeight += p * c.getLexeme().getLength() ;
			c = c.getNext();
		}
		return pWeight;		
	}
	
	LexemePath copy(){
		LexemePath theCopy = new LexemePath();
		theCopy.pathBegin = this.pathBegin;
		theCopy.pathEnd = this.pathEnd;
		theCopy.payloadLength = this.payloadLength;
		Cell c = this.getHead();
		while( c != null && c.getLexeme() != null){
			theCopy.addLexeme(c.getLexeme());
			c = c.getNext();
		}
		return theCopy;
	}

	public int compareTo(LexemePath o) {
		//�Ƚ���Ч�ı�����
		if(this.payloadLength > o.payloadLength){
			return -1;
		}else if(this.payloadLength < o.payloadLength){
			return 1;
		}else{
			//�Ƚϴ�Ԫ������Խ��Խ��
			if(this.size() < o.size()){
				return -1;
			}else if (this.size() > o.size()){
				return 1;
			}else{
				//·�����Խ��Խ��
				if(this.getPathLength() >  o.getPathLength()){
					return -1;
				}else if(this.getPathLength() <  o.getPathLength()){
					return 1;
				}else {
					//����ͳ��ѧ���ۣ������зָ��ʸ��������з֣����λ��Խ���������
					if(this.pathEnd > o.pathEnd){
						return -1;
					}else if(pathEnd < o.pathEnd){
						return 1;
					}else{
						//�ʳ�Խƽ��Խ��
						if(this.getXWeight() > o.getXWeight()){
							return -1;
						}else if(this.getXWeight() < o.getXWeight()){
							return 1;
						}else {
							//��Ԫλ��Ȩ�رȽ�
							if(this.getPWeight() > o.getPWeight()){
								return -1;
							}else if(this.getPWeight() < o.getPWeight()){
								return 1;
							}
							
						}
					}
				}
			}
		}
		return 0;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("pathBegin  : ").append(pathBegin).append("\r\n");
		sb.append("pathEnd  : ").append(pathEnd).append("\r\n");
		sb.append("payloadLength  : ").append(payloadLength).append("\r\n");
		Cell head = this.getHead();
		while(head != null){
			sb.append("lexeme : ").append(head.getLexeme()).append("\r\n");
			head = head.getNext();
		}
		return sb.toString();
	}

}
