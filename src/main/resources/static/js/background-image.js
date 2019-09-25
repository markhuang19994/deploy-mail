$(function () {
    const body = document.body;

    $("<img/>").attr('src', 'base/background/image')
        .on('load', function () {
                if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth === 0) {
                    console.log('broken image!');
                } else {
                    Object.assign(body.style, {
                        backgroundImage: 'url(base/background/image)',
                        backgroundClip: 'padding-box',
                        backgroundSize: 'cover',
                        zIndex: '-10'
                    });
                }
            }
        );
});