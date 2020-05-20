package org.wltea.analyzer.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * Single Word Multi Char Query Builder
 * IK�ִ��㷨ר��
 * @author linliangyi
 *
 */
public class SWMCQueryBuilder {

	/**
	 * ����SWMCQuery
	 * @param fieldName
	 * @param keywords
	 * @param quickMode
	 * @return Lucene Query
	 */
	public static Query create(String fieldName ,String keywords , boolean quickMode){
		if(fieldName == null || keywords == null){
			throw new IllegalArgumentException("���� fieldName �� keywords ����Ϊnull.");
		}
		//1.��keywords���зִʴ���
		List<Lexeme> lexemes = doAnalyze(keywords);
		//2.���ݷִʽ��������SWMCQuery
		Query _SWMCQuery = getSWMCQuery(fieldName , lexemes , quickMode);
		return _SWMCQuery;
	}
	
	/**
	 * �ִ��з֣������ؽ�����
	 * @param keywords
	 * @return
	 */
	private static List<Lexeme> doAnalyze(String keywords){
		List<Lexeme> lexemes = new ArrayList<Lexeme>();
		IKSegmenter ikSeg = new IKSegmenter(new StringReader(keywords) , true);
		try{
			Lexeme l = null;
			while( (l = ikSeg.next()) != null){
				lexemes.add(l);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return lexemes;
	}
	
	
	/**
	 * ���ݷִʽ������SWMC����
	 * @param fieldName
	 * @param lexemes
	 * @param quickMode
	 * @return
	 */
	private static Query getSWMCQuery(String fieldName , List<Lexeme> lexemes , boolean quickMode){
		//����SWMC�Ĳ�ѯ���ʽ
		StringBuffer keywordBuffer = new StringBuffer();
		//�����SWMC�Ĳ�ѯ���ʽ
		StringBuffer keywordBuffer_Short = new StringBuffer();
		//��¼����Ԫ����
		int lastLexemeLength = 0;
		//��¼����Ԫ����λ��
		int lastLexemeEnd = -1;
		
		int shortCount = 0;
		int totalCount = 0;
		for(Lexeme l : lexemes){
			totalCount += l.getLength();
			//������ʽ
			if(l.getLength() > 1){
				keywordBuffer_Short.append(' ').append(l.getLexemeText());
				shortCount += l.getLength();
			}
			
			if(lastLexemeLength == 0){
				keywordBuffer.append(l.getLexemeText());				
			}else if(lastLexemeLength == 1 && l.getLength() == 1
					&& lastLexemeEnd == l.getBeginPosition()){//����λ�����ڣ�����Ϊһ���ϲ�)
				keywordBuffer.append(l.getLexemeText());
			}else{
				keywordBuffer.append(' ').append(l.getLexemeText());
				
			}
			lastLexemeLength = l.getLength();
			lastLexemeEnd = l.getEndPosition();
		}

		//����lucene queryparser ����SWMC Query
		QueryParser qp = new QueryParser(fieldName, new StandardAnalyzer());
		qp.setDefaultOperator(QueryParser.AND_OPERATOR);
		qp.setAutoGeneratePhraseQueries(true);
		
		if(quickMode && (shortCount * 1.0f / totalCount) > 0.5f){
			try {
				//System.out.println(keywordBuffer.toString());
				Query q = qp.parse(keywordBuffer_Short.toString());
				return q;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}else{
			if(keywordBuffer.length() > 0){
				try {
					//System.out.println(keywordBuffer.toString());
					Query q = qp.parse(keywordBuffer.toString());
					return q;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
