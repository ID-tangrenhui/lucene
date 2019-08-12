package com.trh.create;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.nodes.NumericRangeQueryNode;
import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.apache.lucene.queryparser.surround.query.SrndQuery;
import org.apache.lucene.queryparser.xml.builders.NumericRangeFilterBuilder;
import org.apache.lucene.queryparser.xml.builders.NumericRangeQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneCreate {
	
	//1 创建文档对象
	//2 创建存储目录
	//3 创建分词器
	//4 创建索引写入器的配置对象
	//5 创建索引写入器对象
	//6 将文档交给索引写入器
	//7 提交
	//8 关闭
//	============================================
	//创建索引  
	@Test
	public void testCreate1() throws Exception{
		//1,创建文档
		Document document = new Document();
		//创建并且添加字段信息.参数：字段名称，字段的值，是否存储，这里选store.yes代表存储到文档列表，store.no代表不存储
		document.add(new StringField("id","1",Field.Store.YES));
		document.add(new TextField("title","谷歌地图之父跳槽facebook",Field.Store.YES));		
		//2,索引目录类，制定索引在硬盘中的位置
		Directory directory = FSDirectory.open(new File("E:\\EclipseForPersonal\\lucene\\Lucene_directory"));
		//3,创建分词器对象
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
		//引入IK分词器
		Analyzer ikanalyzer = new IKAnalyzer();//专业的中文分词器
		//4,索引写出工具的配置对象
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_9, ikanalyzer);
		//append 会在索引库的基础上追加新索引，create 会清空原来的数据添加新索引
		conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
		//5,创建索引的写出工具类，参数：索引的目录和配置信息
		IndexWriter indexWriter = new IndexWriter(directory, conf);
		//6,把文档交给indexwriter
		indexWriter.addDocument(document);
		//7,提交
		indexWriter.commit();
		//8,关闭
		indexWriter.close();
	}
	
	//批量创建索引
	@Test
	public void testCreate2() throws Exception{
		//创建文档的集合
		Collection<Document> docs = new ArrayList();
		//创建文档对象
		Document document1 = new Document();
		document1.add(new StringField("id","1",Field.Store.YES));
		document1.add(new TextField("title", "谷歌地图之父跳槽Facebook", Field.Store.YES));
		docs.add(document1);
		//创建文档对象
		Document document2 = new Document();
		document1.add(new StringField("id","2",Field.Store.YES));
		document1.add(new TextField("title", "谷歌地图之父加盟Facebook", Field.Store.YES));
		docs.add(document2);
		//创建文档对象
		Document document3 = new Document();
		document1.add(new StringField("id","3",Field.Store.YES));
		document1.add(new TextField("title", "谷歌地图创始人拉斯离开谷歌加盟Facebook", Field.Store.YES));
		docs.add(document3);
        // 创建文档对象
        Document document4 = new Document();
        document4.add(new StringField("id", "4", Field.Store.YES));
        document4.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Field.Store.YES));
        docs.add(document4);
        // 创建文档对象
        Document document5 = new Document();
        document5.add(new StringField("id", "5", Field.Store.YES));
        document5.add(new TextField("title", "谷歌地图之父拉斯加盟社交网站Facebook", Field.Store.YES));
        docs.add(document5);
        
		Directory directory = FSDirectory.open(new File("E:\\EclipseForPersonal\\lucene\\Lucene_directory"));
		//3,创建分词器对象
