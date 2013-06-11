package com.example.orangeapihackaton.guice_configuration;

import com.example.orangeapihackaton.analysis.luceneUtils.Indexer;
import com.example.orangeapihackaton.analysis.luceneUtils.SimpleIndexer;
import com.google.inject.AbstractModule;

public class LuceneModule extends AbstractModule {
  @Override 
  protected void configure() {

      System.out.println("guice configure");
     /*
      * This tells Guice that whenever it sees a dependency on a TransactionLog,
      * it should satisfy the dependency using a DatabaseTransactionLog.
      */
    bind(Indexer.class).to(SimpleIndexer.class);

     /*
      * Similarly, this binding tells Guice that when CreditCardProcessor is used in
      * a dependency, that should be satisfied with a PaypalCreditCardProcessor.
      */
    //bind(CreditCardProcessor.class).to(PaypalCreditCardProcessor.class);
  }
}