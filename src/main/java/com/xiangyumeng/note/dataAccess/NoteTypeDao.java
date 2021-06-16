package com.xiangyumeng.note.dataAccess;

import com.xiangyumeng.note.persistantObject.NoteType;

import java.util.ArrayList;
import java.util.List;

public class NoteTypeDao {

    /**
     *             1. define sql:
     *                 String sql = "select typeId, typeName, userId from tb_note_type where userId = ?"
     *
     *             2. set parameters
     *             3. use basedao search method, return a collection
     * @return a collection
     */
    public List<NoteType> findTypeListByUserId(Integer userId){

        //1. define sql:
        String sql = "select typeId, typeName, userId from tb_note_type where userId = ?";

        //2. set parameters
        List<Object> params = new ArrayList<>();
        params.add(userId);

        //3. use basedao search method, return a collection
        List<NoteType> list = BaseDao.queryRows(sql, params, NoteType.class);
        return list;
    }
}
