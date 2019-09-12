(function loading() {
    const loading = document.querySelector('.loading');
    const defaultLoadingText = 'Loading';

    window.startLoading = function (text) {
        loading.querySelector('.loadingTxt').innerText = text || defaultLoadingText;
        loading.classList.add('on');
    };

    window.endLoading = function () {
        loading.querySelector('.loadingTxt').innerText = defaultLoadingText;
        loading.classList.remove('on');
    };
})();

(function changePage() {
    window.slideOut = function (ele, direction = 'left', speed = 350, callback) {
        const $ele = $(ele);
        if (direction === 'left') {
            $ele.animate({left: '-100%'}, speed, callback);
            $ele.attr('direction', 'left');
        } else if ((direction === 'right')) {
            $ele.animate({right: '-100%'}, speed, callback);
            $ele.attr('direction', 'right');
        } else if ((direction === 'top')) {
            $ele.animate({top: '-100%'}, speed, callback);
            $ele.attr('direction', 'top');
        } else {
            $ele.animate({bottom: '-100%'}, speed, callback);
            $ele.attr('direction', 'bottom');
        }
    };

    window.slideIn = function (ele, direction, speed = 350, callback) {
        const $ele = $(ele);
        if (direction === 'left') {
            $ele.animate({left: '0'}, speed, callback);
        } else if ((direction === 'right')) {
            $ele.animate({right: '0'}, speed, callback);
        } else if ((direction === 'top')) {
            $ele.animate({top: '0'}, speed, callback);
        } else {
            $ele.animate({bottom: '0'}, speed, callback);
        }
        $ele.attr('direction', '');
    };

    window.changePageRightIn = function (oldPage, page) {
        const $page = $(page);
        $page.css({
            display: 'block',
            top: '',
            bottom: '',
            right: '-100%',
            left: ''
        });
        slideOut(oldPage, 'left', null, () => $(oldPage).css('display', 'none'));
        slideIn(page, 'right')
    };

    window.changePageLeftIn = function (oldPage, page) {
        const $page = $(page);
        $page.css({
            display: 'block',
            top: '',
            bottom: '',
            right: '',
            left: '-100%'
        });
        slideOut(oldPage, 'right', null, () => $(oldPage).css('display', 'none'));
        slideIn(page, 'left')
    };

    window.changePageTopIn = function (oldPage, page) {
        const $page = $(page);
        $page.css({
            display: 'block',
            top: '-100%',
            bottom: '',
            right: '',
            left: ''
        });
        slideOut(oldPage, 'bottom', null, () => $(oldPage).css('display', 'none'));
        slideIn(page, 'top')
    };

    window.changePageBottomIn = function (oldPage, page) {
        const $page = $(page);
        $page.css({
            display: 'block',
            top: '',
            bottom: '-100%',
            right: '',
            left: ''
        });
        slideOut(oldPage, 'top', null, () => $(oldPage).css('display', 'none'));
        slideIn(page, 'bottom')
    };

    window.getUserData = function () {
        return new Promise(res => {
            $.ajax({
                type: 'POST',
                url: 'base/getUserData',
                data: {},
                success: d => {
                    res(d);
                },
                error: e => {
                    console.log(e['responseJSON']);
                    showPopup('出現了錯誤，詳情請看console。');
                    res();
                }
            }).done(() => {
                endLoading()
            });
        });
    }


})();