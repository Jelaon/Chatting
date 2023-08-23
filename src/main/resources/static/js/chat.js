var ws;

function wsOpen() {
	//웹소켓 전송시 현재 방의 번호를 넘겨서 보낸다.
	ws = new WebSocket("ws://" + location.host + "/chatting");
	wsEvt();
}

function wsEvt() {
	ws.onopen = function(data) {
		//소켓이 열리면 동작
	}

	ws.onmessage = function(data) {
		//메시지를 받으면 동작
		var msg = data.data;
		if (msg != null && msg.type != '') {
			//파일 업로드가 아닌 경우 메시지를 뿌려준다.
			var d = JSON.parse(msg);
			if (d.type == "getId") {
				var si = d.sessionId != null ? d.sessionId : "";
				if (si != '') {
					$("#sessionId").val(si);
				}
			} else if (d.type == "message") {
				if (d.sessionId == $("#sessionId").val()) {
					$(".display-chatting").append("<li class='myChat'><p>" + d.msg + "</p></li>");
				} else {
					$(".display-chatting").append("<li class='others'>" + d.userName + "</li><li><p>" + d.msg + "</p></li>");
				}

			} else {
				if (d.sessionId == $("#sessionId").val()) {
					sender = 'me'
				} else {
					sender = d.userName
				}
			}
		} else {
			//파일 업로드한 경우 업로드한 파일을 채팅방에 뿌려준다.

			var url = URL.createObjectURL(new Blob([msg]));
			if (sender == 'me') {
				$(".display-chatting").append("<img class='msgImg img' src=" + url + "><div class='clearBoth'></div>");
			} else {
				$(".display-chatting").append("<li class='others'>" + sender + "</li><img class='msgImg ' src=" + url + "><div class='clearBoth'></div>");
			}

		}
		const display = document.getElementsByClassName("display-chatting")[0];
		display.scrollTop = display.scrollHeight;
	}


	document.addEventListener("keypress", function(e) {
		if (e.keyCode == 13) { //enter press
			send();
		}
	});
}

function chatName() {
	var userName = $("#userName").val();
	if (userName == null || userName.trim() == "") {
		alert("사용자 이름을 입력해주세요.");
		$("#userName").focus();
	} else {
		wsOpen();
		$("#yourName").hide();
		$("#yourMsg").show();
	}
}

function send() {
	var option = {
		type: "message",
		sessionId: $("#sessionId").val(),
		userName: $("#userName").val(),
		msg: $("#message").val()
	}
	ws.send(JSON.stringify(option))
	$('#message').val("");
	 $('#sendBtn').attr('disabled', 'disabled');
}

function fileSend() {
	var file = document.querySelector("#fileUpload").files[0];
	var fileReader = new FileReader();
	fileReader.onload = function() {
		var param = {
			type: "fileUpload",
			file: file,
			sessionId: $("#sessionId").val(),
			msg: $("#message").val(),
			userName: $("#userName").val()
		}
		ws.send(JSON.stringify(param)); //파일 보내기전 메시지를 보내서 파일을 보냄을 명시한다.

		arrayBuffer = this.result;
		ws.send(arrayBuffer); //파일 소켓 전송
	};
	fileReader.readAsArrayBuffer(file);
	$('#fileUpload').val('')
}

$('#fileImg').click(function() {
	$('#fileUpload').click();
})

$('#fileUpload').change(function() {
	fileSend();
})

$('#imojiImg').click(function(){
	$("#message").focus()
	
})

$(document).ready(function() {
    $('#sendBtn').attr('disabled', 'disabled');
 
    $('#message').on('change', function() {
        if ($(this).val() !== '') {
            $('#sendBtn').removeAttr("disabled");
        }
        else {
            $('#sendBtn').attr('disabled', 'disabled');
        }
    });
});
