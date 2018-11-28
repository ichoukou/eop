package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.UserCustom;

public interface UserCustomService<T extends UserCustom> extends BaseService<T> {

    public List<T> searchUserCustom(T userCustom);
    
    /**
     * 个性化删除
     * @return
     */
    public boolean individuationDelete(T userCustom);

}
