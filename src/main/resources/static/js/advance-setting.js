let isPasswordTrigger = false;

function fillAdvanceSettingValue() {
    isPasswordTrigger = false
    document.getElementById('setting-mail-account').value = userMailAccount;
    document.getElementById('setting-mail-password').value = '********';
}

$('#setting-mail-password').click(e => {
    if (!isPasswordTrigger) {
        e.currentTarget.value = '';
    }
    isPasswordTrigger = true;
});

$('#advance-setting-save-btn').click(() => {
    const account = document.getElementById('setting-mail-account');
    const pwd = document.getElementById('setting-mail-password');

    $.ajax({
        type: 'POST',
        url: 'mailHandler/saveAdvanceMailSetting',
        data: {
            account: account.value,
            pwd: !isPasswordTrigger ? '' : pwd.value
        },
        success: (d) => alert(d),
        error: e => {
            console.log(e['responseJSON']);
            alert(e.message);
        },
        done: () => {
            changePageBottomIn($('#advance-setting-body'), $('#step1-body'));
        }
    });
});

$('#advance-setting-cancel-btn').click(() => {
    changePageBottomIn($('#advance-setting-body'), $('#step1-body'));
});