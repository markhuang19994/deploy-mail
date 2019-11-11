$(function () {
    const modal = document.getElementById("myModal");
    const closeSpan = modal.querySelector('.close');
    const modelP = document.getElementById('modal-content__p-1');
    let callBack;

    window.showPopup = (text, cb) => {
        modelP.innerText = text;
        modal.style.display = "block";
        if (typeof cb === 'function'){
            callBack = cb
        }
    };

    window.closePopup = () => {
        callBack = null;
        $(closeSpan).trigger('click');
    };

    closeSpan.addEventListener('click', onClose);

    $(document).on('click', 'body', e => {
        if (e.target === modal) {
            onClose()
        }
    });

    function onClose(){
        modal.style.display = "none";
        if (callBack && typeof callBack === 'function') {
            callBack();
            callBack = null;
        }
    }
});
