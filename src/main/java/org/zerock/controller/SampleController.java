package org.zerock.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@RestController
@Controller
@RequestMapping("/sample/*")
@Log4j2
public class SampleController {

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')")
    @GetMapping("/annoMember")
    public void doMember2() {

        log.info("logined annotation member");
    }
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/annoAdmin")
    public void doAdmin2() {

        log.info("admin annotation only");
    }

    @GetMapping("/all")
    public void doAll() {

        log.info("do all can access everybody");
    }

    @GetMapping("/member")
    public void doMember() {

        log.info("logined member");
    }

    @GetMapping("/admin")
    public void doAdmin() {

        log.info("admin only");
    }

    // 단순 문자열 반환
    @GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8")
    public String getText() {

        log.info("MIME TYPE : " + MediaType.TEXT_PLAIN_VALUE);

        return "안녕하세요";
    }

    /* 객체의 반환 */
    @GetMapping(value = "/getSample"
            , produces = { MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public SampleVO getSample() {
        return new SampleVO(112, "스타", "로드");
    }

    // produces 속성 생략 가능
    @GetMapping(value = "/getSample2")
    public SampleVO getSample2() {
        return new SampleVO(113, "로켓", "라쿤");
    }

    /* 컬렉션 타입의 객체 반환 */
    // 리스트 반환
    @GetMapping(value = "/getList")
    public List<SampleVO> getList() {

        return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + "Last"))
                .collect(Collectors.toList());
    }

    // 맵 반환
    @GetMapping(value = "/getMap")
    public Map<String, SampleVO> getMap() {

        Map<String, SampleVO> map = new HashMap<>();
        map.put("First", new SampleVO(111, "그루트", "주니어"));

        return map;
    }

    /* ResponseEntity 반환 */
    @GetMapping(value = "/check", params = { "height", "weight" })
    public ResponseEntity<SampleVO> check(Double height, Double weight) {

        SampleVO vo = new SampleVO(0, "" + height, "" + weight);

        ResponseEntity<SampleVO> result = null;

        if(height < 150) {
            result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
        } else {
            result = ResponseEntity.status(HttpStatus.OK).body(vo);
        }

        return result;
    }

    @GetMapping("/product/{cat}/{pid}")
    public String[] getPath(@PathVariable("cat") String cat,
                            @PathVariable("pid") String pid) {

        return new String[] { "category : " + cat , "productid : " + pid };
    }

    @PostMapping("/ticket")
    public Ticket convert(@RequestBody Ticket ticket) {

        log.info("convert.......ticket" + ticket);

        return ticket;

    }

    /* 파일 업로드 제출 후 처리 */
    @PostMapping("/exUploadPost")
    public void exUploadPost(ArrayList<MultipartFile> files) {

        files.forEach(file -> {
            log.info("------------------------------------");
            log.info("name : " + file.getOriginalFilename());
            log.info("size : " + file.getSize());
        });
    }

    /* 파일 업로드 화면으로 이동 */
    @GetMapping("/exUpload")
    public void exUpload() {
        log.info("/exUpload..........");
    }

    @GetMapping("/ex07")
    public ResponseEntity<String> ex07() {
        log.info("/ex07..........");

        // {"name" : "홍길동"}
        String msg = "{\"name\": \"홍길동\"}";

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>(msg, header, HttpStatus.OK);
    }

    @GetMapping("/ex06")
    public @ResponseBody SampleDTO ex06() {
        log.info("/ex06..........");
        SampleDTO dto = new SampleDTO();
        dto.setAge(10);
        dto.setName("홍길동");

        return dto;
    }

    /* view 로 데이터 전달 : 객체 타입과 기본 자료형의 비교 */
    @GetMapping("/ex04")
    public String ex04(SampleDTO dto, @ModelAttribute("page") int page) {

        log.info("dto : " + dto);
        log.info("page : " + page);

        return "/sample/ex04";
    }

    /* 파라미터를 변환해서 처리해야 하는 경우 ex) Date 타입 */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(java.util.Date.class,
                new CustomDateEditor(dateFormat, false));// true : null 값 허용, false : null 값 불허
        // 커스텀 데이터 타입 변환 작업을 하는 데이터에 대해서 typeMismatch 의 경우 에러 메시지가 표시되도록 할 수 있음
    }

    @GetMapping("/ex03")
    public String ex03(TodoDTO todo) {

        log.info("todo : " + todo);

        return "ex03";
    }

    /* 여러 개의 객체 타입을 처리하는 경우 */
    @GetMapping("/ex02Bean")
    public String ex02Bean(SampleDTOList list) {

        log.info("list dtos : " + list);

        return "ex02Bean";
    }

    /* 동일한 이름의 파라미터가 여러 개 전달되는 경우 : 리스트, 배열 처리 */
    @GetMapping("/ex02List")
    public String ex02List(@RequestParam("ids")ArrayList<String> ids) {

        log.info("ids : " + ids);

        return "ex02List";
    }

    @GetMapping("/ex02Array")
    public String ex02Array(@RequestParam("ids")String[] ids) {

        log.info("array ids : " + Arrays.toString(ids));

        return "ex02Array";
    }

    /* 기본 자료형이나 문자열 등 이용시, @RequestParam 이용해 파라미터의 타입만을 맞게 선언 */
    @GetMapping("/ex02")
    public String ex02(@RequestParam("name") String name, @RequestParam("age") int age) {

        log.info("name : " + name);
        log.info("age : " + age );

        return "ex02";
    }

    /* 파라미터의 수집 - 파라미터 타입에 따라 자동으로 변환*/
    @GetMapping("/ex01")
    public String ex01(SampleDTO dto) {

        log.info("" + dto);

        return "ex01";
    }

    @RequestMapping(value="/basic", method = {RequestMethod.GET, RequestMethod.POST})
    public void basicGet() {

        log.info("basic get...............");

    }

    @GetMapping("/basicOnlyGet")
    public void basicGet2() {

        log.info("basic get only get.................");

    }

    @RequestMapping("")
    public void basic() {

        log.info("basic...................");

    }

    @GetMapping("/doA")
    public void doA() {

        log.info("doA called.............");
        log.info("-----------------------");
    }
}
