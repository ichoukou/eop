package net.ytoec.kernel.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.UserService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext*.xml")
public class EccoreSearchServiceImplTest extends AbstractJUnit38SpringContextTests {

    @Inject
    private EccoreSearchService eccoreSearchService;

    @Inject
    private UserService<User>   userService;
    String                      urlString = "http://yfb0.solr/isolr/eccore";


    @Test
    public void eccoreSearchService() throws Exception {
    	eccoreSearchService.deleteBefore7Days(urlString);
    }
    // @Test
    public void testBuildEccoreData() throws Exception {
    	String mobile = "18801807219";
    	System.out.print(mobile);
    	if (mobile.matches("^\\d{11}$") || mobile.matches("^0\\d{2,3}-\\d{5,9}|0\\d{2,3}-\\d{5,9}$")) {
    		String s = mobile;
    		System.out.print(s);
    	}
    /*    // eccoreSearchService.buildEccoreData("http://yfb.ec.net.cn/isolr/eccore", 0, 100);
        int limit = 100;
        for (int i = 106341918; i < 106342917;) {
            System.out.println(i);
            eccoreSearchService.buildEccoreData(urlString, i, limit);
            i += limit;
        }*/
        // eccoreSearchService.buildEccoreData(urlString, 171307037 , 1);
        // eccoreSearchService.buildEccoreData(urlString, 171307038 , 1);
    }

    public void testSearchEccoreData() throws Exception {

        // File file=new File("D:\\data\\user_mail");
        // BufferedWriter output = new BufferedWriter(new FileWriter(file));
        //
        // try {
        //
        //
        // Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("startDate", "2012-05-01");
        // params.put("endDate", "2012-05-31");
        // List<User> users=userService.getUserListByUserType("1");
        // User user=new User();
        //
        //
        //
        //
        // for (int i = 0; i < users.size(); i++) {
        //
        //
        // user=users.get(i);
        // params.put("customerIDs", user.getTaobaoEncodeKey());
        // searchPage.setParams(params);
        // eccoreSearchService.searchEccoreData(urlString, searchPage);
        // System.out.println(user.getUserName()+","+user.getShopName()+","+user.getMobilePhone()+","+user.getTelePhone()+","
        // +searchPage.getTotalRecords());
        // output.write(user.getUserName()+","+user.getShopName()+","+user.getMobilePhone()+","+user.getTelePhone()+","
        // +searchPage.getTotalRecords()+"\r\n");
        // searchPage.setRecords(null);
        // }
        // output.close();
        //
        // }
        // catch (Exception e) {
        // output.close();
        // }

        Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
        Map<String, String> params = new HashMap<String, String>();
        params.put("startDate", "2012-05-01");
        params.put("endDate", "2012-05-31");
        searchPage.setParams(params);
        Map<String, Integer> resutlMap = eccoreSearchService.searchEccoreDataGroupCustomerId(urlString, searchPage);
        // Map< Short, Integer> resutlMap=eccoreSearchService.searchEccoreDataGroupStatus(urlString, searchPage);
        System.out.println("total:" + resutlMap.size());
        int base = 100;
        Integer[] numsIntegers = new Integer[30];
        Integer[] nums = new Integer[30];
        for (int i = 0; i < 30; i++) {
            numsIntegers[i] = (i + 1) * base;
            nums[i] = 0;
        }

        Integer n99 = 0;
        for (Entry<String, Integer> entry : resutlMap.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());

            for (int i = 29; i >= 0; i--) {
                if (entry != null && entry.getValue() >= numsIntegers[i]) {
                    nums[i] = nums[i] + 1;
                    break;
                }
                if (entry != null && entry.getValue() < 100) {
                    n99++;
                    break;
                }
            }

        }
        System.out.println("group总数" + resutlMap.size());
        System.out.println("小于100" + n99);
        for (int i = 0; i < nums.length; i++) {
            System.out.println(numsIntegers[i] + ":" + nums[i]);
        }
    }

    public void testSearchEccoreDataGroupStatus() throws Exception {
        Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
        Map<String, String> params = new HashMap<String, String>();
        params.put("startDate", "2012-02-01");
        params.put("endDate", "2012-03-01");
        params.put("customerIDs", "25258063966795f5436a621eea3aa4be");
        // params.put("numStatus", "8");
        searchPage.setParams(params);
        eccoreSearchService.searchEccoreDataGroupStatus(urlString, searchPage);
    }

    // @Test
    public void searchEccoreDataGroupCustomerId() throws Exception {

        File file = new File("D:\\data\\user_mail");
        BufferedWriter output = new BufferedWriter(new FileWriter(file));

        try {

            Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
            Map<String, String> params = new HashMap<String, String>();
            params.put("startDate", "2012-05-01");
            params.put("endDate", "2012-05-31");
            searchPage.setParams(params);
            Map<String, Integer> resutlMap = eccoreSearchService.searchEccoreDataGroupCustomerId(urlString, searchPage);
            for (Entry<String, Integer> entry : resutlMap.entrySet()) {
                System.out.println(entry.getKey() + "," + entry.getValue());
                output.write(entry.getKey() + "," + entry.getValue() + "\r\n");
            }
            output.close();
        } catch (Exception e) {
            output.close();
        }

    }

   //  @Test
    public void searchEccoreData() throws Exception {

        File file = new File("D:\\data\\user20120726");
        BufferedWriter output = new BufferedWriter(new FileWriter(file));

        try {

            List<User> users = userService.getUserListByUserType("1");

            User user = new User();

            Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 1);
            Map<String, String> params = new HashMap<String, String>();
            params.put("startDate", "2012-07-01");
            params.put("endDate", "2012-07-26");

            for (int i = 0; i < users.size(); i++) {

                user = users.get(i);

                params.put("customerIDs", user.getTaobaoEncodeKey());
                searchPage.setParams(params);
                eccoreSearchService.searchEccoreData(urlString, searchPage);

                output.write(user.getUserName() + "," + user.getShopName() + "," + user.getTaobaoEncodeKey() + ","
                             + searchPage.getTotalRecords());
            }

            output.close();
        } catch (Exception e) {
            output.close();
        }

    }
}
