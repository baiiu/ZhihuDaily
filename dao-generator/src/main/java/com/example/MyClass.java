package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * more info on the official website:
 *
 * http://greenrobot.org/greendao/documentation/modelling-entities/
 */
public class MyClass {
  public static void main(String[] args) throws Exception {

    //创建Schema模式对象
    //两个参数, 数据库版本号 与 自动生成代码的包路径
    Schema schema = new Schema(1, "baiiu.greendao.gen");

    //添加实体
    addTopStory(schema);
    addStory(schema);
    addStoryDetail(schema);

    //生成代码.

    new DaoGenerator().generateAll(schema, "../ZhihuDaily/app/src/main/java-gen");
  }

  private static void addTopStory(Schema schema) {
    //创建实体类,TopStory则是表明
    Entity topStory = schema.addEntity("SavedTopStory");

    topStory.addIdProperty();
    topStory.addStringProperty("image");
    topStory.addStringProperty("title");
  }

  private static void addStory(Schema schema) {
    Entity savedStory = schema.addEntity("SavedStory");

    savedStory.addIdProperty();
    savedStory.addStringProperty("image");
    savedStory.addStringProperty("title");
    savedStory.addStringProperty("date");
    savedStory.addIntProperty("position");
  }

  private static void addStoryDetail(Schema schema) {
    Entity savedDailyDetail = schema.addEntity("SavedDailyDetail");

    savedDailyDetail.addIdProperty();
    savedDailyDetail.addStringProperty("body");
    savedDailyDetail.addStringProperty("image_source");
    savedDailyDetail.addStringProperty("image");
    savedDailyDetail.addStringProperty("title");
    savedDailyDetail.addStringProperty("share_url");
    savedDailyDetail.addStringProperty("js");
    savedDailyDetail.addStringProperty("css");
  }
}
