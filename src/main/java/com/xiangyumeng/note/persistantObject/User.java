package com.xiangyumeng.note.persistantObject;

import lombok.Getter;
import lombok.Setter;

//using lombok plugins getter and setter, dependency in pom.xml
// don't need to write getter and setter again
@Getter
@Setter

public class User {

    // main key in data base
    private Integer userId;

    // user name
    private String uname;

    // user password
    private String upwd;

    // user nickname
    private String nick;

    // user icon
    private String head;

    // user mood
    private String mood;

}
