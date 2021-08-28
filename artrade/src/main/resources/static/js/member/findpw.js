let index= {
    init: function () {
        $("#findBtn").on("click", () => { //function(){}, ()=>{}this를 바인딩하기 위해
            this.save();
        });
    },
    save:function (){
        let data = {
            username : $("#username").val(),
            email : $("#email").val()

        };
        console.log(data); // 자바스크립트 오브젝트
        console.log(JSON.stringify(data));
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            type: "POST",
            url: "/member/findpw",
            data: JSON.stringify(data),
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json; charset=utf-8",
            dataType: "json",
        }).done(function (resp) {
            alert(resp.message);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}
index.init();