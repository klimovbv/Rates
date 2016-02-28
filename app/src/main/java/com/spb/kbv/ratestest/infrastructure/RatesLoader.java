package com.spb.kbv.ratestest.infrastructure;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RatesLoader extends AsyncTaskLoader<HashMap<String,String>> {
    public final static String DATE_ARGS = "DATE_ARGS";

    private static final String QUERY_PARAM = "date_req";
    private static final String BASE_URL = "http://www.cbr.ru/scripts/XML_daily.asp?";
    private String date;

    public RatesLoader(Context context, Bundle args) {
        super(context);
        if (args != null) {
            date = args.getString(DATE_ARGS);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }


    @Override
    public HashMap<String,String> loadInBackground() {
        if (date == null) {
            return null;
        }

        HashMap<String,String> rates = new HashMap<>();

        try {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, date)
                    .build();

            URL url = new URL(builtUri.toString());
            URLConnection conn = url.openConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            NodeList nodelist = doc.getElementsByTagName("Valute");
            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node = nodelist.item(i);
                Element element = (Element) node;
                NodeList valuteName = element.getElementsByTagName("CharCode");
                Element name = (Element) valuteName.item(0);
                String nameLine = name.getTextContent();
                if (nameLine.equals("USD") || nameLine.equals("EUR")) {
                    NodeList title = element.getElementsByTagName("Value");
                    Element line = (Element) title.item(0);
                    rates.put(nameLine, line.getTextContent());
                    Log.d("loader1", line.getTextContent());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            rates.put("Error", e.toString());
        }
        return rates;
    }
}
