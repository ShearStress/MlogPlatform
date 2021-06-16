package com.xiangyumeng.note.service;

import com.xiangyumeng.note.dataAccess.NoteTypeDao;
import com.xiangyumeng.note.persistantObject.NoteType;

import java.util.List;

public class NoteTypeService {

    private NoteTypeDao typeDao = new NoteTypeDao();

    public List<NoteType> findTypeList(Integer userId){
        List<NoteType> typeList = typeDao.findTypeListByUserId(userId);
        return typeList;
    }
}
