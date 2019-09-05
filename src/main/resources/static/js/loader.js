((self) => {
    function Loader() {
        this.doc = self.document;
        this.head = this.doc.head;
        this.sheets = this.doc.styleSheets;
        this.timeout = 60000 * 10;//ms * gap
    }

    const LOADER = new Loader();

    /**
     * load css and resolve promise when loaded
     */
    let _loadCss = function (href) {
        return new Promise((res, rej) => {
            let link = this.doc.createElement('link');
            link.rel = 'stylesheet';
            link.href = href;
            let count = 0;

            function checkCssLoaded() {
                if (++count >= timeout) rej(new Error(`style ${href} time out`));
                for (let i = 0; i < this.sheets.length; ++i) {
                    if (sheets[i].href && new RegExp(href).test(sheets[i].href)) {
                        res();
                        return true;
                    }
                }
                setTimeout(checkCssLoaded, 10);
            }

            checkCssLoaded();
            this.head.appendChild(link);
        });
    }.bind(LOADER);

    /**
     * load script and ordered execution
     */
    let _loadScript = function (src, isAsync = false) {
        let script = this.doc.createElement('script');
        script.type = 'text/javascript';
        script.async = isAsync;
        script.src = src;
        this.head.appendChild(script);
    }.bind(LOADER);

    /**
     * load script and ordered execution
     */
    let _loadScripts = function (srcs, isAsync = false) {
        srcs.forEach(src => _loadScript(src, isAsync));
    }.bind(LOADER);

    /**
     * load image and resolve promise when loaded
     */
    let _loadImage = function (src) {
        return new Promise((res, rej) => {
            let img = new Image();
            img.src = src;
            let count = 0;

            function checkImageLoaded() {
                if (++count >= timeout) rej(new Error(`load image ${src} time out`));
                if (img.complete) res(img);
                else setTimeout(checkImageLoaded, 10);
            }

            checkImageLoaded();
        });
    }.bind(LOADER);

    self.Loader = LOADER;
    Loader.prototype.loadCss = _loadCss;
    Loader.prototype.loadScript = _loadScript;
    Loader.prototype.loadScripts = _loadScripts;
    Loader.prototype.loadImage = _loadImage;
})(window);