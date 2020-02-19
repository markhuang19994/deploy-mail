$(function () {
    const modal = document.getElementById("myModal");
    const closeSpan = modal.querySelector('.close');
    const modelP = document.getElementById('modal-content__p-1');
    let callBack;
    closeSpan.addEventListener('click', onClose);

    $(document).on('click', 'body', e => {
        if (e.target === modal) {
            onClose()
        }
    });

    function onClose() {
        modal.style.display = "none";
        if (callBack && typeof callBack === 'function') {
            callBack();
            callBack = null;
        }
    }

    window.showPopup = (text, cb) => {
        modelP.innerText = text;
        modal.style.display = "block";
        if (typeof cb === 'function') {
            callBack = cb
        }
    };

    window.showHtmlPopup = (html, cb) => {
        modelP.innerHTML = html;
        modal.style.display = "block";
        if (typeof cb === 'function') {
            callBack = cb
        }
    };

    window.closePopup = () => {
        callBack = null;
        $(closeSpan).trigger('click');
    };

    window.showPopupWithCookie = (message, cName, lastShowTime) => {
        const cookie = getCookie(cName);
        if (cookie === 'true') return;
        if (lastShowTime - Date.now() < 0 ) return;

        showHtmlPopup(message);
        setCookie(cName, 'true');
    }
});
