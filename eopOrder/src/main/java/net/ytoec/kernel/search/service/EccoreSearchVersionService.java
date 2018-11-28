package net.ytoec.kernel.search.service;

public interface EccoreSearchVersionService {

    /**更新状态和重量到solr
     * @param solrUrl
     * @param idTypeMap
     * @param limit
     */
    public void buildPartStatusWeightData(String solrUrl, Integer limit);
   }
