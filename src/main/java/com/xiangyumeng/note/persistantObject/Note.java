package com.xiangyumeng.note.persistantObject;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Note {

    private Integer noteId; //blog id
    private String title;   //blog title
    private String content; //blog content
    private Integer typeId; //blog id
    private Date pubTime;   //publish time
}
