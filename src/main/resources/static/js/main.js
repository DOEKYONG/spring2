$(function(){
호출();
})

function 작성() {
$.ajax({ // ajax 비동기 사용시 제이쿼리 필수
    url : 'save', // 자바 통신할 url 작성
    data : {"content" : $("#content").val()}, // 보낼값
    success : function(re) { // 응답데이터
        $("#content").val(""); 호출();
    }
})
}

let list = [];

// 모든 방문록 호출하는 메소드
function 호출() {
    $.ajax({
        url : 'getlist',
        success : function(re){
            list = re;
            console.log(re);
           // alert(list[0]['content'])

            let html =
             '<tr>' +
                    '<th> 번호 </th> <th> 내용 </th> <th> 비고 </th>'   +

                   ' </tr>'

            for(let i =0; i<list.length; i++) {
                html +=
                   '<tr>'+
                          '<td> '+list[i]['no']+' </td>'  +
                          '<td> '+list[i]['content']+' </td>'  +
                            '<td><button onclick="수정('+list[i]['no']+')">수정</button>'+
                            '<button onclick="삭제('+list[i]['no']+')">삭제</button></td>'+
                        '</tr>'

            }
            $("#viewtable").html(html);
        }

    })
}

// 삭제
function 삭제(no) {
     $.ajax({
            url : "delete" ,
            data : {"no" : no},
            success : function(re) {
                if(re == "1") {
                alert("삭제성공")

                } else {
                alert("삭제실패")

                }
                      호출();
            }
        })
}

function 수정(no) {

    $.ajax({
        url : "update" ,
        data : {"no" : no, "content" : $("#content").val() },
        success : function(re) {
            if(re == "1") {
            alert("수정성공")

            } else {
            alert("실패")

            }
                  호출();
        }
    })
}