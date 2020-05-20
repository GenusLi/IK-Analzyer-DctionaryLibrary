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
 * IK�ִ���ר�õ�Lexem�������򼯺�
 */
class QuickSortSet {
	//����ͷ
	private Cell head;
	//����β
	private Cell tail;
	//�����ʵ�ʴ�С
	private int size;
	
	QuickSortSet(){
		this.size = 0;
	}
	
	/**
	 * ����������Ӵ�Ԫ
	 * @param lexeme
	 */
	boolean addLexeme(Lexeme lexeme){
		Cell newCell = new Cell(lexeme); 
		if(this.size == 0){
			this.head = newCell;
			this.tail = newCell;
			this.size++;
			return true;
			
		}else{
			if(this.tail.compareTo(newCell) == 0){//��Ԫ��β����Ԫ��ͬ�������뼯��
				return false;
				
			}else if(this.tail.compareTo(newCell) < 0){//��Ԫ��������β��
				this.tail.next = newCell;
				newCell.prev = this.tail;
				this.tail = newCell;
				this.size++;
				return true;
				
			}else if(this.head.compareTo(newCell) > 0){//��Ԫ��������ͷ��
				this.head.prev = newCell;
				newCell.next = this.head;
				this.head = newCell;
				this.size++;
				return true;
				
			}else{					
				//��β������
				Cell index = this.tail;
				while(index != null && index.compareTo(newCell) > 0){
					index = index.prev;
				}
				if(index.compareTo(newCell) == 0){//��Ԫ�뼯���еĴ�Ԫ�ظ��������뼯��
					return false;
					
				}else if(index.compareTo(newCell) < 0){//��Ԫ���������е�ĳ��λ��
					newCell.prev = index;
					newCell.next = index.next;
					index.next.prev = newCell;
					index.next = newCell;
					this.size++;
					return true;					
				}
			}
		}
		return false;
	}
	
	/**
	 * ��������ͷ��Ԫ��
	 * @return
	 */
	Lexeme peekFirst(){
		if(this.head != null){
			return this.head.lexeme;
		}
		return null;
	}
	
	/**
	 * ȡ�������ϵĵ�һ��Ԫ��
	 * @return Lexeme
	 */
	Lexeme pollFirst(){
		if(this.size == 1){
			Lexeme first = this.head.lexeme;
			this.head = null;
			this.tail = null;
			this.size--;
			return first;
		}else if(this.size > 1){
			Lexeme first = this.head.lexeme;
			this.head = this.head.next;
			this.size --;
			return first;
		}else{
			return null;
		}
	}
	
	/**
	 * ��������β��Ԫ��
	 * @return
	 */
	Lexeme peekLast(){
		if(this.tail != null){
			return this.tail.lexeme;
		}
		return null;
	}
	
	/**
	 * ȡ�������ϵ����һ��Ԫ��
	 * @return Lexeme
	 */
	Lexeme pollLast(){
		if(this.size == 1){
			Lexeme last = this.head.lexeme;
			this.head = null;
			this.tail = null;
			this.size--;
			return last;
			
		}else if(this.size > 1){
			Lexeme last = this.tail.lexeme;
			this.tail = this.tail.prev;
			this.size--;
			return last;
			
		}else{
			return null;
		}
	}
	
	/**
	 * ���ؼ��ϴ�С
	 * @return
	 */
	int size(){
		return this.size;
	}
	
	/**
	 * �жϼ����Ƿ�Ϊ��
	 * @return
	 */
	boolean isEmpty(){
		return this.size == 0;
	}
	
	/**
	 * ����lexeme����ͷ��
	 * @return
	 */
	Cell getHead(){
		return this.head;
	}
	
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
	 * QuickSortSet���ϵ�Ԫ
	 * 
	 */
	class Cell implements Comparable<Cell>{
		private Cell prev;
		private Cell next;
		private Lexeme lexeme;
		
		Cell(Lexeme lexeme){
			if(lexeme == null){
				throw new IllegalArgumentException("lexeme must not be null");
			}
			this.lexeme = lexeme;
		}

		public int compareTo(Cell o) {
			return this.lexeme.compareTo(o.lexeme);
		}

		public Cell getPrev(){
			return this.prev;
		}
		
		public Cell getNext(){
			return this.next;
		}
		
		public Lexeme getLexeme(){
			return this.lexeme;
		}
	}
}
