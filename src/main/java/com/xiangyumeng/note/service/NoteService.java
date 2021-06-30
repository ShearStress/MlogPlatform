package com.xiangyumeng.note.service;

import cn.hutool.core.util.StrUtil;
import com.xiangyumeng.note.dataAccess.NoteDao;
import com.xiangyumeng.note.persistantObject.Note;
import com.xiangyumeng.note.valueObject.ResultInfo;

public class NoteService {

    private NoteDao noteDao = new NoteDao();

    /**
     * add or update
     * @param typeId type id
     * @param title  title
     * @param content content
     * @return
     */
    public ResultInfo<Note> addOrUpdate(String typeId, String title, String content) {
        ResultInfo<Note> resultInfo = new ResultInfo<>();


        if (StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("Please select blog type!");
            return resultInfo;
        }

        if (StrUtil.isBlank(title)){
            resultInfo.setCode(0);
            resultInfo.setMsg("Blog title can not be empty!");
            return resultInfo;
        }

        if (StrUtil.isBlank(content)){
            resultInfo.setCode(0);
            resultInfo.setMsg("Blog content can not be empty!");
            return resultInfo;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(Integer.parseInt(typeId));
        resultInfo.setResult(note);

        // call dao method
        int row = noteDao.addOrUpdate(note);

        if (row > 0){
            resultInfo.setCode(1);
        } else{
            resultInfo.setCode(0);
            resultInfo.setResult(note);
            resultInfo.setMsg("update fail!");
        }

        return resultInfo;
    }
}
