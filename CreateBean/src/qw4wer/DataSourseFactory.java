
package qw4wer;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**

 * ͨ��ʵ�����Լ���DataSource��ȡ����

 * @author <a href="mailto:wj@itcast.cn">����</a>

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

     * ֱ�ӻ�ȡһ��Connection

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

     * ��ȡ�ֲ߳̾���Connection

     */

    public static Connection getThreadConn(){

       Connection con = thread.get();//�ȴ��߳���ȡ����

       if(con==null){

           con = getConn();

           thread.set(con);

       }

       return con;

    }

    /**

     * ��ѡ�ĵ���ɾ���ֲ��߳��еĶ���

     */

    public static void remove(){

       thread.remove();

    }

    /**

     * ��ȡһ��DataSource

     */

    public static DataSource getDataSource(){

       return dataSource;

    }

}