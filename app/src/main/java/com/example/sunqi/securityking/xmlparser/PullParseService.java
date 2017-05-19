package com.example.sunqi.securityking.xmlparser;

import com.example.sunqi.securityking.bean.VirusApp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析本地的病毒库得到病毒列表
 * Created by sunqi on 2017/5/19.
 */

public class PullParseService {

    public static List<VirusApp> getVirusApps(InputStream ins) throws Exception {
        ArrayList<VirusApp> list = null;
        VirusApp virus = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();// 得到Pull解析器
        parser.setInput(ins, "utf-8");// 解析xml输入流
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {// 文档没有结束继续循环
            String tagName = parser.getName();// 标签名称
            switch (type) {
                case XmlPullParser.START_DOCUMENT:
                    list = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("VirusApp".equals(tagName)) {
                        virus = new VirusApp();
                        virus.setType(Integer.parseInt(parser.getAttributeValue(0)));
                    } else if ("pkgName".equals(tagName)) {
                        virus.setPkgName(parser.nextText());
                    } else if ("appName".equals(tagName)) {
                        virus.setAppName(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("VirusApp".equals(tagName)) {
                        list.add(virus);
                        virus = null;
                    }
                    break;
            }
            type = parser.next();// 继续解析下一个标签
        }
        return list;
    }
}
