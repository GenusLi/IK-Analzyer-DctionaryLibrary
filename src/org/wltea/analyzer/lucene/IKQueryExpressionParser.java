package org.wltea.analyzer.lucene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.BytesRef;

/**
 * IK���ײ�ѯ���ʽ���� 
 * ���SWMCQuery�㷨
 * 
 * ���ʽ���� ��
 * (id='1231231' && title:'monkey') || (content:'�����'  || ulr='www.ik.com') - name:'helloword'
 * @author linliangyi
 *
 */
public class IKQueryExpressionParser {
	
	//public static final String LUCENE_SPECIAL_CHAR = "&&||-()':={}[],";
	
	private List<Element> elements = new ArrayList<Element>();
	
	private Stack<Query> querys =  new Stack<Query>();
	
	private Stack<Element> operates = new Stack<Element>();
	
	/**
	 * ������ѯ���ʽ������Lucene Query����
	 * 
	 * @param expression
	 * @param quickMode 
	 * @return Lucene query
	 */
	public Query parseExp(String expression , boolean quickMode){
		Query lucenceQuery = null;
		if(expression != null && !"".equals(expression.trim())){
			try{
				//�ķ�����
				this.splitElements(expression);
				//�﷨����
				this.parseSyntax(quickMode);
				if(this.querys.size() == 1){
					lucenceQuery = this.querys.pop();
				}else{
					throw new IllegalStateException("���ʽ�쳣�� ȱ���߼������� �� ����ȱʧ");
				}
			}finally{
				elements.clear();
				querys.clear();
				operates.clear();
			}
		}
		return lucenceQuery;
	}	
	
