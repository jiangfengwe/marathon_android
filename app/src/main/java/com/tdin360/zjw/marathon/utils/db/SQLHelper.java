package com.tdin360.zjw.marathon.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类(做缓存)
 * Created by admin on 16/12/10.
 */

public class SQLHelper extends SQLiteOpenHelper {

    //数据库名称
    public static final String DB_NAME="marathon.db";

//    赛事主页表
    public static final String EVENT_TABLE="Event";

    //赛事详情表
    public static final String EVENT_DETAIL_TABLE="EventDetail";

    //通知消息表
    public static final String NOTICE_MESSAGE_TABLE="NoticeMessage";

    //计步历史
    public static final String STEP="Step";
    //个人资料信息表
    public static final String MY_INFO_TABLE="MY_INFO";
    //新闻
    public static final String NEWS_INFO="news";
//    通知
    public static final String NOTICE_INFO="notice";


    public SQLHelper(Context context){

        super(context,DB_NAME,null,1);

    }

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


//        赛事数据表
        String eventSql ="CREATE TABLE "+EVENT_TABLE+" (\n" +
                "    id            INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    eventId       INTEGER NOT NULL,\n" +
                "    eventName     VARCHAR,\n" +
                "    eventImageUrl VARCHAR,\n" +
                "    shareUrl VARCHAR,\n" +
                "    eventStatus   VARCHAR,\n" +
                "    time          VARCHAR,\n" +
                "    enable  VARCHAR"+
                ");";

//        赛事详情表
        String eventDetailSql="CREATE TABLE  "+EVENT_DETAIL_TABLE+" (\n" +
                "    id       INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                     NOT NULL,\n" +
                "    title    VARCHAR,\n"+
                "    eventId  INTEGER,\n" +
                "    imageUrl VARCHAR,\n" +
                "    url      VARCHAR,\n" +
                "    type     VARCHAR\n" +
                ");";

//        通知消息表
        String create_Notice_Sql="CREATE TABLE  "+NOTICE_MESSAGE_TABLE+" (\n" +
                "    id      INTEGER      NOT NULL\n" +
                "                         PRIMARY KEY AUTOINCREMENT,\n" +
                "    forName VARCHAR (20),\n" +
                "    forTime VARCHAR (20),\n" +
                "    content VARCHAR\n" +
                ");";

//        计步表
        String create_Step_Sql="CREATE TABLE "+STEP+" (\n" +
                "    id            INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    userId        INTEGER NOT NULL,\n" +
                "    totalStep     INTEGER NOT NULL,\n" +
                "    totalDistance DOUBLE  NOT NULL,\n" +
                "    totalKcal     DOUBLE  NOT NULL,\n" +
                "    date          DATE    NOT NULL\n" +
                ");";


        //我的资料表
        String myInfoSql="CREATE TABLE  "+MY_INFO_TABLE+" (\n" +
                "    id      INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                    NOT NULL,\n" +
                "    tel    VARCHAR NOT NULL,\n" +
                "    name    VARCHAR,\n" +
                "    gander  BOOLEAN,\n" +
                "    bothday VARCHAR,\n" +
                "    email   VARCHAR\n" +
                ");";
        //新闻
        String newsSql = "CREATE TABLE "+NEWS_INFO+"(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "newsId Integer,eventId VARCHAR,title VARCHAR,imageUrl VARCHAR,url VARCHAR,time VARCHAR)";
        //公告
        String noticeSql = "CREATE TABLE "+NOTICE_INFO+"(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "noticeId Integer, eventId VARCHAR,title VARCHAR,url VARCHAR,time VARCHAR)";


        db.execSQL(eventSql);
        db.execSQL(eventDetailSql);
        db.execSQL(create_Notice_Sql);
        db.execSQL(create_Step_Sql);
        db.execSQL(myInfoSql);
        db.execSQL(newsSql);
        db.execSQL(noticeSql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+EVENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+EVENT_DETAIL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+NOTICE_MESSAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+NEWS_INFO);
        db.execSQL("DROP TABLE IF EXISTS "+NOTICE_INFO);
        db.execSQL("DROP TABLE IF EXISTS "+MY_INFO_TABLE);

        onCreate(db);
    }

}
