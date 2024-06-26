package org.zerock.mapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class BoardMapperTests {

    @Setter(onMethod_ = { @Autowired })
    private BoardMapper mapper;

    @Test
    public void testGetlist() {
        mapper.getList().forEach(board -> log.info(board));
    }

    @Test
    public void testPaging() {
        Criteria cri = new Criteria();

        // 10개씩 3페이지
        cri.setPageNum(3);
        cri.setAmount(10);

        List<BoardVO> list = mapper.getListWithPaging(cri);

        list.forEach(board -> log.info(board));
    }

    @Test
    public void testInsert() {
        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글");
        board.setContent("새로 작성하는 내용");
        board.setWriter("newbie");

        mapper.insert(board);

        log.info(board);
    }

    @Test
    public void testInsertSelectKey() {
        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글 select Key");
        board.setContent("새로 작성하는 내용 select Key");
        board.setWriter("newbie");

        mapper.insertSelectKey(board);

        log.info(board);
    }

    @Test
    public void testRead() {
        BoardVO board = mapper.read(5L);

        log.info(board);
    }

    @Test
    public void testDelete() {
        log.info("DELETE COUNT : " + mapper.delete(3L));
    }

    @Test
    public void testUpdate() {

        BoardVO board = new BoardVO();

        board.setBno(5L);
        board.setTitle("Modified Title");
        board.setContent("Modified Content");
        board.setWriter("user00");

        int count = mapper.update(board);
        log.info("UPDATE COUNT : " + count);
    }

    @Test
    public void testSearch() {

        Criteria cri = new Criteria();
        cri.setKeyword("새로");
        cri.setType("TC");

        List<BoardVO> list = mapper.getListWithPaging(cri);

        list.forEach(board -> log.info(board));
    }

}
