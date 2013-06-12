package com.example.orangeapihackaton.analysis.luceneUtils;

import android.util.Log;
import com.example.orangeapihackaton.database.DatabaseHelper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/12/13
 * Time: 4:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class DestinationLocationSearcher implements Searcher {

    static String TAG = DestinationLocationSearcher.class.getCanonicalName();
    @Override
    public void search(File dirctoryWithIndex) {

        Analyzer analyzer =new StandardAnalyzer(Version.LUCENE_36);// new StandardAnalyzer(Version.LUCENE_43);


        Directory index = null;
        try {

            //  File file = new File(Environment.getDataDirectory() +
            //          File.separator + "lucene_index");
            //  file.createNewFile();
            //

            index = new SimpleFSDirectory(dirctoryWithIndex);
        } catch (IOException e) {
            Log.d(TAG, "cannot create/open file");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String querystr = "at";// args.length > 0 ? args[0] : "lucene";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
         //new QueryParser(Version.LUCENE_36, "title", analyzer);
        Query q = null;
        try {
            q = new QueryParser(Version.LUCENE_36, DatabaseHelper.TEXT_COLUMN_NAME, analyzer).parse(querystr);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        // 3. search
        int hitsPerPage = 10;

        IndexReader reader = null;
        try {
            reader = IndexReader.open(index);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        try {
            searcher.search(q, collector);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ScoreDoc[] hits = collector.topDocs().scoreDocs;



        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = null;
            try {
                d = searcher.doc(docId);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            System.out.println((i + 1) + ". " + d.get(DatabaseHelper.TEXT_COLUMN_NAME) + " id:  " + d.get(DatabaseHelper.TEXT_COLUMN_ID));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }


    }
}
