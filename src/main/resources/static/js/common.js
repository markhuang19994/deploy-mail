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

    const screenMask = $('#screen-mask');
    window.screenMask = function (zIndex) {
        screenMask.css({
            display: 'block',
            zIndex
        })
    };

    window.unScreenMask = function () {
        screenMask.css({
            display: 'none',
            zIndex: 0
        })
    };
})();

(function changePage() {

    function directionAnimate(ele, direction, newDirectionVal,
                              speed  /*animate speed ms*/, fps /* animate fps*/, callback) {
        speed = speed || 300
        fps = fps || 30
        const pureEle = $(ele)[0]
        const oldDirectionVal = pureEle.style[direction] || '0';
        const reg = new RegExp(/(-?\d*)(.*)/g);
        let unit = oldDirectionVal.replace(reg, '$2');

        let oldDirectionNum = 0;
        try {
            oldDirectionNum = parseInt(oldDirectionVal.replace(reg, '$1'));
        } catch (e) {
        }

        if (unit === '') {
            unit = newDirectionVal.replace(reg, '$2')
        }

        let newDirectionNum = 0;
        try {
            newDirectionNum = parseInt(newDirectionVal.replace(reg, '$1'))
        } catch (e) {
        }

        let frequency = speed / fps;
        frequency = frequency < 1 ? frequency : ~~frequency
        const gap = (Math.round(((newDirectionNum - oldDirectionNum) / speed) * frequency) * 100) / 100;

        let nowDirectionNum = oldDirectionNum;
        let isEnd = false;

        function _animate() {
            nowDirectionNum = nowDirectionNum + gap;

            const isNegativeLimit = newDirectionNum < oldDirectionNum && nowDirectionNum < newDirectionNum
            const isPositiveLimit = newDirectionNum > oldDirectionNum && nowDirectionNum > newDirectionNum
            if (isNegativeLimit || isPositiveLimit) {
                nowDirectionNum = newDirectionNum;
                isEnd = true;
            }

            pureEle.style[direction] = nowDirectionNum + unit;

            if (!isEnd) {
                setTimeout(_animate, frequency);
            } else {
                if (typeof callback === 'function') {
                    callback();
                }
            }
        }

        _animate();
    }

    window.slideOut = function (ele, direction = 'left', speed, callback) {
        const $ele = $(ele);
        const destCss = {};
        destCss[direction] = '-100%'
        directionAnimate(ele, direction, '-100%', speed, 30, callback)
        $ele.attr('direction', direction);
    };

    window.slideIn = function (ele, direction, speed, callback) {
        const $ele = $(ele);
        const destCss = {}
        destCss[direction] = '0'
        directionAnimate(ele, direction, '0', speed, 30, callback)
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
        slideIn(page, 'right');
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
        slideIn(page, 'left');
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
        slideIn(page, 'top');
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
        slideIn(page, 'bottom');
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
            })
        });
    };

    window.jsonObjToFormData = function (jsonObj) {
        const formData = new FormData();
        const keys = Object.keys(jsonObj);
        for (let i = 0; i < keys.length; i++) {
            const key = keys[i];
            formData.append(key, jsonObj[key]);
        }
        return formData;
    };

    window.setCookie = (cName, cValue, expDays) => {
        const d = new Date();
        d.setTime(d.getTime() + (expDays * 24 * 60 * 60 * 1000));
        const expires = "expires=" + d.toUTCString();
        document.cookie = cName + "=" + cValue + ";" + expires + ";path=/";
    };

    window.getCookie = (cName) => {
        const name = cName + "=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    };

    window.insertAtCursor = function (ele, val) {
        if (document.selection) {        //IE support
            ele.focus();
            const sel = document.selection.createRange();
            sel.text = val;
        } else if (ele.selectionStart || ele.selectionStart === '0') {//MOZILLA and others
            const startPos = ele.selectionStart;
            const endPos = ele.selectionEnd;
            ele.value = ele.value.substring(0, startPos)
                + val
                + ele.value.substring(endPos, ele.value.length);
            ele.selectionStart = startPos + val.length;
            ele.selectionEnd = startPos + val.length;
        } else {
            ele.value += val;
        }
    }
})();
