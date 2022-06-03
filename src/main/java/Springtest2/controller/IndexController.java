package Springtest2.controller;

import Springtest2.Entity.TestEntity;
import Springtest2.Entity.TestRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")    // : 현재클래스는 home 매핑된다.
public class IndexController {

    @GetMapping("/main")
    public String home(){return "main";} // 타임리프 사용시 리턴값은 html명
    @Autowired
    TestRepository testRepository;

    // 2. main.js 내 작성메소드가 요청하는 url
    @GetMapping("/save")
    @ResponseBody   // Response(응답)Body(객체) : java객체를 반환하겠다. html아닌 객체 반환
    @Transactional
    public String getdata(HttpServletRequest request) {
        //0 변수요청
       String content = request.getParameter("content");
        // 1 엔티티생성
       TestEntity testEntity = new TestEntity();
        testEntity.content= content;
        // 2 엔티티 save를해주는
        testRepository.save(testEntity);
        // 3 반환
        return "작성성공";
    }
    // 3. main.js 내 호출 메소드가 요청하는 url 정의
    @GetMapping("/getlist")
    @Transactional
    public void getlist(HttpServletResponse response) {
        // 1. 모든 엔티티를 호출하기
       List<TestEntity> testEntities = testRepository.findAll();
       // 2. JSON화 하기
        JSONArray jsonArray = new JSONArray();
        for(TestEntity entity : testEntities) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("no",entity.no);
            jsonObject.put("content",entity.content);
            jsonArray.put(jsonObject);
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        } catch (IOException e) {throw new RuntimeException(e);

        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {

        // 변수요청
        int no = Integer.parseInt(request.getParameter("no"));
        // 1. pk 값을 이용한 엔티티 찾기
        Optional<TestEntity> optionalTestEntity =  testRepository.findById(no);
        // 2. Optional 객체내 엔티티 삭제
        TestEntity entity = optionalTestEntity.get();
        testRepository.delete(entity );

        return "1";

    }


    @GetMapping("/update")
    @ResponseBody
    @Transactional // 트랜잭션 :
    public String update(HttpServletRequest request) {

        // 0.  변수 요청
        int no = Integer.parseInt(request.getParameter("no")) ;
        String content = request.getParameter("content");

        // 1. pk값 이용한 엔티티 찾기
        Optional<TestEntity>  optionalTestEntity = testRepository.findById(no);
                    // Optional클래스  : 제네릭 클래스의 객체를 저장 [ 만약에 null이면 null 저장 ]
        // 2. Option 엔티티 뻬오기 [Optional 갹채내 실제 entity 가져오기]
        TestEntity entity = optionalTestEntity.get();
        // 3. 수정
        entity.content = content;
        return "1";

    }

    // @RequestMapping : 모든 url 매핑가능
    // @GetMapping : GET 메소드 url 매핑 [ 요청 변수 보인다 = 보안x 캐시 o ]
    // @PostMapping : POST 메소드 URL매핑 [ 요청 변수 x = 보안o 캐시 x ]
//////////////////////스프링에서 지원하는 요청방식 구분 /////////////////////////////////////////////
    // @PutMapping : PUT 메소드  URL 매핑 [ 삽입, 수정사용 ]
    // @DeleteMapping : DELETE 메소드 URL 매핑 [ 삭제시 사용 ]
    // @PathVariable : 경로에 변수를 바인딩 (넘겨주기)

    // 멱등성 : 요청후에 서버에 상태를 남기기
    // 반복되는 많은 요청이 이씅ㄹ경우 post 속도가 느리다!!


}
