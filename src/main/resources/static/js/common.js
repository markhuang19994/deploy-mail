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
            $ele.animate({left: '-100%'}, speed);
            $ele.attr('direction', 'left');
        } else {
            $ele.animate({right: '-100%'}, speed);
            $ele.attr('direction', 'right');
        }
    };

    window.slideIn = function (ele, direction, speed = 350, callback) {
        const $ele = $(ele);
        if (direction === 'left') {
            $ele.animate({left: '0'}, speed);
        } else {
            $ele.animate({right: '0'}, speed);
        }
        $ele.attr('direction', '');
    };

    window.changePage = function (oldPage, page) {
        const $page = $(page);
        $page.css({
            display: 'block',
            right: '-100%',
            left: ''
        });
        slideOut(oldPage, 'left', () => $(oldPage).css('display', 'none'));
        slideIn(page, 'right')
    };
})();