//		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
		//引入IK分词器
		Analyzer ikanalyzer = new IKAnalyzer();//专业的中文分词器
		//4,索引写出工具的配置对象
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_9, ikanalyzer);
		//append 会在索引库的基础上追加新索引，create 会清空原来的数据添加新索引
		conf.setOpenMode(OpenMode.CREATE);
		//5,创建索引的写出工具类，参数：索引的目录和配置信息
		IndexWriter indexWriter = new IndexWriter(directory, conf);
		//6,把文档交给indexwriter
		indexWriter.addDocuments(docs);
		//7,提交
		indexWriter.commit();
		//8,关闭
		indexWriter.close();
	}
	
	//方法用于查询索引数据
	//1 创建读取目录对象
	//2 创建索引读取工具
	//3 创建索引搜索工具
	//4 创建查询解析器
	//5 创建查询对象
	//6 搜索数据
	//7 各种操作
	@Test
	public void testSearch() throws Exception{
		//索引目录对象
		Directory directory = FSDirectory.open(new File("E:\\EclipseForPersonal\\lucene\\Lucene_directory"));
		//索引读取工具
		IndexReader reader = DirectoryReader.open(directory);
		//索引搜索工具
		IndexSearcher searcher = new IndexSearcher(reader);
		//创建查询解析器，两个参数，默认要查询的字段的名称，分词器
		StandardQueryParser qpHelper = new StandardQueryParser(new IKAnalyzer());
//		QueryParser parser = new QueryParser();
//		MultiFieldQueryParser mul = new MultiFieldQueryParser(Version.LUCENE_4_9, new String[] {"id"},new IKAnalyzer());
//		mul.parse("1");
		Query query = qpHelper.parse("跳槽", "title");
		//搜索数据，两个参数，查询条件对象要查询的最大结果条目
		//返回结果是 按照匹配度排名得分前N名的文档
		TopDocs topDocs = searcher.search(query, 10);
        // 获取总条数
		System.out.println("====================================================================");
        System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");
        //获取得分文档对象（ScoreDoc）数组，ScoreDoc中包含；文档编号，文档得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
        	//取出文档编号
        	int docID = scoreDoc.doc;
        	//根据编号找文档
        	Document doc = reader.document(docID);
        	System.out.println("id====="+doc.get("id"));
        	System.out.println("title====="+doc.get("title"));
        	//取出得分
        	System.out.println("得分======"+scoreDoc.score);
        }
        System.out.println("====================================================================");
	}
	
	//多查询
	@Test
	public void testMutilSearch() throws Exception{
		//索引目录对象
		Directory directory = FSDirectory.open(new File("E:\\EclipseForPersonal\\lucene\\Lucene_directory"));
		//索引读取工具
		IndexReader reader = DirectoryReader.open(directory);
		//索引搜索工具
		IndexSearcher searcher = new IndexSearcher(reader);
		//创建查询解析器，两个参数，默认要查询的字段的名称，分词器
		StandardQueryParser qpHelper = new StandardQueryParser(new IKAnalyzer());
		
		MultiFieldQueryParser mulHelper = new MultiFieldQueryParser(Version.LUCENE_4_9, new String[] {"id","title"}, new IKAnalyzer());
		
		Query multiQuery = mulHelper.parse("1");
		//搜索数据，两个参数，查询条件对象要查询的最大结果条目
		//返回结果是 按照匹配度排名得分前N名的文档
		TopDocs topDocs = searcher.search(multiQuery, 10);
        // 获取总条数
		System.out.println("====================================================================");
        System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");
        //获取得分文档对象（ScoreDoc）数组，ScoreDoc中包含；文档编号，文档得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
        	//取出文档编号
        	int docID = scoreDoc.doc;
        	//根据编号找文档
        	Document doc = reader.document(docID);
        	System.out.println("id====="+doc.get("id"));
        	System.out.println("title====="+doc.get("title"));
        	//取出得分
        	System.out.println("得分======"+scoreDoc.score);
        }
        System.out.println("====================================================================");
	}
	
	
