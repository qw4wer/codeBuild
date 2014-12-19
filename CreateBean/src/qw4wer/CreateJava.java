package qw4wer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 反射创建java文件
 * 
 * @author qw4wer
 * 
 */
public class CreateJava {

	private String fileName;

	private String subPath;

	private Map<String, String> fields;

	private File file;

	private FileWriter fileWriter;

	private static final String head = "package com.qw4wer.bilibili.entity;";

	private static Set<Map.Entry<String, String>> attr;

	public CreateJava(String fileName, String subPath,
			Map<String, String> fields) throws IOException {
		this.fileName = fileName;
		this.subPath = subPath;
		this.fields = fields;
		attr = fields.entrySet();
		file = new File(System.getProperty("user.dir").replace("\\", "/")
				+ File.separator + subPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file.getAbsoluteFile() + File.separator + fileName);
		if (file.exists()) {
			file.delete();
		}
		fileWriter = new FileWriter(System.getProperty("user.dir").replace(
				"\\", "/")
				+ File.separator
				+ subPath
				+ File.separator
				+ fileName
				+ ".java");
		create();
		fileWriter.flush();
		fileWriter.close();
	}

	private void create() throws IOException {
		fileWriter.append(head + "\r\n\r\n");
		fileWriter.append(getImports());
		attr = EnumMysqlColunmType.getFields(fields).entrySet();
		fileWriter.append("public class " + fileName + " {\r\n");
		processAllAttr();
		fileWriter.append("\r\n");
		processAllMethod();
		fileWriter.append("}\r\n");
	}
	
	public StringBuilder createString() throws Exception{
		StringBuilder builder = new StringBuilder();
		builder.append("public class " + fileName + " {\r\n");
		
		builder.append(createDefaultConstructos());
		builder.append(processAllAttrString());
		builder.append("\r\n");
		builder.append(createConstructor());
		builder.append(processAllMethodString());
		builder.append("}\r\n");
		return builder;
	}
	//生成构造器
	private String createDefaultConstructos(){
		StringBuilder builder = new StringBuilder();
		builder.append("public "+fileName+" (){}\r\n");
		return builder.toString();
	}
	
	private String createConstructor(){
		StringBuilder builder = new StringBuilder();
		builder.append("public "+fileName+" (");
		int n = 0;
		for (Entry<String, String> entry : attr) {
			n++;
			builder.append(entry.getValue()+" "+entry.getKey());
			if(n<attr.size()){
				builder.append(",");
			}
		}
		builder.append("){\r\n");
		for (Entry<String, String> entry : attr) {
			builder.append("\tthis."+entry.getKey()+"="+entry.getKey()+";\r\n");
		}
		
		builder.append("}\r\n\n");
		return builder.toString();
	}
//生成字段
	private void processAllAttr() throws IOException {
		for (Entry<String, String> entry : attr) {
			fileWriter.append("\tprivate " + entry.getValue() + " "
					+ entry.getKey() + ";\r\n");
		}
	}
	
	private String processAllAttrString(){
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : attr) {
			builder.append("\tprivate " + entry.getValue() + " "
					+ entry.getKey() + ";\r\n");
		}
		return builder.toString();
	}
//生成get,set方法
	private void processAllMethod() throws IOException {
		for (Entry<String, String> entry : attr) {
			fileWriter.append("\tpublic void set"+toUpperCaseFirstLetter(entry.getKey())+"("+entry.getValue() + " " +entry.getKey()+") {\r\n");
			fileWriter.append("\t\tthis."+entry.getKey()+" = "+entry.getKey()+";\r\n");
			fileWriter.append("\t}\r\n\r\n");
			fileWriter.append("\tpublic "+entry.getValue()+" get"+toUpperCaseFirstLetter(entry.getKey())+"() {\r\n");
			fileWriter.append("\t\treturn "+entry.getKey()+";\r\n");
			fileWriter.append("\t}\r\n\r\n");
		}

	}
	private String processAllMethodString(){
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : attr) {
			builder.append("\tpublic void set"+toUpperCaseFirstLetter(entry.getKey())+"("+entry.getValue() + " " +entry.getKey()+") {\r\n");
			builder.append("\t\tthis."+entry.getKey()+" = "+entry.getKey()+";\r\n");
			builder.append("\t}\r\n\r\n");
			builder.append("\tpublic "+entry.getValue()+" get"+toUpperCaseFirstLetter(entry.getKey())+"() {\r\n");
			builder.append("\t\treturn "+entry.getKey()+";\r\n");
			builder.append("\t}\r\n\r\n");
		}
		return builder.toString();
	}
	//首字母大写转换
	private String toUpperCaseFirstLetter(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}
	private String getImports(){
		StringBuilder builder = new StringBuilder();
		Set<String> set = new HashSet<String>();
		for (Entry<String, String> entry : attr) {
			Class<?> obj = EnumMysqlColunmType.getNotBaseType(entry.getValue());
			if(obj!=null){
				set.add(getImprot(obj));
			}
		}
		
		for (String string : set) {
			builder.append(string);
		}
		return builder.toString();
	}
	
	
	private  String  getImprot(Class<?> t){
		return  "import " + t.getName() +";\r\n";
		
	}
	
	private String getType(){
		return null;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	public static void main(String[] args) throws IOException {
		Map<String, String> filed = new HashMap<String, String>();
		filed.put("id", "int");
		filed.put("count", "int");
		filed.put("money", "datetime");
		new CreateJava("View", "/src/com/qw4wer/bilibili/entity", filed);

	}
}
