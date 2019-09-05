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
        step1();
    });
}

$(() => {
    const searchParams = new URLSearchParams(window.location.search);
    const page = searchParams.get('page') || 'index-body';
    $('#' + page).css('display', 'block');
    index();
});