package qw4wer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.junit.Test;

import qw4wer.pojo.TableColumn;


public class DBUtils {
@Test
	public void t1(){
	try {
		testDbUtil();
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public void testDbUtil() throws SQLException{
		
		
		ResultSetHandler<TableColumn[]> h = new ResultSetHandler<TableColumn[]>() {
		    public TableColumn[] handle(ResultSet rs) throws SQLException {
		        if (!rs.next()) {
		            return null;
		        }
		        rs.last(); //移到最后一行   
		        int rowCount = rs.getRow(); //得到当前行号，也就是记录数   
		        rs.beforeFirst();
		    BeanProcessor b = new BeanProcessor();
		    TableColumn[] result = new TableColumn[rowCount];
		    int i =0;
		    while(rs.next()){
		    	result[i] = b.toBean(rs, TableColumn.class);
		    	i++;
		    }
		        return result;
		    }
		};

		// Create a QueryRunner that will use connections from
		// the given DataSource
		QueryRunner run = new QueryRunner(DataSourseFactory.getDataSource());

		// Execute the query and get the results back from the handler
		TableColumn[] result = run.query( "SELECT c.COLUMN_NAME as columnName,c.COLUMN_TYPE as columnType,c.COLUMN_COMMENT as columnComment,c.DATA_TYPE  as dataType FROM information_schema.COLUMNS c WHERE c.TABLE_NAME = ?",h,"user_account");
		for (TableColumn object : result) {
			System.out.println(object);
		}
		
	}
	
}
