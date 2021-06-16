package com.xiangyumeng.note.service;

import cn.hutool.core.util.StrUtil;
import com.xiangyumeng.note.dataAccess.NoteTypeDao;
import com.xiangyumeng.note.persistantObject.NoteType;
import com.xiangyumeng.note.valueObject.ResultInfo;

import java.util.List;

public class NoteTypeService {

    private NoteTypeDao typeDao = new NoteTypeDao();

    public List<NoteType> findTypeList(Integer userId){
        List<NoteType> typeList = typeDao.findTypeListByUserId(userId);
        return typeList;
    }

    /**
     * delete type
     *             1. check if params is empty
     *             2. call dao layer, search cloud blog numbers via typeId
     *             3. if # > 0, can be deleted
     *                 code =0, msg="this record can not be deleted"
     *             4. if no record, use update method in dao layer, delete it
     *             5. if return value > 1, code=1, else, code=0, msg="deletion failed!"
     * @param typeId id
     * @return result info
     */
    public ResultInfo<NoteType> deleteType(String typeId) {
        ResultInfo<NoteType> resultInfo = new ResultInfo<>();
        //1. check if params is empty
        if (StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("mul function! retry!");
            return resultInfo;
        }

        //2. call dao layer, search cloud blog numbers via typeId
        long noteCount = typeDao.findNoteCountByTypeId(typeId);

        //3. if # > 0, can be deleted
        if (noteCount > 0){
            resultInfo.setCode(0);
            resultInfo.setMsg("this type has child records, can not be deleted!");
            return resultInfo;
        }
        //code =0, msg="this record can not be deleted"

        //4. if no record, use update method in dao layer, delete it
        int row = typeDao.deleteTypeById(typeId);

        //5. if return value > 1, code=1, else, code=0, msg="deletion failed!"
        if (row > 0){
            resultInfo.setCode(1);
        } else{
            resultInfo.setCode(0);
            resultInfo.setMsg("deletion failed!");
        }

        return resultInfo;
    }
}
