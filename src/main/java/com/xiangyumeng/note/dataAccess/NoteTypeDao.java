package com.xiangyumeng.note.dataAccess;

import com.xiangyumeng.note.persistantObject.NoteType;
import com.xiangyumeng.note.utility.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
        String sql =  "select typeId,typeName,userId from tb_note_type where userId = ? ";

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
        String sql = "delete from tb_note_type where typeId = ?";

        // prams
        List<Object> params = new ArrayList<>();
        params.add(typeId);

        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }

    /**
     * checkTypeName
     * @param typeName
     * @param userId
     * @param typeId
     * @return result
     */
    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        String sql = "select * from tb_note_type where userId = ? and typeName = ?";

        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(typeName);

        NoteType noteType = (NoteType) BaseDao.queryRow(sql, params, NoteType.class);

        if (noteType == null){
            return 1;
        } else{
            if (typeId.equals(noteType.getTypeId().toString())){
                return 1;
            }
        }

        return 0;
    }

    /**
     * add type
     * @param typeName type name
     * @param userId user id
     * @return main key
     */
    public Integer addType(String typeName, Integer userId) {
        Integer key = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DBUtil.getConnection();

            String sql = "insert into tb_note_type (typeName, userId) values (?, ?)";

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, typeName);
            preparedStatement.setInt(2, userId);

            int row = preparedStatement.executeUpdate();

            if (row > 0){
                resultSet = preparedStatement.getGeneratedKeys();

                if(resultSet.next()){
                    key = resultSet.getInt(1);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            DBUtil.close(resultSet, preparedStatement, connection);
        }

        return key;
    }

    /**
     * update database
     * @param typeName type name
     * @param typeId type id
     * @return rows
     */
    public Integer updateType(String typeName, String typeId) {
        String sql = "update tb_note_type set typeName = ? where typeId = ?";

        List<Object> params = new ArrayList<>();

        params.add(typeName);
        params.add(typeId);

        int row = BaseDao.executeUpdate(sql, params);

        return row;
    }
}
