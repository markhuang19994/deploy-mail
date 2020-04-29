$(() => {
    (function version(){
        const version = '202004291157';
        const lineSplit = '<div class="line-split" style="margin: 6px 0 -12px 0;"></div>';
        $('#version').text('version:' + version);
        showPopupWithCookie(
            [
                '版本更新:', lineSplit,
                '[修正] Note填寫完畢，再按一次不會再刷掉之前填的了',
                '[其他] 頁面切換更加smooth',
            ].join('\n<br/>\n'),
            version,
            new Date('2020-05-03').getTime() + 86400 * 1000 * 14
        );
    })();
    (function index() {
        $.ajax({
            url: '/isLogin',
            success: d => {
                let page = 'index-body';
                const loginUser = d['loginUser'];
                if (loginUser) {
                    page = 'step1-body';
                    step1(loginUser);
                }
                $('#' + page).css('display', 'block');
            },
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        });

        $.ajax({
            type: 'GET',
            url: 'indexHandler/deployMailUsers',
            data: {},
            success: d => {
                if (d) {
                    const sel = document.getElementById('sel');
                    d.forEach(name => {
                        const opt = new Option();
                        opt.text = name;
                        sel.appendChild(opt)
                    });
                }
            },
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        });

        $('#goStep1').click(() => {
            changePageRightIn($('#index-body'), $('#step1-body'));
            step1($('#sel option:selected').text());
        });
    })();

    (function registerServiceWorker() {
        if ('serviceWorker' in navigator && 'PushManager' in window) {
            console.log('Service Worker and Push is supported');

            navigator.serviceWorker.register('/js/sw.js')
                .then(function (swReg) {
                    console.log('Service Worker is registered', swReg);
                })
                .catch(function (error) {
                    console.error('Service Worker Error', error);
                });
        } else {
            console.warn('Push messaging is not supported');
        }

        const defaultNotificationOption = {
            icon: '',
            body: '',
            image: '',
            data: {
                link: ''
            }
        };

        window.displayNotification = function (options) {
            if (Notification.permission === 'granted') {
                navigator.serviceWorker.getRegistration('/js/').then(reg => {
                    reg.showNotification('IISI Deploy Mail', options);
                    console.log('displayNotification');
                });
            }
        }
    })();


});
