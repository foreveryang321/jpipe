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
                var id = json['id'];
                document.getElementById(id).innerHTML = json['html'];
            }
        };
    }(window));
    return JP;
}));