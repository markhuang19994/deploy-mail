$(() => {
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