package com.example.orangeapihackaton.analysis.luceneUtils;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.surround.parser.ParseException;

import java.io.IOException;

public class HelloLucene {

   public void example() throws IOException, ParseException {
     // 0. Specify the analyzer for tokenizing text.
     //    The same analyzer should be used for indexing and searching
     /*Analyzer analyzer =    */
               /*    Analyzer analyzer =new SnowballAnalyzer(Version.LUCENE_36,"snowball");// new StandardAnalyzer(Version.LUCENE_43);
     // 1. create the index
     Directory index = new RAMDirectory();

     IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);

     IndexWriter w = new IndexWriter(index, config);
     addDoc(w, "Lucene in Action", "193398817");
     addDoc(w, "Lucene for Dummies", "55320055Z");
     addDoc(w, "Managing Gigabytes", "55063554A");
     addDoc(w, "The Art of Computer Science", "9900333X");
     w.close();
                                                               */
     // 2. query
     String querystr = "action";// args.length > 0 ? args[0] : "lucene";

     // the "title" arg specifies the default field to use
     // when no field is explicitly specified in the query.
      // new QueryParser(Version.LUCENE_36, "title", analyzer);
     /*  Query q = new QueryParser(Version.LUCENE_43, "title", analyzer).parse(querystr);


     // 3. search
     int hitsPerPage = 10;

     IndexReader reader = IndexReader.open(index);
     IndexSearcher searcher = new IndexSearcher(reader);
     TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
     searcher.search(q, collector);
     ScoreDoc[] hits = collector.topDocs().scoreDocs;

     // 4. display results
     System.out.println("Found " + hits.length + " hits.");
     for(int i=0;i<hits.length;++i) {
       int docId = hits[i].doc;
       Document d = searcher.doc(docId);
       System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
     }

     // reader can only be closed when there
     // is no need to access the documents any more.
     reader.close();                                       */
   }

   private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
     Document doc = new Document();

   /*  doc.add(new StringField("title", title, Field.Store.YES));



     // use a string field for isbn because we don't want it tokenized
     doc.add(new TextField("isbn", isbn, Field.Store.YES));             */
     w.addDocument(doc);
   }
}