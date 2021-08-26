
// 경매제안 버튼 클릭
$(document).ready(function (){
    $("#suggest").click(function () {
        $.ajax({
            type: 'GET',       // get 방식으로 전송
            url: '/auction/suggest', // 요청보낼 주소
            dataType: 'json',    // 응답의 content-type (json 타입으로 받겠다!)
            success: function (result){ // 요청을 성공적으로 했다면
                $('.modal-body').html(result.message);

                if(result.status === "notLogin" ||  result.status === "notWallet"){
                    $('#confirm').html(result.guide);
                    $('#exampleModal').modal();
                }else{
                    $('#offer').html(result.guide);
                    $('#auctionModal').modal();
                }
            }
        });
    });
});

// 모달 안 버튼
$(document).ready(function (){
    $("#confirm").click(function () {
        $.ajax({
            type: 'GET',       // get 방식으로 전송
            url: '/auction/suggest', // 요청보낼 주소
            dataType: 'json',    // 응답의 content-type (json 타입으로 받겠다!)
            success: function (result){ // 요청을 성공적으로 했다면
                if(result.status === "notLogin" ){
                    location.href="/login";
                }
            }
        });
    });
});


