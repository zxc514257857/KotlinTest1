package com.zhr.case9.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.zhr.case9.BuildConfig;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.jvm.Throws;

/**
 *  sqlite数据库升级工具
 *  please call {@link #migrate(SQLiteDatabase, Class[])} or {@link #migrate(Database, Class[])}
 *
 *  @Project:
 *  @Title:
 *  @Package:
 *  @Author  : tomyang
 *  @Date :   19-11-26  下午4:40
 *  @Version : V1.0
 *             V1.1 新增tableA 数据全部移动至tableB 方法
 *             V1.2 新增库间迁移
 **/
public final class MigrationHelper {

    public static boolean DEBUG = BuildConfig.DEBUG;
    private static String TAG = "MigrationHelper";
    private static final String SQLITE_MASTER = "sqlite_master";
    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";

    private static WeakReference<ReCreateAllTableListener> weakListener;

    public interface ReCreateAllTableListener{
        void onCreateAllTables(Database db, boolean ifNotExists);
        void onDropAllTables(Database db, boolean ifExists);
    }

    public static void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        printLog("【The Old Database Version】" + db.getVersion());
        Database database = new StandardDatabase(db);
        migrate(database, daoClasses);
    }

    public static void migrate(SQLiteDatabase db, ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference<>(listener);
        migrate(db, daoClasses);
    }

    public static void migrate(Database database, ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference<>(listener);
        migrate(database, daoClasses);
    }

    public static void migrate(Database database, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        printLog("【Generate temp table】start");
        generateTempTables(database, daoClasses);
        printLog("【Generate temp table】complete");

        ReCreateAllTableListener listener = null;
        if (weakListener != null) {
            listener = weakListener.get();
        }

        if (listener != null) {
            listener.onDropAllTables(database, true);
            printLog("【Drop all table by listener】");
            listener.onCreateAllTables(database, false);
            printLog("【Create all table by listener】");
        } else {
            dropAllTables(database, true, daoClasses);
            createAllTables(database, false, daoClasses);
        }
        printLog("【Restore data】start");
        restoreData(database, daoClasses);
        printLog("【Restore data】complete");
    }

    private static void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            String tempTableName = null;

            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            if (!isTableExists(db, false, tableName)) {
                printLog("【New Table】" + tableName);
                continue;
            }
            try {
                tempTableName = daoConfig.tablename.concat("_TEMP");
                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
                db.execSQL(dropTableStringBuilder.toString());

                StringBuilder insertTableStringBuilder = new StringBuilder();
                insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
                insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";");
                db.execSQL(insertTableStringBuilder.toString());
                printLog("【Table】" + tableName +"\n ---Columns-->"+getColumnsStr(daoConfig));
                printLog("【Generate temp table】" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "【Failed to generate temp table】" + tempTableName, e);
            }
        }
    }

    private static boolean isTableExists(Database db, boolean isTemp, String tableName) {
        if (db == null || TextUtils.isEmpty(tableName)) {
            return false;
        }
        String dbName = isTemp ? SQLITE_TEMP_MASTER : SQLITE_MASTER;
        String sql = "SELECT COUNT(*) FROM " + dbName + " WHERE type = ? AND name = ?";
        Cursor cursor=null;
        int count = 0;
        try {
            cursor = db.rawQuery(sql, new String[]{"table", tableName});
            if (cursor == null || !cursor.moveToFirst()) {
                return false;
            }
            count = cursor.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count > 0;
    }


    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < daoConfig.allColumns.length; i++) {
            builder.append(daoConfig.allColumns[i]);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


    private static void dropAllTables(Database db, boolean ifExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "dropTable", ifExists, daoClasses);
        printLog("【Drop all table by reflect】");
    }

    private static void createAllTables(Database db, boolean ifNotExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "createTable", ifNotExists, daoClasses);
        printLog("【Create all table by reflect】");
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses.length < 1) {
            return;
        }
        try {
            for (Class cls : daoClasses) {
                Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(null, db, isExists);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");

            if (!isTableExists(db, true, tempTableName)) {
                continue;
            }

            try {
                // get all columns from tempTable, take careful to use the columns list
                List<TableInfo> newTableInfos = TableInfo.getTableInfo(db, tableName);
                List<TableInfo> tempTableInfos = TableInfo.getTableInfo(db, tempTableName);
                ArrayList<String> selectColumns = new ArrayList<>(newTableInfos.size());
                ArrayList<String> intoColumns = new ArrayList<>(newTableInfos.size());
                for (TableInfo tableInfo : tempTableInfos) {
                    if (newTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);
                        selectColumns.add(column);
                    }
                }
                // NOT NULL columns list
                for (TableInfo tableInfo : newTableInfos) {
                    if (tableInfo.notnull && !tempTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);

                        String value;
                        if (tableInfo.dfltValue != null) {
                            value = "'" + tableInfo.dfltValue + "' AS ";
                        } else {
                            value = "'' AS ";
                        }
                        selectColumns.add(value + column);
                    }
                }

                if (intoColumns.size() != 0) {
                    StringBuilder insertTableStringBuilder = new StringBuilder();
                    insertTableStringBuilder.append("REPLACE INTO ").append(tableName).append(" (");
                    insertTableStringBuilder.append(TextUtils.join(",", intoColumns));
                    insertTableStringBuilder.append(") SELECT ");
                    insertTableStringBuilder.append(TextUtils.join(",", selectColumns));
                    insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
                    db.execSQL(insertTableStringBuilder.toString());
                    printLog("【Restore data】 to " + tableName);
                }
                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
                db.execSQL(dropTableStringBuilder.toString());
                printLog("【Drop temp table】" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "【Failed to restore data from temp table 】" + tempTableName, e);
            }
        }
    }

    /**
     * @Desc : copy tableA  all items into tableB, then delete tablaA all items
     *
     * @Name : a
     * @Date : 201-19 上午10:59
     * @Param : db : the tables host
     * @Param : fromDaoClasses  : copy from table list
     * @Param : intoDaoClasses  : copy into table list
     * @Return  a
     * @Throws:
     **/
    @Throws(exceptionClasses = IllegalArgumentException.class)
    public static void moveData(Database db, ArrayList<Class<? extends AbstractDao<?, ?>>> fromDaoClasses, ArrayList<Class<? extends AbstractDao<?, ?>>> intoDaoClasses) {
        Log.e(MigrationHelper.class.getName(), "movde data start  db ");
        if(fromDaoClasses.isEmpty() || fromDaoClasses.size() != intoDaoClasses.size()) {
            throw new IllegalArgumentException("copy table must one by one");
        }
        Log.e(MigrationHelper.class.getName(), "move data real start");
        for (int i = 0; i < fromDaoClasses.size(); i++) {
            Log.e(MigrationHelper.class.getName(), "move data cnt = " + i);
            DaoConfig fromDaoConfig = new DaoConfig(db, fromDaoClasses.get(i));
            DaoConfig intoDaoConfig = new DaoConfig(db, intoDaoClasses.get(i));
            String fromTablename = fromDaoConfig.tablename;
            String toTableName = intoDaoConfig.tablename;
            Log.e(MigrationHelper.class.getName(), "move data from table name = " + fromTablename + ", to table name = " + toTableName);

            if (!isTableExists(db, false, toTableName)) {
                continue;
            }

            try {
                // get all columns from tempTable, take careful to use the columns list
                List<TableInfo> newTableInfos = TableInfo.getTableInfo(db, toTableName);
                List<TableInfo> tempTableInfos = TableInfo.getTableInfo(db, fromTablename);
                ArrayList<String> selectColumns = new ArrayList<>(newTableInfos.size());
                ArrayList<String> intoColumns = new ArrayList<>(newTableInfos.size());
                for (TableInfo tableInfo : tempTableInfos) {
                    if (newTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);
                        selectColumns.add(column);
                    }
                }
                // NOT NULL columns list
                for (TableInfo tableInfo : newTableInfos) {
                    if (tableInfo.notnull && !tempTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);

                        String value;
                        if (tableInfo.dfltValue != null) {
                            value = "'" + tableInfo.dfltValue + "' AS ";
                        } else {
                            value = "'' AS ";
                        }
                        selectColumns.add(value + column);
                    }
                }

                if (intoColumns.size() != 0) {
                    StringBuilder insertTableStringBuilder = new StringBuilder();
                    insertTableStringBuilder.append("REPLACE INTO ").append(toTableName).append(" (");
                    insertTableStringBuilder.append(TextUtils.join(",", intoColumns));
                    insertTableStringBuilder.append(") SELECT ");
                    insertTableStringBuilder.append(TextUtils.join(",", selectColumns));
                    insertTableStringBuilder.append(" FROM ").append(fromTablename).append(";");
                    db.execSQL(insertTableStringBuilder.toString());
                    printLog("【move data】 to " + fromTablename + ", sql = " + insertTableStringBuilder.toString());
                }
                StringBuilder deleteTableDataStringBuilder = new StringBuilder();
                deleteTableDataStringBuilder.append("DELETE FROM ")
                        .append("'")
                        .append(fromTablename)
                        .append("'")
                        .append(";");
                db.execSQL(deleteTableDataStringBuilder.toString());
                printLog("【move table】" + fromTablename + ", 11 sql = " + deleteTableDataStringBuilder.toString());
                deleteTableDataStringBuilder.delete(0, deleteTableDataStringBuilder.length());
                deleteTableDataStringBuilder.append("DELETE from \"sqlite_sequence\" WHERE name = ")
                        .append("'")
                        .append(fromTablename)
                        .append("'")
                        .append(";");
                db.execSQL(deleteTableDataStringBuilder.toString());
                printLog("【move table】" + fromTablename + ", sql = " + deleteTableDataStringBuilder.toString());
            } catch (SQLException e) {
                Log.e(MigrationHelper.class.getName(), "【Failed to move data from table 】" + fromTablename + ", to table " + toTableName + "," + e);
            }
        }
    }

    /**
    *  @Des: 从数据库 fromDb 往 数据库 intoDb 迁移/拷贝 对应表中的数据
    *
    *
    *  @Name:
    *  @Class: MigrationHelper
    *  @Date:   2021/6/17  下午1:43
    *  @Param: fromDb 源数据库
    *  @Param: intoDb 目标数据库
    *  @Param: tableNameList 源和与标数据 相同的表结构的表名
    *  @Return:
    *  @Throws:
    *  @Version: V1.0
    **/
    public static void moveData(SQLiteDatabase fromDb, SQLiteDatabase intoDb, ArrayList<String> tableNameList){
        for(String tableName : tableNameList) {
            try {
                StringBuilder queryStringBuilder = new StringBuilder();
                queryStringBuilder.append("select * from ");
                queryStringBuilder.append(tableName);
                queryStringBuilder.append(";");
                Cursor queryCursor = fromDb.rawQuery(queryStringBuilder.toString(), null);
                int size = queryCursor.getCount();
                String[] columeName = queryCursor.getColumnNames();
                ArrayList<ContentValues> beMoveDataList = new ArrayList<>();
                while (queryCursor.moveToNext()) {
                    ContentValues contentValue = new ContentValues();
                    for (String name : columeName) {
                        int columeIndex = queryCursor.getColumnIndex(name);
                        int type = queryCursor.getType(columeIndex);
                        /** 如果是默认_id字段，不需要处理*/
                        if (name.equalsIgnoreCase("_id")) {
                            continue;
                        }
                        switch (type) {
                            case Cursor.FIELD_TYPE_NULL:
                                contentValue.put(name, "");
                                break;
                            case Cursor.FIELD_TYPE_INTEGER:
                                /** sqlite 无long类型，此处如果当作int取出，但是原始数据为long则存在数据溢出*/
                                long iValue = queryCursor.getLong(columeIndex);
                                contentValue.put(name, iValue);
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                float fValue = queryCursor.getFloat(columeIndex);
                                contentValue.put(name, fValue);
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                                String sValue = queryCursor.getString(columeIndex);
                                contentValue.put(name, sValue);
                                break;
                            case Cursor.FIELD_TYPE_BLOB:
                                byte[] bytes = queryCursor.getBlob(columeIndex);
                                contentValue.put(name, bytes);
                                break;
                            default:
                                break;
                        }
                    }
                    if (contentValue.size() > 0) {
                        beMoveDataList.add(contentValue);
                    }

                }
                queryCursor.close();

                try {
                    intoDb.beginTransaction();
                    for (ContentValues contentValues : beMoveDataList) {
                        long id = intoDb.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                        Log.e(MigrationHelper.class.getName(), "【move data】 to " + tableName + ", id = " + id);
                    }
                    intoDb.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    intoDb.endTransaction();
                }


            } catch (SQLException e) {
                e.printStackTrace();
                Log.e(MigrationHelper.class.getName(), "【Failed to move data from table 】" + tableName + ", to table " + tableName + "," + e);
            }
        }
    }

    /**
     *  @Des: 从数据库 fromDbAbsolutePath 往 数据库 intoDbAbsolutePath 迁移/拷贝 对应表中的数据
     *
     *
     *  @Name:
     *  @Class: MigrationHelper
     *  @Date:   2021/6/17  下午1:43
     *  @Param: fromDbAbsolutePath 源数据库绝对路径
     *  @Param: intoDbAbsolutePath 目标数据库绝对路径
     *  @Param: tableNameList 源和与标数据 相同的表结构的表名
     *  @Return:
     *  @Throws:
     *  @Version: V1.0
     **/
    public static void moveData(String fromDbAbsolutePath, String intoDbAbsolutePath, ArrayList<String> tableNameList){
        try {
            SQLiteDatabase fromDb = SQLiteDatabase.openDatabase(fromDbAbsolutePath, null, SQLiteDatabase.OPEN_READWRITE);
            SQLiteDatabase intoDb = SQLiteDatabase.openDatabase(intoDbAbsolutePath, null, SQLiteDatabase.OPEN_READWRITE);

            moveData(fromDb, intoDb, tableNameList);

            fromDb.close();
            intoDb.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MigrationHelper.class.getName(), "【Failed to move data from table 】 error = " + e.getMessage());
        }
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 0", null);
            if (null != cursor && cursor.getColumnCount() > 0) {
                columns = Arrays.asList(cursor.getColumnNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (null == columns) {
                columns = new ArrayList<>();
            }
        }
        return columns;
    }

    private static void printLog(String info){
        if(DEBUG){
            Log.d(TAG, info);
        }
    }

    private static class TableInfo {
        int cid;
        String name;
        String type;
        boolean notnull;
        String dfltValue;
        boolean pk;

        @Override
        public boolean equals(Object o) {
            return this == o
                    || o != null
                    && getClass() == o.getClass()
                    && name.equals(((TableInfo) o).name);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "TableInfo{" +
                    "cid=" + cid +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", notnull=" + notnull +
                    ", dfltValue='" + dfltValue + '\'' +
                    ", pk=" + pk +
                    '}';
        }

        private static List<TableInfo> getTableInfo(Database db, String tableName) {
            String sql = "PRAGMA table_info(" + tableName + ")";
            printLog(sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor == null) {
                return new ArrayList<>();
            }

            TableInfo tableInfo;
            List<TableInfo> tableInfos = new ArrayList<>();
            while (cursor.moveToNext()) {
                tableInfo = new TableInfo();
                tableInfo.cid = cursor.getInt(0);
                tableInfo.name = cursor.getString(1);
                tableInfo.type = cursor.getString(2);
                tableInfo.notnull = cursor.getInt(3) == 1;
                tableInfo.dfltValue = cursor.getString(4);
                tableInfo.pk = cursor.getInt(5) == 1;
                tableInfos.add(tableInfo);
                // printLog(tableName + "：" + tableInfo);
            }
            cursor.close();
            return tableInfos;
        }
    }
}
