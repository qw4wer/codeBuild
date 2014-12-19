package qw4wer;

import java.sql.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum EnumMysqlColunmType {

	varcharColunm("varchar","String".getClass()),
	
	intColunm("int","int".getClass()),
	
	datetimeColoum("datetime",Date.class),
	
	charColunm("char","char".getClass());
	
	private Class<?> cls;
	
	private String colunmType;
	
	private static Map<String,EnumMysqlColunmType>  map = new LinkedHashMap<String, EnumMysqlColunmType>();
	
	private static String[] noHas = new String[]{"int","double","String","long","float","short","byte","char","boolean"};
	
	EnumMysqlColunmType(String colunmType,Class<?> cls){
		this.cls = cls;
		this.colunmType = colunmType;
	}
	
	static{
		init();
	}
	
	private static void init(){
		EnumMysqlColunmType[] _enum = values();
		for (EnumMysqlColunmType enumMysqlColunmType : _enum) {
			map.put(enumMysqlColunmType.getColunmType(), enumMysqlColunmType);
		}
		
	}
	
	public static Class<?> getNotBaseType(String type){
		List<String> noHasList = Arrays.asList(noHas);
		if(!noHasList.contains(map.get(type).getCls())){
			return map.get(type).getCls();
		}
		
		return null;
	}
	
	public static Map<String,String> getFields(Map<String,String> map){
		Class<?> cls = null;
		for (String key : map.keySet()) {
			if((cls =  EnumMysqlColunmType.getNotBaseType(map.get(key)))!=null){
				map.put(key,cls.getName().substring(cls.getName().lastIndexOf(".")+1,cls.getName().length()));
			}
			
		}
		return map;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	public String getColunmType() {
		return colunmType;
	}

	public void setColunmType(String colunmType) {
		this.colunmType = colunmType;
	}
}
