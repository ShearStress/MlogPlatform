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

    /**
     *
     * @param typeName type name
     * @param userId use id
     * @param typeId type id
     * @return
     */
    public ResultInfo<Integer> addOrUpdate(String typeName, Integer userId, String typeId) {
        ResultInfo<Integer> resultInfo = new ResultInfo<>();

        if (StrUtil.isBlank(typeName)){
            resultInfo.setCode(0);
            resultInfo.setMsg("type name can not be empty!");
            return resultInfo;
        }

        Integer code = typeDao.checkTypeName(typeName, userId, typeId);

        if (code == 0){
            resultInfo.setCode(0);
            resultInfo.setMsg("type name exists!");
            return resultInfo;
        }

        Integer key = null;
        if (StrUtil.isBlank(typeId)){
            key = typeDao.addType(typeName, userId);
        } else{
            key = typeDao.updateType(typeName, typeId);
        }

        if (key > 0){
            resultInfo.setCode(1);
            resultInfo.setResult(key);
        } else{
            resultInfo.setCode(0);
            resultInfo.setMsg("Update failed!");
        }

        return resultInfo;
    }
}