	/**
	 * ���ʽ�ķ�����
	 * @param expression
	 */
	private void splitElements(String expression){
 		
		if(expression == null){
			return;
		}
		Element curretElement = null;
		
		char[] expChars = expression.toCharArray();
		for(int i = 0 ; i < expChars.length ; i++){
			switch(expChars[i]){
			case '&' :
				if(curretElement == null){
					curretElement = new Element();
					curretElement.type = '&';
					curretElement.append(expChars[i]);
				}else if(curretElement.type == '&'){
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;
				}else if(curretElement.type == '\''){
					curretElement.append(expChars[i]);
				}else {
					this.elements.add(curretElement);
					curretElement = new Element();
					curretElement.type = '&';
					curretElement.append(expChars[i]);
				}
				break;
				
			case '|' :
				if(curretElement == null){
					curretElement = new Element();
					curretElement.type = '|';
					curretElement.append(expChars[i]);
				}else if(curretElement.type == '|'){
					curretElement.append(expChars[i]);
					this.elements.add(curretElement);
					curretElement = null;
				}else if(curretElement.type == '\''){
					curretElement.append(expChars[i]);
				}else {
					this.elements.add(curretElement);
					curretElement = new Element();
					curretElement.type = '|';
					curretElement.append(expChars[i]);
				}				
				break;
				
			case '-' :
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = '-';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;			
				break;

			case '(' :
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = '(';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;			
				break;				

			case ')' :
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = ')';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;			
				break;					

			case ':' :
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = ':';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;			
				break;	
			
			case '=' :
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = '=';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;			
				break;					

			case ' ' :
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
					}else{
						this.elements.add(curretElement);
						curretElement = null;
					}
				}
				
				break;
			
			case '\'' :
				if(curretElement == null){
					curretElement = new Element();
					curretElement.type = '\'';
					
				}else if(curretElement.type == '\''){
					this.elements.add(curretElement);
					curretElement = null;
					
				}else{
					this.elements.add(curretElement);
					curretElement = new Element();
					curretElement.type = '\'';
					
				}
				break;
				
			case '[':
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = '[';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;					
				break;
				
			case ']':
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = ']';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;
				
				break;
				
			case '{':
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = '{';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;					
				break;
				
			case '}':
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = '}';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;
				
				break;
			case ',':
				if(curretElement != null){
					if(curretElement.type == '\''){
						curretElement.append(expChars[i]);
						continue;
					}else{
						this.elements.add(curretElement);
					}
				}
				curretElement = new Element();
				curretElement.type = ',';
				curretElement.append(expChars[i]);
				this.elements.add(curretElement);
				curretElement = null;
				
				break;
				
			default :
				if(curretElement == null){
					curretElement = new Element();
					curretElement.type = 'F';
					curretElement.append(expChars[i]);
					
				}else if(curretElement.type == 'F'){
					curretElement.append(expChars[i]);
					
				}else if(curretElement.type == '\''){
					curretElement.append(expChars[i]);

				}else{
					this.elements.add(curretElement);
					curretElement = new Element();
					curretElement.type = 'F';
					curretElement.append(expChars[i]);
				}			
			}
		}
		
		if(curretElement != null){
			this.elements.add(curretElement);
			curretElement = null;
		}
	}
		
	/**
	 * �﷨����
	 * 
	 */
	private void parseSyntax(boolean quickMode){
		for(int i = 0 ; i < this.elements.size() ; i++){
			Element e = this.elements.get(i);
			if('F' == e.type){
				Element e2 = this.elements.get(i + 1);
				if('=' != e2.type && ':' != e2.type){
					throw new IllegalStateException("���ʽ�쳣�� = �� �� �Ŷ�ʧ");
				}
				Element e3 = this.elements.get(i + 2);
				//���� = �� �� ����
				if('\'' == e3.type){
					i+=2;
					if('=' == e2.type){
						TermQuery tQuery = new TermQuery(new Term(e.toString() , e3.toString()));
						this.querys.push(tQuery);
					}else if(':' == e2.type){
						String keyword = e3.toString();
						//SWMCQuery Here
						Query _SWMCQuery =  SWMCQueryBuilder.create(e.toString(), keyword , quickMode);
						this.querys.push(_SWMCQuery);
					}
					
				}else if('[' == e3.type || '{' == e3.type){
					i+=2;
					//���� [] �� {}
					LinkedList<Element> eQueue = new LinkedList<Element>();
					eQueue.add(e3);
					for( i++ ; i < this.elements.size() ; i++){							
						Element eN = this.elements.get(i);
						eQueue.add(eN);
						if(']' == eN.type || '}' == eN.type){
							break;
						}
					}
					//����RangeQuery
					Query rangeQuery = this.toTermRangeQuery(e , eQueue);
					this.querys.push(rangeQuery);
				}else{
					throw new IllegalStateException("���ʽ�쳣��ƥ��ֵ��ʧ");
				}
				
			}else if('(' == e.type){
				this.operates.push(e);
				
			}else if(')' == e.type){
				boolean doPop = true;
				while(doPop && !this.operates.empty()){
					Element op = this.operates.pop();
					if('(' == op.type){
						doPop = false;
					}else {
						Query q = toBooleanQuery(op);
						this.querys.push(q);
					}
					
				}
			}else{ 
				
				if(this.operates.isEmpty()){
					this.operates.push(e);
				}else{
					boolean doPeek = true;
					while(doPeek && !this.operates.isEmpty()){
						Element eleOnTop = this.operates.peek();
						if('(' == eleOnTop.type){
							doPeek = false;
							this.operates.push(e);
						}else if(compare(e , eleOnTop) == 1){
							this.operates.push(e);
							doPeek = false;
						}else if(compare(e , eleOnTop) == 0){
							Query q = toBooleanQuery(eleOnTop);
							this.operates.pop();
							this.querys.push(q);
						}else{
							Query q = toBooleanQuery(eleOnTop);
							this.operates.pop();
							this.querys.push(q);
						}
					}
					
					if(doPeek && this.operates.empty()){
						this.operates.push(e);
					}
				}
			}			
		}
		
		while(!this.operates.isEmpty()){
			Element eleOnTop = this.operates.pop();
			Query q = toBooleanQuery(eleOnTop);
			this.querys.push(q);			
		}		
	}

	/**
	 * �����߼�������������BooleanQuery
	 * @param op
	 * @return
	 */
	private Query toBooleanQuery(Element op){
		if(this.querys.size() == 0){
			return null;
		}

		BooleanQuery.Builder builder = new BooleanQuery.Builder();

		if(this.querys.size() == 1){
			return this.querys.get(0);
		}
		
		Query q2 = this.querys.pop();
		Query q1 = this.querys.pop();
		if('&' == op.type){
			if(q1 != null){
				if(q1 instanceof BooleanQuery){
					BooleanClause[] clauses = ((BooleanQuery)q1).clauses().toArray(new BooleanClause[0]);
					if(clauses.length > 0 
							&& clauses[0].getOccur() == Occur.MUST){
						for(BooleanClause c : clauses){
							builder.add(c);
						}					
					}else{
						builder.add(q1,Occur.MUST);
					}

				}else{
					//q1 instanceof TermQuery 
					//q1 instanceof TermRangeQuery 
					//q1 instanceof PhraseQuery
					//others
					builder.add(q1,Occur.MUST);
				}
			}
			
			if(q2 != null){
				if(q2 instanceof BooleanQuery){
					BooleanClause[] clauses = ((BooleanQuery) q2).clauses().toArray(new BooleanClause[0]);
					if(clauses.length > 0
							&& clauses[0].getOccur() == Occur.MUST){
						for(BooleanClause c : clauses){
							builder.add(c);
						}					
					}else{
						builder.add(q2,Occur.MUST);
					}
					
				}else{
					//q1 instanceof TermQuery 
					//q1 instanceof TermRangeQuery 
					//q1 instanceof PhraseQuery
					//others
					builder.add(q2,Occur.MUST);
				}
			}
			
		}else if('|' == op.type){
			if(q1 != null){
				if(q1 instanceof BooleanQuery){
					BooleanClause[] clauses = ((BooleanQuery)q1).clauses().toArray(new BooleanClause[0]);
					if(clauses.length > 0 
							&& clauses[0].getOccur() == Occur.SHOULD){
						for(BooleanClause c : clauses){
							builder.add(c);
						}					
					}else{
						builder.add(q1,Occur.SHOULD);
					}
					
				}else{
					//q1 instanceof TermQuery 
					//q1 instanceof TermRangeQuery 
					//q1 instanceof PhraseQuery
					//others
					builder.add(q1,Occur.SHOULD);
				}
			}
			
			if(q2 != null){
				if(q2 instanceof BooleanQuery){
					BooleanClause[] clauses = ((BooleanQuery)q2).clauses().toArray(new BooleanClause[0]);
					if(clauses.length > 0 
							&& clauses[0].getOccur() == Occur.SHOULD){
						for(BooleanClause c : clauses){
							builder.add(c);
						}					
					}else{
						builder.add(q2,Occur.SHOULD);
					}
				}else{
					//q2 instanceof TermQuery 
					//q2 instanceof TermRangeQuery 
					//q2 instanceof PhraseQuery
					//others
					builder.add(q2,Occur.SHOULD);
					
				}
			}
			
		}else if('-' == op.type){
			if(q1 == null || q2 == null){
				throw new IllegalStateException("���ʽ�쳣��SubQuery ������ƥ��");
			}
			
			if(q1 instanceof BooleanQuery){
				BooleanClause[] clauses = ((BooleanQuery)q1).clauses().toArray(new BooleanClause[0]);
				if(clauses.length > 0){
					for(BooleanClause c : clauses){
						builder.add(c);
					}					
				}else{
					builder.add(q1,Occur.MUST);
				}

			}else{
				//q1 instanceof TermQuery 
				//q1 instanceof TermRangeQuery 
				//q1 instanceof PhraseQuery
				//others
				builder.add(q1,Occur.MUST);
			}

			builder.add(q2,Occur.MUST_NOT);
		}
		return builder.build();
	}	
	
	/**
	 * ��װTermRangeQuery
	 * @param elements
	 * @return
	 */
	private TermRangeQuery toTermRangeQuery(Element fieldNameEle , LinkedList<Element> elements){

		boolean includeFirst = false;
		boolean includeLast = false;
		String firstValue = null;
		String lastValue = null;
		//����һ��Ԫ���Ƿ���[����{
		Element first = elements.getFirst();
		if('[' == first.type){
			includeFirst = true;
		}else if('{' == first.type){
			includeFirst = false;
		}else {
			throw new IllegalStateException("���ʽ�쳣");
		}
		//������һ��Ԫ���Ƿ���]����}
		Element last = elements.getLast();
		if(']' == last.type){
			includeLast = true;
		}else if('}' == last.type){
			includeLast = false;
		}else {
			throw new IllegalStateException("���ʽ�쳣, RangeQueryȱ�ٽ�������");
		}
		if(elements.size() < 4 || elements.size() > 5){
			throw new IllegalStateException("���ʽ�쳣, RangeQuery ����");
		}			
		//�����м䲿��
		Element e2 = elements.get(1);
		if('\'' == e2.type){
			firstValue = e2.toString();
			//
			Element e3 = elements.get(2);
			if(',' != e3.type){
				throw new IllegalStateException("���ʽ�쳣, RangeQueryȱ�ٶ��ŷָ�");
			}
			//
			Element e4 = elements.get(3);
			if('\'' == e4.type){
				lastValue = e4.toString();
			}else if(e4 != last){
				throw new IllegalStateException("���ʽ�쳣��RangeQuery��ʽ����");
			}				
		}else if(',' == e2.type){
			firstValue = null;
			//
			Element e3 = elements.get(2);
			if('\'' == e3.type){
				lastValue = e3.toString();
			}else{
				throw new IllegalStateException("���ʽ�쳣��RangeQuery��ʽ����");
			}
			
		}else {
			throw new IllegalStateException("���ʽ�쳣, RangeQuery��ʽ����");
		}
		
		return new TermRangeQuery(fieldNameEle.toString() , new BytesRef(firstValue) , new BytesRef(lastValue) , includeFirst , includeLast);
	}	
	
	/**
	 * �Ƚϲ��������ȼ�
	 * @param e1
	 * @param e2
	 * @return
	 */
	private int compare(Element e1 , Element e2){
		if('&' == e1.type){
			if('&' == e2.type){
				return 0;
			}else {
				return 1;
			}
		}else if('|' == e1.type){
			if('&' == e2.type){
				return -1;
			}else if('|' == e2.type){
				return 0;
			}else{
				return 1;
			}
		}else{
			if('-' == e2.type){
				return 0;
			}else{
				return -1;
			}
		}
	}
	
	/**
	 * ���ʽԪ�أ���������FieldName��FieldValue��
	 * @author linliangyi
	 * May 20, 2010
	 */
	private class Element{
		char type = 0;
		StringBuffer eleTextBuff;

		public Element(){
			eleTextBuff = new StringBuffer();
		}
		
		public void append(char c){
			this.eleTextBuff.append(c);
		}
	
		public String toString(){
			return this.eleTextBuff.toString();
		}
	}	

	public static void main(String[] args){
		IKQueryExpressionParser parser = new IKQueryExpressionParser();
		//String ikQueryExp = "newsTitle:'�����ħ�����硷���Bigfoot���¹ⱦ��'";
		String ikQueryExp = "(id='ABcdRf' && date:{'20010101','20110101'} && keyword:'ħ���й�') || (content:'KSHT-KSH-A001-18'  || ulr='www.ik.com') - name:'������'";
		Query result = parser.parseExp(ikQueryExp , true);
		System.out.println(result);

	}	
	
}
