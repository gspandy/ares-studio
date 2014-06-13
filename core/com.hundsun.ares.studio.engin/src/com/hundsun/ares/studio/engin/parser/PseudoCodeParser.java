/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.engin.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.token.CodeTokenFactory;
import com.hundsun.ares.studio.engin.token.ICodeToken;

/**
 * @author qinyuan
 *
 */
public class PseudoCodeParser implements IPseudocodeParser{

	public static final int CPP_TYPE = 0;//ʹ��cppע��
	public static final int PROC_TYPE = 1;//ʹ��procע��
	
	/**
	 * ����ע�ͱ��
	 */
	public static String functionRemark = "//";

	/** C��ע��������ʽ */
	public static Pattern CPP_COMMENT_PATTERN = Pattern.compile("(//[^\\r\\n]+((\\r?\\n)|$))|(/\\*.*?\\*/)", Pattern.DOTALL);
	
	/** plsql��ע��������ʽ */
	public static Pattern PROC_COMMENT_PATTERN = Pattern.compile("(--[^\\r\\n]+((\\r?\\n)|$))|(/\\*.*?\\*/)", Pattern.DOTALL);
	
	/** C���ַ�������ʽ */
	//Pattern.compile("\"((\\\\\")|([^\"]))*\"") ԭ����ƥ�䳤�ַ�����ʱ��Ỻ�������
	public static Pattern CPP_STRING_PATTERN = Pattern.compile("\"[^\"]*\"");
	
	/** plsql���ַ�������ʽ */
	public static Pattern PROC_STRING_PATTERN = Pattern.compile("'[^']*'");
	

	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.uft.engin.parser.IPseudocodeParser#parse(java.lang.String, java.util.Map)
	 */
	@Override
	public Iterator<ICodeToken> parse(String pseudocode,int commentType,
			Map<Object, Object> context) throws Exception {
		if(commentType == PROC_TYPE) {
			return parseEx(pseudocode, PROC_COMMENT_PATTERN, PROC_STRING_PATTERN,context);
		}else {
			return parseEx(pseudocode, CPP_COMMENT_PATTERN, CPP_STRING_PATTERN,context);
		}
	}

	/**
	 * 
	 * �ֽ�Դ����Ϊ�ꡢ�ı���ע�͡��ַ���4������
	 * ��Ŀǰ��Ҫ��һ�е����ף���Ȼǰ������пհ��ַ�����tab space 
	 * 
	 * @param pseudocode α����
	 * @param pComment �����ʹ���ʹ�õ�ע�����͵�����ʽ
	 * @param pString �����ʹ���ʹ�õ��ַ���������ʽ
	 * @return
	 */
	public static Iterator<ICodeToken> parseEx(String pseudocode, Pattern pComment, Pattern pString,Map<Object, Object> context) {

		List<Pattern> patterns = new ArrayList<Pattern>();
		Pattern patternComment = pComment; //
		// һ�п�ͷ�������пհ��ַ��������б�־λ��[]���ݵ�1������
		Pattern patternMacro = Pattern.compile("(^[ \\t]*(<[A-Za-z]+>)?(\\[(('[^']*')|(\"((\\\\\")|([^\"]))*\")|([^\\]\\[]*))\\])+)", Pattern.MULTILINE);
		
		patterns.add(patternComment);
		patterns.add(pString);
		patterns.add(patternMacro);

		List<SourcePartition> patitions = new ArrayList<SourcePartition>();
		doPartition(0, 0, pseudocode, patterns, patitions);

		List<ICodeToken> codes = new ArrayList<ICodeToken>();
		if(!context.containsKey(IEngineContextConstant.PSEUDO_CODE_PARA_LIST)){
			context.put(IEngineContextConstant.PSEUDO_CODE_PARA_LIST, new ArrayList<String>(){
				/* (non-Javadoc)
				 * @see java.util.ArrayList#add(java.lang.Object)
				 */
				@Override
				public boolean add(String arg0) {
					if(!contains(arg0)){
						return super.add(arg0);
					}
					return false;
				}
			});
		}
		//α�������õ��Ķ���α�����б�
		if(!context.containsKey(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST)){
			context.put(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST, new ArrayList<String>(){
				/* (non-Javadoc)
				 * @see java.util.ArrayList#add(java.lang.Object)
				 */
				@Override
				public boolean add(String arg0) {
					if(!contains(arg0)){
						return super.add(arg0);
					}
					return false;
				}
			});
		}

		for (SourcePartition sourcePartition : patitions) {
			ICodeToken code = null;
			String sourceText = sourcePartition.getText();
			List<String> paramList = new ArrayList<String>();
			boolean findResultSetStr = false;
			
			if (!patternComment.equals(sourcePartition.getPattern()) &&
					!pString.equals(sourcePartition.getPattern())){//�ַ�����Ҳ��Ӧ�����
				//ע�Ͳ��ý��ͱ�׼�ֶ�
				List<String> PseudoParaList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
				List<String> pseudoObjectParaList = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_OBJECT_PARA_LIST);
				for (String fld : CodeParserUtil.findStandardField(sourceText)) {
					paramList.add(fld);
					
					//�Ѵ����˾Ͳ����
					if(!StringUtils.endsWith(fld, "ResultSet")){
						PseudoParaList.add(fld);
					}else{
						if(StringUtils.endsWith(fld, "ResultSet")){
							int index = StringUtils.lastIndexOf(fld, "ResultSet");
							String relName = StringUtils.substring(fld, 0,index);
							pseudoObjectParaList.add(relName);
						}
					}
				}
				//�ı��д���ResultSet->
				findResultSetStr = CodeParserUtil.findResultSetStr(sourceText);
			}
			
			if (sourcePartition.getPattern() == null) {
				//ResultSet->�϶�������Proc������ʹ�ã��ʿ϶�����Ҫ��TextWithParamsToken
				if(findResultSetStr){
					code = CodeTokenFactory.getInstance().createResultSetToken(sourceText);
				}else if(paramList.isEmpty()){// ��ͨ�ı�
					code = CodeTokenFactory.getInstance().createCodeTextToken(sourceText);
				}else{
					code = CodeTokenFactory.getInstance().createTextWithParamsToken(sourceText, paramList);
				}
				codes.add(code);
			} else if (sourcePartition.getPattern().equals(patternComment)) {
				// ע�� 
				code = CodeTokenFactory.getInstance().createCommentToken(sourceText);
				codes.add(code);
			} else if (sourcePartition.getPattern().equals(pString)) {
				// �ַ���
//				if(paramList.isEmpty()){
//					code = CodeTokenFactory.getInstance().createCodeTextToken(sourceText);
//				}else{
//					code = CodeTokenFactory.getInstance().createTextWithParamsToken(sourceText, paramList);
//				}
				code = CodeTokenFactory.getInstance().createStringToken(sourceText);
				codes.add(code);
			} else if (sourcePartition.getPattern().equals(patternMacro)) {
				// ��
				code = CodeTokenFactory.getInstance().createMacroToken(sourceText,sourcePartition.getLineNum());
				codes.add(code);
			}
		}

