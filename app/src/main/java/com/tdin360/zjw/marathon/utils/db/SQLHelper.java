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
    // 社交点赞评论表
    public static final String PRAISE_COMMENT_TABLE="Circle";

//    赛事主页表
    public static final String EVENT_TABLE="Event";

    //赛事详情表
    public static final String EVENT_DETAIL_TABLE="EventDetail";

    //通知消息表
    public static final String NOTICE_MESSAGE_TABLE="NoticeMessage";

    //个人资料信息表
    public static final String MY_INFO_TABLE="MY_INFO";

    //新闻
    public static final String NEWS_INFO="news";

//    通知
    public static final String NOTICE_INFO="notice";


    public SQLHelper(Context context){

        super(context,DB_NAME,null,2);

    }

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 社交点赞评论表
        String circleSql ="CREATE TABLE "+PRAISE_COMMENT_TABLE+" (\n" +
                "    DynamicId            INTEGER \n" +
                "    NickName      VARCHAR,\n" +
                "    DynamicPictureUrl     VARCHAR,\n" +
                "    messageType   VARCHAR,\n" +
                "    HeadImg VARCHAR,\n" +
                "    CommentContent VARCHAR,\n" +
                "    DynamicContent   VARCHAR);" ;

//        赛事数据表
        String eventSql ="CREATE TABLE "+EVENT_TABLE+" (\n" +
                "    id            INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "                          NOT NULL,\n" +
                "    eventId       INTEGER NOT NULL,\n" +
                "    eventName     VARCHAR,\n" +
                "    eventImageUrl VARCHAR,\n" +
                "    shareUrl VARCHAR,\n" +
                "    eventStatus   VARCHAR,\n" +
                "    signUpTime    VARCHAR,\n" +
                "    eventTime     VARCHAR,\n" +
                "    enable  VARCHAR,"+
                "    isWebPage VARCHAR);";

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
        db.execSQL(myInfoSql);
        db.execSQL(newsSql);
        db.execSQL(noticeSql);
        db.execSQL(circleSql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PRAISE_COMMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+EVENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+EVENT_DETAIL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+NOTICE_MESSAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+NEWS_INFO);
        db.execSQL("DROP TABLE IF EXISTS "+NOTICE_INFO);
        db.execSQL("DROP TABLE IF EXISTS "+MY_INFO_TABLE);


        onCreate(db);
    }

}
