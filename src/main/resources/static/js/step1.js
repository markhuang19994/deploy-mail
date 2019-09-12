function step1(engName) {
    const $senderName = $('#senderName');

    $.ajax({
        type: 'GET',
        url: '/login',
        data: {engName},
        success: d => {
            if (d) {
                const engName = d['engName'];
                const checkinConfig = d['checkinConfig'];
                const checkoutConfig = d['checkoutConfig'];
                const checksumConfig = d['checksumConfig'];

                if (engName) {
                    $senderName.val(engName);
                }
                if (checkinConfig) {
                    const defaultSendTo = checkinConfig['defaultSendTo'];
                    if (defaultSendTo && Array.isArray(defaultSendTo)) {
                        $('#checkin-default-send-to').val(defaultSendTo.join(';\n'))
                    }
                    const defaultSendCC = checkinConfig['defaultSendCC'];
                    if (defaultSendCC && Array.isArray(defaultSendCC)) {
                        $('#checkin-default-send-cc').val(defaultSendCC.join(';\n'))
                    }
                }

                if (checksumConfig) {
                    const defaultSendTo = checksumConfig['defaultSendTo'];
                    if (defaultSendTo && Array.isArray(defaultSendTo)) {
                        $('#checksum-default-send-to').val(defaultSendTo.join(';\n'))
                    }
                    const defaultSendCC = checksumConfig['defaultSendCC'];
                    if (defaultSendCC && Array.isArray(defaultSendCC)) {
                        $('#checksum-default-send-cc').val(defaultSendCC.join(';\n'))
                    }
                }

                if (checkoutConfig) {
                    const defaultSendTo = checkoutConfig['defaultSendTo'];
                    if (defaultSendTo && Array.isArray(defaultSendTo)) {
                        $('#checkout-default-send-to').val(defaultSendTo.join(';\n'))
                    }
                    const defaultSendCC = checkoutConfig['defaultSendCC'];
                    if (defaultSendCC && Array.isArray(defaultSendCC)) {
                        $('#checkout-default-send-cc').val(defaultSendCC.join(';\n'))
                    }
                }

                if (d['engName']) {
                    $senderName.text(d['engName']);
                }
            }
        },
        error: e => {
            console.log(e['responseJSON']);
            showPopup('出現了錯誤，詳情請看console。');
        }
    });

    $('#checkin-send-btn').click(async () => {
        const searchParams = new URLSearchParams(window.location.search);
        const jenkinsJobName = searchParams.get('jenkinsJobName') || null;
        const jenkinsBuildNum = searchParams.get('jenkinsBuildNum') || null;
        const projectName = searchParams.get('projectName') || null;
        const lacrNo = searchParams.get('lacrNo') || null;

        let data = {
            jenkinsJobName,
            jenkinsBuildNum,
            projectName,
            lacrNo,
            senderName: $senderName.val()
        };

        if (document.getElementById('switch_demo').checked) {
            const userData = await getUserData();
            $.extend(data, {}, {
                checkinDefaultSendTo: userData['mailAccount'],
                checkinDefaultSendCc: ''
            });
        } else {
            $.extend(data, {}, {
                checkinDefaultSendTo: $('#checkin-default-send-to').val(),
                checkinDefaultSendCc: $('#checkin-default-send-cc').val()
            });
        }

        startLoading('信件寄送中');
        $.ajax({
            type: 'POST',
            url: 'mailHandler/checkin',
            data,
            success: d => {
                showPopup(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done(() => {
            endLoading()
        });
    });

    $('#checkout-send-btn').click(async () => {
        const searchParams = new URLSearchParams(window.location.search);
        const jenkinsJobName = searchParams.get('jenkinsJobName') || null;
        const jenkinsBuildNum = searchParams.get('jenkinsBuildNum') || null;
        const projectName = searchParams.get('projectName') || null;
        const lacrNo = searchParams.get('lacrNo') || null;
        startLoading('信件寄送中');

        let data = {
            jenkinsJobName,
            jenkinsBuildNum,
            projectName,
            lacrNo,
            senderName: $senderName.val()
        };

        if (document.getElementById('switch_demo').checked) {
            const userData = await getUserData();
            $.extend(data, {}, {
                checkoutDefaultSendTo: userData['mailAccount'],
                checkoutDefaultSendCc: ''
            });
        } else {
            $.extend(data, {}, {
                checkoutDefaultSendTo: $('#checkout-default-send-to').val(),
                checkoutDefaultSendCc: $('#checkout-default-send-cc').val()
            });
        }

        $.ajax({
            type: 'POST',
            url: 'mailHandler/checkout',
            data,
            success: d => {
                showPopup(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done(() => {
            endLoading()
        });
    });

    $('#checksum-send-btn').click(async () => {
        const searchParams = new URLSearchParams(window.location.search);
        const jenkinsJobName = searchParams.get('jenkinsJobName') || null;
        const jenkinsBuildNum = searchParams.get('jenkinsBuildNum') || null;
        const projectName = searchParams.get('projectName') || null;
        const lacrNo = searchParams.get('lacrNo') || null;
        startLoading('信件寄送中');

        let data = {
            jenkinsJobName,
            jenkinsBuildNum,
            projectName,
            lacrNo,
            senderName: $senderName.val()
        };

        if (document.getElementById('switch_demo').checked) {
            const userData = await getUserData();
            $.extend(data, {}, {
                checksumDefaultSendTo: userData['mailAccount'],
                checksumDefaultSendCc: ''
            });
        } else {
            $.extend(data, {}, {
                checksumDefaultSendTo: $('#checksum-default-send-to').val(),
                checksumDefaultSendCc: $('#checksum-default-send-cc').val()
            });
        }

        $.ajax({
            type: 'POST',
            url: 'mailHandler/checksum',
            data,
            success: d => {
                showPopup(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done(() => {
            endLoading();
        });
    });

    $('#save-mail-setting__btn').click(() => {
        const searchParams = new URLSearchParams(window.location.search);
        startLoading('資料儲存中');
        $.ajax({
            type: 'POST',
            url: 'mailHandler/saveMailSetting',
            data: {
                checkinDefaultSendTo: $('#checkin-default-send-to').val(),
                checkinDefaultSendCc: $('#checkin-default-send-cc').val(),
                checksumDefaultSendTo: $('#checksum-default-send-to').val(),
                checksumDefaultSendCc: $('#checksum-default-send-cc').val(),
                checkoutDefaultSendTo: $('#checkout-default-send-to').val(),
                checkoutDefaultSendCc: $('#checkout-default-send-cc').val(),
            },
            success: d => {
                showPopup(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done(() => {
            endLoading();
        });
    });

    $('#logout__btn').click(() => {
        $.ajax({
            url: '/base/invalidateSession',
            success: () => {
                location.reload();
            },
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        });
    });

    $('#switch_demo').click(e => {
        const target = e.currentTarget;
        if (target.checked) {
            $('textarea').attr('disabled', 'disabled');
        } else {
            $('textarea').removeAttr('disabled');
        }
    });

    $('#advance-setting__btn').click(async () => {
        changePageTopIn($('#step1-body'), $('#advance-setting-body'));
        await fillAdvanceSettingValue();
    });

    const $textarea = $('textarea');
    $textarea.attr('spellcheck', 'false');
    $textarea.attr('disabled', 'disabled');
}