//	抽取公用的搜索方法：
	public void search(Query query) throws Exception{
		//索引目录对象
		Directory directory = FSDirectory.open(new File("E:\\EclipseForPersonal\\lucene\\Lucene_directory"));
		//索引读取工具
		IndexReader reader = DirectoryReader.open(directory);
		//索引搜索工具
		IndexSearcher searcher = new IndexSearcher(reader);
		
		//返回结果是 按照匹配度排名得分前N名的文档
		TopDocs topDocs = searcher.search(query, 10);
        // 获取总条数
        System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");
        //获取得分文档对象（ScoreDoc）数组，ScoreDoc中包含；文档编号，文档得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
        	//取出文档编号
        	int docID = scoreDoc.doc;
        	//根据编号找文档
        	Document doc = reader.document(docID);
        	System.out.println("id====="+doc.get("id"));
        	System.out.println("title====="+doc.get("title"));
        	//取出得分
        	System.out.println("得分======"+scoreDoc.score);
        }
        System.out.println("====================================================================");
	}
	
	//词条查询
	@Test
	public void testTermQuery() throws Exception{
		Query query = new TermQuery(new Term("title", "*歌*"));
        System.out.println("==============================词条查询======================================");
		this.search(query);
	}
	
	//模糊查询
	@Test
	public void testFuzzyQuery() throws Exception{
		//创建模糊查询对象，允许用户输错，但是要求错误的最大编辑距离不能超过2
		//编辑距离：一个单词到另一个单词最少要修改的次数 facebool-->facebook,需要编辑一次编辑距离就是1
		//可以手动指定编辑距离但是编辑距离，但是参数必须在0~2之间
		Query query = new FuzzyQuery(new Term("title","facevoel"),2);
        System.out.println("==============================模糊查询======================================");
		this.search(query);
	}
	
	//数值范围查询
	/**
	 * 数值范围查询，数值范围查询，可以用来对非String类型的ID进行精确的查找
	 */
	@Test
	public void testNumbericRangeQuery() throws Exception{
		//数值范围查询对象，参数：字段名称，最小值，最大值，是否包含最小值，是否包含最大值
		Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);
        System.out.println("==============================数值范围查询======================================");
		search(query);
	}
	
	/**
	 * 组盒查询（布尔查询）
	 * 布尔查询本身没有查询条件，可以吧其他查询通过逻辑运算进行组合
	 * 交集：Occur.MUST+Occur.MUST
	 * 并集：Occur.SHOULD+Occur.SHOULD
	 * 非：Occur.MUST_NOT
	 */
	@Test
	public void testBooleanQuery() throws Exception{
		Query query1 = NumericRangeQuery.newLongRange("id", 1L, 3L, true, true);
		Query query2 = NumericRangeQuery.newLongRange("id", 2L, 4L, true, true);
		//创建布尔查询的对象
		BooleanQuery query= new BooleanQuery();
		//组合其他查询
		query.add(query1, BooleanClause.Occur.MUST_NOT);
		query.add(query2, BooleanClause.Occur.SHOULD);
		
		search(query);
	}
	
	/**
	 * 修改索引
	 */
//	1.创建文档存储目录
//	2.创建索引写入器配置对象
//	3.创建索引写入器
//	4.创建文档数据
//	5.修改
//	6.提交
//	7.关闭
	/**
	 * 测试：修改索引
	 * 注意：
	 * A,lucene修改功能底层会先删除，再把新的文档添加
	 * B,修改功能会根据Term进行匹配，所有匹配到的都会被删除。这样不好
	 * C,因此，一般我们修改时，都会根据一个唯一不重复字段进行匹配修改。例如ID
	 * D,但是词条搜索，要求ID必须是字符串。如果不是，这个方法就不能用。
	 * 如果ID是数值类型，我们不能直接取修改。可以先手动删除deleteDocuments（数值范围查询锁定ID）,再添加
	 */
//	@Test
	public void testUpdate() throws Exception{
		//创建目录对象
		Directory directory = FSDirectory.open(new File("E:\\EclipseForPersonal\\lucene\\Lucene_directory"));
		//创建配置对象
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_9, new IKAnalyzer());
		//创建索引写出工具
		IndexWriter writer = new IndexWriter(directory,conf);
		//创建新的文档数据
		Document doc = new Document();
		doc.add(new StringField("id","1",Store.YES));
		doc.add(new StringField("title","111谷歌地图之父跳槽facebook",Store.YES));
		/**
		 * 修改索引。参数：
		 * 词条：根据这个词条匹配到所有文档都会修改
		 * 文档信息：要修改的新的文档数据
		 */
		writer.updateDocument(new Term("id","1"), doc);
		//提交
		writer.commit();
		//关闭
		writer.close();
	}
	
	/*
     * 演示：删除索引
     * 注意：
     * 	一般，为了进行精确删除，我们会根据唯一字段来删除。比如ID
     * 	如果是用Term删除，要求ID也必须是字符串类型！
     */
//	@Test
	public void testDelete() throws Exception {
	    // 创建目录对象
		Directory directory = FSDirectory.open(new File("E:\\EclipseForPersonal\\lucene\\Lucene_directory"));
	    // 创建配置对象
	    IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_9, new IKAnalyzer());
	    // 创建索引写出工具
	    IndexWriter writer = new IndexWriter(directory, conf);
	
	    // 根据词条进行删除
	    		writer.deleteDocuments(new Term("id", "1"));
	
	    // 根据query对象删除,如果ID是数值类型，那么我们可以用数值范围查询锁定一个具体的ID
	    //		Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);
	    //		writer.deleteDocuments(query);
	
	    // 删除所有
//	    writer.deleteAll();
	    // 提交
	    writer.commit();
	    // 关闭
	    writer.close();
	}
	
}
