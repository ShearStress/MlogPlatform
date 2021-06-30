package com.xiangyumeng.note.dataAccess;

import com.xiangyumeng.note.persistantObject.Note;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class NoteDao {

    /**
     *  add or update
     * @param note coming from service layer
     * @return rows that are affected
     */
    public int addOrUpdate(Note note) {

        String sql = "insert into tb_note (typeId, title, content, pubTime) values (?, ?, ?, now())";

        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());

        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }


}
