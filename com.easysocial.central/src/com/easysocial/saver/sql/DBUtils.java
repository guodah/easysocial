package com.easysocial.saver.sql;
import java.lang.reflect.Field;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

//import org.hsqldb.Server;

import com.easysocial.logging.EasySocialLogger;
import com.easysocial.types.packed.LongField;
import com.easysocial.types.packed.SQLType;
import com.easysocial.types.packed.SavableObject;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.FacebookType;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.StatusMessage;
import com.restfb.types.User;

/**
 * Consists of methods that deals with the database.
 * This class needs to be refactored. Now it only supports HSQL. 
 * 
 * @author Dahai Guo
 *
 */
public class DBUtils {
	
	private static <T extends SavableObject> String getTableCreationSQL(Class<T> jType, 
			int regular_len, int long_len){
		Field[] fields = jType.getDeclaredFields();

		String sql="";
		try {
			sql += "create cached table "+/*jType.getSimpleName()*/
					jType.newInstance().getTableName()+" (";
		} catch (InstantiationException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<fields.length;i++){
			Field field = fields[i];
			String type = SQLType.DEFAULT_TYPE;
			if(field.isAnnotationPresent(SQLType.class)){
				SQLType sqlType = field.getAnnotation(SQLType.class);
				type = sqlType.type();
			}
			
			sql += String.format("%s %s",field.getName(), type);
			if(i!=fields.length-1){
				sql += ',';
			}
		}
		
		sql += ");";
		
		return sql;
	}
	
	/**
	 * Finds the sql commands for the classes whose objects can be stored in the 
	 * database.
	 * 
	 * It is simple in that each field maps to the class's instance variable and all
	 * data type in the table is varchar(...). There are no primary or foreign key yet.
	 * 
	 * @return 
	 */
	public static List<String> createTablesSQL(List<Class<?extends SavableObject>> classes){
		List<String> sqls = new ArrayList<String>();
		for(Class<?extends SavableObject> cl : classes){
			sqls.add(getTableCreationSQL(cl, SQLDBConstants.REGULAR_LEN, 
					SQLDBConstants.LONG_LEN));
			EasySocialLogger.log(Level.INFO, String.
					format("Table creation SQL generated: %s", sqls.get(sqls.size()-1)));
		}
		return sqls;
	}
	
}
