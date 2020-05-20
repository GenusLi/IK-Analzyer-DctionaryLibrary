package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.Serializable;
import java.util.Stack;

/**
 * Created by ������ on 2017/9/12.
 */
public class IKTokenFilter extends TokenFilter {

    private static final String SINGLE = "SINGLE";

    private boolean useSingle;// �Ƿ��Ӣ�ĺ����ֵ��ִַ�
    private boolean useItself;// �Ƿ���Ӣ�ĺ�����ԭ��㵥Ԫ

    private Stack<Pair<Character, Integer>> synonymStack;// ͬ��ʻ�����
    private AttributeSource.State current;// ��ǰ��㵥Ԫ״̬

    //��Ԫ�ı�����
    private final CharTermAttribute termAtt;
    //��Ԫλ������
    private final OffsetAttribute offsetAtt;
    //��Ԫ�������ԣ������Է���ο�org.wltea.analyzer.core.Lexeme�еķ��ೣ����
    private final TypeAttribute typeAtt;
    private final PositionIncrementAttribute posIncrAtt;

    /**
     * Construct a token stream filtering the given input.
     *
     * @param input
     * @param useSingle
     * @param useItself
     */
    public IKTokenFilter(TokenStream input, boolean useSingle, boolean useItself) {
        super(input);
        this.useSingle = useSingle;
        this.useItself = useItself;

        synonymStack = new Stack<>();

        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    }

    /**
     * һ��ֻ�����һ����㵥Ԫ
     *
     * @return ͬ�����㵥Ԫ
     * @throws IOException
     */
    public boolean incrementToken() throws IOException {

        if (synonymStack.size() > 0) {// ����������е�ͬ�����㵥Ԫ
            Pair<Character, Integer> currPair = synonymStack.pop();
            restoreState(current);
            termAtt.copyBuffer(new char[]{currPair.getKey()}, 0, 1);
            int startOffset = offsetAtt.startOffset() + currPair.getValue();
            offsetAtt.setOffset(startOffset, startOffset + 1);
            posIncrAtt.setPositionIncrement(1);
            typeAtt.setType(SINGLE);
            return true;
        }

        // ��ȡ��һ����㵥Ԫ
        // ���ʧ�ܣ�ֱ�ӷ���false
        if (!input.incrementToken())
            return false;

        if (!useSingle) {
            return false;
        }

        // ͬ�����ջ
        // ��ջ�ɹ����򱣴浱ǰ��㵥Ԫ״̬�����ں�����ͬ�����㵥Ԫ���
        if (addAliasesToStack()) {

            if (!useItself) {
                Pair<Character, Integer> currPair = synonymStack.pop();
                termAtt.copyBuffer(new char[]{currPair.getKey()}, 0, 1);
                int startOffset = offsetAtt.startOffset() + currPair.getValue();
                offsetAtt.setOffset(startOffset, startOffset + 1);
                posIncrAtt.setPositionIncrement(1);
                typeAtt.setType(SINGLE);
            }

            current = captureState();
        }
        return true;
    }

    private boolean addAliasesToStack() throws IOException {
        String type = typeAtt.type();
        // ����ΪӢ�Ļ�����
        if (type == null || (!type.equals("ENGLISH") && !type.equals("ARABIC"))) {
            return false;
        }

        final char[] synonyms = new String(termAtt.buffer(), 0, termAtt.length()).toCharArray();
        if (synonyms.length == 0) {
            return false;
        }

        for (int i = synonyms.length - 1; i >= 0; i--) {// ��ջ
            char synonym = synonyms[i];
            if (synonym != '\u0000') {
                synonymStack.push(new Pair<>(synonym, i));
            }
        }

        return true;
    }

    protected class Pair<K, V> implements Serializable {

        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
