
package qw4wer;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**

 * 通过实例化自己的DataSource获取连接

 * @author <a href="mailto:wj@itcast.cn">王健</a>

 * @version 1.0 2012-5-6

 */

public class DataSourseFactory {

    private static DataSource dataSource;

    private static ThreadLocal<Connection> thread = new ThreadLocal<Connection>();

    static{

       dataSource =

              new DataSourseUtils("com.mysql.jdbc.Driver",

                     "jdbc:mysql://127.0.0.1:3306/?characterEncoding=UTF-8",

                     "root","soft",3);

    }

    /**

     * 直接获取一个Connection

     */

    public static Connection getConn(){

       Connection con = null;

       try {

           con= dataSource.getConnection();

       } catch (SQLException e) {

           throw new RuntimeException(e.getMessage(),e);

       }

       return con;

    }

    /**

     * 获取线程局部的Connection

     */

    public static Connection getThreadConn(){

       Connection con = thread.get();//先从线程中取数据

       if(con==null){

           con = getConn();

           thread.set(con);

       }

       return con;

    }

    /**

     * 可选的调用删除局部线程中的对象

     */

    public static void remove(){

       thread.remove();

    }

    /**

     * 获取一个DataSource

     */

    public static DataSource getDataSource(){

       return dataSource;

    }

}