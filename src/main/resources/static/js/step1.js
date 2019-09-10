let userMailAccount = '';

function step1(engName) {
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
                const mailAccount = d['mailAccount'];

                if (mailAccount){
                    userMailAccount = mailAccount
                }

                if (engName) {
                    $('#senderName').val(engName);
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
                    $('#senderName').text(d['engName']);
                }
            }
        },
        error: e => {
            console.log(e['responseJSON']);
            alert(e.message);
        }
    });

    $('#checkin-send-btn').click(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const jenkinsJobName = searchParams.get('jenkinsJobName') || null;
        const jenkinsBuildNum = searchParams.get('jenkinsBuildNum') || null;
        const projectName = searchParams.get('projectName') || null;
        const lacrNo = searchParams.get('lacrNo') || null;
        const senderName = searchParams.get('senderName') || null;
        startLoading('信件寄送中');
        $.ajax({
            type: 'POST',
            url: 'mailHandler/checkin',
            data: {
                jenkinsJobName,
                jenkinsBuildNum,
                projectName,
                lacrNo,
                senderName: $('#senderName').val(),
                checkinDefaultSendTo: $('#checkin-default-send-to').val(),
                checkinDefaultSendCc: $('#checkin-default-send-cc').val()
            },
            success: d => {
                alert(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                alert(e.message);
            }
        }).done(() => {
            endLoading()
        });
    });

    $('#checkout-send-btn').click(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const jenkinsJobName = searchParams.get('jenkinsJobName') || null;
        const jenkinsBuildNum = searchParams.get('jenkinsBuildNum') || null;
        const projectName = searchParams.get('projectName') || null;
        const lacrNo = searchParams.get('lacrNo') || null;
        startLoading('信件寄送中');
        $.ajax({
            type: 'POST',
            url: 'mailHandler/checkout',
            data: {
                jenkinsJobName,
                jenkinsBuildNum,
                projectName,
                lacrNo,
                senderName: $('#senderName').val(),
                checkoutDefaultSendTo: $('#checkout-default-send-to').val(),
                checkoutDefaultSendCc: $('#checkout-default-send-cc').val()
            },
            success: d => {
                alert(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                alert(e.message);
            }
        }).done(() => {
            endLoading()
        });
    });

    $('#checksum-send-btn').click(() => {
        const searchParams = new URLSearchParams(window.location.search);
        const jenkinsJobName = searchParams.get('jenkinsJobName') || null;
        const jenkinsBuildNum = searchParams.get('jenkinsBuildNum') || null;
        const projectName = searchParams.get('projectName') || null;
        const lacrNo = searchParams.get('lacrNo') || null;
        startLoading('信件寄送中');
        $.ajax({
            type: 'POST',
            url: 'mailHandler/checksum',
            data: {
                jenkinsJobName,
                jenkinsBuildNum,
                projectName,
                lacrNo,
                senderName: $('#senderName').val(),
                checksumDefaultSendTo: $('#checksum-default-send-to').val(),
                checksumDefaultSendCc: $('#checksum-default-send-cc').val()
            },
            success: d => {
                alert(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                alert(e.message);
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
                alert(d);
            },
            error: e => {
                console.log(e['responseJSON']);
                alert(e.message);
            }
        }).done(() => {
            endLoading();
        });
    });

    $('#logout__btn').click(() => {
        $.ajax({
            url: '/base/invalidateSession',
            success: () => {
                changePageLeftIn($('#step1-body'), $('#index-body'));
            },
            error: e => {
                console.log(e['responseJSON']);
                alert(e.message);
            }
        });
    });

    $('#advance-setting__btn').click(() => {
        changePageTopIn($('#step1-body'), $('#advance-setting-body'));
        fillAdvanceSettingValue();
    });

    $('textarea').attr('spellcheck', 'false');
}