package org.zerock.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BoardVO {

    private Long bno;
    private String title;
    private String content;
    private String writer;
    private Date regdate;
    private Date updatedate;

    private int replyCnt;

    private List<BoardAttachVO> attachList;

}
