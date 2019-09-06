function fillAdvanceSettingValue() {
    document.getElementById('setting-mail-account').value = userMailAccount;
    document.getElementById('setting-mail-password').value = '********';
}

let isPasswordTrigger = false;
$('#setting-mail-password').click(e => {
    if (!isPasswordTrigger) {
        e.currentTarget.value = '';
    }
    isPasswordTrigger = true;
});

$('#advance-setting-save-btn').click(() => {
    const account = document.getElementById('setting-mail-account');
    const pwd = document.getElementById('setting-mail-password');
    changePageBottomIn($('#advance-setting-body'), $('#step1-body'));
    $.ajax({
        url: '',
        data: {
            account: account.value,
            pwd: !isPasswordTrigger ? '' : pwd.value
        }, success: () => {
        }
        , error: () => {
        }
    });
});

$('#advance-setting-cancel-btn').click(() => {
    changePageBottomIn($('#advance-setting-body'), $('#step1-body'));
});