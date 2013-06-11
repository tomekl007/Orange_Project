package com.example.orangeapihackaton.analysis.luceneUtils;

import com.example.orangeapihackaton.model.Task;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

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

    //DatabaseHelper databaseHelper;

    @Override
    public void index(List<Task> allRecors) {
     //   databaseHelper = new DatabaseHelper();
        System.out.println("in Simple Indexer");
        Analyzer analyzer =new StandardAnalyzer(Version.LUCENE_43);// new StandardAnalyzer(Version.LUCENE_43);
        // 1. create the index
        Directory index = new RAMDirectory();
        Directory index2 = null;
       // try {
      //       index2 = new SimpleFSDirectory(new File("temp"));
      //  } catch (IOException e) {
      //      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      //  }

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);

        IndexWriter w = null;
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
        }

        //task indexing
//        IndexWriter w2 = null;
//        try {
//            w2 = new IndexWriter(index2,config);
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//       for(Task t : allRecors){
//            addTaskToIndexDocument(w2,t);
//       }

    }

    private static void addDoc(IndexWriter w, String title, String isbn)  {
        Document doc = new Document();

        doc.add(new StringField("title", title, Field.Store.YES));



        // use a string field for isbn because we don't want it tokenized
        doc.add(new TextField("isbn", isbn, Field.Store.YES));
        try {
            w.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void addTaskToIndexDocument(IndexWriter w, Task task )  {
        Document doc = new Document();

        doc.add(new StringField("text", task.getText(), Field.Store.YES));



        // use a string field for isbn because we don't want it tokenized
        doc.add(new IntField("id", task.getId(), Field.Store.YES));
        try {
            w.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
