$('#custom-upload-cancel-btn').click(() => {
    document.getElementById('checkout-form-upload').value = '';
    document.getElementById('change-form-upload').value = '';
    document.getElementById('other-upload').value = '';
    changePageBottomIn($('#custom-upload-body'), $('#step1-body'));
});

$('#custom-upload-save-btn').click(() => {
    changePageBottomIn($('#custom-upload-body'), $('#step1-body'));
});
