async function fillAdvanceSettingValue() {
    const userData = await getUserData();
    document.getElementById('setting-mail-account').value = userData['mailAccount'] || '';
    document.getElementById('setting-mail-account-alias').value = userData['mailAccountAlias'] || '';
    document.getElementById('setting-mail-password').value = '********';
}

$('#setting-mail-password').click(e => {
    e.currentTarget.value = '';
});

$('#advance-setting-save-btn').click(async () => {
    const account = document.getElementById('setting-mail-account');
    const accountAlias = document.getElementById('setting-mail-account-alias');
    const pwd = document.getElementById('setting-mail-password');
    const d = await new Promise(res => {
        $.ajax({
            type: 'POST',
            url: 'mailHandler/saveAdvanceMailSetting',
            data: {
                account: account.value,
                accountAlias: accountAlias.value,
                pwd: pwd.value === '********' ? '' : pwd.value
            },
            success: (d) => {
                res(d)
            },
            error: e => {
                console.log(e['responseJSON']);
                res(e);
            }
        });
    });
    changePageBottomIn($('#advance-setting-body'), $('#step1-body'));
    setTimeout(() => showPopup(d), 500)
});

$('#advance-setting-back-btn').click(() => {
    changePageBottomIn($('#advance-setting-body'), $('#step1-body'));
});
