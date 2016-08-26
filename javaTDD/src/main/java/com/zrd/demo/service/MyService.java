package com.zrd.demo.service;

import com.zrd.demo.dao.IMyDAO;

/**
 * Created by Administrator on 2016-08-26.
 */
public class MyService {
    private IMyDAO myDao;
    public boolean exist(int id){
        return myDao.getNameById(id) != null;
    }
    public void hello(){

    }
    public void setMyDao(IMyDAO myDao) {
        this.myDao = myDao;
    }
}
