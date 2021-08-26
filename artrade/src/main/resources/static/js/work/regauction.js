
// 업로드 버튼 클릭
$(document).ready(function (){
    $("#checkNFT").click(function () {
        let data = {
            id: $("#workSelect").val()

        };
        console.log(data); // 자바스크립트 오브젝트
        console.log(JSON.stringify(data));
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            type: 'POST',
            url: '/auction/registration', // 요청보낼 주소
            data: JSON.stringify(data),
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json; charset=utf-8",
            dataType: 'json',   // 응답의 content-type (json 타입으로 받겠다!)

            success: function (result){ // 요청을 성공적으로 했다면
                $('.modal-body').html(result.message);
                $('#resultbtn').html(result.guide);
                $('#UploadModal').modal();
                if(result.status === 'ok'){
                    $upload = $('#upload').attr('disabled', false);
                    $('#workExplanation').html(result.contents);

                }

            }
        });
    });
});




// 모달 안 버튼
$(document).ready(function (){
    $("#resultbtn").click(function () {
        $.ajax({
            type: 'GET',       // get 방식으로 전송
            url: '/auction/registration', // 요청보낼 주소
            dataType: 'json',    // 응답의 content-type (json 타입으로 받겠다!)
            success: function (result){ // 요청을 성공적으로 했다면
                if(result.status === "notLogin" ){
                    location.href="/login";
                }else if (result.status === "notCheckToken"){
                  //   location.href="/"; // TODO NFT 발급 주소로 바꿔주기
                }else if(result.status ==='ok'){
                    location.href="/openMarket";
                }else {
                    location.href="/";
                }
            }
        });
    });
});



// 업로드 버튼 클릭
$(document).ready(function (){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $("#upload").click(function () {
        let data = {
            id: $("#workSelect").val() ,
            defaultValue :  $("#defaultValue").val(),
            date :  $("#date").val(),
            time :  $("#time").val()
        };

        console.log(data); // 자바스크립트 오브젝트
        console.log(JSON.stringify(data));
        $.ajax({
            type: 'POST',
            url: '/auction/upload', // 요청보낼 주소
            data: JSON.stringify(data),
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json; charset=utf-8",
            dataType: 'json',    // 응답의 content-type (json 타입으로 받겠다!)
            success: function (result){ // 요청을 성공적으로 했다면
                $('.modal-body').html(result.message);
                $('#resultbtn').html(result.guide);
                $('#UploadModal').modal();


            }
        });
    });
});



// TODO 업로드 완료시 확인 클릭하면 mypage 이동
