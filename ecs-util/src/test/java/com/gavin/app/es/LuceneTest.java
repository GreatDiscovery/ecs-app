package com.gavin.app.es;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * 查看Lucene写入document原理
 *
 * @author: Gavin
 * @date: 2021/6/7 17:10
 * @description:
 */
public class LuceneTest {
    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        // Store the index in memory:
        Directory directory = new RAMDirectory();
        // To store an index on disk, use this instead:
        //Directory directory = FSDirectory.open("/tmp/testindex");
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iWriter = new IndexWriter(directory, config);
        Document doc = new Document();
        String text = "This is the text to be indexed.";
        doc.add(new Field("fieldName", text, TextField.TYPE_STORED));
        iWriter.addDocument(doc);
        iWriter.close();

        // Now search the index:
        DirectoryReader iReader = DirectoryReader.open(directory);
        IndexSearcher iSearcher = new IndexSearcher(iReader);
        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser("fieldName", analyzer);
        Query query = parser.parse("text");
        ScoreDoc[] hits = iSearcher.search(query, 1000).scoreDocs;
        //assertEquals(1, hits.length);
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = iSearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get("fieldName"));
        }
        iReader.close();
        directory.close();
    }

}
