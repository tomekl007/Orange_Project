package com.example.orangeapihackaton.analysis.luceneUtils;

import com.example.orangeapihackaton.database.DatabaseHelper;
import com.example.orangeapihackaton.model.Task;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/11/13
 * Time: 8:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleIndexer implements Indexer {
    private static String TAG = SimpleIndexer.class.getCanonicalName();

    private String path = "lucene_index";
    //DatabaseHelper databaseHelper;

    @Override
    public void index(List<Task> allRecords, File tempFile) {
        System.out.println("get allRecords : " + allRecords + ", and file : " + tempFile);
     //   databaseHelper = new DatabaseHelper();
        System.out.println("in Simple Indexer");
        Analyzer analyzer =new StandardAnalyzer(Version.LUCENE_36);// new StandardAnalyzer(Version.LUCENE_43);
        // 1. create the index
        //Directory index = new RAMDirectory();
        Directory index2 = null;
       // try {

          //  File file = new File(Environment.getDataDirectory() +
          //          File.separator + "lucene_index");
          //  file.createNewFile();
          //

             index2 = new RAMDirectory();
       // } catch (IOException e) {
       //     Log.d(TAG,"cannot create file");
      //      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       // }

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);

       /* IndexWriter w = null;
        try {
            w = new IndexWriter(index, config);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        addDoc(w, "Lucene in Action", "193398817");
        addDoc(w, "Lucene for Dummies", "55320055Z");
        addDoc(w, "Managing Gigabytes", "55063554A");
        addDoc(w, "The Art of Computer Science", "9900333X");
        try {
            w.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }                            */

        //task indexing
        IndexWriter w2 = null;
        try {
            w2 = new IndexWriter(index2,config);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

       for(Task t : allRecords){
            addTaskToIndexDocument(w2,t);
       }


        //----------------------------------------

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
            reader = IndexReader.open(index2);
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



 /*   private static void addDoc(IndexWriter w, String title, String isbn)  {
        Document doc = new Document();

        doc.add(new Field("title", title, Field.Store.YES, Field.Index.NO));



        // use a string field for isbn because we don't want it tokenized
        doc.add(new Field("isbn", isbn, Field.Store.YES,Field.Index.NO));
        try {
            w.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }   */

    private static void addTaskToIndexDocument(IndexWriter w, Task task )  {
        Document doc = new Document();

        doc.add(new Field("text", task.getText(), Field.Store.YES, Field.Index.NO));



        // use a string field for isbn because we don't want it tokenized
        doc.add(new Field("id", task.getId().toString(), Field.Store.YES, Field.Index.ANALYZED));
        try {
            w.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }




    }
}
