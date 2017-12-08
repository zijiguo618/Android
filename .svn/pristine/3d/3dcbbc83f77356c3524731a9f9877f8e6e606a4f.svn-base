
package com.example.test;

import android.util.Xml;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses XML feeds from stackoverflow.com.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class StackOverflowXmlParser {
    private static final String ns = null;

    // We don't use namespaces

    public List<Entry> parse(InputStream in) throws IOException, JSONException {
       try {
    	   BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
    	    StringBuilder sb = new StringBuilder();

    	    String line = null;
    	    while ((line = reader.readLine()) != null)
    	    {
    	        sb.append(line + "\n");
    	    }
    	 JSONArray j= new JSONArray(sb.toString());

    	 return readFeed(j);
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(JSONArray js) throws JSONException, IOException {
        List<Entry> entries = new ArrayList<Entry>();
        for(int i=0;i<js.length();i++){
        	JSONObject oneObject = js.getJSONObject(i);
        	entries.add(readEntry(oneObject));
        }

        return entries;
    }

    // This class represents a single entry (post) in the XML feed.
    // It includes the data members "title," "link," and "summary."
    public static class Entry {
        public final String title;
        public final String link;
       

        private Entry(String title, String link) {
            this.title = title;
            
            this.link = link;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Entry readEntry(JSONObject js) throws JSONException, IOException {
    	 String title = null;
         String link = null;
         title=js.getString("title");
         link=js.getString("sourceLink");

        return new Entry(title,link);
    }

}
