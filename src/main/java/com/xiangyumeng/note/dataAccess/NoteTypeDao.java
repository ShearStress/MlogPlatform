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


    /**
     * search for cloud blog numbers via typeId
     * @param typeId type id
     * @return number of records
     */
    public long findNoteCountByTypeId(String typeId) {
        // sql
        String sql = "select count(1) from tb_note where typeId = ?";

        // prams
        List<Object> params = new ArrayList<>();
        params.add(typeId);

        // base dao
        long count = (long) BaseDao.findSingleValue(sql, params);

        return count;
    }


    /**
     * delete type records based on typeId
     * @param typeId type id
     * @return result
     */
    public int deleteTypeById(String typeId) {
        String sql = "delete from tb_note where typeId = ?";

        // prams
        List<Object> params = new ArrayList<>();
        params.add(typeId);

        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }
}
