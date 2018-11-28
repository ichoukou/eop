package net.ytoec.kernel.search.service.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.search.dataobject.EccoreItem;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchData {
	public static void main(String[] args) throws Exception {
		 
		SolrServer solrServer = getSolrServer();
		//solrServer.optimize();
         SolrQuery query = new SolrQuery();
        //
        // // String queryString = "customerId:000bc6ec493cea1eb0e8e57c8369d5b7";
        //
         query.setQuery("*:*");
        // // query.setQuery("customerId:4990614deed0227219bf67e35ad50b00");
        // // query.setFilterQueries("status=SIGNED");
        // // query.setQuery(queryString);
        // // query.setQuery("customerId:6c76ea2283c15d5f0786e4f0c322f811");
        // // query.setFilterQueries(new String[] { "createTime:1 2 3 4 5 6 7 8", "status:8" });
        // // query.setFilterQueries(new String[] { "phone:8" });
        // // query.setSortField("orderId", SolrQuery.ORDER.desc);
         query.setRows(10);
         query.setSortField("id", ORDER.desc);
        
         QueryResponse rsp = solrServer.query(query);
         System.out.println("查询时间："+rsp.getQTime());
         System.out.println("总数目：" + rsp.getResults().getNumFound());
         List<EccoreItem> beans = rsp.getBeans(EccoreItem.class);
         for (int i = 0; i < beans.size(); i++) {
         System.out.println(beans.get(i).getId() + "," + beans.get(i).getWeight());
         }
       
//        List<String> ids = new ArrayList<String>();
//        ids.add("1780472973");
//        ids.add("1780472968");
//        solrServer.deleteById(ids);
//        solrServer.commit();
	}

	public static CommonsHttpSolrServer getSolrServer() throws MalformedURLException {
		// 远程服务
        String url = "http://yfb1.solr/isolr/eccore/";

        // return new CommonsHttpSolrServer(urlString2);
        return new CommonsHttpSolrServer(url);
	}
}
