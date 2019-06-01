package com.jfinal.plugin.activerecord.generator;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;



public abstract class LocalModel<M extends LocalModel<M>> extends Model<M> {

	private static final long serialVersionUID = 1L;
	protected static final Logger log = LoggerFactory.getLogger(LocalModel.class);
	
	protected Table getTable() {
		return TableMapping.me().getTable(getClass());
	}

	protected String _tableName() {
		return " " + getTable().getName() + " ";
	}

	protected M findFirstMCached(String cacheName,String cacheKey,String where,Object... paras){
		String sql = " select * from " + getTable().getName() + " " + where;
		return findFirstByCache(cacheName, cacheKey, sql,paras);
	}
	
	protected Integer deleteM(String where,Object...paras) {
		return  Db.update("delete from " + getTable().getName() + where,paras);
	}
	
	/**
	 * 按where过滤条件取M
	 * 
	 * @param where
	 *            过滤条件和排序
	 * @param paras
	 *            条件值
	 * @return M
	 */
	protected M findFirstM(String where, Object... paras) {
		return findFirstMLoadColumns(where, "*", paras);
	}

	protected M findFirstMLoadColumns(String where, String columns, Object... paras) {
		String sql = " select " + columns + " from " + getTable().getName() + " " + where;
		return findFirst(sql, paras);
	}

	/**
	 * 取全表记录 谨慎使用<code>findAll()</code>
	 * 
	 * @return
	 */
	protected List<M> findAllM() {
		return findAllM(null);
	}

	/**
	 * 取全表记录 谨慎使用<code>findAll()</code>
	 * 
	 * @return
	 */
	protected List<M> findAllM(String order) {
		String sql = " select * from " + getTable().getName() + " ";
		if (StrKit.notBlank(order)) {
			sql += order;
		}
		return find(sql);
	}

	protected List<M> findAllMColumns(String columns) {
		String sql = " select "+columns+" from " + getTable().getName() + " ";
		return find(sql);
	}
	/**
	 * 查找单一Model
	 * 
	 * @param where
	 *            过滤条件和排序
	 * @param paras
	 *            参数列表
	 * @return List<M>
	 */
	protected List<M> findMList(String where, Object... paras) {
		return findMListLoadColumns(where, "*", paras);
	}
	
	protected Page<M> pageM(int pageNumber,int pageSize){
		return pageM(pageNumber, pageSize, "", new Object[0]);
	}
	
	protected Page<M> pageM(int pageNumber, int pageSize, String expected, Object... paras){
		String select = "select * " ;
		String where = "from " + getTable().getName();
		if(!StrKit.isBlank(expected)){
			where += " " + expected;
		}
		return  super.paginate(pageNumber, pageSize, select, where, paras);
	}
	
	protected Page<M> pageByTablename(String tableName,int pageNumber, int pageSize, String expected, Object... paras){
		String select = "select * " ;
		String where = "from " + tableName;
		if(!StrKit.isBlank(expected)){
			where += " " + expected;
		}
		return  super.paginate(pageNumber, pageSize, select, where, paras);
	}
	
	protected Page<M> pageMColumns(int pageNumber, int pageSize,String columns, String expected, Object... paras){
		String select = "select "+columns ;
		String where = " from " + getTable().getName();
		if(!StrKit.isBlank(expected)){
			where += " " + expected;
		}
		return  super.paginate(pageNumber, pageSize, select, where, paras);
	}

	/**
	 * 查找单一Model并指定字段
	 * 
	 * @param where
	 *            过滤条件和排序
	 * @param columns
	 *            需要过滤的字段
	 * @param paras
	 *            参数列表
	 * @return List<M>
	 */
	protected List<M> findMListLoadColumns(String where, String columns, Object... paras) {
		String sql = " select " + columns + " from " + getTable().getName() + " " + where;
		return find(sql, paras);
	}

	protected Long countM(String where, Object... paras) {
		  M m =  this.findFirstMLoadColumns(where, "count(0) count", paras);
		  return null == m ? 0 : m.getLong("count");
	}
	
	protected Long countMColumn(String where, String column, Object... paras) {
		  M m =  this.findFirstMLoadColumns(where, "count("+column+") count", paras);
		  return null == m ? 0 : m.getLong("count");
	}
	/**
	 * 根据字段计算汇总值，字段值为整数类型
	 * @param where
	 * @param attr
	 * @param paras
	 * @return
	 */
	protected Long sumM(String where,String attr, Object... paras) {
		M m =  this.findFirstMLoadColumns(where, "IFNULL(sum("+attr+"),0) sum", paras);
		return null == m ? 0 : m.getLong("sum");
	}
	
	protected Long sumMSSQL(String where,String attr, Object... paras) {
		M m =  this.findFirstMLoadColumns(where, "ISNULL(sum("+attr+"),0) sum", paras);
		return null == m ? 0 : m.getLong("sum");
	}
	public boolean isColumn(String attr){
		Table table = getTable();
		return table.hasColumnLabel(attr)?true:false;
	}
	public Class columnType(String attr){
		return getTable().getColumnType(attr);
	}
	
	public String fmtNumber(Object obj){
		return new BigDecimal(String.valueOf(obj)).setScale(2).toString();
	}
	
	 protected String initWhereSql(String sql,String filter) {
	    	boolean first = false;
	    	if(sql.indexOf("where")<0) {
	    		first = true;
	    	}
	    	String concat = first?" where ":" and ";
	    	return sql+concat+filter;
	 }
}
