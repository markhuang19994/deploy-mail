function step1(engName) {
    const $senderName = $('#senderName');
    const mailNote = {};

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

        const isDemoMode = document.getElementById('switch_demo').checked;
        const checkinNote = mailNote['checkin'];
        if (isDemoMode) {
            const userData = await getUserData();
            $.extend(data, {}, {
                checkinNote,
                checkinDefaultSendTo: userData['mailAccount'],
                checkinDefaultSendCc: ''
            });
        } else {
            $.extend(data, {}, {
                checkinNote,
                checkinDefaultSendTo: $('#checkin-default-send-to').val(),
                checkinDefaultSendCc: $('#checkin-default-send-cc').val()
            });
        }

        const formData = jsonObjToFormData(data);
        formData.append('changeForm', document.getElementById('change-form-upload').files[0]);
        for (let otherFile of document.getElementById('other-upload').files) {
            formData.append('otherFiles', otherFile);
        }

        startLoading('信件寄送中');
        $.ajax({
            type: 'POST',
            url: 'mailHandler/checkin',
            data: formData,
            processData: false,
            contentType: false,
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done((d) => {
            endLoading();
            showPopup(d);
            if (!isDemoMode) {
                displayNotification({
                    icon: '/image/notification.png',
                    body: `${engName}送出了一封checkin`,
                    image: '',
                    data: {
                        link: `http://macaque:6080/jenkins/view/CITI_UAT/job/Deploy/job/${jenkinsJobName}/${jenkinsBuildNum}/`
                    }
                });
            }
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

        const checkoutNote = mailNote['checkout'];
        if (document.getElementById('switch_demo').checked) {
            const userData = await getUserData();
            $.extend(data, {}, {
                checkoutNote,
                checkoutDefaultSendTo: userData['mailAccount'],
                checkoutDefaultSendCc: ''
            });
        } else {
            $.extend(data, {}, {
                checkoutNote,
                checkoutDefaultSendTo: $('#checkout-default-send-to').val(),
                checkoutDefaultSendCc: $('#checkout-default-send-cc').val()
            });
        }

        const formData = jsonObjToFormData(data);
        formData.append('checkoutForm', document.getElementById('checkout-form-upload').files[0]);

        $.ajax({
            type: 'POST',
            url: 'mailHandler/checkout',
            data: formData,
            processData: false,
            contentType: false,
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done((d) => {
            endLoading();
            showPopup(d);
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

        const checksumNote = mailNote['checksum'];
        if (document.getElementById('switch_demo').checked) {
            const userData = await getUserData();
            $.extend(data, {}, {
                checksumNote,
                checksumDefaultSendTo: userData['mailAccount'],
                checksumDefaultSendCc: ''
            });
        } else {
            $.extend(data, {}, {
                checksumNote,
                checksumDefaultSendTo: $('#checksum-default-send-to').val(),
                checksumDefaultSendCc: $('#checksum-default-send-cc').val()
            });
        }

        $.ajax({
            type: 'POST',
            url: 'mailHandler/checksum',
            data,
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done((d) => {
            endLoading();
            showPopup(d);
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
            error: e => {
                console.log(e['responseJSON']);
                showPopup('出現了錯誤，詳情請看console。');
            }
        }).done((d) => {
            endLoading();
            showPopup(d);
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

    const $mailNoteDiv = $('div.mail-note');
    $mailNoteDiv.slideUp();
    $('.mail-action.note').click(function () {
        window.screenMask(300);
        const mailType = this.id.split('-')[0];
        $mailNoteDiv.slideDown();
        const $textarea = $mailNoteDiv.find('textarea');
        $textarea.prop('disabled', '');
        $textarea.val(`
            |<style>
            |    div.my-note p {
            |        color: red;
            |    }
            |</style>
            |<div class="my-note">
            |    <p>default test line1</p>
            |    <p>default test line2</p>
            |</div>
        `.replace(/\n\s*\|/g, '\n'));

        const $cancelBtn = $mailNoteDiv.find('button.cancel');
        $cancelBtn.click(function () {
            $textarea.val('');
            $(this).unbind();
            $mailNoteDiv.slideUp(() => window.unScreenMask());
        });

        const $confirmBtn = $mailNoteDiv.find('button.confirm');
        $confirmBtn.click(function () {
            mailNote[mailType] = $textarea.val();
            $(this).unbind();
            $mailNoteDiv.slideUp(() => window.unScreenMask());
        });
    });

    $('#subscribe').click(function () {
        showPopup('訂閱後將會收到所有透過此站發送checkin mail的人的通知', () => {
            if ('Notification' in window) {
                console.log('Notification permission default status:', Notification.permission);
                Notification.requestPermission(function (status) {
                    console.log('Notification permission status:', status);
                    displayNotification({
                        body: '謝謝訂閱!'
                    });
                });
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

    $('#custom_upload__btn').click(() => {
        changePageTopIn($('#step1-body'), $('#custom-upload-body'));
    });

    const $textarea = $('textarea');
    $textarea.attr('spellcheck', 'false');
    $textarea.attr('disabled', 'disabled');
}
