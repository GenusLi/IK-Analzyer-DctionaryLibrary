package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.wltea.analyzer.cfg.Configuration;

/**
 * 自定义配置类分词器 遵循开闭原则
 */
public class IKAnalyzerExt extends Analyzer {

	private boolean useSingle;
	private boolean useItself;
	
	private Configuration config;
	
	/**
     * @param useSingle 是否针对英文和数字做单字切分
     * @param useItself 是否保留英文和数字原语汇单元
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