		return codes.iterator();
	}

	/**
	 * ��ȡ����
	 * @param originalText
	 * @return
	 */
	private static String getMacroID(String originalText) {
		String keyword;
		String trim = originalText.trim();
		if(trim.isEmpty()){
			return "";
		}
		String s[] = trim.substring(trim.indexOf("[") + 1, trim.length() - 1).split("\\]\\[");	
		keyword = s[0];

		return keyword;
	}
	
	/**
	 * ���ݸ�����������ʽ��ֽ��ַ��������������˳�������ڲ���List��˳����
	 * 
	 * @param nStartLine
	 * @param source
	 * @param patterns
	 * @param patitions
	 */
	protected static void doPartition(int nStartLine, int nStart, String source, List<Pattern> patterns, List<SourcePartition> patitions) {
		patitions.addAll(doPartition(source, patterns));
	}
	
	protected static List<SourcePartition> doPartition(String source, List<Pattern> patterns) {
		int pos = 0;
		int nStartLine = 0;
		int length = source.length();
		List<SourcePartition> partitions = new ArrayList<SourcePartition>();
		
		while (pos < length) {
			String subSource = source.substring(pos);
			
			Matcher m = getFirstMatch(subSource, patterns);
			if (m == null) {
				partitions.add(new SourcePartition(subSource, nStartLine, pos, length - pos, null));
				break;
			} else {
				MatchResult result = m.toMatchResult();
				if (result.start() == 0) {
					String matchText = result.group();
					partitions.add(new SourcePartition(matchText, nStartLine, pos, result.end() - result.start(), m.pattern()));
					nStartLine += getLFCount(matchText);
				} else {
					// ǰ��δƥ��Ĳ���
					String notMatchText = subSource.substring(0, result.start());
					partitions.add(new SourcePartition(notMatchText, nStartLine, pos, pos + result.start() - 1, null));

					// ����ĿǰӦ��Ϊ�ڼ���
					nStartLine += getLFCount(notMatchText);
					String matchText = result.group();
					partitions.add(new SourcePartition(matchText, nStartLine, pos + result.start(),  
							result.end() - result.start(), m.pattern()));
					nStartLine += getLFCount(matchText);
				}

				pos += result.end();
			}
		}
		
		return partitions;
	}
	/**
	 * ���֮�������
	 * 
	 * @param content
	 * @return
	 */
	private static int getLFCount(String content) {
		
		//2011��8��3��15:26:57 ���������ʹ�ã�����ƥ���ַ����࣬��ջ�����
		int count = 0;
		for (char c : content.toCharArray()) {
			if (c == '\n') {
				count++;
			}
		}
		
		return count;
	}

	/**
	 * �������������ƥ��,ȡ������ƥ���ƥ����
	 * 
	 * @param source
	 * @param patterns
	 * @return
	 */
	private static Matcher getFirstMatch(String source, List<Pattern> patterns) {
		Matcher result = null;
		int i = 0;

		// ���ҵ�һ��ƥ���
		for (; i < patterns.size(); i++) {
			Matcher m = patterns.get(i).matcher(source);
			if (m.find()) {
				result = m;
				break;
			}
		}

		// ����һ������ƥ���ƥ����
		for (; i < patterns.size(); i++) {
			Matcher m = patterns.get(i).matcher(source);
			if (m.find()) {
				if (m.start() < result.start()) {
					result = m;
				}
			}
		}

		return result;
	}

