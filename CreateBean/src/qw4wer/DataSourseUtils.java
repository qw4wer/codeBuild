package qw4wer;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.sql.DataSource;

/**
 * 
 * Ĭ������£����б������������Ӷ���
 * 
 * ��ͨ����Connection�Ĵ���ʵ����closeʱ���س�
 * 
 * @author <a href="mailto:wj@itcast.cn">����</a>
 * 
 * @version 1.0 2012-5-6
 */

public class DataSourseUtils implements DataSource {

	private int poolSize = 3;// Ĭ��Ϊ3��

	private LinkedList<Connection> pool = new LinkedList<Connection>();

	public DataSourseUtils(String driver, String url, String name, String pwd) {

		this(driver, url, name, pwd, 3);

	}

	public DataSourseUtils(String driver, String url, String name, String pwd,
			int poolSize) {

		try {

			Class.forName(driver);

			this.poolSize = poolSize;

			if (poolSize <= 0) {

				throw new RuntimeException("��֧�ֵĳش�С" + poolSize);

			}

			for (int i = 0; i < poolSize; i++) {

				Connection con = DriverManager.getConnection(url, name, pwd);

				con = ConnProxy.getProxyedConnection(con, pool);// ��ȡ������Ķ���

				pool.add(con);// ��ӱ�����Ķ���

			}

		} catch (Exception e) {

			throw new RuntimeException(e.getMessage(), e);

		}

	}

	/**
	 * 
	 * ��ȡ�ش�С
	 */

	public int getPoolSize() {

		return poolSize;

	}

	/**
	 * 
	 * ��֧����־����
	 */

	public PrintWriter getLogWriter() throws SQLException {

		throw new RuntimeException("Unsupport Operation.");

	}

	public void setLogWriter(PrintWriter out) throws SQLException {

		throw new RuntimeException("Unsupport operation.");

	}

	/**
	 * 
	 * ��֧�ֳ�ʱ����
	 */

	public void setLoginTimeout(int seconds) throws SQLException {

		throw new RuntimeException("Unsupport operation.");

	}

	public int getLoginTimeout() throws SQLException {

		return 0;

	}

	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {

		return (T) this;

	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {

		return DataSource.class.equals(iface);

	}

	/**
	 * 
	 * �ӳ���ȡһ�����Ӷ���<br/>
	 * 
	 * ʹ����ͬ�����̵߳��ȼ���
	 */

	public Connection getConnection() throws SQLException {

		synchronized (pool) {

			if (pool.size() == 0) {

				try {

					pool.wait();

				} catch (InterruptedException e) {

					throw new RuntimeException(e.getMessage(), e);

				}

				return getConnection();

			} else {

				Connection con = pool.removeFirst();

				return con;

			}

		}

	}

	public Connection getConnection(String username, String password)

	throws SQLException {

		throw new RuntimeException("��֧�ֽ����û���������Ĳ���");

	}

	/**
	 * 
	 * ��̬�ڲ��࣬ʵ�ֶ�Connection�Ķ�̬����
	 * 
	 * @author <a href="mailto:wj@itcast.cn">����</a>
	 * 
	 * @version 1.0 2012-5-6
	 */

	static class ConnProxy implements InvocationHandler {

		private Object o;

		private LinkedList<Connection> pool;

		private ConnProxy(Object o, LinkedList<Connection> pool) {

			this.o = o;

			this.pool = pool;

		}

		public static Connection getProxyedConnection(Object o,
				LinkedList<Connection> pool) {

			Object proxed = Proxy.newProxyInstance(o.getClass()
					.getClassLoader(),

			new Class[] { Connection.class }, new ConnProxy(o, pool));

			return (Connection) proxed;

		}

		public Object invoke(Object proxy, Method method, Object[] args)

		throws Throwable {

			if (method.getName().equals("close")) {

				synchronized (pool) {

					pool.add((Connection) proxy);// ��������Ķ���Żس���

					pool.notify();// ֪ͨ�ȴ��߳�ȥ��ȡһ�����Ӱ�

				}

				return null;

			} else {

				return method.invoke(o, args);

			}

		}

	}

}
