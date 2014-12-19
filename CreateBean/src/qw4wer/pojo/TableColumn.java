package qw4wer.pojo;

import java.io.Serializable;

public class TableColumn implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5033953985398919602L;

	private String columnName;
	
	private String dataType;
	
	private String columnComment;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	@Override
	public String toString() {
		return "TableColumn [columnName=" + columnName + ", dataType="
				+ dataType + ", columnComment=" + columnComment + "]";
	}
	
}
