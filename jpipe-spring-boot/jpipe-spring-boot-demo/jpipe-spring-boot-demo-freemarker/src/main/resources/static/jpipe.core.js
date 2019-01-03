;(function (root, factory) {
    if (typeof exports === 'object') {
        module.exports = exports = factory();
    } else if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else {
        root.JP = factory();
    }
}(this, function () {
    var JP = JP || (function (window) {
        return {
            view: function (json) {
                var code = json['code'],
                    message = json['message'],
                    bn = json['bn'],
                    domId = json['domId'],
                    templateId = json['templateId'],
                    data = json['data'];

                document.getElementById(domId).innerHTML =
                    '<p>code: ' + code
                    + '<br/>message: ' + message
                    + '<br/>bn: ' + bn
                    + '<br/>domId: ' + domId
                    + '<br/>templateId: ' + templateId
                    + '<br/>data: ' + JSON.stringify(data)
                    + '</p>';
            }
        };
    }(window));
    return JP;
}));