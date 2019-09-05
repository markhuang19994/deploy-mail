function index() {
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
            alert(e.message);
        }
    });

    $('#goStep1').click(() => {
        changePage($('#index-body'), $('#step1-body'));
        step1($('#sel option:selected').text());
    });
}

$(() => {
    const searchParams = new URLSearchParams(window.location.search);
    index();
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
            alert(e.message);
        }
    });
});