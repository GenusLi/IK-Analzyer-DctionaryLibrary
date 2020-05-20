package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.wltea.analyzer.cfg.Configuration;

/**
 * �Զ���������ִ��� ��ѭ����ԭ��
 */
public class IKAnalyzerExt extends Analyzer {

	private boolean useSingle;
	private boolean useItself;
	
	private Configuration config;
	
	/**
     * @param useSingle �Ƿ����Ӣ�ĺ������������з�
     * @param useItself �Ƿ���Ӣ�ĺ�����ԭ��㵥Ԫ
	 */
	public IKAnalyzerExt(Configuration config,boolean useSingle,boolean useItself) {
		this.config=config;
		this.useItself=useItself;
		this.useSingle=useSingle;
	}
	
	public IKAnalyzerExt(Configuration config) {
		this(config,false,false);
	}
	
	@Override
	protected TokenStreamComponents createComponents(String arg0) {
		Tokenizer _IKTokenizer = new IKTokenizer(config);
		IKTokenFilter _IKTokenFilter = new IKTokenFilter(_IKTokenizer, useSingle, useItself);
		return new TokenStreamComponents(_IKTokenizer, _IKTokenFilter);
	}

}
