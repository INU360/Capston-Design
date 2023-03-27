package com.capstone.yojo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):SQLiteOpenHelper(context,"crimeDB",null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table crime_TB ("+
                "_id integer primary key autoincrement," +
                "name text,"+
                "address text,"+
                "long double,"+
                "lat double,"+
                "content text,"+
                "place text,"+
                "device text,"+
                "num integer,"+
                "dong text)")

        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"강두환\", \"인천광역시 연수구 함박안로134번길 7-14\",126.68327,37.4266006,\"미성년자 성희롱 + 성적 학대행위 9회 / 2회 간음\",\"인천 연수구\",\"N\",0,\"연수동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"고형선\",\"인천광역시 연수구 솔샘로 70\",126.664884,37.4249909,\"강제추행 1회\",\"인천 남동구\",\"Y\",2,\"청학동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"곽병운\", \"인천광역시 연수구 학나래로6번길 48\",126.698075,37.4251979,\"미성년자 강간 3회 + 촬영 1회\",\"인천 동구, 남구, 중구\",\"N\",0,\"선학동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"노광욱\",\"인천광역시 연수구 솔샘로 77-3\",126.664286,37.4247189,\"간음 1회\",\"천안시 서북구\",\"N\",1,\"청학동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"맹순도\", \"인천광역시 연수구 새말로 78\",126.678274,37.4192078,\"강간미수 1회\",\"시흥시\",\"N\",0,\"연수동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"박기수\",\"인천광역시 연수구 비류대로 437번길 70\",126.683233,37.4245282,\"공범과 합동 강간 1회\",\"전남 순천시\",\"N\",0,\"연수동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"백동호\", \"인천광역시 연수구 비류대로 437번길 17\",126.68331,37.4251321,\"공범과 강간미수 1회 + 상해 1회\",\"안산시 상록구\",\"N\",0,\"연수동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"윤재일\",\"인천광역시 연수구 미추홀대로304번길 28\",126.671241,37.4256846,\"미성년자 간음 4회 + 강제추행 1회\",\"안산시 상록구, 원주시\",\"N\",0,\"연수동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"장명직\", \"인천광역시 연수구 청학로12번길 34-6\",126.665268,37.4275869,\"강간 1회\",\"인천 남구\",\"N\",0,\"청학동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"주만성\",\"인천광역시 연수구 독배로90번길 14\",126.646408,37.4282526,\"강간 2회\",\"서울 강남구\",\"N\",0,\"옥련동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"한동원\", \"인천광역시 연수구 비류대로 519번길 12\",126.692354,37.4236874,\"미성년자 강간 1회\",\"경기 의정부\",\"Y\",1,\"선학동\")")
        db?.execSQL("insert into crime_TB (name,address,long,lat,content,place,device,num,dong) values (\"한재영\",\"인천광역시 연수구 청량로113번길 33\",126.653079,37.4173793,\"통신매체를 이용한 음란행위 1회\",\"인터넷\",\"N\",2,\"옥련동\")")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS crime_TB");
    }

}