//	public static List<String> parserVariables(String pseudocode) {
//		
//		// ���ȷֽ��ע��
//		List<ICodeToken> codes = parseEx(pseudocode);
//		List<String> ret = new LinkedList<String>();
//		
//		for (ICodeToken code : codes) {
//			if (code instanceof TextCode) {
//				Pattern p = Pattern.compile("@[\\w\\d_]+");
//				Matcher m = p.matcher(code.toString());
//				while (m.find()) {
//					ret.add(m.group().substring(1));
//				}
//			}
//		}
//		return ret;
//	}
	
	public static void main(String[] args) {
		
		// ��ȡ�ļ�
		StringBuffer trueCode = new StringBuffer();
		try {
			
			FileReader fr = new FileReader(new File("D:\\test\\test.txt"));
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			while (line != null) {
				trueCode.append(line);
				line = br.readLine();
				if (line != null) {
					trueCode.append("\n");
				}
			}
			br.close();
			fr.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("//////////Դ�ļ�///////////////////");
		System.out.println(trueCode.toString());
		System.out.println("//////////�������ļ�/////////////////////");
		Map<Object, Object> context = new HashMap<Object, Object>();
//		Iterator<ICodeToken> parseredCode = parseEx(trueCode.toString(), CPP_COMMENT_PATTERN, CPP_STRING_PATTERN,context);
//		int i = 1;
//		while (parseredCode.hasNext()) {
//			ICodeToken t = parseredCode.next();
//			System.out.println("��" + i +"��������: \n");
//			System.out.println(t.getContent());
//			i++;
//		}
//		List<String> stdflds = (List<String>)context.get(IEngineContextConstant.PSEUDO_CODE_PARA_LIST);
//		System.out.println(stdflds);
		
		Map<String, String> ret = parserKeyValue(trueCode.toString());
		for (String string : ret.keySet()) {
			System.out.println("key is :" + string+", value is :" + ret.get(string));
		}
	}
	
	/**
	 * �������������
	 * @param s
	 * @return
	 */
	public static Map<String, String> parserKeyValue(String s) {
		Map<String, String> ret = new Hashtable<String, String>();
		Pattern p = Pattern.compile("(@?[\\w\\d_]+(\\s*)=(\\s*)@?(('.*?')|([^,]*)))");
		Pattern p2 = Pattern.compile("(@?(\\s)*=@?(\\s*))");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String result = m.group();
			Matcher m2 = p2.matcher(result);
			m2.find();
			ret.put(result.substring(0, m2.start()), result.substring(m2.end()));
		}
		return ret;
	}

	/**
	 * �������������
	 * �ֽ�ʱ����value��@����
	 * 
	 */
	public static Map<String, String> parserKeyValueWithAt(String s) {
		Map<String, String> ret = new Hashtable<String, String>();
		Pattern p = Pattern.compile("(@?[\\w\\d_]+(\\s*)=(\\s*)@?(('.*?')|(\".*?\")|([^,]*)))");
		//("(@?[\\w\\d_]+(\\s*)=(\\s*)@?(('.*?')|([^,]*)))");
		Pattern p2 = Pattern.compile("((\\s)*=(\\s*))");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String result = m.group();
			Matcher m2 = p2.matcher(result);
			m2.find();
			ret.put(result.substring(0, m2.start()).trim(), result.substring(m2.end()).trim());
		}
		return ret;
	}
	/**
	 * ����ע��
	 * @param str
	 * @param replace
	 * @return
	 */
	public static String insertCommonForSql(String str,String replace){
		replace = "/*"+replace + "*/";
		return str.replaceAll("\\bselect\\b", "select"+replace)
		.replaceAll("\\bSELECT\\b", "SELECT"+replace)
		.replaceAll("\\bupdate\\b", "update"+replace)
		.replaceAll("\\bUPDATE\\b", "UPDATE"+replace)
		.replaceAll("\\binsert into\\b", "insert into"+replace)
		.replaceAll("\\bINSERT INTO\\b", "INSERT INTO"+replace)
		.replaceAll("\\bdelete\\b", "delete"+replace)
		.replaceAll("\\bDELETE\\b", "DELETE"+replace);
	}

}
