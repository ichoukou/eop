package net.ytoec.kernel.action.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;


public class WayBillActionHelper {

    /**
     * EccoreSearchResultDTO的list转换成 map
     * @param list
     * @return
     */
    public static Map<String, EccoreSearchResultDTO> ListEccoreSearchResultDTOToMap(List<EccoreSearchResultDTO> list){
        
        Map<String, EccoreSearchResultDTO> map=new HashMap<String, EccoreSearchResultDTO>();
        if (list==null||list.isEmpty()) {
            return map;
        }
        for (EccoreSearchResultDTO eccoreSearchResultDTO : list) {
            map.put(eccoreSearchResultDTO.getMailNo(), eccoreSearchResultDTO);
        }
        
        return map;
    }
}
