const modal = document.getElementById("modal");


function modalOn() {
    modal.style.display = "flex"
	$('.modal-window').draggable();
	$('.modal-window').draggable({'cancel':'.content'});
}

function isModalOn() {
    return modal.style.display === "flex"
}

function modalOff() {
    modal.style.display = "none"
}

const btnModal = document.getElementById("emojiImg")
btnModal.addEventListener("click", e => {
    modalOn()
})

const closeBtn = modal.querySelector(".close-area")
closeBtn.addEventListener("click", e => {
    modalOff()
})

modal.addEventListener("click", e => {
    const evTarget = e.target
    if(evTarget.classList.contains("modal-overlay")) {
        modalOff()
    }
})

window.addEventListener("keyup", e => {
    if(isModalOn() && e.key === "Escape") {
        modalOff()
    }
})
$(function(){
	for(let i = 12; i < 80; i++){
	$('.content').append('<span class="addEmoji">&#1285' + i + ';</span>')
}
  $(document).on("click", ".addEmoji", function(e) {
	   msg = $('#message').val()
             $('#message').val(msg + e.target.textContent)
              modalOff()
	  $('#sendBtn').removeAttr("disabled");
            });
})

modalOff()

$('.addEmoji').each(function(index, emoji){
	 emoji.addEventListener('click', function(){
	  console.log(emoji)
	  msg = $('#message').val()
	  $('#message').val(msg + emoji.textContent)
	  modalOff()
	  $('#sendBtn').removeAttr("disabled");
  })
})
